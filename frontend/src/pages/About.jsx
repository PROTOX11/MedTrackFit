import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Heart, ShieldCheck, Users, Activity, Check, ArrowRight } from 'lucide-react'

const TEAM = [
  { name: 'Dr. Arjun Mehta', role: 'Chief Medical Officer', specialty: 'Internal Medicine', img: 'https://randomuser.me/api/portraits/men/32.jpg' },
  { name: 'Priya Nair', role: 'Head of Community', specialty: 'Mental Health Advocate', img: 'https://randomuser.me/api/portraits/women/44.jpg' },
  { name: 'Siddharth Rao', role: 'CTO', specialty: 'Health Technology', img: 'https://randomuser.me/api/portraits/men/55.jpg' },
  { name: 'Dr. Kavya Pillai', role: 'Wellness Director', specialty: 'Holistic Recovery', img: 'https://randomuser.me/api/portraits/women/62.jpg' },
]

const VALUES = [
  { icon: Heart, color: 'rose', title: 'Empathy First', desc: 'We believe healing starts with being heard and understood.' },
  { icon: ShieldCheck, color: 'green', title: 'Trust & Safety', desc: 'All mentors and doctors are verified for your peace of mind.' },
  { icon: Users, color: 'blue', title: 'Community Power', desc: 'Real recovery stories from people who have walked your path.' },
  { icon: Activity, color: 'purple', title: 'Evidence-Based', desc: 'Every plan is grounded in scientific research and clinical expertise.' },
]

