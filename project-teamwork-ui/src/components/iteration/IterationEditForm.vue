<template>
  <el-scrollbar max-height="80vh">
    <el-form :model="this.editIteration" :rules="this.myRule" ref="editIterationForm" label-width="150px" label-position="top">
      <el-form-item label="迭代名称:" prop="name" required>
        <el-input v-model="editIteration.name" style="width: 100%;height: 40px" placeholder=" 输入迭代名称"></el-input>
      </el-form-item>
      <el-form-item label="迭代描述:" prop="description" style="height: 90px">
        <el-input type="textarea" v-model="editIteration.description" style="width: 100%"
                  placeholder=" 点此编辑迭代描述"></el-input>
      </el-form-item>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="状态:" prop="status" style="width: 40%;">
          <el-select v-model="editIteration.status" placeholder="----请选择----">
            <el-option
                v-for="item in iterationStatus"
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
        <el-form-item label="负责人:" prop="directorId" style="width: 40%">
          <el-select v-model="editIteration.directorId" placeholder="----未指定----">
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
        <el-form-item label="开始日期:" prop="startTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="editIteration.startTime"
              disabled
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="截止日期:" prop="endTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              placeholder="选择截止日期"
              v-model="editIteration.endTime"
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
      </div>


      <el-form-item class="formFooter">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button type="info" @click="closeForm">取消</el-button>
      </el-form-item>
    </el-form>
  </el-scrollbar>
</template>

<script>
import {mapState} from 'pinia'
import {useUserStore} from "@/store/user.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import SvgIcons from "@/assets/svg/index.vue";
import {ElMessageBox} from "element-plus";


const projIterationStore = useProjIterationStore()
const userStore = useUserStore()

export default {
  components: {SvgIcons},
  computed: {
    ...mapState(useUserStore, ['allUserList']),
    ...mapState(useProjIterationStore, ['iterationStatus','editIteration']),
  },
  data() {
    return {
      myRule: {
        name: [
          {required: true, message: '请输入迭代名称', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        status: [{required: true, message: '请选择迭代状态', trigger: 'change'}],
        endTime: [
          {type: 'date', required: true, message: '请选择截止日期', trigger: 'change'}
        ],
        directorId: [{required: true, message: '请选择处理人', trigger: 'change'}]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.editIterationForm.validate((valid) => {
        if (valid) {
          projIterationStore.handleUpdateIteration(this.editIteration)
          projIterationStore.getLoading().then(r => {
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
            projIterationStore.$state.dialogEditVisible = false
          })
          .catch(() => {
            projIterationStore.$state.dialogEditVisible= false
          })
    }
  },
  created() {
  },
  mounted() {
    userStore.getAllUser()
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

.member_name {
  display: inline-block;
  font-weight: bold;
  font-size: 22px;
  color: rgb(51, 118, 253);
}

/deep/ .el-form-item__label {
  font-size: 17px;
}
</style>
