<template>
  <div>
    <el-row class="margin-bottom">
      <el-form :inline="true" :model="searchParam">
        <el-form-item label="我的服务">
          <el-select v-model="searchParam.serviceId" placeholder="请选择">
            <el-option label="全部" :value=null></el-option>
            <el-option
              v-for="item in myServices"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="开始时间">
          <el-date-picker
            style="width: 200px"
            v-model="searchParam.startTime"
            type="date"
            value-format="timestamp"
            placeholder="选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            style="width: 200px"
            v-model="searchParam.endTime"
            value-format="timestamp"
            type="date"
            placeholder="选择时间">
          </el-date-picker>
        </el-form-item>
        <el-button @click="search"
                   icon="el-icon-search"
                   type="primary">搜索
        </el-button>
      </el-form>
    </el-row>
    <div style="width: 100%;text-align: center;font-size: 30px;" v-if="loading"><i
      class="el-icon-loading"></i>加载中,请稍后...
    </div>
    <div id="serviceContainer" v-show="!loading" style="width:100%;border: 1px solid #9BCD9B"></div>
    <div class="statistics_title">
      <span class="barTitle">服务详情★×**×★☆×**×★☆×**×★☆×**×★☆×**×★☆×**×★★×**×★☆×**×★☆×**×★☆×**×★☆×**×★☆×**×★★×**×★☆×**×★☆×**×★☆×**×★☆×**×★☆×**×★</span>
    </div>
    <div style="margin-top: 6%">
      <el-table :data="services" border v-loading="loading">
        <el-table-column prop="id" label="序号" align="center" type="index" width="120px"></el-table-column>
        <el-table-column prop="name" label="服务" align="center"></el-table-column>
        <el-table-column prop="price" label="价格" align="center">
          <template slot-scope="scope">
            {{scope.row.price+'元/'+scope.row.type}}
          </template>
        </el-table-column>
        <el-table-column prop="orderPrice" label="订单价" align="center"></el-table-column>
        <el-table-column prop="employeeName" label="服务人员" align="center" v-if="role===300"></el-table-column>
        <el-table-column prop="employerName" label="下单人员" align="center" v-if="role===200"></el-table-column>

        <el-table-column prop="endTime" label="订单完成时间" align="center">
          <template slot-scope="scope">
            {{$formatTime(scope.row.endTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="rate" label="评价/星" align="center">
          <template slot-scope="scope">
            {{scope.row.rate!=null?scope.row.rate:'无'}}
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="width:100%;height: 100px;float: left">
    </div>
  </div>
</template>

<script>
  import Highcharts from 'highcharts/highstock';

  export default {
    name: "myServiceSta",
    data() {
      return {
        loading: true,
        searchParam: {
          serviceId: null,
          startTime: this.timestamp() * 1000 - (1000 * 60 * 60 * 24) * 6,
          endTime: this.timestamp() * 1000,
        },
        timeList: [],
        serviceList: [],
        moneyList: [],
        services: [],
        myServices: [],//下拉框所选服务
        role: null,
      }
    },
    methods: {
      async search() {
        this.$router.push({
          path: this.$route.path,
          query: this.searchParam
        });
      },
      initQuery() {
        Object.assign(this.searchParam, this.$route.query);
        this.searchParam.serviceId = this.searchParam.serviceId ? parseInt(this.searchParam.serviceId) : null;
        this.searchParam.startTime = this.searchParam.startTime ? parseInt(this.searchParam.startTime) : null;
        this.searchParam.endTime = this.searchParam.endTime ? parseInt(this.searchParam.endTime) : null;
      },
      async getServiceSta() {
        this.loading = true;
        this.searchParam.startTime = new Date(this.searchParam.startTime).setHours(0, 0, 0, 0);// 当天0点
        this.searchParam.endTime = new Date(this.searchParam.endTime).setHours(23, 59, 59, 0);//当天23；59
        let res = await this.$api("Static/serviceChartByEid", {
          serviceId: this.searchParam.serviceId,
          startTime: this.searchParam.startTime,
          endTime: this.searchParam.endTime,
          eid: window.$mine.id,
          role: window.$mine.role,
        });
        this.loading = false;
        this.timeList = res.timeList;
        this.serviceList = res.serviceList;
        this.moneyList = res.moneyList;
        Highcharts.chart('serviceContainer', {
          credits: {//去除生成图上面的网址logo
            enabled: false
          },
          chart: {
            zoomType: 'xy'
          },
          title: {
            text: '服务统计'
          },
          subtitle: {//小标题
          },
          xAxis: [{
            categories: this.timeList,
            crosshair: true
          }],
          yAxis: [{ // Primary yAxis
            labels: {
              format: '{value}单',
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            title: {
              text: "成单数量",
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            allowDecimals: false
            // tickAmount: 5,
          }, { // Secondary yAxis
            title: {
              text: "营业额",
              style: {
                color: Highcharts.getOptions().colors[0]
              }
            },
            allowDecimals: false,
            // tickAmount: 5,
            labels: {
              format: '{value} 元',
              style: {
                color: Highcharts.getOptions().colors[0]
              }
            },
            opposite: true
          },],
          tooltip: {
            shared: true
          },
          legend: {
            layout: 'horizontal',
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || 'rgba(255,255,255,0.25)'
          },
          series: [{
            name: '服务成单',
            type: 'column',
            data: this.serviceList,
            tooltip: {
              valueSuffix: '单'
            },
          }, {
            name: this.$role === 200 ? '服务支出' : '服务收入',
            type: 'spline',
            yAxis: 1,
            data: this.moneyList,
            tooltip: {
              valueSuffix: '元'
            },
          }]
        });
      },
      async getServiceDetailByEid() {
        let res = await this.$api('Static/getServiceDetailByEid', {
          serviceId: this.searchParam.serviceId,
          startTime: this.searchParam.startTime / 1000,
          endTime: this.searchParam.endTime / 1000,
          eid: window.$mine.id,
          role: window.$mine.role,
        });
        this.services = res.data;
      },
      async getMyService() {
        this.role = window.$mine.role;
        if (this.role === 200) {
          //当前登陆的是雇员，查询其擅长的服务集合
          let res = await this.$api('Service/getServiceByUserId', {id: window.$mine.id});
          this.myServices = res.data;
        } else if (this.role === 300) {
          //当前登陆的是雇主，查询订单包含的服务
          this.myServices = await this.$api('Service/getServiceByUserOrder', {employerId: window.$mine.id});
        }
      },
    },
    async created() {
      //初始化查询条件
      this.initQuery();
      //获取当前登陆者拥有的服务
      await this.getMyService();
      await this.getServiceSta();
      await this.getServiceDetailByEid();
    },
    watch: {
      async '$route'() {
        //初始化查询条件
        this.initQuery();
        this.getServiceSta();
        await this.getServiceDetailByEid();
      }
    },
  }
</script>

<style scoped>
  .statistics_title {
    width: 100%;
    height: 30px;
    font-size: medium;
    margin-top: 30px;
    float: left;
    background-color: #F2F6FC;
  }

  .barTitle {
    margin-left: 20px;
    float: left;
    margin-top: 0.5%
  }
</style>
