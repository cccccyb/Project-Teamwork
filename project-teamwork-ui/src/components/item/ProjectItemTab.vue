<template>
  <div class="main">
    <div class="search_nav">
      <div class="search_left">
        <el-input
            v-model="input_search"
            style="width: 300px;height: 40px"
            placeholder="搜索事项"
            class="input_search"
        >
          <template #append>
            <el-button @click="selectByCondition">
              <el-icon style="align-content: center" size="18" color="#0052cb">
                <Search/>
              </el-icon>
            </el-button>
          </template>
        </el-input>
        <el-button type="primary" :size="'default'" style="padding: 0 8px;margin-left: 30px;line-height: 38px"
                   @click="resetForm">
          <el-icon size="25">
            <RefreshLeft/>
          </el-icon>
          重置
        </el-button
        >
      </div>
      <el-dropdown v-if="isShowAdd_bt" trigger="click" placement="bottom">
        <el-button type="primary" :size="'default'"
                   style="font-size: 16px;margin-right: 20px">
          新建
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item divided @click="openAddItem(1)">
              <SvgIcons width="22px" height="22px" color="#ffffff" icon-class="require_menu"/>
              <span style="font-size: 16px;margin-left: 5px;font-weight: bold">需求</span></el-dropdown-item>
            <el-dropdown-item divided @click="openAddItem(2)">
              <SvgIcons width="22px" height="22px" color="#ffffff" icon-class="task_menu"/>
              <span style="font-size: 16px;margin-left: 5px;font-weight: bold">任务</span></el-dropdown-item>
            <el-dropdown-item divided @click="openAddItem(3)">
              <SvgIcons width="22px" height="22px" color="#ffffff" icon-class="bug_menu"/>
              <span style="font-size: 16px;margin-left: 5px;font-weight: bold">缺陷</span></el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

    </div>
    <div class="table_show">
      <el-table
          style="width: 100%;font-size: 17px"
          v-loading="loading"
          height="730"
          stripe
          element-loading-text="加载中..."
          :data="selectData"
          border
          table-layout="auto"
          highlight-current-row
          :header-cell-style="{
            background: 'aliceblue',
            'text-align': 'center',
            'font-size': '18px'
        }"
      >
        <el-table-column
            prop="id"
            label="引用ID"
            show-overflow-tooltip
            align="center"
            width="100"
            fixed="left"
        >
          <template #default="scope">
            #{{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column
            prop="title"
            label="事项标题"
            show-overflow-tooltip
            fixed="left"
            width="320"
        >
          <template #default="scope">
            <div style="display: flex;align-items: center">
              <SvgIcons width="22px" height="22px" color="#ffffff" :icon-class="getItemIcon(scope.row.item_type)"/>
              <a class="jump">{{ formatterName(scope.row.title) }} </a>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="priority"
            label="优先级"
            align="center"
            width="125"
        >
          <template #default="scope">
            <el-select placeholder="" style="width: 110px"
                       @change="changePriority($event,scope.row.item_type,scope.row.id)">
              <el-option
                  v-for="item in bugPriority"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag :color="item.color" style="margin-right: 8px" size="small"/>
                {{ item.name }}
              </el-option>
              <template #prefix>
                <el-tag
                    disable-transitions
                    size="small"
                    :color="getPriorityColor(scope.row.priority)"
                >
                </el-tag>
                <span style="color: #181818;font-size: 17px">{{ formatPriority(scope.row.priority) }}</span>
              </template>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" width="150">
          <template #default="scope">
            <!--  需求状态  -->
            <el-select v-if='scope.row.item_type===1' placeholder="" size="large"
                       @change="changeStatus($event,scope.row.item_type,scope.row.id)">
              <el-option
                  v-for="item in requireStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag
                    disable-transitions
                    style="font-size: 16px"
                    size="small"
                    :type="item.id === 0
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
              <template #prefix>
                <el-tag
                    disable-transitions
                    style="font-size: 15px"
                    size="small"
                    :type="scope.row.status === 0
                            ? 'primary'
                            : scope.row.status === 1
                            ? 'info'
                            : scope.row.status === 6
                            ? 'danger'
                            : scope.row.status === 7
                            ? 'success'
                            : 'warning'"
                >
                  {{ formatStatus(scope.row.item_type, scope.row.status) }}
                </el-tag>
              </template>
            </el-select>
            <!--   任务状态         -->
            <el-select v-if='scope.row.item_type===2' placeholder="" size="large"
                       @change="changeStatus($event,scope.row.item_type,scope.row.id)">
              <el-option
                  v-for="item in taskStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag
                    disable-transitions
                    style="font-size: 16px"
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
              <template #prefix>
                <el-tag
                    disable-transitions
                    style="font-size: 15px"
                    size="small"
                    :type="
                        scope.row.status === 0
                            ? 'primary'
                            : scope.row.status === 1
                            ? 'warning'
                            : 'success'
                    "
                >
                  {{ formatStatus(scope.row.item_type, scope.row.status) }}
                </el-tag>
              </template>
            </el-select>
            <!--   缺陷状态         -->
            <el-select v-if='scope.row.item_type===3' placeholder="" size="large"
                       @change="changeStatus($event,scope.row.item_type,scope.row.id)">
              <el-option
                  v-for="item in bugStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag
                    disable-transitions
                    style="font-size: 16px"
                    size="small"
                    :color="getStatusColor(item.id)"
                >
                  <span style="color: white;font-weight: bolder">{{ item.name }}</span>
                </el-tag>
              </el-option>
              <template #prefix>
                <el-tag
                    disable-transitions
                    style="font-size: 15px"
                    size="small"
                    :color="getStatusColor(scope.row.status)"
                >
                  <span style="color: white;font-weight: bolder">{{
                      formatStatus(scope.row.item_type, scope.row.status)
                    }}</span>
                </el-tag>
              </template>
            </el-select>


          </template>
        </el-table-column>
        <el-table-column
            prop="processer.username"
            label="处理人"
            align="center"
            width="140"
        >
          <template #default="scope">
            <el-tag disable-transitions>{{ scope.row.processer.username }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="iteration.name"
            label="所属迭代"
            align="center"
            width="140"
        />
        <el-table-column
            prop="createTime"
            label="开始日期"
            sortable
            align="center"
            width="200"
        >
          <template #default="scope">
            <el-tag disable-transitions type="info" size="large">{{ formatDate(scope.row, scope.column) }}开始</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="creator.username"
            label="创建人"
            align="center"
            width="200"
        >
          <template #default="scope">
            <el-tag disable-transitions type="success">{{ scope.row.creator.username }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="modifyTime"
            label="更新日期"
            align="center"
            width="200"
        >
          <template #default="scope">
            <el-tag disable-transitions type="info" size="large">{{ formatDate(scope.row, scope.column) }}更新</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="danger" @click="handleDeleteById(scope.row.item_type,scope.row.id)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--添加需求对话框-->
    <el-dialog
        v-model="this.dialogAddRequire"
        center
        :close-on-click-modal="false"
        :show-close="false"
        :before-close="handleRequireDialogClose"
        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">添加需求</h2>
      </template>
      <RequireAddForm ref="addRequireForm"/>
    </el-dialog>

    <!--创建任务对话框-->
    <el-dialog
        v-model="this.dialogAddTask"
        center
        :close-on-click-modal="false"
        :show-close="false"
        :before-close="handleTaskDialogClose"
        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">创建任务</h2>
      </template>
      <TaskAddForm ref="addTaskForm"/>
    </el-dialog>

    <!--创建缺陷对话框-->
    <el-dialog
        v-model="this.dialogAddBug"
        center
        :close-on-click-modal="false"
        :show-close="false"
        :before-close="handleBugDialogClose"
        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">创建缺陷</h2>
      </template>
      <BugAddForm ref="addForm"/>
    </el-dialog>


  </div>
</template>

<script>
import {useProjItemStore} from "@/store/ProjItem.js";
import {mapState} from "pinia";
import {useProjBugStore} from "@/store/ProjBug.js";
import {useProjRequirementStore} from "@/store/ProjRequirement.js";
import {useProjTaskStore} from "@/store/ProjTask.js";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";
import RequireAddForm from "@/components/require/RequireAddForm.vue";
import BugAddForm from "@/components/bug/BugAddForm.vue";
import SvgIcons from "@/assets/svg/index.vue";
import TaskAddForm from "@/components/task/TaskAddForm.vue";

const projItemStore = useProjItemStore()
const projBugStore = useProjBugStore()
const projRequirementStore = useProjRequirementStore()
const projTaskStore = useProjTaskStore()

export default {
  computed: {
    ...mapState(useProjItemStore, ['loading', 'multiDeleteSelection', 'selectData','itemAddFlag']),
    ...mapState(useProjRequirementStore, ['dialogAddRequire','requireStatus']),
    ...mapState(useProjBugStore, ['dialogAddBug','bugStatus','bugPriority']),
    ...mapState(useProjTaskStore, ['dialogAddTask','taskStatus'])
  },
  components: {TaskAddForm, SvgIcons, BugAddForm, RequireAddForm, DeleteFilled, RefreshLeft},
  data() {
    return {
      input_search: '',
      isShowAdd_bt: false
    }
  },
  methods: {
    handleRequireDialogClose() {
      projRequirementStore.$state.dialogAddRequire = false
      projItemStore.$state.itemAddFlag=false
      this.$refs.addRequireForm.resetForm()
    },
    handleTaskDialogClose() {
      projTaskStore.$state.dialogAddTask = false
      projItemStore.$state.itemAddFlag=false
      this.$refs.addTaskForm.resetForm()
    },
    handleBugDialogClose() {
      projBugStore.$state.dialogAddBug = false
      projItemStore.$state.itemAddFlag=false
      this.$refs.addForm.resetForm()
    },
    //修改事项状态
    changeStatus(value, item_type, id) {
      projItemStore.$state.loading = true;
      projItemStore.updateStatusById(item_type, id, value)
    },
    //修改事项优先级
    changePriority(value, item_type, id) {
      projItemStore.$state.loading = true;
      projItemStore.updatePriorityById(item_type, id, value)
    },
    formatterName(name) {
      if (name.length > 20) {
        return name.substring(0, 20) + ' ...'
      } else {
        return name
      }
    },
    formatDate(row, column) {
      // 获取单元格数据
      const data = row[column.property]
      if (data == null) return '暂无数据'
      return new Date(data).toLocaleString();
    },
    handleDeleteById(item_type, deleteId) {
      projItemStore.handleDeleteById(item_type, deleteId)
    },
    //重置按钮操作
    resetForm() {
      this.input_search = ''
      projItemStore.getLoading()
    },
    //模糊查询
    async selectByCondition() {
      projItemStore.$state.loading = true
      await projItemStore.sleep(300)
      projItemStore.selectAllItem(this.input_search, projItemStore.getCurrentPage())
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
    //获得事项类型图标
    getItemIcon(item_type) {
      switch (item_type) {
        case 1:
          return 'require_menu'
        case 2:
          return 'task_menu'
        case 3:
          return 'bug_menu'
      }
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
          return '#645151';
      }
    },
    formatStatus(item_type, status) {
      if (item_type === 1) {
        switch (status) {
          case 0:
            return '待处理'
          case 1:
            return '待评审'
          case 2:
            return '开发中'
          case 3:
            return '待验收'
          case 4:
            return '验收中'
          case 5:
            return '待测试'
          case 6:
            return '变更'
          case 7:
            return '已完成'
          default:
            return ''
        }
      } else if (item_type === 3) {
        switch (status) {
          case 0:
            return '未开始'
          case 1:
            return '处理中'
          case 2:
            return '已解决'
          case 3:
            return '被拒绝'
          case 4:
            return '重新打开'
          case 5:
            return '已关闭'
          default:
            return ''
        }
      } else {
        return status === 0 ? '未开始' : status === 1 ? '进行中' : '已完成'
      }
    },
    formatPriority(priority){
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
    },
    openAddItem(item) {
      projItemStore.$state.itemAddFlag=true
      switch (item) {
        case 1:
          projRequirementStore.$state.dialogAddRequire = true
          break;
        case 2:
          projTaskStore.$state.dialogAddTask = true
          break;
        case 3:
          projBugStore.$state.dialogAddBug = true
          break;
      }
    }
  },
  created() {
    projItemStore.$state.loading=true
    projItemStore.$state.selectData=[]
    this.input_search = ''
    this.isShowAdd_bt = this.$route.name !== 'projectIteration'
    projItemStore.selectAllItem('', projItemStore.getCurrentPage())
  }
}
</script>

<style scoped>
.main {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.search_nav {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
  height: 50px;
  margin-top: 20px;
  padding: 0 10px;
}

.search_left {
  display: flex;
  align-items: center;
  height: 100%;
  margin-right: auto;
}

.el-table {
  margin-top: 20px;
}

.jump {
  color: #0052cb;
  font-size: 16px;
  font-weight: bold;
  margin-left: 5px;
}

.el-tag {
  font-size: 16px;
}
</style>