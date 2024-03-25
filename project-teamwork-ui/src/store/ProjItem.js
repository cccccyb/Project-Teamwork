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
            itemAddFlag:false,
            multiDeleteSelection: [],
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

