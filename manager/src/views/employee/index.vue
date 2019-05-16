<template>
  <div v-if="fetchDone" align="center" style="background-color: #f2f3f8;width: 100%;height: 1000px">
    <template>
      <el-carousel :interval="4000" type="card" height="150px">
        <el-carousel-item v-for="(item,index) in headData" :key="index">
          <h3>{{ getItem(item) }}</h3>
        </el-carousel-item>
      </el-carousel>
    </template>

    <!--<el-row :gutter="16">-->
    <!--<el-col style="width: 33.3%">-->
    <!--<el-card shadow="hover" :body-style="{ background: '#fbad4c'}">-->
    <!--<i class="el-icon-star-off el-icon-star-off1" style="color: white;margin-left: -55%;"></i>-->
    <!--<p class="balance">我的收益</p>-->
    <!--<p style="margin-top: 1%;color: white;font-size: 24px;margin-left: 1%">{{allData.balance}}</p>-->
    <!--</el-card>-->
    <!--</el-col>-->
    <!--<el-col style="width: 33.3%">-->
    <!--<el-card shadow="hover" :body-style="{ background: '#59d05d',height: '15%',fontSize:'18px' }">-->
    <!--<i class="el-icon-document el-icon-star-off1" style="color: white;margin-left: -55%;"></i>-->
    <!--<p class="balance">完成订单</p>-->
    <!--<p style="margin-top: 1%;color: white;font-size: 24px;margin-left:2%">-->
    <!--{{allData.orderCount}}</p>-->
    <!--</el-card>-->
    <!--</el-col>-->
    <!--<el-col style="width: 33.3%">-->
    <!--<el-card shadow="hover" :body-style="{ background: '#ff646d',fontSize:'18px' }">-->
    <!--<i class="el-icon-circle-check-outline el-icon-star-off1"-->
    <!--style="color: white;margin-left: -55%;"></i>-->
    <!--<p class="balance">我的好评</p>-->
    <!--<p style="margin-top: 1%;color: white;font-size: 24px;margin-left: 2%">{{allData.goodComment}}</p>-->
    <!--</el-card>-->
    <!--</el-col>-->
    <!--</el-row>-->
    <div>
      <!--<el-row style="width: 100%;margin-top: 2%;" v-if="role==200">
        <el-col>
          <el-card shadow="hover" style="height: 100px">
            <div style="width: 100%">
              <div style="margin-top: 15px;width: 16.66%;float: left">

                <label style="font-size: 20px;font-weight:normal">
                  <i class="el-icon-mhr-feiyong" style="color: #b3e19d"></i>
                  VPN总数:&emsp;{{allData.atotal}}</label>
              </div>
              <div style="margin-top: 15px;width: 16.66%;float: left">
                <label style="font-size: 20px;font-weight:normal">1日到期:&emsp;{{allData.aOneDay}}</label>
              </div>
              <div style="margin-top: 15px;width: 16.66%;float: left">
                <label style="font-size: 20px;font-weight:normal">7日到期:&emsp;{{allData.aSevenDay}}</label>
              </div>
              <div style="margin-top: 15px;width: 16.66%;float: left">
                <label style="font-size: 20px;font-weight:normal">客户VPN总数:&emsp;{{allData.ctotal}}</label>
              </div>
              <div style="margin-top: 15px;width: 16.66%;float: left">
                <label style="font-size: 20px;font-weight:normal">1日到期:&emsp;{{allData.cOneDay}}</label>
              </div>
              <div style="margin-top: 15px;width: 16.66%;float: left">
                <label style="font-size: 20px;font-weight:normal">7日到期:&emsp;{{allData.cSevenDay}}</label>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>-->
    </div>

    <el-tabs v-model="activeName" @tab-click="handleClick"
             style="height: 0%;margin-left: 90%;margin-top:1% ;width: 50%">
      <el-tab-pane label="近30天" name="first"></el-tab-pane>
      <el-tab-pane label="上个月" name="second"></el-tab-pane>
    </el-tabs>
    <el-row style="margin-top: 50px">
      <!--<div id="container_agentMeal" class="divcss5" style="width:83%;height:400px;margin-top: -310px;float: right"></div>-->
      <div id="container_chart" class="divcss5"
           style="width:100%;height:400px;float: right;margin-top: -10px"></div>
    </el-row>
    <el-row style="margin-top: 2%;">
      <el-col style="width: 18%;">
        <el-card shadow="hover" style="height: 150px">
          <el-button type="primary" icon="el-icon-news" circle
                     style="margin-left: -20%;background-color: #8085e9;border-color: #8085e9"></el-button>
          <label class="lastRow">好评率
            <br/>
            <b>{{allData.goodPercent*100}}%</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 1%;width: 18%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-star-off" circle
                     style="margin-left: -45px;background-color: lightsalmon;border-color: lightsalmon"></el-button>
          <label style="font-size: 18px;font-weight:400;color: #5f6468;margin-left: 6%">退款率
            <br/>
            <b>{{allData.refundPercent*100}}%</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 1%;width: 20%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-edit-outline" circle
                     style="margin-left: -20%;background-color: #eeaaee;border-color: #eeaaee"></el-button>
          <label class="lastRow">额外收益
            <br/>
            <b>{{allData.extra?allData.extra:0}}</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 1%;width: 20%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-circle-check-outline" circle
                     style="margin-left: -20%;background-color: lightskyblue;border-color: lightskyblue"></el-button>
          <label class="lastRow">最优服务
            <br/>
            <b>{{allData.goodService}}</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 1%;width: 20%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-document" circle
                     style="margin-left: -20%;background-color: #66CDAA;border-color: #66CDAA"></el-button>
          <label class="lastRow">最高等级
            <br/>
            <b>{{allData.rank}}</b>
          </label>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Highcharts from 'highcharts/highstock';

  export default {
    data() {
      return {
        fetchDone: false,
        isLoading: false,
        noticeList: [],
        activeName: 'first',
        bigName: 'mine',
        startTime: Date.parse(new Date()) - 86400000 * 30,
        endTime: Date.parse(new Date()),
        allData: {},//单个的小数据
        headData: [],//页面顶头数据
        timeList: [],//坐标轴时间
        balanceList: [],
        ordersList: [],
        refundList: [],
      }
    },
    methods: {
      getItem(json) {
        for (let key in json) {
          return key + '：' + json[key];
        }
      },
      async fetchData() {
        this.fetchDone = true;
      },
      async handleClick(tab) {
        if (tab.name === 'first') {//近30天
          this.endTime = Date.parse(new Date());
          this.startTime = this.endTime - 86400000 * 30;//一天86400秒
          this.init()
        } else if (tab.name === 'second') {//上个月
          let nowdays = new Date();
          let year = nowdays.getFullYear();
          let month = nowdays.getMonth();
          if (month === 0) {
            month = 12;
            year = year - 1;
          }
          if (month < 10) {
            month = "0" + month;
          }
          let firstDay = year + "-" + month + "-" + "01";//上个月的第一天
          let myDate = new Date(year, month, 0);
          let lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
          this.startTime = Date.parse(firstDay);
          this.endTime = Date.parse(lastDay);
          this.init()
        }
        /*if (Object.entries(this.dataList).length < 2) {
          this.$message({
            showClose: true,
            message: '您尚未有购买记录'
          });
          return
        }*/
      },
      async init() {
        this.allData = await this.$api("User/statistics");
        // this.allData.refundPercent = this.allData.refundPercent.toFixed(2);
        this.headData = [];
        for (let i in this.allData.head) {
          let o = {};
          o[i] = this.allData.head[i];
          this.headData.push(o)
        }
        //图表数据
        this.startTime = new Date(this.startTime).setHours(0, 0, 0, 0);// 当天0点
        this.endTime = new Date(this.endTime).setHours(23, 59, 59, 0);//当天23；59
        let resp = await this.$api("User/ordersChart", {
          startTime: this.startTime,
          endTime: this.endTime,
        });
        console.log(resp);
        this.timeList = resp.timeList;
        this.balanceList = resp.balanceList;
        this.ordersList = resp.ordersList;
        this.refundList = resp.refundList;

        // if (this.role == 200) {
        Highcharts.chart('container_chart', {
          credits: {//去除生成图上面的网址logo
            enabled: false
          },
          title: {
            text: '资金订单统计'
          },
          chart: {
            zoomType: 'xy'
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
              text: "单数",
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            allowDecimals: false
          },  {
            title: {
              text: "净收入",
              style: {
                color: Highcharts.getOptions().colors[0]
              }
            },
            allowDecimals: false,
            labels: {
              format: '{value} 元',
              style: {
                color: Highcharts.getOptions().colors[0]
              }
            },
            opposite: true
          },],
          tooltip: {
            crosshairs: true,
            shared: true
          },
          legend: {
            layout: 'horizontal',
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || 'rgba(255,255,255,0.25)'
          },
          series: [{
            name: '服务成单量',
            type: 'line',
            data: this.ordersList,
            tooltip: {
              valueSuffix: '单'
            },
          }, {
            name: '服务退款数',
            type: 'line',
            data: this.refundList,
            tooltip: {
              valueSuffix: '单'
            },
          }, {
            name: '净收入',
            type: 'column',
            yAxis: 1,
            data: this.balanceList,
            tooltip: {
              valueSuffix: '元'
            },
          }],
        });
      }
      /*else {
        Highcharts.chart('container_customMeal', {
          credits: {//去除生成图上面的网址logo
            enabled: false
          },
          chart: {
            type: 'spline'
          },
          title: {
            text: '套餐购买统计'
          },
          xAxis: {
            title: {
              text: '月份',

            },
            categories: timeList//读时间数据
          },
          yAxis: {
            title: {
              text: null
            },

          },
          tooltip: {
            crosshairs: true,
            shared: true
          },
          plotOptions: {
            spline: {
              marker: {
                radius: 4,
                lineColor: '#666666',
                lineWidth: 1
              }
            }
          },
          series: this.newArr
        });
      }*/
    },
    // },
    created() {
      this.init();
      this.fetchData();
    },
  }

</script>

<style>

  .el-carousel__item h3 {
    color: #475669;
    font-size: 20px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }

  .el-carousel__item:nth-child(n) {
    background-color: #A4D3EE;
  }

  /* .el-carousel__item:nth-child(2n+1) {
     background-color: #59d05d;
   }*/

  .divcss5 {
    border: 1px solid #59d05d;
  }

  .el-icon-star-off1 {
    font-size: 50px;
    margin-top: 3%;
    /*margin-left: 30%;*/
  }

  .balance {
    font-size: 22px;
    margin-left: 1%;
    color: white;
    font-weight: normal;
    margin-top: -10%;
  }

  .lastRow {
    font-size: 18px;
    font-weight: 400;
    color: #5f6468;
    margin-left: 9%
  }

  .table1 {
    width: 15%;
    height: 400px;
    margin-top: -310px;
    float: left;
    max-height: 400px;
    overflow: auto
  }

  .table2 {
    width: 15%;
    height: 400px;
    margin-top: -170px;
    float: left;
    max-height: 400px;
    overflow: auto
  }

</style>
