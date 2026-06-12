import React, { useState, useEffect } from 'react'

export default function Blog({ user }) {
  const [blogs, setBlogs] = useState([])
  const [loading, setLoading] = useState(true)
  const [modalOpen, setModalOpen] = useState(false)
  const [newPost, setNewPost] = useState({ title: '', category: 'HEALTH_TIPS', content: '' })

  const loadBlogs = () => {
    setLoading(true)
    fetch('/blog/api/public')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          // Format category display names and assign gradients
          const formatted = data.map((b, idx) => {
            const gradients = [
              'from-amber-400 to-orange-500',
              'from-teal-400 to-emerald-500',
              'from-purple-400 to-indigo-500',
              'from-blue-400 to-indigo-500'
            ]
            return {
              id: b.postId || String(idx),
              title: b.title,
              category: b.category?.replace('_', ' ') || 'Health Tips',
              author: b.authorName || 'Author',
              excerpt: b.excerpt || b.content?.substring(0, 150) + (b.content?.length > 150 ? '...' : ''),
              content: b.content,
              likes: 12 + idx * 3, // Mock likes if not persisted in DB model
              views: 45 + idx * 11,
              gradient: gradients[idx % gradients.length]
            }
          })
          setBlogs(formatted)
        }
        setLoading(false)
      })
      .catch(err => {
        console.error('Error fetching blogs:', err)
        setLoading(false)
      })
  }

  useEffect(() => {
    loadBlogs()
  }, [])

  const role = user?.role?.toUpperCase() || 'USER'

  const handleLike = (id) => {
    setBlogs(prev => prev.map(b => b.id === id ? { ...b, likes: b.likes + 1 } : b))
  }

  const handleAddPost = (e) => {
    e.preventDefault()
    if (!newPost.title.trim() || !newPost.content.trim()) return

    // 1. Create Post
    fetch('/blog/create', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        title: newPost.title,
        content: newPost.content,
        category: newPost.category,
        excerpt: newPost.content.substring(0, 150)
      })
    })
      .then(res => res.json())
      .then(data => {
        if (data.success && data.postId) {
          // 2. Publish Post immediately
          return fetch(`/blog/publish/${data.postId}`, {
            method: 'POST'
          })
        } else {
          throw new Error(data.message || 'Failed to create post')
        }
      })
      .then(() => {
        setNewPost({ title: '', category: 'HEALTH_TIPS', content: '' })
        setModalOpen(false)
        loadBlogs() // reload list
      })
      .catch(err => {
        alert('Error: ' + err.message)
      })
  }

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight text-slate-800 dark:text-white">Medical Blog Workspace</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400">Share clinical insights, recovery tips, and clinical updates.</p>
        </div>

        {(role === 'DOCTOR' || role === 'HEALTHMENTOR' || role === 'MENTOR') && (
          <button 
            onClick={() => setModalOpen(true)}
            className="px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-xs transition-all duration-200 shadow-sm flex items-center gap-1.5 self-start"
          >
            <i className="fa-solid fa-plus text-xs"></i>
            <span>Write a Post</span>
          </button>
        )}
      </div>

      {loading ? (
        <div className="flex h-48 items-center justify-center text-xs text-slate-400">
          <i className="fa-solid fa-circle-notch fa-spin mr-1.5"></i> Loading blogs...
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {blogs.map((blog) => (
            <article 
              key={blog.id} 
              className="group flex flex-col bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl overflow-hidden shadow-sm hover:shadow-md transition-all duration-300"
            >
              {/* Visual Cover Header */}
              <div className={`h-40 bg-gradient-to-br ${blog.gradient} p-6 flex flex-col justify-between relative overflow-hidden`}>
                <div className="absolute top-0 right-0 w-32 h-32 bg-white/10 rounded-full blur-2xl group-hover:scale-110 transition-transform"></div>
                <span className="self-start text-[9px] font-extrabold uppercase tracking-wider bg-white/25 backdrop-blur-md text-white px-2.5 py-1 rounded-full">
                  {blog.category}
                </span>
                <h3 className="text-sm font-extrabold text-white leading-snug truncate-2-lines">{blog.title}</h3>
              </div>

              {/* Post details */}
              <div className="p-5 flex-1 flex flex-col justify-between space-y-4">
                <div className="space-y-2">
                  <p className="text-[10px] text-slate-400 font-bold">BY {blog.author.toUpperCase()}</p>
                  <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed font-medium">
                    {blog.excerpt}
                  </p>
                </div>

                {/* Engagement Controls */}
                <div className="flex items-center justify-between border-t border-slate-100 dark:border-slate-800/80 pt-4 text-[10px] text-slate-400 font-semibold">
                  <div className="flex gap-4">
                    <button 
                      onClick={() => handleLike(blog.id)}
                      className="flex items-center gap-1 hover:text-rose-500 transition-colors"
                    >
                      <i className="fa-solid fa-heart"></i>
                      <span>{blog.likes}</span>
                    </button>
                    <div className="flex items-center gap-1">
                      <i className="fa-solid fa-eye"></i>
                      <span>{blog.views}</span>
                    </div>
                  </div>
                  <button className="text-blue-600 dark:text-blue-400 hover:underline">Read Full</button>
                </div>
              </div>
            </article>
          ))}
          {blogs.length === 0 && (
            <div className="col-span-3 py-16 text-center text-slate-400 text-xs">
              <i className="fa-solid fa-newspaper block text-2xl mb-2"></i>
              No blog posts published yet. Be the first to write a post!
            </div>
          )}
        </div>
      )}

      {/* CREATE BLOG MODAL */}
      {modalOpen && (
        <div className="fixed inset-0 bg-slate-950/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl w-full max-w-lg shadow-xl animate-scale-in overflow-hidden">
            <div className="p-6 border-b border-slate-100 dark:border-slate-800 flex justify-between items-center bg-slate-50/50 dark:bg-slate-900/50">
              <h3 className="text-sm font-extrabold text-slate-800 dark:text-white">Create New Post</h3>
              <button onClick={() => setModalOpen(false)} className="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200">
                <i className="fa-solid fa-xmark text-lg"></i>
              </button>
            </div>

            <form onSubmit={handleAddPost} className="p-6 space-y-4">
              <div>
                <label className="block text-xs font-bold text-slate-500 mb-1.5 uppercase tracking-wider">Post Title</label>
                <input
                  type="text"
                  required
                  value={newPost.title}
                  onChange={(e) => setNewPost({ ...newPost, title: e.target.value })}
                  placeholder="Enter a catchy title..."
                  className="w-full px-4 py-3 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 mb-1.5 uppercase tracking-wider">Category</label>
                <select
                  value={newPost.category}
                  onChange={(e) => setNewPost({ ...newPost, category: e.target.value })}
                  className="w-full px-4 py-3 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all cursor-pointer font-bold"
                >
                  <option value="HEALTH_TIPS">Health Tips</option>
                  <option value="EXERCISE_SCIENCE">Exercise Science</option>
                  <option value="PREVENTATIVE_CARE">Preventative Care</option>
                  <option value="PATIENT_STORIES">Patient Stories</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 mb-1.5 uppercase tracking-wider">Content Body</label>
                <textarea
                  required
                  rows="5"
                  value={newPost.content}
                  onChange={(e) => setNewPost({ ...newPost, content: e.target.value })}
                  placeholder="Share details here..."
                  className="w-full px-4 py-3 border border-slate-200 dark:border-slate-800 rounded-xl text-xs bg-slate-50 dark:bg-slate-950 focus:bg-white focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 resize-none"
                ></textarea>
              </div>

              <div className="pt-2 flex justify-end gap-3">
                <button 
                  type="button" 
                  onClick={() => setModalOpen(false)}
                  className="px-4 py-2.5 border border-slate-200 dark:border-slate-700 text-slate-500 hover:bg-slate-50 dark:hover:bg-slate-800 rounded-xl text-xs font-bold transition-colors"
                >
                  Cancel
                </button>
                <button 
                  type="submit"
                  className="px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold transition-colors shadow-sm"
                >
                  Publish Post
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
