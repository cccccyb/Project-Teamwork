import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK, DATABASE_SAVE_ERROR, DATABASE_SAVE_OK,
    DATABASE_SELECT_OK, DATABASE_UPDATE_ERROR,
    DATABASE_UPDATE_OK
} from "@/constants/Common.constants.js";
import {ElMessage, ElMessageBox} from "element-plus";

export const useProjRequirementStore = defineStore('projRequirement', {
    state: () => {
        return {
            total: 0,
            pageSize: 10,
            currentPage: 1,
            loading: false,
            dialogAddRequire: false,
            drawerVisible:false,
            clickRequirement:{},
            allRequirementList:[],
            relationBugsList:[],
            multiDeleteSelection: [],
            requireStatus: [
                {
                    id: 0,
                    name: '待处理'
                },
                {
                    id: 1,
                    name: '待评审'
                },
                {
                    id: 2,
                    name: '开发中'
                },
                {
                    id: 3,
                    name: '待验收'
                },
                {
                    id: 4,
                    name: '验收中'
                },
                {
                    id: 5,
                    name: '待测试'
                },
                {
                    id: 6,
                    name: '变更'
                },
                {
                    id: 7,
                    name: '已完成'
                },
            ],
            requirePriority: [
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
            requireOrigins: [
                {
                    id: 1,
                    name: 'PM'
                },
                {
                    id: 2,
                    name: 'NPI新产品导入'
                },
                {
                    id: 3,
                    name: 'RD需求开发'
                },
                {
                    id: 4,
                    name: 'QA'
                },
                {
                    id: 5,
                    name: 'CED'
                },
                {
                    id: 6,
                    name: '用户反馈'
                },
                {
                    id: 7,
                    name: '缺陷转需求'
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
            await this.selectAllRequirement(
                this.currentPage,
                this.pageSize,
                '',
                '',
                '',
                this.getCurrentPage()
            )
        },
        //添加需求
        handleAddRequire(addFormData) {
            request.post('/requirement/addRequirement', addFormData).then((response) => {
                if (response.data.code === DATABASE_SAVE_OK) {
                    this.dialogAddRequire = false
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
        //修改需求
        handleUpdateRequire(updateRequire) {
            request.put('/requirement/updateRequirement', updateRequire).then((response) => {
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
            this.getLoading().then(r => {})
        },
        //根据当前项目id分页查询需求
        selectAllRequirement(currentPage, pageSize, title, status, priority,iterationId) {
            void request
                .get('/requirement/page', {
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
        // 根据需求id修改状态
        async updateStatusById(reqId, status) {
            await request
                .get('/requirement/updateStatus', {
                    params: {
                        reqId,
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
            this.selectAllRequirement(this.currentPage, this.pageSize, '', '', '',this.getCurrentPage())
        },
        // 根据需求id修改优先级
        async updatePriorityById(reqId, priority) {
            await request
                .get('/requirement/updatePriority', {
                    params: {
                        reqId,
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
            this.selectAllRequirement(this.currentPage, this.pageSize, '', '', '',this.getCurrentPage())
        },
        //根据需求id删除需求
        handleDeleteById(reqId) {
            ElMessageBox.confirm('确定是否要删除？该操作将无法回退', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '我再想想',
                type: 'warning'
            })
                .then(() => {
                    request.delete('/requirement/' + reqId).then((response) => {
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
        //批量删除需求
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
                        await request.post('/requirement/batch', multiDeleteIds).then((response) => {
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
        //根据需求id查所关联的缺陷
        selectRelationBugById(){
           request.get('/bug/getRelationBugs',{params:{
                   requireId:this.clickRequirement.id
                }}).then((response) => {
                this.relationBugsList = response.data.data
            })
        },
        //判断当前是否位于具体的迭代中
        getCurrentPage() {
            return localStorage.getItem('iterateId')===null?'':localStorage.getItem('iterateId')
        },
        //获得所有需求
        async getAllRequirement(projectId) {
            await request.get('/requirement/getAllRequirement',{params:{
                    projectId
                }}).then((response) => {
                this.allRequirementList = response.data.data
            })
        },
        //根据需求id添加关联缺陷
        addRelationBugById(bugsIdList) {
            request.post('/bug/addRelationBugs/'+this.clickRequirement.id, bugsIdList
                ).then((response) => {
                if (response.data.code === DATABASE_SAVE_OK) {
                    this.dialogAddRequire = false
                    ElMessage({
                        message: response.data.msg,
                        type: 'success'
                    })
                    this.selectRelationBugById()
                } else if (response.data.code === DATABASE_SAVE_ERROR) {
                    ElMessage({
                        message: response.data.msg,
                        type: 'error'
                    })
                }
            })
        }
    }
})

