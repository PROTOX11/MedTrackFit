import React, { useState, useEffect, useRef } from 'react'

export default function Chat({ user }) {
  const [activeTab, setActiveTab] = useState('direct') // direct, group
  const [contacts, setContacts] = useState([])
  const [selectedContact, setSelectedContact] = useState(null)
  const [messages, setMessages] = useState([])
  const [inputVal, setInputVal] = useState('')
  const [loadingHistory, setLoadingHistory] = useState(false)
  const pollInterval = useRef(null)

  // Appointment modal state
  const [aptModalOpen, setAptModalOpen] = useState(false)
  const [aptData, setAptData] = useState({ title: '', date: '', time: '' })

  const isDoctor = user?.role?.toUpperCase() === 'DOCTOR'

  // 1. Fetch connected contacts
  useEffect(() => {
    fetch('/api/connections')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          const connectedOnly = data.filter(c => c.connected)
          setContacts(connectedOnly)
          
          // Check if there is a redirection request in localStorage
          const targetEmail = localStorage.getItem('active_chat_recipient')
          if (targetEmail) {
            localStorage.removeItem('active_chat_recipient')
            const targetContact = connectedOnly.find(c => c.email === targetEmail)
            if (targetContact) {
              setSelectedContact(targetContact)
              setActiveTab('direct')
              return
            }
          }

          // Select first contact by default if in direct tab
          if (connectedOnly.length > 0 && activeTab === 'direct' && !selectedContact) {
            setSelectedContact(connectedOnly[0])
          }
        }
      })
      .catch(err => console.error('Error fetching contacts:', err))
  }, [activeTab])

  // 2. Select default channel based on tab
  useEffect(() => {
    if (activeTab === 'group') {
      setSelectedContact({
        id: 'group_community',
        name: 'Community Group Chat',
        email: 'Community Group Chat',
        role: 'Group',
        status: 'General Discussion'
      })
    } else if (activeTab === 'direct') {
      if (contacts.length > 0) {
        setSelectedContact(contacts[0])
      } else {
        setSelectedContact(null)
      }
    }
  }, [activeTab])

  // 3. Load chat history with polling
  const loadHistory = (recipientEmail) => {
    if (!recipientEmail) return
    setLoadingHistory(true)
    fetch(`/api/chat/history?recipient=${encodeURIComponent(recipientEmail)}`)
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          const formatted = data.map(msg => ({
            text: msg.text,
            sender: msg.senderEmail === user.email ? 'me' : 'them',
            author: msg.senderName,
            time: new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
          }))
          setMessages(formatted)
        }
        setLoadingHistory(false)
      })
      .catch(err => {
        console.error('Error fetching chat history:', err)
        setLoadingHistory(false)
      })
  }

  // Setup/Tear down polling
  useEffect(() => {
    if (pollInterval.current) clearInterval(pollInterval.current)
    
    if (selectedContact) {
      loadHistory(selectedContact.email)
      
      pollInterval.current = setInterval(() => {
        fetch(`/api/chat/history?recipient=${encodeURIComponent(selectedContact.email)}`)
          .then(res => res.json())
          .then(data => {
            if (Array.isArray(data)) {
              const formatted = data.map(msg => ({
                text: msg.text,
                sender: msg.senderEmail === user.email ? 'me' : 'them',
                author: msg.senderName,
                time: new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
              }))
              setMessages(formatted)
            }
          })
          .catch(err => console.error('Polling error:', err))
      }, 3000)
    }

    return () => {
      if (pollInterval.current) clearInterval(pollInterval.current)
    }
  }, [selectedContact])

  const handleSendMessage = (e) => {
    e.preventDefault()
    if (!inputVal.trim() || !selectedContact) return

    const messageText = inputVal
    setInputVal('')

    fetch('/api/chat/send', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        recipientEmail: selectedContact.email,
        text: messageText
      })
    })
      .then(res => res.json())
      .then(data => {
        if (data && data.id) {
          // Immediately append sent message to UI
          setMessages(prev => [
            ...prev,
            {
              text: messageText,
              sender: 'me',
              author: user.name,
              time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
            }
          ])
        }
      })
      .catch(err => console.error('Error sending message:', err))
  }

  const handleCreateAppointment = (e) => {
    e.preventDefault()
    if (!aptData.title || !aptData.date || !aptData.time || !selectedContact) return

    fetch('/api/appointments/create', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        patientId: selectedContact.id,
        patientName: selectedContact.name,
        title: aptData.title,
        date: aptData.date,
        time: aptData.time
      })
    })
      .then(res => res.json())
      .then(data => {
        if (data && data.id) {
          // Send automatic chat notification to the patient about the meeting
          fetch('/api/chat/send', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              recipientEmail: selectedContact.email,
              text: `📅 Scheduled appointment: "${aptData.title}" on ${aptData.date} at ${aptData.time}`
            })
          })
          
          setAptData({ title: '', date: '', time: '' })
          setAptModalOpen(false)
          loadHistory(selectedContact.email)
        }
      })
      .catch(err => console.error(err))
  }

  return (
    <div className="h-[calc(100vh-8.5rem)] flex rounded-3xl overflow-hidden border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 shadow-sm animate-scale-in">
      {/* Sidebar List */}
      <div className="w-80 border-r border-slate-200 dark:border-slate-800 flex flex-col bg-slate-50/50 dark:bg-slate-900/30">
        <div className="p-4 border-b border-slate-200 dark:border-slate-800 space-y-3">
          <h2 className="text-lg font-bold text-slate-800 dark:text-white">Messages</h2>
          <div className="flex bg-slate-100 dark:bg-slate-800 rounded-xl p-1 text-xs font-bold">
            <button 
              onClick={() => setActiveTab('direct')}
              className={`flex-1 py-2 rounded-lg transition-colors ${activeTab === 'direct' ? 'bg-white dark:bg-slate-700 text-blue-600 dark:text-blue-400 shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
            >
              Direct
            </button>
            <button 
              onClick={() => setActiveTab('group')}
              className={`flex-1 py-2 rounded-lg transition-colors ${activeTab === 'group' ? 'bg-white dark:bg-slate-700 text-blue-600 dark:text-blue-400 shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
            >
              Group
            </button>
          </div>
        </div>

        {/* Contacts Lists */}
        <div className="flex-1 overflow-y-auto p-2 space-y-1">
          {activeTab === 'direct' ? (
            contacts.map((chan) => (
              <button
                key={chan.id}
                onClick={() => setSelectedContact(chan)}
                className={`w-full text-left px-3 py-3 rounded-2xl flex items-center gap-3 transition-colors ${
                  selectedContact?.id === chan.id 
                    ? 'bg-blue-50 dark:bg-blue-950/40 border border-blue-100 dark:border-blue-900/40 text-blue-600 dark:text-blue-400' 
                    : 'hover:bg-slate-100/70 dark:hover:bg-slate-800/40 text-slate-700 dark:text-slate-300'
                }`}
              >
                <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-blue-500 to-indigo-600 flex items-center justify-center text-white font-extrabold text-sm shadow-sm">
                  {chan.name.charAt(0)}
                </div>
                <div className="min-w-0 flex-1">
                  <p className="text-xs font-bold truncate">{chan.name}</p>
                  <p className="text-[10px] text-slate-400 truncate mt-0.5">{chan.status || chan.role}</p>
                </div>
              </button>
            ))
          ) : (
            <button
              onClick={() => setSelectedContact({ id: 'group_community', name: 'Community Group Chat', email: 'Community Group Chat', role: 'Group' })}
              className="w-full text-left px-3 py-3 rounded-2xl flex items-center gap-3 bg-blue-50 dark:bg-blue-950/40 border border-blue-100 dark:border-blue-900/40 text-blue-600 dark:text-blue-400"
            >
              <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-blue-500 to-indigo-600 flex items-center justify-center text-white font-extrabold text-sm shadow-sm">
                C
              </div>
              <div className="min-w-0 flex-1">
                <p className="text-xs font-bold truncate">Community Group Chat</p>
                <p className="text-[10px] text-slate-400 truncate mt-0.5">Public discussion room</p>
              </div>
            </button>
          )}

          {activeTab === 'direct' && contacts.length === 0 && (
            <div className="py-8 text-center text-slate-400 text-xs">
              <i className="fa-solid fa-user-slash block text-lg mb-1"></i>
              No connected contacts.<br/>Use Patients Directory to connect.
            </div>
          )}
        </div>
      </div>

      {/* Main Chat Box */}
      {selectedContact ? (
        <div className="flex-1 flex flex-col bg-white dark:bg-slate-900">
          {/* Chat Header */}
          <div className="h-16 px-6 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-blue-500 to-indigo-600 flex items-center justify-center text-white font-bold text-xs">
                {selectedContact.name.charAt(0)}
              </div>
              <div>
                <p className="text-xs font-bold text-slate-800 dark:text-white">{selectedContact.name}</p>
                <p className="text-[9px] text-green-500 font-semibold flex items-center gap-1">
                  <span className="w-1.5 h-1.5 rounded-full bg-green-500 animate-pulse"></span>
                  Active now
                </p>
              </div>
            </div>
            <div className="flex items-center gap-3 text-slate-400">
              {isDoctor && activeTab === 'direct' && (
                <button 
                  onClick={() => setAptModalOpen(true)}
                  className="px-3.5 py-1.5 bg-blue-50 hover:bg-blue-100 dark:bg-blue-950/40 text-blue-600 dark:text-blue-400 border border-blue-100/50 dark:border-blue-900/30 rounded-xl text-[10px] font-bold flex items-center gap-1 transition-all"
                >
                  <i className="fa-solid fa-calendar-plus"></i>
                  <span>Appoint Meeting</span>
                </button>
              )}
            </div>
          </div>

          {/* Message Thread */}
          <div className="flex-1 overflow-y-auto p-6 space-y-4 bg-slate-50/20 dark:bg-slate-900/10">
            {loadingHistory ? (
              <div className="flex h-full items-center justify-center text-xs text-slate-400">
                <i className="fa-solid fa-circle-notch fa-spin mr-1.5"></i> Loading chat...
              </div>
            ) : (
              messages.map((msg, index) => (
                <div 
                  key={index} 
                  className={`flex items-end gap-2.5 ${msg.sender === 'me' ? 'justify-end' : 'justify-start'}`}
                >
                  {msg.sender !== 'me' && (
                    <div className="w-8 h-8 rounded-full bg-slate-200 dark:bg-slate-800 flex items-center justify-center text-xs text-slate-600 dark:text-slate-300 font-bold">
                      {msg.author ? msg.author.charAt(0) : selectedContact.name.charAt(0)}
                    </div>
                  )}
                  <div className="flex flex-col max-w-[65%]">
                    {activeTab === 'group' && msg.sender !== 'me' && (
                      <span className="text-[10px] text-slate-400 font-bold mb-1 ml-1">{msg.author}</span>
                    )}
                    <div 
                      className={`px-4 py-2.5 rounded-2xl text-xs font-medium leading-relaxed shadow-sm ${
                        msg.sender === 'me'
                          ? 'bg-blue-600 text-white rounded-br-none'
                          : 'bg-slate-100 dark:bg-slate-800 text-slate-800 dark:text-slate-200 rounded-bl-none'
                      }`}
                    >
                      {msg.text}
                    </div>
                    <span className={`text-[9px] text-slate-400 mt-1 ${msg.sender === 'me' ? 'text-right mr-1' : 'ml-1'}`}>{msg.time}</span>
                  </div>
                </div>
              ))
            )}
            {!loadingHistory && messages.length === 0 && (
              <div className="flex h-full items-center justify-center text-xs text-slate-400">
                Start of conversation history.
              </div>
            )}
          </div>

          {/* Chat Input form */}
          <form onSubmit={handleSendMessage} className="p-4 border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 flex items-center gap-3">
            <input
              type="text"
              value={inputVal}
              onChange={(e) => setInputVal(e.target.value)}
              placeholder="Type your message here..."
              className="flex-1 px-4 py-3 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white dark:focus:bg-slate-900 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400"
            />
            <button 
              type="submit"
              className="px-5 py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-xs transition-colors shadow-sm flex items-center gap-1.5"
            >
              <span>Send</span>
              <i className="fa-solid fa-paper-plane"></i>
            </button>
          </form>
        </div>
      ) : (
        <div className="flex-1 flex flex-col items-center justify-center bg-slate-50/20 dark:bg-slate-900/10 text-slate-400">
          <i className="fa-solid fa-comments text-4xl mb-3"></i>
          <p className="text-xs">Select a contact to start chatting</p>
        </div>
      )}

      {/* APPOINTMENT MODAL */}
      {aptModalOpen && (
        <div className="fixed inset-0 bg-slate-950/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl w-full max-w-sm shadow-xl animate-scale-in overflow-hidden">
            <div className="p-5 border-b border-slate-100 dark:border-slate-800 flex justify-between items-center bg-slate-50/50 dark:bg-slate-900/50">
              <h3 className="text-sm font-extrabold text-slate-800 dark:text-white flex items-center gap-1.5">
                <i className="fa-solid fa-calendar-check text-blue-600"></i>
                <span>Schedule Appointment</span>
              </h3>
              <button onClick={() => setAptModalOpen(false)} className="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200">
                <i className="fa-solid fa-xmark text-lg"></i>
              </button>
            </div>

            <form onSubmit={handleCreateAppointment} className="p-5 space-y-4">
              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">Meeting Title</label>
                <input
                  type="text"
                  required
                  value={aptData.title}
                  onChange={(e) => setAptData({ ...aptData, title: e.target.value })}
                  placeholder="e.g. Consultation or Checkup"
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white outline-none focus:ring-2 focus:ring-blue-500/15"
                />
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">Date</label>
                <input
                  type="date"
                  required
                  value={aptData.date}
                  onChange={(e) => setAptData({ ...aptData, date: e.target.value })}
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white outline-none focus:ring-2 focus:ring-blue-500/15"
                />
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">Time Slot</label>
                <input
                  type="text"
                  required
                  value={aptData.time}
                  onChange={(e) => setAptData({ ...aptData, time: e.target.value })}
                  placeholder="e.g. 11:30 AM - 12:00 PM"
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white outline-none focus:ring-2 focus:ring-blue-500/15"
                />
              </div>

              <div className="pt-2 flex justify-end gap-3">
                <button 
                  type="button" 
                  onClick={() => setAptModalOpen(false)}
                  className="px-4 py-2 border border-slate-200 dark:border-slate-700 text-slate-500 hover:bg-slate-50 dark:hover:bg-slate-800 rounded-xl text-xs font-bold transition-colors"
                >
                  Cancel
                </button>
                <button 
                  type="submit"
                  className="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold transition-colors shadow-sm"
                >
                  Confirm Schedule
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
