<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>新闻列表</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>文章管理</h3>
            <el-button type="primary" @click="handleAdd">+ 添加文章</el-button>
          </div>

          <div class="filter-bar">
            <el-select v-model="filterCategoryId" placeholder="按分类筛选" clearable style="width: 200px" @change="handleFilterChange">
              <el-option label="全部分类" :value="null" />
              <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </div>

          <el-table :data="articles" border style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="标题" min-width="200" />
            <el-table-column prop="author" label="作者" width="120" />
            <el-table-column label="分类" width="120">
              <template #default="{ row }">
                <span>{{ getCategoryName(row.categoryId) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="views" label="阅读量" width="100" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.published ? 'success' : 'warning'">
                  {{ row.published ? '已发布' : '草稿' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage + 1"
            :page-sizes="[10, 20, 30]"
            :page-size="pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next"
            class="pagination"
          />
        </div>
      </div>

      <el-dialog v-model="dialogVisible" :title="editingId ? '编辑文章' : '添加文章'" width="800px">
        <el-form :model="formData" label-width="100px">
          <el-form-item label="标题">
            <el-input v-model="formData.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="formData.author" placeholder="请输入作者" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
              <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="摘要">
            <el-input v-model="formData.summary" type="textarea" :rows="3" placeholder="请输入摘要" />
          </el-form-item>
          <el-form-item label="封面图片">
            <div class="upload-wrapper">
              <el-upload
                class="image-uploader"
                :show-file-list="false"
                :http-request="handleUpload"
                accept="image/*"
              >
                <img v-if="formData.coverImage" :src="formData.coverImage" class="uploaded-img" />
                <div v-else class="upload-placeholder">
                  <el-icon class="uploader-icon"><Plus /></el-icon>
                  <span class="upload-text">点击上传</span>
                </div>
              </el-upload>
              <el-button v-if="formData.coverImage" type="danger" size="small" @click="removeImage">移除图片</el-button>
            </div>
          </el-form-item>
          <el-form-item label="内容">
            <el-input v-model="formData.content" type="textarea" :rows="10" placeholder="请输入文章内容" />
          </el-form-item>
          <el-form-item label="发布状态">
            <el-switch v-model="formData.published" active-text="发布" inactive-text="草稿" />
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
import { articleApi, newsCategoryApi } from '@/api'
import api from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'


const saving = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const articles = ref([])
const categories = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)
const filterCategoryId = ref(null)
const formData = ref({
  title: '',
  author: '',
  categoryId: null,
  summary: '',
  coverImage: '',
  content: '',
  published: false
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const loadData = async () => {
  try {
    const params = filterCategoryId.value ? { categoryId: filterCategoryId.value } : {}
    const res = await articleApi.getArticles(currentPage.value, pageSize.value, params)
    articles.value = res.data.data.content || []
    total.value = res.data.data.totalElements || 0
  } catch (e) {
    console.error('加载文章失败', e)
  }
}

const loadCategories = async () => {
  try {
    const res = await newsCategoryApi.getCategories()
    categories.value = res.data.data || []
  } catch (e) {
    console.error('加载分类失败', e)
  }
}

const getCategoryName = (categoryId) => {
  const cat = categories.value.find(c => c.id === categoryId)
  return cat ? cat.name : '--'
}

const handleAdd = () => {
  editingId.value = null
  formData.value = {
    title: '',
    author: '',
    categoryId: null,
    summary: '',
    coverImage: '',
    content: '',
    published: false
  }
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
      await articleApi.updateArticle(editingId.value, formData.value)
      ElMessage.success('更新成功')
    } else {
      await articleApi.createArticle(formData.value)
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
  await ElMessageBox.confirm('确定要删除该文章吗？', '提示', {
    type: 'warning'
  })
  try {
    await articleApi.deleteArticle(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

const handleUpload = async (options) => {
  const file = options.file
  const formData = new FormData()
  formData.append('file', file)
  formData.append('folder', 'article')
  try {
    const res = await api.post('/api/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.code === 200) {
      formData.value.coverImage = res.data.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

const removeImage = () => {
  formData.value.coverImage = ''
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadData()
}

const handleCurrentChange = (page) => {
  currentPage.value = page - 1
  loadData()
}

const handleFilterChange = () => {
  currentPage.value = 0
  loadData()
}

onMounted(() => {
  loadCategories()
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
.filter-bar { margin-bottom: 1rem; }
.pagination { margin-top: 1rem; display: flex; justify-content: center; }

.upload-wrapper { display: flex; flex-direction: column; gap: 0.5rem; }
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

@media (max-width: 768px) { .admin-page { flex-direction: column; } }
</style>
