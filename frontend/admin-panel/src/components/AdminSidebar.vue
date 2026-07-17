<template>
  <aside class="sidebar">
    <div class="logo">
      <div class="brand-logo" :style="logoStyle"></div>
      <h2>管理后台</h2>
    </div>

    <nav class="nav">
      <ul>
        <li :class="{ active: isPathActive('/') }">
          <router-link to="/">
            <el-icon><DataBoard /></el-icon>
            <span>仪表盘</span>
          </router-link>
        </li>

        <li class="nav-section" :class="{ open: homeOpen }">
          <button type="button" class="nav-section-trigger" @click="homeOpen = !homeOpen">
            <span class="nav-label">
              <el-icon><HomeFilled /></el-icon>
              <span>首页管理</span>
            </span>
            <el-icon class="nav-chevron" :class="{ rotated: homeOpen }"><ArrowDown /></el-icon>
          </button>
          <ul v-show="homeOpen" class="nav-submenu">
            <li :class="{ active: isPathActive('/home/carousel') }">
              <router-link to="/home/carousel">轮播图管理</router-link>
            </li>
            <li :class="{ active: isPathActive('/home/features') }">
              <router-link to="/home/features">核心优势管理</router-link>
            </li>
            <li :class="{ active: isPathActive('/home/about-intro') }">
              <router-link to="/home/about-intro">公司简介模块</router-link>
            </li>
            <li :class="{ active: isPathActive('/home/spotlight') }">
              <router-link to="/home/spotlight">XRE亮点模块</router-link>
            </li>
            <li :class="{ active: isPathActive('/home/news-section') }">
              <router-link to="/home/news-section">最新动态模块</router-link>
            </li>
            <li :class="{ active: isPathActive('/home/products-section') }">
              <router-link to="/home/products-section">热门产品模块</router-link>
            </li>
            <li :class="{ active: isPathActive('/home/cta-section') }">
              <router-link to="/home/cta-section">立即行动模块</router-link>
            </li>
          </ul>
        </li>

        <li class="nav-section" :class="{ open: aboutOpen }">
          <button type="button" class="nav-section-trigger" @click="aboutOpen = !aboutOpen">
            <span class="nav-label">
              <el-icon><User /></el-icon>
              <span>关于我们</span>
            </span>
            <el-icon class="nav-chevron" :class="{ rotated: aboutOpen }"><ArrowDown /></el-icon>
          </button>
          <ul v-show="aboutOpen" class="nav-submenu">
            <li :class="{ active: isPathActive('/about/hero') }">
              <router-link to="/about/hero">页面文案</router-link>
            </li>
            <li :class="{ active: isPathActive('/about/detail') }">
              <router-link to="/about/detail">公司简介</router-link>
            </li>
            <li :class="{ active: isPathActive('/about/values') }">
              <router-link to="/about/values">核心价值观</router-link>
            </li>
            <li :class="{ active: isPathActive('/about/team') }">
              <router-link to="/about/team">核心团队</router-link>
            </li>
            <li :class="{ active: isPathActive('/about/milestones') }">
              <router-link to="/about/milestones">发展历程</router-link>
            </li>
            <li :class="{ active: isPathActive('/about/awards') }">
              <router-link to="/about/awards">所获荣誉</router-link>
            </li>
          </ul>
        </li>

        <li class="nav-section" :class="{ open: productsOpen }">
          <button type="button" class="nav-section-trigger" @click="productsOpen = !productsOpen">
            <span class="nav-label">
              <el-icon><Goods /></el-icon>
              <span>产品服务</span>
            </span>
            <el-icon class="nav-chevron" :class="{ rotated: productsOpen }"><ArrowDown /></el-icon>
          </button>
          <ul v-show="productsOpen" class="nav-submenu">
            <li :class="{ active: isPathActive('/product-hero') }">
              <router-link to="/product-hero">页面文案</router-link>
            </li>
            <li :class="{ active: isPathActive('/categories') }">
              <router-link to="/categories">分类管理</router-link>
            </li>
            <li :class="{ active: isPathActive('/products') }">
              <router-link to="/products">产品列表</router-link>
            </li>
          </ul>
        </li>

        <li class="nav-section" :class="{ open: newsOpen }">
          <button type="button" class="nav-section-trigger" @click="newsOpen = !newsOpen">
            <span class="nav-label">
              <el-icon><Document /></el-icon>
              <span>新闻动态</span>
            </span>
            <el-icon class="nav-chevron" :class="{ rotated: newsOpen }"><ArrowDown /></el-icon>
          </button>
          <ul v-show="newsOpen" class="nav-submenu">
            <li :class="{ active: isPathActive('/news/hero') }">
              <router-link to="/news/hero">页面文案</router-link>
            </li>
            <li :class="{ active: isPathActive('/news/categories') }">
              <router-link to="/news/categories">新闻分类</router-link>
            </li>
            <li :class="{ active: isPathActive('/news/list') }">
              <router-link to="/news/list">新闻列表</router-link>
            </li>
          </ul>
        </li>

        <li class="nav-section" :class="{ open: contactOpen }">
          <button type="button" class="nav-section-trigger" @click="contactOpen = !contactOpen">
            <span class="nav-label">
              <el-icon><Phone /></el-icon>
              <span>联系我们</span>
            </span>
            <el-icon class="nav-chevron" :class="{ rotated: contactOpen }"><ArrowDown /></el-icon>
          </button>
          <ul v-show="contactOpen" class="nav-submenu">
            <li :class="{ active: isPathActive('/contact/hero') }">
              <router-link to="/contact/hero">页面文案</router-link>
            </li>
            <li :class="{ active: isPathActive('/contact/company-info') }">
              <router-link to="/contact/company-info">公司信息</router-link>
            </li>
            <li :class="{ active: isPathActive('/contact/online') }">
              <router-link to="/contact/online">在线咨询</router-link>
            </li>
            <li :class="{ active: isPathActive('/contact/form') }">
              <router-link to="/contact/form">提交留言</router-link>
            </li>
          </ul>
        </li>

        <li :class="{ active: isPathActive('/messages') }">
          <router-link to="/messages">
            <el-icon><ChatDotRound /></el-icon>
            <span>留言管理</span>
          </router-link>
        </li>

        <li class="nav-section" :class="{ open: settingsOpen }">
          <button type="button" class="nav-section-trigger" @click="settingsOpen = !settingsOpen">
            <span class="nav-label">
              <el-icon><Setting /></el-icon>
              <span>网站设置</span>
            </span>
            <el-icon class="nav-chevron" :class="{ rotated: settingsOpen }"><ArrowDown /></el-icon>
          </button>
          <ul v-show="settingsOpen" class="nav-submenu">
            <li :class="{ active: isPathActive('/settings/footer') }">
              <router-link to="/settings/footer">页脚信息</router-link>
            </li>
            <li :class="{ active: isPathActive('/settings/global') }">
              <router-link to="/settings/global">全局设置</router-link>
            </li>
          </ul>
        </li>

        <li v-if="isSuperAdmin" :class="{ active: isPathActive('/users') }">
          <router-link to="/users">
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </router-link>
        </li>
      </ul>
    </nav>

    <div class="sidebar-footer">
      <div class="user-info" @click="openProfile">
        <el-icon><EditPen /></el-icon>
        <span>个人设置</span>
      </div>
      <div class="logout">
        <button type="button" @click="$emit('logout')">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </button>
      </div>
    </div>
  </aside>

  <el-dialog v-model="profileVisible" title="个人设置" width="480px" :close-on-click-modal="false">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基本信息" name="info">
        <el-form :model="profileForm" label-width="80px" style="margin-top: 10px">
          <el-form-item label="头像">
            <div class="avatar-upload-wrapper">
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :http-request="handleAvatarUpload"
                accept="image/*"
              >
                <div v-if="profileForm.avatar" class="avatar-preview">
                  <img :src="getAvatarUrl(profileForm.avatar)" class="avatar-image" />
                  <div class="avatar-overlay">
                    <el-icon><Camera /></el-icon>
                    <span>更换头像</span>
                  </div>
                </div>
                <div v-else class="avatar-placeholder">
                  <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                  <span class="upload-text">上传头像</span>
                </div>
              </el-upload>
              <div class="avatar-tips">支持 JPG、PNG 格式，建议尺寸 200x200</div>
            </div>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="profileForm.username" disabled />
          </el-form-item>
          <el-form-item label="角色">
            <el-tag :type="profileForm.role === 'SUPER_ADMIN' ? 'danger' : 'primary'" size="small">
              {{ profileForm.role === 'SUPER_ADMIN' ? '超级管理员' : '管理员' }}
            </el-tag>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="profileForm.phone" placeholder="请输入电话" />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="修改密码" name="password">
        <el-form :model="passwordForm" label-width="80px" style="margin-top: 10px">
          <el-form-item label="旧密码">
            <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码（至少6位）" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="profileVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="saveProfile">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, onMounted, onUnmounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import api, { authApi, uploadApi } from '@/api'
