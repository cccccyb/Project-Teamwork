<template>
  <div class="main">
    <div class="search_nav">
      <div class="search_left">
        <el-input
            v-model="input_search"
            style="width: 300px;height: 40px"
            placeholder="搜索任务"
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
        <span style="margin-left: 30px;line-height: 38px;font-size: 18px">状态：</span>
        <el-select v-model="task_status" placeholder="--请选择--" size="large" style="width: 150px;"
                   @change="selectByCondition"
        >
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
        <span style="margin-left: 30px;line-height: 38px;font-size: 18px">优先级：</span>
        <el-select v-model="task_priority" placeholder="--请选择--" size="large" style="width: 150px;"
                   @change="selectByCondition"
        >
          <el-option
              v-for="item in taskPriority"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          >
            <el-tag :color="item.color" style="margin-right: 8px" size="small"/>
            {{ item.name }}
          </el-option>
        </el-select>
        <el-button type="primary" :size="'default'" style="padding: 0 8px;margin-left: 30px;line-height: 38px"
                   @click="resetForm">
          <el-icon size="25">
            <RefreshLeft/>
          </el-icon>
          重置
        </el-button
        >
      </div>
      <el-button v-if="isShowAdd_bt" type="primary" :size="'large'" @click="openAddTaskDialog" style="font-size: 16px"
      >
        创建任务
      </el-button
      >
      <el-button type="primary" :size="'large'" @click="deleteBatchByIds" style="padding: 0 8px;font-size: 16px"
      >
        <el-icon size="19">
          <DeleteFilled/>
        </el-icon>
        批量删除
      </el-button
      >
    </div>

    <div class="table_show">
      <el-table
          style="width: 100%;font-size: 17px"
          v-loading="loading"
          stripe
          element-loading-text="加载中..."
          ref="tableRef"
          :data="selectData"
          border
          table-layout="auto"
          highlight-current-row
          :header-cell-style="{
            background: 'aliceblue',
            'text-align': 'center',
            'font-size': '18px'
        }"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="center"/>
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
            label="任务标题"
            show-overflow-tooltip
            fixed="left"
            width="320"
        >
          <template #default="scope">
            <div style="display: flex;align-items: center" @click="openTaskDrawer(scope.row)">
              <SvgIcons width="22px" height="22px" color="#ffffff" icon-class="task_menu"/>
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
            <el-select placeholder="" style="width: 110px" @change="changePriority($event,scope.row.id)">
              <el-option
                  v-for="item in taskPriority"
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
        <el-table-column prop="status" label="状态" align="center" width="140">
          <template #default="scope">
            <el-select placeholder="" size="large" @change="changeStatus($event,scope.row.id)">
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
                  {{ formatStatus(scope.row.status) }}
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
            prop="endTime"
            label="截止日期"
            sortable
            :formatter="formatDate"
            align="center"
            width="200"
        >
          <template #default="scope">
            <el-tag disable-transitions type="info" size="large">{{ formatDate(scope.row, scope.column) }}截止</el-tag>
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
            <el-button size="small" type="danger" @click="handleDeleteById(scope.row.id)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--      分页条-->
      <div class="pagination">
        <el-pagination
            style="text-align: center"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            layout="total, sizes, prev, pager, next, jumper"
            background
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            v-model:current-page="this.currentPage"
            v-model:page-size="this.pageSize"
        >
        </el-pagination>
      </div>
    </div>

    <!--创建任务对话框-->
    <el-dialog
        v-model="this.dialogAddTask"
        center
        :close-on-click-modal="false"
        :show-close="false"
        :before-close="handleDialogClose"
        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">创建任务</h2>
      </template>
      <TaskAddForm ref="addTaskForm"/>
    </el-dialog>

    <!-- 展示and编辑任务抽屉   -->
    <div v-if="drawerVisible">
      <el-drawer
          ref="drawerRef"
          v-model="drawerVisible"
          @close="handleDrawerClose"
          size="48%"
      >
        <template #header style="margin-bottom: 0">
          <div style="display: flex;align-items: center">
            <SvgIcons width="32px" height="32px" color="#ffffff" icon-class="bug_menu"/>
            <span
                style="font-weight: bolder;font-size: 25px;color: #2b2929;margin-left: 10px">#{{ clickTask.id }} </span>
          </div>
        </template>
        <TaskDrawerForm/>
      </el-drawer>
    </div>


  </div>
</template>

