<template>
  <div class="news">
    <section class="news-hero" :style="heroStyle">
      <div class="hero-overlay"></div>
      <div class="container">
        <div class="hero-content">
          <h1>{{ heroTitle || '新闻动态' }}</h1>
          <p>{{ heroSubtitle || '了解公司最新动态和行业资讯' }}</p>
        </div>
      </div>
    </section>
    
    <section class="news-filter" :style="{ top: filterTop }">
      <div class="container">
        <div class="filter-wrapper">
          <div class="filter-row">
            <span class="filter-label">新闻分类</span>
            <div class="filter-tabs">
              <button
                :class="['filter-tab', { active: selectedCategoryId === null }]"
                @click="selectAllCategories"
              >
                全部
              </button>
              <button
                v-for="category in categories"
                :key="category.id"
                :class="['filter-tab', { active: selectedCategoryId === category.id }]"
                @click="selectCategory(category.id)"
              >
                {{ category.name }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="news-list">
      <div class="container">
        <div class="news-grid">
          <div 
            v-for="article in articles" 
            :key="article.id" 
            class="news-card"
            @click="$router.push(`/news/${article.id}`)"
          >
            <div class="news-image-wrapper">
              <img :src="article.coverImage" alt="文章封面" v-if="article.coverImage" />
              <div class="news-category" v-if="getCategoryName(article.categoryId)">{{ getCategoryName(article.categoryId) }}</div>
            </div>
            <div class="news-content">
              <h3>{{ article.title }}</h3>
              <p>{{ article.summary }}</p>
              <div class="news-meta">
                <span class="author">{{ article.author }}</span>
                <span class="divider">·</span>
                <span class="date">{{ formatDate(article.createdAt) }}</span>
                <span class="divider">·</span>
                <span class="views">👁 {{ article.views }}</span>
              </div>
              <div class="read-more">
                <span>阅读更多</span>
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
                  <path d="M5 12h14M12 5l7 7-7 7"/>
                </svg>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="articles.length === 0" class="empty-state">
          <svg viewBox="0 0 100 100" fill="none" width="80" height="80">
            <circle cx="50" cy="50" r="45" stroke="#e2e8f0" stroke-width="2" fill="none"/>
            <path d="M35 50 L50 35 L65 50 L50 65 Z" stroke="#718096" stroke-width="2" fill="none"/>
          </svg>
          <p>暂无新闻资讯</p>
        </div>

        <div class="pagination">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30]"
            :page-size="pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { articleApi } from '@/api'
import { siteCache } from '@/utils/siteCache'

const articles = ref([])
const categories = ref([])
const selectedCategoryId = ref(null)
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)
const heroTitle = ref('')
const heroSubtitle = ref('')
const heroBgImage = ref('')
const bgPosition = ref('center')
const filterTop = ref('60px')
let rafId = null

const heroStyle = computed(() => {
  if (heroBgImage.value) {
    const pos = bgPosition.value || 'center'
    return {
      backgroundImage: `url(${heroBgImage.value})`,
      backgroundPosition: `center ${pos}`
    }
  }
  return {}
})

const updateFilterTop = () => {
  if (rafId) {
    cancelAnimationFrame(rafId)
  }
  rafId = requestAnimationFrame(() => {
    const header = document.querySelector('header.header')
    if (header) {
      filterTop.value = `${header.offsetHeight}px`
    }
    rafId = null
  })
}

