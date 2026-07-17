<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>产品列表</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>产品管理</h3>
            <el-button type="primary" @click="openDialog(null)">+ 添加产品</el-button>
          </div>
          <p class="card-desc">管理公司网站产品服务页面展示的产品，支持添加、编辑和删除</p>

          <div class="filter-bar">
            <el-select v-model="filterParentId" placeholder="按父分类筛选" clearable style="width: 160px" @change="handleFilterChange">
              <el-option label="全部" :value="null" />
              <el-option v-for="cat in parentCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
            <el-select v-model="filterCategoryId" placeholder="按子分类筛选" clearable style="width: 160px" @change="handleFilterChange" :disabled="!filterParentId">
              <el-option label="全部" :value="null" />
              <el-option v-for="cat in filterChildCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </div>

          <el-table :data="products" stripe v-loading="loading" style="width: 100%">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="name" label="名称" min-width="150" />
            <el-table-column label="父分类" width="100">
              <template #default="{ row }">
                <span>{{ getParentName(row.categoryId) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="子分类" width="100">
              <template #default="{ row }">
                <span>{{ getCategoryName(row.categoryId) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="图片" width="100">
              <template #default="{ row }">
                <img v-if="row.image" :src="row.image" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;" />
                <span v-else style="color: #c0c4cc;">无</span>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="价格" width="120">
              <template #default="{ row }">¥{{ row.price }}</template>
            </el-table-column>
            <el-table-column prop="stock" label="库存" width="80" />
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tooltip :content="row.enabled ? '上架' : '下架'" placement="top">
                  <el-switch v-model="row.enabled" @change="handleStatusChange(row)" />
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button size="small" type="primary" link @click="openDialog(row)">编辑</el-button>
                <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <el-dialog v-model="dialogVisible" :title="editingId ? '编辑产品' : '添加产品'" width="600px" destroy-on-close>
        <el-form :model="form" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="form.name" placeholder="请输入产品名称" />
          </el-form-item>
          <el-form-item label="父分类">
            <el-select v-model="form.parentId" placeholder="请选择父分类" clearable style="width: 100%" @change="handleParentChange">
              <el-option v-for="cat in parentCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="子分类">
            <el-select v-model="form.categoryId" placeholder="请选择子分类" clearable style="width: 100%" :disabled="!form.parentId">
              <el-option v-for="cat in childCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入产品描述" />
          </el-form-item>
          <el-form-item label="图片">
            <div class="upload-wrapper">
              <el-upload
                class="image-uploader"
                :show-file-list="false"
                :http-request="handleUpload"
                accept="image/*"
              >
                <img v-if="form.image" :src="form.image" class="uploaded-img" />
                <div v-else class="upload-placeholder">
                  <el-icon class="uploader-icon"><Plus /></el-icon>
                  <span class="upload-text">点击上传</span>
                </div>
              </el-upload>
              <div class="upload-actions">
                <el-button v-if="form.image" type="danger" size="small" @click="removeImage">移除图片</el-button>
              </div>
            </div>
            <div class="form-hint">支持 jpg、png、gif 格式，建议尺寸 800x600</div>
          </el-form-item>
          <el-form-item label="价格">
            <el-input-number v-model="form.price" :min="0" :precision="2" :step="100" style="width: 200px" />
          </el-form-item>
          <el-form-item label="库存">
            <el-input-number v-model="form.stock" :min="0" :step="1" style="width: 200px" />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="form.enabled" active-text="上架" inactive-text="下架" />
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
import { productApi, categoryApi } from '@/api'
import api from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'


const loading = ref(false)
const saving = ref(false)
const products = ref([])
const parentCategories = ref([])
const childCategories = ref([])
const filterParentId = ref(null)
const filterCategoryId = ref(null)
const dialogVisible = ref(false)
const editingId = ref(null)
const form = ref({ name: '', description: '', image: '', parentId: null, categoryId: null, price: 0, stock: 0, enabled: true })

const filterChildCategories = computed(() => {
  if (!filterParentId.value) return []
  return childCategories.value
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const loadParentCategories = async () => {
  try {
    const res = await categoryApi.getParentCategories()
    parentCategories.value = res.data.data || []
  } catch (e) { console.error('加载父分类失败', e) }
}

const loadChildCategories = async (parentId) => {
  if (!parentId) {
    childCategories.value = []
    return
  }
  try {
    const res = await categoryApi.getChildCategories(parentId)
    childCategories.value = res.data.data || []
  } catch (e) { console.error('加载子分类失败', e) }
}

const handleParentChange = (parentId) => {
  form.value.categoryId = null
  loadChildCategories(parentId)
}

const allCategoriesMap = ref({})

const buildCategoryMap = async () => {
  try {
    const res = await categoryApi.getCategories()
    const cats = res.data.data || []
    cats.forEach(c => { allCategoriesMap.value[c.id] = { name: c.name, parentId: c.parentId } })
  } catch (e) { console.error('加载分类失败', e) }
}

const getCategoryName = (categoryId) => {
  const entry = allCategoriesMap.value[categoryId]
  return entry?.name || '--'
}

const getParentName = (categoryId) => {
  const entry = allCategoriesMap.value[categoryId]
  if (!entry?.parentId) return '--'
  const parent = allCategoriesMap.value[entry.parentId]
  return parent?.name || '--'
}

const handleStatusChange = async (row) => {
  try {
    await productApi.updateProduct(row.id, {
      name: row.name,
      description: row.description,
      image: row.image,
      categoryId: row.categoryId,
      price: row.price,
      stock: row.stock,
      enabled: row.enabled
    })
    ElMessage.success('状态更新成功')
  } catch (e) {
    row.enabled = !row.enabled
    ElMessage.error('状态更新失败')
  }
}

const load = async () => {
  loading.value = true
  try {
    const res = await productApi.getProducts(0, 100, {})
    let allProducts = res.data.data.content || []

    if (filterParentId.value) {
      const children = childCategories.value
      const childIds = children.map(c => c.id)
      allProducts = allProducts.filter(p => childIds.includes(p.categoryId))
    }
    if (filterCategoryId.value) {
      allProducts = allProducts.filter(p => p.categoryId === filterCategoryId.value)
    }

    products.value = allProducts
  } catch (e) { console.error('加载失败', e) }
  finally { loading.value = false }
}

const handleFilterChange = async () => {
  if (filterParentId.value) {
    await loadChildCategories(filterParentId.value)
  } else {
    childCategories.value = []
    filterCategoryId.value = null
  }
  load()
}

const openDialog = async (row) => {
  if (row) {
    editingId.value = row.id
    // Find parent category from the category map
    const cat = allCategoriesMap.value[row.categoryId]
    const parentId = cat?.parentId || null

    form.value = { name: row.name, description: row.description || '', image: row.image || '', parentId, categoryId: row.categoryId || null, price: row.price || 0, stock: row.stock || 0, enabled: row.enabled }
    if (parentId) {
      await loadChildCategories(parentId)
    }
  } else {
    editingId.value = null
    form.value = { name: '', description: '', image: '', parentId: null, categoryId: null, price: 0, stock: 0, enabled: true }
    childCategories.value = []
  }
  dialogVisible.value = true
}

const handleUpload = async (options) => {
  const file = options.file
  const formData = new FormData()
  formData.append('file', file)
  formData.append('folder', 'product')
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

const handleSave = async () => {
  saving.value = true
  try {
    if (editingId.value) {
      await productApi.updateProduct(editingId.value, form.value)
      ElMessage.success('产品更新成功')
    } else {
      await productApi.createProduct(form.value)
      ElMessage.success('产品添加成功')
    }
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error('保存失败')
  }
  finally { saving.value = false }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该产品？', '确认', { type: 'warning' })
  try {
    await productApi.deleteProduct(row.id)
    ElMessage.success('删除成功')
    load()
  } catch (e) { /* 取消删除 */ }
}

onMounted(() => {
  loadParentCategories()
  buildCategoryMap()
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

.filter-bar { margin-bottom: 1rem; }

.upload-wrapper { display: flex; flex-direction: column; gap: 0.5rem; }
.upload-actions { display: flex; }
.image-uploader { width: 100%; }
.image-uploader :deep(.el-upload) {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
  width: 200px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}
.image-uploader :deep(.el-upload:hover) { border-color: #00a859; background: #f6ffed; }
.uploader-icon { font-size: 28px; color: #bfbfbf; }
.upload-text { font-size: 0.8rem; color: #bfbfbf; margin-top: 4px; }
.upload-placeholder { display: flex; flex-direction: column; align-items: center; justify-content: center; }
.uploaded-img { width: 200px; height: 120px; object-fit: cover; display: block; }
.form-hint { font-size: 0.75rem; color: #bfbfbf; }

@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
