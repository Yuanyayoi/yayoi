<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>Hero横幅</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>横幅内容</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">公司名称</span>
              <span class="text-value">{{ formData.companyName }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">公司描述</span>
              <span class="text-value">{{ formData.companyDesc }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="公司名称">
              <el-input v-model="formData.companyName" placeholder="如：四川国驰恒创科技有限公司" />
            </el-form-item>
            <el-form-item label="公司描述">
              <el-input v-model="formData.companyDesc" type="textarea" :rows="3" placeholder="请输入公司描述" />
            </el-form-item>
            <el-form-item label="背景图片">
              <div class="upload-area">
                <img v-if="formData.backgroundImage" :src="formData.backgroundImage" class="preview-img" />
                <el-upload
                  class="hero-upload"
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :data="{ folder: 'common' }"
                  name="file"
                  :show-file-list="false"
                  :before-upload="beforeUpload"
                  :on-success="onUploadSuccess"
                  :on-error="onUploadError"
                  accept=".jpg,.jpeg,.png,.webp"
                >
                  <el-button type="primary" :loading="uploading">
                    {{ formData.backgroundImage ? '更换图片' : '上传图片' }}
                  </el-button>
                </el-upload>
                <el-button v-if="formData.backgroundImage" type="danger" link @click="removeBackground">移除</el-button>
              </div>
              <div class="form-hint">支持 jpg、png、webp 格式，建议尺寸 1920x1080</div>
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
            <div class="preview-hero" :style="formData.backgroundImage ? { backgroundImage: `url(${formData.backgroundImage})` } : {}">
              <h1>{{ formData.companyName || '四川国驰恒创科技有限公司' }}</h1>
              <p>{{ formData.companyDesc || '核心技术源自美国辛辛那提大学博士团队，专注于提供新能源电池快充技术解决方案。' }}</p>
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
import axios from 'axios'
import { ElMessage } from 'element-plus'


const saving = ref(false)
const uploading = ref(false)
const isEditing = ref(false)
const formData = ref({
  companyName: '',
  companyDesc: '',
  backgroundImage: ''
})

const uploadUrl = '/api/upload'
const uploadHeaders = computed(() => {
  const token = sessionStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const loadData = async () => {
  try {
    const res = await axios.get('/api/site/about-hero')
    if (res.data.code === 200) {
      formData.value = res.data.data
    }
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}

const beforeUpload = (file) => {
  const validTypes = ['image/jpeg', 'image/png', 'image/webp']
  if (!validTypes.includes(file.type)) {
    ElMessage.error('仅支持 jpg、png、webp 格式')
    return false
  }
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  uploading.value = true
  return true
}

const onUploadSuccess = (response) => {
  uploading.value = false
  const url = response?.data?.url || response?.url || ''
  if (url) {
    formData.value.backgroundImage = url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response?.message || '上传失败')
  }
}

const onUploadError = () => {
  uploading.value = false
  ElMessage.error('图片上传失败，请重试')
}

const removeBackground = () => {
  formData.value.backgroundImage = ''
}

const saveFormData = async () => {
  saving.value = true
  try {
    const res = await axios.put('/api/site/about-hero', formData.value)
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
.form-actions { display: flex; justify-content: flex-end; gap: 0.5rem; margin-top: 1rem; }

.text-display { padding: 0; border: 1px solid #e5e7eb; border-radius: 4px; overflow: hidden; }
.text-item { display: flex; align-items: center; border-bottom: 1px solid #e5e7eb; }
.text-item:last-child { border-bottom: none; }
.text-label { width: 100px; padding: 0.75rem 1rem; color: #7f8c8d; font-weight: 600; background: #f8fafc; border-right: 1px solid #e5e7eb; }
.text-value { flex: 1; padding: 0.75rem 1rem; color: #2c3e50; font-size: 1rem; }

.preview-container { border: 1px solid #e5e7eb; border-radius: 8px; overflow: hidden; }
.preview-hero {
  background: linear-gradient(135deg, #0f2027 0%, #203a43 50%, #2c5364 100%);
  background-size: cover;
  background-position: center;
  padding: 4rem 2rem;
  text-align: center;
  color: white;
  min-height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.preview-hero h1 { font-size: 2.5rem; font-weight: 800; margin: 0 0 1rem; text-shadow: 0 2px 4px rgba(0,0,0,0.5); }
.preview-hero p { font-size: 1rem; color: rgba(255,255,255,0.9); max-width: 600px; margin: 0 auto; line-height: 1.6; text-shadow: 0 1px 2px rgba(0,0,0,0.5); }

.upload-area { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.preview-img { width: 120px; height: 80px; object-fit: cover; border: 1px solid #e5e7eb; border-radius: 6px; }
.form-hint { font-size: 0.75rem; color: #999; margin-top: 4px; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
