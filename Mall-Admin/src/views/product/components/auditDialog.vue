<template>
      <el-dialog
      model-value="auditedt"
      title="审核意见"
      width="50%"
      @close="handleClose"
  >
  <div style="display: flex; justify-content: center;">
    
  <textarea name="" id="" cols="80" rows="10" v-model="form.auditMsg" style="border:1px grey solid;" placeholder="请输入内容"></textarea>
  </div>
  <template #footer>
      <span class="dialog-footer">
        <el-button  @click="handleClose">取消</el-button>
     <el-button type="primary" @click="handleConfirm">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>

import {defineEmits, defineProps, ref, watch} from "vue";
import axios from "@/util/axios";
import { ElMessage } from 'element-plus'

const props=defineProps(
    {
      id:{
        type:Number,
        default:-1,
        required:true
      },
      dialogVisible:{
        type:Boolean,
        default:false,
        required:true
      },
      proaudit:Object
    }
)
const form=ref({
  id:-1,
  productId:props.id,
  userId:props.proaudit.userId,
  isAudit:props.proaudit.isAudit,
  productName:props.proaudit.productName,
  auditMsg:null
})
const initauditSelect=async()=>{
    const res=await axios.get("/admin/product/audit"+props.id);
    form.value.id = res.data['id'];
    form.value.auditMsg = res.data['auditMsg'];
    form.value.productId = res.data['productId'];
}
initauditSelect()
watch(
    ()=>{
      let id=props.id;
        if(id!=-1){
            initauditSelect();
        }
    }
      
)
const emits=defineEmits(['update:modelValue','initProductList'])
const handleClose=()=>{
  emits('update:modelValue',false)
}
const handleConfirm=()=>{
    console.log(form.value)
        const res=axios.post("/admin/product/auditsave",{
            id:form.value.id,
            productId:props.id,
            userId:props.proaudit.userId,
            isAudit:props.proaudit.isAudit,
            productName:props.proaudit.productName,
            auditMsg:form.value.auditMsg
        });
        handleClose();
      
}
</script>

<style scoped>

</style>