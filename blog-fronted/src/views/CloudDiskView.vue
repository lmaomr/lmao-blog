<script setup>
import { ref, computed, onMounted } from 'vue'

// 状态管理
const searchQuery = ref('')
const viewType = ref('grid')
const currentPath = ref(['我的文件'])
const loading = ref(true)
const folderDialog = ref(false)
const newFolder = ref({ name: '' })
const selectedCategory = ref(null)

// 模拟数据
const usedStorage = ref('2.5 GB')
const totalStorage = ref('10 GB')
const storagePercentage = ref(25)

const categories = ref([
  { id: 'all', name: '全部文件', icon: 'Files', count: 42 },
  { id: 'image', name: '图片', icon: 'Picture', count: 16 },
  { id: 'document', name: '文档', icon: 'Document', count: 12 },
  { id: 'video', name: '视频', icon: 'VideoPlay', count: 5 },
  { id: 'audio', name: '音频', icon: 'Headset', count: 3 },
  { id: 'other', name: '其他', icon: 'MoreFilled', count: 6 }
])

const files = ref([
  { id: 1, name: '工作文档', type: 'folder', size: 0, lastModified: '2023-06-10', category: 'document' },
  { id: 2, name: '旅行照片', type: 'folder', size: 0, lastModified: '2023-05-22', category: 'image' },
  { id: 3, name: '项目计划.docx', type: 'file', size: 1024 * 1024 * 2.3, lastModified: '2023-06-12', category: 'document' },
  { id: 4, name: '会议记录.pdf', type: 'file', size: 1024 * 1024 * 1.7, lastModified: '2023-06-11', category: 'document' },
  { id: 5, name: '海边日落.jpg', type: 'file', size: 1024 * 1024 * 3.5, lastModified: '2023-06-09', category: 'image' },
  { id: 6, name: '产品演示.mp4', type: 'file', size: 1024 * 1024 * 28.5, lastModified: '2023-06-05', category: 'video' },
  { id: 7, name: '背景音乐.mp3', type: 'file', size: 1024 * 1024 * 4.2, lastModified: '2023-06-03', category: 'audio' },
  { id: 8, name: '数据分析.xlsx', type: 'file', size: 1024 * 1024 * 1.2, lastModified: '2023-06-01', category: 'document' },
  { id: 9, name: '公司Logo.png', type: 'file', size: 1024 * 512, lastModified: '2023-05-28', category: 'image' },
  { id: 10, name: '客户反馈.txt', type: 'file', size: 1024 * 10, lastModified: '2023-05-25', category: 'document' },
  { id: 11, name: '产品设计图.sketch', type: 'file', size: 1024 * 1024 * 8.1, lastModified: '2023-05-20', category: 'other' },
  { id: 12, name: '系统备份.zip', type: 'file', size: 1024 * 1024 * 42, lastModified: '2023-05-15', category: 'other' }
])

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

