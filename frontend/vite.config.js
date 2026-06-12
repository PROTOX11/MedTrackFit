import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // Proxy API calls to Spring Boot during dev
  server: {
    proxy: {
      '/api': 'http://localhost:8081',
      '/doctor': 'http://localhost:8081',
      '/mentor': 'http://localhost:8081',
      '/suff-pat': 'http://localhost:8081',
      '/recoveredpatient': 'http://localhost:8081',
    }
  },
  build: {
    // Output to Spring Boot static directory
    outDir: resolve(__dirname, '../src/main/resources/static/react-app'),
    emptyOutDir: true,
    rollupOptions: {
      output: {
        entryFileNames: 'assets/[name].js',
        chunkFileNames: 'assets/[name].js',
        assetFileNames: 'assets/[name].[ext]'
      }
    }
  },
  base: '/react-app/',
})

