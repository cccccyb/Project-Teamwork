<template>
  <el-scrollbar max-height="60vh">
    <el-form :model="this.addData" :rules="this.myRule" ref="addRequireForm" label-width="150px" label-position="top">
      <el-form-item label="需求标题:" prop="title" required>
        <el-input v-model="addData.title" style="width: 100%;height: 40px"></el-input>
      </el-form-item>
      <el-form-item label="需求描述:" prop="description" style="height: 90px">
        <el-input type="textarea" v-model="addData.description" style="width: 100%"></el-input>
      </el-form-item>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="状态:" prop="status" style="width: 40%;">
          <el-select v-model="addData.status" placeholder="----请选择----" >
            <el-option
                v-for="item in requireStatus"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <el-tag
                  style="width: 60%"
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
        <el-form-item label="优先级:" prop="priority" style="width: 40%">
          <el-select v-model="addData.priority" placeholder="----请选择----" >
            <el-option
                v-for="item in requirePriority"
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
        <el-form-item label="截止日期:" prop="endTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              placeholder="选择截止日期"
              v-model="addData.endTime"
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="负责人:" prop="processerId" style="width: 40%">
          <el-select v-model="addData.processerId" placeholder="----请选择----" >
            <el-option
                v-for="item in allUserList"
                :key="item.id"
                :label="item.username"
                :value="item.id"
           />
          </el-select>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="所属迭代:" prop="iteration_id" style="width: 40%">
          <el-select v-model="addData.iteration_id" placeholder="----请选择----" >
            <el-option
                v-for="item in allIterationList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="需求来源:" prop="origin" style="width: 40%">
          <el-select v-model="addData.origin" placeholder="----请选择----" >
            <el-option
                v-for="item in requireOrigins"
                :key="item.id"
                :label="item.name"
                :value="item.name"
            />
          </el-select>
        </el-form-item>
      </div>



      <el-form-item class="formFooter">
        <el-button type="info" @click="resetForm()">重置</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button type="primary" @click="closeForm">取消</el-button>
      </el-form-item>
    </el-form>
  </el-scrollbar>
</template>

<script>
import {mapState} from 'pinia'
import {useProjRequirementStore} from "@/store/ProjRequirement.js";
import {useUserStore} from "@/store/user.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import SvgIcons from "@/assets/svg/index.vue";

const projRequirementStore = useProjRequirementStore()
const projIterationStore = useProjIterationStore()
const userStore = useUserStore()
export default {
  components: {SvgIcons},
  computed: {
    ...mapState(useProjRequirementStore, ['requireStatus','requirePriority','requireOrigins']),
    ...mapState(useUserStore, ['allUserList']),
    ...mapState(useProjIterationStore, ['allIterationList']),
  },
  data() {
    return {
      addData: {
        title: '',
        description: '',
        status: '',
        priority:'',
        endTime: '',
        creatorId: localStorage.getItem('uid'),
        processerId:'',
        iteration_id:'',
        projectId:localStorage.getItem('pid'),
        origin:''
      },
      myRule: {
        title: [
          {required: true, message: '请输入需求标题', trigger: 'blur'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
        ],
        status: [{ required: true, message: '请选择需求状态', trigger: 'change' }],
        priority: [{ required: true, message: '请选择需求优先级', trigger: 'change' }],
        processerId: [{ required: true, message: '请选择处理人', trigger: 'change' }],
        endTime: [
          { type: 'date', required: true, message: '请选择截止日期', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.addRequireForm.validate((valid) => {
        if (valid) {
          projRequirementStore.handleAddRequire(this.addData)
        } else {
          return false
        }
      })
    },
    closeForm() {
      projRequirementStore.$state.dialogAddVisible = false
      this.resetForm()
    },
    resetForm() {
      this.$refs.addRequireForm.resetFields()
    }
  },
  created() {
  },
  mounted() {
    userStore.getAllUser()
    projIterationStore.getAllIteration(localStorage.getItem('pid'))
  },
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
  justify-content: center;
}
.transfer-footer {
  margin-left: 75px;
  padding: 6px 5px;
}
.member_name{
  display: inline-block;
  font-weight: bold;
  font-size: 22px;
  color: rgb(51, 118, 253);
}
/deep/ .el-form-item__label{
  font-size: 17px;
}
</style>
