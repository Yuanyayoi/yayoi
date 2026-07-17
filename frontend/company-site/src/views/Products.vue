<template>
  <div class="products">
    <section class="products-hero" :style="heroStyle">
      <div class="hero-overlay"></div>
      <div class="hero-grid"></div>
      <div class="container">
        <div class="hero-content">
          <h1>{{ heroTitle }}</h1>
          <p>{{ heroSubtitle }}</p>
        </div>
      </div>
    </section>

    <section class="products-filter" :style="{ top: filterTop }">
      <div class="container">
        <div class="filter-wrapper">
          <div class="filter-row">
            <span class="filter-label">产品分类</span>
            <div class="filter-tabs">
              <button
                :class="['filter-tab', { active: selectedParentId === null }]"
                @click="selectAll"
              >
                全部
              </button>
              <button
                v-for="parent in parentCategories"
                :key="parent.id"
                :class="['filter-tab', { active: selectedParentId === parent.id }]"
                @click="selectParent(parent.id)"
              >
                {{ parent.name }}
              </button>
            </div>
          </div>
          <div class="filter-row child-row" v-if="selectedParentId !== null && currentChildren.length > 0">
            <span class="filter-label sub-label">子分类</span>
            <div class="filter-chips">
              <button
                :class="['filter-chip', { active: activeCategory === null }]"
                @click="selectChild(null)"
              >
                全部
              </button>
              <button
                v-for="child in currentChildren"
                :key="child.id"
                :class="['filter-chip', { active: activeCategory === child.id }]"
                @click="selectChild(child.id)"
              >
                {{ child.name }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="products-list">
      <div class="container">
        <div class="products-header" v-if="filteredProducts.length > 0">
          <span class="products-count">共 <strong>{{ filteredProducts.length }}</strong> 款产品</span>
        </div>
        <div class="products-grid">
          <div 
            v-for="product in filteredProducts" 
            :key="product.id" 
            class="product-card"
            @click="$router.push(`/products/${product.id}`)"
          >
            <div class="product-image-wrapper">
              <img :src="product.image" alt="产品图片" v-if="product.image" />
              <div class="product-badge" v-if="product.isNew">新品</div>
              <div class="product-badge hot" v-if="product.isHot">热销</div>
              <div class="product-overlay">
                <span>查看详情</span>
              </div>
            </div>
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <p class="product-desc">{{ product.description }}</p>
              <div class="product-price-row">
                <span class="product-price">¥{{ product.price.toLocaleString() }}</span>
                <span class="product-original-price" v-if="product.originalPrice">¥{{ product.originalPrice.toLocaleString() }}</span>
              </div>
              <div class="product-tags">
                <span v-for="tag in product.tags?.slice(0, 3)" :key="tag" class="product-tag">{{ tag }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="filteredProducts.length === 0" class="empty-state">
          <svg viewBox="0 0 100 100" fill="none" width="80" height="80">
            <circle cx="50" cy="50" r="45" stroke="var(--border-color)" stroke-width="2" fill="none"/>
            <path d="M35 50 L50 35 L65 50 L50 65 Z" stroke="var(--text-muted)" stroke-width="2" fill="none"/>
          </svg>
          <p>暂无相关产品</p>
        </div>

        <div class="pagination">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[12, 24, 36]"
            :page-size="pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { productApi, categoryApi } from '@/api'
import { siteCache } from '@/utils/siteCache'

const products = ref([])
const parentCategories = ref([])
const childCategories = ref({})
const heroTitle = ref('')
const heroSubtitle = ref('')
const heroBgImage = ref('')
const bgPosition = ref('center')
const activeCategory = ref(null)
const selectedParentId = ref(null)
const currentPage = ref(0)
const pageSize = ref(12)
const total = ref(0)
const filterTop = ref('60px')
let rafId = null

const heroStyle = computed(() => {
  if (heroBgImage.value) {
    const pos = bgPosition.value || 'center'
    return {
      backgroundImage: `url(${heroBgImage.value})`,
      backgroundPosition: `center ${pos}`
    }
  }
  return {}
})

const updateFilterTop = () => {
  if (rafId) {
    cancelAnimationFrame(rafId)
  }
  rafId = requestAnimationFrame(() => {
    const header = document.querySelector('header.header')
    if (header) {
      filterTop.value = `${header.offsetHeight}px`
    }
    rafId = null
  })
}

const handleScroll = () => {
  updateFilterTop()
}

const currentChildren = computed(() => {
  return childCategories.value[selectedParentId.value] || []
})

const filteredProducts = computed(() => {
  if (selectedParentId.value === null) return products.value
  
  const children = childCategories.value[selectedParentId.value] || []
  const childIds = children.map(c => c.id)

  if (activeCategory.value !== null) {
    return products.value.filter(p => p.categoryId === activeCategory.value)
  }
  
  if (childIds.length > 0) {
    return products.value.filter(p => childIds.includes(p.categoryId))
  }
  
  return []
})

const loadProducts = async () => {
  try {
    const res = await productApi.getEnabledProducts(currentPage.value, pageSize.value)
    products.value = res.data.data.content || []
    total.value = res.data.data.totalElements || 0
  } catch (error) {
    console.error('获取产品列表失败:', error)
  }
}

const loadParentCategories = async () => {
  try {
    parentCategories.value = await siteCache.getParentCategories()
  } catch (e) {
    console.error('获取父分类失败', e)
  }
}

const loadChildCategories = async (parentId) => {
  if (!parentId || childCategories.value[parentId]) return
  try {
    const res = await categoryApi.getChildCategories(parentId)
    childCategories.value[parentId] = res.data.data || []
  } catch (e) {
    console.error('获取子分类失败', e)
    childCategories.value[parentId] = []
  }
}

const selectAll = () => {
  selectedParentId.value = null
  activeCategory.value = null
}

const selectParent = (parentId) => {
  selectedParentId.value = parentId
  activeCategory.value = null
  loadChildCategories(parentId)
}

const selectChild = (childId) => {
  activeCategory.value = childId
}

const loadHero = async () => {
  try {
    const d = await siteCache.getProductHero()
    if (d.heroTitle) heroTitle.value = d.heroTitle
    if (d.heroSubtitle) heroSubtitle.value = d.heroSubtitle
    if (d.heroBgImage) heroBgImage.value = d.heroBgImage
    if (d.bgPosition) bgPosition.value = d.bgPosition
  } catch (e) {
    console.error('获取产品页面文案失败', e)
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  loadProducts()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadProducts()
}

onMounted(() => {
  loadProducts()
  loadParentCategories()
  loadHero()
  updateFilterTop()
  nextTick(() => {
    updateFilterTop()
  })
  window.addEventListener('scroll', handleScroll, { passive: true })
  window.addEventListener('resize', updateFilterTop)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', updateFilterTop)
})
</script>

<style scoped>
.products {
  padding-top: 0;
}

/* ========== Hero Section ========== */
.products-hero {
  position: relative;
  min-height: 600px;
  padding-top: 120px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background-image: linear-gradient(160deg, #0a1628 0%, #132742 25%, #1a3a5c 55%, #0f2440 100%);
  background-size: cover;
  background-position: center;
  overflow: hidden;
}

.products-hero::before {
  content: '';
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse 80% 60% at 25% 40%, rgba(0, 168, 89, 0.12) 0%, transparent 55%),
    radial-gradient(ellipse 60% 50% at 75% 65%, rgba(0, 168, 89, 0.07) 0%, transparent 50%),
    radial-gradient(ellipse 50% 40% at 50% 20%, rgba(0, 168, 89, 0.05) 0%, transparent 45%);
  z-index: 0;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.25);
  z-index: 0;
}

.hero-grid {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(rgba(255, 255, 255, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.02) 1px, transparent 1px);
  background-size: 60px 60px;
  z-index: 1;
  mask-image: radial-gradient(ellipse 70% 60% at 50% 50%, black 30%, transparent 70%);
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  padding: 1rem 0;
}

.hero-content .section-subtitle {
  display: inline-block;
  font-size: 0.75rem;
  letter-spacing: 3px;
  color: #33d17a;
  background: rgba(51, 209, 122, 0.12);
  padding: 0.4rem 1.2rem;
  border-radius: 20px;
  border: 1px solid rgba(51, 209, 122, 0.2);
  margin-bottom: 20px;
}

.hero-content h1 {
  font-size: clamp(2rem, 5vw, 3.2rem);
  font-weight: 800;
  margin-bottom: 14px;
  letter-spacing: -0.02em;
  background: linear-gradient(180deg, #ffffff 0%, #c8d6e5 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-content p {
  font-size: 1.05rem;
  color: rgba(255, 255, 255, 0.7);
  letter-spacing: 0.02em;
}

/* ========== Filter Section ========== */
.products-filter {
  padding: 16px 0;
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid #e8ecf1;
  position: sticky;
  top: 60px;
  z-index: 100;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  transition: box-shadow 0.3s ease;
  margin-top: -1px;
}

.filter-wrapper {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.filter-label {
  font-size: 0.88rem;
  font-weight: 600;
  color: #374151;
  white-space: nowrap;
  min-width: 64px;
}

.sub-label {
  color: #6b7280;
  font-weight: 500;
}

.filter-tabs {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  flex: 1;
}

.filter-tab {
  padding: 7px 22px;
  font-size: 0.88rem;
  font-weight: 500;
  color: #4b5563;
  background: #f3f4f6;
  border: 1px solid transparent;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s ease;
}

.filter-tab:hover {
  color: #00a859;
  background: #ecfdf5;
  border-color: rgba(0, 168, 89, 0.2);
}

.filter-tab.active {
  color: white;
  background: #00a859;
  border-color: #00a859;
  box-shadow: 0 2px 8px rgba(0, 168, 89, 0.25);
}

.child-row {
  padding-left: 16px;
}

.filter-chips {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  flex: 1;
}

.filter-chip {
  padding: 5px 18px;
  font-size: 0.82rem;
  color: #6b7280;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.25s ease;
}

.filter-chip:hover {
  color: #00a859;
  border-color: #00a859;
  background: #ecfdf5;
}

.filter-chip.active {
  color: #00a859;
  background: #ecfdf5;
  border-color: #00a859;
  font-weight: 600;
}

/* ========== Products Grid ========== */
.products-list {
  padding: 48px 0 80px;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
  min-height: 60vh;
}

.products-header {
  margin-bottom: 24px;
}

.products-count {
  font-size: 0.9rem;
  color: #718096;
}

.products-count strong {
  color: #00a859;
  font-weight: 700;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 28px;
  margin-bottom: 56px;
}

/* ========== Product Card ========== */
.product-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.45s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04), 0 4px 12px rgba(0, 0, 0, 0.02);
  position: relative;
}

.product-card::after {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 16px;
  box-shadow: 0 0 0 0 rgba(0, 168, 89, 0);
  transition: box-shadow 0.45s ease;
  pointer-events: none;
  z-index: 0;
}

.product-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 48px rgba(0, 0, 0, 0.1), 0 8px 16px rgba(0, 0, 0, 0.04);
  border-color: rgba(0, 168, 89, 0.18);
}

.product-card:hover::after {
  box-shadow: 0 0 0 3px rgba(0, 168, 89, 0.08);
}

.product-image-wrapper {
  position: relative;
  height: 220px;
  overflow: hidden;
  background: #f1f5f9;
}

.product-image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.7s cubic-bezier(0.4, 0, 0.2, 1);
}

