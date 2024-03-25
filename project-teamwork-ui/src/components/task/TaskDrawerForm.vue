<template>
  <el-scrollbar>
    <el-form :model="clickTask" :rules="this.myRule" ref="drawerTaskForm" label-width="150px"
             label-position="top" style="padding-right: 20px">
      <el-form-item label="缺陷标题:" prop="title" required class="title">
        <el-input v-model="clickTask.title" style="width: 100%;height: 50px"></el-input>
      </el-form-item>
      <el-form-item label="缺陷描述:" prop="description" style="height: 133px" class="description">
        <el-input type="textarea" v-model="clickTask.description" style="width: 100%"></el-input>
      </el-form-item>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="状态:" prop="status" style="width: 40%;">
          <el-select v-model="clickTask.status" placeholder="----请选择----">
            <el-option
                v-for="item in taskStatus"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <el-tag
                  disable-transitions
                  style="font-size: 15px"
                  size="small"
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
        <el-form-item label="优先级:" prop="priority" style="width: 40%">
          <el-select v-model="clickTask.priority" placeholder="----请选择----">
            <el-option
                v-for="item in taskPriority"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <el-tag :color="item.color" style="margin-right: 8px" size="large"/>
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="处理人:" prop="processerId" style="width: 40%">
          <el-select v-model="clickTask.processerId" placeholder="----请选择----">
            <el-option
                v-for="item in allUserList"
                :key="item.id"
                :label="item.username"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人:" prop="clickTask.creator.username" style="width: 39%">
          <el-tag type="primary" size="large" style="font-size: 18px">{{ clickTask.creator.username }}</el-tag>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: flex-end">
        <el-form-item label="创建日期:" prop="createTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="clickTask.createTime"
              disabled
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期:" prop="endTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="clickTask.endTime"
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: flex-end">
        <el-form-item label="上次更新日期:" prop="modifyTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="clickTask.modifyTime"
              disabled
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="所属迭代:" prop="iteration_id" style="width: 40%">
          <el-select v-model="clickTask.iteration_id" placeholder="----请选择----">
            <el-option
                v-for="item in allIterationList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
      </div>


      <div class="formFooter">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="primary" @click="closeForm">取消</el-button>
      </div>
    </el-form>

  </el-scrollbar>
</template>

<script>
import {mapState} from 'pinia'
import {useUserStore} from "@/store/user.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import {useProjTaskStore} from "@/store/ProjTask.js";
import SvgIcons from "@/assets/svg/index.vue";
import {ElMessageBox} from "element-plus";

const projIterationStore = useProjIterationStore()
const projTaskStore = useProjTaskStore()
const userStore = useUserStore()
export default {
  components: {SvgIcons},
  computed: {
    ...mapState(useProjTaskStore, ['taskStatus', 'taskPriority', 'clickTask']),
    ...mapState(useUserStore, ['allUserList']),
    ...mapState(useProjIterationStore, ['allIterationList']),
  },
  data() {
    return {
      myRule: {
        title: [
          {required: true, message: '请输入任务标题', trigger: 'blur'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
        ],
        priority: [{required: true, message: '请选择任务优先级', trigger: 'change'}],
        endTime: [
          {type: 'date', required: true, message: '请选择截止日期', trigger: 'change'}
        ],
        processerId: [{required: true, message: '请选择处理人', trigger: 'change'}],
        iteration_id: [{required: true, message: '请选择任务所属迭代', trigger: 'change'}]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.drawerTaskForm.validate((valid) => {
        if (valid) {
          // 更新最新修改时间
          this.clickTask.modifyTime = new Date()
          projTaskStore.handleUpdateTask(this.clickTask)
          projTaskStore.getLoading().then(r => {
          })
        } else {
          return false
        }
      })
    },
    closeForm() {
      ElMessageBox.confirm('是否要保存本次修改内容？', '警告', {
        confirmButtonText: '是的',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(() => {
            this.submitForm()
            projTaskStore.$state.drawerVisible = false
          })
          .catch(() => {
            projTaskStore.$state.drawerVisible = false
          })

    },
    formatterRequireTitle(title) {
      if (title.length > 10) {
        return title.substring(0, 10) + ' ...'
      } else {
        return title
      }
    },
  },
  created() {
  },
  mounted() {
    userStore.getAllUser()
    projIterationStore.getAllIteration(localStorage.getItem('pid'))
  }
}
</script>
<style scoped>
.el-button {
  margin: 0 auto;
}

.el-button--text {
  margin-right: 15px;
}

.el-select {
  width: 300px;
}

.el-input {
  width: 300px;
}

.el-slider {
  margin-left: 20px;
}

.formFooter {
  display: flex;
  margin: 20px 0;
}

/deep/ .el-form-item__label {
  font-size: 19px;
  font-weight: bold;
}

.title /deep/ .el-input__inner {
  font-size: 18px !important;
  font-weight: bold;
  color: #181818;
}

.description /deep/ .el-textarea__inner {
  height: 100px;
  font-size: 17px !important;
}

.el-tag {
  font-size: 13px;
}

</style>
