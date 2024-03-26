import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK, DATABASE_SAVE_ERROR, DATABASE_SAVE_OK,
    DATABASE_SELECT_OK, DATABASE_UPDATE_ERROR,
    DATABASE_UPDATE_OK
} from "@/constants/Common.constants.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useProjTaskStore = defineStore('projTask', {
    state: () => {
        return {
            total: 0,
            pageSize: 10,
            currentPage: 1,
            loading: false,
            dialogAddTask: false,
            drawerVisible:false,
            clickTask:{},
            multiDeleteSelection: [],
            cardSelectData:[],
            taskStatus: [
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
            taskPriority: [
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
            await this.selectAllTask(
                this.currentPage,
                this.pageSize,
                '',
                '',
                '',
                this.getCurrentPage()
            )
        },
        //根据当前项目id分页查询任务
        selectAllTask(currentPage, pageSize, title, status, priority,iterationId) {
            void request
                .get('/task/page', {
                    params: {
                        currentPage,
                        pageSize,
                        title,
                        status,
                        priority,
                        creatorId: '',
                        processerId: '',
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
        //添加任务
        handleAddTask(addFormData) {
            request.post('/task/addTask', addFormData).then((response) => {
                if (response.data.code === DATABASE_SAVE_OK) {
                    this.dialogAddTask = false
                    ElMessage({
                        message: '添加成功.',
                        type: 'success'
                    })
                } else if (response.data.code === DATABASE_SAVE_ERROR) {
                    ElMessage({
                        message: response.data.msg,
                        type: 'error'
                    })
                }
            })
        },
        //修改任务
        handleUpdateTask(updateTask) {
            request.put('/task/updateTask', updateTask).then((response) => {
                if (response.data.code === DATABASE_UPDATE_OK) {
                    this.drawerVisible = false
                    ElMessage({
                        message: '修改成功.',
                        type: 'success'
                    })
                } else if (response.data.code === DATABASE_UPDATE_ERROR) {
                    ElMessage({
                        message: response.data.msg,
                        type: 'error'
                    })
                }
            })
        },
        // 根据任务id修改状态
        async updateStatusById(tId, status) {
            await request
                .get('/task/updateStatus', {
                    params: {
                        tId,
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
            this.selectAllTask(this.currentPage, this.pageSize, '', '', '',this.getCurrentPage())
        },
        // 根据任务id修改优先级
        async updatePriorityById(tId, priority) {
            await request
                .get('/task/updatePriority', {
                    params: {
                        tId,
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
            this.selectAllTask(this.currentPage, this.pageSize, '', '', '',this.getCurrentPage())
        },
        //根据任务id删除任务
        handleDeleteById(tId) {
            ElMessageBox.confirm('确定是否要删除？该操作将无法回退', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '我再想想',
                type: 'warning'
            })
                .then(() => {
                    request.delete('/task/' + tId).then((response) => {
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
        //批量删除任务
        deleteBatchByIds() {
            const multiDeleteIds = []
            if (this.multiDeleteSelection.length > 0) {
                for (let i = 0; i < this.multiDeleteSelection.length; i++) {
                    multiDeleteIds.push(this.multiDeleteSelection[i].id)
                }
                ElMessageBox.confirm('确定是否要批量删除？该操作将无法回退', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '我再想想',
                    type: 'warning'
                })
                    .then(async () => {
                        await request.post('/task/batch', multiDeleteIds).then((response) => {
                            if (response.data.code === DATABASE_DELETE_OK) {
                                ElMessage({
                                    message: '删除成功.',
                                    type: 'success'
                                })
                                this.getLoading()
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
            } else {
                ElMessage({
                    message: '请至少选择一项进行删除',
                    type: 'warning'
                })
            }
        },
        //判断当前是否位于具体的迭代中
        getCurrentPage() {
            return localStorage.getItem('iterateId')===null?'':localStorage.getItem('iterateId')
        },
        //获得卡片数据
        getCardSelectData(){
            request.get('/task/cardLimit').then((response) => {
                this.cardSelectData = response.data.data
            })
        }
    }
})

