<template>
  <div class="home" :style="{ '--xre-spotlight-image': `url(${batteryLabImage})` }">
    <section class="hero" @mouseenter="stopSlideInterval" @mouseleave="startSlideInterval">
      <div class="hero-carousel">
        <div 
          v-for="(slide, index) in slides" 
          :key="index"
          :class="['slide', { active: currentSlide === index }]"
        >
          <img :src="slide.image" :alt="slide.title" />
          <div class="slide-overlay"></div>
        </div>
      </div>
      <div class="hero-content" v-if="!isLoading && slides.length > 0">
        <div class="container">
          <Transition name="hero-fade" mode="out-in">
            <div class="hero-text" :key="currentSlide">
              <h1 v-if="slides[currentSlide].title">{{ slides[currentSlide].title }}</h1>
              <h2 v-if="slides[currentSlide].subtitle">{{ slides[currentSlide].subtitle }}</h2>
              <p v-if="slides[currentSlide].description">{{ slides[currentSlide].description }}</p>
            </div>
          </Transition>
        </div>
      </div>
      <div class="hero-loading" v-else>
        <div class="loading-spinner"></div>
      </div>
      <div class="hero-controls">
        <div class="hero-controls-inner">
          <div class="hero-arrow-group">
            <button class="control-btn prev" type="button" aria-label="上一张" @click="prevSlide">
              <svg viewBox="0 0 58 18" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M10 9h45"/>
                <path d="M18 1L10 9l8 8"/>
              </svg>
            </button>
            <button class="control-btn next" type="button" aria-label="下一张" @click="nextSlide">
              <svg viewBox="0 0 58 18" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9h45"/>
                <path d="M40 1l8 8-8 8"/>
              </svg>
            </button>
          </div>
          <div class="carousel-indicators" role="tablist" aria-label="轮播图切换">
            <button
              v-for="(slide, index) in slides"
              :key="index"
              type="button"
              :class="['indicator', { active: currentSlide === index }]"
              :aria-label="`切换到第 ${index + 1} 张`"
              :aria-selected="currentSlide === index"
              @click="goToSlide(index)"
            ></button>
          </div>
        </div>
      </div>
    </section>
    
    <section class="features" id="features">
      <div class="container">
        <div class="section-header" v-if="featuresText.sectionTitle || featuresText.sectionSubtitle">
          <span class="section-subtitle" v-if="featuresText.sectionTitle">{{ featuresText.sectionTitle }}</span>
          <h2 class="section-title" v-if="featuresText.sectionSubtitle">{{ featuresText.sectionSubtitle }}</h2>
        </div>
        <div class="features-grid">
          <div 
            v-for="feature in features" 
            :key="feature.id" 
            class="feature-card"
            :class="'style-' + (feature.style || 1)"
          >
            <div class="feature-icon">
              <img :src="getIconImage(feature.style)" class="feature-icon-img" />
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="company-intro">
      <div class="container intro-grid">
        <div class="intro-image" v-if="homeAboutIntro.imageUrl">
          <img :src="homeAboutIntro.imageUrl" alt="公司介绍" />
        </div>
        <div class="intro-content">
          <span class="section-subtitle" v-if="homeAboutIntro.subtitle">{{ homeAboutIntro.subtitle }}</span>
          <h2 v-if="aboutData.companyName">{{ aboutData.companyName }}</h2>
          <p v-if="aboutData.companyDesc">{{ aboutData.companyDesc }}</p>
          <p v-if="aboutData.mission">{{ aboutData.mission }}</p>
          <button v-if="homeAboutIntro.buttonText" class="btn btn-primary" @click="$router.push(homeAboutIntro.buttonLink || '/about')">{{ homeAboutIntro.buttonText }}</button>
        </div>
      </div>
    </section>

    <section class="xre-spotlight" v-if="homeSpotlight.enabled">
      <div class="container xre-grid">
        <div class="xre-copy">
          <span class="section-subtitle">{{ homeSpotlight.subtitle }}</span>
          <h2>{{ homeSpotlight.title }}</h2>
          <p>
            {{ homeSpotlight.description }}
          </p>
          <button class="btn btn-primary" @click="$router.push(homeSpotlight.buttonLink || '/products')">
            {{ homeSpotlight.buttonText }}
          </button>
        </div>
        <div class="xre-metrics">
          <div v-for="item in homeSpotlight.metrics" :key="item.label" class="xre-metric">
            <strong>{{ item.value }}</strong>
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>
    </section>
    
    <section class="latest-news" id="news">
      <div class="container">
        <div class="section-header">
          <span class="section-subtitle" v-if="homeNews.sectionSubtitle">{{ homeNews.sectionSubtitle }}</span>
          <h2 class="section-title" v-if="homeNews.sectionTitle">{{ homeNews.sectionTitle }}</h2>
        </div>
        <div class="news-list">
          <div 
            v-for="article in homeNewsList" 
            :key="article.id" 
            class="news-card"
            @click="$router.push(`/news/${article.id}`)"
          >
            <div class="news-image">
              <img :src="article.coverImage" alt="文章封面" v-if="article.coverImage" />
              <div class="news-date">{{ formatDate(article.createdAt) }}</div>
            </div>
            <div class="news-content">
              <h3>{{ article.title }}</h3>
              <p>{{ article.summary }}</p>
              <span class="read-more">阅读更多 →</span>
            </div>
          </div>
        </div>
        <div class="text-center" v-if="homeNews.buttonText">
          <button @click="$router.push(homeNews.buttonLink || '/news')" class="btn btn-outline-secondary">{{ homeNews.buttonText }}</button>
        </div>
      </div>
    </section>
    
    <section class="latest-products" id="products">
      <div class="container">
        <div class="section-header">
          <span class="section-subtitle" v-if="homeProducts.sectionSubtitle">{{ homeProducts.sectionSubtitle }}</span>
          <h2 class="section-title" v-if="homeProducts.sectionTitle">{{ homeProducts.sectionTitle }}</h2>
        </div>
        <div class="products-grid">
          <div 
            v-for="product in homeProductList" 
            :key="product.id" 
            class="product-card"
            @click="$router.push(`/products/${product.id}`)"
          >
            <div class="product-image">
              <img :src="product.image" alt="产品图片" v-if="product.image" />
              <div class="product-overlay">
                <span>查看详情</span>
              </div>
            </div>
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <p class="product-price" v-if="product.price">¥{{ product.price.toLocaleString() }}</p>
            </div>
          </div>
        </div>
        <div class="text-center" v-if="homeProducts.buttonText">
          <button @click="$router.push(homeProducts.buttonLink || '/products')" class="btn btn-outline-secondary">{{ homeProducts.buttonText }}</button>
        </div>
      </div>
    </section>
    
    <section class="contact-cta">
      <div class="cta-bg-pattern"></div>
      <div class="container">
        <div class="cta-content">
          <span class="cta-eyebrow" v-if="homeCta.eyebrow">{{ homeCta.eyebrow }}</span>
          <h2 v-if="homeCta.title">{{ homeCta.title }}</h2>
          <p v-if="homeCta.description">{{ homeCta.description }}</p>
          <div class="cta-actions">
            <button v-if="homeCta.button1Text" class="btn btn-primary" @click="$router.push(homeCta.button1Link || '/contact')">{{ homeCta.button1Text }}</button>
            <button v-if="homeCta.button2Text" class="btn btn-outline-light" @click="$router.push(homeCta.button2Link || '/products')">{{ homeCta.button2Text }}</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { articleApi, productApi } from '@/api'
