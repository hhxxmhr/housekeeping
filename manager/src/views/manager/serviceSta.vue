<template>
  <div>
    <el-row class="margin-bottom">
      <el-form :inline="true" :model="searchParam">
        <el-form-item label="rosId">
          <el-input v-model="searchParam.rosId" placeholder="请输入rosId" style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            style="width: 195px"
            v-model="searchParam.startTime"
            type="datetime"
            value-format="timestamp"
            placeholder="选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            style="width: 195px"
            v-model="searchParam.endTime"
            value-format="timestamp"
            type="datetime"
            placeholder="选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="时间间隔">
          <el-input v-model="searchParam.intervals" style="width: 120px">
            <i slot="suffix">秒</i>
          </el-input>
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
      <el-table :data="datas"
                border
                v-loading="loading">
        <el-table-column prop="rosId" label="ID" align="center" width="80px"></el-table-column>
        <el-table-column label="地区" align="center">
          <template slot-scope="scope">
            {{scope.row.prov+"-"+scope.row.city}}
          </template>
        </el-table-column>
        <el-table-column prop="domain" label="服务" align="center"></el-table-column>
        <el-table-column prop="ipTotal" label="IP总量" align="center" sortable></el-table-column>
        <el-table-column prop="ipIndependent" label="独立IP总量" align="center" sortable></el-table-column>
        <el-table-column prop="ipRepeatRate" label="IP重复率" align="center" sortable></el-table-column>
        <el-table-column prop="AdslRepeatRate" label="拨号重复率" align="center" sortable></el-table-column>
        <el-table-column prop="AdslFailRate" label="拨号失败率" align="center" sortable></el-table-column>
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
          rosId: null,
          startTime: this.timestamp() * 1000 - (1000 * 60 * 60 * 24),
          endTime: null,
          intervals: 3600,
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
        dateList: [],
        datas: [],
      }
    },
    methods: {
      async search() {
        this.$router.push({
          path: this.$route.path,
          query: this.searchParam
        });
      },
      async getIpStatics() {
        this.loading = true;
        let res = await this.$api("Statistic.IPChart", {
          rosId: this.searchParam.rosId,
          interval: this.searchParam.intervals,
          start: this.searchParam.startTime / 1000,
          end: this.searchParam.endTime ? this.searchParam.endTime / 1000 : this.timestamp()
        });
        this.loading = false;
        this.IPtotal = res.IPtotal;//ip总量
        this.IPNotRepeatCount = res.IPNotRepeatCount;//独立ip总量
        this.RepeatRate = Math.round(res.RepeatRate * Math.pow(10, 2)) / Math.pow(10, 2);//拨号重复率
        this.dateList = res.dateList;
        if (this.dateList.length == 0) {
          this.$message({
            type: 'warning',
            message: '请检查搜索条件'
          });
          return
        }
        this.dateListChange = [];
        //将时间戳转成日期时间
        this.dateList.forEach(i => {
          let date = new Date(i * 1000);
          let Y = date.getFullYear() + '-';
          let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
          let D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
          let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
          let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
          let s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
          this.dateListChange.push(Y + M + D + h + m + s);
        });
        this.independentList = res.independentList;
        this.repeatRateList = res.repeatRateList;
        this.totalList = res.totalList;
        this.ipRepeatRateList = res.ipRepeatRateList;//ip重复率
        this.failRateList = res.failRateList;//拨号失败率
        Highcharts.chart('serviceContainer', {
          credits: {//去除生成图上面的网址logo
            enabled: false
          },
          chart: {
            zoomType: 'xy'
          },
          title: {
            text: 'IP统计'
          },
          subtitle: {
            text: 'IP总量：' + this.IPtotal + "   独立IP总量：" + this.IPNotRepeatCount + "   拨号重复率: " + this.RepeatRate + "%",
            align: 'center'
          },
          xAxis: [{
            categories: this.dateListChange,
          }],
          yAxis: [{ // Primary yAxis
            labels: {
              format: '{value}个',
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            title: {
              text: null,
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            tickAmount: 5,
          }, { // Secondary yAxis
            title: {
              text: null,
              style: {
                color: '#43CD80'
              }
            },
            tickAmount: 5,
            max: 100,
            labels: {
              format: '{value} %',
              style: {
                color: '#43CD80'
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
            name: '总量',
            type: 'column',
            data: this.totalList,
            tooltip: {
              valueSuffix: ''
            },
          }, {
            name: '独立总量',
            type: 'column',
            data: this.independentList,
            tooltip: {
              valueSuffix: ''
            },
          }, {
            name: '重复率',
            yAxis: 1,
            type: 'spline',
            data: this.repeatRateList,
            tooltip: {
              valueSuffix: '%'
            }
          }, {
            name: 'ip重复率',
            yAxis: 1,
            type: 'spline',
            data: this.ipRepeatRateList,
            tooltip: {
              valueSuffix: '%'
            }
          }, {
            name: '拨号失败率',
            yAxis: 1,
            type: 'spline',
            data: this.failRateList,
            tooltip: {
              valueSuffix: '%'
            }
          }]
        });
      },
      initQuery() {
        Object.assign(this.searchParam, this.$route.query);
        this.searchParam.username = this.searchParam.username ? this.searchParam.username : null;
        this.searchParam.rosId = this.searchParam.rosId ? parseInt(this.searchParam.rosId) : null;
        this.searchParam.intervals = this.searchParam.intervals != null ? parseInt(this.searchParam.intervals) : null;
      },
      async getIpDetail() {
        this.datas = await this.$api('Statistic.IPDetail', {
          rosId: this.searchParam.rosId,
          start: this.searchParam.startTime / 1000,
          end: this.searchParam.endTime === null ? this.timestamp() : this.searchParam.endTime / 1000
        });
      },
    },
    async created() {
      //初始化查询条件
      this.initQuery();
      //获取当前拨号统计
      await this.getIpStatics();
      await this.getIpDetail();
    },
    watch: {
      async '$route'() {
        //初始化查询条件
        this.initQuery();
        //获取当前拨号统计
        this.getIpStatics();
        await this.getIpDetail();
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