import { ElMessage } from 'element-plus'
import {
  DataBoard, HomeFilled, User, Goods, Document, Phone,
  ChatDotRound, Setting, UserFilled, SwitchButton, ArrowDown, EditPen,
  Camera, Plus
} from '@element-plus/icons-vue'

const emit = defineEmits(['logout', 'user-updated'])

const route = useRoute()

const currentUser = computed(() => {
  try {
    const u = sessionStorage.getItem('user')
    return u ? JSON.parse(u) : null
  } catch { return null }
})

const isSuperAdmin = computed(() => currentUser.value?.role === 'SUPER_ADMIN')

let cachedGlobalSettings = null
let cachedLogoUrl = '/favicon-color.png'

const globalSettings = ref(cachedGlobalSettings || {})
const logoUrl = ref(cachedLogoUrl)

const logoStyle = computed(() => ({
  backgroundImage: `url(${logoUrl.value})`
}))

const computeLogoUrl = (settings) => {
  const raw = settings?.logoUrl
  if (!raw) return '/favicon-color.png'
  if (raw.startsWith('http://') || raw.startsWith('https://')) return raw
  return raw
}

const loadGlobalSettings = async () => {
  try {
    const res = await api.get('/site/global')
    const data = res.data?.data || {}
    cachedGlobalSettings = data
    globalSettings.value = data
    cachedLogoUrl = computeLogoUrl(data)
    logoUrl.value = cachedLogoUrl
  } catch (e) {
    console.error('加载全局设置失败', e)
  }
}

