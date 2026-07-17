<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-brand">
          <div class="logo">
            <img :src="globalSettings.logoUrl || '/favicon-color.png'" :alt="globalSettings.siteName || '四川国驰恒创科技有限公司'" class="logo-icon" />
            <span class="logo-text">{{ globalSettings.siteName || '四川国驰恒创科技有限公司' }}</span>
          </div>
          <p class="brand-description" v-if="footerData.brandDescription">{{ footerData.brandDescription }}</p>
        </div>

        <div class="footer-links">
          <h4>快速链接</h4>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/about">关于我们</router-link></li>
            <li><router-link to="/products">产品服务</router-link></li>
            <li><router-link to="/news">新闻动态</router-link></li>
            <li><router-link to="/contact">联系我们</router-link></li>
          </ul>
        </div>

        <div class="footer-links">
          <h4>服务项目</h4>
          <ul>
            <template v-for="i in 5" :key="i">
              <li v-if="footerData['service' + i] && footerData['service' + i].trim()">
                <a v-if="isExternalLink(footerData['service' + i + 'Url'])"
                   :href="footerData['service' + i + 'Url']"
                   target="_blank"
                   rel="noopener noreferrer">{{ footerData['service' + i] }}</a>
                <router-link v-else-if="getInternalLink(footerData['service' + i + 'Url'])"
                             :to="getInternalLink(footerData['service' + i + 'Url'])">{{ footerData['service' + i] }}</router-link>
                <span v-else>{{ footerData['service' + i] }}</span>
              </li>
            </template>
          </ul>
        </div>

        <div class="footer-contact">
          <h4>联系方式</h4>
          <ul>
            <li v-if="contactInfo.address">
              <svg class="contact-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              <span>{{ contactInfo.address }}</span>
            </li>
            <li v-if="contactInfo.phone">
              <svg class="contact-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>
              </svg>
              <span>{{ contactInfo.phone }}</span>
            </li>
            <li v-if="contactInfo.email">
              <svg class="contact-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
              <span>{{ contactInfo.email }}</span>
            </li>
            <li v-if="contactInfo.businessHours">
              <svg class="contact-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              <span>{{ contactInfo.businessHours }}</span>
            </li>
          </ul>
        </div>
      </div>

      <div class="footer-bottom">
        <p v-if="footerData.copyright">{{ footerData.copyright }}</p>
        <div class="footer-bottom-links">
          <a href="#" v-if="footerData.privacyText">{{ footerData.privacyText }}</a>
          <span class="divider" v-if="footerData.privacyText && footerData.termsText">|</span>
          <a href="#" v-if="footerData.termsText">{{ footerData.termsText }}</a>
          <span class="divider" v-if="(footerData.privacyText || footerData.termsText) && footerData.sitemapText">|</span>
          <a href="#" v-if="footerData.sitemapText">{{ footerData.sitemapText }}</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { siteCache } from '@/utils/siteCache'

const footerData = ref({})
const contactInfo = ref({})
const globalSettings = ref({})

const getInternalLink = (url) => {
  if (!url || !url.trim()) return null
  const trimmed = url.trim()
  if (trimmed.startsWith('http://') || trimmed.startsWith('https://')) return null
  if (trimmed.startsWith('/')) return trimmed
  return null
}

const isExternalLink = (url) => {
  if (!url || !url.trim()) return false
  const trimmed = url.trim()
  return trimmed.startsWith('http://') || trimmed.startsWith('https://')
}

const loadData = async () => {
  try {
    const [footer, contact, global] = await Promise.all([
      siteCache.getFooter(),
      siteCache.getContact(),
      siteCache.getGlobal()
    ])
    footerData.value = footer
    contactInfo.value = contact
    globalSettings.value = global
  } catch (e) {
    console.error('加载页脚数据失败', e)
  }
}

onMounted(loadData)
</script>

<style scoped>
.footer {
  background: linear-gradient(180deg, #1a365d 0%, #15294a 100%);
  color: white;
  padding: 4rem 0 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

.footer-content {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1.5fr;
  gap: 3rem;
  padding-bottom: 3rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-brand {
  color: rgba(255, 255, 255, 0.9);
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.logo-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: contain;
}

.logo-text {
  font-size: 1.1rem;
  font-weight: 700;
  color: white;
}

.brand-description {
  font-size: 0.9rem;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.7);
}

.footer-links h4,
.footer-contact h4 {
  color: white;
  font-size: 1.05rem;
  margin-bottom: 1.25rem;
  position: relative;
  padding-bottom: 0.75rem;
}

.footer-links h4::after,
.footer-contact h4::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 30px;
  height: 2px;
  background: #00a859;
  border-radius: 1px;
}

.footer-links ul,
.footer-contact ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 0.75rem;
}

.footer-links a,
.footer-links span {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #33d17a;
}

.footer-links span {
  cursor: default;
}

.footer-contact li {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 0.85rem;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  align-items: flex-start;
}

.contact-icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  margin-top: 2px;
  color: #33d17a;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 0;
  flex-wrap: wrap;
  gap: 1rem;
}

.footer-bottom p {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.85rem;
  margin: 0;
}

.footer-bottom-links {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.footer-bottom-links a {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.85rem;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-bottom-links a:hover {
  color: #33d17a;
}

.divider {
  color: rgba(255, 255, 255, 0.3);
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr 1fr;
    gap: 2rem;
  }

  .footer-brand {
    grid-column: 1 / -1;
  }

  .footer-bottom {
    flex-direction: column;
    text-align: center;
    gap: 0.75rem;
  }

  .footer-bottom-links {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .footer {
    padding: 3rem 0 0;
  }
}
</style>
