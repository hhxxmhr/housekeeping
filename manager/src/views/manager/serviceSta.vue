<template>
  <div>
    <el-row class="margin-bottom">
      <el-form :inline="true" :model="searchParam">
        <el-form-item label="服务ID">
          <el-input v-model="searchParam.serviceId" placeholder="请输入服务ID" style="width: 120px"></el-input>
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
      <span class="barTitle">服务详情</span>
    </div>
    <div style="margin-top: 6%">
      <el-table :data="services"
                border
                v-loading="loading">
        <el-table-column prop="id" label="服务ID" align="center" width="80px"></el-table-column>
        <el-table-column prop="name" label="服务" align="center"></el-table-column>
        <el-table-column prop="price" label="价格/次" align="center"></el-table-column>
        <el-table-column prop="orderPrice" label="订单价" align="center"></el-table-column>
        <el-table-column prop="employeeName" label="雇员" align="center"></el-table-column>
        <el-table-column prop="employerName" label="雇主" align="center"></el-table-column>
        <el-table-column prop="rate" label="评价/星" align="center">
          <template slot-scope="scope">
            {{scope.row.rate!=null?scope.row.rate:'待评'}}
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
    data() {
      return {
        loading: true,
        searchParam: {
          serviceId: null,
          startTime: this.timestamp() * 1000 - (1000 * 60 * 60 * 24) * 6,
          endTime: this.timestamp() * 1000,
        },
        IPNotRepeatCount: 0,
        IPtotal: 0,
        RepeatRate: 0,
        independentList: [],
        repeatRateList: [],
        totalList: [],
        dateListChange: [],
        ipRepeatRateList: [],
        failRateList: [],
        timeList: [],
        serviceList: [],
        moneyList: [],
        services: [],
      }
    },
    methods: {
      async search() {
        this.$router.push({
          path: this.$route.path,
          query: this.searchParam
        });
      },
      async getServiceSta() {
        this.loading = true;
        this.searchParam.startTime = new Date(this.searchParam.startTime).setHours(0, 0, 0, 0);// 当天0点
        this.searchParam.endTime = new Date(this.searchParam.endTime).setHours(23, 59, 59, 0);//当天23；59
        let res = await this.$api("Static/serviceChart", {
          serviceId: this.searchParam.serviceId,
          startTime: this.searchParam.startTime,
          endTime: this.searchParam.endTime,
        });
        this.loading = false;
        console.log(res);
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
            name: '服务成单量',
            type: 'column',
            data: this.serviceList,
            tooltip: {
              valueSuffix: '单'
            },
          }, {
            name: '每日营业额',
            type: 'spline',
            yAxis: 1,
            data: this.moneyList,
            tooltip: {
              valueSuffix: '元'
            },
          }]
        });
      },
      initQuery() {
        Object.assign(this.searchParam, this.$route.query);
        this.searchParam.serviceId = this.searchParam.serviceId ? parseInt(this.searchParam.serviceId) : null;
        this.searchParam.startTime = this.searchParam.startTime ? parseInt(this.searchParam.startTime) : null;
        this.searchParam.endTime = this.searchParam.endTime ? parseInt(this.searchParam.endTime) : null;
      },
      async getServiceDetail() {
        this.services = await this.$api('Static/serviceDetail', {
          serviceId: this.searchParam.serviceId,
          startTime: this.searchParam.startTime / 1000,
          endTime: this.searchParam.endTime / 1000
        });
        console.log(this.services)
      },
    },
    async created() {
      //初始化查询条件
      this.initQuery();
      //获取当前拨号统计
      await this.getServiceSta();
      await this.getServiceDetail();
    },
    watch: {
      async '$route'() {
        //初始化查询条件
        this.initQuery();
        //获取当前拨号统计
        this.getServiceSta();
        await this.getServiceDetail();
      }
    },
  }

</script>

<style>
  .statistics_title {
    width: 100%;
    height: 45px;
    font-size: large;
    margin-top: 40px;
    float: left;
    background-color: #d3dce6;
  }

  .barTitle {
    margin-left: 20px;
    float: left;
    margin-top: 0.5%
  }
</style>
