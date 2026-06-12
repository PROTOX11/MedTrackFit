import React, { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom'
import Sidebar from './components/Sidebar.jsx'
import Header from './components/Header.jsx'
import Dashboard from './pages/Dashboard.jsx'
import Chat from './pages/Chat.jsx'
import Patients from './pages/Patients.jsx'
import Blog from './pages/Blog.jsx'
import Profile from './pages/Profile.jsx'
import Home from './pages/Home.jsx'
import Auth from './pages/Auth.jsx'
import About from './pages/About.jsx'
import Contact from './pages/Contact.jsx'
import Features from './pages/Features.jsx'
import Services from './pages/Services.jsx'
import Privacy from './pages/Privacy.jsx'
import Terms from './pages/Terms.jsx'

import PublicNavbar from './components/PublicNavbar.jsx'
import PublicFooter from './components/PublicFooter.jsx'
import { Outlet } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'

/* ─── Public route paths ─── */
const PUBLIC_PATHS = ['/home', '/auth', '/join', '/about', '/contact', '/features', '/services', '/privacy', '/terms', '/']

function isPublicPath(pathname) {
  return PUBLIC_PATHS.some(p => {
    if (p === '/') return pathname === '/'
    return pathname === p || pathname.startsWith(p + '/')
  })
}

/* ─── Shared Public Layout Wrapper ─── */
function PublicLayout() {
  const location = useLocation()
  return (
    <div className="flex flex-col min-h-screen bg-white dark:bg-gray-950 text-slate-900 dark:text-slate-100 bg-grid-pattern">
      <PublicNavbar />
      <main className="flex-1 w-full max-w-full overflow-x-hidden pt-16">
        <AnimatePresence mode="wait">
          <motion.div
            key={location.pathname}
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -12 }}
            transition={{ duration: 0.35, ease: [0.16, 1, 0.3, 1] }}
          >
            <Outlet />
          </motion.div>
        </AnimatePresence>
      </main>
      <PublicFooter />
    </div>
  )
}

/* ─── Public layout wrapper (no sidebar) ─── */
function PublicRoutes() {
  return (
    <Routes>
      <Route element={<PublicLayout />}>
        <Route path="/home" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/features" element={<Features />} />
        <Route path="/services" element={<Services />} />
        <Route path="/privacy" element={<Privacy />} />
        <Route path="/terms" element={<Terms />} />
      </Route>
      <Route path="/auth" element={<Auth />} />
      <Route path="/join" element={<Auth />} />
      <Route path="/" element={<Navigate to="/home" replace />} />
    </Routes>
  )
}