import { siteCache } from '@/utils/siteCache'
import batteryLabImage from '@/assets/site/battery-lab.jpg'

const newsList = ref([])
const productList = ref([])
const currentSlide = ref(0)
let slideTimeout = null

const features = ref([])
const iconMap = ref({})
const featuresText = ref({})
const slides = ref([])
const aboutData = ref({})
const homeAboutIntro = ref({})
const homeNews = ref({})
const homeProducts = ref({})
const homeCta = ref({})
const homeNewsList = ref([])
const homeProductList = ref([])
const isLoading = ref(true)

const homeSpotlight = ref({
  enabled: false,
  subtitle: '',
  title: '',
  description: '',
  buttonText: '',
  buttonLink: '',
  metrics: []
})

const applyHomeModules = (modules) => {
  if (modules?.homeSpotlight) {
    homeSpotlight.value = {
      enabled: true,
      subtitle: modules.homeSpotlight.subtitle || '',
      title: modules.homeSpotlight.title || '',
      description: modules.homeSpotlight.description || '',
      buttonText: modules.homeSpotlight.buttonText || '',
      buttonLink: modules.homeSpotlight.buttonLink || '/products',
      metrics: Array.isArray(modules.homeSpotlight.metrics) ? modules.homeSpotlight.metrics : []
    }
  }
}

