<template>
  <el-table
      v-loading="loading"
      stripe
      element-loading-text="加载中..."
      ref="tableRef"
      :data="selectData"
      border
      highlight-current-row
      :header-cell-style="{
            background: 'aliceblue',
            'text-align': 'center',
            'font-size': '16px'
        }"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" align="center"/>
    <el-table-column type="index" width="80" label="序号" align="center">
      <template #default="scope">
        {{ (this.currentPage - 1) * this.pageSize + scope.$index + 1 }}
      </template>
    </el-table-column>
    <el-table-column
        prop="name"
        label="项目名"
        show-overflow-tooltip
        align="center"
    >
      <template #default="scope">
        <router-link :to="{name:'projectDetail', query: {pid:scope.row.id}}" class="jump">
          {{ formatterTitle(scope.row.name) }}
        </router-link>
      </template>
    </el-table-column>
    <el-table-column
        prop="description"
        label="描述"
        show-overflow-tooltip
        align="center"
    />
    <el-table-column prop="status" label="状态" align="center" width="140">
      <template #default="scope">
        <el-select placeholder="" size="large" @change="changeStatus($event,scope.row.id)">
          <el-option
              v-for="item in projectStatus"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          >
            <el-tag
                disable-transitions
                size="default"
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
        prop="startTime"
        label="开始日期"
        sortable
        :formatter="formatDate"
        align="center"
    />
    <el-table-column
        prop="endTime"
        label="截止日期"
        sortable
        :formatter="formatDate"
        align="center"
    />
    <el-table-column
        prop="creator.username"
        label="创建者"
        align="center"
    >
      <template #default="scope">
        <el-tag disable-transitions>{{ scope.row.creator.username }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center">
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
</template>

<script>
import {mapState} from 'pinia'
import {useProjectStore} from "@/store/project.js";

const projectStore = useProjectStore()

export default {
  computed: {
    ...mapState(useProjectStore, [
      'total',
      'selectData',
      'loading',
      'projectStatus',
      'dialogShowVisible',
      'dialogEditVisible',
      'currentPage',
      'pageSize',
      'searchNav'
    ])
  },
  data() {
    return {}
  },
  methods: {
    //表格多选，批量删除
    handleSelectionChange(val) {
      // val的值为所勾选行的数组对象
      projectStore.$state.multiDeleteSelection = val
    },
    changeStatus(value, id) {
      projectStore.$state.loading = true;
      projectStore.updateStatusById(id, value)
    },
    formatterTitle(title) {
      if (title.length > 10) {
        return title.substring(0, 10) + ' ...'
      } else {
        return title
      }
    },
    formatDate(row, column) {
      // 获取单元格数据
      const data = row[column.property]
      if (data == null) return '暂无数据'
      return new Date(data).toLocaleString();
    },
    handleDeleteById(deleteId) {
      projectStore.handleDeleteById(deleteId,0)
    },
    handleSizeChange(size) {
      // pageSize：每页多少条数据
      projectStore.$state.pageSize = size
      // projectStore.$patch({
      //   pageSize: size
      // })
      projectStore.selectAllProject(
          this.currentPage,
          parseInt(size),
          projectStore.getCurrentViewPage(),
          this.searchNav.name,
          this.searchNav.status,
          this.searchNav.startTime,
          this.searchNav.endTime,
          projectStore.getCurrentViewPage()
      )
    },
    handleCurrentChange(current) {
      // currentPage：当前第几页
      projectStore.$state.currentPage = current
      // projectStore.$patch({
      //   currentPage: current
      // })
      projectStore.selectAllProject(
          parseInt(current),
          this.pageSize,
          projectStore.getCurrentViewPage(),
          this.searchNav.name,
          this.searchNav.status,
          this.searchNav.startTime,
          this.searchNav.endTime,
          projectStore.getCurrentViewPage()
      )
    },
    formatStatus(status) {
      return status === 0 ? '未开始' : status === 1 ? '进行中' : '已完成'
    },
  },
  // watch: {
  //   //   监听属性对象，newValue为新的值，也就是改变后的值
  //   rightClickVisible(newValue, oldValue) {
  //     if (newValue) {
  //       // 菜单显示的时候
  //       // 在body上添加事件处理程序
  //       document.body.addEventListener('click', this.closeMenu)
  //     } else {
  //       // 菜单隐藏的时候
  //       // 移除body上添加的事件处理程序
  //       document.body.removeEventListener('click', this.closeMenu)
  //     }
  //   }
  // },
  mounted() {

  },
}
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}

.pagination {
  display: flex;
  margin: 20px 0;
  justify-content: center;
}

.contextmenu {
  background: #fff;
  z-index: 3000;
  position: fixed;
  list-style-type: none;
  padding: 5px 0;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  color: #333;
  border: 1px solid #dadadc;
}

.contextmenu li {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 7px 16px;
  cursor: pointer;
}

.contextmenu li:hover {
  background: #eee;
}

.jump {
  color: #0052cb;
  font-size: 16px;
  font-weight: bold;
}

.jump:hover {
  color: rgba(243, 150, 110, 0.85);
  font-weight: bolder;
  font-size: large;
  text-decoration: underline;
}
</style>
