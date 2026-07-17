<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>最新动态模块管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>模块配置</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">副标题</span>
              <span class="text-value">{{ formData.sectionSubtitle }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">主标题</span>
              <span class="text-value">{{ formData.sectionTitle }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮文字</span>
              <span class="text-value">{{ formData.buttonText }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">按钮链接</span>
              <span class="text-value">{{ getPageLabel(formData.buttonLink) }}</span>
            </div>
          </div>
          <el-form v-else :model="formData" label-width="100px">
            <el-form-item label="副标题">
              <el-input v-model="formData.sectionSubtitle" placeholder="如：最新动态" />
            </el-form-item>
            <el-form-item label="主标题">
              <el-input v-model="formData.sectionTitle" placeholder="如：公司新闻" />
            </el-form-item>
            <el-form-item label="按钮文字">
              <el-input v-model="formData.buttonText" placeholder="如：查看全部文章" />
            </el-form-item>
            <el-form-item label="按钮链接">
              <el-select v-model="formData.buttonLink" placeholder="选择跳转页面" style="width: 100%">
                <el-option v-for="page in pageOptions" :key="page.value" :label="page.label" :value="page.value" />
              </el-select>
            </el-form-item>
            <div class="form-actions">
              <el-button @click="toggleEdit">取消</el-button>
              <el-button type="primary" :loading="saving" @click="saveFormData">保存</el-button>
            </div>
          </el-form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>首页展示新闻（最多3篇）</h3>
            <el-button type="primary" size="small" @click="saveHomeArticles" :loading="savingArticles">保存选择</el-button>
          </div>
          <div class="article-grid">
            <div
              v-for="article in allArticles"
              :key="article.id"
              class="article-chip"
              :class="{ selected: selectedArticleIds.includes(article.id), disabled: !selectedArticleIds.includes(article.id) && selectedArticleIds.length >= 3 }"
              @click="toggleArticle(article)"
            >
              <el-checkbox
                :model-value="selectedArticleIds.includes(article.id)"
                :disabled="!selectedArticleIds.includes(article.id) && selectedArticleIds.length >= 3"
                @change="() => toggleArticle(article)"
                class="chip-checkbox"
              />
              <div class="chip-info">
                <span class="chip-title">{{ article.title }}</span>
                <span class="chip-status" :class="article.published ? 'published' : 'draft'">
                  {{ article.published ? '已发布' : '草稿' }}
                </span>
              </div>
            </div>
            <div v-if="allArticles.length === 0" class="no-articles">
              暂无已发布的文章，请先在"新闻动态 → 文章管理"中添加文章。
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>预览效果</h3>
          </div>
          <div class="preview-container">
            <div class="preview-section">
              <span class="preview-subtitle">{{ formData.sectionSubtitle || '最新动态' }}</span>
              <h2 class="preview-title">{{ formData.sectionTitle || '公司新闻' }}</h2>
              <div class="preview-news-grid">
                <div class="preview-news-card" v-for="article in previewArticles" :key="article.id">
                  <div class="preview-news-image">
                    <img v-if="article.coverImage" :src="article.coverImage" alt="" />
                  </div>
                  <div class="preview-news-content">
                    <h3>{{ article.title }}</h3>
                    <p>{{ article.summary || '暂无摘要' }}</p>
                    <span class="preview-read-more">阅读更多 →</span>
                  </div>
                </div>
                <div class="preview-news-card placeholder" v-for="i in (3 - previewArticles.length)" :key="'p'+i">
                  <div class="preview-news-image"></div>
                  <div class="preview-news-content">
                    <h3>待选择</h3>
                    <p>请从上方列表中选择要展示的文章</p>
                  </div>
                </div>
              </div>
              <button class="preview-btn-outline">{{ formData.buttonText || '查看全部文章' }}</button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref, computed } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'


const saving = ref(false)
const savingArticles = ref(false)
const isEditing = ref(false)
const formData = ref({
  sectionSubtitle: '',
  sectionTitle: '',
  buttonText: '',
  buttonLink: ''
})

const allArticles = ref([])
const selectedArticleIds = ref([])

const pageOptions = [
  { label: '首页', value: '/' },
  { label: '关于我们', value: '/about' },
  { label: '产品服务', value: '/products' },
  { label: '新闻动态', value: '/news' },
  { label: '联系我们', value: '/contact' }
]

const getPageLabel = (link) => {
  const page = pageOptions.find(p => p.value === link)
  return page ? page.label : link || '未设置'
}

const previewArticles = computed(() => {
  return selectedArticleIds.value
    .map(id => allArticles.value.find(a => a.id === id))
    .filter(Boolean)
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const loadData = async () => {
  try {
    const res = await axios.get('/api/site/home-news')
    if (res.data.code === 200) {
      formData.value = res.data.data
    }
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}

const loadArticles = async () => {
  try {
    const res = await axios.get('/articles/public')
    if (res.data.code === 200) {
      allArticles.value = res.data.data.content || []
    }
    const homeRes = await axios.get('/articles/public/home')
    if (homeRes.data.code === 200) {
      selectedArticleIds.value = homeRes.data.data.map(a => a.id)
    }
  } catch (e) {
    console.error('加载文章失败:', e)
  }
}

const toggleArticle = (article) => {
  const idx = selectedArticleIds.value.indexOf(article.id)
  if (idx > -1) {
    selectedArticleIds.value.splice(idx, 1)
  } else {
    if (selectedArticleIds.value.length >= 3) {
      ElMessage.warning('最多只能选择3篇文章')
      return
    }
    selectedArticleIds.value.push(article.id)
  }
}

const saveHomeArticles = async () => {
  savingArticles.value = true
  try {
    const allIds = new Set(allArticles.value.map(a => a.id))
    const selectedSet = new Set(selectedArticleIds.value)

    const updatePromises = allArticles.value.map(article => {
      const shouldShow = selectedSet.has(article.id)
      if (article.showOnHome !== shouldShow) {
        return axios.put(`/articles/${article.id}`, {
          ...article,
          showOnHome: shouldShow
        })
      }
      return Promise.resolve()
    })

    await Promise.all(updatePromises)
    ElMessage.success('保存成功')
    await loadArticles()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  } finally {
    savingArticles.value = false
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    const res = await axios.put('/api/site/home-news', formData.value)
    if (res.data.code === 200) {
      formData.value = res.data.data
      ElMessage.success('保存成功')
      isEditing.value = false
    } else {
      ElMessage.error(res.data.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadData()
  loadArticles()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; }
.content-wrapper { padding: 2rem; display: grid; gap: 1.5rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
.card-header h3 { color: #2c3e50; margin: 0; }
.form-actions { display: flex; justify-content: flex-end; gap: 0.5rem; margin-top: 1rem; }

.text-display { padding: 0; border: 1px solid #e5e7eb; border-radius: 4px; overflow: hidden; }
.text-item { display: flex; align-items: center; border-bottom: 1px solid #e5e7eb; }
.text-item:last-child { border-bottom: none; }
.text-label { width: 100px; padding: 0.75rem 1rem; color: #7f8c8d; font-weight: 600; background: #f8fafc; border-right: 1px solid #e5e7eb; }
.text-value { flex: 1; padding: 0.75rem 1rem; color: #2c3e50; font-size: 1rem; }

.article-grid { display: flex; flex-wrap: wrap; gap: 0.5rem; }
.article-chip {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.4rem 0.75rem;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  background: white;
  font-size: 0.85rem;
}
.article-chip:hover { border-color: #00a859; background: #f0fdf4; }
.article-chip.selected { border-color: #00a859; background: #f0fdf4; }
.article-chip.disabled { opacity: 0.4; cursor: not-allowed; }
.article-chip.disabled:hover { border-color: #e5e7eb; background: white; }
.chip-checkbox { margin: 0; }
.chip-info { display: flex; align-items: center; gap: 0.5rem; }
.chip-title { font-weight: 500; color: #2c3e50; }
.chip-status { padding: 0.1rem 0.4rem; border-radius: 4px; font-size: 0.7rem; }
.chip-status.published { background: #d4edda; color: #155724; }
.chip-status.draft { background: #f8d7da; color: #721c24; }
.no-articles { text-align: center; color: #95a5a6; padding: 1.5rem; font-size: 0.9rem; }

.preview-container { border: 1px solid #e5e7eb; border-radius: 8px; padding: 2rem; background: #fafafa; }
.preview-section { text-align: center; }
.preview-subtitle { display: inline-block; background: rgba(0, 168, 89, 0.1); color: #00a859; padding: 0.3rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600; margin-bottom: 1rem; }
.preview-title { font-size: 1.8rem; color: #1a202c; margin: 0 0 2rem; }
.preview-news-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.5rem; margin-bottom: 2rem; }
.preview-news-card { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.preview-news-card.placeholder { opacity: 0.5; }
.preview-news-image { height: 150px; background: #e2e8f0; }
.preview-news-image img { width: 100%; height: 100%; object-fit: cover; }
.preview-news-content { padding: 1rem; text-align: left; }
.preview-news-content h3 { font-size: 1rem; color: #1a202c; margin: 0 0 0.5rem; }
.preview-news-content p { font-size: 0.85rem; color: #718096; margin: 0 0 0.5rem; }
.preview-read-more { color: #00a859; font-size: 0.85rem; }
.preview-btn-outline { background: white; color: #4a5568; border: 1px solid #e2e8f0; padding: 0.6rem 1.5rem; border-radius: 6px; font-size: 0.9rem; cursor: pointer; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } .preview-news-grid { grid-template-columns: 1fr; } }
</style>
