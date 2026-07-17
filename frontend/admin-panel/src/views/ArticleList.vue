
<template>
  <div class="article-list">
    <AdminSidebar @logout="handleLogout" />
    
    <main class="main-content">
      <header class="top-bar">
        <h1>文章管理</h1>
        <div class="top-bar-actions">
          <button @click="showAddModal = true" class="add-btn">+ 添加文章</button>
          <TopUserInfo />
        </div>
      </header>
      
      <div class="table-container">
        <el-table :data="articles" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="author" label="作者" width="100" />
          <el-table-column prop="categoryId" label="分类ID" width="100" />
          <el-table-column prop="views" label="阅读量" width="100" />
          <el-table-column prop="published" label="状态">
            <template #default="scope">
              <span :class="scope.row.published ? 'status active' : 'status inactive'">
                {{ scope.row.published ? '已发布' : '草稿' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间" />
          <el-table-column label="操作">
            <template #default="scope">
              <button @click="editArticle(scope.row)" class="action-btn edit">编辑</button>
              <button @click="deleteArticle(scope.row.id)" class="action-btn delete">删除</button>
            </template>
          </el-table-column>
        </el-table>
        
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          class="pagination"
        />
      </div>
    </main>
    
    <el-dialog v-model="showAddModal" title="添加文章" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :data="{ folder: 'article' }"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
            accept="image/*"
            name="file"
          >
            <el-button type="primary">点击上传图片</el-button>
          </el-upload>
          <div v-if="form.coverImage" class="image-preview">
            <img :src="form.coverImage" alt="预览" class="preview-img" />
            <button @click="removeImage" class="remove-btn">移除</button>
          </div>
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input v-model.number="form.categoryId" type="number" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
        <el-form-item label="是否发布">
          <el-switch v-model="form.published" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { ref, onMounted, computed } from 'vue'
import { articleApi } from '@/api'

const articles = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)
const showAddModal = ref(false)
const uploadUrl = '/api/upload'
const uploadHeaders = computed(() => {
  const token = sessionStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})
const form = ref({
  title: '',
  author: '',
  summary: '',
  coverImage: '',
  categoryId: null,
  content: '',
  published: false
})

const handleUploadSuccess = (response) => {
  console.log('上传成功:', response)
  if (response && response.data) {
    form.value.coverImage = response.data.url
  } else if (response && response.url) {
    form.value.coverImage = response.url
  }
}

const handleUploadError = (error) => {
  console.error('上传失败:', error)
  if (error.response) {
    alert('上传失败: ' + (error.response.data?.message || error.response.data?.msg || '未知错误'))
  } else {
    alert('上传失败: ' + error.message)
  }
}

const removeImage = () => {
  form.value.coverImage = ''
}

const loadArticles = async () => {
  try {
    const res = await articleApi.getArticles(currentPage.value, pageSize.value)
    articles.value = res.data.data.content || []
    total.value = res.data.data.totalElements || 0
  } catch (error) {
    console.error('获取文章列表失败:', error)
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadArticles()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadArticles()
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/login'
}

const editArticle = (article) => {
  form.value = { ...article }
  showAddModal.value = true
}

const deleteArticle = async (id) => {
  if (!confirm('确定要删除该文章吗？')) return
  try {
    await articleApi.deleteArticle(id)
    alert('删除成功')
    loadArticles()
  } catch (error) {
    alert('删除失败')
  }
}

const submitForm = async () => {
  try {
    if (form.value.id) {
      await articleApi.updateArticle(form.value.id, form.value)
      alert('更新成功')
    } else {
      await articleApi.createArticle(form.value)
      alert('创建成功')
    }
    showAddModal.value = false
    form.value = { title: '', author: '', summary: '', coverImage: '', categoryId: null, content: '', published: false }
    loadArticles()
  } catch (error) {
    alert('操作失败')
  }
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.article-list {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background-color: #2c3e50;
  color: white;
  padding: 1rem;
  display: flex;
  flex-direction: column;
}

.logo {
  padding: 1rem;
  border-bottom: 1px solid #34495e;
  margin-bottom: 1rem;
}

.logo h2 {
  font-size: 1.2rem;
}

.nav ul {
  list-style: none;
}

.nav li {
  margin-bottom: 0.5rem;
}

.nav a {
  color: #bdc3c7;
  display: block;
  padding: 0.8rem 1rem;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.nav a:hover,
.nav li.active a {
  background-color: #3498db;
  color: white;
}

.logout {
  margin-top: auto;
}

.logout button {
  width: 100%;
  background-color: #e74c3c;
  color: white;
  padding: 0.8rem;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.logout button:hover {
  background-color: #c0392b;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1.5rem;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.top-bar h1 {
  color: #2c3e50;
}

.add-btn {
  background-color: #3498db;
  color: white;
  padding: 0.6rem 1.5rem;
  border-radius: 5px;
  font-weight: 500;
}

.table-container {
  background-color: white;
  border-radius: 10px;
  padding: 1.5rem;
}

.status {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
}

.status.active {
  background-color: #d4edda;
  color: #155724;
}

.status.inactive {
  background-color: #f8d7da;
  color: #721c24;
}

.action-btn {
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  margin-right: 0.5rem;
  font-size: 0.8rem;
}

.action-btn.edit {
  background-color: #3498db;
  color: white;
}

.action-btn.delete {
  background-color: #e74c3c;
  color: white;
}

.pagination {
  margin-top: 1rem;
  display: flex;
  justify-content: center;
}

.image-preview {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.preview-img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.remove-btn {
  background-color: #e74c3c;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.8rem;
}

@media (max-width: 768px) {
  .article-list {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-between;
  }
  
  .nav ul {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
  }
}
</style>
