import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { FileText, Handshake, Mail, ArrowRight } from 'lucide-react'

const SECTIONS = [
  {
    title: '1. Acceptance of Terms',
    content: 'By creating an account on MedTrackFit, you agree to these Terms of Service. If you do not agree, please do not use the platform. We reserve the right to update these terms at any time, with notice provided via email.',
  },
  {
    title: '2. Eligibility',
    content: 'You must be at least 18 years of age to use MedTrackFit. By registering, you confirm that you are 18 or older and have the legal capacity to enter into a binding agreement. MedTrackFit is intended for use by individuals seeking health support and community connection.',
  },
  {
    title: '3. Account Responsibility',
    content: 'You are responsible for maintaining the confidentiality of your account. Since we use OTP-based authentication, you should never share your OTP codes with anyone. Report any unauthorized access immediately to support@medtrackfit.com.',
  },
  {
    title: '4. Medical Disclaimer',
    content: 'MedTrackFit is NOT a medical service and does not provide medical advice, diagnosis, or treatment. Information shared by mentors and community members is based on personal experience and should not replace professional medical advice. Always consult a qualified healthcare professional for medical decisions.',
  },
  {
    title: '5. User Conduct',
    content: 'You agree not to: share false medical information, harass or bully other members, post spam or promotional content, impersonate healthcare professionals without verification, or use the platform for any illegal purposes. Violations may result in immediate account termination.',
  },
  {
    title: '6. Mentor & Doctor Roles',
    content: 'Health Mentors and Doctors on the platform have been verified through our onboarding process. However, MedTrackFit cannot guarantee the accuracy of all information shared. Mentors share lived experience, not clinical advice. Doctors provide general guidance and plan verification only.',
  },
  {
    title: '7. Content & Intellectual Property',
    content: 'You retain ownership of content you post (blog articles, recovery stories, messages). By posting, you grant MedTrackFit a non-exclusive license to display your content on the platform. MedTrackFit\'s brand, design, and code remain the exclusive property of MedTrackFit.',
  },
  {
    title: '8. Limitation of Liability',
    content: 'MedTrackFit is provided "as is." We are not liable for any health outcomes, data loss, or decisions made based on content found on the platform. Our maximum liability in any dispute is limited to the amount paid (if any) in the 12 months preceding the claim.',
  },
  {
    title: '9. Termination',
    content: 'We reserve the right to suspend or terminate your account at any time for violations of these terms. You may also delete your account at any time from your profile settings. Upon deletion, all personal data will be removed within 30 days.',
  },
  {
    title: '10. Governing Law',
    content: 'These Terms shall be governed by the laws of India. Any disputes shall be subject to the exclusive jurisdiction of courts in Noida, Uttar Pradesh. If any provision of these Terms is found invalid, the remaining provisions shall remain in full force.',
  },
]

export default function Terms() {
  useEffect(() => { document.title = 'Terms of Service — MedTrackFit' }, [])

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
          <div className="inline-flex items-center gap-2 bg-amber-500/10 border border-amber-500/20 text-amber-600 dark:text-amber-400 text-xs font-bold px-4 py-2 rounded-full mb-6">
            <FileText className="w-4 h-4" />
            <span>Legal Agreement</span>
          </div>
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            Terms of <span className="bg-gradient-to-r from-amber-600 to-orange-600 bg-clip-text text-transparent">Service</span>
          </h1>
          <p className="mt-4 text-slate-400 text-xs font-semibold">Effective Date: June 1, 2025</p>
          <p className="mt-6 text-lg text-slate-500 dark:text-slate-400 max-w-2xl mx-auto leading-relaxed">
            Please read these terms carefully before using MedTrackFit. Using our platform means you accept these terms.
          </p>
        </motion.div>
      </section>

      {/* Table of contents */}
      <section className="py-8 px-6 bg-white dark:bg-slate-900/40 border-y border-slate-200/50 dark:border-slate-800/50">
        <div className="max-w-4xl mx-auto">
          <p className="text-xs font-bold text-slate-400 uppercase tracking-widest mb-4">Jump to section</p>
          <div className="flex flex-wrap gap-2">
            {SECTIONS.map((s) => {
              const name = s.title.split('. ')[1] || s.title
              return (
                <a
                  key={s.title}
                  href={`#section-${s.title.replace(/\s+/g, '-').toLowerCase()}`}
                  className="text-xs font-semibold text-blue-600 dark:text-blue-400 bg-blue-500/10 px-3 py-1.5 rounded-lg border border-blue-500/10 hover:bg-blue-500/20 transition-colors"
                >
                  {name}
                </a>
              )
            })}
          </div>
        </div>
      </section>

      {/* Content list */}
      <section className="py-16 px-6 max-w-4xl mx-auto">
        <motion.div 
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
          transition={{ duration: 0.5 }}
          className="space-y-6"
        >
          {SECTIONS.map((s, idx) => (
            <div
              key={idx}
              id={`section-${s.title.replace(/\s+/g, '-').toLowerCase()}`}
              className="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/50 dark:border-slate-800 p-8 shadow-sm"
            >
              <div className="flex items-center gap-3 mb-4">
                <span className="w-8 h-8 rounded-lg bg-amber-500/10 text-amber-600 dark:text-amber-400 font-extrabold text-xs flex items-center justify-center">
                  {idx + 1}
                </span>
                <h2 className="text-base font-bold text-slate-900 dark:text-white">{s.title.split('. ')[1] || s.title}</h2>
              </div>
              <p className="text-slate-500 dark:text-slate-400 text-sm leading-relaxed">{s.content}</p>
            </div>
          ))}

          {/* Legal support banner */}
          <div className="bg-gradient-to-br from-slate-900 to-slate-850 dark:from-slate-900 dark:to-slate-950 border border-slate-800 rounded-2xl p-8 text-white text-center shadow-lg">
            <Handshake className="w-8 h-8 mx-auto mb-4 text-blue-400" />
            <h3 className="text-xl font-bold mb-2">Questions About These Terms?</h3>
            <p className="text-slate-400 text-sm mb-6">We're here to clarify anything in plain language.</p>
            <Link 
              to="/contact" 
              className="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-bold px-6 py-3 rounded-xl transition-all hover:-translate-y-0.5 text-sm"
            >
              <span>Contact Support</span>
              <ArrowRight className="w-4 h-4" />
            </Link>
          </div>
        </motion.div>
      </section>
    </div>
  )
}
