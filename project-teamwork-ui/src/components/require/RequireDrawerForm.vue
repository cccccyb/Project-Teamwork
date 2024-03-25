<template>
  <el-scrollbar>
    <el-form :model="clickRequirement" :rules="this.myRule" ref="drawerRequireForm" label-width="150px"
             label-position="top" style="padding-right: 20px">
      <el-form-item label="需求标题:" prop="title" required class="title">
        <el-input v-model="clickRequirement.title" style="width: 100%;height: 50px"></el-input>
      </el-form-item>
      <el-form-item label="需求描述:" prop="description" style="height: 133px" class="description">
        <el-input type="textarea" v-model="clickRequirement.description" style="width: 100%"></el-input>
      </el-form-item>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: center">
        <el-form-item label="状态:" prop="status" style="width: 40%;">
          <el-select v-model="clickRequirement.status" placeholder="----请选择----">
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
                            : item.id===1
                            ? 'info'
                            : item.id===6
                            ? 'danger'
                            : item.id === 7
                            ? 'success'
                            : 'warning'
                    "
              >
                {{ item.name }}
              </el-tag>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级:" prop="priority" style="width: 40%">
          <el-select v-model="clickRequirement.priority" placeholder="----请选择----">
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
        <el-form-item label="开始日期:" prop="createTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              v-model="clickRequirement.createTime"
              disabled
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="截止日期:" prop="endTime" style="width: 40%">
          <el-date-picker
              type="datetime"
              placeholder="选择截止日期"
              v-model="clickRequirement.endTime"
              style="width: 90%"
              size="large"
          ></el-date-picker>
        </el-form-item>

      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: flex-end">
        <el-form-item label="处理人:" prop="processerId" style="width: 40%">
          <el-select v-model="clickRequirement.processerId" placeholder="----请选择----">
            <el-option
                v-for="item in allUserList"
                :key="item.id"
                :label="item.username"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人:" prop="clickRequirement.creator.username" style="width: 39%">
          <el-tag type="primary" size="large" style="font-size: 18px">{{clickRequirement.creator.username}}</el-tag>
        </el-form-item>
      </div>
      <div style="display: flex;width:100%;justify-content: space-between;align-items: flex-end">
        <el-form-item label="所属迭代:" prop="iteration_id" style="width: 40%">
          <el-select v-model="clickRequirement.iteration_id" placeholder="----请选择----">
            <el-option
                v-for="item in allIterationList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="需求来源:" prop="origin" style="width: 40%">
          <el-select v-model="clickRequirement.origin" placeholder="----请选择----">
            <el-option
                v-for="item in requireOrigins"
                :key="item.id"
                :label="item.name"
                :value="item.name"
            />
          </el-select>
        </el-form-item>
      </div>
      <el-form-item label="上次更新日期:" prop="modifyTime" style="width: 40%">
        <el-date-picker
            type="datetime"
            v-model="clickRequirement.modifyTime"
            disabled
            style="width: 90%"
            size="large"
        ></el-date-picker>
      </el-form-item>
      <!-- 关联缺陷     -->
      <el-button type="info" size="large" style="padding: 0 10px;height: 40px" @click="openTransfer">
        <el-icon size="25"><Link /></el-icon>
        关联缺陷
      </el-button>
      <!--  展示关联的缺陷    -->
<!--      v-loading="showLoading" element-loading-text="加载中..."-->
      <div class="myTimeline">
        <el-timeline>
          <el-timeline-item
              v-for="bugs in relationBugsList"
              :key="bugs.id"
              :timestamp="formatDate(bugs.createTime)"
              placement="top"
          >
            <el-card class="bugsCard">
              <div class="relationBugsCard">
                <div style="display: flex;justify-content: center;align-items: center">
                  <SvgIcons width="30px" height="30px" color="#ffffff" icon-class="bug_menu"/>
                  <span style="font-weight: bold;font-size: 21px;margin-left: 8px">#{{ bugs.id }}</span>
                  <span style="font-size: 19px;margin-left: 8px">{{ bugs.title }}</span>
                </div>
                <div style="display: flex;justify-content: center;align-items: center">
                  <el-tag
                      disable-transitions
                      size="default"
                      :color="getPriorityColor(bugs.priority)"
                  >
                  </el-tag>
                  <span style="color: #181818;font-size: 19px;margin-left: 2px">{{ formatPriority(bugs.priority) }}</span>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <!--        空状态-->
        <div style="height: max-content">
          <el-empty v-if="relationBugsList.length === 0" :image-size="20" description="暂无关联的缺陷"/>
        </div>
      </div>

      <div class="formFooter">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="primary" @click="closeForm">取消</el-button>
      </div>
    </el-form>

    <!-- 添加关联缺陷对话框     -->
    <el-dialog
        v-model="innerVisible"
        width="700"
        title="添加项目成员"
        append-to-body
    >
      <el-transfer
          v-model="relationBugs"
          filterable
          :data="allBugList"
          :titles="['所有缺陷', '关联缺陷']"
          :button-texts="['取消', '关联']"
          :props="{
            key: 'id',
            label: 'title',
            }"
      >
        <template #right-footer>
          <el-button class="transfer-footer" size="default" @click="closeTransfer">确定</el-button>
        </template>
      </el-transfer>
    </el-dialog>
  </el-scrollbar>
