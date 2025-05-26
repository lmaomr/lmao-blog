<script setup>

</script>

<template>
    <div class="cloud-view">
        <!--这是侧边栏 -->
        <div class="sidebar">
            <div class="logo">
                <img src="/ui/logo.png" alt="Logo" />
            </div>
            <div class="menu">
                <el-aside width="200px">
                    <el-scrollbar>
                        <el-menu>
                            <el-sub-menu index="1">
                                <template #title class="menu-title">
                                    <img class="menu-icon" src="/ui/Home, homepage, menu.svg" alt="home" />我的文件
                                </template>
                                <el-menu-item index="1-1">Option 1</el-menu-item>
                                <el-menu-item index="1-2">Option 2</el-menu-item>
                                <el-menu-item index="1-3">Option 3</el-menu-item>
                            </el-sub-menu>
                        </el-menu>
                    </el-scrollbar>
                </el-aside>
            </div>
        </div>
        <div class="main">
            <div class="header">

            </div>
            <div class="content">

            </div>
        </div>
    </div>
</template>

<style scoped>
.cloud-view {
    height: 100vh;
    display: flex;
    background-color: rgb(245, 245, 245);
}

.logo {
    margin: auto;
    width: 16rem;
}

/* 深度选择器穿透组件作用域 */
:deep(.el-aside) {
    background-color: transparent;
}

:deep(.el-menu) {
    background-color: transparent;
    border: none;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
    background-color: transparent;
}

/* .sidebar {

} */

.menu-title {
    font-size: 1rem;
    font-weight: 400;
    display: flex;
    justify-content: center;

}

.menu-icon {
    width: 20px;
    height: 20px;
    margin-right: 8px;
    transition: all 0.3s;
    /* 添加悬停动画 */
}

.main {
    flex: 1;
}
</style>