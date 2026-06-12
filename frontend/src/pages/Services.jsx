import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Heart, Activity, ShieldAlert, Sparkles, MessageSquare, Smartphone, Check, ArrowRight } from 'lucide-react'

const SERVICES = [
  {
    icon: Heart,
    color: 'blue',
    title: 'Peer Mentorship',
    price: 'Free',
    desc: 'Get matched with a recovered patient who has overcome your exact condition. Build a genuine connection with someone who truly understands.',
    points: ['Condition-based matching', 'Unlimited messaging', 'Anonymous option available', '1-on-1 or group sessions'],
  },
  {
    icon: ShieldAlert,
    color: 'indigo',
    title: 'Doctor Consultations',
    price: 'Verified',
    desc: 'Browse verified doctors and health mentors. Get your recovery plan reviewed and approved by a licensed medical professional.',
    points: ['Doctor-verified plans', 'Prescription advice', 'Medication tracking', 'Follow-up consultations'],
    featured: true,
  },
  {
    icon: Activity,
    color: 'purple',
    title: 'Wellness Tracking',
    price: 'Included',
    desc: 'Track nutrition, hydration, meditation, and activity. Visualize your progress and celebrate milestones with your support network.',
    points: ['Food diary & macro tracking', 'Hydration goals', 'Meditation scores', 'Progress charts & analytics'],
  },
  {
    icon: Sparkles,
    color: 'rose',
    title: 'Community Blog',
    price: 'Free',
    desc: 'Read and publish recovery stories, medical tips, and community insights. Learn from thousands who have walked similar paths.',
    points: ['Publish your story', 'Follow topics & authors', 'Doctor-verified articles', 'Comment & discuss'],
  },
  {
    icon: MessageSquare,
    color: 'green',
    title: 'Secure Messaging',
    price: 'Included',
    desc: 'Chat with your entire care team in one place. Messages are encrypted and your health data is always private.',
    points: ['End-to-end privacy', 'File & image sharing', 'Group channels', 'Read receipts'],
  },
  {
    icon: Smartphone,
    color: 'amber',
    title: 'Mobile-Ready',
    price: 'Included',
    desc: 'Access MedTrackFit from any device. The responsive design ensures a seamless experience on phones, tablets, and desktops.',
    points: ['Responsive web app', 'Offline mode (coming soon)', 'Push notifications (coming soon)', 'Dark mode support'],
  },
]

export default function Services() {
  useEffect(() => { document.title = 'Services — MedTrackFit' }, [])

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
      <section className="pt-20 pb-16 text-center px-6 relative overflow-hidden">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto"
        >
          <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest bg-blue-500/10 px-3 py-1.5 rounded-full">
            What We Offer
          </span>
          <h1 className="mt-6 text-4xl md:text-5xl lg:text-6xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            Services built for <br />
            <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">every recovery stage</span>
          </h1>
          <p className="mt-6 text-lg text-slate-500 dark:text-slate-400 max-w-2xl mx-auto">
            From peer support to doctor-verified plans — MedTrackFit offers a complete ecosystem for your health journey.
          </p>
        </motion.div>
      </section>

      {/* Services Grid */}
      <section className="py-16 px-6">
        <div className="max-w-7xl mx-auto">
          <motion.div
            variants={containerVariants}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
          >
            {SERVICES.map((s, idx) => {
              const Icon = s.icon
              return (
                <motion.div
                  key={idx}
                  variants={cardVariants}
                  className={`relative rounded-3xl p-8 border flex flex-col justify-between hover:-translate-y-1 transition-all duration-300 group min-h-[460px] ${
                    s.featured
                      ? 'bg-gradient-to-br from-indigo-600 to-blue-700 border-indigo-500 text-white shadow-xl'
                      : 'bg-white dark:bg-slate-900 border-slate-200/60 dark:border-slate-800/80 shadow-sm hover:shadow-md'
                  }`}
                >
                  {s.featured && (
                    <span className="absolute top-6 right-6 text-[10px] font-bold bg-white/20 text-white px-3 py-1 rounded-full uppercase tracking-wider">
                      Most Popular
                    </span>
                  )}
                  
                  <div>
                    <div className={`w-12 h-12 rounded-xl flex items-center justify-center mb-6 ${
                      s.featured ? 'bg-white/20 text-white' : 'bg-blue-500/10 text-blue-600 dark:text-blue-400'
                    }`}>
                      <Icon className="w-6 h-6" />
                    </div>

                    <div className="flex items-baseline gap-2 mb-4">
                      <h3 className="text-xl font-bold">{s.title}</h3>
                      <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full uppercase ${
                        s.featured ? 'bg-white/20 text-white' : 'bg-blue-500/10 text-blue-600 dark:text-blue-400'
                      }`}>
                        {s.price}
                      </span>
                    </div>

                    <p className={`text-sm leading-relaxed mb-6 ${s.featured ? 'text-blue-100' : 'text-slate-500 dark:text-slate-400'}`}>
                      {s.desc}
                    </p>
                  </div>

                  <div>
                    <ul className="flex flex-col gap-3 mb-8">
                      {s.points.map((pt, pIdx) => (
                        <li key={pIdx} className="flex items-center gap-2.5 text-sm">
                          <Check className={`w-4 h-4 flex-shrink-0 ${s.featured ? 'text-blue-200' : 'text-blue-600 dark:text-blue-400'}`} />
                          <span className={s.featured ? 'text-blue-100' : 'text-slate-600 dark:text-slate-300'}>{pt}</span>
                        </li>
                      ))}
                    </ul>

                    <Link
                      to="/auth"
                      className={`w-full inline-flex items-center justify-center gap-2 font-bold py-3.5 px-6 rounded-xl transition-all text-sm ${
                        s.featured
                          ? 'bg-white text-indigo-700 hover:bg-slate-100 shadow-md hover:-translate-y-0.5'
                          : 'border border-slate-200 dark:border-slate-800 text-slate-700 dark:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800'
                      }`}
                    >
                      <span>Get Started</span>
                      <ArrowRight className="w-4 h-4" />
                    </Link>
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
          <h2 className="text-3xl md:text-4xl font-extrabold mb-4">Start your free account today</h2>
          <p className="text-slate-400 text-base mb-8">No credit card. No commitment. Just healing.</p>
          <Link to="/auth" className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold px-10 py-4 rounded-xl shadow-md hover:-translate-y-0.5 transition-all text-base group">
            Create Free Account
            <ArrowRight className="w-4 h-4 group-hover:translate-x-0.5 transition-transform" />
          </Link>
        </div>
      </section>
    </div>
  )
}
