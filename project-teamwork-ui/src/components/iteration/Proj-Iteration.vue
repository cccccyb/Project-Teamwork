<template>
  <div class="main">
    <div class="search_nav">
      <div class="search_left">
        <el-input
            v-model="input_search"
            style="width: 300px;height: 40px"
            placeholder="搜索迭代"
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
        <el-select v-model="iteration_status" placeholder="--请选择--" size="large" style="width: 150px;" @change="selectByCondition"
        >
          <el-option
              v-for="item in iterationStatus"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          >
            <el-tag
                disable-transitions
                size="large"
                style="font-size: 15px"
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
        <el-button type="primary" :size="'default'" style="padding: 0 8px;margin-left: 30px;line-height: 38px" @click="resetForm">
          <el-icon size="25">
            <RefreshLeft/>
          </el-icon>
          重置
        </el-button
        >
      </div>
      <el-button type="primary" :size="'large'" @click="" style="font-size: 16px"
      >
        创建迭代
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
        >
          <template #default="scope">
            #{{scope.row.id}}
          </template>
        </el-table-column>
        <el-table-column
            prop="name"
            label="迭代名称"
            show-overflow-tooltip
            align="center"
        >
          <template #default="scope">
            <router-link :to="{name:'projectIteration', query: {itId:scope.row.id}}" class="jump">
              {{ formatterName(scope.row.name) }}
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
                  v-for="item in iterationStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag
                    disable-transitions
                    size="small"
                    style="font-size: 15px"
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
                    size="small"
                    style="font-size: 15px"
                    :type="
                        scope.row.status === '未开始'
                            ? 'primary'
                            : scope.row.status === '进行中'
                            ? 'warning'
                            : 'success'
                    "
                >
                  {{ scope.row.status }}
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
            label="结束日期"
            sortable
            :formatter="formatDate"
            align="center"
        />
        <el-table-column
            prop="director.username"
            label="负责人"
            align="center"
        >
          <template #default="scope">
            <el-tag disable-transitions>{{ scope.row.director.username }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
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
import {useProjIterationStore} from "@/store/ProjIteration.js";
import {mapState} from "pinia";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";


const projIteration = useProjIterationStore()

export default {
  computed: {
    Search() {
      return Search
    },
    ...mapState(useProjIterationStore, ['iterationStatus', 'loading', 'total', 'currentPage', 'pageSize','multiDeleteSelection', 'selectData'])
  },
  components: {DeleteFilled, RefreshLeft},
  data() {
    return {
      input_search: '',
      iteration_status: ''
    }
  },
  methods: {
    //表格多选，批量删除
    handleSelectionChange(val) {
      // val的值为所勾选行的数组对象
      projIteration.$state.multiDeleteSelection = val
    },
    changeStatus(value, id) {
      projIteration.$state.loading = true;
      projIteration.updateStatusById(id, value)
    },
    formatterName(name) {
      if (name.length > 10) {
        return name.substring(0, 10) + ' ...'
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
      projIteration.handleDeleteById(deleteId)
    },
    handleSizeChange(size) {
      // pageSize：每页多少条数据
      projIteration.$state.pageSize = size
      projIteration.selectAllIteration(
          this.currentPage,
          parseInt(size),
          this.input_search,
          this.iteration_status,
      )
    },
    handleCurrentChange(current) {
      // currentPage：当前第几页
      projIteration.$state.currentPage = current
      projIteration.selectAllIteration(
          parseInt(current),
          this.pageSize,
          this.input_search,
          this.iteration_status,
      )
    },
    //重置按钮操作
    resetForm() {
      this.input_search=''
      this.iteration_status=''
      projIteration.getLoading()
    },
    //批量删除项目
    deleteBatchByIds() {
      projIteration.deleteBatchByIds()
    },
    //模糊查询
    async selectByCondition(){
      projIteration.$state.loading=true
      await projIteration.sleep(300)
      projIteration.selectAllIteration(this.currentPage,this.pageSize,this.input_search,this.iteration_status)
    },
    //
  },
  created() {
    this.input_search=''
    this.iteration_status=''
    projIteration.$state.currentPage=1
    projIteration.$state.pageSize=10
    projIteration.selectAllIteration(this.currentPage,this.pageSize,'','')
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
.jump{
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
.el-tag{
  font-size: 16px;
}
</style>