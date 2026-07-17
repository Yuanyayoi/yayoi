
import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: window.location.origin,
  timeout: 30000
})

let isRedirecting = false

export const forceLogout = (message = '登录已过期，请重新登录') => {
  if (isRedirecting) return
  isRedirecting = true
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  sessionStorage.removeItem('lastActivityTime')
  ElMessage.error(message)
  setTimeout(() => {
    window.location.href = '/admin/login'
    isRedirecting = false
  }, 500)
}

api.interceptors.request.use(config => {
  const token = sessionStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  sessionStorage.setItem('lastActivityTime', Date.now().toString())
  return config
})

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      forceLogout('登录已过期，请重新登录')
    }
    else if (error.response?.status === 403) {
      const msg = error.response?.data?.message
      if (msg && msg.includes('whitelist')) {
        forceLogout('访问被拒绝：IP不在白名单中')
      } else {
        ElMessage.error('权限不足: ' + (msg || '您没有权限执行此操作'))
      }
    }
    else if (error.response?.status === 500) {
      console.error('服务器错误:', error.response?.data?.message || error.message)
    }
    else if (!error.response) {
      console.error('服务器无响应:', error.message)
    }
    return Promise.reject(error)
  }
)

export const IDLE_TIMEOUT = 30 * 60 * 1000

export const checkIdleTimeout = () => {
  const token = sessionStorage.getItem('token')
  if (!token) return false
  const lastActivity = sessionStorage.getItem('lastActivityTime')
  if (lastActivity) {
    const idle = Date.now() - parseInt(lastActivity)
    if (idle > IDLE_TIMEOUT) {
      forceLogout('由于长时间未操作，已自动退出登录')
      return true
    }
  }
  return false
}

export const resetIdleTimer = () => {
  if (sessionStorage.getItem('token')) {
    sessionStorage.setItem('lastActivityTime', Date.now().toString())
  }
}

export const authApi = {
  login(data) {
    return api.post('/auth/login', data)
  },
  getProfile() {
    return api.get('/auth/profile')
  },
  updateProfile(data) {
    return api.put('/auth/profile', data)
  },
  changePassword(data) {
    return api.put('/auth/change-password', data)
  }
}

export const userApi = {
  getUsers(page = 0, size = 10) {
    return api.get(`/api/admin/users?page=${page}&size=${size}`)
  },
  getUserById(id) {
    return api.get(`/api/admin/users/${id}`)
  },
  createUser(data) {
    return api.post('/api/admin/users', data)
  },
  updateUser(id, data) {
    return api.put(`/api/admin/users/${id}`, data)
  },
  deleteUser(id) {
    return api.delete(`/api/admin/users/${id}`)
  }
}

export const articleApi = {
  getArticles(page = 0, size = 10, params = {}) {
    const query = new URLSearchParams({ page, size, ...params })
    return api.get(`/articles?${query}`)
  },
  getArticleById(id) {
    return api.get(`/articles/${id}`)
  },
  createArticle(data) {
    return api.post('/articles', data)
  },
  updateArticle(id, data) {
    return api.put(`/articles/${id}`, data)
  },
  deleteArticle(id) {
    return api.delete(`/articles/${id}`)
  }
}

export const newsCategoryApi = {
  getCategories() {
    return api.get('/api/news-categories')
  },
  getCategoryById(id) {
    return api.get(`/api/news-categories/${id}`)
  },
  createCategory(data) {
    return api.post('/api/news-categories', data)
  },
  updateCategory(id, data) {
    return api.put(`/api/news-categories/${id}`, data)
  },
  deleteCategory(id) {
    return api.delete(`/api/news-categories/${id}`)
  }
}

