import React, { useState, useRef } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { Camera, CheckCircle, Edit, Save, X, Info } from 'lucide-react'

export default function Profile({ user, onUpdateUser }) {
  const fileInputRef = useRef(null)
  const [editing, setEditing] = useState(false)
  const [formData, setFormData] = useState({
    name: user?.name || '',
    email: user?.email || '',
    phoneNumber: user?.phoneNumber || '',
    about: user?.about || '',
    role: user?.role || 'Patient'
  })
  const [notification, setNotification] = useState('')

  const handleSave = (e) => {
    e.preventDefault()
    setEditing(false)
    if (onUpdateUser) {
      onUpdateUser(formData)
    }
    setNotification('Profile details updated successfully!')
    setTimeout(() => setNotification(''), 4000)
  }

  const handleUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click()
    }
  }

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (!file) return

    const data = new FormData()
    data.append('file', file)

    fetch('/api/profile/upload-photo', {
      method: 'POST',
      body: data
    })
      .then(res => res.json())
      .then(data => {
        if (data.success && data.profilePicture) {
          if (onUpdateUser) {
            onUpdateUser({ profilePicture: data.profilePicture })
          }
          setNotification('Profile picture updated successfully!')
          setTimeout(() => setNotification(''), 4000)
        } else {
          setNotification(data.message || 'Failed to upload image')
          setTimeout(() => setNotification(''), 4000)
        }
      })
      .catch(err => {
        console.error(err)
        setNotification('Error uploading file')
        setTimeout(() => setNotification(''), 4000)
      })
  }

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <div className="flex flex-col gap-1.5">
        <h1 className="text-2xl font-bold tracking-tight text-slate-800 dark:text-white">Profile Portal</h1>
        <p className="text-xs text-slate-500 dark:text-slate-400">View and update your health credentials and custom bio details.</p>
      </div>

      <AnimatePresence>
        {notification && (
          <motion.div 
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            className="p-4 rounded-xl bg-green-500/10 border border-green-500/20 text-green-600 dark:text-green-400 text-xs font-semibold flex items-center gap-2"
          >
            <CheckCircle className="w-4 h-4" />
            <span>{notification}</span>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Main card */}
      <motion.div 
        initial={{ opacity: 0, y: 15 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-white dark:bg-slate-900 border border-slate-200/60 dark:border-slate-800/80 rounded-3xl p-6 shadow-sm flex flex-col md:flex-row gap-6 relative overflow-hidden"
      >
        {/* Profile Pic Column */}
        <div className="flex flex-col items-center gap-3.5 w-full md:w-48 shrink-0">
          <img 
            src={user?.profilePicture || 'https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o='} 
            alt="Profile Avatar" 
            className="w-32 h-32 rounded-3xl object-cover border-4 border-slate-100 dark:border-slate-800/80 shadow-md"
          />
          <input 
            type="file" 
            ref={fileInputRef} 
            onChange={handleFileChange} 
            accept="image/*" 
            className="hidden" 
          />
          <button 
            type="button"
            onClick={handleUploadClick}
            className="inline-flex items-center gap-1.5 px-4 py-2 border border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800 rounded-xl text-[10px] font-bold text-slate-600 dark:text-slate-400 transition-colors"
          >
            <Camera className="w-3.5 h-3.5" />
            <span>Upload Photo</span>
          </button>
        </div>

        {/* Details Form */}
        <div className="flex-1 space-y-4">
          <div className="flex justify-between items-start gap-4">
            <div>
              <h2 className="text-lg font-bold text-slate-800 dark:text-white">{formData.name}</h2>
              <span className="inline-block mt-1 text-[9px] font-extrabold uppercase tracking-wider bg-blue-500/10 text-blue-600 dark:text-blue-400 px-2.5 py-0.5 rounded-full">
                {formData.role}
              </span>
            </div>
            {!editing && (
              <button 
                onClick={() => setEditing(true)}
                className="inline-flex items-center gap-1.5 px-4 py-2 bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 rounded-xl text-xs font-bold transition-all"
              >
                <Edit className="w-3.5 h-3.5" />
                <span>Edit Details</span>
              </button>
            )}
          </div>

          <form onSubmit={handleSave} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">Full Name</label>
                <input
                  type="text"
                  required
                  disabled={!editing}
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 disabled:opacity-75 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 font-bold"
                />
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">Email Address</label>
                <input
                  type="email"
                  required
                  disabled
                  value={formData.email}
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 disabled:opacity-75 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 font-bold"
                />
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">Phone Number</label>
                <input
                  type="tel"
                  disabled={!editing}
                  value={formData.phoneNumber}
                  onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 disabled:opacity-75 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 font-bold"
                />
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">User Role</label>
                <input
                  type="text"
                  disabled
                  value={formData.role}
                  className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 disabled:opacity-75 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 font-bold"
                />
              </div>
            </div>

            <div>
              <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1.5">About / Bio</label>
              <textarea
                rows="4"
                disabled={!editing}
                value={formData.about}
                onChange={(e) => setFormData({ ...formData, about: e.target.value })}
                className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 disabled:opacity-75 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 resize-none font-medium"
              ></textarea>
            </div>

            <AnimatePresence>
              {editing && (
                <motion.div 
                  initial={{ opacity: 0, height: 0 }}
                  animate={{ opacity: 1, height: 'auto' }}
                  exit={{ opacity: 0, height: 0 }}
                  className="pt-2 flex justify-end gap-3 overflow-hidden"
                >
                  <button 
                    type="button" 
                    onClick={() => { setEditing(false); setFormData({ ...user }); }}
                    className="inline-flex items-center gap-1 px-4 py-2 border border-slate-200 dark:border-slate-700 text-slate-500 hover:bg-slate-50 dark:hover:bg-slate-800 rounded-xl text-xs font-bold transition-all"
                  >
                    <X className="w-3.5 h-3.5" />
                    <span>Cancel</span>
                  </button>
                  <button 
                    type="submit"
                    className="inline-flex items-center gap-1.5 px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold transition-all shadow-sm"
                  >
                    <Save className="w-3.5 h-3.5" />
                    <span>Save Changes</span>
                  </button>
                </motion.div>
              )}
            </AnimatePresence>
          </form>
        </div>
      </motion.div>
    </div>
  )
}
