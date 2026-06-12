import React, { useRef } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Users, Activity, Heart, ShieldAlert, Sparkles, CheckCircle, ArrowRight, Star } from 'lucide-react'
import CountUp from '../components/CountUp'

const STATS = [
  { label: 'Active Patients', value: '10000', icon: Users, color: 'blue', suffix: '+' },
  { label: 'Recovery Rate', value: '92', icon: Activity, color: 'green', suffix: '%' },
  { label: 'Verified Experts', value: '500', icon: CheckCircle, color: 'indigo', suffix: '+' },
  { label: 'Medical Conditions', value: '80', icon: Heart, color: 'pink', suffix: '+' },
]

const CONDITIONS = [
  'Diabetes',
  'Chronic Back Pain',
  'Mental Health',
  'Heart Conditions',
  'Cancer Recovery',
  'Arthritis',
  'Post-Surgery',
]

const HOW_IT_WORKS = [
  { step: 1, icon: Users, title: 'Create Account', desc: 'Sign up securely in seconds via email OTP — no passwords needed', color: 'from-blue-500 to-indigo-500' },
  { step: 2, icon: Sparkles, title: 'Find Mentors', desc: 'Browse recovered patients & health mentors for your specific condition', color: 'from-indigo-500 to-purple-500' },
  { step: 3, icon: Activity, title: 'Connect & Chat', desc: 'Message, get advice, and follow doctor-verified recovery plans', color: 'from-purple-500 to-pink-500' },
  { step: 4, icon: Heart, title: 'Track & Recover', desc: 'Monitor your progress and celebrate milestones with your community', color: 'from-pink-500 to-rose-500' },
]

const TESTIMONIALS = [
  {
    quote: 'After years of expensive treatments, connecting with someone who had overcome the same back issues changed everything. More valuable than all the doctor visits combined.',
    name: 'Priya Sharma',
    role: 'Recovered from Chronic Back Pain',
    avatar: 'https://randomuser.me/api/portraits/women/44.jpg',
    rating: 5,
    featured: false,
  },
  {
    quote: "When I was diagnosed with diabetes, I felt lost. My MedTrackFit mentor showed me how to actually live with this condition day-to-day. Doctors told me what to do, but my mentor showed me how.",
    name: 'Rahul Verma',
    role: 'Diabetes Management',
    avatar: 'https://randomuser.me/api/portraits/men/67.jpg',
    rating: 5,
    featured: true,
  },
  {
    quote: 'Finding someone who had overcome anxiety and depression was life-changing. My mentor provided practical coping strategies that actually worked for me.',
    name: 'Ananya Desai',
    role: 'Mental Health Recovery',
    avatar: 'https://randomuser.me/api/portraits/women/28.jpg',
    rating: 5,
    featured: false,
  },
]

function StarRating({ rating }) {
  return (
    <div className="flex gap-0.5 mb-4 text-amber-400">
      {[...Array(5)].map((_, i) => (
        <Star key={i} className="w-4 h-4 fill-current" />
      ))}
    </div>
  )
}

