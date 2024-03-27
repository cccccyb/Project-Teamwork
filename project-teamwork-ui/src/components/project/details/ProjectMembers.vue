<template>
  <div class="main">
    <div class="search_nav">
      <div class="search_left">
        <el-input
            v-model="input_search"
            style="width: 300px;height: 40px"
            placeholder="请输入成员名称"
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
        <el-button type="primary" color="#0052cb" :size="'default'"
                   style="padding: 0 8px;margin-left: 30px;line-height: 38px" @click="resetForm">
          <el-icon size="25">
            <RefreshLeft/>
          </el-icon>
          重置
        </el-button
        >
      </div>
      <el-button type="primary" color="#0052cb" :size="'large'" @click="openAddIterationDialog" style="font-size: 16px"
      >
        添加成员
      </el-button
      >
      <el-button type="primary" color="#0052cb" :size="'large'" @click="deleteBatchByIds"
                 style="padding: 0 8px;font-size: 16px"
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
          style="width:100%;font-size: 17px"
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
        <el-table-column type="index" width="80" label="序号" align="center">
          <template #default="scope">
            {{ (this.currentPage - 1) * this.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
            prop="username"
            label="成员名称"
            show-overflow-tooltip
            align="center"
        />
        <el-table-column
            prop="email"
            label="邮箱"
            show-overflow-tooltip
            align="center"
        />
        <el-table-column label="是否启用" prop="enable" width="160" align="center">
          <template #default="scope">
            <el-switch
                :loading="switchLoading"
                v-model="scope.row.enable"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #afb2b8"
                active-text="启用"
                inactive-text="禁用"
                :active-value="1"
                :inactive-value="0"
                @change="switchChang(scope.row.id, scope.row.enable)"
            />
          </template>
        </el-table-column>
        <el-table-column
            prop="groups"
            label="所属用户组"
        >
          <template #default="scope">
            <el-tag
                v-if="!scope.row.groups || scope.row.groups.length === 0"
                type="info"
            >无
            </el-tag>
            <el-tooltip
                v-for="(group, index) in scope.row.groups"
                :key="index"
                :content="group.name"
            >
              <el-tag>{{
                  group.name.length > 5 ? group.name.substring(0, 4) + '...' : group.name
                }}
              </el-tag>
            </el-tooltip>
            <!--            <p><span v-for="(group,index) in scope.row.groups">{{group.name}}<span v-if="index!==scope.row.groups.length-1">、</span></span></p>-->
          </template>
        </el-table-column>
        <el-table-column
            prop="roles"
            label="所属角色"
            align="center"
        >
          <template #default="scope">
            <el-tag
                v-if="!scope.row.roles || scope.row.roles.length === 0"
                type="info"
            >无
            </el-tag>
            <el-tooltip
                v-for="(role, index) in scope.row.roles"
                :key="index"
                :content="role.name"
            >
              <el-tag type="warning" size="default">{{
                  role.name.length > 5 ? role.name.substring(0, 4) + '...' : role.name
                }}
              </el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <!--            <el-button size="small" type="info" @click="openEditIterationDialog(scope.row)"-->
            <!--            >详情-->
            <!--            </el-button>-->
            <el-button size="small" type="danger" @click="handleDeleteById(scope.row.id)"
            >退出项目
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

    <!--    &lt;!&ndash;创建迭代对话框&ndash;&gt;-->
    <!--    <el-dialog-->
    <!--        v-model="this.dialogAddVisible"-->
    <!--        center-->
    <!--        :close-on-click-modal="false"-->
    <!--        :before-close="handleDialogClose"-->
    <!--        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"-->
    <!--    >-->
    <!--      <template #header>-->
    <!--        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">创建迭代</h2>-->
    <!--      </template>-->
    <!--      <IterationAddForm ref="addIterationForm"/>-->
    <!--    </el-dialog>-->

    <!--    &lt;!&ndash;编辑迭代对话框&ndash;&gt;-->
    <!--    <el-dialog-->
    <!--        v-model="this.dialogEditVisible"-->
    <!--        center-->
    <!--        :close-on-click-modal="false"-->
    <!--        :before-close="handleEditClose"-->
    <!--        style="min-width: 400px; max-width: 900px;z-index: 10;padding: 30px"-->
    <!--    >-->
    <!--      <template #header>-->
    <!--        <h2 style="color: rgba(71,138,173,0.85);font-size: 22px;font-weight: bolder">详情</h2>-->
    <!--      </template>-->
    <!--      <IterationEditForm ref="editIterationForm"/>-->
    <!--    </el-dialog>-->

  </div>
</template>

<script>
import {useProjectMembersStore} from "@/store/ProjectMembers.js";
import {mapState} from "pinia";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";


const projectMembers = useProjectMembersStore()

export default {
  computed: {
    Search() {
      return Search
    },
    ...mapState(useProjectMembersStore, ['loading', 'total', 'currentPage', 'pageSize', 'multiDeleteSelection', 'selectData', 'switchLoading'])
  },
  components: {DeleteFilled, RefreshLeft},
  data() {
    return {
      input_search: '',
    }
  },
  methods: {
    // 打开添加迭代对话框
    openAddIterationDialog() {
      // projIteration.$state.dialogAddVisible = true
      // this.$refs.addIterationForm.resetForm()
    },
    handleDialogClose() {
      projIteration.$state.dialogAddVisible = false
      this.$refs.addIterationForm.resetForm()
    },
    // 打开编辑迭代对话框
    openEditIterationDialog(row) {
      //若直接赋值是浅拷贝，编辑修改时原表格数据也跟着改变
      projIteration.$state.editIteration = JSON.parse(JSON.stringify(row))
      projIteration.$state.dialogEditVisible = true
    },
    handleEditClose() {
      projIteration.$state.dialogEditVisible = false
    },
    //表格多选，批量删除
    handleSelectionChange(val) {
      // val的值为所勾选行的数组对象
      projectMembers.$state.multiDeleteSelection = val
    },
    changeStatus(value, id) {
      projectMembers.$state.loading = true;
      // projectMembers.updateStatusById(id, value)
    },
    handleDeleteById(deleteId) {
      // projIteration.handleDeleteById(deleteId)
    },
    handleSizeChange(size) {
      // pageSize：每页多少条数据
      projectMembers.$state.pageSize = size
      projectMembers.selectPageProjectMembers(
          this.currentPage,
          parseInt(size),
          this.input_search
      )
    },
    handleCurrentChange(current) {
      // currentPage：当前第几页
      projectMembers.$state.currentPage = current
      projectMembers.selectPageProjectMembers(
          parseInt(current),
          this.pageSize,
          this.input_search
      )
    },
    //重置按钮操作
    resetForm() {
      this.input_search = ''
      projectMembers.getLoading()
    },
    //批量删除项目
    deleteBatchByIds() {
      // projIteration.deleteBatchByIds()
    },
    //模糊查询
    async selectByCondition() {
      projectMembers.$state.loading = true
      await projectMembers.sleep(300)
      projectMembers.selectPageProjectMembers(this.currentPage, this.pageSize, this.input_search)
    },
    //改变启用状态
    async switchChang(id, value) {
      projectMembers.$state.switchLoading = true
      projectMembers.$state.loading = true
      // await noticeTypeStore.updateNoticeTypeEnable(id, value)
      await projectMembers.selectPageProjectMembers(
          this.currentPage,
          this.pageSize,
          this.input_search
      )
    },
  },
  created() {
    this.input_search = ''
    projectMembers.$state.currentPage = 1
    projectMembers.$state.pageSize = 10
    projectMembers.selectPageProjectMembers(this.currentPage, this.pageSize, '')
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
}

.el-tag {
  font-size: 16px;
}
</style>