<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>XRE亮点模块管理</h1>
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
              <span class="text-label">副标题</span>
              <span class="text-value">{{ formData.subtitle }}</span>
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
              <span class="text-label">按钮文字</span>
              <span class="text-value">{{ formData.buttonText }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮链接</span>
              <span class="text-value">{{ getPageLabel(formData.buttonLink) }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">指标数量</span>
              <span class="text-value">{{ formData.metrics?.length || 0 }} 个</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="副标题">
              <el-input v-model="formData.subtitle" placeholder="如：XRE 高能极速超充" />
            </el-form-item>
            <el-form-item label="主标题">
              <el-input v-model="formData.title" placeholder="如：以 10C 快充技术服务新能源产业" />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="formData.description" type="textarea" :rows="4" placeholder="请输入描述内容" />
            </el-form-item>
            <el-form-item label="按钮文字">
              <el-input v-model="formData.buttonText" placeholder="如：了解产品服务" />
            </el-form-item>
            <el-form-item label="按钮链接">
              <el-select v-model="formData.buttonLink" placeholder="选择跳转页面" style="width: 100%">
                <el-option v-for="page in pageOptions" :key="page.value" :label="page.label" :value="page.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="指标数据">
              <div class="metrics-editor">
                <div v-for="(metric, index) in formData.metrics" :key="index" class="metric-item">
                  <el-input v-model="metric.value" placeholder="数值" style="width: 120px" />
                  <el-input v-model="metric.label" placeholder="标签" style="width: 200px" />
                  <el-button type="danger" size="small" @click="removeMetric(index)">删除</el-button>
                </div>
                <el-button type="primary" size="small" @click="addMetric">+ 添加指标</el-button>
              </div>
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
              <div class="preview-copy">
                <span class="preview-subtitle">{{ formData.subtitle || 'XRE 高能极速超充' }}</span>
                <h2 class="preview-title">{{ formData.title || '以 10C 快充技术服务新能源产业' }}</h2>
                <p class="preview-desc">{{ formData.description || '围绕动力电池、储能电池与移动电力场景，融合高速激光成型、3D 打印增材制造和 AI 驱动 BMS，提供从电芯开发、热管理、Pack 集成到量产导入的完整解决方案。' }}</p>
                <button class="preview-btn">{{ formData.buttonText || '了解产品服务' }}</button>
              </div>
              <div class="preview-metrics">
                <div v-for="metric in formData.metrics" :key="metric.label" class="preview-metric">
                  <strong>{{ metric.value || '0' }}</strong>
                  <span>{{ metric.label || '指标标签' }}</span>
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
import axios from 'axios'
import { ElMessage } from 'element-plus'


const saving = ref(false)
const isEditing = ref(false)
const formData = ref({
  subtitle: '',
  title: '',
  description: '',
  buttonText: '',
  buttonLink: '',
  metrics: []
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

const addMetric = () => {
  formData.value.metrics.push({ value: '', label: '' })
}

const removeMetric = (index) => {
  formData.value.metrics.splice(index, 1)
}

const loadData = async () => {
  try {
    const res = await axios.get('/api/site/modules')
    if (res.data.code === 200 && res.data.data?.homeSpotlight) {
      const spotlight = res.data.data.homeSpotlight
      formData.value = {
        subtitle: spotlight.subtitle || '',
        title: spotlight.title || '',
        description: spotlight.description || '',
        buttonText: spotlight.buttonText || '',
        buttonLink: spotlight.buttonLink || '',
        metrics: spotlight.metrics || []
      }
    }
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    const res = await axios.get('/api/site/modules')
    const modules = res.data.code === 200 ? res.data.data : {}
    modules.homeSpotlight = {
      subtitle: formData.value.subtitle,
      title: formData.value.title,
      description: formData.value.description,
      buttonText: formData.value.buttonText,
      buttonLink: formData.value.buttonLink,
      metrics: formData.value.metrics
    }
    const saveRes = await axios.put('/api/site/modules', modules)
    if (saveRes.data.code === 200) {
      ElMessage.success('保存成功')
      isEditing.value = false
    } else {
      ElMessage.error(saveRes.data.message || '保存失败')
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

.metrics-editor { width: 100%; }
.metric-item { display: flex; gap: 0.5rem; margin-bottom: 0.5rem; align-items: center; }

.preview-container { border: 1px solid #e5e7eb; border-radius: 8px; padding: 2rem; background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); color: white; }
.preview-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; align-items: center; }
.preview-copy .preview-subtitle { display: inline-block; background: rgba(0, 168, 89, 0.2); color: #33d17a; padding: 0.3rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600; margin-bottom: 1rem; }
.preview-title { font-size: 2rem; color: white; margin: 0 0 1rem; line-height: 1.2; }
.preview-desc { color: rgba(255, 255, 255, 0.8); line-height: 1.6; margin-bottom: 1.5rem; }
.preview-btn { background: #00a859; color: white; border: none; padding: 0.75rem 2rem; border-radius: 6px; font-size: 1rem; cursor: pointer; }
.preview-metrics { display: grid; grid-template-columns: 1fr 1fr; gap: 1px; background: rgba(255, 255, 255, 0.1); border: 1px solid rgba(255, 255, 255, 0.15); border-radius: 8px; overflow: hidden; }
.preview-metric { min-height: 100px; padding: 1.5rem; background: rgba(255, 255, 255, 0.05); backdrop-filter: blur(10px); }
.preview-metric strong { display: block; color: #33d17a; font-size: 2rem; line-height: 1.1; margin-bottom: 0.5rem; font-weight: 700; }
.preview-metric span { color: rgba(255, 255, 255, 0.7); font-size: 0.85rem; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } .preview-grid { grid-template-columns: 1fr; } }
</style>