<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { login } from '../api/auth'

const router = useRouter()
const auth = useAuthStore()

const form = ref({ username: '', password: '' })
const loading = ref(false)

async function handleLogin() {
  loading.value = true
  try {
    const res = await login(form.value)
    auth.setAuth(res.data.token, res.data.user)
    ElMessage.success('登录成功')
    router.push('/admin')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <el-card class="auth-card" shadow="always">
      <h2 class="auth-title">登录</h2>
      <el-form :model="form" label-width="0" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" style="width: 100%" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.auth-page {
  display: flex;
  justify-content: center;
  padding-top: 80px;
}

.auth-card {
  width: 400px;
}

.auth-title {
  text-align: center;
  margin-bottom: 24px;
  font-size: 24px;
  color: #333;
}

.auth-footer {
  text-align: center;
  font-size: 14px;
  color: #999;
}

.auth-footer a {
  color: #409eff;
}
</style>
