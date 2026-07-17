<template>
  <div class="dashboard">
    <AdminSidebar @logout="handleLogout" />
    
    <main class="main-content">
      <header class="top-bar">
        <div class="page-header">
          <h1>仪表盘</h1>
          <p class="page-subtitle">数据概览与最新动态</p>
        </div>
        <TopUserInfo />
      </header>
      
      <div class="stats-cards" v-if="!loading">
        <div v-for="card in statCards" :key="card.key" class="stat-card" :class="card.color">
          <div class="stat-icon">
            <component :is="card.icon" />
          </div>
          <div class="stat-content">
            <p class="stat-value">{{ stats[card.key] || 0 }}</p>
            <p class="stat-label">{{ card.label }}</p>
          </div>
        </div>
      </div>
      
      <div class="stats-cards skeleton-grid" v-else>
        <div v-for="i in 8" :key="i" class="stat-card skeleton-card">
          <div class="skeleton-icon skeleton-animate"></div>
          <div class="skeleton-content">
            <div class="skeleton-value skeleton-animate"></div>
            <div class="skeleton-label skeleton-animate"></div>
          </div>
        </div>
      </div>
      
      <div class="charts-section">
        <div class="chart-card">
          <div class="card-header">
            <h3>内容概览</h3>
          </div>
          <div class="content-summary">
            <div class="summary-item">
              <div class="summary-icon home">
                <House />
              </div>
              <div class="summary-content">
                <h4>首页模块</h4>
                <p>轮播图 <strong>{{ stats.carouselCount || 0 }}</strong> 张 · 核心优势 <strong>{{ stats.featureCount || 0 }}</strong> 项</p>
              </div>
            </div>
            <div class="summary-item">
              <div class="summary-icon product">
                <Goods />
              </div>
              <div class="summary-content">
                <h4>产品服务</h4>
                <p>产品 <strong>{{ stats.productCount || 0 }}</strong> 个（已发布 <strong>{{ stats.enabledProductCount || 0 }}</strong>） · 分类 <strong>{{ stats.categoryCount || 0 }}</strong> 个</p>
              </div>
            </div>
            <div class="summary-item">
              <div class="summary-icon article">
                <Document />
              </div>
              <div class="summary-content">
                <h4>新闻动态</h4>
                <p>文章 <strong>{{ stats.articleCount || 0 }}</strong> 篇（已发布 <strong>{{ stats.publishedArticleCount || 0 }}</strong>）</p>
              </div>
            </div>
            <div class="summary-item">
              <div class="summary-icon about">
                <UserFilled />
              </div>
              <div class="summary-content">
                <h4>关于我们</h4>
                <p>团队成员 <strong>{{ stats.teamCount || 0 }}</strong> 人 · 荣誉奖项 <strong>{{ stats.awardCount || 0 }}</strong> 项</p>
              </div>
            </div>
          </div>
        </div>
        
        <div class="chart-card">
          <div class="card-header">
            <h3>最新留言</h3>
            <router-link to="/messages" class="view-all">
              查看全部
              <ArrowRight />
            </router-link>
          </div>
          
          <div class="messages-list" v-if="latestMessages.length > 0">
            <div v-for="msg in latestMessages" :key="msg.id" class="message-item">
              <div class="message-avatar" :style="{ background: getAvatarColor(msg.name) }">
                {{ msg.name ? msg.name.charAt(0).toUpperCase() : '?' }}
              </div>
              <div class="message-content">
                <div class="message-header">
                  <span class="message-name">{{ msg.name }}</span>
                  <span class="message-time">{{ formatTime(msg.created_at) }}</span>
                </div>
                <div class="message-subject" v-if="msg.subject">{{ msg.subject }}</div>
                <p class="message-text">{{ truncateText(msg.message, 60) }}</p>
              </div>
            </div>
          </div>
          
          <div class="empty-messages" v-else-if="!loading">
            <div class="empty-icon">
              <ChatDotRound />
            </div>
            <p class="empty-title">暂无留言</p>
            <p class="empty-desc">访客留言将在这里显示</p>
          </div>
          
          <div class="messages-skeleton" v-else>
            <div v-for="i in 4" :key="i" class="message-skeleton-item">
              <div class="skeleton-avatar skeleton-animate"></div>
              <div class="skeleton-msg-content">
                <div class="skeleton-msg-header">
                  <div class="skeleton-name skeleton-animate"></div>
                  <div class="skeleton-time skeleton-animate"></div>
                </div>
                <div class="skeleton-text skeleton-animate"></div>
                <div class="skeleton-text-long skeleton-animate"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { ref, onMounted, computed } from 'vue'
