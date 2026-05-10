<script setup>
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const menuItems = [
  { path: '/admin/articles', label: '文章管理', icon: 'Document' },
  { path: '/admin/tags', label: '标签管理', icon: 'PriceTag' },
]

function isActive(path) {
  return route.path === path
}
</script>

<template>
  <div class="admin-layout">
    <el-card class="admin-sidebar" shadow="never">
      <el-menu
        :default-active="route.path"
        @select="(index) => router.push(index)"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
    </el-card>

    <div class="admin-content">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
.admin-layout {
  display: flex;
  gap: 20px;
  min-height: calc(100vh - 108px);
}

.admin-sidebar {
  width: 220px;
  flex-shrink: 0;
}

.admin-content {
  flex: 1;
  min-width: 0;
}
</style>
