import { defineStore } from 'pinia';
import request from "@/util/request.js";
import {
    DATABASE_DELETE_ERROR,
    DATABASE_DELETE_OK, DATABASE_SAVE_ERROR, DATABASE_SAVE_OK,
    DATABASE_SELECT_OK, DATABASE_UPDATE_ERROR,
    DATABASE_UPDATE_OK
} from "@/constants/Common.constants.js";
import {ElMessage, ElMessageBox} from "element-plus";
export const useProjIterationStore=defineStore('projIteration',{
    state:()=>{
        return{
            total: 0,
            pageSize: 10,
            currentPage: 1,
            loading: false,
            dialogAddVisible: false,
            dialogEditVisible: false,
            editIteration:{},
            multiDeleteSelection: [],
            currentIteration:{},
            allIterationList:[],
            cardSelectData:[],
            iterationStatus: [
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
            selectData: []
        }
    },
    getters:{},
    actions: {
        //添加睡眠时间
        sleep(d) {
            return new Promise((resolve) => setTimeout(resolve, d))
        },
        //重置刷新数据
        async getLoading() {
            this.loading = true
            await this.sleep(300)
            await this.selectAllIteration(
                this.currentPage,
                this.pageSize,
                '',
                ''
            )
        },
        //根据当前项目id分页查询迭代
        selectAllIteration(currentPage, pageSize, name, status) {
            void request
                .get('/iteration/page', {
                    params: {
                        currentPage,
                        pageSize,
                        name,
                        status,
                        directorId:'',
                        projectId:localStorage.getItem('pid')
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
        //添加迭代
        handleAddIteration(addFormData) {
            request.post('/iteration/addIteration', addFormData).then((response) => {
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
        },
        //修改迭代
        handleUpdateIteration(updateIteration) {
            request.put('/iteration/updateIteration', updateIteration).then((response) => {
                if (response.data.code === DATABASE_UPDATE_OK) {
                    this.dialogEditVisible = false
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
        // 根据迭代id修改状态
        async updateStatusById(iteId, status) {
            await request
                .get('/iteration/updateStatus', {
                    params: {
                        iteId,
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
            this.selectAllIteration(this.currentPage, this.pageSize,'', '')
        },
        //根据迭代id删除迭代
        handleDeleteById(iteId) {
            ElMessageBox.confirm('确定是否要删除？该操作将无法回退', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '我再想想',
                type: 'warning'
            })
                .then(() => {
                    request.delete('/iteration/' + iteId).then((response) => {
                        if (response.data.code === DATABASE_DELETE_OK) {
                            ElMessage({
                                message: '删除成功.',
                                type: 'success'
                            })
                            this.getLoading().then(r => {})
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
        //批量删除迭代
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
                        await request.post('/iteration/batch', multiDeleteIds).then((response) => {
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
        //获得当前访问的迭代对象
        getCurrentIteration(iteId) {
            request.get('/iteration/'+iteId).then((response) => {
                this.currentIteration = response.data.data
            })
        },
        //获得所有迭代
        async getAllIteration(projectId) {
            await request.get('/iteration/getAllIteration',{params:{
                projectId
                }}).then((response) => {
                this.allIterationList = response.data.data
            })
        },
        //获得卡片数据
        getCardSelectData(){
            request.get('/iteration/cardLimit').then((response) => {
                this.cardSelectData = response.data.data
            })
        }
    }
})

