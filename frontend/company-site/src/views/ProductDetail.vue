<template>
  <div class="product-detail">
    <section class="product-header">
      <div class="container">
        <button @click="$router.back()" class="back-btn">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            width="20"
            height="20"
          >
            <path d="M19 12H5M12 19l-7-7 7-7" />
          </svg>
          <span>返回列表</span>
        </button>
      </div>
    </section>

    <section class="product-info" v-if="product">
      <div class="container">
        <div class="product-content">
          <div class="product-image-wrapper">
            <img :src="product.image" alt="产品图片" v-if="product.image" />
            <div class="image-overlay">
              <button class="zoom-btn">
                <svg
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  width="24"
                  height="24"
                >
                  <circle cx="11" cy="11" r="8" />
                  <line x1="21" y1="21" x2="16.65" y2="16.65" />
                  <line x1="11" y1="8" x2="11" y2="14" />
                  <line x1="8" y1="11" x2="14" y2="11" />
                </svg>
              </button>
            </div>
          </div>
          <div class="product-details">
            <div class="product-badges">
              <span class="product-badge" v-if="product.isNew">新品</span>
              <span class="product-badge hot" v-if="product.isHot">热销</span>
            </div>
            <h1>{{ product.name }}</h1>
            <p class="product-category">
              {{ getCategoryName(product.categoryId) }}
            </p>
            <div class="product-price-row">
              <span class="product-price"
                >¥{{ product.price.toLocaleString() }}</span
              >
              <span class="product-original-price" v-if="product.originalPrice"
                >¥{{ product.originalPrice.toLocaleString() }}</span
              >
            </div>
            <p class="product-discount" v-if="product.originalPrice">
              立省 ¥{{
                (product.originalPrice - product.price).toLocaleString()
              }}
            </p>
            <p class="description">{{ product.description }}</p>

            <div class="product-specs">
              <h3>产品规格</h3>
              <div class="spec-grid">
                <div class="spec-item">
                  <span class="spec-label">库存</span>
                  <span class="spec-value">{{ product.stock }} 件</span>
                </div>
                <div class="spec-item">
                  <span class="spec-label">状态</span>
                  <span
                    class="spec-value"
                    :class="product.enabled ? 'available' : 'unavailable'"
                  >
                    {{ product.enabled ? "在售" : "下架" }}
                  </span>
                </div>
              </div>
            </div>

            <div class="product-tags">
              <span
                v-for="tag in product.tags"
                :key="tag"
                class="product-tag"
                >{{ tag }}</span
              >
            </div>

            <div class="product-actions">
              <button class="btn btn-primary" :disabled="!product.enabled">
                立即购买
              </button>
              <button class="btn btn-outline">加入询价单</button>
            </div>
          </div>
        </div>

        <div class="product-tabs">
          <div class="tabs-header">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              :class="['tab-btn', { active: activeTab === tab.id }]"
              @click="activeTab = tab.id"
            >
              {{ tab.name }}
            </button>
          </div>
          <div class="tabs-content">
            <div v-if="activeTab === 'details'" class="tab-pane">
              <div
                v-html="sanitizeHtml(product.details)"
                v-if="product.details"
              ></div>
            </div>
            <div v-else-if="activeTab === 'specs'" class="tab-pane">
              <table class="spec-table">
                <tbody>
                  <tr v-if="product.model">
                    <td>产品型号</td>
                    <td>{{ product.model }}</td>
                  </tr>
                  <tr v-if="product.capacity">
                    <td>额定容量</td>
                    <td>{{ product.capacity }}</td>
                  </tr>
                  <tr v-if="product.voltage">
                    <td>额定电压</td>
                    <td>{{ product.voltage }}</td>
                  </tr>
                  <tr v-if="product.maxCurrent">
                    <td>最大电流</td>
                    <td>{{ product.maxCurrent }}</td>
                  </tr>
                  <tr v-if="product.dimensions">
                    <td>尺寸</td>
                    <td>{{ product.dimensions }}</td>
                  </tr>
                  <tr v-if="product.weight">
                    <td>重量</td>
                    <td>{{ product.weight }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else-if="activeTab === 'service'" class="tab-pane">
              <ul v-if="product.serviceItems?.length" class="service-list">
                <li v-for="item in product.serviceItems" :key="item">
                  {{ item }}
                </li>
              </ul>
              <p v-else class="text-muted">暂无服务保障信息</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <div v-else class="loading-state">
      <svg
        viewBox="0 0 50 50"
        fill="none"
        width="40"
        height="40"
        class="loading-spinner"
      >
        <circle
          cx="25"
          cy="25"
          r="20"
          stroke="var(--primary-color)"
          stroke-width="4"
          fill="none"
          stroke-linecap="round"
        >
          <animateTransform
            attributeName="transform"
            type="rotate"
            from="0 25 25"
            to="360 25 25"
            dur="1s"
            repeatCount="indefinite"
          />
        </circle>
      </svg>
      <p>产品加载中...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { productApi } from "@/api";

const route = useRoute();
const product = ref(null);
const activeTab = ref("details");

// XSS 防护：移除危险标签和事件处理器
const sanitizeHtml = (html) => {
  if (!html) return "";
  return String(html)
    .replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, "")
    .replace(/<iframe\b[^<]*(?:(?!<\/iframe>)<[^<]*)*<\/iframe>/gi, "")
    .replace(/<embed\b[^>]*>/gi, "")
    .replace(/<object\b[^<]*(?:(?!<\/object>)<[^<]*)*<\/object>/gi, "")
    .replace(/\bon\w+\s*=\s*"[^"]*"/gi, "")
    .replace(/\bon\w+\s*=\s*'[^']*'/gi, "")
    .replace(/javascript\s*:/gi, "")
    .replace(/vbscript\s*:/gi, "");
};

