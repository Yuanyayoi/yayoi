<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>全局设置</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper" v-loading="loading">
        <div class="card">
          <div class="card-header">
            <h3>网站基本信息</h3>
          </div>
          <p class="card-desc">设置网站全局信息，如网站名称、描述和Logo图片</p>
          <el-form :model="form" label-width="120px">
            <el-form-item label="网站名称">
              <el-input v-model="form.siteName" placeholder="请输入网站名称" />
            </el-form-item>
            <el-form-item label="网站描述">
              <el-input v-model="form.siteDescription" type="textarea" :rows="3" placeholder="请输入网站描述" />
            </el-form-item>
          </el-form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>Logo设置</h3>
          </div>
          <p class="card-desc">上传网站Logo图片，支持JPG、PNG、GIF格式，建议尺寸200x200像素以内</p>
          <el-form :model="form" label-width="120px">
            <el-form-item label="彩色Logo">
              <div class="logo-upload-area">
                <el-upload
                  class="logo-uploader"
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :data="{ folder: 'logo' }"
                  :show-file-list="false"
                  :on-success="(res) => handleUploadSuccess(res, 'logoUrl')"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
                  accept="image/*"
                  name="file"
                >
                  <img v-if="form.logoUrl" :src="getImageUrl(form.logoUrl)" class="logo-preview" />
                  <div v-else class="logo-uploader-icon">
                    <el-icon :size="28"><Plus /></el-icon>
                    <span>点击上传</span>
                  </div>
                </el-upload>
                <div class="logo-info">
                  <p class="logo-label">彩色Logo（浅色背景使用）</p>
                  <el-button v-if="form.logoUrl" type="danger" link size="small" @click="form.logoUrl = ''">移除图片</el-button>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="深色Logo">
              <div class="logo-upload-area">
                <el-upload
                  class="logo-uploader logo-uploader-dark"
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :data="{ folder: 'logo' }"
                  :show-file-list="false"
                  :on-success="(res) => handleUploadSuccess(res, 'logoUrlDark')"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
                  accept="image/*"
                  name="file"
                >
                  <img v-if="form.logoUrlDark" :src="getImageUrl(form.logoUrlDark)" class="logo-preview" />
                  <div v-else class="logo-uploader-icon">
                    <el-icon :size="28"><Plus /></el-icon>
                    <span>点击上传</span>
                  </div>
                </el-upload>
                <div class="logo-info">
                  <p class="logo-label">深色Logo（深色背景使用）</p>
                  <el-button v-if="form.logoUrlDark" type="danger" link size="small" @click="form.logoUrlDark = ''">移除图片</el-button>
                </div>
              </div>
            </el-form-item>
          </el-form>
        </div>

        <div class="card action-card">
          <el-button type="primary" :loading="saving" @click="save" size="large">保存全局设置</el-button>
          <el-button @click="load" :disabled="loading">重置</el-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref, computed } from 'vue'
import { siteApi } from '@/api'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const saving = ref(false)
const loading = ref(false)
const form = ref({ siteName: '', siteDescription: '', logoUrl: '', logoUrlDark: '' })
const uploadUrl = '/api/upload'
const uploadHeaders = computed(() => {
  const token = sessionStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})

const getImageUrl = (path) => {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) return path
  return path
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB')
    return false
  }
  return true
}

const handleUploadSuccess = (response, field) => {
  const url = response?.data?.url || response?.url || ''
  if (url) {
    form.value[field] = url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('上传失败，请重试')
  }
}

const handleUploadError = () => {
  ElMessage.error('图片上传失败')
}

const load = async () => {
  loading.value = true
  try {
    const res = await siteApi.getGlobal()
    const d = res.data.data || {}
    form.value = {
      siteName: d.siteName || '',
      siteDescription: d.siteDescription || '',
      logoUrl: d.logoUrl || '',
      logoUrlDark: d.logoUrlDark || ''
    }
  } catch (e) {
    console.error('加载失败', e)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const updateFavicon = (logoUrl) => {
  if (!logoUrl) return
  const head = document.getElementsByTagName('head')[0]
  const oldIcons = document.querySelectorAll("link[rel='icon'], link[rel='shortcut icon']")
  oldIcons.forEach(link => link.parentNode?.removeChild(link))
  
  const fullUrl = logoUrl.startsWith('http') ? logoUrl : (window.location.origin + logoUrl)
  const timestamp = '?t=' + Date.now()
  
  const newLink = document.createElement('link')
  newLink.rel = 'icon'
  newLink.type = 'image/png'
  newLink.href = fullUrl + timestamp
  head.appendChild(newLink)
  
  const newLink2 = document.createElement('link')
  newLink2.rel = 'shortcut icon'
  newLink2.type = 'image/png'
  newLink2.href = fullUrl + timestamp
  head.appendChild(newLink2)
}

const save = async () => {
  saving.value = true
  try {
    await siteApi.saveGlobal(form.value)
    if (form.value.siteName) {
      document.title = form.value.siteName + ' - 管理后台'
    }
    if (form.value.logoUrl) {
      updateFavicon(form.value.logoUrl)
    }
    window.dispatchEvent(new CustomEvent('globalSettingsUpdated', { detail: form.value }))
    ElMessage.success('全局设置保存成功')
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  load()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; margin: 0; font-size: 1.5rem; }
.content-wrapper { padding: 2rem; display: flex; flex-direction: column; gap: 1.5rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
.card-header h3 { color: #2c3e50; margin: 0; font-size: 1.1rem; }
.card-desc { color: #7f8c8d; font-size: 0.85rem; margin-bottom: 1.5rem; margin-top: 0; }
.action-card { display: flex; gap: 0.75rem; justify-content: center; padding: 1.5rem; }

.logo-upload-area {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo-uploader :deep(.el-upload) {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.logo-uploader-dark :deep(.el-upload) {
  background: #2c3e50;
  border-color: #4a5568;
}

.logo-uploader-dark :deep(.el-upload:hover) {
  border-color: #67c23a;
}

.logo-preview {
  width: 120px;
  height: 120px;
  object-fit: contain;
  display: block;
}

.logo-uploader-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #8c939d;
  font-size: 0.75rem;
}

.logo-uploader-dark .logo-uploader-icon {
  color: #a0aec0;
}

.logo-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.logo-label {
  margin: 0;
  font-size: 0.9rem;
  color: #606266;
}

@media (max-width: 768px) {
  .admin-page { flex-direction: column; }
  .logo-upload-area { flex-direction: column; align-items: flex-start; }
}
</style>
