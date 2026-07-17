<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-box">
        <div class="login-header">
          <h2>后台管理系统</h2>
          <p>请登录您的账户</p>
        </div>
        
        <form @submit.prevent="handleLogin" class="login-form">
          <el-form :model="form" ref="formRef" label-width="80px">
            <el-form-item label="用户名" prop="username">
              <el-input
                ref="usernameInputRef"
                v-model="form.username"
                placeholder="请输入用户名"
                @keydown.down.prevent="focusPassword"
                @keydown.enter.prevent="handleLogin"
              />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                ref="passwordInputRef"
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                @keydown.up.prevent="focusUsername"
                @keydown.enter.prevent="handleLogin"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" native-type="submit" @click="handleLogin" class="login-btn">登录</el-button>
            </el-form-item>
          </el-form>
        </form>
        
        
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authApi, resetIdleTimer } from '@/api'

const router = useRouter()
const form = ref({
  username: '',
  password: ''
})

const formRef = ref(null)
const usernameInputRef = ref(null)
const passwordInputRef = ref(null)

const focusUsername = () => {
  usernameInputRef.value?.focus()
}

const focusPassword = () => {
  passwordInputRef.value?.focus()
}

const handleLogin = async () => {
  try {
    const res = await authApi.login(form.value)
    const { token, user } = res.data.data
    sessionStorage.setItem('token', token)
    sessionStorage.setItem('user', JSON.stringify(user))
    resetIdleTimer()
    router.push('/')
  } catch (error) {
    const message = error.response?.data?.message || '登录失败'
    alert(message)
  }
}

onMounted(() => {
  usernameInputRef.value?.focus()
})
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  width: 100%;
  max-width: 400px;
  padding: 20px;
}

.login-box {
  background-color: white;
  border-radius: 10px;
  padding: 2rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-header h2 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.login-header p {
  color: #7f8c8d;
}

.login-form {
  margin-bottom: 1.5rem;
}

.login-btn {
  width: 100%;
}

.login-footer {
  text-align: center;
  color: #bdc3c7;
  font-size: 0.8rem;
}

.login-footer p {
  margin-bottom: 0.3rem;
}
</style>
