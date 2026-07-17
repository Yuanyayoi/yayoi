<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>新闻分类管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>分类列表</h3>
            <el-button type="primary" @click="handleAdd">+ 添加分类</el-button>
          </div>
          <el-table :data="categories" border style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="分类名称" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="sortOrder" label="排序" width="100" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'">
                  {{ row.enabled ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <el-dialog v-model="dialogVisible" :title="editingId ? '编辑分类' : '添加分类'" width="500px">
        <el-form :model="formData" label-width="100px">
          <el-form-item label="分类名称">
            <el-input v-model="formData.name" placeholder="请输入分类名称" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述" />
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="formData.sortOrder" :min="0" />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="formData.enabled" active-text="启用" inactive-text="禁用" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref } from 'vue'
import { newsCategoryApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'


const saving = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const categories = ref([])
const formData = ref({
  name: '',
  description: '',
  sortOrder: 0,
  enabled: true
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const loadData = async () => {
  try {
    const res = await newsCategoryApi.getCategories()
    categories.value = res.data.data || []
  } catch (e) {
    console.error('加载分类失败', e)
  }
}

const handleAdd = () => {
  editingId.value = null
  formData.value = { name: '', description: '', sortOrder: 0, enabled: true }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  editingId.value = row.id
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  saving.value = true
  try {
    if (editingId.value) {
      await newsCategoryApi.updateCategory(editingId.value, formData.value)
      ElMessage.success('更新成功')
    } else {
      await newsCategoryApi.createCategory(formData.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    type: 'warning'
  })
  try {
    await newsCategoryApi.deleteCategory(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    ElMessage.error('删除失败')
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
.content-wrapper { padding: 2rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
.card-header h3 { color: #2c3e50; margin: 0; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
