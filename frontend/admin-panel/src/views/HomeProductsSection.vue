<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>热门产品模块管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>模块配置</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <p class="card-desc">设置首页热门产品模块的标题、副标题和按钮文字</p>
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
              <el-input v-model="formData.sectionSubtitle" placeholder="如：热门产品" />
            </el-form-item>
            <el-form-item label="主标题">
              <el-input v-model="formData.sectionTitle" placeholder="如：我们的产品" />
            </el-form-item>
            <el-form-item label="按钮文字">
              <el-input v-model="formData.buttonText" placeholder="如：查看全部产品" />
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
            <h3>首页展示产品（最多6个）</h3>
            <el-button type="primary" size="small" @click="saveHomeProducts" :loading="savingProducts">保存选择</el-button>
          </div>
          <p class="card-desc">选择要在首页热门产品模块展示的产品，最多可选择6个</p>
          <div class="product-grid">
            <div
              v-for="product in allProducts"
              :key="product.id"
              class="product-chip"
              :class="{ selected: selectedProductIds.includes(product.id), disabled: !selectedProductIds.includes(product.id) && selectedProductIds.length >= 6 }"
              @click="toggleProduct(product)"
            >
              <el-checkbox
                :model-value="selectedProductIds.includes(product.id)"
                :disabled="!selectedProductIds.includes(product.id) && selectedProductIds.length >= 6"
                @change="() => toggleProduct(product)"
                class="chip-checkbox"
              />
              <div class="chip-info">
                <span class="chip-title">{{ product.name }}</span>
                <span class="chip-price" v-if="product.price">¥{{ product.price }}</span>
              </div>
            </div>
            <div v-if="allProducts.length === 0" class="no-products">
              暂无已启用的产品，请先在"产品服务 → 产品管理"中添加产品。
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>预览效果</h3>
          </div>
          <div class="preview-container">
            <div class="preview-section">
              <span class="preview-subtitle">{{ formData.sectionSubtitle || '热门产品' }}</span>
              <h2 class="preview-title">{{ formData.sectionTitle || '我们的产品' }}</h2>
              <div class="preview-products-grid">
                <div class="preview-product-card" v-for="product in previewProducts" :key="product.id">
                  <div class="preview-product-image">
                    <img v-if="product.image" :src="product.image" alt="" />
                  </div>
                  <div class="preview-product-info">
                    <h3>{{ product.name }}</h3>
                    <p class="preview-product-price" v-if="product.price">¥{{ product.price }}</p>
                  </div>
                </div>
                <div class="preview-product-card placeholder" v-for="i in (6 - previewProducts.length)" :key="'p'+i">
                  <div class="preview-product-image"></div>
                  <div class="preview-product-info">
                    <h3>待选择</h3>
                    <p class="preview-product-price">请从上方列表中选择要展示的产品</p>
                  </div>
                </div>
              </div>
              <button class="preview-btn-outline">{{ formData.buttonText || '查看全部产品' }}</button>
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
import { productApi, siteApi } from '@/api'
import { ElMessage } from 'element-plus'

const saving = ref(false)
const savingProducts = ref(false)
const isEditing = ref(false)
const formData = ref({
  sectionSubtitle: '',
  sectionTitle: '',
  buttonText: '',
  buttonLink: ''
})

const allProducts = ref([])
const selectedProductIds = ref([])

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

const previewProducts = computed(() => {
  return selectedProductIds.value
    .map(id => allProducts.value.find(p => p.id === id))
    .filter(Boolean)
})

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
    const res = await siteApi.getHomeProductsSection()
    if (res.data.code === 200) {
      formData.value = res.data.data
    }
  } catch (e) {
    console.error('加载数据失败:', e)
    ElMessage.error('加载模块配置失败')
  }
}

const loadProducts = async () => {
  try {
    const res = await productApi.getAllEnabledProducts()
    if (res.data.code === 200) {
      allProducts.value = res.data.data || []
    }
    const homeRes = await productApi.getHomeProducts()
    if (homeRes.data.code === 200) {
      selectedProductIds.value = (homeRes.data.data || []).map(p => p.id)
    }
  } catch (e) {
    console.error('加载产品失败:', e)
    ElMessage.error('加载产品列表失败')
  }
}

const toggleProduct = (product) => {
  const idx = selectedProductIds.value.indexOf(product.id)
  if (idx > -1) {
    selectedProductIds.value.splice(idx, 1)
  } else {
    if (selectedProductIds.value.length >= 6) {
      ElMessage.warning('最多只能选择6个产品')
      return
    }
    selectedProductIds.value.push(product.id)
  }
}

