<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-form :model="searchForm" :inline="true">
          <el-form-item label="筛选区域" style="margin-left: 40px">
            <v-distpicker :province="searchForm.prov" :city="searchForm.city" @province="selectProv"
                          @city="selectCity"
                          hide-area></v-distpicker>
          </el-form-item>
          <el-form-item label="经验">
            <el-select v-model="searchForm.experience" style="width: 150px">
              <el-option label="全部" :value=null></el-option>
              <el-option v-for="item in Object.entries(UserExperience)"
                         :key="item[0]"
                         :label="item[1]"
                         :value="parseInt(item[0])"></el-option>
            </el-select>
          </el-form-item>
          <el-button @click="search" style="margin-left: 10px;" icon="el-icon-star-off">好评优先</el-button>
          <el-button @click="search" style="margin-left: 10px;" icon="el-icon-search" type="primary">搜索</el-button>
        </el-form>
      </el-row>

      <div v-for="item in info" :key="item.id" class="meal-card">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="'http://localhost:8888/'+item.photo" class="image" v-if="item.photo">
          <img src="../../../src/assets/xiao.jpg" class="image" v-else>
          <div class="time" style="padding: 14px;">
            <p style="text-align: center;margin-top: -5px;color: black">{{item.name|ellipsis}}
              <el-button type="text" class="button" style="margin-left: 10px" @click="showDialogInfo(item)">详细资料
              </el-button>
            </p>
            <p style="margin-top: 5px;">性别: {{item.sex}}</p>
            <p style="margin-top: 5px;">家政经验: {{UserExperience[item.experience]}}</p>
            <p style="margin-top: 5px">从事服务: {{'尚未完成'}}</p>
            <p style="margin-top: 5px">完成订单: {{'尚未完成'}}</p>
            <p style="margin-top: 5px">好评率值: {{'尚未完成'}}</p>
            <!--<p>口味选项: {{(item.taste.length>0?item.taste.toString():"无")|ellipsis}}</p>-->
            <p style="margin-top: 5px">自我介绍: {{item.introduction}}</p>
          </div>
          <el-button type="primary" plain class="reserve" style="margin-left: 10px" @click="reserve(item)"
                     icon="el-icon-d-arrow-right">去预定
          </el-button>

        </el-card>
        <!--</a>-->
      </div>
      <!--更多资料-->
      <el-dialog width="390px" title="详细信息" :visible.sync="dialog_visible"
                 :close-on-click-modal="false">
        <el-form :model="moreInfo" size="small">
          <el-form-item label="姓名：" :label-width="formLabelWidth">
            {{moreInfo.name}}
          </el-form-item>
          <el-form-item label="年龄：" :label-width="formLabelWidth">
            {{moreInfo.age}}
          </el-form-item>
          <el-form-item label="学历：" :label-width="formLabelWidth">
            {{UserDegree[moreInfo.education]}}
          </el-form-item>
          <el-form-item label="性别：" :label-width="formLabelWidth">
            {{moreInfo.sex}}
          </el-form-item>

          <el-form-item label="工作地区：" :label-width="formLabelWidth">
            {{moreInfo.prov+'--'+moreInfo.city}}
          </el-form-item>
          <el-form-item label="从事服务：" :label-width="formLabelWidth" style="color: red">
            {{'尚未完成联表查询'}}
          </el-form-item>
          <el-form-item label="家政经验：" :label-width="formLabelWidth">
            {{UserExperience[moreInfo.experience]+' '}}
          </el-form-item>
          <el-form-item label="自我介绍：" :label-width="formLabelWidth">
            {{moreInfo.introduction}}
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
  import VDistpicker from 'v-distpicker'

  export default {
    components: {VDistpicker},
    computed: {},
    filters: {
      ellipsis(value) {
        if (value === null) return '';
        if (value.length > 15) {
          return value.slice(0, 15) + '...'
        }
        return value
      },
    },
    async created() {
      this.initQuery();
      this.getInfo();
    },
    watch: {
      '$route'() {
        this.initQuery();
        this.getInfo();
      }
    },
    data() {
      let isPhone = false;
      //平台、设备和操作系统
      const system = {
        win: false,
        mac: false,
        xll: false,
        ipad: false
      };
      //检测平台
      const p = navigator.platform;
      system.win = p.indexOf("Win") === 0;
      system.mac = p.indexOf("Mac") === 0;
      system.x11 = (p === "X11") || (p.indexOf("Linux") === 0);
      system.ipad = (navigator.userAgent.match(/iPad/i) != null);
      isPhone = !(system.win || system.mac || system.xll || system.ipad);
      let sixHalf = new Date(new Date().setHours(0, 0, 0, 0)) / 1000 + 18 * 3600 + 5 * 360;
      return {
        dialog_title: '',
        dialog_visible: false,
        formLabelWidth: "90px",
        moreInfo: {},
        searchForm: {
          serviceId: null,//从服务中心页面跳转过来的参数
          experience: null,
          prov: '',
          city: '',
          state: 3,
          role: 200
        },
        limitTime: sixHalf,
        info: [],
        isLoading: false,
        dialogVisible: false,
        itemTaste: [],
        item: {},

      form: {
          id: null,//所选菜品的id
          name: '',
          shopName: '',
          desc: '',
          amount: null,
          taste: '',
        },
        rules: {
          amount: [
            {required: true, message: '此项不可为空', trigger: 'change'}
          ],
        },
        dialogWidth: isPhone ? ((window.screen.width * 0.9) + 'px') : '420px',
        isPhone: isPhone,
      }
    },
    methods: {
      initQuery() {
        Object.assign(this.searchForm, this.$route.query);
        this.searchForm.serviceId = this.searchForm.serviceId ? parseInt(this.searchForm.serviceId) : null;//接受跳转页面的参数
        this.searchForm.experience = this.searchForm.experience != null ? parseInt(this.searchForm.experience) : null;//页面搜索
      },
      async getInfo() {
        let res;
        if (this.searchForm.serviceId == null){
           res = await this.$api("getAll", this.searchForm);
           // console.log(res)
        }else {
          res = await this.$api("User/listUserByServiceId", this.searchForm);
        }
        this.info = res.list;
      },
      //详细资料按钮点击
      showDialogInfo(item) {
        this.dialog_title = "详细资料";
        this.moreInfo = item;
        this.dialog_visible = true;
      },
      search() {
        if (this.searchForm.prov === '省' || this.searchForm.city === '市') {
          this.searchForm.prov = null;
          this.searchForm.city = null;
        }
        this.$router.push({
          path: this.$route.path,
          query: this.searchForm
        });
      },
      reserve(employee) {
        //跳转到预定页面
        this.$router.push({
          path: "/employer/reserve",
          query: {employeeId: employee.id, serviceId: this.searchForm.serviceId}
        });
      },
      selectProv(data) {
        this.searchForm.prov = data.value;
      },
      selectCity(data) {
        this.searchForm.city = data.value;
      },
    },
  };
</script>
<style lang="scss" scoped>
  .dialog-wrapper .el-dialog__body {
    margin-top: -20px !important;
  }

  .meal-card {
    display: inline-block;
    height: 360px;
    width: 280px !important;
    margin-top: 15px;
    margin-left: 45px;
  }

  .dialog-desc {
    width: 260px;
  }

  @media screen and (max-device-width: 960px) {
    .meal-card {
      width: 100%;
      margin: 10px auto;
      float: unset;
      min-height: 235px;
    }
    .dialog-desc {
      width: 90%;
    }
  }

  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    font-size: 13px;
    /*padding: 0;*/
    /*margin-left: 72%;*/
    margin-top: -10%;
    /*float: right;*/
    color: #20a0ff;
  }

  .reserve {
    font-size: 13px;
    /*padding: 0;*/
    margin-left: 72%;
    margin-top: -10%;
    float: right;
    /*color: #E6A23C;*/
  }

  .image {
    width: 100%;
    height: 180px;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .a {
    margin-left: 0 !important;
    margin-top: 10px;
  }
</style>
