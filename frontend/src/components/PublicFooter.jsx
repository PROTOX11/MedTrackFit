import React from 'react'
import { Link } from 'react-router-dom'

export default function PublicFooter() {
  return (
    <footer className="bg-gray-950 border-t border-white/5 py-12 px-6">
      <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-6">
        <div className="flex items-center gap-2 font-extrabold text-white text-lg">
          <span className="w-7 h-7 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-sm">
            <i className="fa-solid fa-heart-pulse" />
          </span>
          MedTrackFit
        </div>
        <div className="flex flex-wrap justify-center gap-6 text-sm text-gray-400 font-semibold">
          <Link to="/about" className="hover:text-white transition-colors">About</Link>
          <Link to="/privacy" className="hover:text-white transition-colors">Privacy</Link>
          <Link to="/terms" className="hover:text-white transition-colors">Terms</Link>
          <Link to="/contact" className="hover:text-white transition-colors">Contact</Link>
          <a href="https://www.linkedin.com/in/protox1142" target="_blank" rel="noopener noreferrer" className="hover:text-white transition-colors flex items-center gap-1">
            <i className="fa-brands fa-linkedin text-base" /> LinkedIn
          </a>
          <a href="https://github.com/PROTOX11/MedTrackFit" target="_blank" rel="noopener noreferrer" className="hover:text-white transition-colors flex items-center gap-1">
            <i className="fa-brands fa-github text-base" /> GitHub
          </a>
        </div>
        <p className="text-xs text-gray-500">© 2025 MedTrackFit. All rights reserved.</p>
      </div>
    </footer>
  )
}
