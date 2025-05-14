// stores/user.js
// 导入必要的函数和库
import { defineStore } from 'pinia'  // Pinia的状态管理库
import { ref, computed } from 'vue'  // Vue的响应式API
import { isTokenValid } from '@/api/valid'  // 自定义的验证工具函数

// 定义一个名为'user'的Pinia store
export const useUserStore = defineStore('user', () => {
  // 1. 状态定义部分
  // --------------------------------------------------

  // 使用ref定义token响应式变量，初始值从localStorage获取
  const token = ref(localStorage.getItem('token'))
  // 使用ref定义userInfo响应式变量，从localStorage获取并解析JSON
  // 如果localStorage中没有userInfo，则默认为null
  // 修改 userInfo 的初始化方式
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))

  // 存储重定向路径
  const redirectPath = ref('')

  // 设置路径的方法
  const setRedirectPath = (path) => {
    redirectPath.value = path
  }

  // 2. 默认用户信息
  function getDefaultUserInfo() {
    return {
      nickname: '游客',
      avatarUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      bio: '这个人很懒，什么都没留下',
    }
  }
  // 2. 计算属性部分
  // --------------------------------------------------

  // 计算用户是否已登录：检查token是否存在（双感叹号将值转为布尔值）
  const isLoggedIn = computed(() => !!token.value)

  // 3. 方法定义部分
  // --------------------------------------------------
  // 这个保险箱里有 2 个格子：

  // 登录方法：接收后端返回的响应数据
  function login(responseData) {
    // 更新token和userInfo
    token.value = responseData.token
    userInfo.value = responseData.data

    // 将token和userInfo持久化到localStorage
    localStorage.setItem('token', token.value)
    localStorage.setItem('userInfo', JSON.stringify(responseData.data))
    localStorage.setItem('isLoggedIn', isLoggedIn.value)
  }

  // 登出方法
  function logout() {
    // 清空token和userInfo
    token.value = null
    userInfo.value = getDefaultUserInfo()

    // 从localStorage中移除token和userInfo
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('isLoggedIn')
  }

  // 检查登录状态
  function checkLoginStatus() {
    try {
      // 1. 从 localStorage 读取 token 和 userInfo
      const storedToken = localStorage.getItem('token');
      const storedUserInfo = localStorage.getItem('userInfo');

      // 2. 更新响应式数据
      token.value = storedToken;
      userInfo.value = storedUserInfo ? JSON.parse(storedUserInfo) : getDefaultUserInfo();

      // 验证token有效性（可选）
      const isValid = token && isTokenValid(token.value).valid;

      // 清理无效存储
      if (!isValid) {
        logout(); // 直接调用 logout 清理状态
      }
    } catch (error) {
      console.error('登录状态检查失败:', error);
      logout(); // 直接调用 logout 清理状态
    }
  }

  // 4. 暴露给组件使用的属性和方法
  // --------------------------------------------------

  return {
    token,       // 响应式token
    userInfo,    // 响应式用户信息
    isLoggedIn,  // 计算属性：是否已登录
    redirectPath,
    login,       // 登录方法
    logout,       // 登出方法
    checkLoginStatus, // 检查登录状态方法
    setRedirectPath
  }
})