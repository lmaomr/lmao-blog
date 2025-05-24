<script setup>
import NavHeader from "@/components/layout/NavHeader.vue";
import NavFooter from "@/components/layout/NavFooter.vue";
import { h, ref, onMounted } from 'vue'
import { ElNotification } from 'element-plus'

const isMobile = ref(false)
const checkMobile = () => {
  const userAgent = navigator.userAgent
  const mobileDevices = ['Android', 'iPhone', 'iPad', 'iPod', 'BlackBerry', 'Windows Phone']
  isMobile.value = mobileDevices.some(device => userAgent.includes(device))
}
onMounted(() => {
  checkMobile()
  if (isMobile.value) {
    ElNotification({
      title: '温馨提示',
      message: '当前为移动端，部分功能可能不支持',
      type: 'warning',
      duration: 5000
    })
  }
})

const notify = () => {
  const currentHour = new Date().getHours() // 不需要ref，直接获取
  let message = ''

  if (currentHour >= 0 && currentHour < 6) {
    message = `🌙 凌晨${currentHour}点！你是夜之精灵吗？再不睡头发要离家出走了！`;
  } else if (currentHour >= 6 && currentHour < 9) {
    message = `☀️ 早上${currentHour}点！打工人，你的工位想你想到哭（装的）`;
  } else if (currentHour >= 9 && currentHour < 12) {
    message = `⏰ 上午${currentHour}点！咖啡续命了吗？再不干活老板要表演「眼神杀」了`;
  } else if (currentHour >= 12 && currentHour < 14) {
    message = `🍱 中午${currentHour}点！干饭不积极，脑壳有问题！干饭人冲鸭！`;
  } else if (currentHour >= 14 && currentHour < 18) {
    message = `😪 下午${currentHour}点！困到灵魂出窍？建议和同事互相讲冷笑话提神`;
  } else if (currentHour >= 18 && currentHour < 22) {
    message = `🍻 晚上${currentHour}点！下班了？不，你只是换地方继续打工（远程警告）`;
  } else {
    message = `🦉 深夜${currentHour}点！熬夜冠军你好，你的黑眼圈正在申请吉尼斯纪录`;
  }

  ElNotification({
    title: '一条废话',
    message, // 直接使用message字符串
    type: 'info', // 添加通知类型
    duration: 5000 // 5秒后自动关闭
  })
}
onMounted(() => {
  notify()
})
</script>

<template>
  <el-container class="app-container">
    <el-header class="header">
      <NavHeader />
    </el-header>
    <el-main class="el-main">
      <router-view />
    </el-main>
    <el-footer class="footer">
      <NavFooter />
    </el-footer>
  </el-container>
</template>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  flex: 0 0 20%;
  --el-header-padding: 0;
}

.el-main {
  flex: 1;
  min-height: calc(100vh - 60px);
}

/* .el-header {
  
} */

.footer {
  flex: 0 0 18rem;
  --el-footer-padding: 0;
}
</style>
