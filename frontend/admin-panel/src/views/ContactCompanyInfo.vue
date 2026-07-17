<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>联系我们 - 公司信息</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>公司联系信息</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">公司地址</span>
              <span class="text-value">{{ formData.address || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">联系电话</span>
              <span class="text-value">{{ formData.phone || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">邮箱地址</span>
              <span class="text-value">{{ formData.email || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">营业时间</span>
              <span class="text-value">{{ formData.businessHours || '未设置' }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="公司地址">
              <el-input v-model="formData.address" placeholder="请输入公司地址" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="邮箱地址">
              <el-input v-model="formData.email" placeholder="请输入邮箱地址" />
            </el-form-item>
            <el-form-item label="营业时间">
              <el-input v-model="formData.businessHours" placeholder="请输入营业时间" />
            </el-form-item>
            <div class="form-actions">
              <el-button @click="toggleEdit">取消</el-button>
              <el-button type="primary" :loading="saving" @click="saveFormData">保存</el-button>
            </div>
          </el-form>
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
  address: '',
  phone: '',
  email: '',
  businessHours: ''
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
    const response = await siteApi.getContact()
    const d = response.data.data || {}
    formData.value = {
      address: d.address || '',
      phone: d.phone || '',
      email: d.email || '',
      businessHours: d.businessHours || ''
    }
  } catch (e) {
    console.error('加载失败', e)
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    await siteApi.saveContact(formData.value)
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

@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
