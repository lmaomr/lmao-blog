<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const formRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  agree: false
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
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
      }, 
      trigger: 'change'
    }
  ]
}

const emit = defineEmits(['success', 'switch-to-login'])

const handleRegister = async () => {
  await formRef.value.validate((valid) => {
    if (valid) {
      // 这里调用注册API
      ElMessage.success('注册成功')
      emit('success', {
        username: form.username,
        password: form.password
      })
    }
  })
}

const resetForm = () => {
  formRef.value?.resetFields()
}

defineExpose({
  resetForm
})
</script>

<template>
  <div class="auth-dialog-content">
    <el-form ref="formRef" :model="form" :rules="rules" class="auth-form" label-position="top">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="User" />
      </el-form-item>

      <el-form-item prop="email" label="邮箱">
        <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="Message" />
      </el-form-item>

      <el-form-item prop="password" label="密码">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
      </el-form-item>

      <el-form-item prop="confirmPassword" label="确认密码">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" prefix-icon="Lock" show-password />
      </el-form-item>

      <el-form-item prop="agree">
        <el-checkbox v-model="form.agree">我已阅读并同意<el-button type="text">服务条款</el-button></el-checkbox>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" class="auth-button" @click="handleRegister">注册</el-button>
      </el-form-item>
    </el-form>

    <div class="auth-footer">
      <p>已有账号? 
        <el-button type="text" @click="$emit('switch-to-login')">登录</el-button>
      </p>
    </div>
  </div>
</template>

<style scoped>
/* 样式与LoginForm.vue相同，可以提取到公共CSS文件中 */
</style>