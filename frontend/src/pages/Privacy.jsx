import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Database, Settings, Share2, Lock, UserCheck, Eye, ShieldAlert, Mail } from 'lucide-react'

const SECTIONS = [
  {
    title: 'Information We Collect',
    icon: Database,
    content: [
      'Account information: name, email address, phone number, and role (patient, mentor, doctor).',
      'Health data: conditions, recovery notes, food logs, hydration and wellness tracking data you choose to enter.',
      'Usage data: pages visited, features used, timestamps, and device/browser information.',
      'Messages: communications between you and your mentors, doctors, or community members.',
    ],
  },
  {
    title: 'How We Use Your Information',
    icon: Settings,
    content: [
      'To provide and improve the MedTrackFit platform and its features.',
      'To match you with appropriate mentors, doctors, and peers based on your condition and preferences.',
      'To send you OTP codes for authentication and important account notifications.',
      'To generate anonymized analytics to improve community health outcomes.',
    ],
  },
  {
    title: 'Data Sharing',
    icon: Share2,
    content: [
      'We do NOT sell your personal or health data to any third parties.',
      'Your data is only shared with mentors and doctors you explicitly connect with.',
      'Anonymized, aggregated data may be used for research purposes with your consent.',
      'We may share data with service providers (e.g., email delivery) under strict confidentiality agreements.',
    ],
  },
  {
    title: 'Data Security',
    icon: Lock,
    content: [
      'All data is encrypted at rest and in transit using industry-standard protocols.',
      'OTP-based authentication eliminates the risk of password breaches.',
      'Regular security audits and penetration testing are conducted.',
      'You can request deletion of all your data at any time.',
    ],
  },
  {
    title: 'Your Rights',
    icon: UserCheck,
    content: [
      'Access: Request a copy of all personal data we hold about you.',
      'Correction: Update inaccurate personal information through your profile.',
      'Deletion: Request complete deletion of your account and associated data.',
      'Portability: Export your health data in standard formats (CSV, JSON).',
    ],
  },
  {
    title: 'Cookies & Tracking',
    icon: Eye,
    content: [
      'We use essential session cookies for authentication and security only.',
      'No advertising or cross-site tracking cookies are used.',
      'Analytics are privacy-first and do not include personally identifiable information.',
      'You can disable non-essential cookies in your browser settings at any time.',
    ],
  },
]

export default function Privacy() {
  useEffect(() => { document.title = 'Privacy Policy — MedTrackFit' }, [])

  return (
    <div className="relative overflow-x-hidden min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-800 dark:text-slate-100 transition-colors duration-500">
      
      {/* Background blobs */}
      <div className="absolute top-0 right-1/4 w-[500px] h-[500px] bg-blue-500/5 dark:bg-blue-600/5 rounded-full blur-3xl pointer-events-none -z-10" />

      {/* Hero Section */}
      <section className="pt-20 pb-12 text-center px-6">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto"
        >
          <div className="inline-flex items-center gap-2 bg-green-500/10 border border-green-500/20 text-green-600 dark:text-green-400 text-xs font-bold px-4 py-2 rounded-full mb-6">
            <ShieldAlert className="w-4 h-4 animate-pulse" />
            <span>GDPR & Privacy Compliant</span>
          </div>
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            Privacy <span className="bg-gradient-to-r from-green-600 to-blue-600 bg-clip-text text-transparent">Policy</span>
          </h1>
          <p className="mt-4 text-slate-400 text-xs font-semibold">Last updated: June 2025</p>
          <p className="mt-6 text-lg text-slate-500 dark:text-slate-400 max-w-2xl mx-auto leading-relaxed">
            Your health data is deeply personal. We take privacy seriously and are transparent about every aspect of how your information is handled.
          </p>
        </motion.div>
      </section>

      {/* Content grid */}
      <section className="py-12 px-6 max-w-4xl mx-auto">
        <motion.div 
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 0.5 }}
          className="space-y-6"
        >
          {SECTIONS.map((s, idx) => {
            const Icon = s.icon
            return (
              <div key={idx} className="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/50 dark:border-slate-800 shadow-sm overflow-hidden">
                <div className="flex items-center gap-4 p-6 border-b border-slate-200/50 dark:border-slate-800/80">
                  <div className="w-10 h-10 rounded-xl bg-blue-500/10 flex items-center justify-center text-blue-600 dark:text-blue-400">
                    <Icon className="w-5 h-5" />
                  </div>
                  <h2 className="text-base font-bold text-slate-900 dark:text-white">{s.title}</h2>
                </div>
                <ul className="p-6 space-y-3">
                  {s.content.map((item, iIdx) => (
                    <li key={iIdx} className="flex items-start gap-3 text-sm text-slate-500 dark:text-slate-400">
                      <span className="w-1.5 h-1.5 rounded-full bg-blue-500 mt-2 flex-shrink-0" />
                      <span>{item}</span>
                    </li>
                  ))}
                </ul>
              </div>
            )
          })}

          {/* DPO Support Card */}
          <div className="bg-gradient-to-br from-blue-600 to-indigo-700 rounded-2xl p-8 text-white text-center shadow-lg">
            <Mail className="w-8 h-8 mx-auto mb-4" />
            <h3 className="text-xl font-bold mb-2">Privacy Questions?</h3>
            <p className="text-blue-100 text-sm mb-6">Contact our Data Protection Officer at any time.</p>
            <a 
              href="mailto:privacy@medtrackfit.com" 
              className="inline-flex items-center gap-2 bg-white text-blue-700 font-bold px-6 py-3 rounded-xl hover:bg-slate-100 transition-all text-sm"
            >
              <span>privacy@medtrackfit.com</span>
            </a>
          </div>
        </motion.div>
      </section>
    </div>
  )
}
