<template>
  <div class="info_form">
    <el-form :model="this.currentProject" :rules="this.myRule" ref="editForm" label-position="top" v-loading="this.editLoading">
      <el-form-item label="项目名称:" prop="name" required class="title">
        <el-input v-model="currentProject.name" style="width: 100%;height: 40px"></el-input>
      </el-form-item>
      <el-form-item label="项目描述:" prop="description" style="height: 120px" class="description">
        <el-input type="textarea" v-model="currentProject.description" style="width: 100%"></el-input>
      </el-form-item>
      <el-form-item label="状态:" prop="status" required>
        <el-select v-model="currentProject.status" placeholder="--请选择--">
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
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="开始日期:" prop="startTime" style="width: 45%">
          <el-date-picker
              type="datetime"
              disabled
              v-model="currentProject.startTime"
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期:" prop="endTime" style="width: 45%" required>
          <el-date-picker
              type="datetime"
              placeholder="选择结束日期"
              v-model="currentProject.endTime"
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
      </div>

    </el-form>

    <div class="formFooter">
      <el-button type="primary" @click="submitForm" color="#0052cb">保存</el-button>
    </div>
    <div class="formFooter">
      <el-button type="info" @click="resetForm" style="">重置</el-button>
    </div>
    <div class="formFooter">
      <h2 style="color: #383737">删除项目</h2>
      <h3 style="color: #383737;font-size: 15px">删除项目会删除项目内的数据（迭代、事项、事项关联关系等数据），但不会删除关联项目数据</h3>
      <el-button type="danger" @click="handleDelete" color="red" style="margin-top: 20px">删除</el-button>
    </div>

  </div>
</template>

<script>
import {mapState} from 'pinia'
import {useProjectStore} from "@/store/project.js";
import {useDetailProjectStore} from "@/store/detailProject.js";
import SvgIcons from "@/assets/svg/index.vue";

const projectStore = useProjectStore()
const detailProjectStore = useDetailProjectStore()
export default {
  components: {SvgIcons},
  computed: {
    ...mapState(useProjectStore, ['projectStatus','editLoading']),
    ...mapState(useDetailProjectStore, ['currentProject']),
  },
  data() {
    return {
      myRule: {
        name: [
          {required: true, message: '请输入项目名', trigger: 'change'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'change'}
        ],
        status: [{required: true, message: '请选择项目状态', trigger: 'change'}],
        endTime: [
          {type: 'date', required: true, message: '请选择结束日期', trigger: 'change'}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          projectStore.$state.editLoading=true
          projectStore.handleUpdateProject(this.currentProject)
        } else {
          return false
        }
      })
    },
    resetForm() {
      this.$refs.editForm.resetFields()
    },
    handleDelete(){
      projectStore.handleDeleteById(this.currentProject.id,1)
    }
  },
  created() {
  },
  mounted() {
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
  width: 150px;
}

.el-input {
  width: 300px;
}

.el-slider {
  margin-left: 20px;
}

.info_form {
  width: 60%;
  height: fit-content;
  margin-top: 25px;
  padding: 0 30px 15px 10px;
  border-right: #9E9E9E 1px solid;
}

.formFooter {
  margin: 25px 0;
}

.transfer-footer {
  margin-left: 75px;
  padding: 6px 5px;
}

.el-form-item {
  margin-bottom: 25px;
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
  height: 90px;
  font-size: 17px !important;
}
</style>
