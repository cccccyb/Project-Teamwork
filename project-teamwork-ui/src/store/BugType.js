import { defineStore } from 'pinia';
import request from "@/util/request.js";
export const useBugTypeStore=defineStore('bugType',{
    state:()=>{
        return{
            allBugTypeList:[]
        }
    },
    getters:{},
    actions:{
        //获得所有缺陷类型
        async getAllBugType() {
            await request.get('/bugType/getAllBugType').then((response) => {
                this.allBugTypeList = response.data.data
            })
        },
    }
})

