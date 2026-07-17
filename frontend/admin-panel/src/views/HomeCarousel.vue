<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>轮播图管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>轮播图列表</h3>
            <el-button type="primary" @click="openDialog(null)">+ 新增轮播图</el-button>
          </div>
          <el-table :data="carousels" border v-loading="loading">
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="subtitle" label="副标题" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column label="图片" width="120">
              <template #default="scope">
                <img :src="scope.row.imageUrl" style="width:80px;height:auto;border-radius:4px" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160">
              <template #default="scope">
                <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" link @click="del(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <p v-if="!loading && carousels.length === 0" style="text-align:center;color:#999;padding:2rem">暂无轮播图</p>
        </div>
      </div>

      <el-dialog v-model="dialogVisible" :title="form.id ? '编辑轮播图' : '新增轮播图'" width="550px" destroy-on-close>
        <el-form :model="form" label-width="100px">
          <el-form-item label="标题">
            <el-input v-model="form.title" placeholder="请输入轮播图标题" />
          </el-form-item>
          <el-form-item label="副标题">
            <el-input v-model="form.subtitle" placeholder="请输入轮播图副标题" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入轮播图描述" />
          </el-form-item>
          <el-form-item label="图片">
            <el-upload
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="{ folder: 'carousel' }"
              :on-success="handleUpload"
              :on-error="handleUploadError"
              :show-file-list="false"
              accept="image/*"
              name="file"
            >
              <el-button type="primary">点击上传图片</el-button>
            </el-upload>
            <div v-if="form.imageUrl" class="image-preview">
              <img :src="form.imageUrl" alt="预览" />
              <el-button type="danger" size="small" @click="form.imageUrl = ''">移除</el-button>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="save">{{ form.id ? '更新' : '保存' }}</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref, computed } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'


const uploadUrl = '/api/upload'
const uploadHeaders = computed(() => {
  const token = sessionStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})
const saving = ref(false)
const loading = ref(false)
const carousels = ref([])
const dialogVisible = ref(false)

const emptyForm = () => ({ id: null, title: '', subtitle: '', description: '', imageUrl: '' })
const form = ref(emptyForm())

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const handleUpload = (response) => {
  form.value.imageUrl = response?.data?.url || response?.url || ''
}

const handleUploadError = () => {
  ElMessage.error('上传失败')
}

const openDialog = (row) => {
  if (row) {
    form.value = { ...row }
  } else {
    form.value = emptyForm()
  }
  dialogVisible.value = true
}

const loadCarousels = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/carousels')
    if (res.data.success) carousels.value = res.data.data
  } catch (e) {
    console.error('加载轮播图失败', e)
  } finally {
    loading.value = false
  }
}

const save = async () => {
  if (!form.value.imageUrl) { ElMessage.warning('请上传图片'); return }
  saving.value = true
  try {
    const data = { title: form.value.title, subtitle: form.value.subtitle, description: form.value.description, imageUrl: form.value.imageUrl }
    if (form.value.id) {
      await axios.put(`/api/carousels/${form.value.id}`, data)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/carousels', data)
      ElMessage.success('保存成功')
    }
    dialogVisible.value = false
    await loadCarousels()
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const del = async (id) => {
  await ElMessageBox.confirm('确定删除该轮播图吗？', '确认', { type: 'warning' })
  try {
    await axios.delete(`/api/carousels/${id}`)
    ElMessage.success('删除成功')
    await loadCarousels()
  } catch (e) { /* 取消删除 */ }
}

onMounted(() => {
  loadCarousels()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; }
.content-wrapper { padding: 2rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
.card-header h3 { color: #2c3e50; margin: 0; }
.image-preview { margin-top: 1rem; display: flex; align-items: center; gap: 1rem; }
.image-preview img { max-width: 300px; max-height: 200px; border-radius: 8px; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>