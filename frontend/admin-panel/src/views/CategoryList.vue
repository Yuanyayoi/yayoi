<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>分类管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>产品分类管理</h3>
            <el-button type="primary" @click="openDialog(null)">+ 添加分类</el-button>
          </div>
          <p class="card-desc">管理公司网站产品页面的分类标签，支持父子层级结构</p>

          <div v-loading="loading">
            <div v-for="parent in parentList" :key="parent.id" class="parent-group">
              <div class="parent-header">
                <div class="parent-info">
                  <el-tag type="success" size="small">顶级</el-tag>
                  <span class="parent-name">{{ parent.name }}</span>
                  <span class="parent-desc" v-if="parent.description">— {{ parent.description }}</span>
                </div>
                <div class="parent-actions">
                  <el-input-number
                    v-model="parent.sortOrder"
                    :min="0"
                    :step="1"
                    size="small"
                    controls-position="right"
                    style="width: 110px"
                    @change="handleSortChange(parent)"
                  />
                  <el-switch
                    v-model="parent.enabled"
                    active-text="启用"
                    inactive-text="禁用"
                    inline-prompt
                    @change="handleStatusChange(parent)"
                  />
                  <el-button size="small" type="primary" link @click="openDialog(parent)">编辑</el-button>
                  <el-button size="small" type="danger" link @click="handleDelete(parent)">删除</el-button>
                </div>
              </div>

              <div class="children-table" v-if="getChildren(parent.id).length > 0">
                <div v-for="child in getChildren(parent.id)" :key="child.id" class="child-row">
                  <div class="child-info">
                    <span class="child-name">{{ child.name }}</span>
                    <span class="child-desc" v-if="child.description">— {{ child.description }}</span>
                  </div>
                  <div class="child-actions">
                    <el-input-number
                      v-model="child.sortOrder"
                      :min="0"
                      :step="1"
                      size="small"
                      controls-position="right"
                      style="width: 110px"
                      @change="handleSortChange(child)"
                    />
                    <el-switch
                      v-model="child.enabled"
                      active-text="启用"
                      inactive-text="禁用"
                      inline-prompt
                      @change="handleStatusChange(child)"
                    />
                    <el-button size="small" type="primary" link @click="openDialog(child)">编辑</el-button>
                    <el-button size="small" type="danger" link @click="handleDelete(child)">删除</el-button>
                  </div>
                </div>
              </div>
              <div v-else class="no-children">
                <span>暂无子分类，点击上方"添加分类"并选择该父分类来添加</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-dialog v-model="dialogVisible" :title="editingId ? '编辑分类' : '添加分类'" width="500px" destroy-on-close>
        <el-form :model="form" label-width="80px">
          <el-form-item label="分类名称">
            <el-input v-model="form.name" placeholder="请输入分类名称" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" placeholder="请输入分类描述" />
          </el-form-item>
          <el-form-item label="父分类">
            <el-select v-model="form.parentId" placeholder="留空为顶级分类" clearable style="width: 100%">
              <el-option v-for="cat in parentCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="form.sortOrder" :min="0" :step="1" style="width: 200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="form.enabled" active-text="启用" inactive-text="禁用" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { computed, onMounted, ref } from 'vue'
import { categoryApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'


const loading = ref(false)
const saving = ref(false)
const categories = ref([])
const dialogVisible = ref(false)
const editingId = ref(null)
const form = ref({ name: '', description: '', parentId: null, sortOrder: 0, enabled: true })

const parentList = computed(() => {
  return categories.value
    .filter(c => c.parentId === null)
    .sort((a, b) => a.sortOrder - b.sortOrder)
})

const parentCategories = computed(() => {
  return categories.value.filter(c => c.parentId === null && c.id !== editingId.value)
})

const getChildren = (parentId) => {
  return categories.value
    .filter(c => c.parentId === parentId)
    .sort((a, b) => a.sortOrder - b.sortOrder)
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const load = async () => {
  loading.value = true
  try {
    const res = await categoryApi.getCategories()
    categories.value = res.data.data || []
  } catch (e) { console.error('加载失败', e) }
  finally { loading.value = false }
}

const openDialog = (row) => {
  if (row) {
    editingId.value = row.id
    form.value = { name: row.name, description: row.description || '', parentId: row.parentId || null, sortOrder: row.sortOrder || 0, enabled: row.enabled }
  } else {
    editingId.value = null
    form.value = { name: '', description: '', parentId: null, sortOrder: 0, enabled: true }
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  saving.value = true
  try {
    if (editingId.value) {
      await categoryApi.updateCategory(editingId.value, form.value)
      ElMessage.success('分类更新成功')
    } else {
      await categoryApi.createCategory(form.value)
      ElMessage.success('分类添加成功')
    }
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error('保存失败')
  }
  finally { saving.value = false }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该分类？', '确认', { type: 'warning' })
  try {
    await categoryApi.deleteCategory(row.id)
    ElMessage.success('删除成功')
    load()
  } catch (e) { /* 取消删除 */ }
}

const handleSortChange = async (row) => {
  try {
    await categoryApi.updateCategory(row.id, {
      name: row.name,
      description: row.description,
      parentId: row.parentId,
      sortOrder: row.sortOrder,
      enabled: row.enabled
    })
    ElMessage.success('排序已更新')
  } catch (e) {
    ElMessage.error('排序更新失败')
    load()
  }
}

const handleStatusChange = async (row) => {
  try {
    await categoryApi.updateCategory(row.id, {
      name: row.name,
      description: row.description,
      parentId: row.parentId,
      sortOrder: row.sortOrder,
      enabled: row.enabled
    })
    ElMessage.success(row.enabled ? '已启用' : '已禁用')
  } catch (e) {
    ElMessage.error('状态更新失败')
    load()
  }
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
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
.card-header h3 { color: #2c3e50; margin: 0; }
.card-desc { color: #7f8c8d; font-size: 0.85rem; margin-bottom: 1.5rem; }

.parent-group {
  border: 1px solid #e8ecf1;
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
}

.parent-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 20px;
  background: #f8fafb;
  border-bottom: 1px solid #e8ecf1;
}

.parent-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.parent-name {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
}

.parent-desc {
  font-size: 0.85rem;
  color: #9ca3af;
}

.parent-actions, .child-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.children-table {
  background: white;
}

.child-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px 12px 48px;
  border-bottom: 1px solid #f3f4f6;
  transition: background 0.2s;
}

.child-row:last-child {
  border-bottom: none;
}

.child-row:hover {
  background: #f9fafb;
}

.child-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.child-name {
  font-size: 0.92rem;
  font-weight: 500;
  color: #374151;
}

.child-desc {
  font-size: 0.82rem;
  color: #9ca3af;
}

.no-children {
  padding: 16px 20px 16px 48px;
  font-size: 0.85rem;
  color: #c0c4cc;
  background: #fafbfc;
}

@media (max-width: 768px) {
  .admin-page { flex-direction: column; }
  .parent-header, .child-row { flex-direction: column; align-items: flex-start; gap: 8px; }
  .parent-actions, .child-actions { width: 100%; justify-content: flex-end; }
}
</style>
