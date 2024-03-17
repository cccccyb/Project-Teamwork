<template>
  <div class="info_form">
    <el-form :model="this.addData" :rules="this.myRule" ref="addForm" label-position="top">
      <el-form-item label="项目名称:" prop="name" required>
        <el-input v-model="addData.name"></el-input>
      </el-form-item>
      <el-form-item label="项目描述:" prop="description">
        <el-input type="textarea" v-model="addData.description"></el-input>
      </el-form-item>
      <el-form-item label="状态:" prop="status">
        <el-select v-model="addData.status" placeholder="--请选择--">
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
      <el-form-item label="项目时间:" prop="timeRange">
        <el-date-picker
            v-model="timeRange"
            type="datetimerange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
        />
      </el-form-item>

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
import {useUserStore} from "@/store/user.js";
import SvgIcons from "@/assets/svg/index.vue";

const projectStore = useProjectStore()
const userStore = useUserStore()
export default {
  components: {SvgIcons},
  computed: {
    ...mapState(useProjectStore, ['projectStatus']),
    ...mapState(useUserStore, []),
  },
  data() {
    return {
      timeRange: [],
      addData: {
        name: '',
        description: '',
        status: '',
        endTime: '',
        creatorId: localStorage.getItem('uid'),
        members: []
      },
      myRule: {
        name: [
          {required: true, message: '请输入项目名', trigger: 'change'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'change'}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          projectStore.handleAddProject(this.addData)
        } else {
          return false
        }
      })
    },
    resetForm() {
      this.selectedMembersName = []
      this.$refs.addForm.resetFields()
    },
    handleDelete(){

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
  margin: 35px 0;
}

.transfer-footer {
  margin-left: 75px;
  padding: 6px 5px;
}

.el-form-item {
  margin-bottom: 28px;
}
</style>
