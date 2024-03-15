<template>
  <project-view-table/>
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
    projectStore.$state.currentViewPage='All'
    projectStore.selectAllProject(this.currentPage, this.pageSize, '', -1, '', '', '')
  },
  computed:{
    ...mapState(useProjectStore,['currentViewPage'])
  }
}
</script>

<style scoped>

</style>