export default function About() {
  useEffect(() => { document.title = 'About Us — MedTrackFit' }, [])

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.1, delayChildren: 0.1 }
    }
  }

  const cardVariants = {
    hidden: { opacity: 0, y: 15 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { type: 'spring', stiffness: 100, damping: 15 }
    }
  }

  return (
    <div className="relative overflow-x-hidden min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-800 dark:text-slate-100 transition-colors duration-500">
      {/* Background blobs */}
      <div className="absolute top-0 right-1/4 w-[500px] h-[500px] bg-blue-500/5 dark:bg-blue-600/5 rounded-full blur-3xl pointer-events-none -z-10" />

      {/* Hero Section */}
      <section className="pt-20 pb-20 px-6 max-w-4xl mx-auto text-center">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, ease: 'easeOut' }}
        >
          <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest bg-blue-500/10 px-3 py-1.5 rounded-full">
            Our Story & Mission
          </span>
          <h1 className="mt-6 text-4xl md:text-5xl lg:text-6xl font-extrabold text-slate-900 dark:text-white tracking-tight leading-tight">
            Built for the <br />
            <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Human Side</span> of Healing
          </h1>
          <p className="mt-6 text-lg text-slate-500 dark:text-slate-400 leading-relaxed">
            MedTrackFit was born from a simple belief: that the most powerful medicine is connecting with someone who has already successfully navigated and overcome what you're currently facing.
          </p>
        </motion.div>
      </section>

      {/* Mission details */}
      <section className="py-20 bg-white dark:bg-slate-900/40 border-y border-slate-200/50 dark:border-slate-800/50">
        <div className="max-w-7xl mx-auto px-6 grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">
          <motion.div
            initial={{ opacity: 0, x: -30 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.5 }}
          >
            <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest">Our Mission</span>
            <h2 className="mt-3 text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">
              Transforming health journeys through shared experience
            </h2>
            <p className="mt-4 text-slate-500 dark:text-slate-400 leading-relaxed text-sm">
              Traditional healthcare focuses primarily on diagnosis and clinical treatments. We focus on daily recovery — the messy, emotional, day-to-day journey that clinicians don't always have time to address. We connect patients with mentors who have been exactly where they are.
            </p>
            <p className="mt-4 text-slate-500 dark:text-slate-400 leading-relaxed text-sm">
              By combining doctor verification with active support networks, MedTrackFit gives you the complete blueprint: professional medical validation matched with real-world support.
            </p>
            <div className="mt-8 flex gap-4">
              <Link to="/auth" className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold px-6 py-3 rounded-xl shadow-md hover:-translate-y-0.5 transition-all text-sm">
                Join Us
                <ArrowRight className="w-4 h-4" />
              </Link>
              <Link to="/features" className="inline-flex items-center gap-2 border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300 font-bold px-6 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-all text-sm">
                Learn More
              </Link>
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            whileInView={{ opacity: 1, scale: 1 }}
            viewport={{ once: true }}
            transition={{ duration: 0.5 }}
            className="grid grid-cols-2 gap-4"
          >
            {[
              { value: '10k+', label: 'Active Members', color: 'text-blue-500 bg-blue-500/10' },
              { value: '92%', label: 'Recovery Success Rate', color: 'text-green-500 bg-green-500/10' },
              { value: '500+', label: 'Verified Experts', color: 'text-purple-500 bg-purple-500/10' },
              { value: '80+', label: 'Conditions Supported', color: 'text-pink-500 bg-pink-500/10' },
            ].map((s, idx) => (
              <div key={idx} className="bg-slate-50 dark:bg-slate-900/80 rounded-2xl p-6 border border-slate-200/50 dark:border-slate-800 flex flex-col gap-2">
                <span className={`w-8 h-8 rounded-lg flex items-center justify-center font-bold text-sm ${s.color}`}>✓</span>
                <p className="text-2xl font-extrabold text-slate-900 dark:text-white">{s.value}</p>
                <p className="text-xs text-slate-400 font-semibold uppercase tracking-wider">{s.label}</p>
              </div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Core Values grid */}
      <section className="py-24 bg-slate-50 dark:bg-slate-950/20">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-16">
            <span className="text-sm font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest">Our Beliefs</span>
            <h2 className="mt-3 text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">Our Core Values</h2>
          </div>

          <motion.div
            variants={containerVariants}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
          >
            {VALUES.map((v, idx) => {
              const Icon = v.icon
              return (
                <motion.div
                  key={idx}
                  variants={cardVariants}
                  className="bg-white dark:bg-slate-900 rounded-2xl p-8 border border-slate-200/60 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-all group"
                >
                  <div className="w-12 h-12 rounded-xl bg-blue-500/10 flex items-center justify-center mb-6 text-blue-600 dark:text-blue-400">
                    <Icon className="w-6 h-6" />
                  </div>
                  <h3 className="text-lg font-bold text-slate-900 dark:text-white mb-2">{v.title}</h3>
                  <p className="text-slate-500 dark:text-slate-400 text-sm leading-relaxed">{v.desc}</p>
                </motion.div>
              )
            })}
          </motion.div>
        </div>
      </section>

      {/* Leadership team section */}
      <section className="py-24 bg-white dark:bg-slate-900/40">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-16">
            <span className="text-sm font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest">Leadership</span>
            <h2 className="mt-3 text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">The Team Behind MedTrackFit</h2>
          </div>

          <motion.div
            variants={containerVariants}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true }}
            className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8"
          >
            {TEAM.map((t, idx) => (
              <motion.div
                key={idx}
                variants={cardVariants}
                className="flex flex-col items-center text-center group bg-slate-50 dark:bg-slate-900/30 p-6 rounded-3xl border border-slate-200/50 dark:border-slate-800/60 shadow-sm"
              >
                <div className="relative mb-5">
                  <img src={t.img} alt={t.name} className="w-24 h-24 rounded-2xl object-cover shadow-md group-hover:scale-105 transition-transform duration-300" />
                  <div className="absolute -bottom-2 -right-2 w-8 h-8 bg-blue-600 rounded-xl flex items-center justify-center text-white shadow">
                    <Check className="w-4 h-4" />
                  </div>
                </div>
                <h3 className="font-bold text-slate-900 dark:text-white text-base">{t.name}</h3>
                <p className="text-blue-600 dark:text-blue-400 text-sm font-semibold">{t.role}</p>
                <p className="text-slate-400 text-xs mt-1 font-medium">{t.specialty}</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* CTA section */}
      <section className="py-24 bg-gradient-to-br from-blue-600 to-indigo-700 text-white text-center">
        <div className="max-w-3xl mx-auto px-6">
          <h2 className="text-3xl md:text-4xl font-extrabold mb-4">Ready to start your journey?</h2>
          <p className="text-blue-100 mb-8 text-base">Join thousands finding support, guidance and hope on MedTrackFit.</p>
          <Link to="/auth" className="inline-flex items-center gap-2 bg-white text-blue-700 font-bold px-10 py-4 rounded-xl shadow-lg hover:bg-slate-100 transition-all text-base">
            Get Started Free
            <ArrowRight className="w-4 h-4" />
          </Link>
        </div>
      </section>
    </div>
  )
}
