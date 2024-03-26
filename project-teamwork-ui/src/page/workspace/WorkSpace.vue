<template>
  <div class="main">
    <div class="my_card"><IterationCard/></div>
    <div class="my_card"><RequirementCard/></div>
    <div class="my_card"><TaskCard/></div>
    <div class="my_card"><BugCard/></div>
  </div>
</template>

<script>
import IterationCard from "@/components/workspace/IterationCard.vue";
import RequirementCard from "@/components/workspace/RequirementCard.vue";
import TaskCard from "@/components/workspace/TaskCard.vue";
import BugCard from "@/components/workspace/BugCard.vue";
export default {
  components:{IterationCard,RequirementCard,TaskCard,BugCard},
  data() {
    return {}
  },
  methods: {},
  created() {
  }
}
</script>

<style scoped>
.main{
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
}
.my_card{
    width: 48%;
    height: 42vh;
    margin: 1vh 1%;
}
</style>