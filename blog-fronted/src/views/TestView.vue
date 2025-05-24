<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { getUserCloud, uploadFile } from '@/api/cloud.js'
import { ElMessage } from 'element-plus'
import {
  Folder,
  Picture,
  Document,
  VideoPlay,
  Headset,
  Files
} from '@element-plus/icons-vue'

// 状态管理
const searchQuery = ref('')
const viewType = ref('grid')
const currentPath = ref(['我的文件'])
const loading = ref(true)
const folderDialog = ref(false)
const newFolder = ref({ name: '' })
const selectedCategory = ref(null)

// 模拟数据
const usedStorage = ref(0)
const totalStorage = ref(0)
const storagePercentage = ref(0)

const categories = ref([])

const files = ref([])

// 计算属性
const filteredFiles = computed(() => {
  let result = files.value

  // 搜索过滤
  if (searchQuery.value) {
    result = result.filter(file =>
      file.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  // 分类过滤
  if (selectedCategory.value && selectedCategory.value !== 'all') {
    result = result.filter(file => file.category === selectedCategory.value)
  }

  return result
})

const initCloud = () => {
  getUserCloud()
    .then(res => {
      // 处理获取的云盘数据
      usedStorage.value = formatSize(res.data.usedCapacity)
      totalStorage.value = formatSize(res.data.totalCapacity)
      storagePercentage.value = Math.round((res.data.usedCapacity / res.data.totalCapacity) * 1000) / 10
      // 处理文件列表
      files.value = res.data.files || []
      categories.value = res.data.categories || []
      console.log('云盘文件列表:', res.data)
      // 这里可以更新文件列表等
      loading.value = false; // 隐藏加载状态
    })
    .catch(err => {
      console.error('获取云盘数据失败:', err)
    })
}

// 文件上传方法
const handleFileChange = (file) => {
  // 调用上传API
  uploadFile(file)
    .then(res => {
      console.log('上传成功:', res)
      ElMessage({
        message: res.message,
        type: 'res.success == true' ? 'success' : 'error',
      })
      initCloud() // 重新获取文件列表
    })
    .catch(err => {
      ElMessage.error(`上传失败: ${err.message}`)
    })
}

const createFolder = () => {
  folderDialog.value = true
}

const confirmCreateFolder = () => {
  if (newFolder.value.name.trim()) {
    const folder = {
      id: Date.now(),
      name: newFolder.value.name,
      type: 'folder',
      size: 0,
      updateTime: new Date().toISOString().split('T')[0],
      category: 'document'
    }
    files.value.unshift(folder)
    folderDialog.value = false
    newFolder.value.name = ''
  }
}

const handleFileClick = (file) => {
  if (file.type === 'folder') {
    currentPath.value.push(file.name)
    // 这里应该加载该文件夹下的文件
  } else {
    // 预览或下载文件
    console.log('打开文件:', file.name)
  }
}

const navigateTo = (index) => {
  currentPath.value = currentPath.value.slice(0, index + 1)
  // 根据当前路径加载文件
}

const filterByCategory = (categoryId) => {
  selectedCategory.value = categoryId
}

const sortBy = (criteria) => {
  switch (criteria) {
    case 'name':
      files.value.sort((a, b) => a.name.localeCompare(b.name))
      break
    case 'date':
      files.value.sort((a, b) => new Date(b.updateTime) - new Date(a.updateTime))
      break
    case 'size':
      files.value.sort((a, b) => b.size - a.size)
      break
    case 'type':
      files.value.sort((a, b) => {
        // 文件夹优先
        if (a.type === 'folder' && b.type !== 'folder') return -1
        if (a.type !== 'folder' && b.type === 'folder') return 1
        return a.name.localeCompare(b.name)
      })
      break
  }
}

const formatSize = (size) => {
  if (size === 0) return '-'

  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let i = 0

  while (size >= 1024 && i < units.length - 1) {
    size /= 1024
    i++
  }

  return `${size.toFixed(1)} ${units[i]}`
}

const formatDate = (date) => {
  return date
}

const getFileType = (filename) => {
  const patterns = {
    image: /\.(jpg|jpeg|png|gif|bmp|webp)$/i,
    document: /\.(doc|docx|pdf|txt|xls|xlsx|ppt|pptx)$/i,
    video: /\.(mp4|avi|mov|wmv|flv|mkv)$/i,
    audio: /\.(mp3|wav|ogg|flac|aac)$/i
  };

  for (const type in patterns) {
    if (patterns[type].test(filename)) {
      return type;
    }
  }

  return 'other';
};

// 统一更新分类计数
const updateCategoryCounts = () => {
  categories.value.forEach(cat => cat.count = 0);
  files.value.forEach(file => {
    categories.value[0].count++;
    const type = getFileType(file.name);
    const category = categories.value.find(cat => cat.id === type);
    if (category) category.count++;
  });
};

// 在文件列表变化时调用
watch(files, updateCategoryCounts, { deep: true });

const fileIconMap = {
  folder: Folder,
  image: Picture,
  document: Document,
  video: VideoPlay,
  audio: Headset,
  unknown: Files
}

const getFileIconComponent = (file) => {
  return file.type === 'folder'
    ? fileIconMap.folder
    : fileIconMap[getFileType(file.name)] || fileIconMap.unknown
}

const showContextMenu = (event, file) => {
  // 显示右键菜单
  console.log('右键菜单:', file.name)
}

// 生命周期
onMounted(() => {
  initCloud()
  // 初始化分类计数
})
</script>

<template>
  <div class="cloud-disk-container">
    <div class="disk-header">
      <h1>我的云盘</h1>
      <div class="disk-actions">
        <el-input placeholder="搜索文件..." prefix-icon="Search" v-model="searchQuery" clearable class="search-input" />
        <el-upload class="upload-btn" :auto-upload="false" :on-change="handleFileChange" :show-file-list="false">
          <el-button type="primary" class="upload-button">
            <el-icon>
              <Upload />
            </el-icon>上传文件
          </el-button>
        </el-upload>
        <el-button type="success" @click="createFolder">
          <el-icon>
            <FolderAdd />
          </el-icon>新建文件夹
        </el-button>
      </div>
    </div>

    <div class="navigation-path">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item v-for="(path, index) in currentPath" :key="index" @click="navigateTo(index)">
          {{ path }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="disk-content">
      <div class="sidebar">
        <div class="storage-info">
          <div class="storage-text">
            <span>存储空间</span>
            <span>{{ usedStorage }} / {{ totalStorage }}</span>
          </div>
          <el-progress :percentage="storagePercentage" :stroke-width="6" class="storage-progress"></el-progress>
        </div>

        <div class="file-categories">
          <div class="category-title">文件分类</div>
          <div class="category-list">
            <div class="category-item" v-for="category in categories" :key="category.id"
              @click="filterByCategory(category.id)">
              <el-icon :class="['category-icon', category.icon]">
                <component :is="category.icon" />
              </el-icon>
              <span>{{ category.name }}</span>
              <span class="file-count">{{ category.count }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="files-area">
        <div class="view-controls">
          <el-radio-group v-model="viewType" size="small">
            <el-radio-button value="grid" label="">
              <el-icon>
                <Grid />
              </el-icon>
            </el-radio-button>
            <el-radio-button value="list" label="">
              <el-icon>
                <List />
              </el-icon>
            </el-radio-button>
          </el-radio-group>

          <el-dropdown>
            <el-button>
              排序方式<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="sortBy('name')">按名称排序</el-dropdown-item>
                <el-dropdown-item @click="sortBy('date')">按日期排序</el-dropdown-item>
                <el-dropdown-item @click="sortBy('size')">按大小排序</el-dropdown-item>
                <el-dropdown-item @click="sortBy('type')">按类型排序</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>

        <div v-if="true" class="loading-container">
          <el-skeleton :rows="8" animated />
        </div>

      </div>
    </div>

  </div>
</template>

<style scoped>
.cloud-disk-container {
  display: flex;
  flex-direction: column;
  gap: 2.4rem;
  padding: 2rem;
  /* background-color: #f5f5f5; */
  border-radius: 1rem;
  font-size: 1.4rem;
}

.disk-header {
  display: flex;
  justify-content: space-between;
}

.disk-header h1 {
  font-size: 2.6rem;
  color: #333;
}

.disk-actions {
  display: flex;
  align-items: center;
  gap: 1.2rem;
}

.disk-actions .search-input {
  width: 24rem;
}

.navigation-path {
  background-color: #f8f8f8;
  border-radius: 0.8rem;
  padding: 16px 10px;
}

.disk-content {
  display: flex;
  gap: 2rem;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.storage-info {
  display: flex;
  gap: 1rem;
  flex-direction: column;
  background-color: #f8f8f8;
  border-radius: 1rem;
  padding: 12px 16px;
}

.storage-text {
  display: flex;
  gap: 2rem;
}

.file-categories {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  background-color: #f8f8f8;
  border-radius: 1rem;
  padding: 12px 16px;
  font-size: 1.6rem;
  font-weight: 500;
}

.category-icon {
  margin-right: 1.2rem;
  font-size: 1.8rem;
  color: #409eff;
}

.category-list {
  display: flex;
  flex-direction: column;
  justify-items: center;
  font-size: 1.6rem;
  font-weight: normal;
  gap: 1rem;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 1rem 1.2rem;
  cursor: pointer;
  border-radius: 0.6rem;
  transition: background-color 0.2s;
}

.file-count {
  margin-left: auto;
  background-color: #f0f2f5;
  padding: 0.2rem 0.8rem;
  border-radius: 1rem;
  font-size: 1.2rem;
}

.category-item:hover {
  background-color: #ecf5ff;
}

.files-area {
  flex: 1;
  border-radius: 1rem 1rem 0 0;
  box-shadow: 1rem 1rem 2rem rgba(0, 0, 0, 0.1);
}

.view-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.2rem 2.4rem;
}


</style>