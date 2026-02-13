import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/article': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/category': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/tag': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/comment': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
