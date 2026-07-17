<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>联系我们 - 提交留言</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>留言表单配置</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">姓名标签</span>
              <span class="text-value">{{ formData.nameLabel || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">姓名占位符</span>
              <span class="text-value">{{ formData.namePlaceholder || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">电话标签</span>
              <span class="text-value">{{ formData.phoneLabel || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">电话占位符</span>
              <span class="text-value">{{ formData.phonePlaceholder || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">邮箱标签</span>
              <span class="text-value">{{ formData.emailLabel || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">邮箱占位符</span>
              <span class="text-value">{{ formData.emailPlaceholder || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">公司标签</span>
              <span class="text-value">{{ formData.companyLabel || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">公司占位符</span>
              <span class="text-value">{{ formData.companyPlaceholder || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">留言标签</span>
              <span class="text-value">{{ formData.messageLabel || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">留言占位符</span>
              <span class="text-value">{{ formData.messagePlaceholder || '未设置' }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">提交按钮文字</span>
              <span class="text-value">{{ formData.submitButtonText || '未设置' }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="120px">
            <el-form-item label="姓名标签">
              <el-input v-model="formData.nameLabel" placeholder="请输入姓名标签" />
            </el-form-item>
            <el-form-item label="姓名占位符">
              <el-input v-model="formData.namePlaceholder" placeholder="请输入姓名占位符" />
            </el-form-item>
            <el-form-item label="电话标签">
              <el-input v-model="formData.phoneLabel" placeholder="请输入电话标签" />
            </el-form-item>
            <el-form-item label="电话占位符">
              <el-input v-model="formData.phonePlaceholder" placeholder="请输入电话占位符" />
            </el-form-item>
            <el-form-item label="邮箱标签">
              <el-input v-model="formData.emailLabel" placeholder="请输入邮箱标签" />
            </el-form-item>
            <el-form-item label="邮箱占位符">
              <el-input v-model="formData.emailPlaceholder" placeholder="请输入邮箱占位符" />
            </el-form-item>
            <el-form-item label="公司标签">
              <el-input v-model="formData.companyLabel" placeholder="请输入公司标签" />
            </el-form-item>
            <el-form-item label="公司占位符">
              <el-input v-model="formData.companyPlaceholder" placeholder="请输入公司占位符" />
            </el-form-item>
            <el-form-item label="留言标签">
              <el-input v-model="formData.messageLabel" placeholder="请输入留言标签" />
            </el-form-item>
            <el-form-item label="留言占位符">
              <el-input v-model="formData.messagePlaceholder" type="textarea" :rows="3" placeholder="请输入留言占位符" />
            </el-form-item>
            <el-form-item label="提交按钮文字">
              <el-input v-model="formData.submitButtonText" placeholder="请输入提交按钮文字" />
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
            <form class="preview-form" @submit.prevent>
              <div class="form-row">
                <div class="form-group">
                  <label>{{ formData.nameLabel || '姓名' }} <span class="required">*</span></label>
                  <input type="text" :placeholder="formData.namePlaceholder || '请输入您的姓名'" readonly />
                </div>
                <div class="form-group">
                  <label>{{ formData.phoneLabel || '电话' }} <span class="required">*</span></label>
                  <input type="tel" :placeholder="formData.phonePlaceholder || '请输入您的联系电话'" readonly />
                </div>
              </div>
              <div class="form-row">
                <div class="form-group">
                  <label>{{ formData.emailLabel || '邮箱' }}</label>
                  <input type="email" :placeholder="formData.emailPlaceholder || '请输入您的邮箱地址'" readonly />
                </div>
                <div class="form-group">
                  <label>{{ formData.companyLabel || '公司' }}</label>
                  <input type="text" :placeholder="formData.companyPlaceholder || '请输入您的公司名称'" readonly />
                </div>
              </div>
              <div class="form-group">
                <label>{{ formData.messageLabel || '留言内容' }} <span class="required">*</span></label>
                <textarea rows="5" :placeholder="formData.messagePlaceholder || '请详细描述您的需求或问题，以便我们为您提供更精准的服务...'" readonly></textarea>
              </div>
              <button type="button" class="btn btn-primary w-full">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
                <span>{{ formData.submitButtonText || '提交留言' }}</span>
              </button>
            </form>
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
  nameLabel: '',
  namePlaceholder: '',
  phoneLabel: '',
  phonePlaceholder: '',
  emailLabel: '',
  emailPlaceholder: '',
  companyLabel: '',
  companyPlaceholder: '',
  messageLabel: '',
  messagePlaceholder: '',
  submitButtonText: ''
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
    const res = await siteApi.getContactForm()
    const d = res.data.data || {}
    formData.value = {
      nameLabel: d.nameLabel || '',
      namePlaceholder: d.namePlaceholder || '',
      phoneLabel: d.phoneLabel || '',
      phonePlaceholder: d.phonePlaceholder || '',
      emailLabel: d.emailLabel || '',
      emailPlaceholder: d.emailPlaceholder || '',
      companyLabel: d.companyLabel || '',
      companyPlaceholder: d.companyPlaceholder || '',
      messageLabel: d.messageLabel || '',
      messagePlaceholder: d.messagePlaceholder || '',
      submitButtonText: d.submitButtonText || ''
    }
  } catch (e) {
    console.error('加载失败', e)
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    await siteApi.saveContactForm(formData.value)
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
.text-label { width: 120px; color: #8c8c8c; font-size: 0.875rem; flex-shrink: 0; }
.text-value { color: #262626; font-size: 0.9rem; word-break: break-all; }

.form-actions { display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.25rem; }

.preview-wrapper {
  background: #f7fafc;
  border-radius: 12px;
  padding: 2rem;
}

.preview-form {
  background: white;
  padding: 2.5rem;
  border-radius: 20px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  border: 1px solid #e2e8f0;
  max-width: 600px;
  margin: 0 auto;
}

.preview-form .form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
  margin-bottom: 0.5rem;
}

.preview-form .form-group {
  margin-bottom: 1.25rem;
}

.preview-form .form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #1a365d;
  font-weight: 600;
  font-size: 0.88rem;
}

.preview-form .required {
  color: #e53e3e;
}

.preview-form .form-group input,
.preview-form .form-group textarea {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 1.5px solid #e2e8f0;
  border-radius: 10px;
  font-size: 0.95rem;
  outline: none;
  font-family: inherit;
  background: #f8fafc;
  color: #1a365d;
  box-sizing: border-box;
}

.preview-form .form-group input::placeholder,
.preview-form .form-group textarea::placeholder {
  color: #a0aec0;
}

.preview-form .btn {
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

.preview-form .btn-primary {
  background: linear-gradient(135deg, #00a859 0%, #008f4d 100%);
  color: white;
  box-shadow: 0 4px 16px rgba(0, 168, 89, 0.3);
}

.preview-form .w-full {
  width: 100%;
}

@media (max-width: 768px) {
  .admin-page { flex-direction: column; }
  .preview-form .form-row { grid-template-columns: 1fr; }
  .preview-form { padding: 1.5rem; }
}
</style>
