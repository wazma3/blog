<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getArticles } from '../api/article'
import { getTags } from '../api/tag'

const router = useRouter()
const articles = ref([])
const tags = ref([])
const total = ref(0)
const loading = ref(false)
const currentTag = ref('')
const keyword = ref('')

const page = ref(1)
const pageSize = 10

async function fetchArticles() {
  loading.value = true
  try {
    const params = { page: page.value, size: pageSize }
    if (currentTag.value) params.tagId = currentTag.value
    if (keyword.value) params.keyword = keyword.value
    const res = await getArticles(params)
    articles.value = res.data.content
    total.value = res.data.totalElements
  } finally {
    loading.value = false
  }
}

async function fetchTags() {
  const res = await getTags()
  tags.value = res.data
}

function filterByTag(tagId) {
  currentTag.value = currentTag.value === tagId ? '' : tagId
  page.value = 1
  fetchArticles()
}

function search() {
  page.value = 1
  fetchArticles()
}

function viewArticle(id) {
  window.open(`/article/${id}`, '_blank')
}

onMounted(() => {
  fetchTags()
  fetchArticles()
})
</script>

<template>
  <div class="home-page">
    <!-- Search -->
    <el-card class="search-card" shadow="never">
      <div class="search-bar">
        <el-input
          v-model="keyword"
          placeholder="搜索文章..."
          clearable
          style="width: 400px"
          @keyup.enter="search"
        />
        <el-button type="primary" @click="search">搜索</el-button>
      </div>

      <div class="tag-filter">
        <span class="filter-label">标签：</span>
        <el-tag
          v-for="tag in tags"
          :key="tag.id"
          :type="currentTag === tag.id ? 'primary' : 'info'"
          :effect="currentTag === tag.id ? 'dark' : 'plain'"
          style="cursor: pointer; margin: 4px"
          @click="filterByTag(tag.id)"
        >
          {{ tag.name }}
        </el-tag>
        <el-tag v-if="currentTag" type="danger" effect="plain" style="cursor: pointer; margin: 4px" @click="filterByTag('')">
          清除筛选
        </el-tag>
      </div>
    </el-card>

    <!-- Article List -->
    <div class="article-list" v-loading="loading">
      <el-card
        v-for="article in articles"
        :key="article.id"
        class="article-card"
        shadow="hover"
        @click="viewArticle(article.id)"
      >
        <div class="article-body">
          <div class="article-info">
            <h2 class="article-title">{{ article.title }}</h2>
            <p class="article-summary">{{ article.summary || '暂无摘要' }}</p>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><User /></el-icon>
                {{ article.authorName }}
              </span>
              <span class="meta-item">
                <el-icon><View /></el-icon>
                {{ article.viewCount }}
              </span>
              <span class="meta-item">
                <el-icon><Clock /></el-icon>
                {{ article.createdAt?.split('T')[0] }}
              </span>
            </div>
            <div class="article-tags" v-if="article.tags?.length">
              <el-tag v-for="tag in article.tags" :key="tag.id" size="small">{{ tag.name }}</el-tag>
            </div>
          </div>
          <el-image
            v-if="article.coverImage"
            :src="article.coverImage"
            class="article-cover"
            fit="cover"
          />
        </div>
      </el-card>

      <el-empty v-if="!loading && articles.length === 0" description="暂无文章" />

      <div class="pagination" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="fetchArticles"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  max-width: 900px;
  margin: 0 auto;
}

.search-card {
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.tag-filter {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.filter-label {
  font-size: 14px;
  color: #666;
  margin-right: 8px;
}

.article-card {
  margin-bottom: 16px;
  cursor: pointer;
  transition: transform 0.2s;
}

.article-card:hover {
  transform: translateY(-2px);
}

.article-body {
  display: flex;
  gap: 20px;
}

.article-info {
  flex: 1;
  min-width: 0;
}

.article-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.article-summary {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #999;
  margin-bottom: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.article-cover {
  width: 160px;
  height: 120px;
  border-radius: 6px;
  flex-shrink: 0;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
