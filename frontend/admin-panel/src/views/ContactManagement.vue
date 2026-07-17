<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />

    <main class="main-content">
      <header class="top-bar">
        <h1>联系我们</h1>
        <TopUserInfo />
      </header>

      <div class="content-wrapper">
        <section class="card">
          <h3>联系信息设置</h3>
          <el-form :model="contactForm" label-width="120px">
            <el-form-item label="公司地址">
              <el-input v-model="contactForm.address" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="contactForm.phone" />
            </el-form-item>
            <el-form-item label="邮箱地址">
              <el-input v-model="contactForm.email" />
            </el-form-item>
            <el-form-item label="营业时间">
              <el-input v-model="contactForm.businessHours" />
            </el-form-item>
            <el-form-item label="说明">
              <el-input v-model="contactForm.description" type="textarea" :rows="4" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="savingContact" @click="saveContact">保存联系信息</el-button>
            </el-form-item>
          </el-form>
        </section>

        <section class="card">
          <div class="card-header">
            <h3>留言管理</h3>
            <span class="message-count">共 {{ messages.length }} 条留言</span>
          </div>
          <el-table :data="messages" border v-loading="loadingMessages" style="width: 100%">
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="phone" label="电话" width="140" />
            <el-table-column prop="email" label="邮箱" width="180" />
            <el-table-column prop="message" label="留言内容" min-width="300">
              <template #default="scope">
                <div class="message-text">{{ scope.row.message }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="提交时间" width="180">
              <template #default="scope">
                {{ formatTime(scope.row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.replied ? 'success' : 'warning'" size="small">
                  {{ scope.row.replied ? '已回复' : '未回复' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
              <template #default="scope">
                <el-button size="small" type="primary" link @click="viewMessage(scope.row)">查看</el-button>
                <el-button 
                  size="small" 
                  type="success" 
                  link 
                  :disabled="scope.row.replied"
                  @click="markAsReplied(scope.row.id)"
                >
                  {{ scope.row.replied ? '已回复' : '标记回复' }}
                </el-button>
                <el-button size="small" type="danger" link @click="deleteMessage(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <p v-if="!loadingMessages && messages.length === 0" style="text-align:center;color:#999;padding:2rem">暂无留言</p>
        </section>
      </div>

      <el-dialog v-model="dialogVisible" title="留言详情" width="600px">
        <div class="message-detail" v-if="currentMessage">
          <p><strong>姓名：</strong>{{ currentMessage.name }}</p>
          <p><strong>电话：</strong>{{ currentMessage.phone }}</p>
          <p><strong>邮箱：</strong>{{ currentMessage.email || '未填写' }}</p>
          <p><strong>提交时间：</strong>{{ formatTime(currentMessage.createdAt) }}</p>
          <p><strong>状态：</strong>
            <el-tag :type="currentMessage.replied ? 'success' : 'warning'" size="small">
              {{ currentMessage.replied ? '已回复' : '未回复' }}
            </el-tag>
          </p>
          <div class="detail-divider"></div>
          <p><strong>留言内容：</strong></p>
          <div class="detail-message">{{ currentMessage.message }}</div>
        </div>
        <template #footer>
          <el-button @click="dialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref } from 'vue'
import { siteApi, messageApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'


const savingContact = ref(false)
const loadingMessages = ref(false)
const messages = ref([])
const dialogVisible = ref(false)
const currentMessage = ref(null)

const contactForm = ref({
  address: '',
  phone: '',
  email: '',
  businessHours: '',
  description: ''
})

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const loadContact = async () => {
  const response = await siteApi.getContact()
  contactForm.value = {
    address: response.data.data?.address || '',
    phone: response.data.data?.phone || '',
    email: response.data.data?.email || '',
    businessHours: response.data.data?.businessHours || '',
    description: response.data.data?.description || ''
  }
}

const saveContact = async () => {
  savingContact.value = true
  try {
    await siteApi.saveContact(contactForm.value)
    ElMessage.success('联系信息保存成功')
  } catch (error) {
    ElMessage.error('联系信息保存失败')
  } finally {
    savingContact.value = false
  }
}

const loadMessages = async () => {
  loadingMessages.value = true
  try {
    const res = await messageApi.getMessages()
    if (res.data.code === 200) {
      messages.value = res.data.data || []
    }
  } catch (e) {
    console.error('加载留言失败:', e)
  } finally {
    loadingMessages.value = false
  }
}

const viewMessage = (message) => {
  currentMessage.value = message
  dialogVisible.value = true
}

const markAsReplied = async (id) => {
  try {
    const res = await messageApi.markAsReplied(id)
    if (res.data.code === 200) {
      ElMessage.success('已标记为已回复')
      await loadMessages()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const deleteMessage = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除此留言吗？', '确认', { type: 'warning' })
    const res = await messageApi.deleteMessage(id)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      await loadMessages()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel' && e !== 'close') {
      const msg = e?.response?.data?.message || e?.message || '删除失败'
      ElMessage.error(msg)
    }
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

onMounted(() => {
  loadContact()
  loadMessages()
})
</script>

<style scoped>
.admin-page {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  background-color: #f5f7fa;
}

.top-bar {
  background-color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.top-bar h1 {
  color: #2c3e50;
}

.content-wrapper {
  padding: 2rem;
  display: grid;
  gap: 2rem;
}

.card {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
}

.card h3 {
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.card-header h3 {
  margin-bottom: 0;
}

.message-count {
  font-size: 0.85rem;
  color: #7f8c8d;
}

.message-text {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.message-detail p {
  margin-bottom: 0.8rem;
  line-height: 1.6;
}

.detail-divider {
  height: 1px;
  background: #e5e7eb;
  margin: 1rem 0;
}

.detail-message {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.7;
  color: #374151;
  background: #f9fafb;
  padding: 1rem;
  border-radius: 6px;
  margin-top: 0.5rem;
}

@media (max-width: 768px) {
  .admin-page {
    flex-direction: column;
  }
}
</style>