<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/api/article'
import { useUserStore } from '@/stores/userStore'
import { useRouter, useRoute } from 'vue-router'


</script>

<template>
  <div class="login-form">
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
  </div>
</template>

<style scoped>
.auth-dialog-content {
  padding: 10px 0;
}

.auth-form {
  margin-bottom: 20px;
}

.auth-form .el-form-item {
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

.auth-button {
  width: 100%;
  padding: 10px 0;
  font-size: 16px;
  border-radius: 4px;
}

.auth-footer {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
}
</style>