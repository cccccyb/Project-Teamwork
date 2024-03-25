import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK, DATABASE_SAVE_ERROR, DATABASE_SAVE_OK,
    DATABASE_SELECT_OK, DATABASE_UPDATE_ERROR,
    DATABASE_UPDATE_OK
} from "@/constants/Common.constants.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useProjBugStore = defineStore('projBug', {
    state: () => {
        return {
            total: 0,
            pageSize: 10,
            currentPage: 1,
            loading: false,
            dialogAddBug: false,
            drawerVisible:false,
            multiDeleteSelection: [],
            clickBug:{},
            allBugList: [],
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
            bugPriority: [
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
            bugLevel: [
                {
                    id: 1,
                    name: '提示/建议'
                },
                {
                    id: 2,
                    name: '一般'
                },
                {
                    id: 3,
                    name: '严重'
                },
                {
                    id: 4,
                    name: '致命'
                }
            ],
            bugReappear: [
                {
                    id: 1,
                    name: '必然重现'
                },
                {
                    id: 2,
                    name: '随机重现'
                },
                {
                    id: 3,
                    name: '很难重现'
                }
            ],
            bugOrigins: [
                {
                    id: 1,
                    name: '用例测试'
                },
                {
                    id: 2,
                    name: '自动化测试'
                },
                {
                    id: 3,
                    name: '内部反馈'
                },
                {
                    id: 4,
                    name: '用户反馈'
                },
                {
                    id: 5,
                    name: 'Free Test'
                },
                {
                    id: 6,
                    name: '需求/建议'
                },
                {
                    id: 7,
                    name: '体验测试'
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
            await this.selectAllBug(
                this.currentPage,
                this.pageSize,
                '',
                '',
                '',
                this.getCurrentPage()
            )
        },
        //添加缺陷
        handleAddBug(addFormData) {
            request.post('/bug/addBug', addFormData).then((response) => {
                if (response.data.code === DATABASE_SAVE_OK) {
                    this.dialogAddBug = false
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
        //修改缺陷
        handleUpdateBug(updateBug) {
            request.put('/bug/updateBug', updateBug).then((response) => {
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
        //根据当前项目id分页查询缺陷
        selectAllBug(currentPage, pageSize, title, status, priority, discoveryIteId) {
            void request
                .get('/bug/page', {
                    params: {
                        currentPage,
                        pageSize,
                        title,
                        status,
                        priority,
                        creatorId: '',
                        processerId: '',
                        projectId: localStorage.getItem('pid'),
                        discoveryIteId
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
        // 根据缺陷id修改状态
        async updateStatusById(bugId, status) {
            await request
                .get('/bug/updateStatus', {
                    params: {
                        bugId,
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
            this.selectAllBug(this.currentPage, this.pageSize, '', '', '', this.getCurrentPage())
        },
        // 根据缺陷id修改优先级
        async updatePriorityById(bugId, priority) {
            await request
                .get('/bug/updatePriority', {
                    params: {
                        bugId,
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
            this.selectAllBug(this.currentPage, this.pageSize, '', '', '', this.getCurrentPage())
        },
        //根据缺陷id删除缺陷
        handleDeleteById(bugId) {
            ElMessageBox.confirm('确定是否要删除？该操作将无法回退', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '我再想想',
                type: 'warning'
            })
                .then(() => {
                    request.delete('/bug/' + bugId).then((response) => {
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
        //批量删除缺陷
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
                        await request.post('/bug/batch', multiDeleteIds).then((response) => {
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
            return localStorage.getItem('iterateId') === null ? '' : localStorage.getItem('iterateId')
        },
        //获得所有缺陷
        async getAllBug(projectId, requireId) {
            await request.get('/bug/getAllBug', {
                params: {
                    projectId,
                    requireId
                }
            }).then((response) => {
                this.allBugList = response.data.data
            })
        }
    }
})

