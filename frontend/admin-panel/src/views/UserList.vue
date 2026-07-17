<template>
  <div class="admin-layout">
    <AdminSidebar @logout="handleLogout" />
    
    <main class="main-content">
      <header class="top-bar">
        <h1>用户管理</h1>
        <div class="top-bar-actions">
          <button @click="openAddModal" class="add-btn">+ 添加用户</button>
          <TopUserInfo />
        </div>
      </header>
      
      <div class="table-container">
        <el-table :data="users" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="phone" label="电话" />
          <el-table-column prop="role" label="角色" width="140">
            <template #default="scope">
              <el-tag :type="scope.row.role === 'SUPER_ADMIN' ? 'danger' : 'primary'" size="small">
                {{ roleLabel(scope.row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="enabled" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.enabled ? 'success' : 'info'" size="small">
                {{ scope.row.enabled ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <el-button size="small" type="primary" @click="editUser(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="confirmDelete(scope.row)" :disabled="scope.row.username === 'admin'">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage + 1"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          class="pagination"
        />
      </div>
    </main>
    
    <el-dialog v-model="showModal" :title="form.id ? '编辑用户' : '添加用户'" width="480px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!form.id" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="密码" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="留空将自动生成" show-password />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="超级管理员" value="SUPER_ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.enabled" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showModal = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { userApi } from '@/api'

const users = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)
const showModal = ref(false)

const resetForm = () => ({
  id: null,
  username: '',
  email: '',
  phone: '',
  password: '',
  role: 'ADMIN',
  enabled: true
})

const form = reactive(resetForm())

const roleLabel = (role) => {
  const map = { SUPER_ADMIN: '超级管理员', ADMIN: '管理员', USER: '普通用户' }
  return map[role] || role
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return dateStr.replace('T', ' ').substring(0, 19)
}

const loadUsers = async () => {
  try {
    const res = await userApi.getUsers(currentPage.value, pageSize.value)
    users.value = res.data.data.content || []
    total.value = res.data.data.totalElements || 0
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 0
  loadUsers()
}

const handleCurrentChange = (page) => {
  currentPage.value = page - 1
  loadUsers()
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const openAddModal = () => {
  Object.assign(form, resetForm())
  showModal.value = true
}

const editUser = (user) => {
  Object.assign(form, {
    id: user.id,
    username: user.username,
    email: user.email,
    phone: user.phone,
    password: '',
    role: user.role || 'ADMIN',
    enabled: user.enabled !== false
  })
  showModal.value = true
}

const confirmDelete = async (user) => {
  if (user.username === 'admin') {
    ElMessage.warning('不能删除默认超级管理员账户')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除用户「${user.username}」吗？`, '确认删除', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
    await userApi.deleteUser(user.id)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const submitForm = async () => {
  if (!form.username.trim()) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!form.email.trim()) {
    ElMessage.warning('请输入邮箱')
    return
  }
  try {
    const payload = {
      username: form.username,
      email: form.email,
      phone: form.phone,
      role: form.role,
      enabled: form.enabled
    }
    if (form.id) {
      await userApi.updateUser(form.id, payload)
      ElMessage.success('更新成功')
    } else {
      if (form.password) payload.password = form.password
      await userApi.createUser(payload)
      ElMessage.success('创建成功')
    }
    showModal.value = false
    loadUsers()
  } catch (error) {
    const msg = error.response?.data?.message || '操作失败'
    ElMessage.error(msg)
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  overflow-x: auto;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.top-bar h1 {
  font-size: 22px;
  color: #303133;
  margin: 0;
}

.add-btn {
  background: #409eff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
}

.add-btn:hover {
  background: #66b1ff;
}

.table-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
