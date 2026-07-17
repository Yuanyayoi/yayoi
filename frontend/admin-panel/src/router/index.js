import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '../api/index.js'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import HomeCarousel from '../views/HomeCarousel.vue'
import HomeFeatures from '../views/HomeFeatures.vue'
import HomeSpotlight from '../views/HomeSpotlight.vue'
import HomeAboutIntro from '../views/HomeAboutIntro.vue'
import HomeNewsSection from '../views/HomeNewsSection.vue'
import HomeProductsSection from '../views/HomeProductsSection.vue'
import HomeCtaSection from '../views/HomeCtaSection.vue'
import HomeHero from '../views/HomeHero.vue'
import AboutHero from '../views/AboutHero.vue'
import AboutValues from '../views/AboutValues.vue'
import AboutTeam from '../views/AboutTeam.vue'
import AboutMilestones from '../views/AboutMilestones.vue'
import AwardList from '../views/AwardList.vue'
import AboutDetail from '../views/AboutDetail.vue'
import NewsHero from '../views/NewsHero.vue'
import NewsCategory from '../views/NewsCategory.vue'
import NewsList from '../views/NewsList.vue'
import ProductList from '../views/ProductList.vue'
import CategoryList from '../views/CategoryList.vue'
import ProductHero from '../views/ProductHero.vue'
import ContactHero from '../views/ContactHero.vue'
import ContactCompanyInfo from '../views/ContactCompanyInfo.vue'
import ContactOnline from '../views/ContactOnline.vue'
import ContactForm from '../views/ContactForm.vue'
import MessageList from '../views/MessageList.vue'
import SettingsFooter from '../views/SettingsFooter.vue'
import SettingsGlobal from '../views/SettingsGlobal.vue'
import UserList from '../views/UserList.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login },
  { 
    path: '/', 
    name: 'Dashboard', 
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/carousel', 
    name: 'HomeCarousel', 
    component: HomeCarousel,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/features', 
    name: 'HomeFeatures', 
    component: HomeFeatures,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/spotlight', 
    name: 'HomeSpotlight', 
    component: HomeSpotlight,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/about-intro', 
    name: 'HomeAboutIntro', 
    component: HomeAboutIntro,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/news-section', 
    name: 'HomeNewsSection', 
    component: HomeNewsSection,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/products-section', 
    name: 'HomeProductsSection', 
    component: HomeProductsSection,
    meta: { requiresAuth: true }
  },
  { 
    path: '/home/cta-section', 
    name: 'HomeCtaSection', 
    component: HomeCtaSection,
    meta: { requiresAuth: true }
  },
  { 
    path: '/about/hero', 
    name: 'AboutHero', 
    component: AboutHero,
    meta: { requiresAuth: true }
  },
  { 
    path: '/about/values', 
    name: 'AboutValues', 
    component: AboutValues,
    meta: { requiresAuth: true }
  },
  { 
    path: '/about/team', 
    name: 'AboutTeam', 
    component: AboutTeam,
    meta: { requiresAuth: true }
  },
  { 
    path: '/about/milestones', 
    name: 'AboutMilestones', 
    component: AboutMilestones,
    meta: { requiresAuth: true }
  },
  { 
    path: '/about/awards', 
    name: 'AwardList', 
    component: AwardList,
    meta: { requiresAuth: true }
  },
  { 
    path: '/about/detail', 
    name: 'AboutDetail', 
    component: AboutDetail,
    meta: { requiresAuth: true }
  },
  { 
    path: '/products', 
    name: 'ProductList', 
    component: ProductList,
    meta: { requiresAuth: true }
  },
  { 
    path: '/categories', 
    name: 'CategoryList', 
    component: CategoryList,
    meta: { requiresAuth: true }
  },
  { 
    path: '/product-hero', 
    name: 'ProductHero', 
    component: ProductHero,
    meta: { requiresAuth: true }
  },
  { 
    path: '/news/hero', 
    name: 'NewsHero', 
    component: NewsHero,
    meta: { requiresAuth: true }
  },
  { 
    path: '/news/categories', 
    name: 'NewsCategory', 
    component: NewsCategory,
    meta: { requiresAuth: true }
  },
  { 
    path: '/news/list', 
    name: 'NewsList', 
    component: NewsList,
    meta: { requiresAuth: true }
  },
  { path: '/news', redirect: '/news/hero' },
  { path: '/articles', redirect: '/news/list' },
  { 
    path: '/contact/hero', 
    name: 'ContactHero', 
    component: ContactHero,
    meta: { requiresAuth: true }
  },
  { 
    path: '/contact/company-info', 
    name: 'ContactCompanyInfo', 
    component: ContactCompanyInfo,
    meta: { requiresAuth: true }
  },
  { 
    path: '/contact/online', 
    name: 'ContactOnline', 
    component: ContactOnline,
    meta: { requiresAuth: true }
  },
  { 
    path: '/contact/form', 
    name: 'ContactForm', 
    component: ContactForm,
    meta: { requiresAuth: true }
  },
  { path: '/contact', redirect: '/contact/hero' },
  { 
    path: '/messages', 
    name: 'MessageList', 
    component: MessageList,
    meta: { requiresAuth: true }
  },
  { 
    path: '/settings/footer', 
    name: 'SettingsFooter', 
    component: SettingsFooter,
    meta: { requiresAuth: true }
  },
  { 
    path: '/settings/global', 
    name: 'SettingsGlobal', 
    component: SettingsGlobal,
    meta: { requiresAuth: true }
  },
  { 
    path: '/users', 
    name: 'UserList', 
    component: UserList,
    meta: { requiresAuth: true, requiresSuperAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory('/admin/'),
  routes
})

const clearAuth = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  sessionStorage.removeItem('lastActivityTime')
}

router.beforeEach(async (to, from, next) => {
  const token = sessionStorage.getItem('token')
  
  if (to.meta.requiresAuth) {
    if (!token) {
      clearAuth()
      next('/login')
      return
    }

    try {
      const res = await api.get('/auth/validate')
      if (res.data?.code === 200 && res.data?.data) {
        const userData = res.data.data
        sessionStorage.setItem('user', JSON.stringify(userData))
        
        if (to.meta.requiresSuperAdmin && userData.role !== 'SUPER_ADMIN') {
          next('/')
          return
        }
      } else {
        clearAuth()
        ElMessage.error('登录已过期，请重新登录')
        next('/login')
        return
      }
    } catch (err) {
      clearAuth()
      if (err.response?.status === 403) {
        ElMessage.error('访问被拒绝，IP不在白名单中')
      } else if (!err.response) {
        ElMessage.error('无法连接到服务器，请检查网络后重新登录')
      } else {
        ElMessage.error('登录已过期，请重新登录')
      }
      next('/login')
      return
    }
  }
  
  if (token && to.path === '/login') {
    next('/')
    return
  }
  
  next()
})

export default router
