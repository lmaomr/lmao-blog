import request from '@/utils/http'

export const getUserCloud = async () => {
  return request({
    url: '/cloud',
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
}

export const uploadFile = async (file) => {
  const formData = new FormData()
  formData.append('file', file.raw)
  console.log(formData)

  return request({
    url: '/files',  // 这里需要与后端路径完全匹配
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    },
    data: formData
  })
}