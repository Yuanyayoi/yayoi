<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>荣誉管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>荣誉列表</h3>
            <el-button type="primary" @click="showAddDialog">+ 添加荣誉</el-button>
          </div>
          <el-table :data="awards" border stripe>
            <el-table-column prop="title" label="荣誉名称" />
            <el-table-column prop="organization" label="颁发机构" />
            <el-table-column prop="year" label="年份" width="100" />
            <el-table-column prop="enabled" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.enabled ? 'success' : 'danger'">
                  {{ scope.row.enabled ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editAward(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteAward(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </main>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑荣誉' : '添加荣誉'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="荣誉名称">
          <el-input v-model="form.title" placeholder="请输入荣誉名称" />
        </el-form-item>
        <el-form-item label="颁发机构">
          <el-input v-model="form.organization" placeholder="请输入颁发机构" />
        </el-form-item>
        <el-form-item label="年份">
          <el-input v-model="form.year" placeholder="如：2024" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入荣誉描述" />
        </el-form-item>
        <el-form-item label="图片">
          <div class="upload-wrapper">
            <el-upload
              class="image-uploader"
              :show-file-list="false"
              :http-request="handleImageUpload"
              accept="image/*"
            >
              <img v-if="form.image" :src="form.image" class="uploaded-image" />
              <el-icon v-else class="uploader-icon"><Plus /></el-icon>
            </el-upload>
            <el-button v-if="form.image" type="danger" size="small" @click="removeImage" class="remove-btn">移除</el-button>
          </div>
          <div class="upload-hint">支持 jpg、png、gif 格式</div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.enabled" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAward">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref } from 'vue'
import api from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'


const awards = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  title: '',
  organization: '',
  year: '',
  description: '',
  image: '',
  sortOrder: 0,
  enabled: true
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const loadAwards = async () => {
  try {
    const res = await api.get('/api/awards')
    if (res.data.code === 200) {
      awards.value = res.data.data
    }
  } catch (e) {
    console.error('加载荣誉列表失败:', e)
  }
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    id: null,
    title: '',
    organization: '',
    year: '',
    description: '',
    image: '',
    sortOrder: 0,
    enabled: true
  }
  dialogVisible.value = true
}

const editAward = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const saveAward = async () => {
  try {
    if (isEdit.value) {
      await api.put(`/api/awards/${form.value.id}`, form.value)
      ElMessage.success('更新成功')
    } else {
      await api.post('/api/awards', form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    await loadAwards()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  }
}

const deleteAward = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个荣誉吗？', '确认删除', {
      type: 'warning'
    })
    await api.delete(`/api/awards/${id}`)
    ElMessage.success('删除成功')
    await loadAwards()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleImageUpload = async (options) => {
  const file = options.file
  const formData = new FormData()
  formData.append('file', file)
  formData.append('folder', 'award')
  try {
    const res = await api.post('/api/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.code === 200) {
      form.value.image = res.data.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

const removeImage = async () => {
  if (!form.value.image) return
  try {
    await api.delete('/api/upload/delete', { params: { fileUrl: form.value.image } })
  } catch (e) {
    // 忽略删除文件失败
  }
  form.value.image = ''
  ElMessage.success('图片已移除')
}

onMounted(() => {
  loadAwards()
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
.uploaded-image { width: 200px; height: 140px; object-fit: cover; display: block; }
.upload-hint { font-size: 0.75rem; color: #999; margin-top: 4px; }
.upload-wrapper { display: flex; align-items: flex-start; gap: 0.75rem; }
.remove-btn { margin-top: 0.5rem; }

@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