import api from '@/api'
import { 
  Picture, Star, Goods, Document, PriceTag, ChatDotRound,
  House, UserFilled, ArrowRight
} from '@element-plus/icons-vue'

const loading = ref(true)
const latestMessages = ref([])
const stats = ref({
  carouselCount: 0,
  featureCount: 0,
  productCount: 0,
  articleCount: 0,
  categoryCount: 0,
  teamCount: 0,
  awardCount: 0,
  messageCount: 0,
  publishedArticleCount: 0,
  enabledProductCount: 0
})

const statCards = computed(() => [
  { key: 'carouselCount', label: '轮播图', icon: Picture, color: 'blue' },
  { key: 'featureCount', label: '核心优势', icon: Star, color: 'green' },
  { key: 'productCount', label: '产品总数', icon: Goods, color: 'orange' },
  { key: 'articleCount', label: '文章总数', icon: Document, color: 'purple' },
  { key: 'categoryCount', label: '分类数量', icon: PriceTag, color: 'cyan' },
  { key: 'teamCount', label: '团队成员', icon: UserFilled, color: 'indigo' },
  { key: 'awardCount', label: '荣誉奖项', icon: Star, color: 'pink' },
  { key: 'messageCount', label: '留言数量', icon: ChatDotRound, color: 'red' }
])

const avatarColors = [
  'linear-gradient(135deg, #0891B2, #22D3EE)',
  'linear-gradient(135deg, #059669, #10B981)',
  'linear-gradient(135deg, #D97706, #F59E0B)',
  'linear-gradient(135deg, #7C3AED, #8B5CF6)',
  'linear-gradient(135deg, #DC2626, #EF4444)',
  'linear-gradient(135deg, #0891B2, #06B6D4)',
  'linear-gradient(135deg, #4F46E5, #6366F1)',
  'linear-gradient(135deg, #DB2777, #EC4899)'
]

