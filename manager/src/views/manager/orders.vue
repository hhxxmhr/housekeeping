<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-form :model="searchForm" :inline="true">
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
      <el-table :data="orderList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" align="center" label="ID" width="60px" type="index"></el-table-column>
        <el-table-column prop="id" align="center" label="ID" width="60px" v-if="searchForm.role===100"></el-table-column>
        <el-table-column prop="employerUsername" align="center" label="雇主账号"
                         v-if="searchForm.role===100"></el-table-column>
        <el-table-column prop="employeeUsername" align="center" label="雇员账号"
                         v-if="searchForm.role===100"></el-table-column>
        <el-table-column prop="employeeName" align="center" label="服务人员" v-if="searchForm.role===300"></el-table-column>
        <el-table-column prop="employeePhone" align="center" label="联系电话"
                         v-if="searchForm.role===300"></el-table-column>
        <el-table-column prop="employerName" align="center" label="雇主名字" v-if="searchForm.role===200"></el-table-column>
        <el-table-column prop="employerPhone" align="center" label="联系电话"
                         v-if="searchForm.role===200"></el-table-column>
        <el-table-column prop="serviceName" align="center" label="服务类型"></el-table-column>
        <el-table-column prop="rankName" align="center" label="等级" width="70px"></el-table-column>
        <el-table-column prop="tip" align="center" label="服务备注" v-if="searchForm.role!==100">
          <template slot-scope="scope">
            {{scope.row.tip?scope.row.tip:'无'}}
          </template>
        </el-table-column>
        <el-table-column prop="address" align="center" label="服务地址" width="165px"></el-table-column>
        <el-table-column prop="orderPrice" align="center" label="价格" width="70px"></el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间" width="160px" sortable>
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="reverseTime" align="center" label="上门时间" width="160px">
          <template slot-scope="scope">
            {{$formatTime(scope.row.reverseTime)}}
          </template>
        </el-table-column>
        <!--<el-table-column prop="endTime" align="center" label="完成时间" width="160px">
          <template slot-scope="scope">
            {{scope.row.endTime?$formatTime(scope.row.endTime):'尚未完成'}}
          </template>
        </el-table-column>-->
        <el-table-column prop="praise" align="center" label="好评" width="60px">
          <template slot-scope="scope">
            {{scope.row.rate?checkRate(scope.row.rate):'待评'}}
          </template>
        </el-table-column>
        <el-table-column prop="state" align="center" label="状态">
          <template slot-scope="scope">
            <div v-if="scope.row.state === 0" style="color:#F56C6C;">{{'待确认'}}</div>
            <div v-if="scope.row.state === 1" style="color:#909399;">{{'已取消'}}</div>
            <div v-if="scope.row.state === 2" style="color:#409EFF;">{{'已确认'}}</div>
            <div v-if="scope.row.state === 3" style="color:#E6A23C;">{{'待评论'}}</div>
            <div v-if="scope.row.state === 4" style="color:#67C23A;">{{'已评论'}}</div>
            <div v-if="scope.row.state === 5" style="color:#8461B3;">{{'退款中'}}</div>
            <div v-if="scope.row.state === 6" style="color:#1B2023;">{{'已退款'}}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <at-button confirmText="确认此订单?" size="mini" type="success"
                       v-if="scope.row.state===0&&searchForm.role===200"
                       @click="changeState(scope.row,2)">确认
            </at-button>
            <at-button confirmText="已经完成此订单?" size="mini" type="primary"
                       style="border-color:lightsalmon ;background-color: lightsalmon"
                       v-if="scope.row.state===2&&searchForm.role===200"
                       @click="changeState(scope.row,3)">完成
            </at-button>
            <at-button confirmText="确定同意此退款申请?" size="mini" type="primary"
                       v-if="scope.row.state===5&&searchForm.role===100"
                       @click="changeState(scope.row,6)">同意
            </at-button>
            <at-button confirmText="确定拒绝此退款申请?" size="mini" type="warning"
                       v-if="scope.row.state===5&&searchForm.role===100"
                       @click="changeState(scope.row,3)">拒绝
            </at-button>
            <at-button size="mini" type="danger"
                       v-if="(scope.row.state===0||scope.row.state===2)&&searchForm.role===300"
                       @click="changeState(scope.row,1)">取消
            </at-button>
            <at-button size="mini" type="primary"
                       v-if="scope.row.state===3&&searchForm.role===300"
                       @click="comment(scope.row.id)">评论
            </at-button>
            <at-button size="mini" type="primary" style="background-color: #f78989;border-color: #f78989"
                       v-if="scope.row.state===3&&searchForm.role===300"
                       @click="refundDialog(scope.row)">退款
            </at-button>
            <at-button confirmText="确定删除此订单?" size="mini" type="warning"
                       v-if="scope.row.state===1&&searchForm.role===300"
                       @click="deleteOrder(scope.row.id)">删除
            </at-button>
            <!--<at-button confirmText="确定通知两个账号?" size="mini" type="info"
                       v-if="scope.row.state===1&&searchForm.role===100"
                       @click="deleteEmployee(scope.row)">一键通知
            </at-button>-->
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
        title="申请退款"
        :visible.sync="dialogVisible"
        width="400px"
        :before-close="close_dialog"
        :close-on-click-modal="false">
        <el-form :model="refund" :rules="rules" ref="refund">
          <el-form-item label="描述理由" prop="reason" label-width="80px">
            <el-input type="textarea" v-model="refund.reason" style="width: 100%" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="退款金额" prop="money" label-width="80px">
            {{refund.orderPrice}}元
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="close_dialog">取 消</el-button>
    <el-button type="primary" @click="refundApply">确 定</el-button>
  </span>
      </el-dialog>

      <div style="text-align: right ;margin-top: 10px">
        <el-pagination style="margin: auto"
                       background
                       :current-page="searchForm.page"
                       :page-size="searchForm.size"
                       :page-sizes="[15,30,45]"
                       layout="sizes, prev, pager, next, jumper"
                       :total="total"
                       @current-change="currentChange"
                       @size-change="handleSizeChange">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        refund: {
          reason: '',
          id: null,
          state: 5,
          orderPrice: null,
        },
        rules: {
          reason: [
            {required: true, message: "请描述理由", trigger: "blur"},
          ],
        },
        loading: true,
        tips: '',
        orderList: [],
        total: 0,
        searchForm: {
          page: 1,//当前页
          size: 15,//每页记录的数目
          eid: null,//雇员列表或者雇主列表 更多操作跳转过来携带的参数
          userId: null,//当前登陆者的id
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
        this.searchForm.eid = this.searchForm.eid != null ? parseInt(this.searchForm.eid) : null;
        this.searchForm.page = this.searchForm.page ? parseInt(this.searchForm.page) : 1;
        this.searchForm.size = this.searchForm.size ? parseInt(this.searchForm.size) : 15;
      },
      async init() {
        this.loading = true;
        //查询所有的订单信息
        let res = await this.$api('Order/getAll', this.searchForm);
        this.loading = false;
        this.orderList = res.list;
        this.total = res.total;
      },
      search() {
        this.searchForm.page = 1;
        this.searchForm.size = 15;
        //携带查询的参数再次查询一下列表
        this.$router.push({
          path: "/manager/orders",
          query: this.searchForm
        });
      },
      comment(orderId) {
        //跳转到评价页面
        this.$router.push({
          path: "/manager/doComment",
          query: {orderId: orderId}
        });

      },
      //申请退款dialog
      refundDialog(row) {
        this.refund.orderPrice = row.orderPrice;
        this.refund.id = row.id;
        this.dialogVisible = true;
      },
      close_dialog() {
        this.dialogVisible = false;
        setTimeout(() => {
          this.$refs["refund"].resetFields();
        }, 200);
      },
      async refundApply() {
        //确定申请退款--退款中
        let res = await this.$api("Order/edit", this.refund);
        if (res.code === 200) {
          this.$notify.info({
            title: '提交成功',
            message: '请等待审核'
          });
          this.close_dialog();
          await this.init();
        }
      },
      checkRate(rate) {
        return rate > 2 ? '是' : '否';
      },
      async changeState(row, state) {
        let isTimeOut = this.checkTime(row.createTime);
        if (this.searchForm.role === 300) {
          if (isTimeOut) {
            //提示要收取违约费
            this.$confirm('已超出2小时，若继续取消将收取此单的50%即' + row.orderPrice * 0.05 + '元，确定继续取消订单吗', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(async () => {
              let res = await this.$api("Order/edit", {id: row.id, state: state, pay: row.orderPrice * 0.5});
              this.$message({
                type: res.code === 200 ? 'success' : 'error',
                message: res.msg
              });
              this.init();
            }).catch(() => {
              this.$message.warning("取消操作")
            });
          } else {
            this.$confirm('确定取消订单吗', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(async () => {
              let res = await this.$api("Order/edit", {id: row.id, state: state});
              this.$message({
                type: res.code === 200 ? 'success' : 'error',
                message: res.msg
              });
              this.init();
            }).catch(() => {
              this.$message.warning("取消操作")
            });
          }
        } else if (this.searchForm.role === 200) {
          if (row.state === 2) {
            //state员工完成的时间是订单完成的时间
            let res = await this.$api("Order/edit", {id: row.id, state: state, endTime: this.timestamp()});
            this.$message({
              type: res.code === 200 ? 'success' : 'error',
              message: res.msg
            });
          } else {
            let res = await this.$api("Order/edit", {id: row.id, state: state});
            this.$message({
              type: res.code === 200 ? 'success' : 'error',
              message: res.msg
            });
          }
          this.init();
        } else {//role==100,管理员的同意退款操作
          let res = await this.$api("Order/edit", {id: row.id, state: state});
          this.$message({
            type: res.code === 200 ? 'success' : 'error',
            message: res.msg
          });
          this.init();
        }
      },
      checkTime(createTime) {
        //校验时间---在下完单之后的两个小时之内是可以取消订单的
        return this.timestamp() - createTime > 2 * 60 * 60;
      },
      async currentChange(current) {
        this.searchForm.page = current;
        await this.init();
      },
      async handleSizeChange(val) {
        this.searchForm.size = val;
        // await this.init();
        await this.currentChange(1);
      },
      async deleteOrder(id) {
        let res = await this.$api("Order/delete", {id: id});
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