.product-card:hover .product-image-wrapper img {
  transform: scale(1.1);
}

.product-badge {
  position: absolute;
  top: 14px;
  left: 14px;
  padding: 5px 14px;
  font-size: 0.72rem;
  font-weight: 700;
  color: white;
  background: linear-gradient(135deg, #00a859 0%, #008f4d 100%);
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0, 168, 89, 0.3);
  letter-spacing: 0.03em;
  z-index: 2;
}

.product-badge.hot {
  background: linear-gradient(135deg, #e53e3e 0%, #c53030 100%);
  left: auto;
  right: 14px;
  box-shadow: 0 2px 8px rgba(229, 62, 62, 0.35);
}

.product-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, rgba(0, 168, 89, 0.92), rgba(0, 120, 60, 0.92));
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.4s ease;
  color: white;
  font-weight: 700;
  font-size: 0.95rem;
  letter-spacing: 0.04em;
  z-index: 1;
}

.product-overlay span {
  padding: 10px 28px;
  border: 2px solid rgba(255, 255, 255, 0.7);
  border-radius: 50px;
  backdrop-filter: blur(4px);
  transition: all 0.3s ease;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.product-card:hover .product-overlay span {
  border-color: white;
  box-shadow: 0 0 24px rgba(255, 255, 255, 0.2);
}

/* ========== Product Info ========== */
.product-info {
  padding: 22px 24px 24px;
  position: relative;
  z-index: 1;
}

.product-category {
  font-size: 0.72rem;
  color: #00a859;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin-bottom: 10px;
}

.product-info h3 {
  font-size: 1.15rem;
  font-weight: 700;
  color: #1a365d;
  margin-bottom: 8px;
  line-height: 1.35;
  transition: color 0.3s ease;
}

.product-card:hover .product-info h3 {
  color: #00a859;
}

.product-desc {
  color: #718096;
  font-size: 0.84rem;
  line-height: 1.65;
  margin-bottom: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-price-row {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-bottom: 14px;
}

.product-price {
  font-size: 1.45rem;
  font-weight: 800;
  color: #e53e3e;
  letter-spacing: -0.02em;
}

.product-original-price {
  font-size: 0.95rem;
  color: #a0aec0;
  text-decoration: line-through;
  font-weight: 500;
}

.product-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.product-tag {
  padding: 4px 12px;
  font-size: 0.72rem;
  font-weight: 500;
  color: #64748b;
  background: #f8fafc;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
  transition: all 0.25s ease;
}

.product-tag:hover {
  color: #00a859;
  border-color: rgba(0, 168, 89, 0.2);
  background: #f0fdf4;
}

/* ========== Empty State ========== */
.empty-state {
  text-align: center;
  padding: 80px 0;
}

.empty-state svg {
  margin-bottom: 20px;
  opacity: 0.6;
}

.empty-state p {
  color: #94a3b8;
  font-size: 1rem;
  font-weight: 500;
}

/* ========== Pagination ========== */
.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* ========== Responsive ========== */
@media (max-width: 768px) {
  .products-hero {
    min-height: 400px;
  }

  .products-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .filter-tabs {
    justify-content: flex-start;
  }

  .filter-tab {
    padding: 8px 20px;
    font-size: 0.82rem;
  }

  .products-list {
    padding: 40px 0 60px;
  }

  .hero-content h1 {
    font-size: 1.8rem;
  }

  .hero-content .section-subtitle {
    font-size: 0.7rem;
    padding: 0.3rem 1rem;
  }
}
</style>