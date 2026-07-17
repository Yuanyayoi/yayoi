<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>页脚信息</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper" v-loading="loading">
        <div class="card">
          <div class="card-header">
            <h3>品牌描述</h3>
          </div>
          <p class="card-desc">设置页脚区域显示的公司简介信息</p>
          <el-form :model="form" label-width="120px">
            <el-form-item label="品牌描述">
              <el-input v-model="form.brandDescription" type="textarea" :rows="3" placeholder="请输入页脚品牌描述" />
            </el-form-item>
          </el-form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>版权信息</h3>
          </div>
          <p class="card-desc">设置页脚底部的版权声明和法律链接文字</p>
          <el-form :model="form" label-width="120px">
            <el-form-item label="版权信息">
              <el-input v-model="form.copyright" placeholder="例如：© 2024 四川国驰恒创科技有限公司. 保留所有权利." />
            </el-form-item>
            <el-form-item label="隐私政策">
              <el-input v-model="form.privacyText" placeholder="例如：隐私政策" />
            </el-form-item>
            <el-form-item label="使用条款">
              <el-input v-model="form.termsText" placeholder="例如：使用条款" />
            </el-form-item>
            <el-form-item label="网站地图">
              <el-input v-model="form.sitemapText" placeholder="例如：网站地图" />
            </el-form-item>
          </el-form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>服务项目</h3>
          </div>
          <p class="card-desc">设置页脚"服务项目"栏目显示的链接。可选择预设页面快速设置，或填写自定义链接地址。</p>
          <el-form :model="form" label-width="120px">
            <template v-for="i in 5" :key="i">
              <el-form-item :label="'服务项目 ' + i">
                <div class="service-row">
                  <el-input v-model="form['service' + i]" :placeholder="'链接名称（留空则不显示）'" class="service-name" />
                  <el-select
                    v-model="linkTypes[i - 1]"
                    @change="(val) => handleLinkTypeChange(i, val)"
                    placeholder="选择链接类型"
                    class="service-type"
                    clearable
                  >
                    <el-option label="无链接" value="none" />
                    <el-option label="首页" value="home" />
                    <el-option label="关于我们" value="about" />
                    <el-option label="产品服务" value="products" />
                    <el-option label="新闻动态" value="news" />
                    <el-option label="联系我们" value="contact" />
                    <el-option label="自定义链接" value="custom" />
                  </el-select>
                  <el-input
                    v-if="linkTypes[i - 1] === 'custom'"
                    v-model="form['service' + i + 'Url']"
                    placeholder="链接地址，如 https://example.com"
                    class="service-url"
                  />
                </div>
              </el-form-item>
            </template>
          </el-form>
        </div>

        <div class="card action-card">
          <el-button type="primary" :loading="saving" @click="save" size="large">保存页脚信息</el-button>
          <el-button @click="load" :disabled="loading">重置</el-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref } from 'vue'
import { siteApi } from '@/api'
import { ElMessage } from 'element-plus'

const PRESET_LINKS = {
  none: '',
  home: '/',
  about: '/about',
  products: '/products',
  news: '/news',
  contact: '/contact'
}

const saving = ref(false)
const loading = ref(false)
const form = ref({})
const linkTypes = ref(['none', 'none', 'none', 'none', 'none'])

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const detectLinkType = (url) => {
  if (!url || !url.trim()) return 'none'
  const trimmed = url.trim()
  for (const [key, path] of Object.entries(PRESET_LINKS)) {
    if (key !== 'custom' && path === trimmed) return key
  }
  return 'custom'
}

const handleLinkTypeChange = (index, val) => {
  const i = index
  if (val === 'custom') {
    if (!form.value['service' + i + 'Url'] || Object.values(PRESET_LINKS).includes(form.value['service' + i + 'Url'])) {
      form.value['service' + i + 'Url'] = ''
    }
  } else if (val && val !== 'none') {
    form.value['service' + i + 'Url'] = PRESET_LINKS[val] || ''
  } else {
    form.value['service' + i + 'Url'] = ''
  }
}

const load = async () => {
  loading.value = true
  try {
    const res = await siteApi.getFooter()
    const data = res.data.data || {}
    form.value = {
      brandDescription: data.brandDescription || '',
      copyright: data.copyright || '',
      privacyText: data.privacyText || '',
      termsText: data.termsText || '',
      sitemapText: data.sitemapText || '',
      service1: data.service1 || '',
      service1Url: data.service1Url || '',
      service2: data.service2 || '',
      service2Url: data.service2Url || '',
      service3: data.service3 || '',
      service3Url: data.service3Url || '',
      service4: data.service4 || '',
      service4Url: data.service4Url || '',
      service5: data.service5 || '',
      service5Url: data.service5Url || ''
    }
    for (let i = 1; i <= 5; i++) {
      linkTypes.value[i - 1] = detectLinkType(form.value['service' + i + 'Url'])
    }
  } catch (e) {
    console.error('加载失败', e)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const save = async () => {
  saving.value = true
  try {
    await siteApi.saveFooter(form.value)
    ElMessage.success('页脚信息保存成功')
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
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
.top-bar h1 { color: #2c3e50; margin: 0; font-size: 1.5rem; }
.content-wrapper { padding: 2rem; display: flex; flex-direction: column; gap: 1.5rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
.card-header h3 { color: #2c3e50; margin: 0; font-size: 1.1rem; }
.card-desc { color: #7f8c8d; font-size: 0.85rem; margin-bottom: 1.5rem; margin-top: 0; }
.action-card { display: flex; gap: 0.75rem; justify-content: center; padding: 1.5rem; }
.service-row { display: flex; gap: 0.75rem; width: 100%; }
.service-name { flex: 0 0 200px; }
.service-type { flex: 0 0 150px; }
.service-url { flex: 1; }
@media (max-width: 768px) {
  .admin-page { flex-direction: column; }
  .service-row { flex-direction: column; }
  .service-name, .service-type { flex: none; }
}
</style>
