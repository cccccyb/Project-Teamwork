import { defineStore } from 'pinia';
import request from "@/util/request.js";
export const useDetailProjectStore=defineStore('detailProject',{
    state:()=>{
        return{
            isShowMenu:true,
            isCollapse:false,
            currentProject:{},
            currentPid:''
        }
    },
    getters:{},
    actions:{
    //获得当前访问的项目对象
        getCurrentProject(pid) {
           request.get('/project/'+pid).then((response) => {
                this.currentProject = response.data.data
            })
        }
    }
})

