<template>
  <div class="main">
    <div class="search_nav">
      <div class="search_left">
        <el-input
            v-model="input_search"
            style="width: 300px;height: 40px"
            placeholder="搜索缺陷"
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
        <el-select v-model="bug_status" placeholder="--请选择--" size="large" style="width: 150px;"
                   @change="selectByCondition"
        >
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
        <span style="margin-left: 30px;line-height: 38px;font-size: 18px">优先级：</span>
        <el-select v-model="bug_priority" placeholder="--请选择--" size="large" style="width: 150px;"
                   @change="selectByCondition"
        >
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
        <el-button type="primary" :size="'default'" style="padding: 0 8px;margin-left: 30px;line-height: 38px"
                   @click="resetForm">
          <el-icon size="25">
            <RefreshLeft/>
          </el-icon>
          重置
        </el-button
        >
      </div>
      <el-button v-if="isShowAdd_bt" type="primary" :size="'large'" @click="openAddBugDialog"
                 style="font-size: 16px"
      >
        创建缺陷
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
            label="缺陷标题"
            show-overflow-tooltip
            fixed="left"
            width="320"
        >
          <template #default="scope">
            <div style="display: flex;align-items: center" @click="openBugDrawer(scope.row)">
              <SvgIcons width="22px" height="22px" color="#ffffff" icon-class="bug_menu"/>
              <a class="jump">{{ formatterName(scope.row.title) }} </a>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="priority"
            label="优先级"
            align="center"
            width="120"
        >
          <template #default="scope">
            <el-select placeholder="" style="width: 110px" @change="changePriority($event,scope.row.id)">
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
            <el-select placeholder="" size="large" @change="changeStatus($event,scope.row.id)">
              <el-option
                  v-for="item in bugStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag
                    disable-transitions
                    style="font-size: 16px"
                    size="large"
                    :color="getStatusColor(item.id)"
                >
                  <span style="color: white;font-weight: bolder">{{ item.name }}</span>
                </el-tag>
              </el-option>
              <template #prefix>
                <el-tag
                    disable-transitions
                    style="font-size: 15px"
                    size="default"
                    :color="getStatusColor(scope.row.status)"
                >
                  <span style="color: white;font-weight: bolder">{{ formatStatus(scope.row.status) }}</span>
                </el-tag>
              </template>
            </el-select>

          </template>
        </el-table-column>
        <el-table-column
            prop="level"
            label="严重程度"
            align="center"
            width="120"
        >
          <template #default="scope">
            <el-tag disable-transitions
                    :type="scope.row.level===4?'danger':scope.row.level===3?'warning':scope.row.level===2?'primary':'info'"
            >{{ formatLevel(scope.row.level) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="reappear"
            label="重现规律"
            align="center"
            width="120"
        >
          <template #default="scope">
            {{ formatReappear(scope.row.reappear) }}
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
            prop="discoveryIterate.name"
            label="发现迭代"
            align="center"
            width="140"
        />
        <el-table-column
            prop="createTime"
            label="创建日期"
            sortable
            align="center"
            width="200"
        >
          <template #default="scope">
            <el-tag disable-transitions type="info" size="large">{{ formatDate(scope.row, scope.column) }}创建</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="origin"
            label="缺陷来源"
            align="center"
            width="200"
        />
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

    <!--创建缺陷对话框-->
    <el-dialog
        v-model="this.dialogAddBug"
        center
        :close-on-click-modal="false"
        :show-close="false"
        :before-close="handleDialogClose"
        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"
    >
      <template #header>
        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">创建缺陷</h2>
      </template>
      <BugAddForm ref="addForm"/>
    </el-dialog>

    <!-- 展示and编辑缺陷抽屉   -->
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
                style="font-weight: bolder;font-size: 25px;color: #2b2929;margin-left: 10px">#{{ clickBug.id }} </span>
          </div>
        </template>
        <BugDrawerForm/>
      </el-drawer>
    </div>


  </div>
</template>

<script>
import {useProjBugStore} from "@/store/ProjBug.js";
import {useProjItemStore} from "@/store/ProjItem.js";
import {mapState} from "pinia";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";
import BugAddForm from "@/components/bug/BugAddForm.vue";
import BugDrawerForm from "@/components/bug/BugDrawerForm.vue";
import SvgIcons from "@/assets/svg/index.vue";

const projBugStore = useProjBugStore()
const projItemStore = useProjItemStore()

export default {
  computed: {
    ...mapState(useProjBugStore, ['bugStatus', 'bugPriority', 'loading', 'total', 'currentPage', 'pageSize', 'multiDeleteSelection', 'selectData', 'dialogAddBug', 'clickBug', 'drawerVisible'])
  },
  components: {SvgIcons, BugDrawerForm, DeleteFilled, RefreshLeft, BugAddForm},
  data() {
    return {
      input_search: '',
      bug_status: '',
      bug_priority: '',
      isShowAdd_bt: false
    }
  },
  methods: {
    // 打开添加缺陷对话框
    openAddBugDialog() {
      projBugStore.$state.dialogAddBug = true
      this.$refs.addForm.resetForm()
    },
    handleDialogClose() {
      projBugStore.$state.dialogAddBug = false
      projItemStore.$state.itemAddFlag=false

      this.$refs.addForm.resetForm()
    },
    // 打开缺陷抽屉
    openBugDrawer(row) {
      //若直接赋值是浅拷贝，编辑修改时原表格数据也跟着改变
      projBugStore.$state.clickBug = JSON.parse(JSON.stringify(row))
      projBugStore.$state.drawerVisible = true
    },
    handleDrawerClose() {
      projBugStore.$state.drawerVisible = false
    },
    //表格多选，批量删除
    handleSelectionChange(val) {
      // val的值为所勾选行的数组对象
      projBugStore.$state.multiDeleteSelection = val
    },
    //修改缺陷状态
    changeStatus(value, id) {
      projBugStore.$state.loading = true;
      projBugStore.updateStatusById(id, value)
    },
    //修改缺陷优先级
    changePriority(value, id) {
      projBugStore.$state.loading = true;
      projBugStore.updatePriorityById(id, value)
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
      projBugStore.handleDeleteById(deleteId)
    },
    handleSizeChange(size) {
      // pageSize：每页多少条数据
      projBugStore.$state.pageSize = size
      projBugStore.selectAllBug(
          this.currentPage,
          parseInt(size),
          this.input_search,
          this.bug_status,
          this.bug_priority,
          projBugStore.getCurrentPage()
      )
    },
    handleCurrentChange(current) {
      // currentPage：当前第几页
      projBugStore.$state.currentPage = current
      projBugStore.selectAllBug(
          parseInt(current),
          this.pageSize,
          this.input_search,
          this.bug_status,
          this.bug_priority,
          projBugStore.getCurrentPage()
      )
    },
    //重置按钮操作
    resetForm() {
      this.input_search = ''
      this.bug_status = ''
      this.bug_priority = ''
      projBugStore.getLoading()
    },
    //批量删除项目
    deleteBatchByIds() {
      projBugStore.deleteBatchByIds()
    },
    //模糊查询
    async selectByCondition() {
      projBugStore.$state.loading = true
      await projBugStore.sleep(300)
      projBugStore.selectAllBug(this.currentPage, this.pageSize, this.input_search, this.bug_status, this.bug_priority, projBugStore.getCurrentPage())
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
    formatStatus(status) {
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
    },
    formatLevel(level) {
      switch (level) {
        case 1:
          return '提示/建议'
        case 2:
          return '一般'
        case 3:
          return '严重'
        case 4:
          return '致命'
        default:
          return ''
      }
    },
    formatReappear(reappear) {
      switch (reappear) {
        case 1:
          return '必然重现'
        case 2:
          return '随机重现'
        case 3:
          return '很难重现'
        default:
          return ''
      }
    }
  },
  created() {
    projBugStore.$state.loading = true
    projBugStore.$state.selectData = []
    this.input_search = ''
    this.bug_status = ''
    this.bug_priority = ''
    projBugStore.$state.currentPage = 1
    projBugStore.$state.pageSize = 10
    this.isShowAdd_bt = this.$route.name !== 'projectIteration'
    projBugStore.selectAllBug(this.currentPage, this.pageSize, '', '', '', projBugStore.getCurrentPage())
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