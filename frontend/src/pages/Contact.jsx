import React, { useState, useEffect } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { Mail, Phone, MapPin, Clock, Send, CheckCircle, AlertCircle } from 'lucide-react'

export default function Contact() {
  const [form, setForm] = useState({ name: '', email: '', subject: '', message: '' })
  const [status, setStatus] = useState(null) // null | 'sending' | 'success' | 'error'

  useEffect(() => { document.title = 'Contact Us — MedTrackFit' }, [])

  function handleChange(e) {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setStatus('sending')
    try {
      const res = await fetch('/api/contact', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      })
      setStatus(res.ok ? 'success' : 'error')
    } catch {
      setStatus('error')
    }
  }

  const INPUT = 'w-full px-4 py-3.5 rounded-xl border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-900 text-slate-900 dark:text-white focus:bg-white dark:focus:bg-slate-900 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all placeholder-slate-400 font-medium text-sm'

  return (
    <div className="relative overflow-x-hidden min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-800 dark:text-slate-100 transition-colors duration-500">
      
      {/* Background blobs */}
      <div className="absolute top-0 left-1/4 w-[500px] h-[500px] bg-blue-500/5 dark:bg-blue-600/5 rounded-full blur-3xl pointer-events-none -z-10" />

      {/* Hero Section */}
      <section className="pt-20 pb-16 text-center px-6">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto"
        >
          <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest bg-blue-500/10 px-3 py-1.5 rounded-full">
            Get In Touch
          </span>
          <h1 className="mt-6 text-4xl md:text-5xl lg:text-6xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            We'd love to <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">hear from you</span>
          </h1>
          <p className="mt-6 text-lg text-slate-500 dark:text-slate-400 max-w-2xl mx-auto">
            Have a question, feedback, or need support? Our team is here to help.
          </p>
        </motion.div>
      </section>

      {/* Content Section */}
      <section className="py-12 px-6 max-w-6xl mx-auto">
        <div className="grid grid-cols-1 lg:grid-cols-5 gap-12">

          {/* Contact Details Card */}
          <motion.div 
            initial={{ opacity: 0, x: -25 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.5 }}
            className="lg:col-span-2 flex flex-col gap-6"
          >
            <h2 className="text-2xl font-bold text-slate-900 dark:text-white">Contact Information</h2>
            
            {[
              { icon: Mail, title: 'Email', detail: 'medtrackfit.help@gmail.com', color: 'text-blue-500 bg-blue-500/10' },
              { icon: Phone, title: 'Phone', detail: '+91 9934202241', color: 'text-green-500 bg-green-500/10' },
              { icon: MapPin, title: 'Address', detail: 'Noida, Uttar Pradesh', color: 'text-purple-500 bg-purple-500/10' },
              { icon: Clock, title: 'Support Hours', detail: 'Mon–Sat, 9am–6pm IST', color: 'text-pink-500 bg-pink-500/10' },
            ].map((c, idx) => {
              const Icon = c.icon
              return (
                <div key={idx} className="flex items-start gap-4 p-5 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/50 dark:border-slate-800 shadow-sm hover:border-blue-500/20 transition-all">
                  <div className={`w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 ${c.color}`}>
                    <Icon className="w-5 h-5" />
                  </div>
                  <div>
                    <p className="text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-1">{c.title}</p>
                    <p className="text-slate-800 dark:text-slate-200 font-semibold text-sm">{c.detail}</p>
                  </div>
                </div>
              )
            })}

            {/* Social profiles */}
            <div className="flex gap-3 mt-2">
              <a 
                href="https://www.linkedin.com/in/protox1142" 
                target="_blank" 
                rel="noopener noreferrer" 
                className="w-10 h-10 rounded-xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 flex items-center justify-center text-slate-400 hover:text-blue-600 hover:border-blue-500/20 transition-all"
              >
                <i className="fa-brands fa-linkedin text-lg" />
              </a>
              <a 
                href="https://github.com/PROTOX11/MedTrackFit" 
                target="_blank" 
                rel="noopener noreferrer" 
                className="w-10 h-10 rounded-xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 flex items-center justify-center text-slate-400 hover:text-slate-955 dark:hover:text-white hover:border-slate-500/20 transition-all"
              >
                <i className="fa-brands fa-github text-lg" />
              </a>
            </div>
          </motion.div>

          {/* Form Card */}
          <motion.div 
            initial={{ opacity: 0, x: 25 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.5 }}
            className="lg:col-span-3 bg-white dark:bg-slate-900 rounded-3xl border border-slate-200/50 dark:border-slate-800 shadow-lg p-8"
          >
            <AnimatePresence mode="wait">
              {status === 'success' ? (
                <motion.div 
                  key="success"
                  initial={{ opacity: 0, scale: 0.95 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0 }}
                  className="flex flex-col items-center justify-center h-full text-center gap-4 py-12"
                >
                  <div className="w-16 h-16 bg-green-500/10 rounded-2xl flex items-center justify-center text-green-600 dark:text-green-400">
                    <CheckCircle className="w-8 h-8" />
                  </div>
                  <h3 className="text-2xl font-bold text-slate-900 dark:text-white">Message Sent!</h3>
                  <p className="text-slate-500 dark:text-slate-400 text-sm">We'll get back to you within 24 hours.</p>
                  <button 
                    onClick={() => { setStatus(null); setForm({ name: '', email: '', subject: '', message: '' }) }} 
                    className="mt-4 text-blue-600 dark:text-blue-400 font-semibold text-sm hover:underline"
                  >
                    Send another message
                  </button>
                </motion.div>
              ) : (
                <motion.form 
                  key="form"
                  onSubmit={handleSubmit} 
                  className="space-y-5"
                >
                  <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-4">Send us a message</h3>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                      <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-2">Name</label>
                      <input 
                        name="name" 
                        value={form.name} 
                        onChange={handleChange} 
                        required 
                        className={INPUT} 
                        placeholder="Your name" 
                      />
                    </div>
                    <div>
                      <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-2">Email</label>
                      <input 
                        name="email" 
                        type="email" 
                        value={form.email} 
                        onChange={handleChange} 
                        required 
                        className={INPUT} 
                        placeholder="your@email.com" 
                      />
                    </div>
                  </div>
                  <div>
                    <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-2">Subject</label>
                    <input 
                      name="subject" 
                      value={form.subject} 
                      onChange={handleChange} 
                      required 
                      className={INPUT} 
                      placeholder="How can we help?" 
                    />
                  </div>
                  <div>
                    <label className="block text-[10px] font-bold text-slate-400 uppercase tracking-wider mb-2">Message</label>
                    <textarea 
                      name="message" 
                      rows={5} 
                      value={form.message} 
                      onChange={handleChange} 
                      required 
                      className={INPUT} 
                      placeholder="Tell us more about your query…" 
                    />
                  </div>

                  {status === 'error' && (
                    <motion.div 
                      initial={{ opacity: 0, y: 5 }}
                      animate={{ opacity: 1, y: 0 }}
                      className="flex items-center gap-2 text-sm text-red-600 dark:text-red-400 font-medium"
                    >
                      <AlertCircle className="w-4 h-4" />
                      <span>Something went wrong. Please try again or email us directly.</span>
                    </motion.div>
                  )}

                  <button
                    type="submit"
                    disabled={status === 'sending'}
                    className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold py-4 rounded-xl shadow-md hover:-translate-y-0.5 transition-all flex items-center justify-center gap-2 disabled:opacity-60"
                  >
                    {status === 'sending' ? (
                      <>
                        <motion.span 
                          animate={{ rotate: 360 }}
                          transition={{ repeat: Infinity, duration: 1, ease: 'linear' }}
                          className="w-4 h-4 border-2 border-white border-t-transparent rounded-full"
                        />
                        <span>Sending…</span>
                      </>
                    ) : (
                      <>
                        <Send className="w-4 h-4" />
                        <span>Send Message</span>
                      </>
                    )}
                  </button>
                </motion.form>
              )}
            </AnimatePresence>
          </motion.div>

        </div>
      </section>
    </div>
  )
}
