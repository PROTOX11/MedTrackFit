import React, { useEffect, useState } from 'react'
import { Sun, Moon, LogOut, Menu } from 'lucide-react'
import { motion } from 'framer-motion'

export default function Header({ user, setMobileOpen }) {
  const [theme, setTheme] = useState(
    localStorage.getItem('theme') || 
    (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light')
  )
  const [animate, setAnimate] = useState(false)

  useEffect(() => {
    const root = document.documentElement
    root.classList.remove('light', 'dark')
    root.classList.add(theme)
    localStorage.setItem('theme', theme)
  }, [theme])

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light')
    setAnimate(true)
    setTimeout(() => setAnimate(false), 500)
  }

  const handleLogout = () => {
    window.location.href = '/do-logout'
  }

  return (
    <header className="sticky top-0 z-30 flex items-center justify-between h-16 px-4 md:px-6 bg-white/70 dark:bg-slate-950/70 backdrop-blur-xl border-b border-slate-200 dark:border-slate-800/50 transition-colors duration-500">
      <div className="flex items-center">
        <button
          onClick={() => setMobileOpen(prev => !prev)}
          className="md:hidden p-2 mr-3 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-xl transition-all"
          title="Toggle menu"
        >
          <Menu className="w-5 h-5" />
        </button>
        <div>
          <h2 className="text-sm md:text-base font-bold text-slate-800 dark:text-slate-200 truncate max-w-[150px] sm:max-w-none">
            Welcome back, <span className="text-blue-600 dark:text-blue-400 font-extrabold">{user?.name || 'Friend'}</span>
          </h2>
          <p className="hidden sm:block text-[10px] text-slate-400 font-medium">Have a healthy and productive day!</p>
        </div>
      </div>

      <div className="flex items-center gap-4">
        {/* Theme Toggle Button */}
        <button
          onClick={toggleTheme}
          className="w-10 h-10 rounded-2xl flex items-center justify-center border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-400 bg-slate-50 dark:bg-slate-900 hover:bg-slate-100 dark:hover:bg-slate-800 shadow-sm transition-all transform active:scale-95"
          title="Toggle light/dark theme"
        >
          <motion.div
            animate={{ rotate: theme === 'dark' ? 180 : 0, scale: animate ? 1.2 : 1 }}
            transition={{ type: 'spring', stiffness: 200, damping: 15 }}
          >
            {theme === 'dark' ? (
              <Sun className="w-5 h-5 text-amber-500" />
            ) : (
              <Moon className="w-5 h-5 text-indigo-500" />
            )}
          </motion.div>
        </button>

        {/* Logout Button */}
        <button
          onClick={handleLogout}
          className="flex items-center gap-2 px-4 py-2 border border-red-200 dark:border-red-950/30 bg-red-50/20 dark:bg-red-950/10 hover:bg-red-50 dark:hover:bg-red-950/20 text-red-600 dark:text-red-400 rounded-xl text-xs font-bold transition-all shadow-sm"
        >
          <LogOut className="w-3.5 h-3.5" />
          <span>Logout</span>
        </button>
      </div>
    </header>
  )
}