const loadCarousels = async () => {
  slides.value = await siteCache.getCarousels()
}

const loadFeatures = async () => {
  const [feat, icons] = await Promise.all([
    siteCache.getFeatures(),
    siteCache.getIcons()
  ])
  features.value = feat
  iconMap.value = icons
}

const getIconImage = (styleId) => {
  if (!styleId) return null
  const icon = iconMap.value[styleId]
  return icon ? icon.imageUrl : null
}

const prevSlide = () => {
  if (slides.value.length === 0) return
  currentSlide.value = currentSlide.value === 0 ? slides.value.length - 1 : currentSlide.value - 1
  resetSlideInterval()
}

const loadContentModules = async () => {
  applyHomeModules(await siteCache.getModules())
}

const loadHomeNews = async () => {
  homeNewsList.value = await siteCache.getHomeNewsList()
}

const loadHomeProducts = async () => {
  homeProductList.value = await siteCache.getHomeProductList()
}

const nextSlide = (auto = false) => {
  if (slides.value.length === 0) return
  currentSlide.value = currentSlide.value === slides.value.length - 1 ? 0 : currentSlide.value + 1
  if (!auto) {
    resetSlideInterval()
  }
}

const goToSlide = (index) => {
  currentSlide.value = index
  resetSlideInterval()
}

const startSlideInterval = () => {
  if (slideTimeout || slides.value.length <= 1) return
  slideTimeout = setTimeout(() => {
    slideTimeout = null
    nextSlide(true)
    startSlideInterval()
  }, 15000)
}

const resetSlideInterval = () => {
  stopSlideInterval()
  startSlideInterval()
}

const stopSlideInterval = () => {
  if (slideTimeout) {
    clearTimeout(slideTimeout)
    slideTimeout = null
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

onMounted(async () => {
  window.scrollTo(0, 0)
  await loadCarousels()
  loadFeatures()
  loadContentModules()
  loadHomeNews()
  loadHomeProducts()
  ;(async () => {
    try {
      const [about, featuresTextData, homeAboutIntroData, homeNewsData, homeProductsData, homeCtaData] = await Promise.all([
        siteCache.getAbout(),
        siteCache.getFeaturesText(),
        siteCache.getHomeAboutIntro(),
        siteCache.getHomeNews(),
        siteCache.getHomeProducts(),
        siteCache.getHomeCta()
      ])
      aboutData.value = about
      featuresText.value = featuresTextData
      homeAboutIntro.value = homeAboutIntroData
      homeNews.value = homeNewsData
      homeProducts.value = homeProductsData
      homeCta.value = homeCtaData
    } catch (error) {
      console.error('获取数据失败:', error)
    }
  })()
  isLoading.value = false
  startSlideInterval()
})

onUnmounted(() => {
  stopSlideInterval()
})
</script>

<style scoped>
.hero {
  position: relative;
  min-height: 760px;
  height: 92vh;
  display: flex;
  align-items: center;
  overflow: hidden;
}

.hero-carousel {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.slide {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: opacity 0.8s ease-in-out;
}

.slide.active {
  opacity: 1;
}

.slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 8s ease-in-out;
}

.slide.active img {
  transform: scale(1.08);
}

.slide-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: 
    linear-gradient(180deg, rgba(5, 18, 30, 0.6) 0%, rgba(5, 18, 30, 0.3) 30%, rgba(5, 18, 30, 0.1) 60%, rgba(5, 18, 30, 0.25) 100%),
    linear-gradient(90deg, rgba(5, 18, 30, 0.4) 0%, transparent 50%, rgba(5, 18, 30, 0.2) 100%);
}

.hero-content {
  position: relative;
  z-index: 10;
  width: 100%;
  text-align: left;
  transform: translateY(-8vh);
  animation: fadeInUp 0.5s ease;
}

.hero-content .container {
  max-width: 100%;
  margin: 0;
  padding: 0 2.5rem 0 5rem;
}

.hero-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  width: 100%;
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 3px solid rgba(255, 255, 255, 0.2);
  border-top-color: #33d17a;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(-8vh) translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(-8vh);
  }
}