const homeOpen = ref(route.path.startsWith('/home'))
const aboutOpen = ref(route.path.startsWith('/about'))
const productsOpen = ref(route.path.startsWith('/products') || route.path.startsWith('/categories') || route.path.startsWith('/product-hero'))
const newsOpen = ref(route.path.startsWith('/news'))
const contactOpen = ref(route.path.startsWith('/contact'))
const settingsOpen = ref(route.path.startsWith('/settings'))

const handleSettingsUpdated = (event) => {
  if (event.detail) {
    cachedGlobalSettings = event.detail
    globalSettings.value = event.detail
    cachedLogoUrl = computeLogoUrl(event.detail)
    logoUrl.value = cachedLogoUrl
  }
}

onMounted(() => {
  if (cachedGlobalSettings) {
    globalSettings.value = cachedGlobalSettings
    logoUrl.value = cachedLogoUrl
  }
  loadGlobalSettings()
  window.addEventListener('globalSettingsUpdated', handleSettingsUpdated)
})

onUnmounted(() => {
  window.removeEventListener('globalSettingsUpdated', handleSettingsUpdated)
})

watch(() => route.path, () => {
  homeOpen.value = route.path.startsWith('/home')
  aboutOpen.value = route.path.startsWith('/about')
  productsOpen.value = route.path.startsWith('/products') || route.path.startsWith('/categories') || route.path.startsWith('/product-hero')
  newsOpen.value = route.path.startsWith('/news')
  contactOpen.value = route.path.startsWith('/contact')
  settingsOpen.value = route.path.startsWith('/settings')
})

const isPathActive = (path) => route.path === path

const profileVisible = ref(false)
const activeTab = ref('info')
const saving = ref(false)

const profileForm = reactive({
  username: '',
  role: '',
  email: '',
  phone: '',
  avatar: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const openProfile = async () => {
  activeTab.value = 'info'
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  profileVisible.value = true
  try {
    const res = await authApi.getProfile()
    const data = res.data?.data
    if (data) {
      profileForm.username = data.username || ''
      profileForm.role = data.role || ''
      profileForm.email = data.email || ''
      profileForm.phone = data.phone || ''
      profileForm.avatar = data.avatar || ''
    }
  } catch (e) {
    const stored = currentUser.value
    if (stored) {
      profileForm.username = stored.username || ''
      profileForm.role = stored.role || ''
      profileForm.email = stored.email || ''
      profileForm.phone = stored.phone || ''
      profileForm.avatar = stored.avatar || ''
    }
  }
}

const getAvatarUrl = (avatar) => {
  if (!avatar) return ''
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) return avatar
  return window.location.origin + avatar
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('头像只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB')
    return false
  }
  return true
}

const uploadingAvatar = ref(false)

const handleAvatarUpload = async (options) => {
  uploadingAvatar.value = true
  try {
    const res = await uploadApi.uploadImage(options.file, 'avatar')
    if (res.data?.code === 200 && res.data.data?.url) {
      profileForm.avatar = res.data.data.url
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(res.data?.message || '头像上传失败')
    }
  } catch (e) {
    console.error('头像上传错误:', e)
    ElMessage.error(e.response?.data?.message || '头像上传失败')
  } finally {
    uploadingAvatar.value = false
  }
}

