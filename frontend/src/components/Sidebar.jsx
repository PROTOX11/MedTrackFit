import React from 'react'
import { NavLink } from 'react-router-dom'
import { motion } from 'framer-motion'
import { LayoutDashboard, MessageSquare, Users, BookOpen, UserCheck, ChevronLeft, ChevronRight, X, Heart } from 'lucide-react'

export default function Sidebar({ user, collapsed, setCollapsed, mobileOpen, setMobileOpen }) {
  // Determine role-based prefix route
  const getRolePrefix = (role) => {
    switch (role?.toUpperCase()) {
      case 'DOCTOR':
        return '/doctor'
      case 'HEALTHMENTOR':
      case 'MENTOR':
        return '/mentor'
      case 'RECOVEREDPATIENT':
        return '/recoveredpatient'
      case 'SUFFERINGPATIENT':
      case 'USER':
      default:
        return '/suff-pat'
    }
  }

  const prefix = getRolePrefix(user?.role)

  const navItems = [
    { name: 'Dashboard', path: `${prefix}/dashboard`, icon: LayoutDashboard },
    { name: 'Chat Support', path: `${prefix}/chat`, icon: MessageSquare },
    { name: 'Patients List', path: `${prefix}/patients`, icon: Users },
    { name: 'Medical Blog', path: `${prefix}/blog`, icon: BookOpen },
    { name: 'My Profile', path: `${prefix}/profile`, icon: UserCheck },
  ]

  return (
    <motion.aside 
      animate={{ width: collapsed ? 80 : 256 }}
      transition={{ type: 'spring', stiffness: 300, damping: 30 }}
      className={`fixed top-0 left-0 bottom-0 z-40 bg-white/70 dark:bg-slate-950/70 backdrop-blur-xl border-r border-slate-200 dark:border-slate-800/60 ${
        mobileOpen ? 'translate-x-0' : '-translate-x-full'
      } md:translate-x-0 sidebar-desktop-visible flex flex-col justify-between`}
    >
      <div>
        {/* Brand logo header */}
        <div className="flex items-center justify-between h-16 px-4 border-b border-slate-200 dark:border-slate-800/50">
          {!collapsed && (
            <div className="flex items-center gap-2 font-extrabold text-lg text-slate-950 dark:text-white">
              <span className="w-7 h-7 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xs">
                <Heart className="w-4 h-4 fill-current" />
              </span>
              <span>MedTrackFit</span>
            </div>
          )}
          {collapsed && (
            <div className="mx-auto text-xl text-blue-600 dark:text-blue-400">
              <span className="w-7 h-7 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xs">
                <Heart className="w-4 h-4 fill-current" />
              </span>
            </div>
          )}
          <button 
            onClick={() => setCollapsed(!collapsed)}
            className="hidden md:flex p-1.5 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-500 dark:text-slate-400"
          >
            {collapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
          </button>
          <button 
            onClick={() => setMobileOpen(false)}
            className="md:hidden p-1.5 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-500 dark:text-slate-400"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Navigation List */}
        <nav className="p-3 space-y-1.5 mt-4">
          {navItems.map((item) => {
            const Icon = item.icon
            return (
              <NavLink
                key={item.name}
                to={item.path}
                onClick={() => setMobileOpen && setMobileOpen(false)}
                className={({ isActive }) =>
                  `flex items-center gap-3.5 px-3.5 py-3 rounded-xl font-medium text-sm transition-all ${
                    isActive
                      ? 'bg-blue-500/10 text-blue-600 dark:text-blue-400 font-bold border border-blue-500/15'
                      : 'text-slate-600 dark:text-slate-400 hover:bg-slate-100/50 dark:hover:bg-slate-900/60'
                  }`
                }
              >
                <Icon className="w-5 h-5" />
                {!collapsed && <span>{item.name}</span>}
              </NavLink>
            )
          })}
        </nav>
      </div>

      {/* Footer User Card */}
      <div className="p-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/20">
        <div className="flex items-center gap-3">
          <img 
            src={user?.profilePicture || 'https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o='} 
            alt="Profile" 
            className="w-10 h-10 rounded-full object-cover border-2 border-blue-500/20"
          />
          {!collapsed && (
            <div className="min-w-0 flex-1">
              <p className="text-xs font-bold truncate text-slate-800 dark:text-slate-200">{user?.name || 'User'}</p>
              <p className="text-[10px] text-slate-400 truncate uppercase tracking-wider font-semibold">
                {(() => {
                  if (!user?.role) return 'Member'
                  const roleStr = user.role.toLowerCase()
                  if (roleStr === 'sufferingpatient') return 'Suffering Patient'
                  if (roleStr === 'recoveredpatient') return 'Recovered Patient'
                  if (roleStr === 'healthmentor') return 'Health Mentor'
                  return user.role.replace(/([A-Z])/g, ' $1').trim()
                })()}
              </p>
            </div>
          )}
        </div>
      </div>
    </motion.aside>
  )
}
