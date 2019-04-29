<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-form :model="searchForm" :inline="true">
          <el-form-item label="ID">
            <el-input v-model.number="searchForm.id" placeholder="请输入id"></el-input>
          </el-form-item>
          <el-form-item label="账号">
            <el-input v-model="searchForm.username" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.state" style="width: 90px">
              <el-option label="全部" :value=null></el-option>
              <el-option v-for="item in Object.entries(EmployeeState)"
                         :key="item[0]"
                         :label="item[1]"
                         :value="parseInt(item[0])"></el-option>
            </el-select>
          </el-form-item>
          <el-button @click="search" style="margin-left: 10px;" icon="el-icon-search" type="primary">搜索</el-button>
        </el-form>
      </el-row>
      <!--评论列表-->
      <el-table :data="employeeList" border style="width: 100%">
        <el-table-column prop="id" align="center" label="ID"></el-table-column>
        <el-table-column prop="username" align="center" label="订单ID"></el-table-column>
        <el-table-column prop="rank" align="center" label="订单内容"></el-table-column>
        <el-table-column prop="password" align="center" label="雇主账号"></el-table-column>
        <el-table-column prop="married" align="center" label="雇员账号" ></el-table-column>
        <el-table-column prop="praise" align="center" label="好评"></el-table-column>
        <el-table-column prop="praise" align="center" label="评论内容"></el-table-column>
        <el-table-column prop="createTime" align="center" label="评论时间" width="180px">
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <at-button confirmText="确定删除此账号?" size="mini" type="warning"
                       @click="deleteEmployee(scope.row)">删除
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
        searchForm: {
          id: null,
          username: '',
          state: null,
          role: 200
        },
        formLabelWidth: "70px",
        rechargeVisible: false,
        dialog_visible: false,
        moreInfo: {
          username: '',
          name: '',
          age: '',
          sex: '',
          idCard: '',
          education: '',
          experience: '',
          address: '',
          phone: '',
          introduction: '',
          photo: '',
        },
        employeeList: [],
      };
    },
    async created() {
      //接收参数
      let query = this.$route.query;
      let id = query.id == null ? null : parseInt(query.id);
      let username = query.username;
      let state = query.state == null ? null : parseInt(query.state);
      this.searchForm.id = id;
      this.searchForm.username = username;
      this.searchForm.state = state;
      this.init();
    },
    watch: {
      '$route'() {
        let query = this.$route.query;
        let id = query.id == null ? null : parseInt(query.id);
        let username = query.username;
        let state = query.state == null ? null : parseInt(query.state);
        this.searchForm.id = id;
        this.searchForm.username = username;
        this.searchForm.state = state;
        this.init();
      }
    },
    methods: {
      async init() {
        let res = await this.$api('getAll', this.searchForm);
        this.employeeList = res.list;
      },
      search() {
        //携带查询的参数再次查询一下列表
        this.$router.push({
          path: "/manager/employeeList",
          query: this.searchForm
        });
      },
      async deleteEmployee(row) {
        let res = await this.$api('User/deleteEmployee', {id: row.id});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
      async changeState(row, state) {
        let res = await this.$api('User/changeEmployeeState', {id: row.id, state: state});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
    }
  }
</script>

<style scoped>

</style>
