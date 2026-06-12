import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Users, MessageSquare, Utensils, Brain, Droplets, ShieldAlert, BookOpen, Activity, Lock, ArrowRight } from 'lucide-react'

const FEATURES = [
  {
    icon: Users,
    color: 'blue',
    title: 'Peer Mentor Matching',
    desc: 'Get matched with recovered patients who have overcome your exact condition based on medical history, location, and personality.',
    tag: 'Community',
  },
  {
    icon: MessageSquare,
    color: 'indigo',
    title: 'Real-Time Messaging',
    desc: 'Chat securely with mentors, doctors, and fellow patients. Share files, images, and voice messages in a HIPAA-aware environment.',
    tag: 'Communication',
  },
  {
    icon: Utensils,
    color: 'green',
    title: 'Food & Nutrition Log',
    desc: 'Search from thousands of foods, track macros, and log daily meals. Get personalized nutrition insights based on your condition.',
    tag: 'Tracking',
  },
  {
    icon: Brain,
    color: 'purple',
    title: 'Meditation & Mindfulness',
    desc: 'Guided breathing exercises, meditation timers, and mindfulness scores help you build mental resilience alongside physical recovery.',
    tag: 'Wellness',
  },
  {
    icon: Droplets,
    color: 'cyan',
    title: 'Hydration Tracking',
    desc: 'Set daily water goals and log intake throughout the day. Track hydration scores and streaks to build healthy habits.',
    tag: 'Tracking',
  },
  {
    icon: ShieldAlert,
    color: 'emerald',
    title: 'Doctor-Verified Plans',
    desc: 'All recovery plans are reviewed and approved by licensed medical professionals before being made available to patients.',
    tag: 'Safety',
  },
  {
    icon: BookOpen,
    color: 'rose',
    title: 'Health Blog & Stories',
    desc: 'Read and publish recovery stories, medical tips, and community insights. Doctors and mentors share their expertise freely.',
    tag: 'Knowledge',
  },
  {
    icon: Activity,
    color: 'amber',
    title: 'Progress Analytics',
    desc: 'Visualize your recovery journey with beautiful charts. Track performance scores across meditation, hydration, food, and activity.',
    tag: 'Analytics',
  },
  {
    icon: Lock,
    color: 'violet',
    title: 'OTP Email Authentication',
    desc: 'No passwords to remember or forget. Log in securely with a 6-digit one-time code sent directly to your email address.',
    tag: 'Security',
  },
]

const TAG_COLORS = {
  Community: 'blue', Communication: 'indigo', Tracking: 'green', Wellness: 'purple',
  Safety: 'emerald', Knowledge: 'rose', Analytics: 'amber', Security: 'violet',
}

export default function Features() {
  useEffect(() => { document.title = 'Features — MedTrackFit' }, [])

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
      <div className="absolute top-0 left-1/4 w-[500px] h-[500px] bg-blue-500/5 dark:bg-blue-600/5 rounded-full blur-3xl pointer-events-none -z-10" />

      {/* Hero Section */}
      <section className="pt-20 pb-16 text-center px-6 relative overflow-hidden">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto"
        >
          <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest bg-blue-500/10 px-3 py-1.5 rounded-full">
            Platform Features
          </span>
          <h1 className="mt-6 text-4xl md:text-5xl lg:text-6xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            Everything you need <br />
            <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">to heal &amp; thrive</span>
          </h1>
          <p className="mt-6 text-lg text-slate-500 dark:text-slate-400 max-w-2xl mx-auto">
            A comprehensive suite of tools designed to support every aspect of your recovery journey — physical, mental, and social.
          </p>
          <div className="mt-8 flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/auth" className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold px-8 py-3.5 rounded-xl shadow-md hover:-translate-y-0.5 transition-all text-sm group">
              Try It Free
              <ArrowRight className="w-4 h-4 group-hover:translate-x-0.5 transition-transform" />
            </Link>
            <Link to="/services" className="inline-flex items-center gap-2 border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300 font-bold px-8 py-3.5 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 transition-all text-sm">
              View Services
            </Link>
          </div>
        </motion.div>
      </section>

      {/* Features grid */}
      <section className="py-16 px-6">
        <div className="max-w-7xl mx-auto">
          <motion.div
            variants={containerVariants}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
          >
            {FEATURES.map((f, idx) => {
              const Icon = f.icon
              const tagColor = TAG_COLORS[f.tag] || 'blue'
              return (
                <motion.div
                  key={idx}
                  variants={cardVariants}
                  className="bg-white dark:bg-slate-900 rounded-3xl p-8 border border-slate-200/60 dark:border-slate-800/80 shadow-sm hover:shadow-md transition-all duration-300 group flex flex-col justify-between min-h-[260px]"
                >
                  <div>
                    <div className="flex items-center justify-between mb-6">
                      <div className="w-12 h-12 rounded-xl bg-blue-500/10 flex items-center justify-center text-blue-600 dark:text-blue-400 group-hover:scale-105 transition-transform">
                        <Icon className="w-6 h-6" />
                      </div>
                      <span className="text-[10px] font-bold text-blue-600 dark:text-blue-400 bg-blue-500/10 px-2.5 py-1 rounded-full uppercase tracking-wider">
                        {f.tag}
                      </span>
                    </div>
                    <h3 className="text-lg font-bold text-slate-900 dark:text-white mb-2">{f.title}</h3>
                    <p className="text-slate-500 dark:text-slate-400 text-sm leading-relaxed">{f.desc}</p>
                  </div>
                </motion.div>
              )
            })}
          </motion.div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-slate-950 text-white text-center px-6">
        <div className="max-w-3xl mx-auto">
          <h2 className="text-3xl md:text-4xl font-extrabold mb-4">All features. No subscription required.</h2>
          <p className="text-slate-400 mb-8 text-base">MedTrackFit is free to join. Start your recovery journey today.</p>
          <Link to="/auth" className="inline-flex items-center gap-2 bg-white text-slate-900 font-bold px-10 py-4 rounded-xl shadow-lg hover:bg-slate-100 transition-all text-base">
            Create Free Account
            <ArrowRight className="w-4 h-4 text-slate-950" />
          </Link>
        </div>
      </section>
    </div>
  )
}
