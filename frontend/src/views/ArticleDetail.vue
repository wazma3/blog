<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticle } from '../api/article'
import { marked } from 'marked'

const route = useRoute()
const router = useRouter()
const article = ref(null)
const loading = ref(true)

async function fetchArticle() {
  loading.value = true
  try {
    const res = await getArticle(route.params.id)
    article.value = res.data
  } finally {
    loading.value = false
  }
}

function renderMarkdown(content) {
  return marked(content || '')
}

onMounted(fetchArticle)
</script>

<template>
  <div class="article-detail" v-loading="loading">
    <el-card v-if="article" shadow="never">
      <div class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <span><el-icon><User /></el-icon> {{ article.authorName }}</span>
          <span><el-icon><View /></el-icon> {{ article.viewCount }} 阅读</span>
          <span><el-icon><Clock /></el-icon> {{ article.createdAt?.split('T')[0] }}</span>
        </div>
        <div class="article-tags" v-if="article.tags?.length">
          <el-tag v-for="tag in article.tags" :key="tag.id" size="small">{{ tag.name }}</el-tag>
        </div>
      </div>

      <el-divider />

      <div class="markdown-body" v-html="renderMarkdown(article.content)"></div>
    </el-card>

    <el-empty v-if="!loading && !article" description="文章不存在" />
  </div>
</template>

<style scoped>
.article-detail {
  max-width: 900px;
  margin: 0 auto;
}

.article-header {
  text-align: center;
}

.article-title {
  font-size: 28px;
  font-weight: 700;
  color: #222;
  margin-bottom: 16px;
}

.article-meta {
  display: flex;
  justify-content: center;
  gap: 20px;
  font-size: 14px;
  color: #999;
  margin-bottom: 12px;
}

.article-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-tags {
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>
