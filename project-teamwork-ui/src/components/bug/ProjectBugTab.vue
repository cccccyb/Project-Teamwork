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
            <el-tag :color="getStatusColor(item.name)" style="margin-right: 8px" size="default"/>
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
      <el-button v-if="isShowAdd_bt" type="primary" :size="'large'" @click="" style="font-size: 16px"
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
            <div style="display: flex;align-items: center">
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
                <span style="color: #181818;font-size: 17px">{{ scope.row.priority }}</span>
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
                    :color="getStatusColor(item.name)"
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
                  <span style="color: white;font-weight: bolder">{{ scope.row.status }}</span>
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
                    :type="scope.row.level==='致命'?'danger':scope.row.level==='严重'?'warning':scope.row.level==='一般'?'primary':'info'"
            >{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="reappear"
            label="重现规律"
            align="center"
            width="120"
        />
        <el-table-column
            prop="processer.username"
            label="负责人"
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
            <!--        <el-button size="small" color="#626aef" @click="handleShow(scope.row)"-->
            <!--        >查看-->
            <!--        </el-button>-->
            <!--        <el-button size="small" type="primary" @click="handleEdit(scope.row)"-->
            <!--        >编辑-->
            <!--        </el-button>-->
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


  </div>
</template>

<script>
import {useProjBugStore} from "@/store/ProjBug.js";
import {mapState} from "pinia";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";

const projBugStore = useProjBugStore()

export default {
  computed: {
    ...mapState(useProjBugStore, ['bugStatus', 'bugPriority', 'loading', 'total', 'currentPage', 'pageSize', 'multiDeleteSelection', 'selectData'])
  },
  components: {DeleteFilled, RefreshLeft},
  data() {
    return {
      input_search: '',
      bug_status: '',
      bug_priority: '',
      isShowAdd_bt: false
    }
  },
  methods: {
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
        case '低':
          return '#1EC79D';
        case '中':
          return '#FFDE0A';
        case '高':
          return '#FF6600';
        case '紧急':
          return '#E63415';
        default:
          return '#394049';
      }
    },
    //获得bug状态颜色
    getStatusColor(status) {
      switch (status) {
        case '未开始':
          return '#409dfd';
        case '处理中':
          return 'rgba(110,215,206,0.85)';
        case '已解决':
          return '#7ac555';
        case '被拒绝':
          return '#e7a94b';
        case '重新打开':
          return '#bd6a5c';
        case '已关闭':
          return '#a1a6ad';
        default:
          return '#645151';
      }
    },
  },
  created() {
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