.hero-text {
  max-width: 720px;
  padding-top: 1rem;
  color: white;
  text-shadow: 0 8px 32px rgba(0, 0, 0, 0.35);
}

.hero-kicker {
  display: inline-flex;
  align-items: center;
  color: rgba(255, 255, 255, 0.9);
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 3px;
  margin-bottom: 16px;
  padding: 6px 20px;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.18);
}

.hero-text h1 {
  font-size: clamp(3rem, 7vw, 5.8rem);
  font-weight: 800;
  color: white;
  line-height: 1.02;
  margin-bottom: 12px;
  letter-spacing: -0.02em;
  background: linear-gradient(135deg, #ffffff 0%, rgba(255, 255, 255, 0.9) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-text h2 {
  font-size: clamp(1.6rem, 3.2vw, 3.1rem);
  font-weight: 500;
  color: white;
  line-height: 1.2;
  margin-bottom: 20px;
  letter-spacing: 0;
}

.hero-text p {
  max-width: 620px;
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.85;
  margin-bottom: 32px;
}

.hero-text .btn-primary {
  padding: 16px 32px;
  font-size: 15px;
  font-weight: 600;
}

/* 轮播图文字切换动画 */
.hero-fade-enter-active {
  transition: opacity 1s cubic-bezier(0.4, 0, 0.2, 1);
}
.hero-fade-leave-active {
  transition: opacity 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}
.hero-fade-enter-from {
  opacity: 0;
}
.hero-fade-leave-to {
  opacity: 0;
}

.hero-controls {
  position: absolute;
  bottom: 4.5rem;
  left: 0;
  width: 100%;
  z-index: 10;
  pointer-events: none;
}

.hero-controls-inner {
  max-width: 100%;
  margin: 0;
  padding: 0 2.5rem 28px 5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  pointer-events: auto;
}

.hero-arrow-group {
  display: flex;
  align-items: center;
  gap: 24px;
}

.control-btn {
  width: 56px;
  height: 56px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0.85;
  transition: all 0.3s ease;
}

.control-btn:hover {
  opacity: 1;
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.control-btn svg {
  width: 20px;
  height: 20px;
}

.carousel-indicators {
  display: flex;
  align-items: center;
  gap: 16px;
}

.indicator {
  width: 8px;
  height: 8px;
  padding: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.indicator.active {
  width: 28px;
  border-radius: 4px;
  background: var(--primary-color);
}

.features {
  padding: 80px 0;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
}

.features-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 24px;
}

.feature-card {
  flex: 0 0 calc(25% - 18px);
  background: white;
  min-height: 260px;
  padding: 32px;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  text-align: center;
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--primary-color), var(--primary-light));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--shadow-xl);
  border-color: rgba(0, 168, 89, 0.2);
}

.feature-card:hover::before {
  opacity: 1;
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  background: linear-gradient(135deg, rgba(0, 168, 89, 0.1) 0%, rgba(0, 168, 89, 0.05) 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.feature-icon-img {
  width: 40px;
  height: 40px;
  object-fit: contain;
  transition: all 0.3s ease;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1);
}

.feature-card h3 {
  font-size: 1.35rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
}

.feature-card p {
  color: var(--text-muted);
  line-height: 1.75;
  font-size: 14px;
  white-space: pre-wrap;
}

.company-intro {
  padding: 100px 0;
  background: var(--bg-light);
}

.intro-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1.15fr);
  gap: 64px;
  align-items: center;
}

.intro-image {
  position: relative;
}

