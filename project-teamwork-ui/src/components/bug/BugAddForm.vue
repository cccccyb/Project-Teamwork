<template>
  <el-scrollbar max-height="80vh">
    <el-form :model="this.addData" :rules="this.myRule" ref="addBugForm" label-width="150px" label-position="top">
      <el-form-item label="缺陷标题:" prop="title" required>
        <el-input v-model="addData.title" style="width: 100%;height: 40px" placeholder=" 输入缺陷标题"></el-input>
      </el-form-item>
      <el-form-item label="缺陷描述:" prop="description" style="height: 90px">
        <el-input type="textarea" v-model="addData.description" style="width: 100%" placeholder=" 点此编辑缺陷描述"></el-input>
      </el-form-item>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="状态:" prop="status" style="width: 40%;">
          <el-select v-model="addData.status" placeholder="----请选择----" >
            <el-option
                v-for="item in bugStatus"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <el-tag :color="getStatusColor(item.id)" style="margin-right: 8px" size="default"/>
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级:" prop="priority" style="width: 40%">
          <el-select v-model="addData.priority" placeholder="----请选择----" >
            <el-option
                v-for="item in bugPriority"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <el-tag :color="item.color" style="margin-right: 8px" size="small"/>
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="严重程度:" prop="level" style="width: 40%;">
          <el-select v-model="addData.level" placeholder="----未指定----" >
            <el-option
                v-for="item in bugLevel"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="重现规律:" prop="reappear" style="width: 40%">
          <el-select v-model="addData.reappear" placeholder="----未指定----" >
            <el-option
                v-for="item in bugReappear"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="缺陷类型:" prop="bug_type_id" style="width: 40%;">
          <el-select v-model="addData.bug_type_id" placeholder="----未指定----" >
            <el-option
                v-for="item in allBugTypeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理人:" prop="processerId" style="width: 40%">
          <el-select v-model="addData.processerId" placeholder="----未指定----" >
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
        <el-form-item label="缺陷发现迭代:" prop="discoveryIterateId" style="width: 40%">
          <el-select v-model="addData.discoveryIterateId" placeholder="----请选择----" >
            <el-option
                v-for="item in allIterationList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="缺陷来源:" prop="origin" style="width: 40%">
          <el-select v-model="addData.origin" placeholder="----请选择----" >
            <el-option
                v-for="item in bugOrigins"
                :key="item.id"
                :label="item.name"
                :value="item.name"
            />
          </el-select>
        </el-form-item>
      </div>
      <el-form-item label="关联需求:" prop="requirementId" style="width: 40%">
        <el-select v-model="addData.requirementId" placeholder="----请搜索关联需求----" filterable>
          <el-option
              v-for="item in allRequirementList"
              :key="item.id"
              :label="item.title"
              :value="item.id"
          >
            <div style="display: flex;justify-content: flex-start;align-items: center">
              <SvgIcons width="20px" height="20px" color="#ffffff" icon-class="require_menu"/>
              <span style="color: black;font-weight: bold;font-size: 17px;margin-left: 4px">#{{item.id}}</span>
              <span style="font-size: 17px;margin-left: 3px">{{formatterRequireTitle(item.title)}}</span>
            </div>
          </el-option>
        </el-select>
      </el-form-item>



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
import {useProjBugStore} from "@/store/ProjBug.js";
import {useUserStore} from "@/store/user.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import {useProjRequirementStore} from "@/store/ProjRequirement.js";
import {useBugTypeStore} from "@/store/BugType.js";
import {useProjItemStore} from "@/store/ProjItem.js";
import SvgIcons from "@/assets/svg/index.vue";

const projBugStore = useProjBugStore()
const projIterationStore = useProjIterationStore()
const projRequirementStore = useProjRequirementStore()
const projItemStore = useProjItemStore()
const bugTypeStore = useBugTypeStore()
const userStore = useUserStore()

export default {
  components: {SvgIcons},
  computed: {
    ...mapState(useProjBugStore, ['bugStatus','bugPriority','bugOrigins','bugLevel','bugReappear']),
    ...mapState(useUserStore, ['allUserList']),
    ...mapState(useProjIterationStore, ['allIterationList']),
    ...mapState(useProjRequirementStore, ['allRequirementList']),
    ...mapState(useBugTypeStore, ['allBugTypeList'])
  },
  data() {
    return {
      addData: {
        title: '',
        description: '',
        status: '',
        priority:'',
        level:'',
        reappear:'',
        creatorId: localStorage.getItem('uid'),
        processerId:'',
        discoveryIterateId:'',
        projectId:localStorage.getItem('pid'),
        requirementId:'',
        bug_type_id:'',
        origin:''
      },
      myRule: {
        title: [
          {required: true, message: '请输入缺陷标题', trigger: 'blur'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
        ],
        status: [{ required: true, message: '请选择缺陷状态', trigger: 'change' }],
        priority: [{ required: true, message: '请选择缺陷优先级', trigger: 'change' }],
        processerId: [{ required: true, message: '请选择处理人', trigger: 'change' }],
        discoveryIterateId: [{ required: true, message: '请选择缺陷发现迭代', trigger: 'change' }],
        requirementId: [{ required: true, message: '请关联需求', trigger: 'change' }],
        bug_type_id: [{ required: true, message: '请选择缺陷类型', trigger: 'change' }]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.addBugForm.validate((valid) => {
        if (valid) {
          projBugStore.handleAddBug(this.addData)
          if (projItemStore.itemPageOpenFlag){
            projItemStore.$state.itemPageOpenFlag=false
            projItemStore.getLoading().then(r => {})
          }else {
            projBugStore.getLoading().then(r => {})
          }
        } else {
          return false
        }
      })
    },
    closeForm() {
      projBugStore.$state.dialogAddBug = false
      projItemStore.$state.itemPageOpenFlag=false
      this.resetForm()
    },
    resetForm() {
      this.$refs.addBugForm.resetFields()
    },
    //获得bug状态颜色
    getStatusColor(status) {
      switch (status) {
        case 0:
          return '#409dfd';
        case 1:
          return 'rgba(110,215,206,0.85)';
        case 2:
          return '#7ac555';
        case 3:
          return '#e7a94b';
        case 4:
          return '#bd6a5c';
        case 5:
          return '#a1a6ad';
        default:
          return '#7e7070';
      }
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
    bugTypeStore.getAllBugType()
    projIterationStore.getAllIteration(localStorage.getItem('pid'))
    projRequirementStore.getAllRequirement(localStorage.getItem('pid'))
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
