<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAdminArticles, deleteArticle } from '../../api/article'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const articles = ref([])
const loading = ref(false)
const total = ref(0)
const page = ref(1)
const pageSize = 10

async function fetchArticles() {
  loading.value = true
  try {
    const res = await getAdminArticles({ page: page.value, size: pageSize })
    articles.value = res.data.content
    total.value = res.data.totalElements
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除此文章？', '提示', { type: 'warning' })
    await deleteArticle(id)
    ElMessage.success('删除成功')
    fetchArticles()
  } catch {}
}

function edit(id) {
  router.push(`/admin/articles/edit/${id}`)
}

onMounted(fetchArticles)
</script>

<template>
  <el-card shadow="never">
    <div class="header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="router.push('/admin/articles/new')">写文章</el-button>
    </div>

    <el-table :data="articles" v-loading="loading" stripe style="width: 100%">
      <el-table-column prop="title" label="标题" min-width="300">
        <template #default="{ row }">
          <span class="article-title-link">{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="authorName" label="作者" width="120" />
      <el-table-column prop="viewCount" label="阅读" width="80" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="{ row }">
          {{ row.createdAt?.split('T')[0] }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="edit(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchArticles"
      />
    </div>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  font-size: 20px;
  font-weight: 600;
}

.article-title-link {
  color: #333;
  font-weight: 500;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