const getAvatarColor = (name) => {
  if (!name) return avatarColors[0]
  const index = name.charCodeAt(0) % avatarColors.length
  return avatarColors[index]
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const loadStats = async () => {
  try {
    const res = await api.get('/api/dashboard/stats')
    if (res.data.data) {
      stats.value = res.data.data
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadLatestMessages = async () => {
  try {
    const res = await api.get('/api/dashboard/messages/latest')
    if (res.data.data) {
      latestMessages.value = res.data.data
    }
  } catch (error) {
    console.error('加载最新留言失败:', error)
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}

const truncateText = (text, maxLength) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}

onMounted(async () => {
  await Promise.all([loadStats(), loadLatestMessages()])
  loading.value = false
})
</script>

<style scoped>
.dashboard {
  display: flex;
  min-height: 100vh;
  background-color: #F1F5F9;
}

.main-content {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.top-bar {
  background-color: white;
  padding: 1.25rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 10;
}

.page-header h1 {
  color: #0F172A;
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  line-height: 1.2;
}

.page-subtitle {
  color: #64748B;
  font-size: 0.85rem;
  margin: 0.25rem 0 0 0;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  padding: 1.5rem 2rem;
}

.stat-card {
  background-color: white;
  border-radius: 14px;
  padding: 1.25rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);
  transition: all 0.2s ease-out;
  border: 1px solid #F1F5F9;
  cursor: default;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  flex-shrink: 0;
  transition: transform 0.2s ease-out;
}

.stat-card:hover .stat-icon {
  transform: scale(1.05);
}

.stat-icon :deep(svg) {
  width: 24px;
  height: 24px;
}

.stat-card.blue .stat-icon {
  background: linear-gradient(135deg, #ECFEFF, #CFFAFE);
  color: #0891B2;
}

.stat-card.green .stat-icon {
  background: linear-gradient(135deg, #F0FDF4, #DCFCE7);
  color: #16A34A;
}

.stat-card.orange .stat-icon {
  background: linear-gradient(135deg, #FFF7ED, #FFEDD5);
  color: #EA580C;
}

.stat-card.purple .stat-icon {
  background: linear-gradient(135deg, #FAF5FF, #F3E8FF);
  color: #9333EA;
}

.stat-card.red .stat-icon {
  background: linear-gradient(135deg, #FEF2F2, #FEE2E2);
  color: #DC2626;
}

.stat-card.cyan .stat-icon {
  background: linear-gradient(135deg, #ECFEFF, #CFFAFE);
  color: #06B6D4;
}

.stat-card.indigo .stat-icon {
  background: linear-gradient(135deg, #EEF2FF, #E0E7FF);
  color: #4F46E5;
}

.stat-card.pink .stat-icon {
  background: linear-gradient(135deg, #FDF2F8, #FCE7F3);
  color: #DB2777;
}

.stat-content {
  flex: 1;
  min-width: 0;
}

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: #0F172A;
  line-height: 1.2;
  margin: 0;
  font-variant-numeric: tabular-nums;
}

.stat-label {
  color: #64748B;
  font-size: 0.85rem;
  margin: 0.25rem 0 0 0;
  font-weight: 500;
}

.skeleton-grid {
  pointer-events: none;
}

.skeleton-card {
  cursor: default;
}

.skeleton-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  background-color: #F1F5F9;
  flex-shrink: 0;
}

.skeleton-content {
  flex: 1;
}

.skeleton-value {
  height: 1.75rem;
  width: 50px;
  background-color: #F1F5F9;
  border-radius: 6px;
  margin-bottom: 0.5rem;
}

.skeleton-label {
  height: 0.85rem;
  width: 70px;
  background-color: #F1F5F9;
  border-radius: 4px;
}

.skeleton-animate {
  position: relative;
  overflow: hidden;
}

.skeleton-animate::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  transform: translateX(-100%);
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.6), transparent);
  animation: shimmer 1.5s infinite;
}

@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

.charts-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  padding: 0 2rem 2rem;
}

.chart-card {
  background-color: white;
  border-radius: 14px;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);
  border: 1px solid #F1F5F9;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.25rem;
}

.chart-card h3 {
  margin: 0;
  color: #0F172A;
  font-size: 1.05rem;
  font-weight: 600;
}

.view-all {
  color: #0891B2;
  font-size: 0.85rem;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-weight: 500;
  transition: color 0.2s;
}

.view-all:hover {
  color: #0E7490;
}

.view-all :deep(svg) {
  width: 14px;
  height: 14px;
}

.content-summary {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background-color: #F8FAFC;
  border-radius: 12px;
  transition: all 0.2s ease-out;
  cursor: default;
}

.summary-item:hover {
  background-color: #F1F5F9;
}

.summary-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  flex-shrink: 0;
}

.summary-icon :deep(svg) {
  width: 22px;
  height: 22px;
}

.summary-icon.home {
  background: linear-gradient(135deg, #ECFEFF, #CFFAFE);
  color: #0891B2;
}

.summary-icon.product {
  background: linear-gradient(135deg, #FFF7ED, #FFEDD5);
  color: #EA580C;
}

.summary-icon.article {
  background: linear-gradient(135deg, #FAF5FF, #F3E8FF);
  color: #9333EA;
}

.summary-icon.about {
  background: linear-gradient(135deg, #F0FDF4, #DCFCE7);
  color: #16A34A;
}

.summary-content {
  flex: 1;
  min-width: 0;
}

.summary-item h4 {
  color: #0F172A;
  margin: 0 0 0.25rem 0;
  font-size: 0.95rem;
  font-weight: 600;
}

.summary-item p {
  color: #64748B;
  font-size: 0.82rem;
  margin: 0;
  line-height: 1.5;
}

.summary-item strong {
  color: #0F172A;
  font-weight: 600;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  max-height: 420px;
  overflow-y: auto;
  padding-right: 0.25rem;
}

.messages-list::-webkit-scrollbar {
  width: 4px;
}

.messages-list::-webkit-scrollbar-track {
  background: transparent;
}

.messages-list::-webkit-scrollbar-thumb {
  background: #E2E8F0;
  border-radius: 2px;
}

.messages-list::-webkit-scrollbar-thumb:hover {
  background: #CBD5E1;
}

.message-item {
  display: flex;
  gap: 0.75rem;
  padding: 0.875rem;
  border-radius: 12px;
  background-color: #F8FAFC;
  transition: all 0.2s ease-out;
  cursor: pointer;
}

.message-item:hover {
  background-color: #F1F5F9;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.95rem;
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.25rem;
}

.message-name {
  font-weight: 600;
  color: #0F172A;
  font-size: 0.9rem;
}

.message-time {
  font-size: 0.75rem;
  color: #94A3B8;
  flex-shrink: 0;
}

.message-subject {
  font-size: 0.82rem;
  color: #334155;
  margin-bottom: 0.25rem;
  font-weight: 500;
}

.message-text {
  font-size: 0.8rem;
  color: #64748B;
  margin: 0;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.empty-messages {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3.5rem 1rem;
  color: #94A3B8;
}

.empty-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: #F1F5F9;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  color: #CBD5E1;
}

.empty-icon :deep(svg) {
  width: 32px;
  height: 32px;
}

.empty-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: #64748B;
  margin: 0 0 0.25rem 0;
}

.empty-desc {
  font-size: 0.82rem;
  margin: 0;
}

.messages-skeleton {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.message-skeleton-item {
  display: flex;
  gap: 0.75rem;
  padding: 0.875rem;
  border-radius: 12px;
  background-color: #F8FAFC;
}

.skeleton-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background-color: #E2E8F0;
  flex-shrink: 0;
}

.skeleton-msg-content {
  flex: 1;
}

.skeleton-msg-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.skeleton-name {
  height: 0.9rem;
  width: 60px;
  background-color: #E2E8F0;
  border-radius: 4px;
}

.skeleton-time {
  height: 0.75rem;
  width: 50px;
  background-color: #E2E8F0;
  border-radius: 4px;
}

.skeleton-text {
  height: 0.75rem;
  width: 80%;
  background-color: #E2E8F0;
  border-radius: 4px;
  margin-bottom: 0.4rem;
}

.skeleton-text-long {
  height: 0.75rem;
  width: 100%;
  background-color: #E2E8F0;
  border-radius: 4px;
}

@media (max-width: 1280px) {
  .stats-cards {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}

@media (max-width: 1024px) {
  .stats-cards {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
  
  .charts-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .top-bar {
    padding: 1rem;
  }
  
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
    padding: 1rem;
    gap: 0.75rem;
  }
  
  .stat-card {
    padding: 1rem;
  }
  
  .stat-icon {
    width: 44px;
    height: 44px;
  }
  
  .stat-icon :deep(svg) {
    width: 20px;
    height: 20px;
  }
  
  .stat-value {
    font-size: 1.4rem;
  }
  
  .charts-section {
    padding: 0 1rem 1rem;
  }
  
  .chart-card {
    padding: 1rem;
  }
  
  .user-name {
    display: none;
  }
}

@media (prefers-reduced-motion: reduce) {
  .stat-card,
  .stat-icon,
  .summary-item,
  .message-item,
  .view-all,
  .skeleton-animate::after {
    transition: none;
    animation: none;
  }
  
  .stat-card:hover {
    transform: none;
  }
}
</style>
