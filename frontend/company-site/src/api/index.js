
import axios from 'axios'

export const articleApi = {
  getPublishedArticles(page = 0, size = 10, categoryId = null) {
    let url = `/articles/public?page=${page}&size=${size}`
    if (categoryId !== null) {
      url += `&categoryId=${categoryId}`
    }
    return axios.get(url)
  },
  getLatestArticles() {
    return axios.get('/articles/public/latest')
  },
  getArticleById(id) {
    return axios.get(`/articles/public/${id}`)
  }
}

export const newsCategoryApi = {
  getEnabledCategories() {
    return axios.get('/api/news-categories/public')
  }
}

export const productApi = {
  getEnabledProducts(page = 0, size = 10) {
    return axios.get(`/api/products/public?page=${page}&size=${size}`)
  },
  getAllProducts(page = 0, size = 10) {
    return axios.get(`/api/products?page=${page}&size=${size}`)
  },
  getLatestProducts() {
    return axios.get('/api/products/public/latest')
  },
  getProductById(id) {
    return axios.get(`/api/products/public/${id}`)
  }
}

export const categoryApi = {
  getParentCategories() {
    return axios.get('/categories/public')
  },
  getChildCategories(parentId) {
    return axios.get(`/categories/public/${parentId}/children`)
  }
}

export const messageApi = {
  createMessage(data) {
    return axios.post('/messages/public', data)
  }
}

export const siteApi = {
  getAbout() {
    return axios.get('/site/about')
  },
  getContact() {
    return axios.get('/site/contact')
  },
  getModules() {
    return axios.get('/site/modules')
  },
  getFooter() {
    return axios.get('/site/footer')
  },
  getGlobal() {
    return axios.get('/site/global')
  },
  getValues() {
    return axios.get('/site/values')
  },
  getMilestones() {
    return axios.get('/site/milestones')
  },
  getProductHero() {
    return axios.get('/site/product-hero')
  },
  getNewsHero() {
    return axios.get('/site/news-hero')
  },
  getContactHero() {
    return axios.get('/site/contact-hero')
  },
  getContactOnline() {
    return axios.get('/site/contact-online')
  },
  getContactForm() {
    return axios.get('/site/contact-form')
  },
  getHomeHero() {
    return axios.get('/site/home-hero')
  },
  getHomeAboutIntro() {
    return axios.get('/site/home-about-intro')
  },
  getHomeNews() {
    return axios.get('/site/home-news')
  },
  getHomeProducts() {
    return axios.get('/site/home-products')
  },
  getHomeCta() {
    return axios.get('/site/home-cta')
  },
  getFeatures() {
    return axios.get('/site/features')
  }
}

export default axios
