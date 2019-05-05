<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-form :model="searchForm" :inline="true">
          <el-form-item label="雇员账号" v-if="searchForm.role===100">
            <el-input v-model="searchForm.employeeUN" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="雇主账号" v-if="searchForm.role===100">
            <el-input v-model="searchForm.employerUN" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="searchForm.state" style="width: 90px">
              <el-option label="全部" :value=null></el-option>
              <el-option v-for="item in Object.entries(OrderState)"
                         :key="item[0]"
                         :label="item[1]"
                         :value="parseInt(item[0])"></el-option>
            </el-select>
          </el-form-item>
          <el-button @click="search" style="margin-left: 10px;" icon="el-icon-search" type="primary">搜索</el-button>
        </el-form>
      </el-row>
      <!--订单列表-->
      <el-table :data="orderList" border style="width: 100%">
        <el-table-column prop="id" align="center" label="ID"></el-table-column>
        <el-table-column prop="employeeUN" align="center" label="雇主账号" v-if="searchForm.role===100"></el-table-column>
        <el-table-column prop="employerUN" align="center" label="雇员账号" v-if="searchForm.role===100"></el-table-column>
        <el-table-column prop="employerUN" align="center" label="服务人员" v-if="searchForm.role===300"></el-table-column>
        <el-table-column prop="employerUN" align="center" label="雇主名字" v-if="searchForm.role===200"></el-table-column>
        <el-table-column prop="service" align="center" label="服务类型"></el-table-column>
        <el-table-column prop="rank" align="center" label="服务等级"></el-table-column>
        <el-table-column prop="orderPrice" align="center" label="订单价格"></el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间" width="180px">
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" align="center" label="完成时间" width="180px">
          <template slot-scope="scope">
            {{scope.row.endTime?$formatTime(scope.row.endTime):'尚未完成'}}
          </template>
        </el-table-column>
        <el-table-column prop="praise" align="center" label="好评"></el-table-column>
        <el-table-column prop="state" align="center" label="状态">
          <template slot-scope="scope">
            <div v-if="scope.row.state === 0" style="color:#F56C6C;">{{'待确认'}}</div>
            <div v-if="scope.row.state === 1" style="color:#909399;">{{'已取消'}}</div>
            <div v-if="scope.row.state === 2" style="color:#409EFF;">{{'已确认'}}</div>
            <div v-if="scope.row.state === 3" style="color:#E6A23C;">{{'待评论'}}</div>
            <div v-if="scope.row.state === 4" style="color:#67C23A;">{{'已评论'}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260px" align="center">
          <template slot-scope="scope">
            <at-button confirmText="确认此订单?" size="mini" type="success"
                       v-if="scope.row.state===0&&searchForm.role===200"
                       @click="changeState(scope.row,2)">确认
            </at-button>
            <!--取消进行时间限制-->
            <at-button confirmText="确定取消此订单?" size="mini" type="danger"
                       v-if="scope.row.state===0&&searchForm.role===300"
                       @click="changeState(scope.row,1)">取消
            </at-button>
            <at-button size="mini" type="danger"
                       v-if="scope.row.state===3&&searchForm.role===300"
            >评论
            </at-button>
            <at-button confirmText="确定删除此订单?" size="mini" type="warning"
                       v-if="scope.row.state===1&&searchForm.role===300"
                       @click="deleteOrder(scope.row)">删除
            </at-button>
            <at-button confirmText="确定通知两个账号?" size="mini" type="info"
                       v-if="scope.row.state===1&&searchForm.role===100"
                       @click="deleteEmployee(scope.row)">一键通知
            </at-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        orderList: [],
        searchForm: {
          userId:null,//当前登陆者的id
          employeeUN: '',
          employerUN: '',
          state: null,
          role: null
        },
      };
    },
    async created() {
      //接收参数
      this.searchForm.role = window.$mine.role;
      this.searchForm.userId = window.$mine.id;
      this.initQuery();
      this.init();
    },

    watch: {
      '$route'() {
        this.initQuery();
        this.init();
      }
    },
    methods: {
      initQuery() {
        Object.assign(this.searchForm, this.$route.query);
        this.searchForm.state = this.searchForm.state != null ? parseInt(this.searchForm.state) : null;
      },
      async init() {
        //查询所有的订单信息
        let res = await this.$api('Order/getAll', this.searchForm);
        this.orderList = res.data;
        // console.log(this.orderList)
      },
      search() {
        //携带查询的参数再次查询一下列表
        this.$router.push({
          path: "/manager/orders",
          query: this.searchForm
        });
      },
      async changeState(row, state) {
        let res = await this.$api('User/changeEmployeeState', {id: row.id, state: state});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
      async deleteOrder() {

      },
    }
  }
</script>

<style scoped>

</style>
