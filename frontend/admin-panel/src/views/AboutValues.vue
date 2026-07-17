<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>核心价值观</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>核心价值观列表</h3>
            <el-button type="primary" @click="showAddDialog">+ 添加价值观</el-button>
          </div>
          <div class="values-grid">
            <div v-for="item in values" :key="item.id" class="value-card">
              <div class="card-actions">
                <el-button size="small" @click="editItem(item)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteItem(item.id)">删除</el-button>
              </div>
              <div class="value-icon">{{ item.icon || '🎯' }}</div>
              <h4>{{ item.title }}</h4>
              <p>{{ item.description }}</p>
              <div class="value-meta">
                <el-tag :type="item.enabled ? 'success' : 'info'" size="small">{{ item.enabled ? '启用' : '停用' }}</el-tag>
                <span class="sort">排序: {{ item.sortOrder }}</span>
              </div>
            </div>
            <div v-if="values.length === 0" class="empty-state">
              <p>暂无核心价值观，点击"+ 添加价值观"开始</p>
            </div>
          </div>
        </div>
      </div>
    </main>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑价值观' : '添加价值观'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="例如：创新驱动" />
        </el-form-item>
        <el-form-item label="图标(emoji)">
          <el-input v-model="form.icon" placeholder="例如：🎯" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="启用">
          <el-switch v-model="form.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveItem">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'


const saving = ref(false)
const values = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const emptyForm = () => ({ id: null, title: '', icon: '', description: '', sortOrder: 0, enabled: true })
const form = ref(emptyForm())

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const showAddDialog = () => {
  form.value = emptyForm()
  isEdit.value = false
  dialogVisible.value = true
}

const editItem = (item) => {
  form.value = { ...item }
  isEdit.value = true
  dialogVisible.value = true
}

const load = async () => {
  try {
    const res = await axios.get('/api/core-values')
    values.value = res.data.data || []
  } catch (e) { console.error('加载失败', e) }
}

const saveItem = async () => {
  if (!form.value.title.trim()) { ElMessage.warning('请输入标题'); return }
  saving.value = true
  try {
    if (form.value.id) {
      await axios.put(`/api/core-values/${form.value.id}`, form.value)
    } else {
      await axios.post('/api/core-values', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await load()
  } catch (e) { ElMessage.error('保存失败') }
  finally { saving.value = false }
}

const deleteItem = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该价值观吗？', '提示', { type: 'warning' })
    await axios.delete(`/api/core-values/${id}`)
    ElMessage.success('删除成功')
    await load()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => {
  load()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; }
.content-wrapper { padding: 2rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
.card-header h3 { color: #2c3e50; margin: 0; }

.values-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 1.5rem; }
.value-card { background: #f8fafc; border-radius: 12px; padding: 1.5rem; position: relative; transition: all 0.3s; border: 1px solid #e5e7eb; text-align: center; }
.value-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.08); transform: translateY(-2px); }
.card-actions { position: absolute; top: 1rem; right: 1rem; display: flex; gap: 0.5rem; }
.value-icon { font-size: 2.5rem; margin-bottom: 0.75rem; }
.value-card h4 { color: #2c3e50; margin: 0 0 0.5rem; font-size: 1.1rem; }
.value-card p { color: #718096; font-size: 0.85rem; line-height: 1.5; margin: 0 0 1rem; }
.value-meta { display: flex; justify-content: center; align-items: center; gap: 1rem; }
.value-meta .sort { color: #a0aec0; font-size: 0.8rem; }
.empty-state { grid-column: 1 / -1; text-align: center; padding: 3rem; color: #a0aec0; }

@media (max-width: 768px) { .admin-page { flex-direction: column; } .values-grid { grid-template-columns: 1fr; } }
</style>
