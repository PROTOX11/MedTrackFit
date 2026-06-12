import React, { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import { Users, Activity, CheckCircle, Award, MessageSquare, Plus, Play, Pause, Flame, Heart, BookOpen, Clock } from 'lucide-react'
import CountUp from '../components/CountUp'

export default function Dashboard({ user }) {
  const navigate = useNavigate()
  const [stats, setStats] = useState({
    hydration: 0,
    calories: 0,
    activePatients: 18,
    blogsWritten: 7,
    consultations: 5,
    meditationMinutes: 0,
  })
  
  const [contacts, setContacts] = useState([])
  const [appointments, setAppointments] = useState([])
  const [loading, setLoading] = useState(false)

  // Meditation timer state
  const [medSeconds, setMedSeconds] = useState(0)
  const [sessionSeconds, setSessionSeconds] = useState(0)
  const [timerRunning, setTimerRunning] = useState(false)
  const [meditationType, setMeditationType] = useState('long')
  const timerInterval = useRef(null)

  const role = user?.role?.toUpperCase() || 'USER'
  const isPatient = role === 'SUFFERINGPATIENT' || role === 'USER'

  const getRolePrefix = (roleName) => {
    switch (roleName?.toUpperCase()) {
      case 'DOCTOR': return '/doctor'
      case 'HEALTHMENTOR':
      case 'MENTOR': return '/mentor'
      case 'RECOVEREDPATIENT': return '/recoveredpatient'
      case 'SUFFERINGPATIENT':
      case 'USER':
      default: return '/suff-pat'
    }
  }

  const prefix = getRolePrefix(user?.role)

  useEffect(() => {
    if (!user?.id) return
    setLoading(true)

    if (isPatient) {
      fetch(`/suff-pat/performance/${user.id}`)
        .then(res => res.json())
        .then(data => {
          if (data.success) {
            setStats(prev => ({
              ...prev,
              hydration: data.hydrationScore || 0,
              calories: data.calories || 0,
              meditationMinutes: data.meditationScore || 0
            }))
            setMedSeconds(data.meditationScore || 0)
          }
        })
        .catch(err => console.error(err))
    }

    fetch('/api/connections')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          const active = data.filter(c => c.connected)
          setContacts(active)
          setStats(prev => ({
            ...prev,
            activePatients: active.length
          }))
        }
      })
      .catch(err => console.error(err))

    fetch('/api/appointments/me')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          setAppointments(data)
          setStats(prev => ({
            ...prev,
            consultations: data.length
          }))
        }
        setLoading(false)
      })
      .catch(err => {
        console.error(err)
        setLoading(false)
      })

    fetch('/blog/api/public')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          const written = data.filter(b => b.authorId === user.id)
          setStats(prev => ({
            ...prev,
            blogsWritten: written.length
          }))
        }
      })
      .catch(err => console.error(err))
  }, [user?.id])

  useEffect(() => {
    if (timerRunning) {
      timerInterval.current = setInterval(() => {
        setSessionSeconds(prev => prev + 1)
        setMedSeconds(prev => prev + 1)
      }, 1000)
    } else {
      clearInterval(timerInterval.current)
    }
    return () => clearInterval(timerInterval.current)
  }, [timerRunning])

  const handleJumpToChat = (contactEmail) => {
    localStorage.setItem('active_chat_recipient', contactEmail)
    navigate(`${prefix}/chat`)
  }

  const addWater = () => {
    if (!user?.id) return
    fetch(`/suff-pat/${user.id}/hydration`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ hydrationAmount: 1 })
    })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          setStats(prev => ({ ...prev, hydration: data.hydrationScore }))
        }
      })
      .catch(err => console.error(err))
  }

  const toggleMeditationTimer = () => {
    if (timerRunning) {
      setTimerRunning(false)
      if (sessionSeconds > 0 && user?.id) {
        fetch(`/suff-pat/${user.id}/meditation`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ meditationTime: sessionSeconds })
        })
          .then(res => res.json())
          .then(data => {
            if (data.success) {
              setStats(prev => ({ ...prev, meditationMinutes: data.meditationScore }))
              setSessionSeconds(0)
            }
          })
          .catch(err => console.error(err))
      }
    } else {
      setTimerRunning(true)
    }
  }

  const logCalories = (amount) => {
    if (!user?.id) return
    const newCalories = stats.calories + amount
    fetch(`/suff-pat/${user.id}/calories`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ calories: newCalories })
    })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          setStats(prev => ({ ...prev, calories: data.calories }))
        }
      })
      .catch(err => console.error(err))
  }

  const getMedTargetSeconds = () => {
    if (meditationType === 'short') return 5 * 60
    if (meditationType === 'mid') return 15 * 60
    return 30 * 60
  }

  const medTargetSeconds = getMedTargetSeconds()
  const medProgress = Math.min(100, (medSeconds / medTargetSeconds) * 100)
  const circumference = 283
  const medDashOffset = circumference - (circumference * medProgress) / 100

  const calTarget = 2000
  const calProgress = Math.min(100, (stats.calories / calTarget) * 100)
  const calDashOffset = circumference - (circumference * calProgress) / 100

  const formatTimer = (totalSecs) => {
    const mins = Math.floor(totalSecs / 60)
    const secs = totalSecs % 60
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
  }

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.1 } }
  }

  const cardVariants = {
    hidden: { opacity: 0, y: 12 },
    visible: { opacity: 1, y: 0, transition: { type: 'spring', stiffness: 100, damping: 15 } }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-1.5">
        <h1 className="text-2xl font-bold tracking-tight text-slate-800 dark:text-white">
          {role === 'DOCTOR' ? 'Doctor Workspace' : 
           role === 'HEALTHMENTOR' || role === 'MENTOR' ? 'Mentor Workspace' : 
           role === 'RECOVEREDPATIENT' ? 'Wellness Champion Workspace' : 
           'Patient Health Center'}
        </h1>
        <p className="text-xs text-slate-500 dark:text-slate-400">
          Real-time insights, activity logs, and metric goals.
        </p>
      </div>

      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          {[...Array(4)].map((_, i) => (
            <div key={i} className="h-32 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/60 dark:border-slate-800 animate-shimmer relative overflow-hidden" />
          ))}
        </div>
      ) : (
        <motion.div 
          variants={containerVariants}
          initial="hidden"
          animate="visible"
          className="grid grid-cols-1 md:grid-cols-4 gap-5"
        >
          {/* Doctor Stats */}
          {role === 'DOCTOR' && (
            <>
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-blue-500/10 text-blue-600 dark:text-blue-400 flex items-center justify-center mb-3">
                  <Users className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Connected Patients</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={stats.activePatients} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-indigo-500/10 text-indigo-600 dark:text-indigo-400 flex items-center justify-center mb-3">
                  <Activity className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Consultations</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={stats.consultations} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-pink-500/10 text-pink-600 dark:text-pink-400 flex items-center justify-center mb-3">
                  <BookOpen className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Blogs Published</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={stats.blogsWritten} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 flex items-center justify-center mb-3">
                  <Award className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Verification Level</p>
                <h3 className="text-xl font-extrabold text-slate-800 dark:text-white mt-1">Senior MD</h3>
              </motion.div>
            </>
          )}

          {/* Mentor Stats */}
          {(role === 'HEALTHMENTOR' || role === 'MENTOR') && (
            <>
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-blue-500/10 text-blue-600 dark:text-blue-400 flex items-center justify-center mb-3">
                  <Heart className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Assigned Mentees</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={12} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-purple-500/10 text-purple-600 dark:text-purple-400 flex items-center justify-center mb-3">
                  <Award className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Mentor Rating</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">4.9 / 5.0</h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-orange-500/10 text-orange-600 dark:text-orange-400 flex items-center justify-center mb-3">
                  <Activity className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Supervised Sets</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={140} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-teal-500/10 text-teal-600 dark:text-teal-400 flex items-center justify-center mb-3">
                  <Clock className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Guided Hours</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={94} />
                </h3>
              </motion.div>
            </>
          )}

          {/* Champion Stats */}
          {role === 'RECOVEREDPATIENT' && (
            <>
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-blue-500/10 text-blue-600 dark:text-blue-400 flex items-center justify-center mb-3">
                  <Users className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Patients Guided</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={stats.activePatients} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-indigo-500/10 text-indigo-600 dark:text-indigo-400 flex items-center justify-center mb-3">
                  <Flame className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Champion Rating</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">4.8 / 5.0</h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-pink-500/10 text-pink-600 dark:text-pink-400 flex items-center justify-center mb-3">
                  <BookOpen className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Stories Shared</p>
                <h3 className="text-2xl font-extrabold text-slate-800 dark:text-white mt-1">
                  <CountUp end={stats.blogsWritten} />
                </h3>
              </motion.div>

              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow">
                <div className="w-10 h-10 rounded-xl bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 flex items-center justify-center mb-3">
                  <CheckCircle className="w-5 h-5" />
                </div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Verification</p>
                <h3 className="text-xl font-extrabold text-slate-800 dark:text-white mt-1">Verified</h3>
              </motion.div>
            </>
          )}

          {/* Suffering Patient / User Tracker Cards */}
          {isPatient && (
            <>
              {/* Active Chat Team Card */}
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow flex flex-col justify-between min-h-[160px]">
                <div>
                  <div className="w-10 h-10 rounded-xl bg-blue-500/10 text-blue-600 dark:text-blue-400 flex items-center justify-center mb-3">
                    <MessageSquare className="w-5 h-5" />
                  </div>
                  <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Care Team Support</p>
                </div>
                <div className="flex flex-wrap gap-1.5 mt-2 overflow-x-auto">
                  {contacts.slice(0, 4).map((c) => (
                    <button
                      key={c.id}
                      onClick={() => handleJumpToChat(c.email)}
                      title={`Chat with ${c.name}`}
                      className="w-9 h-9 rounded-full overflow-hidden border-2 border-blue-500/20 hover:border-blue-500 transition-all transform hover:scale-105 active:scale-95"
                    >
                      <img
                        src={c.profilePicture || 'https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o='}
                        alt={c.name}
                        className="w-full h-full object-cover"
                      />
                    </button>
                  ))}
                  {contacts.length === 0 && (
                    <p className="text-[10px] text-slate-400 font-semibold italic">No connected mentors yet.</p>
                  )}
                </div>
              </motion.div>

              {/* Water Card */}
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow flex flex-col justify-between min-h-[160px]">
                <div>
                  <div className="w-10 h-10 rounded-xl bg-sky-500/10 text-sky-600 dark:text-sky-400 flex items-center justify-center mb-3">
                    <CheckCircle className="w-5 h-5" />
                  </div>
                  <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Hydration Intake</p>
                  <h3 className="text-xl font-extrabold text-slate-800 dark:text-white mt-1">
                    <CountUp end={stats.hydration} /> / 8 <span className="text-xs text-slate-400 font-medium">cups</span>
                  </h3>
                </div>
                <div className="flex items-center justify-between mt-3">
                  <span className="text-[10px] text-slate-400 font-semibold">Goal: 8 cups</span>
                  <button 
                    onClick={addWater}
                    className="w-7 h-7 rounded-full bg-sky-500/10 text-sky-600 dark:text-sky-400 flex items-center justify-center hover:bg-sky-500 hover:text-white transition-all transform active:scale-95"
                  >
                    <Plus className="w-4 h-4" />
                  </button>
                </div>
              </motion.div>

              {/* Calorie Ring */}
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow flex flex-col justify-between min-h-[160px]">
                <div className="flex items-center gap-4">
                  <div className="relative w-16 h-16">
                    <svg className="w-full h-full transform -rotate-90">
                      <circle cx="50%" cy="50%" r="40%" fill="none" strokeWidth="6" className="stroke-slate-100 dark:stroke-slate-800"></circle>
                      <circle 
                        cx="50%" 
                        cy="50%" 
                        r="40%" 
                        fill="none" 
                        strokeWidth="6" 
                        stroke="#f97316"
                        strokeDasharray={circumference} 
                        strokeDashoffset={calDashOffset}
                        className="transition-all duration-500 ease-out"
                      ></circle>
                    </svg>
                    <div className="absolute inset-0 flex flex-col items-center justify-center">
                      <span className="text-xs font-bold text-slate-800 dark:text-white">{stats.calories}</span>
                      <span className="text-[8px] text-slate-400 font-bold uppercase">kcal</span>
                    </div>
                  </div>
                  <div>
                    <p className="text-[10px] text-slate-400 font-bold uppercase">Calories Burned</p>
                    <p className="text-xs text-slate-400 font-semibold mt-1">Goal: 2000</p>
                  </div>
                </div>
                <div className="flex gap-1.5 mt-3">
                  <button 
                    onClick={() => logCalories(100)}
                    className="flex-1 py-1.5 bg-orange-500/10 hover:bg-orange-500 hover:text-white text-orange-600 dark:text-orange-400 text-[10px] font-bold rounded-lg transition-all"
                  >
                    +100
                  </button>
                  <button 
                    onClick={() => logCalories(500)}
                    className="flex-1 py-1.5 bg-orange-500/10 hover:bg-orange-500 hover:text-white text-orange-600 dark:text-orange-400 text-[10px] font-bold rounded-lg transition-all"
                  >
                    +500
                  </button>
                </div>
              </motion.div>

              {/* Meditation timer */}
              <motion.div variants={cardVariants} className="p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200/50 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-shadow flex flex-col justify-between min-h-[160px]">
                <div className="flex items-center gap-4">
                  <div className="relative w-16 h-16">
                    <svg className="w-full h-full transform -rotate-90">
                      <circle cx="50%" cy="50%" r="40%" fill="none" strokeWidth="6" className="stroke-slate-100 dark:stroke-slate-800"></circle>
                      <circle 
                        cx="50%" 
                        cy="50%" 
                        r="40%" 
                        fill="none" 
                        strokeWidth="6" 
                        stroke="#06b6d4"
                        strokeDasharray={circumference} 
                        strokeDashoffset={medDashOffset}
                        className="transition-all duration-1000 linear"
                      ></circle>
                    </svg>
                    <div className="absolute inset-0 flex flex-col items-center justify-center">
                      <span className="text-xs font-bold text-slate-800 dark:text-white">{formatTimer(medSeconds)}</span>
                      <span className="text-[7px] text-slate-400 font-bold uppercase">{meditationType}</span>
                    </div>
                  </div>
                  <div>
                    <p className="text-[10px] text-slate-400 font-bold uppercase">Meditation</p>
                    <div className="flex gap-1 mt-1 bg-slate-100 dark:bg-slate-800 p-0.5 rounded-md text-[8px] font-bold">
                      <button onClick={() => setMeditationType('short')} className={`px-1 py-0.5 rounded ${meditationType === 'short' ? 'bg-white dark:bg-slate-700 text-cyan-600' : 'text-slate-400'}`}>5m</button>
                      <button onClick={() => setMeditationType('mid')} className={`px-1 py-0.5 rounded ${meditationType === 'mid' ? 'bg-white dark:bg-slate-700 text-cyan-600' : 'text-slate-400'}`}>15m</button>
                      <button onClick={() => setMeditationType('long')} className={`px-1 py-0.5 rounded ${meditationType === 'long' ? 'bg-white dark:bg-slate-700 text-cyan-600' : 'text-slate-400'}`}>30m</button>
                    </div>
                  </div>
                </div>
                <div className="flex justify-center mt-3">
                  <button 
                    onClick={toggleMeditationTimer}
                    className="w-7 h-7 rounded-full bg-cyan-500/10 text-cyan-600 dark:text-cyan-400 flex items-center justify-center hover:bg-cyan-500 hover:text-white transition-all transform active:scale-95"
                    title={timerRunning ? "Pause & Save" : "Start meditation"}
                  >
                    {timerRunning ? <Pause className="w-3.5 h-3.5" /> : <Play className="w-3.5 h-3.5" />}
                  </button>
                </div>
              </motion.div>
            </>
          )}
        </motion.div>
      )}
    </div>
  )
}
