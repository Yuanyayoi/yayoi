<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>首页公司简介管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>公司简介模块</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">副标题</span>
              <span class="text-value">{{ formData.subtitle }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮文字</span>
              <span class="text-value">{{ formData.buttonText }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮链接</span>
              <span class="text-value">{{ getPageLabel(formData.buttonLink) }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">图片</span>
              <div class="text-value image-preview">
                <img v-if="formData.imageUrl" :src="formData.imageUrl" alt="公司简介图片" class="preview-img" />
                <span v-else class="no-image">未设置图片</span>
              </div>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="副标题">
              <el-input v-model="formData.subtitle" placeholder="如：关于国驰恒创" />
            </el-form-item>
            <el-form-item label="按钮文字">
              <el-input v-model="formData.buttonText" placeholder="如：了解更多" />
            </el-form-item>
            <el-form-item label="按钮链接">
              <el-select v-model="formData.buttonLink" placeholder="选择跳转页面" style="width: 100%">
                <el-option v-for="page in pageOptions" :key="page.value" :label="page.label" :value="page.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="图片上传">
              <div class="upload-wrapper">
                <el-upload
                  class="image-uploader"
                  :show-file-list="false"
                  :http-request="handleUpload"
                  accept="image/*"
                >
                  <img v-if="formData.imageUrl" :src="formData.imageUrl" class="uploaded-img" />
                  <el-icon v-else class="uploader-icon"><Plus /></el-icon>
                </el-upload>
                <el-button v-if="formData.imageUrl" type="danger" size="small" @click="removeImage" class="remove-btn">移除</el-button>
              </div>
              <div class="form-hint">支持 jpg、png、gif 格式，建议尺寸 600x400</div>
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
          <div class="preview-container">
            <div class="preview-grid">
              <div class="preview-image">
                <img v-if="formData.imageUrl" :src="formData.imageUrl" alt="公司简介图片" />
                <div v-else class="preview-placeholder">暂无图片</div>
              </div>
              <div class="preview-content">
                <span class="preview-subtitle">{{ formData.subtitle || '关于国驰恒创' }}</span>
                <h2 class="preview-title">四川国驰恒创科技有限公司</h2>
                <p class="preview-desc">公司简介和描述内容在"关于我们 → 公司信息"中管理</p>
                <button class="preview-btn">{{ formData.buttonText || '了解更多' }}</button>
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
import { onMounted, ref } from 'vue'
import api from '@/api'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const saving = ref(false)
const isEditing = ref(false)
const formData = ref({
  subtitle: '',
  buttonText: '',
  buttonLink: '',
  imageUrl: ''
})

const pageOptions = [
  { label: '首页', value: '/' },
  { label: '关于我们', value: '/about' },
  { label: '产品服务', value: '/products' },
  { label: '新闻动态', value: '/news' },
  { label: '联系我们', value: '/contact' }
]

const getPageLabel = (link) => {
  const page = pageOptions.find(p => p.value === link)
  return page ? page.label : link || '未设置'
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const handleUpload = async (options) => {
  const file = options.file
  const formDataUpload = new FormData()
  formDataUpload.append('file', file)
  formDataUpload.append('folder', 'common')
  try {
    const res = await api.post('/api/upload', formDataUpload, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.code === 200) {
      formData.value.imageUrl = res.data.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

const removeImage = async () => {
  if (!formData.value.imageUrl) return
  try {
    await api.delete('/api/upload/delete', { params: { fileUrl: formData.value.imageUrl } })
  } catch (e) {
    // 忽略删除文件失败
  }
  formData.value.imageUrl = ''
  ElMessage.success('图片已移除')
}

const loadData = async () => {
  try {
    const res = await api.get('/api/site/home-about-intro')
    if (res.data.code === 200) {
      formData.value = res.data.data
    }
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    const res = await api.put('/api/site/home-about-intro', formData.value)
    if (res.data.code === 200) {
      formData.value = res.data.data
      ElMessage.success('保存成功')
      isEditing.value = false
    } else {
      ElMessage.error(res.data.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; }
.content-wrapper { padding: 2rem; display: grid; gap: 1.5rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
.card-header h3 { color: #2c3e50; margin: 0; }
.form-hint { font-size: 0.75rem; color: #999; margin-top: 4px; }
.form-actions { display: flex; justify-content: flex-end; gap: 0.5rem; margin-top: 1rem; }

.text-display { padding: 0; border: 1px solid #e5e7eb; border-radius: 4px; overflow: hidden; }
.text-item { display: flex; align-items: center; border-bottom: 1px solid #e5e7eb; }
.text-item:last-child { border-bottom: none; }
.text-label { width: 100px; padding: 0.75rem 1rem; color: #7f8c8d; font-weight: 600; background: #f8fafc; border-right: 1px solid #e5e7eb; }
.text-value { flex: 1; padding: 0.75rem 1rem; color: #2c3e50; font-size: 1rem; }
.image-preview { display: flex; align-items: center; }
.preview-img { max-width: 200px; max-height: 120px; border-radius: 4px; object-fit: cover; }
.no-image { color: #999; font-style: italic; }

.image-uploader { width: 100%; }
.image-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
  width: 200px;
  height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.image-uploader :deep(.el-upload:hover) { border-color: #00a859; }
.uploader-icon { font-size: 28px; color: #8c939d; }
.uploaded-img { width: 200px; height: 140px; object-fit: cover; display: block; }
.upload-wrapper { display: flex; align-items: flex-start; gap: 0.75rem; }
.remove-btn { margin-top: 0.5rem; }

.preview-container { border: 1px solid #e5e7eb; border-radius: 8px; padding: 2rem; background: #fafafa; }
.preview-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; align-items: center; }
.preview-image { border-radius: 12px; overflow: hidden; background: #eee; aspect-ratio: 4/3; display: flex; align-items: center; justify-content: center; }
.preview-image img { width: 100%; height: 100%; object-fit: cover; }
.preview-placeholder { color: #999; }
.preview-subtitle { display: inline-block; background: rgba(0, 168, 89, 0.1); color: #00a859; padding: 0.3rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600; margin-bottom: 1rem; }
.preview-title { font-size: 1.8rem; color: #1a202c; margin: 0 0 1rem; }
.preview-desc { color: #4a5568; line-height: 1.6; margin-bottom: 1.5rem; }
.preview-btn { background: #00a859; color: white; border: none; padding: 0.75rem 2rem; border-radius: 6px; font-size: 1rem; cursor: pointer; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } .preview-grid { grid-template-columns: 1fr; } }
</style>