import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK,
    DATABASE_SELECT_OK,
    DATABASE_UPDATE_OK
} from "@/constants/Common.constants.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useProjItemStore = defineStore('projItem', {
    state: () => {
        return {
            loading: false,
            dialogAddVisible: false,
            multiDeleteSelection: [],
            itemPageOpenFlag:false,
            itemStatus: [
                {
                    id: 0,
                    name: '未开始'
                },
                {
                    id: 1,
                    name: '进行中'
                },
                {
                    id: 2,
                    name: '已完成'
                },
            ],
            itemPriority: [
                {
                    id: 5,
                    name: '紧急',
                    color: '#E63415'
                },
                {
                    id: 4,
                    name: '高',
                    color: '#FF6600'
                },
                {
                    id: 3,
                    name: '中',
                    color: '#FFDE0A'
                },
                {
                    id: 2,
                    name: '低',
                    color: '#1EC79D'
                },
                {
                    id: 1,
                    name: '无',
                    color: '#394049'
                }
            ],
            bugStatus: [
                {
                    id: 0,
                    name: '未开始'
                },
                {
                    id: 1,
                    name: '处理中'
                },
                {
                    id: 2,
                    name: '已解决'
                },
                {
                    id: 3,
                    name: '被拒绝'
                },
                {
                    id: 4,
                    name: '重新打开'
                },
                {
                    id: 5,
                    name: '已关闭'
                }
            ],
            selectData: []
        }
    },
    getters: {},
    actions: {
        //添加睡眠时间
        sleep(d) {
            return new Promise((resolve) => setTimeout(resolve, d))
        },
        //重置刷新数据
        async getLoading() {
            this.loading = true
            await this.sleep(300)
            await this.selectAllItem(
                '',
                this.getCurrentPage()
            )
        },
        //根据当前项目id分页查询事项
        selectAllItem(title, iterationId) {
            void request
                .get('/project/getAllItem', {
                    params: {
                        title,
                        projectId: localStorage.getItem('pid'),
                        iterationId
                    }
                })
                .then((response) => {
                    if (response.data.code === DATABASE_SELECT_OK) {
                        this.selectData = response.data.data
                        for (let i = 0; i < this.selectData.length; i++) {
                            let data = this.selectData[i];
                            if (data.item_type === 3) {
                                switch (data.status) {
                                    case 0:
                                        data.status = '未开始';
                                        break;
                                    case 1:
                                        data.status = '处理中';
                                        break;
                                    case 2:
                                        data.status = '已解决';
                                        break;
                                    case 3:
                                        data.status = '被拒绝';
                                        break;
                                    case 4:
                                        data.status = '重新打开';
                                        break;
                                    case 5:
                                        data.status = '已关闭';
                                        break;
                                    default:
                                        data.status = '';
                                        break;
                                }
                            } else {
                                data.status = parseInt(data.status) === 0 ? '未开始' : parseInt(data.status) === 1 ? '进行中' : '已完成'
                            }
                            switch (data.priority) {
                                case 2:
                                    data.priority = '低';
                                    break;
                                case 3:
                                    data.priority = '中';
                                    break;
                                case 4:
                                    data.priority = '高';
                                    break;
                                case 5:
                                    data.priority = '紧急';
                                    break;
                                default:
                                    data.priority = '无';
                                    break;
                            }
                        }
                        this.total = parseInt(response.data.msg)
                        this.loading = false
                    } else {
                        this.loading = false
                        ElMessage({
                            message: response.data.msg,
                            type: 'error'
                        })
                    }
                })
        },
        // 根据事项类型修改状态
        async updateStatusById(itemType, itemId, status) {
            await request
                .get('/project/updateItemStatus', {
                    params: {
                        itemType,
                        itemId,
                        status
                    }
                })
                .then((response) => {
                    if (response.data.code === DATABASE_UPDATE_OK) {
                        ElMessage({
                            message: '设置成功',
                            type: 'success'
                        })
                    } else {
                        ElMessage({
                            message: response.data.msg,
                            type: 'error'
                        })
                    }
                })
            this.selectAllItem('', this.getCurrentPage())
        },
        // 根据事项类型修改优先级
        async updatePriorityById(itemType, itemId, priority) {
            await request
                .get('/project/updateItemPriority', {
                    params: {
                        itemType,
                        itemId,
                        priority
                    }
                })
                .then((response) => {
                    if (response.data.code === DATABASE_UPDATE_OK) {
                        ElMessage({
                            message: '设置成功',
                            type: 'success'
                        })
                    } else {
                        ElMessage({
                            message: response.data.msg,
                            type: 'error'
                        })
                    }
                })
            this.selectAllItem('', this.getCurrentPage())
        },
        //根据事项类型删除事项
        handleDeleteById(itemType, itemId) {
            ElMessageBox.confirm('确定是否要删除？该操作将无法回退', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '我再想想',
                type: 'warning'
            })
                .then(() => {
                    request.get('/project/deleteItem', {
                        params: {
                            itemType,
                            itemId
                        }
                    }).then((response) => {
                        if (response.data.code === DATABASE_DELETE_OK) {
                            ElMessage({
                                message: '删除成功.',
                                type: 'success'
                            })
                            this.getLoading().then(r => {
                            })
                        } else if (response.data.code === DATABASE_DELETE_ERROR) {
                            ElMessage({
                                message: response.data.msg,
                                type: 'error'
                            })
                        }
                    })
                })
                .catch(() => {
                })
        },
        //判断当前是否位于具体的迭代中
        getCurrentPage() {
            return localStorage.getItem('iterateId') === null ? '' : localStorage.getItem('iterateId')
        },
    }
})

