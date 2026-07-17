
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  base: '/admin/',
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'vendor-vue': ['vue', 'vue-router'],
          'vendor-element': ['element-plus'],
          'vendor-echarts': ['echarts'],
          'vendor-axios': ['axios']
        }
      }
    },
    chunkSizeWarningLimit: 1000
  },
  server: {
    port: 5174,
    host: 'localhost',
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/auth': 'http://localhost:8080',
      '/articles': 'http://localhost:8080',
      '/categories': 'http://localhost:8080',
      '/messages': 'http://localhost:8080',
      '/site': 'http://localhost:8080',
      '/products': 'http://localhost:8080',
      '/public': 'http://localhost:8080'
    }
  }
})