export default function Home() {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.1, delayChildren: 0.1 },
    },
  }

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: {
      y: 0,
      opacity: 1,
      transition: { type: 'spring', stiffness: 100, damping: 15 },
    },
  }

  return (
    <div className="relative overflow-x-hidden min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-800 dark:text-slate-100 transition-colors duration-500">
      {/* SaaS Background Radial Glows */}
      <div className="absolute top-0 left-1/4 w-[500px] h-[500px] bg-blue-500/10 dark:bg-blue-600/5 rounded-full blur-3xl pointer-events-none -z-10" />
      <div className="absolute top-[20%] right-1/4 w-[600px] h-[600px] bg-indigo-500/10 dark:bg-indigo-600/5 rounded-full blur-3xl pointer-events-none -z-10" />

      {/* Hero Section */}
      <section className="relative pt-20 pb-24 md:pt-32 md:pb-32 px-6 max-w-7xl mx-auto">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
          {/* Left Text Content */}
          <motion.div 
            initial={{ opacity: 0, x: -30 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
            className="lg:col-span-7 flex flex-col gap-6"
          >
            <div className="inline-flex items-center gap-2 px-3 py-1.5 w-fit rounded-full bg-blue-500/10 border border-blue-500/20 text-blue-600 dark:text-blue-400 text-xs font-bold tracking-wider uppercase">
              <Sparkles className="w-3.5 h-3.5 animate-pulse" />
              <span>EMPATHY & CLINICAL SUPPORT COMBINED</span>
            </div>

            <h1 className="text-4xl md:text-5xl lg:text-[4rem] font-extrabold leading-[1.05] tracking-tight text-slate-900 dark:text-white">
              Your Health,<br />
              <span className="bg-gradient-to-r from-blue-600 via-indigo-500 to-indigo-600 bg-clip-text text-transparent">
                Your Support
              </span>{' '}System.
            </h1>

            <p className="text-lg text-slate-500 dark:text-slate-400 max-w-lg leading-relaxed">
              Connect with patients who have successfully recovered from your condition. Access doctor-verified plans and join a community designed around real lived experiences.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 mt-2">
              <Link
                to="/auth"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-2xl shadow-[0_8px_20px_rgba(37,99,235,0.25)] hover:shadow-[0_12px_24px_rgba(37,99,235,0.35)] hover:-translate-y-0.5 transition-all text-base group"
              >
                <span>Get Started Free</span>
                <ArrowRight className="w-4 h-4 group-hover:translate-x-0.5 transition-transform" />
              </Link>
              <Link
                to="/features"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 border border-slate-200 dark:border-slate-800 text-slate-700 dark:text-slate-300 font-bold rounded-2xl bg-white dark:bg-slate-900 hover:bg-slate-100 dark:hover:bg-slate-800 hover:-translate-y-0.5 transition-all text-base"
              >
                See How It Works
              </Link>
            </div>

            {/* Social proof */}
            <div className="flex items-center gap-4 pt-4 border-t border-slate-200 dark:border-slate-900 mt-4">
              <div className="flex -space-x-2.5">
                {['women/44', 'men/67', 'women/28'].map((p, i) => (
                  <img
                    key={i}
                    src={`https://randomuser.me/api/portraits/${p}.jpg`}
                    className="w-9 h-9 rounded-full border-2 border-white dark:border-slate-950 object-cover"
                    alt="User Profile"
                  />
                ))}
                <div className="w-9 h-9 rounded-full border-2 border-white dark:border-slate-950 bg-blue-600 flex items-center justify-center text-white text-xs font-bold shadow-md">+</div>
              </div>
              <div>
                <div className="flex text-amber-500 gap-0.5">
                  {[...Array(5)].map((_, i) => <Star key={i} className="w-3.5 h-3.5 fill-current" />)}
                </div>
                <p className="text-xs text-slate-500 dark:text-slate-400 font-semibold mt-1">4.9/5 stars from 2,400+ recovery stories</p>
              </div>
            </div>
          </motion.div>

          {/* Right Floating Cards Widget */}
          <motion.div 
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.7, ease: [0.16, 1, 0.3, 1], delay: 0.2 }}
            className="hidden lg:block lg:col-span-5 relative w-full aspect-square max-w-[460px] ml-auto"
          >
            <div className="absolute inset-0 bg-gradient-to-tr from-blue-500/10 to-indigo-500/10 rounded-[2.5rem] blur-3xl -z-10" />
            <div className="relative rounded-[2.5rem] border border-slate-200 dark:border-slate-800 overflow-hidden shadow-[0_20px_50px_rgba(0,0,0,0.15)] dark:shadow-[0_20px_50px_rgba(0,0,0,0.4)] aspect-square bg-white dark:bg-slate-900">
              <img
                src="https://images.pexels.com/photos/7089401/pexels-photo-7089401.jpeg?auto=compress&cs=tinysrgb&w=800&h=800&dpr=2"
                alt="Doctor Consultation"
                className="w-full h-full object-cover transition-transform duration-700 hover:scale-[1.02]"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-slate-950/40 via-transparent to-transparent" />
            </div>

            {/* Floating widget 1 */}
            <motion.div 
              animate={{ y: [0, -6, 0] }}
              transition={{ repeat: Infinity, duration: 4, ease: 'easeInOut' }}
              className="absolute -left-12 top-1/4 bg-white/90 dark:bg-slate-900/90 backdrop-blur-xl border border-slate-200 dark:border-slate-800/80 shadow-2xl p-4 rounded-2xl flex items-center gap-3.5"
            >
              <div className="w-10 h-10 bg-green-500/10 rounded-xl flex items-center justify-center">
                <CheckCircle className="w-5 h-5 text-green-600 dark:text-green-400" />
              </div>
              <div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Success Rate</p>
                <p className="text-lg font-extrabold text-slate-900 dark:text-white">92%</p>
              </div>
            </motion.div>

            {/* Floating widget 2 */}
            <motion.div 
              animate={{ y: [0, 6, 0] }}
              transition={{ repeat: Infinity, duration: 4, ease: 'easeInOut', delay: 1 }}
              className="absolute -right-8 bottom-1/4 bg-white/90 dark:bg-slate-900/90 backdrop-blur-xl border border-slate-200 dark:border-slate-800/80 shadow-2xl p-4 rounded-2xl flex items-center gap-3.5"
            >
              <div className="w-10 h-10 bg-blue-500/10 rounded-xl flex items-center justify-center">
                <Users className="w-5 h-5 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Verified Doctors</p>
                <p className="text-lg font-extrabold text-slate-900 dark:text-white">500+</p>
              </div>
            </motion.div>
          </motion.div>
        </div>
      </section>

      {/* Statistics section */}
      <section className="py-16 bg-white dark:bg-slate-900/40 border-y border-slate-200/50 dark:border-slate-800/50">
        <div className="max-w-7xl mx-auto px-6 grid grid-cols-2 md:grid-cols-4 gap-8">
          {STATS.map((s, i) => {
            const Icon = s.icon
            return (
              <motion.div 
                key={s.label}
                initial={{ opacity: 0, y: 15 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true, margin: '-50px' }}
                transition={{ duration: 0.5, delay: i * 0.1 }}
                className="flex flex-col items-center text-center gap-2"
              >
                <div className={`w-12 h-12 rounded-2xl bg-blue-500/10 flex items-center justify-center text-blue-600 dark:text-blue-400 mb-2`}>
                  <Icon className="w-6 h-6" />
                </div>
                <h3 className="text-3xl font-extrabold text-slate-900 dark:text-white">
                  <CountUp end={s.value} suffix={s.suffix} />
                </h3>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">{s.label}</p>
              </motion.div>
            )
          })}
        </div>
      </section>

      {/* Conditions Horizontal list */}
      <section className="py-10 bg-slate-50 dark:bg-slate-950/20">
        <div className="max-w-7xl mx-auto px-6 text-center">
          <p className="text-xs font-bold text-slate-400 uppercase tracking-widest mb-6">Connecting individuals managing</p>
          <div className="flex flex-wrap justify-center gap-2.5">
            {CONDITIONS.map((c, i) => (
              <motion.span 
                key={c}
                initial={{ opacity: 0, scale: 0.95 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true }}
                transition={{ duration: 0.3, delay: i * 0.05 }}
                className="px-5 py-2 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300 text-sm font-semibold shadow-sm hover:border-blue-500/30 dark:hover:border-blue-500/30 transition-all cursor-default"
              >
                {c}
              </motion.span>
            ))}
          </div>
        </div>
      </section>

      {/* How it works */}
      <section className="py-24 bg-white dark:bg-transparent">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-20">
            <span className="text-sm font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest">Simple Workflow</span>
            <h2 className="mt-3 text-3xl md:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">How MedTrackFit Works</h2>
            <p className="mt-4 text-base text-slate-500 dark:text-slate-400 max-w-xl mx-auto">Get connected, verify plans, and access structured recovery tools.</p>
          </div>

          <motion.div 
            variants={containerVariants}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-80px' }}
            className="grid grid-cols-1 md:grid-cols-4 gap-8 relative"
          >
            <div className="hidden md:block absolute top-10 left-[12.5%] right-[12.5%] h-0.5 bg-gradient-to-r from-blue-200 via-indigo-200 to-purple-200 dark:from-slate-800/80 dark:to-slate-800/80 -z-10" />
            {HOW_IT_WORKS.map((s) => {
              const Icon = s.icon
              return (
                <motion.div 
                  key={s.step} 
                  variants={itemVariants}
                  className="flex flex-col items-center text-center gap-4 group"
                >
                  <div className={`w-20 h-20 rounded-[2rem] bg-gradient-to-br ${s.color} flex items-center justify-center shadow-lg hover:scale-105 transition-transform duration-300`}>
                    <Icon className="w-8 h-8 text-white" />
                  </div>
                  <span className="text-xs font-bold text-blue-600 dark:text-blue-400 uppercase tracking-wider">Step {s.step}</span>
                  <h3 className="text-lg font-bold text-slate-900 dark:text-white">{s.title}</h3>
                  <p className="text-sm text-slate-500 dark:text-slate-400 leading-relaxed max-w-xs">{s.desc}</p>
                </motion.div>
              )
            })}
          </motion.div>
        </div>
      </section>

      {/* Bento grid features */}
      <section className="py-24 bg-slate-50 dark:bg-slate-900/30 border-y border-slate-200/50 dark:border-slate-800/50">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-20">
            <span className="text-sm font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest">Ecosystem Benefits</span>
            <h2 className="mt-3 text-3xl md:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">Why Choose MedTrackFit?</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {/* Bento card 1 */}
            <motion.div 
              initial={{ opacity: 0, y: 15 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5 }}
              className="md:col-span-2 bg-gradient-to-br from-blue-600 to-indigo-700 rounded-3xl p-10 text-white flex flex-col justify-between min-h-[300px] shadow-lg hover:shadow-2xl transition-all duration-300 group"
            >
              <div className="w-14 h-14 bg-white/10 rounded-2xl flex items-center justify-center group-hover:scale-105 transition-transform">
                <Users className="w-7 h-7 text-white" />
              </div>
              <div>
                <h3 className="text-2xl font-bold mb-2">Real-World Peer Connections</h3>
                <p className="text-blue-100 leading-relaxed text-sm max-w-xl">
                  Get directly connected with mentors who have fully recovered from your specific condition. Real guidance from people with lived experience.
                </p>
              </div>
            </motion.div>

            {/* Bento card 2 */}
            <motion.div 
              initial={{ opacity: 0, y: 15 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: 0.1 }}
              className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800/80 rounded-3xl p-8 flex flex-col justify-between min-h-[300px] shadow-sm hover:shadow-xl transition-all duration-300 group"
            >
              <div className="w-14 h-14 bg-green-500/10 rounded-2xl flex items-center justify-center text-green-600 dark:text-green-400 group-hover:scale-105 transition-transform">
                <CheckCircle className="w-7 h-7" />
              </div>
              <div>
                <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">Doctor-Verified Plans</h3>
                <p className="text-slate-500 dark:text-slate-400 text-sm leading-relaxed">
                  Every recovery blueprint and routine can be submitted to verified medical professionals on the platform for strict checking.
                </p>
              </div>
            </motion.div>

            {/* Bento card 3 */}
            <motion.div 
              initial={{ opacity: 0, y: 15 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5 }}
              className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800/80 rounded-3xl p-8 flex flex-col justify-between min-h-[300px] shadow-sm hover:shadow-xl transition-all duration-300 group"
            >
              <div className="w-14 h-14 bg-purple-500/10 rounded-2xl flex items-center justify-center text-purple-600 dark:text-purple-400 group-hover:scale-105 transition-transform">
                <ShieldAlert className="w-7 h-7" />
              </div>
              <div>
                <h3 className="text-xl font-bold text-slate-900 dark:text-white mb-2">Secure OTP Login</h3>
                <p className="text-slate-500 dark:text-slate-400 text-sm leading-relaxed">
                  No usernames or insecure passwords required. Get access to your portal instantly using a secure email verification OTP.
                </p>
              </div>
            </motion.div>

            {/* Bento card 4 */}
            <motion.div 
              initial={{ opacity: 0, y: 15 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: 0.1 }}
              className="md:col-span-2 bg-slate-900 dark:bg-slate-900 border border-slate-800 rounded-3xl p-10 text-white flex flex-col justify-between min-h-[300px] shadow-lg hover:shadow-2xl transition-all duration-300 group"
            >
              <div className="w-14 h-14 bg-white/10 rounded-2xl flex items-center justify-center text-rose-400 group-hover:scale-105 transition-transform">
                <Heart className="w-7 h-7 fill-current" />
              </div>
              <div>
                <h3 className="text-2xl font-bold mb-2">Built on Empathy, Not Just Statistics</h3>
                <p className="text-slate-400 leading-relaxed text-sm max-w-xl">
                  Healing requires dynamic social bonds. Support groups, dynamic chats, and shared story timelines help keep spirits up along the recovery journey.
                </p>
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Testimonials */}
      <section className="py-24 bg-white dark:bg-transparent">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-20">
            <span className="text-sm font-bold text-blue-600 dark:text-blue-400 uppercase tracking-widest">Testimonials</span>
            <h2 className="mt-3 text-3xl md:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">Real Results, Genuine Care</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {TESTIMONIALS.map((t, i) => (
              <motion.div 
                key={i}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5, delay: i * 0.1 }}
                className={`rounded-3xl p-8 border hover:-translate-y-1 transition-all duration-300 ${
                  t.featured
                    ? 'bg-gradient-to-br from-blue-600 to-indigo-700 text-white border-blue-500 shadow-xl'
                    : 'bg-slate-50 dark:bg-slate-900 border-slate-200/70 dark:border-slate-800'
                }`}
              >
                <StarRating rating={t.rating} />
                <p className={`text-sm leading-relaxed mb-6 italic ${t.featured ? 'text-blue-100' : 'text-slate-600 dark:text-slate-400'}`}>
                  "{t.quote}"
                </p>
                <div className="flex items-center gap-3">
                  <img src={t.avatar} className="w-11 h-11 rounded-full object-cover border border-slate-200/50" alt={t.name} />
                  <div>
                    <h4 className={`font-bold text-sm ${t.featured ? 'text-white' : 'text-slate-950 dark:text-white'}`}>{t.name}</h4>
                    <p className={`text-xs ${t.featured ? 'text-blue-200' : 'text-slate-400'}`}>{t.role}</p>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA section */}
      <section className="py-24 bg-slate-950 text-white text-center relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(59,130,246,0.1),transparent_70%)]" />
        <div className="relative z-10 max-w-4xl mx-auto px-6">
          <h2 className="text-4xl md:text-5xl font-extrabold tracking-tight mb-4">Start your recovery path today.</h2>
          <p className="text-slate-400 text-base max-w-xl mx-auto mb-8 leading-relaxed">
            MedTrackFit is free to join. Create a secure account to begin mapping your personal wellness routine.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/auth"
              className="inline-flex items-center justify-center gap-2 px-8 py-3.5 bg-white text-slate-950 hover:bg-slate-100 font-bold rounded-xl transition-all hover:-translate-y-0.5"
            >
              <span>Join Now</span>
              <ArrowRight className="w-4 h-4" />
            </Link>
          </div>
        </div>
      </section>
    </div>
  )
}
