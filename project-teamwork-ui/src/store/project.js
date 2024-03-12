import {defineStore} from 'pinia';
import request from "@/util/request.js";
import {ElMessage} from "element-plus";
import {DATABASE_SELECT_OK} from "@/constants/Common.constants.js";

export const useProjectStore = defineStore('project', {
    state: () => ({
        total: 0,
        pageSize: 10,
        currentPage: 1,
        loading: true,
        showLoading: true,
        dialogShowVisible: false,
        dialogAddVisible: false,
        dialogEditVisible: false,
        editFlag: false,
        searchNav: {
            name: '',
            status: 0,
            startTime: '',
            endTime: ''
        },
        selectData: []
    }),
    getters: {},
    actions: {
        // getLoading(search) {
        //     this.selectAllNotice(
        //         this.currentPage,
        //         this.pageSize,
        //         search.name,
        //         search.status,
        //         search.startTime,
        //         search.endTime,
        //         search.creatorId
        //     )
        // },
        selectAllProject(currentPage, pageSize, name, status, startTime, endTime, creatorId) {
            void request
                .get('/project/page', {
                    params: {
                        currentPage,
                        pageSize,
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


