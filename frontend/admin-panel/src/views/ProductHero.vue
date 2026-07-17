<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>产品页面文案</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>Hero 区域文案</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">主标题</span>
              <span class="text-value">{{ formData.heroTitle }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">副标题</span>
              <span class="text-value">{{ formData.heroSubtitle }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">背景图片</span>
              <div class="text-value image-preview">
                <img v-if="formData.heroBgImage" :src="formData.heroBgImage" alt="背景图片" class="preview-img" />
                <span v-else class="no-image">未设置背景图片</span>
              </div>
            </div>
            <div class="text-item">
              <span class="text-label">显示位置</span>
              <span class="text-value">{{ positionLabels[formData.bgPosition] || '居中' }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="主标题">
              <el-input v-model="formData.heroTitle" placeholder="请输入主标题，如：产品服务" />
            </el-form-item>
            <el-form-item label="副标题">
              <el-input v-model="formData.heroSubtitle" type="textarea" :rows="3" placeholder="请输入副标题描述文字" />
            </el-form-item>
            <el-form-item label="背景图片">
              <div class="upload-wrapper">
                <el-upload
                  class="image-uploader"
                  :show-file-list="false"
                  :http-request="handleUpload"
                  :before-upload="beforeUpload"
                  accept="image/*"
                >
                  <img v-if="formData.heroBgImage" :src="formData.heroBgImage" class="uploaded-img" />
                  <div v-else class="upload-placeholder">
                    <el-icon class="uploader-icon"><Plus /></el-icon>
                    <span class="upload-text">点击上传</span>
                  </div>
                </el-upload>
                <div class="upload-actions">
                  <el-button v-if="formData.heroBgImage" type="danger" size="small" @click="removeImage">移除图片</el-button>
                </div>
              </div>
              <div class="form-hint">支持 jpg、png、gif 格式，建议尺寸 1920x600（16:5 比例），大小不超过 5MB</div>
            </el-form-item>
            <el-form-item label="显示位置">
              <el-radio-group v-model="formData.bgPosition">
                <el-radio value="top">顶部</el-radio>
                <el-radio value="center">居中</el-radio>
                <el-radio value="bottom">底部</el-radio>
              </el-radio-group>
            </el-form-item>
            <div class="form-actions">
              <el-button @click="toggleEdit">取消</el-button>
              <el-button type="primary" :loading="saving" @click="saveFormData">保存</el-button>
            </div>
          </el-form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>预览效果</h3>
          </div>
          <div class="preview-wrapper">
            <div
              class="preview-hero"
              :style="previewStyle"
            >
              <div class="hero-overlay"></div>
              <div class="hero-content">
                <h1>{{ formData.heroTitle || '产品服务' }}</h1>
                <p>{{ formData.heroSubtitle || '专业的充电设备与能源解决方案提供商' }}</p>
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
import { onMounted, ref, computed } from 'vue'
import api from '@/api'
import { siteApi } from '@/api'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'


const saving = ref(false)
const uploading = ref(false)
const isEditing = ref(false)
const formData = ref({ heroTitle: '', heroSubtitle: '', heroBgImage: '', bgPosition: 'center' })

const positionLabels = {
  top: '顶部',
  center: '居中',
  bottom: '底部'
}

const previewStyle = computed(() => {
  const style = {}
  if (formData.value.heroBgImage) {
    style.backgroundImage = `url(${formData.value.heroBgImage})`
  }
  const pos = formData.value.bgPosition || 'center'
  style.backgroundPosition = `center ${pos}`
  return style
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('请上传图片文件！')
    return false
  }
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB！')
    return false
  }
  return true
}

const handleUpload = (options) => {
  uploading.value = true
  const formDataUpload = new FormData()
  formDataUpload.append('file', options.file)
  formDataUpload.append('folder', 'common')
  api.post('/api/upload', formDataUpload).then(res => {
    if (res.data.code === 200) {
      formData.value.heroBgImage = res.data.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  }).catch((err) => {
    console.error('上传失败:', err)
    ElMessage.error('上传失败：' + (err.response?.data?.message || err.message || '网络错误'))
  }).finally(() => {
    uploading.value = false
  })
}

const removeImage = async () => {
  if (!formData.value.heroBgImage) return
  try {
    await api.delete('/api/upload/delete', { params: { fileUrl: formData.value.heroBgImage } })
  } catch (e) {
  }
  formData.value.heroBgImage = ''
  ElMessage.success('图片已移除')
}

const load = async () => {
  try {
    const res = await siteApi.getProductHero()
    const d = res.data.data || {}
    formData.value = {
      heroTitle: d.heroTitle || '产品服务',
      heroSubtitle: d.heroSubtitle || '专业的充电设备与能源解决方案提供商',
      heroBgImage: d.heroBgImage || '',
      bgPosition: d.bgPosition || 'center'
    }
  } catch (e) { console.error('加载失败', e) }
}

const saveFormData = async () => {
  saving.value = true
  try {
    await siteApi.saveProductHero(formData.value)
    ElMessage.success('保存成功')
    isEditing.value = false
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
.main-content { flex: 1; display: flex; flex-direction: column; background: #f0f2f5; }
.top-bar {
  background: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.top-bar h1 { color: #1a1a2e; font-size: 1.25rem; font-weight: 600; }
.content-wrapper { padding: 1.5rem; }

.card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.06);
  margin-bottom: 1.5rem;
}
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem; }
.card-header h3 { color: #1a1a2e; margin: 0; font-size: 1.05rem; font-weight: 600; }

.text-display { padding: 0.5rem 0; }
.text-item { display: flex; align-items: center; padding: 0.85rem 0; border-bottom: 1px solid #f0f0f0; }
.text-item:last-child { border-bottom: none; }
.text-label { width: 100px; color: #8c8c8c; font-size: 0.875rem; flex-shrink: 0; }
.text-value { color: #262626; font-size: 0.9rem; word-break: break-all; }
.image-preview { display: flex; align-items: center; }
.preview-img { max-width: 320px; max-height: 100px; border-radius: 6px; object-fit: cover; border: 1px solid #f0f0f0; }
.no-image { color: #bfbfbf; font-style: italic; }

.upload-wrapper { display: flex; flex-direction: column; gap: 0.5rem; }
.upload-actions { display: flex; }
.image-uploader { width: 100%; }
.image-uploader :deep(.el-upload) {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
  width: 320px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}
.image-uploader :deep(.el-upload:hover) { border-color: #00a859; background: #f6ffed; }
.uploader-icon { font-size: 28px; color: #bfbfbf; }
.upload-text { font-size: 0.8rem; color: #bfbfbf; margin-top: 4px; }
.upload-placeholder { display: flex; flex-direction: column; align-items: center; justify-content: center; }
.uploaded-img { width: 320px; height: 100px; object-fit: cover; display: block; }
.form-hint { font-size: 0.75rem; color: #bfbfbf; }

.form-actions { display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.25rem; }

.preview-wrapper {
  width: 100%;
  border: 1px solid #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
}
.preview-hero {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 5;
  background-image: linear-gradient(135deg, #0a1929 0%, #132f4c 50%, #0a1929 100%);
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
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
  max-width: 60%;
  color: white;
  padding: 2rem;
}
.preview-hero h1 {
  font-size: clamp(1.5rem, 3.5vw, 3rem);
  font-weight: 800;
  margin: 0 0 1rem;
  line-height: 1.2;
}
.preview-hero p {
  font-size: clamp(0.85rem, 1.5vw, 1.05rem);
  color: rgba(255,255,255,0.75);
  margin: 0;
}

@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
