<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createArticle, updateArticle, getArticle } from '../../api/article'
import { getTags } from '../../api/tag'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const isEdit = !!route.params.id

const form = ref({
  title: '',
  summary: '',
  content: '',
  coverImage: '',
  tagIds: [],
})

const tags = ref([])
const saving = ref(false)
const loading = ref(isEdit)

onMounted(async () => {
  const res = await getTags()
  tags.value = res.data

  if (isEdit) {
    const articleRes = await getArticle(route.params.id)
    const a = articleRes.data
    form.value = {
      title: a.title,
      summary: a.summary || '',
      content: a.content,
      coverImage: a.coverImage || '',
      tagIds: a.tags?.map((t) => t.id) || [],
    }
    loading.value = false
  }
})

async function save(asDraft = false) {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('标题和内容不能为空')
    return
  }

  saving.value = true
  try {
    const payload = { ...form.value }
    if (!isEdit) {
      payload.status = asDraft ? 0 : 1
    }
    if (isEdit) {
      await updateArticle(route.params.id, payload)
      ElMessage.success('更新成功')
    } else {
      await createArticle(payload)
      ElMessage.success(asDraft ? '草稿已保存' : '发布成功')
    }
    router.push('/admin/articles')
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <el-card shadow="never" v-loading="loading">
    <h2 class="editor-title">{{ isEdit ? '编辑文章' : '写文章' }}</h2>

    <el-form :model="form" label-width="80px">
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="文章标题" size="large" />
      </el-form-item>

      <el-form-item label="摘要">
        <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="文章摘要（可选）" />
      </el-form-item>

      <el-form-item label="标签">
        <el-select v-model="form.tagIds" multiple placeholder="选择标签" style="width: 100%">
          <el-option v-for="tag in tags" :key="tag.id" :label="tag.name" :value="tag.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="封面">
        <el-input v-model="form.coverImage" placeholder="封面图片URL（可选）" />
      </el-form-item>

      <el-form-item label="内容">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="20"
          placeholder="支持 Markdown 语法"
        />
      </el-form-item>

      <el-form-item v-if="!isEdit">
        <el-button type="primary" :loading="saving" @click="save(false)">发布</el-button>
        <el-button :loading="saving" @click="save(true)">存为草稿</el-button>
        <el-button @click="router.push('/admin/articles')">取消</el-button>
      </el-form-item>
      <el-form-item v-else>
        <el-button type="primary" :loading="saving" @click="save()">保存修改</el-button>
        <el-button @click="router.push('/admin/articles')">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style scoped>
.editor-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
}
</style>
