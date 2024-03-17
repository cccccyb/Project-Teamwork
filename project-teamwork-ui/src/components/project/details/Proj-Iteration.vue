<template>
  <div class="main">
    <div class="search_nav">
      <div class="search_left">
        <el-input
            v-model="input_search"
            style="max-width: 600px;height: 38px"
            placeholder="搜索迭代"
            class="input_search"
        >
          <template #append>
            <el-button>
              <el-icon style="align-content: center" size="18" color="#0052cb">
                <Search />
              </el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
      <el-button type="primary" :size="'large'" @click="" style="font-size: 16px"
      >
        创建迭代
      </el-button
      >
    </div>
    <div class="table_show">
      <el-button><router-link :to="{name:'projectIteration', query: {itId:'1111'}}" class="jump">test</router-link></el-button>
    </div>


  </div>
</template>

<script>
import {useProjectStore} from "@/store/project.js";
import {mapState} from "pinia";
import {Search} from "@element-plus/icons-vue";

const projectStore = useProjectStore()

export default {
  computed: {
    Search() {
      return Search
    }
  },
  components: {},
  data() {
    return {
      input_search:''
    }
  },
  methods: {},
  created() {
  }
}
</script>

<style scoped>
.main {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.search_nav {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
  height: 50px;
  margin-top: 20px;
  padding: 0 10px;
}

.search_left {
  margin-right: auto;
}
</style>