<script setup>
import AppHeader from './components/AppHeader.vue'
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<template>
  <div id="blog-app">
    <AppHeader />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style>
#blog-app {
  min-height: 100vh;
  background: #f5f5f5;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}
</style>