<script>
import {useProjTaskStore} from "@/store/ProjTask.js";
import {useProjItemStore} from "@/store/ProjItem.js";
import {mapState} from "pinia";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";
import SvgIcons from "@/assets/svg/index.vue";
import TaskAddForm from "@/components/task/TaskAddForm.vue";
import TaskDrawerForm from "@/components/task/TaskDrawerForm.vue";

const projTaskStore = useProjTaskStore()
const projItemStore = useProjItemStore()

export default {
  computed: {
    ...mapState(useProjTaskStore, ['taskStatus', 'taskPriority', 'loading', 'total', 'currentPage', 'pageSize', 'multiDeleteSelection', 'selectData','dialogAddTask','drawerVisible','clickTask'])
  },
  components: {TaskAddForm,TaskDrawerForm,SvgIcons, DeleteFilled, RefreshLeft},
  data() {
    return {
      input_search: '',
      task_status: '',
      task_priority: '',
      isShowAdd_bt: false
    }
  },
  methods: {
    // 打开添加任务对话框
    openAddTaskDialog() {
      projTaskStore.$state.dialogAddTask = true
      this.$refs.addTaskForm.resetForm()
    },
    handleDialogClose() {
      projTaskStore.$state.dialogAddTask = false
      projItemStore.$state.itemAddFlag=false
      this.$refs.addTaskForm.resetForm()
    },
    // 打开任务抽屉
    openTaskDrawer(row) {
      //若直接赋值是浅拷贝，编辑修改时原表格数据也跟着改变
      projTaskStore.$state.clickTask = JSON.parse(JSON.stringify(row))
      projTaskStore.$state.drawerVisible = true
    },
    handleDrawerClose() {
      projTaskStore.$state.drawerVisible = false
    },
    //表格多选，批量删除
    handleSelectionChange(val) {
      // val的值为所勾选行的数组对象
      projTaskStore.$state.multiDeleteSelection = val
    },
    //修改任务状态
    changeStatus(value, id) {
      projTaskStore.$state.loading = true;
      projTaskStore.updateStatusById(id, value)
    },
    //修改任务优先级
    changePriority(value, id) {
      projTaskStore.$state.loading = true;
      projTaskStore.updatePriorityById(id, value)
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
    handleDeleteById(deleteId) {
      projTaskStore.handleDeleteById(deleteId)
    },
    handleSizeChange(size) {
      // pageSize：每页多少条数据
      projTaskStore.$state.pageSize = size
      projTaskStore.selectAllTask(
          this.currentPage,
          parseInt(size),
          this.input_search,
          this.task_status,
          this.task_priority,
          projTaskStore.getCurrentPage()
      )
    },
    handleCurrentChange(current) {
      // currentPage：当前第几页
      projTaskStore.$state.currentPage = current
      projTaskStore.selectAllTask(
          parseInt(current),
          this.pageSize,
          this.input_search,
          this.task_status,
          this.task_priority,
          projTaskStore.getCurrentPage()
      )
    },
    //重置按钮操作
    resetForm() {
      this.input_search = ''
      this.task_status = ''
      this.task_priority = ''
      projTaskStore.getLoading()
    },
    //批量删除项目
    deleteBatchByIds() {
      projTaskStore.deleteBatchByIds()
    },
    //模糊查询
    async selectByCondition() {
      projTaskStore.$state.loading = true
      await projTaskStore.sleep(300)
      projTaskStore.selectAllTask(this.currentPage, this.pageSize, this.input_search, this.task_status, this.task_priority, projTaskStore.getCurrentPage())
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
    formatStatus(status) {
      return status === 0 ? '未开始' : status === 1 ? '进行中' : '已完成'
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
    projTaskStore.$state.loading=true
    projTaskStore.$state.selectData=[]
    this.input_search = ''
    this.task_status = ''
    this.task_priority = ''
    projTaskStore.$state.currentPage = 1
    projTaskStore.$state.pageSize = 10
    this.isShowAdd_bt = this.$route.name !== 'projectIteration'
    projTaskStore.selectAllTask(this.currentPage, this.pageSize, '', '', '', projTaskStore.getCurrentPage())
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

.pagination {
  display: flex;
  margin: 20px 0;
  justify-content: center;
}

.jump {
  color: #0052cb;
  font-size: 16px;
  font-weight: bold;
  margin-left: 5px;
}

.jump:hover {
  color: #0052cb;
  font-weight: bolder;
  font-size: 16px;
  text-decoration: underline;
  cursor: pointer;
}

.el-tag {
  font-size: 16px;
}
</style>