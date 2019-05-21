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
              <el-option v-for="item in Object.entries(UserState)"
                         :key="item[0]"
                         :label="item[1]"
                         :value="parseInt(item[0])"></el-option>
            </el-select>
          </el-form-item>
          <el-button @click="search" style="margin-left: 10px;" icon="el-icon-search" type="primary">搜索</el-button>
        </el-form>
      </el-row>
      <!--家政人员列表-->
      <el-table :data="employeeList" border style="width: 100%">
        <el-table-column prop="id" align="center" label="ID"></el-table-column>
        <el-table-column prop="username" align="center" label="账号"></el-table-column>
        <el-table-column prop="password" align="center" label="密码"></el-table-column>
        <el-table-column prop="married" align="center" label="婚否">
          <template slot-scope="scope">
            <div v-if="scope.row.married === 0" style="color:#20a0ff">
              {{'是'}}
            </div>
            <div v-else>
              {{'否'}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="rank" align="center" label="拥有服务-等级" width="180px">
          <template slot-scope="scope">
            <el-tag size="mini" v-for="service in scope.row.services" :key="service.id"
                    @click="getRank(scope.row.id,service.id)">
              {{service.name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" align="center" label="有效订单"></el-table-column>
        <el-table-column prop="praise" align="center" label="好评率">
          <template slot-scope="scope">
            {{scope.row.totalComment?(scope.row.goodComment/(scope.row.totalComment)*100).toFixed(2):0}}%
          </template>
        </el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间" width="180px">
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="state" align="center" label="状态">
          <template slot-scope="scope">
            <div v-if="scope.row.state === 0" style="color:#5daf34;">{{'正常'}}</div>
            <div v-if="scope.row.state === 1" style="color:#F56C6C;">{{'禁用'}}</div>
            <div v-if="scope.row.state === 2" style="color:#409EFF;">{{'待审核'}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260px" align="center">
          <template slot-scope="scope">
            <at-button confirmText="确定启用此账号?" size="mini" type="success" v-if="scope.row.state===1||scope.row.state===2"
                       @click="changeState(scope.row,0)">启用
            </at-button>
            <at-button confirmText="确定停用此账号?" size="mini" type="danger" v-if="scope.row.state===0"
                       @click="changeState(scope.row,1)">禁用
            </at-button>
            <at-button confirmText="确定删除此账号?" size="mini" type="warning"
                       @click="deleteEmployee(scope.row)">删除
            </at-button>
            <el-dropdown size="mini" split-button type="info"
                         @command="handleCommand($event, scope.row.id,scope.row.role)"
                         style="margin-left: 5px">
              更多操作
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="showOrders">他的订单</el-dropdown-item>
                <el-dropdown-item command="showComment">他的评论</el-dropdown-item>
                <el-dropdown-item command="showFund">他的资金</el-dropdown-item>
                <el-dropdown-item command="moreInfo">他的资料</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <!--更多资料-->
      <el-dialog width="500px"
                 title="更多资料"
                 :visible.sync="dialog_visible"
                 :close-on-click-modal="true">
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
      Object.assign(this.searchForm, this.$route.query);
      this.searchForm.id = this.searchForm.id ? parseInt(this.searchForm.id) : null;
      this.searchForm.state = this.searchForm.state ? parseInt(this.searchForm.state) : null;
      this.init();
    },
    watch: {
      '$route'() {
        Object.assign(this.searchForm, this.$route.query);
        this.searchForm.id = this.searchForm.id ? parseInt(this.searchForm.id) : null;
        this.searchForm.state = this.searchForm.state ? parseInt(this.searchForm.state) : null;
        this.init();
      }
    },
    methods: {
      async init() {
        let res = await this.$api('getAll', this.searchForm);
        this.employeeList = res.list;
      },
      async search() {
        //携带查询的参数再次查询一下列表
        this.$router.push({
          path: "/manager/employeeList",
          query: this.searchForm
        });
      },
      handleCommand(command, id, role) {
        if (command === 'showOrders') {
          this.showOrders(id);
        } else if (command === 'showComment') {
          this.showComment(id, role);
        } else if (command === 'moreInfo') {
          this.showMoreInfo(id);
        } else if (command === 'showFund') {
          this.showFund(id);
        }
      },
      showOrders(id) {
        //跳转到订单列表页面
        this.$router.push({
          path: "/manager/orders",
          query: {eid: id}
        });
      },
      showComment(id, role) {
        //跳转到评论页面
        this.$router.push({
          path: "/manager/myComment",
          query: {eid: id, role: role}
        });
      },
      showFund(id) {
        //跳转到资金记录页面
        this.$router.push({
          path: "/manager/moneySta",
          query: {eid: id}
        });
      },
      showMoreInfo(id) {
        let tmp = this.employeeList.find(item => item.id === id);
        this.moreInfo.username = tmp.username;
        this.moreInfo.name = tmp.name;
        this.moreInfo.age = tmp.age;
        this.moreInfo.sex = tmp.sex;
        this.moreInfo.idCard = tmp.idCard;
        this.moreInfo.phone = tmp.phone;
        this.moreInfo.education = this.UserDegree[tmp.education];
        this.moreInfo.address = tmp.prov + '|' + tmp.city;
        this.moreInfo.experience = this.UserExperience[tmp.experience];
        this.moreInfo.introduction = tmp.introduction;
        this.moreInfo.photo = tmp.photo;
        this.dialog_visible = true;
      },
      async deleteEmployee(row) {
        let res = await this.$api('User/deleteEmployee', {id: row.id, role: row.role});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
      async getRank(userId, serviceId) {
        //根据员工id和服务id查询等级
        let res = await this.$api('Rank/findRankByUidAndSid', {userId: userId, serviceId: serviceId});
        alert("该人员在此服务中的等级为：" + res.data.name)
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