const saveProfile = async () => {
  saving.value = true
  try {
    if (activeTab.value === 'info') {
      const res = await authApi.updateProfile({
        email: profileForm.email,
        phone: profileForm.phone,
        avatar: profileForm.avatar
      })
      if (res.data?.data) {
        const updated = res.data.data
        const stored = currentUser.value
        const newUser = { 
          ...(stored || {}), 
          email: updated.email, 
          phone: updated.phone,
          avatar: updated.avatar
        }
        sessionStorage.setItem('user', JSON.stringify(newUser))
        window.dispatchEvent(new CustomEvent('userUpdated', { detail: newUser }))
        emit('user-updated', newUser)
      }
      ElMessage.success('个人信息更新成功')
      profileVisible.value = false
    } else {
      if (!passwordForm.oldPassword) {
        ElMessage.warning('请输入旧密码')
        return
      }
      if (!passwordForm.newPassword || passwordForm.newPassword.length < 6) {
        ElMessage.warning('新密码长度不能少于6位')
        return
      }
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        ElMessage.warning('两次输入的新密码不一致')
        return
      }
      await authApi.changePassword({
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword
      })
      ElMessage.success('密码修改成功，请重新登录')
      profileVisible.value = false
      setTimeout(() => {
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('user')
        window.location.href = '/admin/login'
      }, 800)
    }
  } catch (e) {
    const msg = e.response?.data?.message || '保存失败'
    ElMessage.error(msg)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.sidebar {
  display: flex !important;
  flex-direction: column !important;
  padding: 0 !important;
  overflow: hidden !important;
}

.brand-logo {
  width: 36px;
  height: 36px;
  border-radius: 6px;
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  background-color: transparent;
  flex-shrink: 0;
}

.logo {
  padding: 18px 18px 16px !important;
  margin: 0 !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08) !important;
  display: flex !important;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.logo h2 {
  font-size: 15px !important;
  font-weight: 800 !important;
  color: #ffffff !important;
  margin: 0 !important;
  letter-spacing: 0 !important;
  line-height: 1.3;
}

.nav {
  flex: 1 1 auto !important;
  padding: 12px 12px 8px !important;
  overflow-y: auto !important;
  overflow-x: hidden !important;
  min-height: 0;
}

.nav > ul {
  display: grid !important;
  gap: 4px !important;
  list-style: none !important;
  padding: 0 !important;
  margin: 0 !important;
}

.nav > ul > li {
  margin: 0 !important;
}

.nav > ul > li > a {
  justify-content: flex-start !important;
  gap: 10px !important;
  min-height: 40px !important;
  display: flex !important;
  align-items: center;
}

.nav > ul > li > a .el-icon {
  font-size: 17px;
  width: 20px;
  text-align: center;
  flex-shrink: 0;
}

.nav-section-trigger {
  display: flex !important;
  align-items: center;
  justify-content: space-between !important;
  min-height: 40px !important;
  width: 100%;
  padding: 0 14px !important;
}

.nav-section-trigger .nav-label {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-section-trigger .nav-label .el-icon {
  font-size: 17px;
  width: 20px;
  text-align: center;
  flex-shrink: 0;
}

.nav-chevron {
  font-size: 13px;
  opacity: 0.8;
  transition: transform 0.2s ease;
}

.nav-chevron.rotated {
  transform: rotate(180deg);
}

.nav-submenu li {
  margin-bottom: 4px;
}

.nav-submenu li:last-child {
  margin-bottom: 0;
}

.nav-submenu a {
  min-height: 34px !important;
  padding: 0 12px 0 44px !important;
  font-size: 13px !important;
}

.sidebar-footer {
  flex-shrink: 0;
  padding: 8px 12px 14px !important;
  border-top: 1px solid rgba(255, 255, 255, 0.08) !important;
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-height: 38px;
  border-radius: 8px;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  margin-bottom: 8px;
  transition: background-color 0.2s, color 0.2s;
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.08);
  color: #ffffff;
}

.user-info .el-icon {
  font-size: 16px;
}

.logout button {
  width: 100%;
  min-height: 40px !important;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.logout button .el-icon {
  margin-right: 0 !important;
  font-size: 16px;
}

.avatar-upload-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
}

.avatar-uploader :deep(.el-upload) {
  border: none;
  border-radius: 0;
  cursor: pointer;
  overflow: hidden;
}

.avatar-preview {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: white;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

.avatar-overlay .el-icon {
  font-size: 20px;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #f5f7fa;
  border: 2px dashed #dcdfe6;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  color: #8c939d;
  transition: border-color 0.2s, color 0.2s;
}

.avatar-uploader :deep(.el-upload:hover) .avatar-placeholder {
  border-color: #409eff;
  color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
}

.upload-text {
  font-size: 12px;
}

.avatar-tips {
  font-size: 12px;
  color: #909399;
}

@media (prefers-reduced-motion: reduce) {
  .nav-chevron,
  .avatar-overlay,
  .avatar-placeholder {
    transition: none;
  }
}
</style>
