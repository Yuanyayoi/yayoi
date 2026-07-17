<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>立即行动模块管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>模块配置</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">标签文字</span>
              <span class="text-value">{{ formData.eyebrow }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">主标题</span>
              <span class="text-value">{{ formData.title }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">描述</span>
              <span class="text-value">{{ formData.description }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮1文字</span>
              <span class="text-value">{{ formData.button1Text }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮1链接</span>
              <span class="text-value">{{ getPageLabel(formData.button1Link) }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮2文字</span>
              <span class="text-value">{{ formData.button2Text }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮2链接</span>
              <span class="text-value">{{ getPageLabel(formData.button2Link) }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="标签文字">
              <el-input v-model="formData.eyebrow" placeholder="如：立即行动" />
            </el-form-item>
            <el-form-item label="主标题">
              <el-input v-model="formData.title" placeholder="如：准备好开启新能源技术合作了吗？" />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述内容" />
            </el-form-item>
            <el-form-item label="按钮1文字">
              <el-input v-model="formData.button1Text" placeholder="如：立即咨询" />
            </el-form-item>
            <el-form-item label="按钮1链接">
              <el-select v-model="formData.button1Link" placeholder="选择跳转页面" style="width: 100%">
                <el-option v-for="page in pageOptions" :key="page.value" :label="page.label" :value="page.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="按钮2文字">
              <el-input v-model="formData.button2Text" placeholder="如：了解产品" />
            </el-form-item>
            <el-form-item label="按钮2链接">
              <el-select v-model="formData.button2Link" placeholder="选择跳转页面" style="width: 100%">
                <el-option v-for="page in pageOptions" :key="page.value" :label="page.label" :value="page.value" />
              </el-select>
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
            <div class="preview-section">
              <span class="preview-eyebrow">{{ formData.eyebrow || '立即行动' }}</span>
              <h2 class="preview-title">{{ formData.title || '准备好开启新能源技术合作了吗？' }}</h2>
              <p class="preview-desc">{{ formData.description || '无论是电池快充技术、储能系统还是智能装备，我们的专家团队为您提供从研发到量产的一站式解决方案' }}</p>
              <div class="preview-actions">
                <button class="preview-btn-primary">{{ formData.button1Text || '立即咨询' }}</button>
                <button class="preview-btn-outline">{{ formData.button2Text || '了解产品' }}</button>
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
import axios from 'axios'
import { ElMessage } from 'element-plus'


const saving = ref(false)
const isEditing = ref(false)
const formData = ref({
  eyebrow: '',
  title: '',
  description: '',
  button1Text: '',
  button1Link: '',
  button2Text: '',
  button2Link: ''
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

const loadData = async () => {
  try {
    const res = await axios.get('/api/site/home-cta')
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
    const res = await axios.put('/api/site/home-cta', formData.value)
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

.preview-container { border: 1px solid #e5e7eb; border-radius: 8px; padding: 2rem; background: linear-gradient(135deg, #0a2e1f 0%, #00a859 100%); }
.preview-section { text-align: center; color: white; }
.preview-eyebrow { display: inline-block; background: rgba(255, 255, 255, 0.15); color: rgba(255, 255, 255, 0.9); padding: 0.3rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600; margin-bottom: 1rem; }
.preview-title { font-size: 1.8rem; color: white; margin: 0 0 1rem; }
.preview-desc { color: rgba(255, 255, 255, 0.85); line-height: 1.6; margin-bottom: 2rem; max-width: 600px; margin-left: auto; margin-right: auto; }
.preview-actions { display: flex; gap: 1rem; justify-content: center; }
.preview-btn-primary { background: white; color: #00a859; border: none; padding: 0.75rem 2rem; border-radius: 6px; font-size: 1rem; font-weight: 600; cursor: pointer; }
.preview-btn-outline { background: transparent; color: white; border: 2px solid rgba(255, 255, 255, 0.5); padding: 0.75rem 2rem; border-radius: 6px; font-size: 1rem; font-weight: 600; cursor: pointer; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>