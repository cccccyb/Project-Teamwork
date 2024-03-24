<template>
  <el-scrollbar>
    <el-form :model="clickBug" :rules="this.myRule" ref="drawerBugForm" label-width="150px"
             label-position="top" style="padding-right: 20px">
      <el-form-item label="缺陷标题:" prop="title" required class="title">
        <el-input v-model="clickBug.title" style="width: 100%;height: 50px"></el-input>
      </el-form-item>
      <el-form-item label="缺陷描述:" prop="description" style="height: 133px" class="description">
        <el-input type="textarea" v-model="clickBug.description" style="width: 100%"></el-input>
      </el-form-item>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="状态:" prop="status" style="width: 40%;">
          <el-select v-model="clickBug.status" placeholder="----请选择----">
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
          <el-select v-model="clickBug.priority" placeholder="----请选择----">
            <el-option
                v-for="item in bugPriority"
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
          <el-select v-model="clickBug.processerId" placeholder="----请选择----">
            <el-option
                v-for="item in allUserList"
                :key="item.id"
                :label="item.username"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="严重程度:" prop="level" style="width: 40%;">
          <el-select v-model="clickBug.level" placeholder="----未指定----" >
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

      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: flex-end">
        <el-form-item label="创建日期:" prop="createTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="clickBug.createTime"
              disabled
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="创建人:" prop="clickBug.creator.username" style="width: 39%">
          <el-tag type="primary" size="large" style="font-size: 18px">{{clickBug.creator.username}}</el-tag>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: flex-end">
        <el-form-item label="缺陷发现迭代:" prop="discoveryIterateId" style="width: 40%">
          <el-select v-model="clickBug.discoveryIterateId" placeholder="----请选择----">
            <el-option
                v-for="item in allIterationList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="缺陷来源:" prop="origin" style="width: 40%">
          <el-select v-model="clickBug.origin" placeholder="----请选择----">
            <el-option
                v-for="item in bugOrigins"
                :key="item.id"
                :label="item.name"
                :value="item.name"
            />
          </el-select>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="缺陷类型:" prop="bug_type_id" style="width: 40%;">
          <el-select v-model="clickBug.bug_type_id" placeholder="----未指定----" >
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
        <el-form-item label="重现规律:" prop="reappear" style="width: 40%">
          <el-select v-model="clickBug.reappear" placeholder="----未指定----" >
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
        <el-form-item label="上次更新日期:" prop="modifyTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="clickBug.modifyTime"
              disabled
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="所关联需求:" prop="requirementId" style="width: 40%">
          <el-select v-model="clickBug.requirementId" placeholder="----请搜索关联需求----" filterable>
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
import {useProjBugStore} from "@/store/ProjBug.js";
import {useBugTypeStore} from "@/store/BugType.js";
import {useUserStore} from "@/store/user.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import {useProjRequirementStore} from "@/store/ProjRequirement.js";
import SvgIcons from "@/assets/svg/index.vue";
import {ElMessageBox} from "element-plus";

const projBugStore = useProjBugStore()
const projIterationStore = useProjIterationStore()
const projRequirementStore = useProjRequirementStore()
const userStore = useUserStore()
const bugTypeStore = useBugTypeStore()
export default {
  components: {SvgIcons},
  expose:['closeForm'],
  computed: {
    ...mapState(useProjBugStore, ['bugStatus', 'bugPriority', 'bugOrigins', 'clickBug','bugLevel','bugReappear']),
    ...mapState(useUserStore, ['allUserList']),
    ...mapState(useProjIterationStore, ['allIterationList']),
    ...mapState(useBugTypeStore, ['allBugTypeList']),
    ...mapState(useProjRequirementStore, ['allRequirementList']),
  },
  data() {
    return {
      innerVisible:false,
      relationBugs:[],
      myRule: {
        title: [
          {required: true, message: '请输入缺陷标题', trigger: 'blur'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
        ],
        status: [{required: true, message: '请选择缺陷状态', trigger: 'change'}],
        priority: [{required: true, message: '请选择缺陷优先级', trigger: 'change'}],
        processerId: [{required: true, message: '请选择处理人', trigger: 'change'}],
        endTime: [
          {type: 'date', required: true, message: '请选择截止日期', trigger: 'change'}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.drawerBugForm.validate((valid) => {
        if (valid) {
          // 更新最新修改时间
          this.clickBug.modifyTime=new Date()
          projBugStore.handleUpdateBug(this.clickBug)
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
            projBugStore.$state.drawerVisible = false
          })
          .catch(() => {
            projBugStore.$state.drawerVisible = false
          })

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
    bugTypeStore.getAllBugType()
    userStore.getAllUser()
    projIterationStore.getAllIteration(localStorage.getItem('pid'))
    projRequirementStore.getAllRequirement(localStorage.getItem('pid'))
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
