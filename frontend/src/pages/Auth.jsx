import React, { useState, useEffect, useRef } from 'react'
import { Link } from 'react-router-dom'

/* ───────────────────────────────────────────────
   Auth Page – Login (OTP) + Signup
   All API calls go to the Spring Boot backend.
─────────────────────────────────────────────── */

const INPUT_CLASS =
  'w-full px-4 py-2 rounded-xl border-2 border-gray-100 dark:border-gray-800 bg-gray-50 dark:bg-gray-950/50 text-gray-900 dark:text-white focus:bg-white dark:focus:bg-gray-900 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all placeholder-gray-400 dark:placeholder-gray-500 font-medium text-sm'

function Spinner() {
  return <i className="fa-solid fa-circle-notch fa-spin" />
}

export default function Auth() {
  const [tab, setTab] = useState('login')      // 'login' | 'signup'
  const [stage, setStage] = useState('form')   // 'form' | 'otp'
  const [pendingEmail, setPendingEmail] = useState('')
  const [msg, setMsg] = useState(null)         // { type: 'error'|'success', text }
  const [loading, setLoading] = useState(false)
  const [showRegisteredModal, setShowRegisteredModal] = useState(false)
  const [registeredEmail, setRegisteredEmail] = useState('')
  const [oauthData, setOauthData] = useState(null)
  const otpRef = useRef(null)

  useEffect(() => {
    document.title = 'Login / Sign Up — MedTrackFit'
    
    // Check if redirecting from Google OAuth signup
    const params = new URLSearchParams(window.location.search)
    if (params.get('oauth-register') === 'true') {
      setTab('signup')
      setOauthData({
        email: params.get('email') || '',
        name: params.get('name') || '',
        picture: params.get('picture') || ''
      })
    }
  }, [])

  useEffect(() => {
    setMsg(null)
    setStage('form')
    setPendingEmail('')
  }, [tab])

  useEffect(() => {
    if (stage === 'otp' && otpRef.current) otpRef.current.focus()
  }, [stage])

  const showMsg = (type, text) => setMsg({ type, text })

  /* ──── LOGIN: send OTP ──── */
  async function handleLoginSubmit(e) {
    e.preventDefault()
    const email = e.target.email.value.trim()
    if (!email) return showMsg('error', 'Please enter your email address.')
    setLoading(true)
    setMsg(null)
    try {
      const res = await fetch('/api/auth/send-otp', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email }),
      })
      const data = await res.json().catch(() => ({}))
      if (res.ok) {
        setPendingEmail(email)
        setStage('otp')
        showMsg('success', `OTP sent to ${email}. Check your inbox.`)
      } else {
        showMsg('error', data.message || 'Failed to send OTP. Please try again.')
      }
    } catch {
      showMsg('error', 'Network error. Please check your connection.')
    } finally {
      setLoading(false)
    }
  }

  /* ──── LOGIN: verify OTP ──── */
  async function handleOtpVerify(e) {
    e.preventDefault()
    const otp = e.target.otp.value.trim()
    if (otp.length !== 6) return showMsg('error', 'Please enter the 6-digit OTP.')
    setLoading(true)
    setMsg(null)
    try {
      const res = await fetch('/authenticate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ email: pendingEmail, username: pendingEmail, password: otp }),
        redirect: 'follow',
      })
      const isSuccess = (res.ok || res.redirected) && res.url && !res.url.includes('error')
      if (isSuccess) {
        showMsg('success', 'Login successful! Redirecting…')
        setTimeout(() => { window.location.href = res.url || '/suff-pat/dashboard' }, 800)
      } else {
        showMsg('error', 'Invalid or expired OTP. Please try again.')
      }
    } catch {
      showMsg('error', 'Authentication failed. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  /* ──── SIGNUP ──── */
  async function handleSignupSubmit(e) {
    e.preventDefault()
    const fd = new FormData(e.target)
    const payload = {
      name: fd.get('name')?.trim(),
      email: fd.get('email')?.trim(),
      phoneNumber: fd.get('phone')?.trim() || '',
      about: fd.get('about')?.trim() || '',
      role: fd.get('role'),
    }
    if (!payload.name || !payload.email) return showMsg('error', 'Name and email are required.')
    setLoading(true)
    setMsg(null)
    try {
      const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
      const data = await res.json().catch(() => ({}))
      if (res.status === 409 || data.alreadyExists) {
        setRegisteredEmail(payload.email)
        setShowRegisteredModal(true)
      } else if (res.ok) {
        setPendingEmail(payload.email)
        setStage('otp')
        showMsg('success', `Account created! OTP sent to ${payload.email}.`)
      } else {
        showMsg('error', data.message || 'Registration failed. Please try again.')
      }
    } catch {
      showMsg('error', 'Network error. Please check your connection.')
    } finally {
      setLoading(false)
    }
  }

  /* ──── OAUTH COMPLETE REGISTRATION ──── */
  async function handleOauthSignupSubmit(e) {
    e.preventDefault()
    if (!oauthData) return
    const fd = new FormData(e.target)
    const payload = {
      name: oauthData.name,
      email: oauthData.email,
      picture: oauthData.picture,
      phoneNumber: fd.get('phone')?.trim() || '',
      about: fd.get('about')?.trim() || '',
      role: fd.get('role'),
    }
    setLoading(true)
    setMsg(null)
    try {
      const res = await fetch('/api/auth/oauth-complete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
      const data = await res.json().catch(() => ({}))
      if (res.ok && data.success) {
        showMsg('success', 'Registration complete! Redirecting…')
        setTimeout(() => { window.location.href = data.redirectUrl || '/suff-pat/dashboard' }, 800)
      } else {
        showMsg('error', data.message || 'Registration failed. Please try again.')
      }
    } catch {
      showMsg('error', 'Network error. Please check your connection.')
    } finally {
      setLoading(false)
    }
  }

  function goToLoginFromModal() {
    setShowRegisteredModal(false)
    setTab('login')
  }

  return (
    <div className="grid grid-cols-1 lg:grid-cols-12 min-h-screen overflow-x-hidden bg-white dark:bg-gray-950 text-gray-700 dark:text-gray-100">

      {/* ── LEFT: Branding (7 cols) ── */}
      <div className="lg:col-span-7 relative p-8 lg:p-12 xl:p-16 flex items-center justify-center lg:justify-end overflow-hidden min-h-[40vh] lg:min-h-screen lg:h-full">
        <div className="absolute inset-0 h-full w-full bg-gradient-to-br from-blue-950 via-indigo-950 to-slate-950" />
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-blue-600 rounded-full blur-[100px] opacity-40" />
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-purple-600 rounded-full blur-[120px] opacity-40" />

        <div className="relative z-10 w-full max-w-2xl animate-[fadeInLeft_0.7s_ease_both]">
          <div className="mb-12">
            <Link
              to="/home"
              className="inline-flex items-center gap-3 bg-white/5 px-5 py-2.5 rounded-full backdrop-blur-md border border-white/10 hover:bg-white/10 transition-colors"
            >
              <i className="fa-solid fa-arrow-left text-blue-400" />
              <span className="text-white font-medium text-sm">Back to Home</span>
            </Link>
          </div>

          <h1 className="text-5xl xl:text-6xl font-extrabold text-white leading-tight tracking-tight mb-6">
            Your Health, <br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-indigo-400">
              Your Support System
            </span>
          </h1>
          <p className="text-xl text-blue-100/90 leading-relaxed mb-12 max-w-2xl font-light">
            Join a community dedicated to real recovery. Connect with mentors who have walked the same path and doctors who validate your journey.
          </p>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 xl:gap-6">
            {[
              { icon: 'fa-users', color: 'blue', value: '10k+', label: 'Mentors' },
              { icon: 'fa-chart-line', color: 'green', value: '92%', label: 'Success Rate' },
              { icon: 'fa-user-doctor', color: 'purple', value: 'Verified', label: 'Guidance' },
            ].map(s => (
              <div key={s.label} className="bg-white/5 backdrop-blur-xl rounded-[1.5rem] p-5 border border-white/10 hover:-translate-y-1 transition-transform shadow-xl">
                <div className={`w-10 h-10 rounded-xl bg-${s.color}-500/20 flex items-center justify-center text-${s.color}-400 mb-3`}>
                  <i className={`fa-solid ${s.icon} text-lg`} />
                </div>
                <h3 className="text-2xl font-extrabold text-white tracking-tight mb-1">{s.value}</h3>
                <p className={`text-${s.color}-200/70 text-xs font-semibold uppercase tracking-wider`}>{s.label}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ── RIGHT: Auth Card (5 cols) ── */}
      <div className="lg:col-span-5 flex items-center justify-center lg:justify-start p-6 py-12 lg:p-12 bg-gray-50 dark:bg-transparent relative z-20">

        <div className="w-full max-w-md relative z-30">

          {/* Mobile back */}
          <div className="lg:hidden mb-6 text-center">
            <Link to="/home" className="inline-flex items-center gap-2 text-gray-500 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-400 text-sm font-medium bg-white/50 dark:bg-gray-800/50 px-4 py-2 rounded-full backdrop-blur-md border border-gray-200 dark:border-gray-700">
              <i className="fa-solid fa-arrow-left" /> Back to Home
            </Link>
          </div>

          {/* Card */}
          <div className="bg-white dark:bg-gray-900/80 backdrop-blur-3xl rounded-[2rem] shadow-[0_20px_50px_-12px_rgba(0,0,0,0.1)] dark:shadow-[0_20px_50px_-12px_rgba(0,0,0,0.5)] overflow-hidden border border-gray-100 dark:border-white/5 p-5 sm:p-7">

            <div className="text-center mb-5 lg:hidden">
              <h2 className="text-2xl font-extrabold text-gray-900 dark:text-white">
                Welcome to <span className="text-blue-600 dark:text-blue-500">MedTrackFit</span>
              </h2>
            </div>

            {/* Tab switcher */}
            {stage === 'form' && (
              <div className="flex p-1.5 bg-gray-100/80 dark:bg-black/40 rounded-2xl mb-5 relative border border-gray-200/50 dark:border-white/5 shadow-inner">
                <div
                  className="absolute top-1.5 bottom-1.5 left-1.5 w-[calc(50%-6px)] bg-white dark:bg-gray-800/90 rounded-xl shadow-[0_2px_8px_rgba(0,0,0,0.08)] border border-gray-200/50 dark:border-white/5 transition-transform duration-300"
                  style={{ transform: tab === 'signup' ? 'translateX(100%)' : 'translateX(0)' }}
                />
                <button
                  className={`flex-1 py-3 text-sm font-bold z-10 relative transition-colors duration-300 ${tab === 'login' ? 'text-blue-600 dark:text-blue-400' : 'text-gray-500 dark:text-gray-400'}`}
                  onClick={() => setTab('login')}
                >
                  Login
                </button>
                <button
                  className={`flex-1 py-3 text-sm font-bold z-10 relative transition-colors duration-300 ${tab === 'signup' ? 'text-blue-600 dark:text-blue-400' : 'text-gray-500 dark:text-gray-400'}`}
                  onClick={() => setTab('signup')}
                >
                  Create Account
                </button>
              </div>
            )}

            {/* Message area */}
            {msg && (
              <div className={`mb-6 p-4 rounded-xl text-sm font-medium ${
                msg.type === 'error'
                  ? 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-300 border border-red-100 dark:border-red-800'
                  : 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-300 border border-green-100 dark:border-green-800'
              }`}>
                <i className={`fa-solid ${msg.type === 'error' ? 'fa-circle-exclamation' : 'fa-circle-check'} mr-2`} />
                {msg.text}
              </div>
            )}

            {/* ── OTP Stage ── */}
            {stage === 'otp' ? (
              <form onSubmit={handleOtpVerify} className="flex flex-col items-center text-center gap-5">
                <div className="w-16 h-16 bg-blue-50 dark:bg-blue-900/20 rounded-2xl flex items-center justify-center text-blue-600 dark:text-blue-400 border border-blue-100 dark:border-blue-800/30 shadow-inner">
                  <i className="fa-solid fa-shield-halved text-2xl" />
                </div>
                <div>
                  <h3 className="text-2xl font-extrabold text-gray-900 dark:text-white mb-1 tracking-tight">Check your email</h3>
                  <p className="text-gray-500 dark:text-gray-400 text-sm px-4 leading-relaxed">
                    We've sent a 6-digit secure code to<br />
                    <span className="font-bold text-gray-900 dark:text-white">{pendingEmail}</span>
                  </p>
                </div>
                <input
                  ref={otpRef}
                  name="otp"
                  type="text"
                  maxLength={6}
                  required
                  placeholder="••••••"
                  className="w-full text-center text-4xl tracking-[0.5em] px-4 py-4 rounded-xl border-2 border-gray-200 dark:border-gray-800 bg-gray-50 dark:bg-gray-950/50 text-gray-900 dark:text-white focus:bg-white dark:focus:bg-gray-900 focus:ring-2 focus:ring-blue-500/30 focus:border-blue-500 outline-none transition-all font-mono placeholder-gray-300 dark:placeholder-gray-700 shadow-inner"
                />
                <button
                  type="submit"
                  disabled={loading}
                  className="w-full bg-gray-900 dark:bg-white text-white dark:text-gray-900 font-bold py-4 rounded-xl shadow-lg hover:shadow-xl hover:-translate-y-0.5 transition-all disabled:opacity-60 flex items-center justify-center gap-2"
                >
                  {loading ? <Spinner /> : <><i className="fa-solid fa-check-circle" /> Verify &amp; Continue</>}
                </button>
                <button
                  type="button"
                  onClick={() => setStage('form')}
                  className="text-sm font-bold text-gray-500 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white transition-colors flex items-center gap-2"
                >
                  <i className="fa-solid fa-arrow-left" /> Back to Email
                </button>
              </form>
            ) : (
              <div 
                className="transition-all duration-500 ease-in-out overflow-hidden" 
                style={{ maxHeight: tab === 'login' ? '180px' : '450px' }}
              >
                {tab === 'login' ? (
                  /* ── LOGIN FORM ── */
                  <form onSubmit={handleLoginSubmit} className="space-y-6 pb-2">
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Email Address</label>
                      <div className="relative group">
                        <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-gray-400 group-focus-within:text-blue-500 transition-colors">
                          <i className="fa-regular fa-envelope" />
                        </div>
                        <input
                          type="email"
                          name="email"
                          required
                          className={`${INPUT_CLASS} pl-11`}
                          placeholder="name@example.com"
                        />
                      </div>
                    </div>
                    <button
                      type="submit"
                      disabled={loading}
                      className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold py-2.5 rounded-xl shadow-[0_8px_20px_-6px_rgba(37,99,235,0.4)] hover:shadow-[0_12px_25px_-6px_rgba(37,99,235,0.5)] hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2 group disabled:opacity-60"
                    >
                      {loading ? <Spinner /> : (
                        <>
                          <span>Send OTP Code</span>
                          <i className="fa-solid fa-arrow-right group-hover:translate-x-1 transition-transform" />
                        </>
                      )}
                    </button>
                  </form>
                ) : oauthData ? (
                  /* ── OAUTH SIGNUP FORM ── */
                  <form onSubmit={handleOauthSignupSubmit} className="space-y-3.5 pb-2">
                    <div>
                      <p className="text-xs text-blue-600 dark:text-blue-400 font-bold mb-4 bg-blue-50 dark:bg-blue-950/40 p-3 rounded-xl border border-blue-100 dark:border-blue-900/30">
                        <i className="fa-solid fa-info-circle mr-1.5"></i>
                        Complete your profile registration to select your role.
                      </p>
                    </div>
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Full Name</label>
                      <input type="text" disabled value={oauthData.name} className={INPUT_CLASS + " opacity-75"} />
                    </div>
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Email Address</label>
                      <input type="email" disabled value={oauthData.email} className={INPUT_CLASS + " opacity-75"} />
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Phone</label>
                        <input type="tel" name="phone" className={INPUT_CLASS} placeholder="+91 00000 00000" />
                      </div>
                      <div>
                        <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Role</label>
                        <select name="role" required className={INPUT_CLASS + ' cursor-pointer'}>
                          <option value="USER">Patient</option>
                          <option value="RECOVERED_PATIENT">Recovered Patient</option>
                          <option value="HEALTH_MENTOR">Health Mentor</option>
                          <option value="DOCTOR">Doctor</option>
                        </select>
                      </div>
                    </div>
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">About (Optional)</label>
                      <textarea name="about" rows={2} className={INPUT_CLASS} placeholder="Tell us a bit about yourself…" />
                    </div>
                    <button
                      type="submit"
                      disabled={loading}
                      className="w-full mt-2 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold py-4 rounded-xl shadow-[0_8px_20px_-6px_rgba(37,99,235,0.4)] hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2 group disabled:opacity-60"
                    >
                      {loading ? <Spinner /> : (
                        <>
                          <span>Complete Registration</span>
                          <i className="fa-solid fa-arrow-right group-hover:translate-x-1 transition-transform" />
                        </>
                      )}
                    </button>
                  </form>
                ) : (
                  /* ── SIGNUP FORM ── */
                  <form onSubmit={handleSignupSubmit} className="space-y-3.5 pb-2">
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Full Name</label>
                      <input type="text" name="name" required className={INPUT_CLASS} placeholder="Dr. John Smith" />
                    </div>
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Email Address</label>
                      <input type="email" name="email" required className={INPUT_CLASS} placeholder="name@example.com" />
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Phone</label>
                        <input type="tel" name="phone" className={INPUT_CLASS} placeholder="+91 00000 00000" />
                      </div>
                      <div>
                        <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">Role</label>
                        <select name="role" required className={INPUT_CLASS + ' cursor-pointer'}>
                          <option value="USER">Patient</option>
                          <option value="RECOVERED_PATIENT">Recovered Patient</option>
                          <option value="HEALTH_MENTOR">Health Mentor</option>
                          <option value="DOCTOR">Doctor</option>
                        </select>
                      </div>
                    </div>
                    <div>
                      <label className="block text-sm font-bold text-gray-700 dark:text-gray-300 mb-2">About (Optional)</label>
                      <textarea name="about" rows={2} className={INPUT_CLASS} placeholder="Tell us a bit about yourself…" />
                    </div>
                    <button
                      type="submit"
                      disabled={loading}
                      className="w-full mt-2 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold py-2.5 rounded-xl shadow-[0_8px_20px_-6px_rgba(37,99,235,0.4)] hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2 group disabled:opacity-60"
                    >
                      {loading ? <Spinner /> : (
                        <>
                          <span>Create Account</span>
                          <i className="fa-solid fa-arrow-right group-hover:translate-x-1 transition-transform" />
                        </>
                      )}
                    </button>
                  </form>
                )}
              </div>
            )}

            {/* OAuth divider (only on form stage) */}
            {stage === 'form' && (
              <div id="oauth-section">
                <div className="relative flex py-4 items-center">
                  <div className="flex-grow border-t border-gray-200 dark:border-gray-800" />
                  <span className="flex-shrink-0 mx-4 text-gray-400 dark:text-gray-500 text-xs font-bold uppercase tracking-wider">or continue with</span>
                  <div className="flex-grow border-t border-gray-200 dark:border-gray-800" />
                </div>
                <a
                  href="/oauth2/authorization/google"
                  className="w-full flex items-center justify-center gap-3 bg-white dark:bg-gray-800/50 border-2 border-gray-200 dark:border-gray-700/50 text-gray-700 dark:text-gray-200 font-bold py-2.5 rounded-xl hover:bg-gray-50 dark:hover:bg-gray-700 hover:border-gray-300 dark:hover:border-gray-600 transition-all shadow-sm group"
                >
                  <svg className="w-5 h-5 group-hover:scale-110 transition-transform" viewBox="0 0 24 24">
                    <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
                    <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
                    <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
                    <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
                  </svg>
                  Sign in with Google
                </a>
              </div>
            )}

          </div>
        </div>
      </div>

      {/* ── Email Already Registered Modal ── */}
      {showRegisteredModal && (
        <div className="fixed inset-0 z-[9999] flex items-center justify-center">
          <div
            className="absolute inset-0 bg-black/60 backdrop-blur-sm"
            onClick={() => setShowRegisteredModal(false)}
          />
          <div
            className="relative bg-white dark:bg-gray-900 rounded-[2rem] shadow-[0_30px_60px_-10px_rgba(0,0,0,0.3)] border border-gray-100 dark:border-white/5 p-8 max-w-sm w-full mx-4 z-10"
            style={{ animation: 'modalPop 0.25s cubic-bezier(0.34,1.56,0.64,1) both' }}
          >
            <div className="w-16 h-16 bg-amber-50 dark:bg-amber-900/20 rounded-2xl flex items-center justify-center text-amber-500 mx-auto mb-5 border border-amber-100 dark:border-amber-800/30">
              <i className="fa-solid fa-user-check text-2xl" />
            </div>
            <h3 className="text-xl font-extrabold text-gray-900 dark:text-white text-center mb-2">Email Already Registered</h3>
            <p className="text-gray-500 dark:text-gray-400 text-sm text-center mb-2">An account with this email address already exists.</p>
            <p className="text-blue-600 dark:text-blue-400 font-bold text-sm text-center mb-6 break-all">{registeredEmail}</p>
            <div className="flex flex-col gap-3">
              <button
                onClick={goToLoginFromModal}
                className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold py-3.5 rounded-xl shadow-[0_8px_20px_-6px_rgba(37,99,235,0.35)] hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2"
              >
                <i className="fa-solid fa-arrow-right-to-bracket" /> Go to Login
              </button>
              <button
                onClick={() => setShowRegisteredModal(false)}
                className="w-full bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 text-gray-700 dark:text-gray-300 font-semibold py-3 rounded-xl transition-all"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      <style>{`
        @keyframes fadeInLeft {
          from { opacity: 0; transform: translateX(-24px); }
          to   { opacity: 1; transform: translateX(0); }
        }
        @keyframes modalPop {
          from { opacity: 0; transform: scale(0.9) translateY(10px); }
          to   { opacity: 1; transform: scale(1) translateY(0); }
        }
      `}</style>
    </div>
  )
}
