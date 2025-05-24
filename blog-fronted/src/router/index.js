import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
    },
    // router/index.js
    {
      path: '/article/:id',  // 动态参数
      name: 'Article',
      component: () => import('@/views/ArticleView.vue')
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('@/views/AboutView.vue'),
    },
    {
      path: '/cloud-disk',
      name: 'cloud-disk',
      component: () => import('@/views/CloudDiskView.vue'), // 关键点,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/test',
      name: 'test',
      component: () => import('@/views/TestView.vue'),
    },
  ],
})

// 导入Element Plus的消息通知函数
import { ElMessage } from 'element-plus'

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  // 判断该路由是否需要登录权限
  if (to.matched.some(record => record.meta.requiresAuth)) {
    userStore.checkLoginStatus()
    // 获取登录状态
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
    if (!isLoggedIn) {
      // 未登录，显示提示消息
      ElMessage({
        message: '该功能需要登录才能使用',
        type: 'warning',
        duration: 3000,
        showClose: true,
        grouping: true
      })
      // 1. 保存用户想去的页面路径
      userStore.setRedirectPath(to.fullPath)
      // 触发登录事件
      window.dispatchEvent(new CustomEvent('show-login-dialog'))
      // 停留在当前页面
      next(from.path)
    } else {
      next() // 已登录，正常访问
    }
  } else {
    userStore.setRedirectPath('')
    next() // 不需要登录权限的页面，正常访问
  }
})

export default router