// 方法
const handleFileChange = (file) => {
  // 处理文件上传
  console.log('上传文件:', file)
  // 这里添加上传逻辑
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
      lastModified: new Date().toISOString().split('T')[0],
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
      files.value.sort((a, b) => new Date(b.lastModified) - new Date(a.lastModified))
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

const isImage = (filename) => {
  return /\.(jpg|jpeg|png|gif|bmp|webp)$/i.test(filename)
}

const isDocument = (filename) => {
  return /\.(doc|docx|pdf|txt|xls|xlsx|ppt|pptx)$/i.test(filename)
}

const isVideo = (filename) => {
  return /\.(mp4|avi|mov|wmv|flv|mkv)$/i.test(filename)
}

const isAudio = (filename) => {
  return /\.(mp3|wav|ogg|flac|aac)$/i.test(filename)
}

const showContextMenu = (event, file) => {
  // 显示右键菜单
  console.log('右键菜单:', file.name)
}

// 生命周期
onMounted(() => {
  // 模拟加载数据
  setTimeout(() => {
    loading.value = false
  }, 800)
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

        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="8" animated />
        </div>

        <div v-else-if="filteredFiles.length === 0" class="empty-state">
          <el-empty description="暂无文件" />
        </div>

        <div v-else :class="['files-list', viewType === 'grid' ? 'grid-view' : 'list-view']">
          <div v-for="file in filteredFiles" :key="file.id" class="file-item" @click="handleFileClick(file)"
            @contextmenu.prevent="showContextMenu($event, file)">
            <div class="file-icon">
              <el-icon v-if="file.type === 'folder'">
                <Folder />
              </el-icon>
              <el-icon v-else-if="isImage(file.name)">
                <Picture />
              </el-icon>
              <el-icon v-else-if="isDocument(file.name)">
                <Document />
              </el-icon>
              <el-icon v-else-if="isVideo(file.name)">
                <VideoPlay />
              </el-icon>
              <el-icon v-else-if="isAudio(file.name)">
                <Headset />
              </el-icon>
              <el-icon v-else>
                <Files />
              </el-icon>
            </div>
            <div class="file-details">
              <div class="file-name">{{ file.name }}</div>
              <div v-if="viewType === 'list'" class="file-meta">
                <span>{{ formatSize(file.size) }}</span>
                <span>{{ formatDate(file.lastModified) }}</span>
              </div>
            </div>
            <div class="file-actions" v-if="viewType === 'list'">
              <el-button circle size="small"><el-icon>
                  <Download />
                </el-icon></el-button>
              <el-button circle size="small"><el-icon>
                  <Share />
                </el-icon></el-button>
              <el-button circle size="small"><el-icon>
                  <More />
                </el-icon></el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="folderDialog" title="新建文件夹" width="30%">
      <el-form :model="newFolder">
        <el-form-item label="文件夹名称">
          <el-input v-model="newFolder.name" placeholder="请输入文件夹名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="folderDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmCreateFolder">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.cloud-disk-container {
  display: flex;
  flex-direction: column;
  padding: 20px;
  height: calc(100vh - 120px);
  min-height: 600px;
}

.disk-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.disk-header h1 {
  font-size: 26px;
  color: #303133;
  margin: 0;
}

.disk-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input {
  width: 240px;
}

.navigation-path {
  background-color: #f8f9fa;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.disk-content {
  display: flex;
  flex: 1;
  gap: 24px;
  overflow: hidden;
}

.sidebar {
  width: 220px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.storage-info {
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
}

.storage-text {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  margin-bottom: 10px;
}

.storage-progress {
  margin-top: 8px;
}

.file-categories {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.category-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 16px;
  color: #303133;
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.category-item:hover {
  background-color: #ecf5ff;
}

.category-icon {
  margin-right: 12px;
  font-size: 18px;
  color: #409eff;
}

.file-count {
  margin-left: auto;
  background-color: #f0f2f5;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  color: #606266;
}

.files-area {
  flex: 1;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 20px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.view-controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.loading-container,
.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
}

.files-list {
  flex: 1;
  overflow-y: auto;
}

.grid-view {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
  padding: 10px;
}

.list-view .file-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #ebeef5;
}

.list-view .file-item:last-child {
  border-bottom: none;
}

.grid-view .file-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  border-radius: 8px;
  transition: all 0.2s;
  text-align: center;
}

.grid-view .file-item:hover {
  background-color: #f5f7fa;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.grid-view .file-icon {
  font-size: 40px;
  margin-bottom: 10px;
  color: #409eff;
}

.list-view .file-icon {
  font-size: 24px;
  margin-right: 16px;
  color: #409eff;
}

.file-name {
  word-break: break-all;
  font-size: 14px;
  color: #303133;
}

.grid-view .file-name {
  max-width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.list-view .file-details {
  flex: 1;
}

.file-meta {
  display: flex;
  gap: 24px;
  color: #909399;
  font-size: 13px;
  margin-top: 4px;
}

.file-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.2s;
}

.list-view .file-item:hover .file-actions {
  opacity: 1;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .disk-content {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
  }

  .disk-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .disk-actions {
    width: 100%;
    flex-wrap: wrap;
  }

  .search-input {
    width: 100%;
  }
}
</style>