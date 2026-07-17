<template>
  <header
    class="header"
    :class="{ 'header-scrolled': isScrolled, 'menu-open': isMenuOpen }"
  >
    <div class="container">
      <div class="navbar-brand">
        <router-link to="/" class="logo" @click="isMenuOpen = false">
          <img
            :src="currentLogo"
            :alt="globalSettings.siteName || '四川国驰恒创科技有限公司'"
            class="logo-icon"
          />
          <span class="logo-text">{{
            globalSettings.siteName || "四川国驰恒创科技有限公司"
          }}</span>
        </router-link>
      </div>
      <nav class="navbar-nav">
        <ul>
          <li>
            <router-link to="/" :class="{ active: $route.path === '/' }"
              >首页</router-link
            >
          </li>
          <li>
            <router-link
              to="/about"
              :class="{ active: $route.path === '/about' }"
              >关于我们</router-link
            >
          </li>
          <li>
            <router-link
              to="/products"
              :class="{ active: $route.path.startsWith('/products') }"
              >产品服务</router-link
            >
          </li>
          <li>
            <router-link
              to="/news"
              :class="{ active: $route.path.startsWith('/news') }"
              >新闻动态</router-link
            >
          </li>
          <li>
            <router-link
              to="/contact"
              :class="{ active: $route.path === '/contact' }"
              >联系我们</router-link
            >
          </li>
        </ul>
      </nav>
      <button
        class="menu-toggle"
        :class="{ open: isMenuOpen }"
        @click="toggleMenu"
        aria-label="菜单"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>
    <Transition name="mobile-slide">
      <div class="mobile-menu" v-if="isMenuOpen">
        <div class="mobile-menu-inner">
          <ul>
            <li>
              <router-link to="/" @click="isMenuOpen = false">首页</router-link>
            </li>
            <li>
              <router-link to="/about" @click="isMenuOpen = false"
                >关于我们</router-link
              >
            </li>
            <li>
              <router-link to="/products" @click="isMenuOpen = false"
                >产品服务</router-link
              >
            </li>
            <li>
              <router-link to="/news" @click="isMenuOpen = false"
                >新闻动态</router-link
              >
            </li>
            <li>
              <router-link to="/contact" @click="isMenuOpen = false"
                >联系我们</router-link
              >
            </li>
          </ul>
          <div class="mobile-contact">
            <p>{{ globalSettings.siteName || "四川国驰恒创科技有限公司" }}</p>
            <p>
              {{ globalSettings.siteDescription || "专注新能源电池快充技术" }}
            </p>
          </div>
        </div>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { siteCache } from "@/utils/siteCache";

const router = useRouter();
const isScrolled = ref(false);
const isMenuOpen = ref(false);
const globalSettings = ref({});

const currentLogo = computed(() => {
  if (isScrolled.value) {
    return globalSettings.value.logoUrlDark || "/favicon.png";
  }
  return globalSettings.value.logoUrl || "/favicon-color.png";
});

const handleScroll = () => {
  isScrolled.value = window.scrollY > 60;
};

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
  if (isMenuOpen.value) {
    document.body.style.overflow = "hidden";
    document.body.style.touchAction = "none";
  } else {
    document.body.style.overflow = "";
    document.body.style.touchAction = "";
  }
};

const loadGlobal = async () => {
  try {
    globalSettings.value = await siteCache.getGlobal();
  } catch (e) {
    console.error("加载全局设置失败", e);
  }
};

const closeMenu = () => {
  if (isMenuOpen.value) {
    isMenuOpen.value = false;
    document.body.style.overflow = "";
    document.body.style.touchAction = "";
  }
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll, { passive: true });
  loadGlobal();
  router.afterEach(closeMenu);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
  document.body.style.overflow = "";
  document.body.style.touchAction = "";
});
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(12, 30, 42, 0.45);
  backdrop-filter: blur(18px);
  -webkit-backdrop-filter: blur(18px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
  padding: 0.7rem 0;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}

.header-scrolled {
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(15, 23, 42, 0.06);
  box-shadow: 0 4px 24px rgba(15, 23, 42, 0.06);
  padding: 0.5rem 0;
}

.header .container {
  max-width: 100%;
  margin: 0;
  padding: 0 2.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
}

.logo-icon {
  width: 38px;
  height: 38px;
  border-radius: 8px;
  object-fit: contain;
  transition: all 0.35s ease;
}

.logo-text {
  font-size: 1.05rem;
  font-weight: 700;
  color: white;
  transition: color 0.35s ease;
  white-space: nowrap;
}

.header-scrolled .logo-text {
  color: #1a365d;
}

.navbar-nav ul {
  display: flex;
  gap: 2rem;
  list-style: none;
  margin: 0;
  padding: 0;
}

.navbar-nav a {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-weight: 600;
  font-size: 0.92rem;
  letter-spacing: 0.02em;
  transition: color 0.3s ease;
  position: relative;
  padding: 0.35rem 0;
}

.header-scrolled .navbar-nav a {
  color: #4a5568;
}

.navbar-nav a:hover,
.navbar-nav a.active {
  color: #33d17a;
}

.header-scrolled .navbar-nav a:hover,
.header-scrolled .navbar-nav a.active {
  color: #00a859;
}

.navbar-nav a::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #00a859, #33d17a);
  border-radius: 1px;
  transition: width 0.3s ease;
}

.navbar-nav a:hover::after,
.navbar-nav a.active::after {
  width: 100%;
}

.menu-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px 4px;
  z-index: 1001;
}

.menu-toggle span {
  display: block;
  width: 24px;
  height: 2px;
  background: white;
  border-radius: 2px;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}

.header-scrolled .menu-toggle span {
  background: #1a365d;
}

.menu-toggle.open span:nth-child(1) {
  transform: translateY(7px) rotate(45deg);
}

.menu-toggle.open span:nth-child(2) {
  opacity: 0;
}

.menu-toggle.open span:nth-child(3) {
  transform: translateY(-7px) rotate(-45deg);
}

.mobile-menu {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
}

.mobile-menu-inner {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  background: white;
  padding: 5rem 1.5rem 2rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  border-radius: 0 0 24px 24px;
}

.mobile-menu-inner ul {
  list-style: none;
  padding: 0;
  margin: 0 0 2rem;
}

.mobile-menu-inner li {
  border-bottom: 1px solid #f0f0f0;
}

.mobile-menu-inner a {
  display: block;
  padding: 1rem 0.5rem;
  color: #1a365d;
  text-decoration: none;
  font-size: 1.05rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.mobile-menu-inner a:hover {
  color: #00a859;
  padding-left: 1rem;
}

.mobile-contact {
  text-align: center;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
}

.mobile-contact p {
  color: #718096;
  font-size: 0.85rem;
  margin: 0;
  line-height: 1.6;
}

.mobile-slide-enter-active,
.mobile-slide-leave-active {
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}

.mobile-slide-enter-from,
.mobile-slide-leave-to {
  opacity: 0;
}

.mobile-slide-enter-from .mobile-menu-inner,
.mobile-slide-leave-to .mobile-menu-inner {
  transform: translateY(-100%);
}

@media (max-width: 768px) {
  .navbar-nav {
    display: none;
  }

  .menu-toggle {
    display: flex;
  }

  .logo-text {
    font-size: 0.9rem;
  }
}
</style>
