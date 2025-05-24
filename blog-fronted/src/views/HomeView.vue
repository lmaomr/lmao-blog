<script setup>
import getArticles from '@/api/article.js'
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/userStore.js'
import { debounce } from 'lodash';

const articles = ref([])

const userStore = useUserStore()

const pagination = ref({
  current: 1, // 当前页（从1开始，符合用户习惯）
  pageSize: 5, // 每页条数
  total: 0, // 总条数
  sort: 'id,desc' // 排序
})

const fetchArticles = async () => {
  try {
    const response = await getArticles({
      page: pagination.value.current - 1, // 后端从0开始，前端展示从1开始
      size: pagination.value.pageSize,
      sort: pagination.value.sort
    })

    // 假设后端返回结构：
    // {
    //   content: [], // 当前页数据
    //   totalElements: 100, // 总条数
    //   number: 0, // 当前页码（从0开始）
    //   size: 10 // 每页条数
    // }
    articles.value = response.data.content
    pagination.value.total = response.data.totalElements
    // 保持前后端页码转换一致
    pagination.value.current = response.data.number + 1

  } catch (error) {
    console.error('获取文章列表失败:', error)
    // 可以在这里添加错误提示，如使用ElMessage等
  }
}

const handlePageChange = debounce((page) => {
  pagination.value.current = page
  fetchArticles()
}, 300) // 防抖函数，500ms内只执行一次

const getCommentsLength = (articleId) => {
  const article = articles.value.find(item => item.id === articleId)
  return article?.comments?.length || 0
}

const truncate = (str, length) => {
  if (str.length > length) {
    return str.slice(0, length) + '...'
  }
  return str
}

onMounted(() => {
  fetchArticles()
})
</script>

<template>
  <div class="home-view">
    <el-row :gutter="40">
      <el-col :span="17" :md="17" :sm="24">
        <el-card v-for="post in articles" :key="post.id" class="article-card hover">
          <h3 class="article-title">
            <router-link :to="'/article/' + post.id">
              {{ truncate(post.title, 20) }}
            </router-link>
          </h3>
          <div class="article-info">
            <span class="article-date">
              <el-icon>
                <Calendar />
              </el-icon>
              {{ post.createTime.slice(0, 10) }}
            </span>
            <span>
              <el-icon>
                <View />
              </el-icon>
              {{ post.view }}
            </span>
            <span>
              <el-icon>
                <ChatLineRound />
              </el-icon>
              {{ getCommentsLength(post.id) }}
            </span>
            <span class="article-author">
              <el-icon>
                <User />
              </el-icon>
              {{ post.author }}
            </span>
          </div>
          <div class="article-content">
            <p>{{ truncate(post.content, 50) }} <!-- 超过50字符显示省略号 --></p>
          </div>
          <div class="article-footer">
            <div class="article-tags">
              <div class="article-tag" v-for="tag in post.tags" :key="tag.id">
                <el-tag>
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>
            <div class="article-read-more">
              <router-link :to="'/article/' + post.id">
                阅读全文
                <el-icon class="el-icon--right">
                  <ArrowRight />
                </el-icon>
              </router-link>
            </div>

          </div>
        </el-card>
        <div class="pagination-block">
          <el-pagination layout="prev, pager, next" :total="pagination.total" :page-size="pagination.pageSize"
            :current-page="pagination.current" @current-change="handlePageChange" hide-on-single-page />
        </div>
      </el-col>

      <el-col :span="7" :md="7" :sm="0">
        <div class="user-card">
          <el-card class="hover">
            <template #header>
              <div class="about-me">
                <span>关于我</span>
              </div>
            </template>
            <div class="profile-content">
              <!-- 头像 -->
              <el-avatar :size="100" :src="userStore.userInfo.avatarUrl" />
              <!-- 昵称 -->
              <h3>{{ userStore.userInfo.nickname }}</h3>
              <!-- 简介 -->
              <p>{{ userStore.userInfo.bio }}</p>
            </div>
          </el-card>

          <el-card class="category-card hover">
            <template #header>
              <div class="card-header">
                <span>分类</span>
              </div>
            </template>
            <!-- <el-tree :data="categories" :props="defaultProps" @node-click="handleNodeClick" /> -->
          </el-card>

          <el-card class="tag-card hover">
            <template #header>
              <div class="card-header">
                <span>标签</span>
              </div>
            </template>
            <div class="tag-cloud">
              <!-- <el-tag v-for="tag in tags" :key="tag.name" :type="tag.type" class="tag" @click="handleTagClick(tag)">
                {{ tag.name }}
              </el-tag> -->
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.home-view {
  margin: 0 20%;
}

.article-card {
  /* max-height: 45%; */
  width: 100%;
  margin-bottom: 1.5rem;
}

.article-info span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-right: 1.35rem;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 3.25rem;
}

.pagination-block {
  display: flex;
  justify-content: center;
  /* 水平居中 */
  margin-top: 20px;
  /* 可选：添加顶部间距 */
}

.user-card .el-card {
  margin-bottom: 2rem;
}

.profile-content {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-direction: column;
}
</style>