const saveHomeProducts = async () => {
  savingProducts.value = true
  try {
    const selectedSet = new Set(selectedProductIds.value)

    const updatePromises = allProducts.value.map(product => {
      const shouldShow = selectedSet.has(product.id)
      if (product.showOnHome !== shouldShow) {
        return productApi.updateProduct(product.id, {
          showOnHome: shouldShow
        })
      }
      return Promise.resolve()
    })

    await Promise.all(updatePromises)
    ElMessage.success('首页展示产品保存成功')
    await loadProducts()
  } catch (e) {
    console.error('保存产品失败:', e)
    ElMessage.error(e.response?.data?.message || '保存失败，请重试')
  } finally {
    savingProducts.value = false
  }
}

const saveFormData = async () => {
  saving.value = true
  try {
    const res = await siteApi.saveHomeProductsSection(formData.value)
    if (res.data.code === 200) {
      formData.value = res.data.data
      ElMessage.success('模块配置保存成功')
      isEditing.value = false
    } else {
      ElMessage.error(res.data.message || '保存失败')
    }
  } catch (e) {
    console.error('保存配置失败:', e)
    ElMessage.error(e.response?.data?.message || '保存失败，请重试')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadData()
  loadProducts()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; margin: 0; font-size: 1.5rem; }
.content-wrapper { padding: 2rem; display: flex; flex-direction: column; gap: 1.5rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
.card-header h3 { color: #2c3e50; margin: 0; font-size: 1.1rem; }
.card-desc { color: #7f8c8d; font-size: 0.85rem; margin-bottom: 1.5rem; margin-top: 0; }
.form-actions { display: flex; justify-content: flex-end; gap: 0.5rem; margin-top: 1rem; }

.text-display { padding: 0; border: 1px solid #e5e7eb; border-radius: 4px; overflow: hidden; }
.text-item { display: flex; align-items: center; border-bottom: 1px solid #e5e7eb; }
.text-item:last-child { border-bottom: none; }
.text-label { width: 100px; padding: 0.75rem 1rem; color: #7f8c8d; font-weight: 600; background: #f8fafc; border-right: 1px solid #e5e7eb; }
.text-value { flex: 1; padding: 0.75rem 1rem; color: #2c3e50; font-size: 1rem; }

.product-grid { display: flex; flex-wrap: wrap; gap: 0.5rem; }
.product-chip {
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
.product-chip:hover { border-color: #00a859; background: #f0fdf4; }
.product-chip.selected { border-color: #00a859; background: #f0fdf4; }
.product-chip.disabled { opacity: 0.4; cursor: not-allowed; }
.product-chip.disabled:hover { border-color: #e5e7eb; background: white; }
.chip-checkbox { margin: 0; }
.chip-info { display: flex; align-items: center; gap: 0.5rem; }
.chip-title { font-weight: 500; color: #2c3e50; }
.chip-price { color: #e53e3e; font-weight: 600; font-size: 0.8rem; }
.no-products { text-align: center; color: #95a5a6; padding: 1.5rem; font-size: 0.9rem; width: 100%; }

.preview-container { border: 1px solid #e5e7eb; border-radius: 8px; padding: 2rem; background: #fafafa; }
.preview-section { text-align: center; }
.preview-subtitle { display: inline-block; background: rgba(0, 168, 89, 0.1); color: #00a859; padding: 0.3rem 1rem; border-radius: 20px; font-size: 0.85rem; font-weight: 600; margin-bottom: 1rem; }
.preview-title { font-size: 1.8rem; color: #1a202c; margin: 0 0 2rem; }
.preview-products-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.5rem; margin-bottom: 2rem; }
.preview-product-card { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.preview-product-card.placeholder { opacity: 0.5; }
.preview-product-image { height: 180px; background: #e2e8f0; }
.preview-product-image img { width: 100%; height: 100%; object-fit: cover; }
.preview-product-info { padding: 1rem; }
.preview-product-info h3 { font-size: 1rem; color: #1a202c; margin: 0 0 0.5rem; }
.preview-product-price { font-size: 1.2rem; color: #e53e3e; font-weight: 700; margin: 0; }
.preview-btn-outline { background: white; color: #4a5568; border: 1px solid #e2e8f0; padding: 0.6rem 1.5rem; border-radius: 6px; font-size: 0.9rem; cursor: pointer; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } .preview-products-grid { grid-template-columns: 1fr; } }
</style>
