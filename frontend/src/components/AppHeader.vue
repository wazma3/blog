<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { computed } from 'vue'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const isHome = computed(() => route.name === 'Home')
const showAdminLink = computed(() => auth.isLoggedIn)
</script>

<template>
  <header class="app-header">
    <div class="header-inner">
      <div class="logo" @click="router.push('/')">
        <span class="logo-icon">✦</span>
        <span class="logo-text">My Blog</span>
      </div>

      <nav class="nav-links">
        <router-link to="/" :class="{ active: isHome }">首页</router-link>
        <router-link to="/admin" v-if="showAdminLink">后台管理</router-link>
      </nav>

      <div class="header-right">
        <template v-if="auth.isLoggedIn">
          <span class="user-name">{{ auth.username }}</span>
          <el-button size="small" @click="auth.logout(); router.push('/')">退出</el-button>
        </template>
        <template v-else>
          <el-button size="small" @click="router.push('/login')">登录</el-button>
          <el-button size="small" type="primary" @click="router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 20px;
  font-weight: 700;
  color: #409eff;
}

.logo-icon {
  font-size: 24px;
}

.nav-links {
  display: flex;
  gap: 24px;
}

.nav-links a {
  color: #666;
  font-size: 15px;
  transition: color 0.2s;
}

.nav-links a:hover,
.nav-links a.active {
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  color: #666;
  font-size: 14px;
}
</style>
