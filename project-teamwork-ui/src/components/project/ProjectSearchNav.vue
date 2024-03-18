<template>
  <div class="search_nav">
    <div class="search_left">
      <el-form :inline="true" :model="searchNav" ref="search_nav">
        <el-form-item label="项目名称：">
          <el-input v-model="searchNav.name" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item label="状态：">
          <el-select v-model="searchNav.status" placeholder="--请选择--" size="large" style="width: 130px;"
          >
            <el-option
                v-for="item in projectStatus"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <el-tag
                  disable-transitions
                  size="large"
                  :type="
                        item.id === 0
                            ? 'primary'
                            : item.id === 1
                            ? 'warning'
                            : 'success'
                    "
              >
                {{ item.name }}
              </el-tag>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期：">
          <el-date-picker
              v-model="timeRang"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="截止日期"
              style="width: auto;height: 38px"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :size="'default'" @click="selectByCondition" style="padding: 0 8px;font-size: 16px"
          >
            <el-icon size="19">
              <Search/>
            </el-icon>
            查询
          </el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :size="'default'" style="padding: 0 8px" @click="resetForm">
            <el-icon size="25">
              <RefreshLeft/>
            </el-icon>
            重置
          </el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <el-button type="primary" :size="'large'" @click="deleteBatchByIds" style="padding: 0 8px;font-size: 16px"
    >
      <el-icon size="19">
        <DeleteFilled/>
      </el-icon>
      批量删除
    </el-button
    >
  </div>

</template>

<script>
import _ from 'lodash'
import {mapState} from 'pinia'
import {useProjectStore} from "@/store/project.js";
import {DeleteFilled, RefreshLeft} from "@element-plus/icons-vue";

const projectStore = useProjectStore()
export default {
  name: 'ProjectSearchNav',
  components: {RefreshLeft, DeleteFilled},
  data() {
    return {
      timeRang: [],
      selectStatus: ''
    }
  },
  methods: {
    selectByCondition() {
      if (!_.isEmpty(this.timeRang)) {
        projectStore.$state.searchNav.startTime = this.handleDateFormatUTC(this.timeRang[0])
        projectStore.$state.searchNav.endTime = this.handleDateFormatUTC(this.timeRang[1])
      }
      projectStore.$state.loading = true
      projectStore.selectAllProject(this.currentPage, this.pageSize,projectStore.getCurrentViewPage(), this.searchNav.name, this.searchNav.status, this.searchNav.startTime, this.searchNav.endTime, projectStore.getCurrentViewPage())
    },
    handleDateFormatUTC(date) {
      let newFormat = ''
      const dateParse = new Date(Date.parse(date))
      const yy = dateParse.getUTCFullYear()
      //padStart方法：用chars参数从开头填充字符串到length长度
      const mm = _.padStart((dateParse.getUTCMonth() + 1).toString(), 2, '0')
      const dd = _.padStart(dateParse.getUTCDate().toString(), 2, '0')
      const hh = _.padStart(dateParse.getUTCHours().toString(), 2, '0')
      const mf = _.padStart(dateParse.getUTCMinutes().toString(), 2, '0')
      const ss = _.padStart(dateParse.getUTCSeconds().toString(), 2, '0')
      newFormat = yy + '-' + mm + '-' + dd + ' ' + hh + ':' + mf + ':' + ss
      return newFormat
    },
    //重置按钮操作
    resetForm() {
      this.timeRang = []
      projectStore.$state.searchNav={
        name: '',
        status: '',
        startTime: '',
        endTime: ''
      }
      projectStore.getLoading()
    },
    //批量删除项目
    deleteBatchByIds() {
      projectStore.deleteBatchByIds()
    }
  },
  computed: {
    ...mapState(useProjectStore, [
      'total',
      'selectData',
      'loading',
      'projectStatus',
      'currentPage',
      'pageSize',
      'searchNav',
      'currentViewPage'
    ])
  },

}
</script>

<style scoped>
.search_nav {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
  margin-top: 20px;
  padding: 0 10px;
}

.search_left {
  margin-right: auto;
}

/deep/ .el-form-item__label {
  font-size: 20px;
  padding: 0;
  height: 38px;
  line-height: 38px;;
}

/deep/ .el-input__wrapper {
  height: 38px;
}
</style>
