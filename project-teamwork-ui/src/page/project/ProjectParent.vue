<template>
  <div class="main">
    <div class="view_head">
      <ProjectViewHead/>
    </div>

    <div class="search_nav">
      <ProjectSearchNav/>
    </div>
    <router-view/>
  </div>
</template>

<script>
import ProjectViewHead from "@/components/project/ProjectViewHead.vue";
import ProjectSearchNav from "@/components/project/ProjectSearchNav.vue";
import {useProjectStore} from "@/store/project.js";
import {mapState} from "pinia";
const projectStore=useProjectStore()

export default {
  components: {ProjectViewHead,ProjectSearchNav},
  data() {
    return {}
  },
  methods: {
  },
  created() {
  }
}
</script>

<style scoped>
.main{
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.view_head{
  width: 100%;
  height: 50px;
}
</style>