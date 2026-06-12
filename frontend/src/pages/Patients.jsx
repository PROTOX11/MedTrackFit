import React, { useState, useEffect } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { Search, Shield, Inbox, CheckCircle, ArrowRight, Heart } from 'lucide-react'

export default function Patients({ user }) {
  const [searchQuery, setSearchQuery] = useState('')
  const [patientsList, setPatientsList] = useState([])
  const [loading, setLoading] = useState(true)

  const loadConnections = () => {
    setLoading(true)
    fetch('/api/connections')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          setPatientsList(data)
        }
        setLoading(false)
      })
      .catch(err => {
        console.error('Error loading connections:', err)
        setLoading(false)
      })
  }

  useEffect(() => {
    loadConnections()
  }, [])

  const toggleConnection = (targetId) => {
    fetch('/api/connections/toggle', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ targetId })
    })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          loadConnections()
        }
      })
      .catch(err => console.error('Error toggling connection:', err))
  }

  const filteredPatients = patientsList.filter(p => 
    p.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    (p.status && p.status.toLowerCase().includes(searchQuery.toLowerCase())) ||
    p.role.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.05 } }
  }

  const rowVariants = {
    hidden: { opacity: 0, x: -10 },
    visible: { opacity: 1, x: 0, transition: { type: 'spring', stiffness: 100, damping: 15 } }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-slate-800 dark:text-white">Directory Portal</h1>
          <p className="text-xs text-slate-500 dark:text-slate-400">Search and build connections with health practitioners, mentors, and recovery peers.</p>
        </div>

        {/* Search Input */}
        <div className="relative w-full sm:w-80">
          <Search className="absolute inset-y-0 left-3.5 my-auto w-4 h-4 text-slate-400" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search directory..."
            className="w-full pl-10 pr-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-white dark:bg-slate-900 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all placeholder-slate-400"
          />
        </div>
      </div>

      {loading ? (
        <div className="space-y-4">
          <div className="h-10 w-full bg-slate-200/50 dark:bg-slate-800 animate-pulse rounded-xl" />
          <div className="space-y-2">
            {[...Array(6)].map((_, i) => (
              <div key={i} className="h-14 w-full bg-slate-200/40 dark:bg-slate-800/80 animate-shimmer relative overflow-hidden rounded-xl" />
            ))}
          </div>
        </div>
      ) : (
        /* Directory Table */
        <motion.div 
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-white dark:bg-slate-900 border border-slate-200/60 dark:border-slate-800/80 rounded-2xl overflow-hidden shadow-sm"
        >
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/50 text-[10px] font-bold uppercase tracking-wider text-slate-400">
                  <th className="py-4 px-6">Name</th>
                  <th className="py-4 px-6">Role</th>
                  <th className="py-4 px-6">Status / Track</th>
                  <th className="py-4 px-6 text-center">Status</th>
                  <th className="py-4 px-6 text-right">Action</th>
                </tr>
              </thead>
              <motion.tbody 
                variants={containerVariants}
                initial="hidden"
                animate="visible"
                className="divide-y divide-slate-100 dark:divide-slate-800/80"
              >
                {filteredPatients.map((patient) => (
                  <motion.tr 
                    key={patient.id} 
                    variants={rowVariants}
                    className="hover:bg-slate-50/40 dark:hover:bg-slate-850/20 transition-colors text-xs"
                  >
                    {/* Name & Email */}
                    <td className="py-4 px-6">
                      <div className="flex items-center gap-3">
                        <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center text-white font-extrabold text-sm shadow-sm">
                          {patient.name.charAt(0)}
                        </div>
                        <div>
                          <p className="font-bold text-slate-850 dark:text-slate-100">{patient.name}</p>
                          <p className="text-[10px] text-slate-400 font-medium mt-0.5">{patient.email}</p>
                        </div>
                      </div>
                    </td>

                    {/* Role */}
                    <td className="py-4 px-6 font-semibold text-slate-700 dark:text-slate-300">
                      {patient.role}
                    </td>

                    {/* Program Profile */}
                    <td className="py-4 px-6">
                      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-bold bg-indigo-500/10 text-indigo-600 dark:text-indigo-400 border border-indigo-500/10">
                        <Heart className="w-3 h-3" />
                        {patient.status || 'General Wellness'}
                      </span>
                    </td>

                    {/* Status Indicator */}
                    <td className="py-4 px-6 text-center">
                      <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-green-500/10 text-green-600 dark:text-green-400">
                        <span className="w-1.5 h-1.5 rounded-full bg-green-500 animate-pulse"></span>
                        Online
                      </span>
                    </td>

                    {/* Action buttons */}
                    <td className="py-4 px-6 text-right">
                      <button 
                        onClick={() => toggleConnection(patient.id)}
                        className={`px-3 py-1.5 rounded-xl font-bold text-[10px] transition-all border ${
                          patient.connected
                            ? 'bg-slate-100 dark:bg-slate-800 border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400'
                            : 'bg-blue-600 hover:bg-blue-700 text-white border-transparent shadow-sm'
                        }`}
                      >
                        {patient.connected ? 'Disconnect' : 'Connect'}
                      </button>
                    </td>
                  </motion.tr>
                ))}
                {filteredPatients.length === 0 && (
                  <tr>
                    <td colSpan="5" className="py-12 text-center text-slate-400">
                      <Inbox className="w-6 h-6 mx-auto mb-2 text-slate-400" />
                      <span>No matches found in directory.</span>
                    </td>
                  </tr>
                )}
              </motion.tbody>
            </table>
          </div>
        </motion.div>
      )}
    </div>
  )
}