export const productApi = {
  getProducts(page = 0, size = 10, params = {}) {
    const query = new URLSearchParams({ page, size, ...params })
    return api.get(`/api/products?${query}`)
  },
  getAllEnabledProducts() {
    return api.get('/api/products/public/all')
  },
  getHomeProducts() {
    return api.get('/api/products/public/home')
  },
  getProductById(id) {
    return api.get(`/api/products/${id}`)
  },
  createProduct(data) {
    return api.post('/api/products', data)
  },
  updateProduct(id, data) {
    return api.put(`/api/products/${id}`, data)
  },
  deleteProduct(id) {
    return api.delete(`/api/products/${id}`)
  }
}

export const categoryApi = {
  getCategories() {
    return api.get('/categories')
  },
  getParentCategories() {
    return api.get('/categories/public')
  },
  getChildCategories(parentId) {
    return api.get(`/categories/public/${parentId}/children`)
  },
  getCategoryById(id) {
    return api.get(`/categories/${id}`)
  },
  createCategory(data) {
    return api.post('/categories', data)
  },
  updateCategory(id, data) {
    return api.put(`/categories/${id}`, data)
  },
  deleteCategory(id) {
    return api.delete(`/categories/${id}`)
  }
}

export const siteApi = {
  getAbout() {
    return api.get('/api/site/about')
  },
  saveAbout(data) {
    return api.put('/api/site/about', data)
  },
  getContact() {
    return api.get('/api/site/contact')
  },
  saveContact(data) {
    return api.put('/api/site/contact', data)
  },
  getFooter() {
    return api.get('/api/site/footer')
  },
  saveFooter(data) {
    return api.put('/api/site/footer', data)
  },
  getGlobal() {
    return api.get('/api/site/global')
  },
  saveGlobal(data) {
    return api.put('/api/site/global', data)
  },
  getValues() {
    return api.get('/api/site/values')
  },
  saveValues(data) {
    return api.put('/api/site/values', data)
  },
  getMilestones() {
    return api.get('/api/site/milestones')
  },
  saveMilestones(data) {
    return api.put('/api/site/milestones', data)
  },
  getTeamMembers() {
    return api.get('/api/site/team')
  },
  createTeamMember(data) {
    return api.post('/api/site/team', data)
  },
  updateTeamMember(id, data) {
    return api.put(`/api/site/team/${id}`, data)
  },
  deleteTeamMember(id) {
    return api.delete(`/api/site/team/${id}`)
  },
  getModules() {
    return api.get('/api/site/modules')
  },
  saveModules(data) {
    return api.put('/api/site/modules', data)
  },
  getProductHero() {
    return api.get('/api/site/product-hero')
  },
  saveProductHero(data) {
    return api.put('/api/site/product-hero', data)
  },
  getNewsHero() {
    return api.get('/api/site/news-hero')
  },
  saveNewsHero(data) {
    return api.put('/api/site/news-hero', data)
  },
  getContactHero() {
    return api.get('/api/site/contact-hero')
  },
  saveContactHero(data) {
    return api.put('/api/site/contact-hero', data)
  },
  getContactOnline() {
    return api.get('/api/site/contact-online')
  },
  saveContactOnline(data) {
    return api.put('/api/site/contact-online', data)
  },
  getContactForm() {
    return api.get('/api/site/contact-form')
  },
  saveContactForm(data) {
    return api.put('/api/site/contact-form', data)
  },
  getHomeProductsSection() {
    return api.get('/api/site/home-products')
  },
  saveHomeProductsSection(data) {
    return api.put('/api/site/home-products', data)
  }
}

export const messageApi = {
  getMessages(params) {
    return api.get('/api/messages', { params })
  },
  deleteMessage(id) {
    return api.delete(`/api/messages/${id}`)
  },
  markAsReplied(id) {
    return api.put(`/api/messages/${id}/reply`)
  }
}

export const uploadApi = {
  uploadImage(file, folder = '') {
    const formData = new FormData()
    formData.append('file', file)
    if (folder) {
      formData.append('folder', folder)
    }
    return api.post('/api/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 60000
    })
  }
}

export default api
