import { defineStore } from 'pinia';
export const useSettingStore=defineStore('setting',{
    state:()=>{
        return{
            isShowMenu:true,
            isCollapse:false
        }
    },
    getters:{},
    actions:{
    }
})

