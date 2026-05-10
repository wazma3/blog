import request from './request'

export function getTags() {
  return request.get('/tags')
}

export function createTag(name) {
  return request.post('/admin/tags', null, { params: { name } })
}

export function deleteTag(id) {
  return request.delete(`/admin/tags/${id}`)
}