/* ─── Authenticated (dashboard) layout ─── */
function AuthenticatedRoutes({ user, handleUpdateUser }) {
  const location = useLocation()
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false)
  const [mobileSidebarOpen, setMobileSidebarOpen] = useState(false)
  const [submittingRole, setSubmittingRole] = useState(false)

  const getRolePrefix = (role) => {
    switch (role?.toUpperCase()) {
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
  const isNewOAuth = user?.role === 'USER'

  const handleOauthComplete = (e) => {
    e.preventDefault()
    const fd = new FormData(e.target)
    const payload = {
      name: user.name,
      email: user.email,
      picture: user.profilePicture,
      phoneNumber: fd.get('phone')?.trim() || '',
      about: fd.get('about')?.trim() || '',
      role: fd.get('role'),
    }
    setSubmittingRole(true)
    fetch('/api/auth/oauth-complete', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(res => res.json())
      .then(data => {
        if (data.success && data.redirectUrl) {
          window.location.href = data.redirectUrl
        } else {
          alert(data.message || 'Failed to complete profile registration.')
          setSubmittingRole(false)
        }
      })
      .catch(err => {
        console.error(err)
        alert('Network error')
        setSubmittingRole(false)
      })
  }

  return (
    <>
      <div className={`min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-800 dark:text-slate-100 flex ${isNewOAuth ? "filter blur-md pointer-events-none select-none" : ""}`}>
        <Sidebar 
          user={user} 
          collapsed={sidebarCollapsed} 
          setCollapsed={setSidebarCollapsed} 
          mobileOpen={mobileSidebarOpen} 
          setMobileOpen={setMobileSidebarOpen} 
        />
        {mobileSidebarOpen && (
          <div 
            onClick={() => setMobileSidebarOpen(false)}
            className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-30 md:hidden transition-opacity duration-300"
          />
        )}
        <div className={`flex-1 flex flex-col transition-all duration-300 min-h-screen pl-0 md:${sidebarCollapsed ? 'pl-20' : 'pl-64'}`}>
          <Header user={user} setMobileOpen={setMobileSidebarOpen} />
          <main className="flex-1 p-4 md:p-8 max-w-full overflow-x-hidden">
            <AnimatePresence mode="wait">
              <motion.div
                key={location.pathname}
                initial={{ opacity: 0, y: 12 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -12 }}
                transition={{ duration: 0.3, ease: [0.16, 1, 0.3, 1] }}
                className="h-full w-full"
              >
                <Routes location={location} key={location.pathname}>
                  {/* Doctor Routes */}
                  <Route path="/doctor/dashboard" element={<Dashboard user={user} />} />
                  <Route path="/doctor/chat" element={<Chat user={user} />} />
                  <Route path="/doctor/patients" element={<Patients user={user} />} />
                  <Route path="/doctor/blog" element={<Blog user={user} />} />
                  <Route path="/doctor/profile" element={<Profile user={user} onUpdateUser={handleUpdateUser} />} />

                  {/* Mentor Routes */}
                  <Route path="/mentor/dashboard" element={<Dashboard user={user} />} />
                  <Route path="/mentor/chat" element={<Chat user={user} />} />
                  <Route path="/mentor/patients" element={<Patients user={user} />} />
                  <Route path="/mentor/blog" element={<Blog user={user} />} />
                  <Route path="/mentor/profile" element={<Profile user={user} onUpdateUser={handleUpdateUser} />} />

                  {/* Suffering Patient Routes */}
                  <Route path="/suff-pat/dashboard" element={<Dashboard user={user} />} />
                  <Route path="/suff-pat/chat" element={<Chat user={user} />} />
                  <Route path="/suff-pat/patients" element={<Patients user={user} />} />
                  <Route path="/suff-pat/blog" element={<Blog user={user} />} />
                  <Route path="/suff-pat/profile" element={<Profile user={user} onUpdateUser={handleUpdateUser} />} />

                  {/* Recovered Patient Routes */}
                  <Route path="/recoveredpatient/dashboard" element={<Dashboard user={user} />} />
                  <Route path="/recoveredpatient/chat" element={<Chat user={user} />} />
                  <Route path="/recoveredpatient/patients" element={<Patients user={user} />} />
                  <Route path="/recoveredpatient/blog" element={<Blog user={user} />} />
                  <Route path="/recoveredpatient/profile" element={<Profile user={user} onUpdateUser={handleUpdateUser} />} />

                  {/* Fallback redirect to role dashboard */}
                  <Route path="*" element={<Navigate to={`${prefix}/dashboard`} replace />} />
                </Routes>
              </motion.div>
            </AnimatePresence>
          </main>
        </div>
      </div>

      {isNewOAuth && (
        <div className="fixed inset-0 bg-slate-950/45 backdrop-blur-sm z-[9999] flex items-center justify-center p-4">
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl w-full max-w-md shadow-2xl p-8 animate-scale-in text-slate-800 dark:text-white">
            <div className="flex flex-col items-center gap-3 text-center mb-6">
              <span className="w-14 h-14 rounded-2xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-2xl shadow-xl animate-bounce">
                <i className="fa-solid fa-heart-pulse" />
              </span>
              <h2 className="text-xl font-extrabold">Welcome to MedTrackFit!</h2>
              <p className="text-xs text-slate-500 dark:text-slate-400">Please select your role and details to access your custom health workspace.</p>
            </div>

            <form onSubmit={handleOauthComplete} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1.5">Full Name</label>
                <input type="text" disabled value={user?.name || ''} className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 opacity-70 font-bold" />
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1.5">Email Address</label>
                <input type="email" disabled value={user?.email || ''} className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 opacity-70 font-bold" />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1.5">Phone Number</label>
                  <input type="tel" name="phone" required className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white outline-none focus:ring-2 focus:ring-blue-500/15" placeholder="+91 00000 00000" />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1.5">Account Role</label>
                  <select name="role" required className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white outline-none focus:ring-2 focus:ring-blue-500/15 cursor-pointer font-bold">
                    <option value="USER">Patient</option>
                    <option value="RECOVERED_PATIENT">Recovered Patient</option>
                    <option value="HEALTH_MENTOR">Health Mentor</option>
                    <option value="DOCTOR">Doctor</option>
                  </select>
                </div>
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-400 uppercase tracking-wider mb-1.5">About (Optional)</label>
                <textarea name="about" rows="2" className="w-full px-4 py-2.5 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white outline-none focus:ring-2 focus:ring-blue-500/15 resize-none" placeholder="Tell us about yourself..." />
              </div>
              <button
                type="submit"
                disabled={submittingRole}
                className="w-full mt-2 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold py-3.5 rounded-xl shadow-lg hover:shadow-xl hover:-translate-y-0.5 transition-all disabled:opacity-60 flex items-center justify-center gap-2"
              >
                {submittingRole ? <i className="fa-solid fa-circle-notch fa-spin" /> : 'Complete Registration'}
              </button>
            </form>
          </div>
        </div>
      )}
    </>
  )
}

/* ─── Root Router: decides public vs authenticated ─── */
function AppRouter() {
  const location = useLocation()
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // 1. For public pages, skip auth check entirely
    if (isPublicPath(location.pathname)) {
      setLoading(false)
      return
    }

    // 2. Check window.__USER__ set by Thymeleaf — only trust it if email and role are present
    if (window.__USER__ && window.__USER__.email && window.__USER__.email.trim() !== '' && window.__USER__.role) {
      setUser(window.__USER__)
      setLoading(false)
      return
    }

    // 3. Always fetch /api/me for dashboard routes (Spring Security session-based)
    fetch('/api/me')
      .then(res => {
        if (!res.ok) throw new Error('Unauthenticated')
        return res.json()
      })
      .then(data => {
        if (data && data.email) {
          setUser(data)
        }
        setLoading(false)
      })
      .catch(() => {
        setLoading(false)
      })
  }, [location.pathname])

  const handleUpdateUser = (updatedData) => {
    setUser(prev => ({ ...prev, ...updatedData }))
  }

  if (loading) {
    return (
      <div className="flex h-screen w-screen items-center justify-center bg-white dark:bg-gray-950">
        <div className="flex flex-col items-center gap-3">
          <span className="w-14 h-14 rounded-2xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-2xl shadow-xl">
            <i className="fa-solid fa-heart-pulse fa-beat-fade" />
          </span>
          <p className="text-xs font-bold text-slate-400 uppercase tracking-wider mt-2">Loading MedTrackFit…</p>
        </div>
      </div>
    )
  }

  // If current path is public → show public layout
  if (isPublicPath(location.pathname)) {
    return <PublicRoutes />
  }

  // If authenticated → show dashboard
  if (user) {
    return <AuthenticatedRoutes user={user} handleUpdateUser={handleUpdateUser} />
  }

  // Not authenticated, not a public page → redirect to auth
  return <Navigate to="/auth" replace />
}

function ScrollToTop() {
  const { pathname } = useLocation()
  useEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])
  return null
}

export default function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <AppRouter />
    </BrowserRouter>
  )
}
