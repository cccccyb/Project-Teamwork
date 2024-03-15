<template>
  <div>
    <h1>我参与的</h1>
  </div>
</template>

<script>
import projectViewTable from "@/components/project/ProjectViewTable.vue";
import {useProjectStore} from "@/store/project.js";
import {mapState} from "pinia";

const projectStore=useProjectStore()
export default {
    components: {projectViewTable},
    data() {
      return {}
    },
    methods: {

    },
    created() {

    },
    mounted() {
      projectStore.$state.currentViewPage='Person'
    },
    computed:{
      ...mapState(useProjectStore,['currentViewPage'])
    }
  }
</script>

<style scoped>

</style>