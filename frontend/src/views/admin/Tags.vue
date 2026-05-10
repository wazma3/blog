<script setup>
import { ref, onMounted } from 'vue'
import { getTags, createTag, deleteTag } from '../../api/tag'
import { ElMessage, ElMessageBox } from 'element-plus'

const tags = ref([])
const loading = ref(false)
const newTagName = ref('')

async function fetchTags() {
  loading.value = true
  try {
    const res = await getTags()
    tags.value = res.data
  } finally {
    loading.value = false
  }
}

async function handleCreate() {
  if (!newTagName.value.trim()) return
  try {
    await createTag(newTagName.value.trim())
    ElMessage.success('创建成功')
    newTagName.value = ''
    fetchTags()
  } catch {}
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除此标签？', '提示', { type: 'warning' })
    await deleteTag(id)
    ElMessage.success('删除成功')
    fetchTags()
  } catch {}
}

onMounted(fetchTags)
</script>

<template>
  <el-card shadow="never">
    <h2 class="header-title">标签管理</h2>

    <div class="create-tag">
      <el-input
        v-model="newTagName"
        placeholder="输入标签名称"
        style="width: 300px"
        @keyup.enter="handleCreate"
      />
      <el-button type="primary" @click="handleCreate">添加标签</el-button>
    </div>

    <el-table :data="tags" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="标签名" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style scoped>
.header-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
}

.create-tag {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}
</style>
