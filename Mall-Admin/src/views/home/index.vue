<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img src="@/assets/logo.png" class="user-avator" alt />
                        <div class="user-info-cont">
                            <div class="user-info-name">{{ user.username }}</div>
                            <div>{{ role }}</div>
                        </div>
                    </div>
                    <!-- <div class="user-info-list">
                        上次登录时间：
                        <span>111</span>
                    </div>
                    <div class="user-info-list">
                        上次登录地点：
                        <span>111</span>
                    </div> -->
                </el-card>
            </el-col>
            <el-col :span="16">
                <!-- <el-row :gutter="20" class="mgb20">
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-1">
                                <i class="el-icon-lx-friendaddfill grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num" v-html="_data.fwl"></div>
                                    <div>用户访问量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-message-solid grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{_data.xxsum}}</div>
                                    <div>系统消息</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-lx-friend grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{_data.yhsum}}</div>
                                    <div>用户数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row> -->
                <div style="width:100%;height:403px">
                        <el-scrollbar wrap-style="overflow-x:hidden;" style="height:100%;width:100%">
                            
                <el-card shadow="hover" style="height:100%;">
                    
                    <template #header>
                        <div class="clearfix">
                            <span>待办事项</span>
                            <el-button style="float: right; padding: 3px 0" type="text" @click="addMsg">添加</el-button>
                        </div>
                    </template>
                            <el-table :show-header="false" :data="todoList" style="width:100%;height:100%">
                                
                                <el-table-column width="40">
                                    <template #default="scope">
                                        <el-checkbox v-model="scope.row.status"></el-checkbox>
                                    </template>
                                </el-table-column>
                                <el-table-column width="200">
                                    <template #default="scope">
                                        <div class="todo-item" :class="{
                                                'todo-item-del': scope.row.status,
                                            }">{{ scope.row.title }}</div>
                                    </template>
                                </el-table-column>
                                <el-table-column width="60">
                                    <template>
                                        <i class="el-icon-edit"></i>
                                        <i class="el-icon-delete"></i>
                                    </template>
                                </el-table-column>
                            </el-table>
                            
                </el-card>
                        </el-scrollbar>
                    </div>
            </el-col>
        </el-row>
        <el-dialog title="新建任务" v-model="editVisible" width="40%">
            <el-form label-width="100px">
                <el-form-item label="内容">
                    <el-input v-model="work.title"></el-input>
                </el-form-item>
                
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editVisible = false">取 消</el-button>
                    <el-button type="primary" @click="addinfo"
                        >确 定</el-button
                    >
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { onBeforeMount, onBeforeUpdate, onMounted, reactive,ref } from "vue";
import axios from 'axios';
export default {
    components: {  },
    
    setup() {
        
        const _data = ref({
            loalstate:"169.254.117.97",
            logtime:"2022-06-04 10:23:27",
            fwl:76,
            xxsum:0,
            yhsum:0
        })
        const work = ref({
                title: "",
                status: false,
            })
        const user = '' ;
        const role = "管理员";
        const editVisible = ref(false);
        const addMsg =() =>{
            editVisible.value = true;
        };
        const addinfo =() =>{
            todoList.unshift(work.value)
            editVisible.value = false;
        }
        const data = reactive([
            {
                name: "2018/09/04",
                value: 1083,
            },
            {
                name: "2018/09/05",
                value: 941,
            },
            {
                name: "2018/09/06",
                value: 1139,
            },
            {
                name: "2018/09/07",
                value: 816,
            },
            {
                name: "2018/09/08",
                value: 327,
            },
            {
                name: "2018/09/09",
                value: 228,
            },
            {
                name: "2018/09/10",
                value: 1065,
            },
        ]);
        const options = {
            type: "bar",
            title: {
                text: "最近一周各品类销售图",
            },
            xRorate: 25,
            labels: ["周一", "周二", "周三", "周四", "周五"],
            datasets: [
                {
                    label: "家电",
                    data: [234, 278, 270, 190, 230],
                },
                {
                    label: "百货",
                    data: [164, 178, 190, 135, 160],
                },
                {
                    label: "食品",
                    data: [144, 198, 150, 235, 120],
                },
            ],
        };
        const options2 = {
            type: "line",
            title: {
                text: "最近几个月各品类销售趋势图",
            },
            labels: ["6月", "7月", "8月", "9月", "10月"],
            datasets: [
                {
                    label: "家电",
                    data: [234, 278, 270, 190, 230],
                },
                {
                    label: "百货",
                    data: [164, 178, 150, 135, 160],
                },
                {
                    label: "食品",
                    data: [74, 118, 200, 235, 90],
                },
            ],
        };
        const todoList = reactive([
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: true,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: true,
            },{
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: true,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: true,
            },
        ]);

        return {
            _data,
            user,
            data,
            work,
            options,
            options2,
            todoList,
            role,
            editVisible,
            addMsg,
            addinfo
        };
    },
};
</script>

<style scoped>
.el-row {
    margin-bottom: 20px;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}

.schart {
    width: 100%;
    height: 300px;
}
</style>
