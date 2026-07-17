<template>
  <Transition name="fade">
    <button 
      v-if="visible" 
      class="back-to-top" 
      @click="scrollToTop" 
      aria-label="返回顶部"
      title="返回顶部"
    >
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="20" height="20">
        <path d="M18 15l-6-6-6 6"/>
      </svg>
    </button>
  </Transition>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const visible = ref(false)

const handleScroll = () => {
  visible.value = window.scrollY > 400
}

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.back-to-top {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  z-index: 900;
  width: 52px;
  height: 52px;
  border-radius: 16px;
  background: var(--primary-gradient);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 20px rgba(0, 168, 89, 0.4);
  transition: all var(--transition-base);
}

.back-to-top:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 30px rgba(0, 168, 89, 0.5);
  border-radius: 14px;
}

.back-to-top:active {
  transform: translateY(-2px) scale(0.97);
}

.fade-enter-active,
.fade-leave-active {
  transition: all var(--transition-base);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.9);
}

@media (max-width: 768px) {
  .back-to-top {
    bottom: 1.5rem;
    right: 1.5rem;
    width: 46px;
    height: 46px;
    border-radius: 14px;
  }
}
</style>