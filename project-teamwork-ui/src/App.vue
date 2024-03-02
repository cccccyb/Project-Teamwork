<script setup>

</script>

<template>
<!--  <div>-->
<!--    <a href="https://vitejs.dev" target="_blank">-->
<!--      <img src="/vite.svg" class="logo" alt="Vite logo" />-->
<!--    </a>-->
<!--  </div>-->
<!--  &lt;!&ndash;   路由链接，点击是路由地址会切换到属性 to 的地方   &ndash;&gt;-->
<!--    <router-link to="/">首页</router-link>-->
<!--  |-->
<!--    <router-link to="/route">路由测试</router-link>-->
<!--  |-->
<!--    <router-link to="/main">Main</router-link>-->
<!--    <hr>-->
  <!--   路由试图，路由切换组件的地方   -->
    <router-view/>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
