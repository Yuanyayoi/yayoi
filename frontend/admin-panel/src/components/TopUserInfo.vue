<template>
  <div class="top-user-info">
    <div class="user-avatar-wrapper">
      <img v-if="userAvatar" :src="userAvatar" class="user-avatar-img" alt="头像" />
      <div v-else class="user-avatar-placeholder">
        {{ user?.username?.charAt(0)?.toUpperCase() || 'A' }}
      </div>
    </div>
    <span class="user-name">{{ user?.username }}</span>
  </div>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue'

const user = ref(null)

const userAvatar = computed(() => {
  if (!user.value?.avatar) return ''
  const avatar = user.value.avatar
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) return avatar
  return window.location.origin + avatar
})

const loadUser = () => {
  try {
    const userStr = sessionStorage.getItem('user')
    if (userStr) {
      user.value = JSON.parse(userStr)
    }
  } catch {
    user.value = null
  }
}

const handleUserUpdated = (event) => {
  if (event.detail) {
    user.value = event.detail
  }
}

const handleStorage = () => {
  loadUser()
}

onMounted(() => {
  loadUser()
  window.addEventListener('userUpdated', handleUserUpdated)
  window.addEventListener('storage', handleStorage)
})

onUnmounted(() => {
  window.removeEventListener('userUpdated', handleUserUpdated)
  window.removeEventListener('storage', handleStorage)
})
</script>

<style scoped>
.top-user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.user-avatar-wrapper {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.user-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.user-avatar-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #0891B2, #22D3EE);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
}

.user-name {
  color: #334155;
  font-size: 0.9rem;
  font-weight: 500;
}

@media (max-width: 768px) {
  .user-name {
    display: none;
  }
}
</style>