.intro-image img {
  width: 100%;
  height: 450px;
  object-fit: cover;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

.intro-image::after {
  content: '';
  position: absolute;
  bottom: -20px;
  right: -20px;
  width: 100%;
  height: 100%;
  background: var(--primary-color);
  border-radius: var(--radius-xl);
  z-index: -1;
  opacity: 0.1;
}

.intro-content .section-subtitle {
  margin-bottom: 16px;
}

.intro-content h2 {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.2;
  margin-bottom: 24px;
}

.intro-content p {
  color: var(--text-secondary);
  line-height: 1.85;
  margin-bottom: 16px;
  font-size: 15px;
}

.xre-spotlight {
  padding: 100px 0;
  color: white;
  background: 
    linear-gradient(90deg, rgba(16, 32, 51, 0.98) 0%, rgba(16, 32, 51, 0.92) 50%, rgba(16, 32, 51, 0.98) 100%),
    var(--xre-spotlight-image) center/cover;
  position: relative;
  overflow: hidden;
}

.xre-spotlight::before {
  content: '';
  position: absolute;
  top: 0;
  left: -50%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.03), transparent);
  animation: shimmer 3s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.xre-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(320px, 0.85fr);
  gap: 48px;
  align-items: center;
}

.xre-copy .section-subtitle {
  color: var(--primary-light);
  background: rgba(0, 168, 89, 0.15);
  margin-bottom: 16px;
}

.xre-copy h2 {
  max-width: 680px;
  color: white;
  font-size: clamp(2.2rem, 4vw, 3.2rem);
  line-height: 1.15;
  margin-bottom: 20px;
  font-weight: 700;
}

.xre-copy p {
  max-width: 680px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.85;
  margin-bottom: 32px;
  font-size: 16px;
}

.xre-metrics {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.xre-metric {
  min-height: 130px;
  padding: 24px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.xre-metric:hover {
  background: rgba(0, 168, 89, 0.15);
}

.xre-metric strong {
  display: block;
  color: var(--primary-light);
  font-size: 2.5rem;
  line-height: 1.1;
  margin-bottom: 8px;
  font-weight: 700;
}

.xre-metric span {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
}

.latest-news {
  padding: 80px 0;
  background-color: var(--bg-light);
}

.news-list {
  display: grid;
  grid-template-columns: repeat(3, 380px);
  gap: 24px;
  justify-content: center;
}

@media (max-width: 1200px) {
  .news-list {
    grid-template-columns: repeat(2, 380px);
  }
}

@media (max-width: 800px) {
  .news-list {
    grid-template-columns: 1fr;
  }
}

.news-card {
  background: white;
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-color);
}

.news-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-xl);
  border-color: rgba(0, 168, 89, 0.2);
}

