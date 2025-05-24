<script setup>

</script>

<template>
  <footer class="footer">
    <div class="footer-content">
      <p class="copyright">&copy; 2022 by lmao. All rights reserved.</p>
      <a class="icp-links" href="https://qqmail.com" target="_blank" rel="noopener noreferrer">邮箱:lmao.mr@foxmail.com</a>
      <a class="mail-links" href="https://github.com/lmaomr" target="_blank" rel="noopener noreferrer">GitHub:github.com/lmaomr</a>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  animation: animateBg 16s linear infinite;
  background-image: linear-gradient(60deg,#cf5c5c,#64b3d9,#208ea2,#5b73df,#897ed3,#c19b4a,#4695ba,#df6868);
  background-size: 800% 100%;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
@keyframes animateBg {
  0% { background-position: 0% 0%; }
  100% { background-position: 100% 0%; }
}
.footer-content {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
  flex-direction: column; /* 使子元素垂直排列 */
  gap: 0.125rem;
  width: 100%;
  text-align: center;
  /* 字体设置 */
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif; /* 中文字体 */
  font-size: 1.5rem; /* 基础字体大小 */
  font-weight: 400; /* 字体粗细 */
  letter-spacing: 0.01em; /* 字间距 */
  line-height: 1.5; /* 行高 */
  color: #f0f0f0; /* 字体颜色 */
}
/* 让链接继承父元素的颜色 */
.footer-content a {
  color: inherit;
}
/* .copyright {
}
.icp-links {
}
.mail-links {
} */
</style>
