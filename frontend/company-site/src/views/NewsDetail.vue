<template>
  <div class="news-detail">
    <section class="news-header">
      <div class="container">
        <button @click="$router.back()" class="back-btn">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="20" height="20">
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
          <span>返回列表</span>
        </button>
        <div v-if="article" class="article-header">
          <span class="article-category">{{ article.category || '公司新闻' }}</span>
          <h1>{{ article.title }}</h1>
          <div class="article-meta">
            <span class="author">{{ article.author }}</span>
            <span class="date">{{ formatDate(article.createdAt) }}</span>
            <span class="views">👁 {{ article.views }}</span>
          </div>
        </div>
      </div>
    </section>
    
    <section class="news-content">
      <div class="container">
        <div v-if="article" class="content-wrapper">
          <img v-if="article.coverImage" :src="article.coverImage" alt="文章封面" class="cover-image" />
          <div class="article-content" v-html="sanitizeHtml(article.content)"></div>
          
          <div class="article-footer">
            <div class="article-tags">
              <span v-for="tag in article.tags" :key="tag" class="article-tag">{{ tag }}</span>
            </div>
            <div class="share-links">
              <span>分享：</span>
              <button class="share-btn">微信</button>
              <button class="share-btn">微博</button>
              <button class="share-btn">LinkedIn</button>
            </div>
          </div>
        </div>
        <div v-else class="loading-state">
          <svg viewBox="0 0 50 50" fill="none" width="40" height="40" class="loading-spinner">
            <circle cx="25" cy="25" r="20" stroke="var(--primary-color)" stroke-width="4" fill="none" stroke-linecap="round">
              <animateTransform attributeName="transform" type="rotate" from="0 25 25" to="360 25 25" dur="1s" repeatCount="indefinite"/>
            </circle>
          </svg>
          <p>文章加载中...</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { articleApi } from '@/api'

const route = useRoute()
const article = ref(null)

// XSS 防护：移除危险标签和事件处理器
const sanitizeHtml = (html) => {
  if (!html) return ''
  return String(html)
    .replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '')
    .replace(/<iframe\b[^<]*(?:(?!<\/iframe>)<[^<]*)*<\/iframe>/gi, '')
    .replace(/<embed\b[^>]*>/gi, '')
    .replace(/<object\b[^<]*(?:(?!<\/object>)<[^<]*)*<\/object>/gi, '')
    .replace(/\bon\w+\s*=\s*"[^"]*"/gi, '')
    .replace(/\bon\w+\s*=\s*'[^']*'/gi, '')
    .replace(/javascript\s*:/gi, '')
    .replace(/vbscript\s*:/gi, '')
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

onMounted(async () => {
  const id = route.params.id
  try {
    const res = await articleApi.getArticleById(id)
    article.value = res.data.data
  } catch (error) {
    console.error('获取文章详情失败:', error)
  }
})
</script>

<style scoped>
.news-detail {
  padding-top: 0;
}

.news-header {
  padding: 32px 0;
  padding-top: 100px;
  background: linear-gradient(135deg, var(--secondary-color) 0%, #2c5282 100%);
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.9);
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 24px;
}

.back-btn:hover {
  color: white;
  gap: 12px;
}

.article-header {
  color: white;
}

.article-category {
  display: inline-block;
  padding: 4px 16px;
  font-size: 12px;
  font-weight: 600;
  color: var(--primary-light);
  background: rgba(0, 168, 89, 0.15);
  border-radius: 20px;
  margin-bottom: 16px;
}

.article-header h1 {
  font-size: clamp(1.8rem, 4vw, 2.8rem);
  font-weight: 700;
  margin-bottom: 16px;
  line-height: 1.25;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
}

.news-content {
  padding: 60px 0;
  background: var(--bg-light);
}

.content-wrapper {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: var(--radius-lg);
  padding: 40px;
  box-shadow: var(--shadow-md);
}

.cover-image {
  width: 100%;
  height: auto;
  margin-bottom: 32px;
  border-radius: var(--radius-md);
}

.article-content {
  line-height: 1.9;
  color: var(--text-primary);
  font-size: 16px;
}

.article-content h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-top: 32px;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid var(--border-color);
}

.article-content h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-top: 24px;
  margin-bottom: 12px;
}

.article-content p {
  margin-bottom: 16px;
}

.article-content img {
  max-width: 100%;
  height: auto;
  margin: 16px 0;
  border-radius: var(--radius-sm);
}

.article-content ul,
.article-content ol {
  padding-left: 24px;
  margin-bottom: 16px;
}

.article-content li {
  margin-bottom: 8px;
}

.article-content blockquote {
  border-left: 4px solid var(--primary-color);
  padding-left: 16px;
  margin: 20px 0;
  color: var(--text-muted);
  font-style: italic;
}

.article-footer {
  margin-top: 40px;
  padding-top: 24px;
  border-top: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.article-tag {
  padding: 4px 12px;
  font-size: 12px;
  color: var(--text-secondary);
  background: var(--bg-light);
  border-radius: 4px;
}

.share-links {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: var(--text-muted);
}

.share-btn {
  padding: 6px 16px;
  font-size: 12px;
  color: var(--text-secondary);
  background: var(--bg-light);
  border: 1px solid var(--border-color);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.share-btn:hover {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.loading-spinner {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.loading-state p {
  color: var(--text-muted);
  margin-top: 16px;
}

@media (max-width: 768px) {
  .article-header h1 {
    font-size: 1.6rem;
  }

  .article-meta {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }

  .content-wrapper {
    padding: 24px;
  }

  .news-content {
    padding: 40px 0;
  }
}
</style>