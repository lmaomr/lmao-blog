<script setup>
import { ref, onMounted, watch, reactive, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import login from '@/api/user'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()
const router = useRouter()
// const route = useRoute()
const loginDialogVisible = ref(false)
const loginFormRef = ref(null)
const registerDialogVisible = ref(false)
const registerFormRef = ref(null)

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

// 注册表单
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  agree: false
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在3到20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 30, message: '长度在6到30个字符之间', trigger: 'blur' }
  ],
}

// 确认密码验证
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

// 注册表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在3到20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 30, message: '长度在6到30个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  agree: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请阅读并同意服务条款'))
        } else {
          callback()
        }
      }, trigger: 'change'
    }
  ]
}

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    // 退出登录
    userStore.logout()
    ElMessage.info('已退出登录')

    // 跳转到首页
    router.push('/')
  } else if (command === 'profile') {
    // 跳转到个人中心
    router.push('/profile')
  } else if (command === 'settings') {
    // 跳转到设置
    router.push('/settings')
  }
}

// 显示登录对话框
const showLoginDialog = () => {
  loginDialogVisible.value = true
  registerDialogVisible.value = false
}

// 显示注册对话框
const showRegisterDialog = () => {
  registerDialogVisible.value = true
  loginDialogVisible.value = false
}

// 处理登录逻辑
const handleLogin = async () => {
  try {
    // 1. 表单验证
    await loginFormRef.value.validate();
    // 2. 发送登录请求
    const { username, password } = loginForm;
    await login({ username, password });

    // 3. 登录成功处理
    ElMessage.success('登录成功');
    loginDialogVisible.value = false;

    // 4. 跳转逻辑
    // const redirectPath = route.query.redirect;
    router.push(userStore.redirectPath || '/'); // 默认跳转首页

  } catch (error) {
    // 修改这里的判断条件
    if (error?.username || error?.password) {
      // 表单验证错误（根据实际错误对象结构）
      const firstError = error.username?.[0] || error.password?.[0];
      ElMessage.warning(firstError?.message || '请填写正确的登录信息');
    } else {
      // 网络请求错误
      const errorMsg = error.response?.data?.message || '登录失败，请重试';
    }
  }
};

// 处理注册逻辑
const handleRegister = async () => {
  if (!registerFormRef.value) return;
  try {
    await registerFormRef.value.validate(); // 等待校验完成
    // 校验通过
    console.log('注册数据:', registerForm);
    registerDialogVisible.value = false;
    loginDialogVisible.value = true;
  } catch (error) {
    // 校验失败
    console.log('校验失败:', error);
    ElMessage.warning('请填写正确的表单信息');
  }
};

// 重置表单
const resetForm = (formRef) => {
  if (formRef) {
    formRef.resetFields()
  }
}

// 组件挂载时检查登录状态
onMounted(() => {
  userStore.checkLoginStatus()

  // 监听存储变化（用于其他标签页登录/登出同步状态）
  window.addEventListener('storage', userStore.checkLoginStatus)

  // 监听显示登录对话框事件
  window.addEventListener('show-login-dialog', showLoginDialog)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('storage', userStore.checkLoginStatus)
  window.removeEventListener('show-login-dialog', showLoginDialog)
})

const menuItems = [
  { path: "/", title: "首页" },
  // { path: "/search", title: "搜索" },
  // { path: "/archives", title: "归档" },
  // { path: "/message", title: "留言" },
  // { path: "/resource", title: "资源" },
  { path: "/cloud-disk", title: "云盘" },
  { path: "/about", title: "关于我" },
  // { path: "/tv", title: "影视" },
  // { path: "/game", title: "游戏" },
];
</script>

