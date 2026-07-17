<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>联系我们 - 在线咨询</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>在线咨询文案</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">标题</span>
              <span class="text-value">{{ formData.title || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">副标题</span>
              <span class="text-value">{{ formData.subtitle || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">描述</span>
              <span class="text-value">{{ formData.description || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">特色项1</span>
              <span class="text-value">{{ formData.feature1Title || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">特色项2</span>
              <span class="text-value">{{ formData.feature2Title || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">特色项3</span>
              <span class="text-value">{{ formData.feature3Title || '未设置' }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="标题">
              <el-input v-model="formData.title" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item label="副标题">
              <el-input v-model="formData.subtitle" placeholder="请输入副标题" />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="formData.description" type="textarea" :rows="4" placeholder="请输入描述文字" />
            </el-form-item>
            <el-form-item label="特色项1">
              <el-input v-model="formData.feature1Title" placeholder="请输入特色项1标题" />
            </el-form-item>
            <el-form-item label="特色项2">
              <el-input v-model="formData.feature2Title" placeholder="请输入特色项2标题" />
            </el-form-item>
            <el-form-item label="特色项3">
              <el-input v-model="formData.feature3Title" placeholder="请输入特色项3标题" />
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
            <div class="preview-online">
              <span class="section-subtitle">{{ formData.title || '在线咨询' }}</span>
              <h2>{{ formData.subtitle || '发送消息给我们' }}</h2>
              <p>{{ formData.description || '如有任何问题或合作意向，请填写以下表单，我们的专业团队将在24小时内与您联系。' }}</p>
              <div class="form-benefits">
                <div class="benefit-item" v-if="formData.feature1Title">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="18" height="18"><polyline points="20 6 9 17 4 12"/></svg>
                  <span>{{ formData.feature1Title }}</span>
                </div>
                <div class="benefit-item" v-if="formData.feature2Title">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="18" height="18"><polyline points="20 6 9 17 4 12"/></svg>
                  <span>{{ formData.feature2Title }}</span>
                </div>
                <div class="benefit-item" v-if="formData.feature3Title">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="18" height="18"><polyline points="20 6 9 17 4 12"/></svg>
                  <span>{{ formData.feature3Title }}</span>
                </div>
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
import { siteApi } from '@/api'
import { ElMessage } from 'element-plus'

const saving = ref(false)
const isEditing = ref(false)
const formData = ref({
  title: '',
  subtitle: '',
  description: '',
  feature1Title: '',
  feature2Title: '',
  feature3Title: ''
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const load = async () => {
  try {
    const res = await siteApi.getContactOnline()
    const d = res.data.data || {}
    formData.value = {
      title: d.title || '',
      subtitle: d.subtitle || '',
      description: d.description || '',
      feature1Title: d.feature1Title || '',
      feature2Title: d.feature2Title || '',
      feature3Title: d.feature3Title || ''
    }
  } catch (e) {
    console.error('加载失败', e)
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    await siteApi.saveContactOnline(formData.value)
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

.form-actions { display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.25rem; }

.preview-wrapper {
  background: #f7fafc;
  border-radius: 12px;
  padding: 2rem;
}

.preview-online {
  background: #f7fafc;
  color: #1a202c;
  max-width: 500px;
}

.preview-online .section-subtitle {
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

.preview-online h2 {
  font-size: 2.2rem;
  color: #1a365d;
  margin-bottom: 1rem;
  font-weight: 700;
  line-height: 1.3;
}

.preview-online > p {
  color: #4a5568;
  line-height: 1.8;
  margin-bottom: 2rem;
  font-size: 0.95rem;
}

.preview-online .form-benefits {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.preview-online .benefit-item {
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

.preview-online .benefit-item svg {
  color: #00a859;
  flex-shrink: 0;
}

@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
