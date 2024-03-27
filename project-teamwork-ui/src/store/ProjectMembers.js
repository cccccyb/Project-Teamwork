import { defineStore } from 'pinia';
import request from "@/util/request.js";
import {DATABASE_SELECT_OK} from "@/constants/Common.constants.js";
import {ElMessage} from "element-plus";
export const useProjectMembersStore=defineStore('projectMembers',{
    state:()=>{
        return{
            total: 0,
            pageSize: 10,
            currentPage: 1,
            loading: false,
            switchLoading:false,
            selectData: [],
            multiDeleteSelection: [],
        }
    },
    getters:{},
    actions:{
        //添加睡眠时间
        sleep(d) {
            return new Promise((resolve) => setTimeout(resolve, d))
        },
        //重置刷新数据
        async getLoading() {
            this.loading = true
            await this.sleep(300)
            await this.selectPageProjectMembers(
                this.currentPage,
                this.pageSize,
                ''
            )
        },
        //分页查询项目成员以及每个成员的用户组和角色及模糊查询
        selectPageProjectMembers(currentPage, pageSize, username) {
            void request
                .get('/project/selectPageProjectMembers', {
                    params: {
                        currentPage,
                        pageSize,
                        username,
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
    }
})