<template>
  <div class="nav-header">
    <div class="logo">
      lmao's blog
    </div>
    <div class="nav-menu">
      <el-menu mode="horizontal" :router="true" class="menu">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          {{ item.title }}
        </el-menu-item>
      </el-menu>
    </div>
    <div class="nav-right">
      <template v-if="userStore.isLoggedIn">
        <el-dropdown class="no-outline" @command="handleCommand">
          <div class="user-dropdown">
            <el-avatar :size="32" :src="userStore.userInfo.avatarUrl" />
            <span class="username">{{ userStore.userInfo.username }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="settings">设置</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button type="primary" @click="showLoginDialog">登录</el-button>
        <el-button type="primary" plain @click="showRegisterDialog">注册</el-button>
      </template>
    </div>
  </div>

  <!-- 登录弹出框 -->
  <el-dialog v-model="loginDialogVisible" title="登录" width="360px" :show-close="true" :close-on-click-modal="false"
    :append-to-body="true" center @closed="resetForm(loginFormRef)">
    <div class="login-dialog-content">
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
        </el-form-item>

        <div class="remember-forgot">
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
          <a href="#" class="forgot-link">忘记密码?</a>
        </div>

        <el-form-item>
          <el-button type="primary" class="login-button" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>

      <div class="btn login-footer">
        <p>还没有账号? <el-button link @click="loginDialogVisible = false; showRegisterDialog()">注册</el-button></p>
      </div>
    </div>
  </el-dialog>

  <!-- 注册弹出框 -->
  <el-dialog v-model="registerDialogVisible" title="注册" width="400px" :show-close="true" :close-on-click-modal="false"
    :append-to-body="true" center @closed="resetForm(registerFormRef)">
    <div class="register-dialog-content">
      <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form"
        label-position="top">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="email" label="邮箱">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="Message" />
        </el-form-item>

        <el-form-item prop="password" label="密码">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock"
            show-password />
        </el-form-item>

        <el-form-item prop="confirmPassword" label="确认密码">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" prefix-icon="Lock"
            show-password />
        </el-form-item>

        <el-form-item prop="agree">
          <el-checkbox v-model="registerForm.agree">我已阅读并同意<el-button link>服务条款</el-button></el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="register-button" @click="handleRegister">注册</el-button>
        </el-form-item>
      </el-form>

      <div class="btn register-footer">
        <p>已有账号? <el-button link @click="registerDialogVisible = false; showLoginDialog()">登录</el-button></p>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
.nav-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  box-shadow: 3px 3px 10px rgba(0, 0, 0, 0.091); /* 添加单位 + 提高透明度 */
  border-radius: 1.2rem;
  padding: 0 4rem;
}

.nav-header:hover {
  box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.245); /* 添加单位 + 提高透明度 */
  border-radius: 1.2rem;
}

.logo {
  font-size: 2.5rem;
  font-weight: bold;
}

.logo:hover {
  color: #409eff;
  /* cursor: pointer; */
}

/* .nav-menu {

} */

.el-menu--horizontal.el-menu {
    border-bottom: none;
}

/* .nav-right {
} */

/* 自定义类名移除黑框 */
:deep(.no-outline .el-tooltip__trigger:focus-visible) {
  outline: none;
}

.user-dropdown {
  display: flex;
  align-items: center;     /* 水平居中（交叉轴） */
  justify-content: center; /* 垂直居中（主轴，可选） */
  gap: 1rem;               /* 可选：设置头像和文字间距 */
}


/* 登录弹出框样式 */
.login-dialog-content {
  padding: 10px 0;
}

.login-form {
  margin-bottom: 20px;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forgot-link {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.forgot-link:hover {
  color: #79bbff;
}

.login-button,
.register-button {
  width: 100%;
  padding: 10px 0;
  font-size: 16px;
  border-radius: 4px;
}

.btn p {
  display: flex;
  align-items: center;
  margin: 0;
}

.login-footer,
.register-footer {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
}

.register-link {
  padding: 0;
  font-size: 14px;
}

/* 注册弹出框样式 */
.register-dialog-content {
  padding: 10px 0;
}

.register-form {
  margin-bottom: 20px;
}

.register-form .el-form-item {
  margin-bottom: 20px;
}
</style>