const handleScroll = () => {
  updateFilterTop()
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const loadHero = async () => {
  try {
    const d = await siteCache.getNewsHero()
    if (d.heroTitle) heroTitle.value = d.heroTitle
    if (d.heroSubtitle) heroSubtitle.value = d.heroSubtitle
    if (d.heroBgImage) heroBgImage.value = d.heroBgImage
    if (d.bgPosition) bgPosition.value = d.bgPosition
  } catch (e) {
    console.error('获取新闻页面文案失败', e)
  }
}

const loadCategories = async () => {
  try {
    categories.value = await siteCache.getNewsCategories()
  } catch (e) {
    console.error('获取新闻分类失败', e)
  }
}

const loadArticles = async () => {
  try {
    const res = await articleApi.getPublishedArticles(currentPage.value, pageSize.value, selectedCategoryId.value)
    articles.value = res.data.data.content || []
    total.value = res.data.data.totalElements || 0
  } catch (error) {
    console.error('获取文章列表失败:', error)
  }
}

const selectAllCategories = () => {
  selectedCategoryId.value = null
  currentPage.value = 0
  loadArticles()
}

const getCategoryName = (categoryId) => {
  if (!categoryId) return ''
  const category = categories.value.find(c => c.id === categoryId)
  return category ? category.name : ''
}

const selectCategory = (categoryId) => {
  selectedCategoryId.value = categoryId
  currentPage.value = 0
  loadArticles()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadArticles()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadArticles()
}

onMounted(() => {
  loadHero()
  loadCategories()
  loadArticles()
  updateFilterTop()
  nextTick(() => {
    updateFilterTop()
  })
  window.addEventListener('scroll', handleScroll, { passive: true })
  window.addEventListener('resize', updateFilterTop)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', updateFilterTop)
})
</script>

<style scoped>
.news {
  padding-top: 0;
}

/* Hero */
.news-hero {
  position: relative;
  min-height: 600px;
  padding-top: 120px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background-image: linear-gradient(135deg, #0a1929 0%, #1a3a5c 50%, #0a1929 100%);
  background-size: cover;
  background-position: center;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(10,25,41,0.6) 0%, rgba(10,25,41,0.4) 100%);
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem 1rem;
  color: white;
}

.hero-content h1 {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 800;
  margin-bottom: 0.75rem;
}

.hero-content p {
  font-size: 1.05rem;
  color: rgba(255, 255, 255, 0.75);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

/* News Filter */
.news-filter {
  padding: 16px 0;
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid #e8ecf1;
  position: sticky;
  top: 60px;
  z-index: 100;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  transition: box-shadow 0.3s ease;
  margin-top: -1px;
}

.filter-wrapper {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.filter-label {
  font-size: 0.88rem;
  font-weight: 600;
  color: #374151;
  white-space: nowrap;
  min-width: 64px;
}

.filter-tabs {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  flex: 1;
}

.filter-tab {
  padding: 7px 22px;
  font-size: 0.88rem;
  font-weight: 500;
  color: #4b5563;
  background: #f3f4f6;
  border: 1px solid transparent;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s ease;
}

.filter-tab:hover {
  color: #00a859;
  background: #ecfdf5;
  border-color: rgba(0, 168, 89, 0.2);
}

.filter-tab.active {
  color: white;
  background: #00a859;
  border-color: #00a859;
  box-shadow: 0 2px 8px rgba(0, 168, 89, 0.25);
}

/* News List */
.news-list {
  padding: 3rem 0 5rem;
  background: #f7fafc;
  min-height: 60vh;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(370px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.news-card {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.news-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
  border-color: rgba(0, 168, 89, 0.2);
}

.news-image-wrapper {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.news-image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.news-card:hover .news-image-wrapper img {
  transform: scale(1.08);
}

.news-category {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 0.3rem 0.75rem;
  font-size: 0.75rem;
  font-weight: 700;
  color: white;
  background: linear-gradient(135deg, #00a859, #008f4d);
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 168, 89, 0.3);
}

.news-content {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.news-content h3 {
  font-size: 1.15rem;
  font-weight: 700;
  color: #1a365d;
  margin-bottom: 0.75rem;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.news-card:hover .news-content h3 {
  color: #00a859;
}

.news-content p {
  color: #718096;
  font-size: 0.9rem;
  line-height: 1.7;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.8rem;
  color: #a0aec0;
  margin-bottom: 1rem;
}

.news-meta .divider {
  color: #cbd5e0;
}

.read-more {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #00a859;
  font-size: 0.85rem;
  font-weight: 700;
  transition: all 0.3s ease;
}

.read-more:hover {
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 4rem 0;
}

.empty-state svg {
  margin-bottom: 1rem;
}

.empty-state p {
  color: #718096;
  font-size: 1rem;
}

.pagination {
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .news-hero {
    min-height: 400px;
  }

  .filter-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .filter-tabs {
    width: 100%;
  }

  .filter-tab {
    padding: 6px 16px;
    font-size: 0.82rem;
  }

  .news-grid {
    grid-template-columns: 1fr;
  }

  .news-image-wrapper {
    height: 180px;
  }

  .news-list {
    padding: 2rem 0 3rem;
  }

  .hero-content h1 {
    font-size: 1.8rem;
  }
}
</style>