</template>

<script>
import {mapState} from 'pinia'
import {useProjRequirementStore} from "@/store/ProjRequirement.js";
import {useUserStore} from "@/store/user.js";
import {useProjIterationStore} from "@/store/ProjIteration.js";
import {useProjBugStore} from "@/store/ProjBug.js";
import SvgIcons from "@/assets/svg/index.vue";
import {ElMessageBox} from "element-plus";

const projRequirementStore = useProjRequirementStore()
const projIterationStore = useProjIterationStore()
const projBugStore = useProjBugStore()
const userStore = useUserStore()
export default {
  components: {SvgIcons},
  expose:['closeForm'],
  computed: {
    ...mapState(useProjRequirementStore, ['requireStatus', 'requirePriority', 'requireOrigins', 'clickRequirement','relationBugsList']),
    ...mapState(useUserStore, ['allUserList']),
    ...mapState(useProjIterationStore, ['allIterationList']),
    ...mapState(useProjBugStore, ['allBugList']),
  },
  data() {
    return {
      innerVisible:false,
      relationBugs:[],
      myRule: {
        title: [
          {required: true, message: '请输入需求标题', trigger: 'blur'},
          {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
        ],
        status: [{required: true, message: '请选择需求状态', trigger: 'change'}],
        priority: [{required: true, message: '请选择需求优先级', trigger: 'change'}],
        processerId: [{required: true, message: '请选择处理人', trigger: 'change'}],
        endTime: [
          {type: 'date', required: true, message: '请选择截止日期', trigger: 'change'}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.drawerRequireForm.validate((valid) => {
        if (valid) {
          // 更新最新修改时间
          this.clickRequirement.modifyTime=new Date()
          projRequirementStore.handleUpdateRequire(this.clickRequirement)
          projRequirementStore.getLoading().then(r => {})
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
            projRequirementStore.$state.drawerVisible = false
          })
          .catch(() => {
            projRequirementStore.$state.drawerVisible = false
          })

    },
    openTransfer(){
      this.relationBugs=[]
      this.innerVisible=true
      projBugStore.getAllBug(localStorage.getItem('pid'),this.clickRequirement.id)
    },
    closeTransfer(){
      this.innerVisible=false
      if (this.relationBugs.length>0){
        projRequirementStore.addRelationBugById(this.relationBugs)
      }
    },
    formatDate(date) {
      if (date == null) return null
      return '创建于'+new Date(date).toLocaleString()
    },
    //获得优先级颜色
    getPriorityColor(priority) {
      switch (priority) {
        case 2:
          return '#1EC79D';
        case 3:
          return '#FFDE0A';
        case 4:
          return '#FF6600';
        case 5:
          return '#E63415';
        default:
          return '#394049';
      }
    },
    formatPriority(priority) {
      switch (priority) {
        case 2:
          return '低'
        case 3:
          return '中'
        case 4:
          return '高'
        case 5:
          return '紧急'
        default:
          return '无'
      }
    }
  },
  created() {
  },
  mounted() {
    projRequirementStore.selectRelationBugById()
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

.transfer-footer {
  margin-left: 75px;
  padding: 6px 5px;
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

.myTimeline {
  margin-top: 20px;
}

.el-timeline {
  font-size: 18px;
  --el-timeline-node-size-normal: 16px;
  --el-timeline-node-size-large: 16px;
}

.bugsCard{
  --el-card-padding: 10px 20px;
  height: max-content;
  border-radius: 8px;
}
.relationBugsCard {
  display: flex;
  justify-content: space-between;
}

:deep(.el-card__header) {
  border-bottom: 2px solid #e4e7ed;
}

.el-tag {
  font-size: 13px;
}

.el-empty{
  --el-empty-padding:20px;
  --el-empty-description-margin-top:0
}
</style>
