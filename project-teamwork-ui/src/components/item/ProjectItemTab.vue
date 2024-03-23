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
                  v-for="item in itemPriority"
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
            <!--  需求or任务状态  -->
            <el-select v-else placeholder="" size="large"
                       @change="changeStatus($event,scope.row.item_type,scope.row.id)">
              <el-option
                  v-for="item in itemStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              >
                <el-tag
                    disable-transitions
                    style="font-size: 16px"
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
                    size="default"
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


  </div>
</template>

<script>
import {useProjItemStore} from "@/store/ProjItem.js";
import {mapState} from "pinia";
import {DeleteFilled, RefreshLeft, Search} from "@element-plus/icons-vue";

const projItemStore = useProjItemStore()

export default {
  computed: {
    ...mapState(useProjItemStore, ['itemStatus', 'itemPriority', 'loading', 'multiDeleteSelection', 'selectData', 'bugStatus'])
  },
  components: {DeleteFilled, RefreshLeft},
  data() {
    return {
      input_search: ''
    }
  },
  methods: {
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
    }
  },
  created() {
    this.input_search = ''
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