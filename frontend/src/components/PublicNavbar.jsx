import React, { useState, useEffect } from 'react'
import { Link, NavLink, useLocation } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'

export default function PublicNavbar() {
  const location = useLocation()
  const [visible, setVisible] = useState(true)
  const [lastScrollY, setLastScrollY] = useState(0)

  useEffect(() => {
    const handleScroll = () => {
      const currentScrollY = window.scrollY
      if (currentScrollY > lastScrollY && currentScrollY > 80) {
        setVisible(false)
      } else {
        setVisible(true)
      }
      setLastScrollY(currentScrollY)
    }
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [lastScrollY])

  const links = [
    { name: 'Home', path: '/home' },
    { name: 'Features', path: '/features' },
    { name: 'Services', path: '/services' },
    { name: 'About', path: '/about' },
    { name: 'Contact', path: '/contact' },
  ]

  return (
    <motion.nav
      initial={{ y: 0 }}
      animate={{ y: visible ? 0 : -80 }}
      transition={{ duration: 0.3, ease: 'easeInOut' }}
      className="fixed top-0 inset-x-0 z-50 backdrop-blur-xl bg-white/70 dark:bg-gray-950/70 border-b border-gray-100 dark:border-white/5 transition-colors duration-500"
    >
      <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        <Link to="/home" className="flex items-center gap-2 font-extrabold text-xl text-gray-900 dark:text-white group">
          <span className="w-8 h-8 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-sm shadow group-hover:scale-105 transition-transform duration-300">
            <i className="fa-solid fa-heart-pulse" />
          </span>
          MedTrackFit
        </Link>
        
        <div className="hidden md:flex items-center gap-6 h-full relative">
          {links.map((link) => {
            const isActive = location.pathname === link.path
            return (
              <NavLink
                key={link.path}
                to={link.path}
                className={`relative py-5 px-1.5 font-medium text-sm transition-colors duration-300 ${
                  isActive
                    ? 'text-blue-600 dark:text-blue-400 font-semibold'
                    : 'text-gray-500 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white'
                }`}
              >
                {link.name}
                {isActive && (
                  <motion.div
                    layoutId="navbar-underline"
                    className="absolute bottom-0 left-0 right-0 h-[2px] bg-blue-600 dark:bg-blue-400"
                    transition={{ type: 'spring', stiffness: 350, damping: 30 }}
                  />
                )}
              </NavLink>
            )
          })}
        </div>

        <Link
          to="/auth"
          className="relative overflow-hidden inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-semibold px-5 py-2.5 rounded-xl shadow-[0_4px_14px_rgba(37,99,235,0.25)] hover:shadow-[0_6px_20px_rgba(37,99,235,0.35)] hover:-translate-y-0.5 active:translate-y-0 active:scale-[0.98] transition-all text-sm group"
        >
          <span>Get Started</span>
          <i className="fa-solid fa-arrow-right text-xs group-hover:translate-x-0.5 transition-transform" />
        </Link>
      </div>
    </motion.nav>
  )
}
