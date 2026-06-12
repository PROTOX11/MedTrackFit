import React, { useEffect, useState, useRef } from 'react'

export default function CountUp({ end, duration = 2, prefix = '', suffix = '' }) {
  const [count, setCount] = useState(0)
  const elementRef = useRef(null)
  const [isVisible, setIsVisible] = useState(false)

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsVisible(true)
          observer.unobserve(entry.target)
        }
      },
      { threshold: 0.1 }
    )
    if (elementRef.current) {
      observer.observe(elementRef.current)
    }
    return () => observer.disconnect()
  }, [])

  useEffect(() => {
    if (!isVisible) return

    let start = 0
    const endStr = end.toString().replace(/[^0-9.]/g, '')
    const endNum = parseFloat(endStr)
    if (isNaN(endNum)) {
      setCount(end)
      return
    }

    const totalFrames = Math.round(duration * 60)
    let frame = 0

    const counter = setInterval(() => {
      frame++
      const progress = frame / totalFrames
      // Ease out quad
      const easeProgress = progress * (2 - progress)
      
      const current = start + easeProgress * (endNum - start)
      
      if (end.toString().includes('.')) {
        setCount(current.toFixed(1))
      } else {
        setCount(Math.floor(current))
      }

      if (frame >= totalFrames) {
        clearInterval(counter)
        setCount(endNum)
      }
    }, 1000 / 60)

    return () => clearInterval(counter)
  }, [isVisible, end, duration])

  return (
    <span ref={elementRef}>
      {prefix}
      {count}
      {suffix}
    </span>
  )
}