.news-image {
  position: relative;
  height: 220px;
  overflow: hidden;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.news-card:hover .news-image img {
  transform: scale(1.08);
}

.news-date {
  position: absolute;
  bottom: 16px;
  left: 16px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  color: white;
  padding: 8px 16px;
  font-size: 12px;
  font-weight: 500;
  border-radius: 20px;
  box-shadow: 0 4px 12px rgba(0, 168, 89, 0.3);
}

.news-content {
  padding: 24px;
}

.news-content h3 {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.news-card:hover .news-content h3 {
  color: var(--primary-color);
}

.news-content p {
  color: var(--text-muted);
  font-size: 14px;
  line-height: 1.7;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.read-more {
  display: inline-flex;
  align-items: center;
  color: var(--primary-color);
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  gap: 4px;
}

.read-more:hover {
  gap: 8px;
}

.latest-products {
  padding: 80px 0;
  background-color: white;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
  max-width: 1200px;
  margin: 0 auto;
}

.product-card {
  background: white;
  border-radius: var(--radius-lg);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  width: 100%;
  max-width: 380px;
  margin: 0 auto;
}

.product-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--shadow-xl);
  border-color: rgba(0, 168, 89, 0.3);
}

.product-image {
  position: relative;
  width: 100%;
  height: 280px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.1);
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(0, 168, 89, 0.95) 0%, rgba(0, 143, 77, 0.95) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  color: white;
  font-weight: 600;
  font-size: 15px;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.product-info {
  padding: 20px;
  text-align: center;
}

.product-info h3 {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.product-price {
  font-size: 1.5rem;
  font-weight: 700;
  color: #e53e3e;
}

.contact-cta {
  background: linear-gradient(135deg, #0a2e1f 0%, #0d3d2a 30%, #00a859 70%, #00c864 100%);
  padding: 100px 0;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.cta-bg-pattern {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(circle at 20% 50%, rgba(255, 255, 255, 0.08) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255, 255, 255, 0.05) 0%, transparent 40%),
    radial-gradient(circle at 50% 80%, rgba(255, 255, 255, 0.06) 0%, transparent 45%);
  animation: ctaPulse 6s ease-in-out infinite;
}

@keyframes ctaPulse {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 1; }
}

.cta-content {
  position: relative;
  z-index: 1;
  max-width: 700px;
  margin: 0 auto;
}

.cta-eyebrow {
  display: inline-block;
  font-size: 0.8rem;
  font-weight: 700;
  letter-spacing: 3px;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.15);
  padding: 0.4rem 1.2rem;
  border-radius: 20px;
  margin-bottom: 1.5rem;
  backdrop-filter: blur(10px);
}

.cta-content h2 {
  font-size: clamp(1.8rem, 4vw, 2.8rem);
  font-weight: 700;
  color: white;
  margin-bottom: 1rem;
  line-height: 1.3;
}

.cta-content p {
  color: rgba(255, 255, 255, 0.85);
  font-size: 1.1rem;
  margin-bottom: 2.5rem;
  line-height: 1.7;
}

.cta-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.cta-content .btn-primary {
  background: white;
  color: #00a859;
  padding: 1rem 2.5rem;
  font-size: 1rem;
  font-weight: 700;
  border-radius: 12px;
}

.cta-content .btn-primary:hover {
  background: rgba(255, 255, 255, 0.95);
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

.btn-outline-light {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.5);
  padding: 1rem 2.5rem;
  font-size: 1rem;
  font-weight: 700;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.btn-outline-light:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
  transform: translateY(-3px);
}

.text-center {
  text-align: center;
  margin-top: 48px;
}

@media (max-width: 1024px) {
  .feature-card {
    flex: 0 0 calc(33.33% - 16px);
  }

  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero {
    min-height: 650px;
    height: 85vh;
  }

  .hero-content {
    text-align: center;
    transform: translateY(-3vh);
  }

  .hero-text {
    padding-top: 1rem;
    margin: 0 auto;
  }

  .hero-text h1 {
    font-size: 2.5rem;
  }

  .hero-text h2 {
    font-size: 1.4rem;
  }

  .hero-text p {
    font-size: 15px;
  }

  .hero-controls {
    bottom: 2rem;
  }

  .hero-controls-inner {
    margin: 0 24px;
    padding: 0 0 16px;
  }

  .hero-arrow-group {
    gap: 16px;
  }

  .control-btn {
    width: 48px;
    height: 48px;
  }

  .carousel-indicators {
    gap: 12px;
  }

  .indicator {
    width: 8px;
    height: 8px;
  }

  .indicator.active {
    width: 24px;
  }

  .feature-card {
    flex: 0 0 calc(50% - 12px);
  }

  .news-list,
  .products-grid {
    grid-template-columns: 1fr;
  }

  .xre-grid,
  .xre-metrics,
  .intro-grid {
    grid-template-columns: 1fr;
  }

  .intro-grid {
    gap: 40px;
  }

  .intro-image img {
    width: 100%;
    height: 280px !important;
    object-fit: cover;
    border-radius: var(--radius-xl);
  }

  .xre-grid {
    gap: 32px;
  }

  .section-title {
    font-size: 1.75rem;
  }

  .company-intro {
    padding: 60px 0;
  }

  .xre-spotlight,
  .latest-news,
  .latest-products,
  .features {
    padding: 60px 0;
  }

  .contact-cta {
    padding: 60px 0;
  }
}

@media (max-width: 480px) {
  .feature-card {
    flex: 0 0 100%;
  }
}
</style>
