<template>
  <div class="contact-page">
    <!-- Hero -->
    <section class="hero" :style="heroStyle">
      <div class="hero-overlay"></div>
      <div class="container">
        <div class="hero-content">
          <h1>{{ heroTitle || '联系我们' }}</h1>
          <p>{{ heroSubtitle || '有任何问题，欢迎随时与我们联系。' }}</p>
        </div>
      </div>
    </section>

    <!-- Contact Info Cards -->
    <section class="contact-info-section">
      <div class="container">
        <div class="info-grid">
          <div class="info-card" v-if="contactInfo.address">
            <div class="info-icon-wrapper">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
            </div>
            <h3>公司地址</h3>
            <p>{{ contactInfo.address }}</p>
          </div>
          <div class="info-card" v-if="contactInfo.phone">
            <div class="info-icon-wrapper">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
              </svg>
            </div>
            <h3>联系电话</h3>
            <p>{{ contactInfo.phone }}</p>
          </div>
          <div class="info-card" v-if="contactInfo.email">
            <div class="info-icon-wrapper">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
            </div>
            <h3>邮箱地址</h3>
            <p>{{ contactInfo.email }}</p>
          </div>
          <div class="info-card" v-if="contactInfo.businessHours">
            <div class="info-icon-wrapper">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="24" height="24">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
            </div>
            <h3>工作时间</h3>
            <p>{{ contactInfo.businessHours }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Form & Map -->
    <section class="contact-form-section">
      <div class="container">
        <div class="form-grid">
          <div class="form-info">
            <span class="section-subtitle">{{ onlineData.title || '在线咨询' }}</span>
            <h2>{{ onlineData.subtitle || '发送消息给我们' }}</h2>
            <p>{{ onlineData.description || '如有任何问题或合作意向，请填写以下表单，我们的专业团队将在24小时内与您联系。' }}</p>
            <div class="form-benefits">
              <div class="benefit-item" v-if="onlineData.feature1Title">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="18" height="18"><polyline points="20 6 9 17 4 12"/></svg>
                <span>{{ onlineData.feature1Title }}</span>
              </div>
              <div class="benefit-item" v-if="onlineData.feature2Title">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="18" height="18"><polyline points="20 6 9 17 4 12"/></svg>
                <span>{{ onlineData.feature2Title }}</span>
              </div>
              <div class="benefit-item" v-if="onlineData.feature3Title">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="18" height="18"><polyline points="20 6 9 17 4 12"/></svg>
                <span>{{ onlineData.feature3Title }}</span>
              </div>
            </div>
          </div>
          <form @submit.prevent="handleSubmit" class="contact-form" novalidate>
            <div class="form-row">
              <div class="form-group">
                <label>{{ formData.nameLabel || '姓名' }} <span class="required">*</span></label>
                <input type="text" v-model="form.name" :placeholder="formData.namePlaceholder || '请输入您的姓名'" required />
              </div>
              <div class="form-group">
                <label>{{ formData.phoneLabel || '电话' }} <span class="required">*</span></label>
                <input type="tel" v-model="form.phone" :placeholder="formData.phonePlaceholder || '请输入您的联系电话'" required />
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>{{ formData.emailLabel || '邮箱' }}</label>
                <input type="text" v-model="form.email" :placeholder="formData.emailPlaceholder || '请输入您的邮箱地址'" />
              </div>
              <div class="form-group">
                <label>{{ formData.companyLabel || '公司' }}</label>
                <input type="text" v-model="form.company" :placeholder="formData.companyPlaceholder || '请输入您的公司名称'" />
              </div>
            </div>
            <div class="form-group">
              <label>{{ formData.messageLabel || '留言内容' }} <span class="required">*</span></label>
              <textarea v-model="form.message" rows="5" :placeholder="formData.messagePlaceholder || '请详细描述您的需求或问题，以便我们为您提供更精准的服务...'" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary w-full" :disabled="isSubmitting">
              <svg v-if="!isSubmitting" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
              <span v-if="!isSubmitting">{{ formData.submitButtonText || '提交留言' }}</span>
              <span v-else>提交中...</span>
            </button>
          </form>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { messageApi } from '@/api'
import { siteCache } from '@/utils/siteCache'

const contactInfo = ref({})
const heroTitle = ref('')
const heroSubtitle = ref('')
const heroBgImage = ref('')
const bgPosition = ref('center')
const onlineData = ref({})
const formData = ref({})
const isSubmitting = ref(false)
const form = ref({ name: '', phone: '', email: '', company: '', message: '' })

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

const loadData = async () => {
  try {
    const [contact, hero, online, formCfg] = await Promise.all([
      siteCache.getContact(),
      siteCache.getContactHero(),
      siteCache.getContactOnline(),
      siteCache.getContactForm()
    ])
    contactInfo.value = contact
    if (hero.heroTitle) heroTitle.value = hero.heroTitle
    if (hero.heroSubtitle) heroSubtitle.value = hero.heroSubtitle
    if (hero.heroBgImage) heroBgImage.value = hero.heroBgImage
    if (hero.bgPosition) bgPosition.value = hero.bgPosition
    onlineData.value = online
    formData.value = formCfg
  } catch (e) {
    console.error('加载联系页面数据失败', e)
  }
}

const validatePhone = (phone) => {
  if (!phone || !phone.trim()) return false
  const cleaned = phone.trim()
  const digitCount = (cleaned.match(/\d/g) || []).length
  if (digitCount < 4 || digitCount > 15) return false
  return /^[+]?[\d\s\-.()]{4,25}(?:\s*(?:ext\.?|x|分机|转)\s*\d{1,6})?$/.test(cleaned)
}

const validateEmail = (email) => {
  if (!email || !email.trim()) return true
  return /^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$/.test(email.trim())
}

const handleSubmit = async () => {
  if (!form.value.name || !form.value.name.trim()) {
    alert('请填写姓名')
    return
  }
  if (!form.value.phone || !form.value.phone.trim()) {
    alert('请填写联系电话')
    return
  }
  if (!validatePhone(form.value.phone)) {
    alert('请输入有效的电话号码（支持手机、座机、国际号码，如：13800138000、028-12345678、+86 28 1234 5678）')
    return
  }
  if (form.value.email && form.value.email.trim() && !validateEmail(form.value.email)) {
    alert('请输入有效的邮箱地址')
    return
  }
  if (!form.value.message || !form.value.message.trim()) {
    alert('请填写留言内容')
    return
  }
  isSubmitting.value = true
  try {
    const payload = {
      name: form.value.name.trim(),
      phone: form.value.phone.trim(),
      email: form.value.email ? form.value.email.trim() : '',
      company: form.value.company ? form.value.company.trim() : '',
      message: form.value.message.trim()
    }
    await messageApi.createMessage(payload)
    alert('留言提交成功！我们将尽快回复您。')
    form.value = { name: '', phone: '', email: '', company: '', message: '' }
  } catch (e) {
    alert('留言提交失败，请稍后重试')
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  window.scrollTo(0, 0)
  loadData()
})
</script>

<style scoped>
.contact-page {
  padding-top: 0;
}

/* Hero */
.hero {
  position: relative;
  min-height: 600px;
  padding-top: 120px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background-image: linear-gradient(135deg, #0a1929 0%, #132f4c 50%, #0a1929 100%);
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
  margin-bottom: 1rem;
  line-height: 1.2;
}

.hero-content p {
  color: rgba(255, 255, 255, 0.75);
  font-size: 1.05rem;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

/* Contact Info Cards */
.contact-info-section {
  padding: 0;
  margin-top: -50px;
  position: relative;
  z-index: 2;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.12);
  overflow: hidden;
}

.info-card {
  text-align: center;
  padding: 2.25rem 1.5rem;
  transition: all 0.35s ease;
  position: relative;
}

.info-card:not(:last-child)::after {
  content: '';
  position: absolute;
  right: 0;
  top: 25%;
  height: 50%;
  width: 1px;
  background: #e2e8f0;
}

.info-card:hover {
  background: #f0fdf4;
}

.info-icon-wrapper {
  width: 52px;
  height: 52px;
  margin: 0 auto 1rem;
  background: rgba(0, 168, 89, 0.1);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #00a859;
  transition: all 0.35s ease;
}

.info-card:hover .info-icon-wrapper {
  background: linear-gradient(135deg, #00a859, #008f4d);
  color: white;
  transform: scale(1.1);
}

.info-card h3 {
  color: #1a365d;
  margin-bottom: 0.5rem;
  font-size: 1rem;
  font-weight: 700;
}

.info-card p {
  color: #718096;
  font-size: 0.88rem;
  line-height: 1.5;
}

/* Contact Form */
.contact-form-section {
  padding: 5rem 0;
  background: #f7fafc;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 4rem;
  align-items: flex-start;
}

.form-info {
  color: #1a202c;
}

.section-subtitle {
  display: inline-block;
  color: #00a859;
  font-weight: 700;
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-bottom: 0.75rem;
  background: rgba(0, 168, 89, 0.1);
  padding: 0.3rem 1rem;
  border-radius: 20px;
}

.form-info h2 {
  font-size: 2.2rem;
  color: #1a365d;
  margin-bottom: 1rem;
  font-weight: 700;
}

.form-info p {
  color: #4a5568;
  line-height: 1.8;
  margin-bottom: 2rem;
  font-size: 0.95rem;
}

.form-benefits {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.benefit-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.85rem 1rem;
  background: white;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
  color: #1a365d;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.benefit-item:hover {
  border-color: rgba(0, 168, 89, 0.3);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}

.benefit-item svg {
  color: #00a859;
  flex-shrink: 0;
}

.contact-form {
  background: white;
  padding: 2.5rem;
  border-radius: 20px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  border: 1px solid #e2e8f0;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
  margin-bottom: 0.5rem;
}

.form-group {
  margin-bottom: 1.25rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #1a365d;
  font-weight: 600;
  font-size: 0.88rem;
}

.required {
  color: #e53e3e;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 1.5px solid #e2e8f0;
  border-radius: 10px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  outline: none;
  font-family: inherit;
  background: #f8fafc;
  color: #1a365d;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: #a0aec0;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #00a859;
  box-shadow: 0 0 0 4px rgba(0, 168, 89, 0.08);
  background: white;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.9rem 2rem;
  font-size: 1rem;
  font-weight: 700;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #00a859 0%, #008f4d 100%);
  color: white;
  box-shadow: 0 4px 16px rgba(0, 168, 89, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(0, 168, 89, 0.4);
}

.btn-primary:disabled {
  background: #a0aec0;
  box-shadow: none;
  cursor: not-allowed;
  transform: none;
}

.w-full {
  width: 100%;
}

@media (max-width: 768px) {
  .hero {
    min-height: 400px;
  }
  .hero-content h1 { font-size: 1.8rem; }
  .hero-content p { font-size: 0.95rem; }
  .info-grid { 
    grid-template-columns: repeat(2, 1fr); 
    border-radius: 16px;
  }
  .info-card:nth-child(2)::after { display: none; }
  .info-card { padding: 1.5rem 1rem; }
  .form-grid { 
    grid-template-columns: 1fr; 
    gap: 2rem;
  }
  .form-row { grid-template-columns: 1fr; }
  .form-info h2 { font-size: 1.6rem; }
  .contact-form { padding: 1.5rem; }
  .contact-form-section { padding: 3rem 0; }
}
</style>