import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK, DATABASE_SAVE_ERROR, DATABASE_SAVE_OK,
    DATABASE_SELECT_OK,
    DATABASE_UPDATE_OK
} from "@/constants/Common.constants.js";

export const useProjectStore = defineStore('project', {
    state: () => ({
        total: 0,
        pageSize: 10,
        currentPage: 1,
        loading: true,
        dialogAddVisible: false,
        currentViewPage: 'All',
        multiDeleteSelection: [],
        projectStatus: [
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
        searchNav: {
            name: '',
            status: '',
            startTime: '',
            endTime: ''
        },
        selectData: [
            {
                creator: {
                    id: '',
                    username: ''
                },
                creatorId: '',
                deleted: 0,
                description: '',
                endTime: '',
                id: '',
                members: [],
                name: '',
                startTime: '',
                status: 0,
                version: 0

            }
        ]
    }),
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
            await this.selectAllProject(
                this.currentPage,
                this.pageSize,
                this.getCurrentViewPage(),
                '',
                '',
                '',
                '',
                this.getCurrentViewPage()
            )
        },
        //分页查询项目
        selectAllProject(currentPage, pageSize,userId, name, status, startTime, endTime, creatorId) {
            void request
                .get('/project/page', {
                    params: {
                        currentPage,
                        pageSize,
                        userId,
                        name,
                        status,
                        startTime,
                        endTime,
                        creatorId
                    }
                })
                .then((response) => {
                    if (response.data.code === DATABASE_SELECT_OK) {
                        this.selectData = response.data.data
                        for (let i = 0; i < this.selectData.length; i++) {
                            let data = this.selectData[i];
                            data.status = parseInt(data.status) === 0 ? '未开始' : parseInt(data.status) === 1 ? '进行中' : '已完成'
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
        // 根据项目id修改状态
        async updateStatusById(pid, status) {
            await request
                .get('/project/updateStatus', {
                    params: {
                        pid,
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
            this.selectAllProject(this.currentPage, this.pageSize, this.getCurrentViewPage(),'', '', '', '',  this.getCurrentViewPage())
        },
        //根据项目id删除项目
        handleDeleteById(pid) {
            ElMessageBox.confirm('确定是否要删除？该操作将无法回退', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '我再想想',
                type: 'warning'
            })
                .then(() => {
                    request.delete('/project/' + pid).then((response) => {
                        if (response.data.code === DATABASE_DELETE_OK) {
                            ElMessage({
                                message: '删除成功.',
                                type: 'success'
                            })
                            this.selectAllProject(
                                this.currentPage,
                                this.pageSize,
                                this.getCurrentViewPage(),
                                '',
                                '',
                                '',
                                '',
                                this.getCurrentViewPage()
                            )
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
        //批量删除项目
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
                        await request.post('/project/batch', multiDeleteIds).then((response) => {
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
        //判断当前位于哪一个子页面
        getCurrentViewPage() {
            let currentUser = ''
            if (this.currentViewPage === 'All') {
                currentUser = ''
            } else if (this.currentViewPage === 'MyManage') {
                currentUser = localStorage.getItem('uid') ? localStorage.getItem('uid') : ''
            } else if (this.currentViewPage === 'Person') {
                currentUser = localStorage.getItem('uid') ? localStorage.getItem('uid') : ''
            }
            return currentUser
        },
        //添加项目
        handleAddProject(addFormData) {
            request.post('/project/addProject', addFormData).then((response) => {
                if (response.data.code === DATABASE_SAVE_OK) {
                    this.dialogAddVisible = false
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
            this.getLoading().then(r => {})
        },
    }
})