const tabs = [
  { id: "details", name: "产品详情" },
  { id: "specs", name: "技术规格" },
  { id: "service", name: "服务保障" },
];

const getCategoryName = (categoryId) => {
  return product.value?.categoryName || "";
};

onMounted(async () => {
  const id = route.params.id;
  try {
    const res = await productApi.getProductById(id);
    product.value = res.data.data;
  } catch (error) {
    console.error("获取产品详情失败:", error);
  }
});
</script>

<style scoped>
.product-detail {
  padding-top: 0;
}

.product-header {
  padding: 24px 0;
  padding-top: 100px;
  background: var(--bg-light);
  border-bottom: 1px solid var(--border-color);
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: var(--text-secondary);
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-btn:hover {
  color: var(--primary-color);
  gap: 12px;
}

.product-info {
  padding: 60px 0;
  background: white;
}

.product-content {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 48px;
  margin-bottom: 48px;
}

.product-image-wrapper {
  position: relative;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

.product-image-wrapper img {
  width: 100%;
  height: 500px;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 16px;
  right: 16px;
}

.zoom-btn {
  width: 44px;
  height: 44px;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--text-secondary);
  transition: all 0.3s ease;
  box-shadow: var(--shadow-md);
}

.zoom-btn:hover {
  background: var(--primary-color);
  color: white;
}

.product-details {
  display: flex;
  flex-direction: column;
}

.product-badges {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.product-badge {
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 600;
  color: white;
  background: linear-gradient(
    135deg,
    var(--primary-color) 0%,
    var(--primary-dark) 100%
  );
  border-radius: 20px;
}

.product-badge.hot {
  background: linear-gradient(135deg, #e53e3e 0%, #c53030 100%);
}

.product-details h1 {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.product-category {
  font-size: 13px;
  color: var(--primary-color);
  font-weight: 500;
  margin-bottom: 20px;
}

.product-price-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.product-price {
  font-size: 2.2rem;
  font-weight: 700;
  color: #e53e3e;
}

.product-original-price {
  font-size: 1.2rem;
  color: var(--text-muted);
  text-decoration: line-through;
}

.product-discount {
  color: #e53e3e;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 20px;
}

.description {
  color: var(--text-secondary);
  line-height: 1.8;
  font-size: 15px;
  margin-bottom: 24px;
}

.product-specs {
  background: var(--bg-light);
  padding: 20px;
  border-radius: var(--radius-md);
  margin-bottom: 20px;
}

.product-specs h3 {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
}

.spec-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid var(--border-color);
}

.spec-label {
  color: var(--text-muted);
  font-size: 14px;
}

.spec-value {
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
}

.spec-value.available {
  color: var(--primary-color);
}

.spec-value.unavailable {
  color: #e53e3e;
}

.product-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}

.product-tag {
  padding: 4px 12px;
  font-size: 12px;
  color: var(--text-secondary);
  background: var(--bg-light);
  border-radius: 4px;
}

.product-actions {
  display: flex;
  gap: 16px;
}

.product-actions .btn {
  flex: 1;
  padding: 14px 24px;
  font-size: 15px;
}

.btn-outline {
  background: transparent;
  color: var(--primary-color);
  border: 2px solid var(--primary-color);
}

.btn-outline:hover {
  background: var(--primary-color);
  color: white;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.product-tabs {
  background: var(--bg-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.tabs-header {
  display: flex;
  border-bottom: 1px solid var(--border-color);
}

.tab-btn {
  flex: 1;
  padding: 16px 24px;
  font-size: 15px;
  font-weight: 500;
  color: var(--text-secondary);
  background: none;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.tab-btn:hover {
  color: var(--primary-color);
}

.tab-btn.active {
  color: var(--primary-color);
  font-weight: 600;
}

.tab-btn.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: var(--primary-color);
}

.tabs-content {
  padding: 32px;
}

.tab-pane {
  color: var(--text-secondary);
  line-height: 1.8;
}

.tab-pane h4 {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
}

.spec-table {
  width: 100%;
  border-collapse: collapse;
}

.spec-table tbody tr {
  border-bottom: 1px solid var(--border-color);
}

.spec-table tbody tr:last-child {
  border-bottom: none;
}

.spec-table td {
  padding: 12px 16px;
}

.spec-table td:first-child {
  color: var(--text-muted);
  width: 30%;
}

.spec-table td:last-child {
  color: var(--text-primary);
}

.service-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.service-list li {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: white;
  border-radius: var(--radius-sm);
}

.service-list li::before {
  content: "✓";
  width: 24px;
  height: 24px;
  background: rgba(0, 168, 89, 0.1);
  color: var(--primary-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120px 0;
}

.loading-spinner {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.loading-state p {
  color: var(--text-muted);
  margin-top: 16px;
}

@media (max-width: 768px) {
  .product-content {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .product-image-wrapper img {
    width: 100%;
    height: 350px !important;
    object-fit: cover;
  }

  .product-details h1 {
    font-size: 1.5rem;
  }

  .product-price {
    font-size: 1.8rem;
  }

  .product-actions {
    flex-direction: column;
  }

  .spec-grid {
    grid-template-columns: 1fr;
  }

  .service-list {
    grid-template-columns: 1fr;
  }

  .product-info {
    padding: 40px 0;
  }
}
</style>