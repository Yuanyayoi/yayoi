
<template>
  <div class="admin-app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import axios from 'axios'
import { checkIdleTimeout, resetIdleTimer } from './api/index.js'

const updateFavicon = (logoUrl) => {
  if (!logoUrl) return
  const head = document.getElementsByTagName('head')[0]
  const oldIcons = document.querySelectorAll("link[rel='icon'], link[rel='shortcut icon']")
  oldIcons.forEach(link => link.parentNode?.removeChild(link))
  
  const newLink = document.createElement('link')
  newLink.rel = 'icon'
  newLink.type = 'image/png'
  newLink.href = logoUrl.startsWith('http') ? logoUrl : (window.location.origin + logoUrl) + '?t=' + Date.now()
  head.appendChild(newLink)
  
  const newLink2 = document.createElement('link')
  newLink2.rel = 'shortcut icon'
  newLink2.type = 'image/png'
  newLink2.href = logoUrl.startsWith('http') ? logoUrl : (window.location.origin + logoUrl) + '?t=' + Date.now()
  head.appendChild(newLink2)
}

const loadGlobalSettings = async () => {
  try {
    const res = await axios.get('/site/global')
    const settings = res.data?.data || {}
    if (settings.siteName) {
      document.title = settings.siteName + ' - 管理后台'
    }
    if (settings.logoUrl) {
      updateFavicon(settings.logoUrl)
    }
  } catch (e) {
    console.error('加载全局设置失败', e)
  }
}

const handleSettingsUpdated = (event) => {
  if (event.detail) {
    if (event.detail.siteName) {
      document.title = event.detail.siteName + ' - 管理后台'
    }
    if (event.detail.logoUrl) {
      updateFavicon(event.detail.logoUrl)
    }
  }
}

const IDLE_CHECK_INTERVAL = 60 * 1000
let idleCheckTimer = null

const startActivityTracking = () => {
  const events = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click']
  events.forEach(event => {
    window.addEventListener(event, resetIdleTimer, { passive: true })
  })

  idleCheckTimer = setInterval(() => {
    if (!window.location.pathname.startsWith('/admin/login')) {
      checkIdleTimeout()
    }
  }, IDLE_CHECK_INTERVAL)
}

const stopActivityTracking = () => {
  if (idleCheckTimer) {
    clearInterval(idleCheckTimer)
    idleCheckTimer = null
  }
}

onMounted(() => {
  loadGlobalSettings()
  window.addEventListener('globalSettingsUpdated', handleSettingsUpdated)
  
  if (sessionStorage.getItem('token')) {
    resetIdleTimer()
  }
  startActivityTracking()
})

onUnmounted(() => {
  window.removeEventListener('globalSettingsUpdated', handleSettingsUpdated)
  stopActivityTracking()
})
</script>

<style scoped>
.admin-app {
  min-height: 100vh;
}
</style>
