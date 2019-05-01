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
      <!--订单列表-->
      <el-table :data="employeeList" border style="width: 100%">
        <el-table-column prop="id" align="center" label="ID"></el-table-column>
        <el-table-column prop="username" align="center" label="雇员账号"></el-table-column>
        <el-table-column prop="password" align="center" label="雇主账号"></el-table-column>
        <el-table-column prop="rank" align="center" label="服务类型"></el-table-column>
        <el-table-column prop="orders" align="center" label="订单价格"></el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间" width="180px">
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" align="center" label="完成时间" width="180px">
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="praise" align="center" label="好评"></el-table-column>
        <el-table-column prop="state" align="center" label="状态">
          <template slot-scope="scope">
            <div v-if="scope.row.state === 1" style="color:#F56C6C;">{{'禁用'}}</div>
            <div v-if="scope.row.state === 2" style="color:#409EFF;">{{'待审核'}}</div>
            <div v-if="scope.row.state === 3" style="color:#E6A23C;">{{'待岗'}}</div>
            <div v-if="scope.row.state === 4" style="color:#67C23A;">{{'值岗'}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260px">
          <template slot-scope="scope">
            <at-button confirmText="确定启用此账号?" size="mini" type="success" v-if="scope.row.state===1||scope.row.state===2"
                       @click="changeState(scope.row,3)">启用
            </at-button>
            <at-button confirmText="确定停用此账号?" size="mini" type="danger" v-if="scope.row.state===3"
                       @click="changeState(scope.row,1)">禁用
            </at-button>
            <at-button confirmText="确定删除此账号?" size="mini" type="warning"
                       @click="deleteEmployee(scope.row)">删除
            </at-button>
            <at-button confirmText="确定通知两个账号?" size="mini" type="info"
                       @click="deleteEmployee(scope.row)">一键通知
            </at-button>
          </template>
        </el-table-column>
      </el-table>
      <!--更多资料-->
      <el-dialog width="500px"
                 title="更多资料"
                 :visible.sync="dialog_visible"
                 :close-on-click-modal="false">
        <el-form :model="moreInfo" size="small">
          <el-form-item label="账号" autocomplete="off"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.username" readonly></el-input>
          </el-form-item>
          <el-form-item label="姓名"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.name" readonly></el-input>
          </el-form-item>
          <el-form-item label="年龄"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.age" readonly></el-input>
          </el-form-item>
          <el-form-item label="性别"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.sex" readonly></el-input>
          </el-form-item>
          <el-form-item label="身份证"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.idCard" readonly></el-input>
          </el-form-item>
          <el-form-item label="电话"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.phone" readonly></el-input>
          </el-form-item>
          <el-form-item label="学历"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.education" readonly></el-input>
          </el-form-item>
          <el-form-item label="地址"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.address" readonly></el-input>
          </el-form-item>
          <el-form-item label="工作经验"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.experience" readonly></el-input>
          </el-form-item>
          <el-form-item label="自我介绍"
                        :label-width="formLabelWidth">
            <el-input v-model="moreInfo.introduction" type="textarea" readonly></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" style="margin-top: -30px">
          <el-button @click="dialog_visible=false">关闭</el-button>
        </div>
      </el-dialog>
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
