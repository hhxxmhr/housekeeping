<template>
  <div>
    <el-carousel :interval="5000" arrow="always" height="350px">
      <el-carousel-item v-for="item in imagesbox" :key="item.id">
        <img :src="item.idView" :class="{imageLoop0:(item.id===0||item.id===2),imageLoop2:(item.id===1)}">
        <h1 v-if="item.id===0" align="center"
            style="margin-top: -80px;font-size: 48px;font-family: 宋体;font-weight: bold">
          你家我洁--家政管理平台</h1>
        <h1 v-if="item.id===2" align="center"
            style="margin-top: -180px;font-size: 48px;font-family: 宋体;font-weight: bold">
          你的家有我来守护</h1>
        <img src="../../../src/assets/index8.jpg" class="imageLoop0" v-if="item.id===2"
             style="float: right;margin-top: -180px">

      </el-carousel-item>
    </el-carousel>

    <el-row style="margin-top: 2%;">
      <el-col style="width: 22.7%;">
        <el-card shadow="hover" style="height: 150px">
          <el-button type="primary" icon="el-icon-news" circle
                     style="background-color: #8085e9;border-color: #8085e9;margin-left: 15%"></el-button>
          <label class="lastRow">好评率
            <br/>
            <b style="margin-left: 45%;">{{detailData.goodPercent*100}}%</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 3%;width: 22.7%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-star-off" circle
                     style="margin-left: 40px;background-color: lightsalmon;border-color: lightsalmon"></el-button>
          <label style="font-size: 18px;font-weight:400;color: #5f6468;margin-left: 12%">退款率
            <br/>
            <b style="margin-left: 45%;">{{detailData.refundPercent*100}}%</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 3%;width: 22.7%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-edit-outline" circle
                     style="margin-left: 15%;background-color: #eeaaee;border-color: #eeaaee"></el-button>
          <label class="lastRow">超时费用
            <br/>
            <b style="margin-left: 47%;">{{detailData.extra?detailData.extra:0}}</b>
          </label>
        </el-card>
      </el-col>
      <el-col style="margin-left: 3%;width: 22.7%">
        <el-card shadow="hover" style="height: 150px;">
          <el-button type="primary" icon="el-icon-circle-check-outline" circle
                     style="margin-left: 15%;background-color: lightskyblue;border-color: lightskyblue"></el-button>
          <label class="lastRow">常用服务
            <br/>
            <b style="margin-left: 45%;">{{detailData.mostService}}</b>
          </label>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>


<script>
  export default {
    data() {
      return {
        imagesbox: [
          {id: 0, idView: require("../../assets/index.jpg")},
          {id: 1, idView: require("../../assets/banner.jpg")},
          {id: 2, idView: require("../../assets/index9.jpg")}],
        detailData: {},
      }
    },
    async created() {
      this.detailData = await this.$api("User/employerIndex");
    },
    methods: {},
    mounted() {
    }
  }
</script>


<style>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 350px;
    margin: 0;
  }

  .imageLoop0 {
    width: auto;
  }

  .imageLoop2 {
    width: 100%;
    height: 350px;
  }

  .el-carousel__item:nth-child(n) {
    background-color: rgba(64, 158, 255, .1);
  }

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
    margin-left: 14%
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
