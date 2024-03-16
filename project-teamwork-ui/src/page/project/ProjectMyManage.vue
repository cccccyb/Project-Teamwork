<template>
  <project-view-table/>
</template>

<script>
import projectViewTable from "@/components/project/ProjectViewTable.vue";
import {useProjectStore} from "@/store/project.js";
import {mapState} from "pinia";

const projectStore = useProjectStore()
export default {
    components: {projectViewTable},
    data() {
      return {}
    },
    methods: {},
    created() {

    },
    mounted() {
      projectStore.$state.currentViewPage = 'MyManage'
      projectStore.$state.currentPage=1
      projectStore.$state.pageSize=10
      projectStore.selectAllProject(this.currentPage, this.pageSize, '','', '', '', '', projectStore.getCurrentViewPage())
    },
    computed: {
      ...mapState(useProjectStore, ['currentViewPage','currentPage','pageSize'])
    }
  }
</script>

<style scoped>

</style>