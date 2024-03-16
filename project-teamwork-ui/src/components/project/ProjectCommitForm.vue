<template>
  <el-scrollbar max-height="60vh">
    <el-form :model="this.addData" :rules="this.myRule" ref="addForm" label-width="150px">
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
      <el-form-item label="截止日期:" prop="endTime">
        <el-date-picker
            type="datetime"
            placeholder="选择截止日期"
            v-model="addData.endTime"
            style="width: 100%"
            size="large"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="添加项目成员:" prop="members" style="align-items: center">
        <div style="margin-left: 10px;cursor: pointer" @click="innerVisible=true">
          <SvgIcons width="32px" height="32px" color="#304156" icon-class="addMembers" style="margin-right: 8px"/>
          <span v-for="item in selectedMembersName" class="member_name">{{item}}；</span>
        </div>
      </el-form-item>

      <!-- 添加项目成员对话框     -->
      <el-dialog
          v-model="innerVisible"
          width="700"
          title="添加项目成员"
          append-to-body
      >
        <el-transfer
            v-model="addData.members"
            filterable
            :data="allUserList"
            :titles="['可选员工', '已选成员']"
            :button-texts="['取消', '选择']"
            @right-check-change="getSelectedName"
            :props="{
            key: 'id',
            label: 'username',
            }"
        >
          <template #right-footer>
            <el-button class="transfer-footer" size="default" @click="innerVisible=false">确定</el-button>
          </template>
        </el-transfer>
      </el-dialog>

      <el-form-item class="formFooter">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button type="primary" @click="closeForm">取消</el-button>
        <el-button @click="resetForm()">重置</el-button>
      </el-form-item>
    </el-form>
  </el-scrollbar>
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
    ...mapState(useUserStore, ['allUserList']),
  },
  data() {
    return {
      innerVisible: false,
      selectedMembersName:[],
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
          {required: true, message: '请输入项目名', trigger: 'blur'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
        ],
        status: [{ required: true, message: '请选择项目状态', trigger: 'change' }],
        endTime: [
          { type: 'date', required: true, message: '请选择截止日期', trigger: 'change' }
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
    closeForm() {
      projectStore.$state.dialogAddVisible = false
      this.resetForm()
    },
    resetForm() {
      this.selectedMembersName=[]
      this.$refs.addForm.resetFields()
    },
    //获取选择的团队成员名字
    getSelectedName(newKeys){
        this.selectedMembersName = newKeys.map(key => {
          const item = this.allUserList.find(item =>
              item.id === key);
          return item ? item.username : '';
        });

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
.member_name{
  display: inline-block;
  font-weight: bold;
  font-size: 22px;
  color: rgb(51, 118, 253);
}
</style>
