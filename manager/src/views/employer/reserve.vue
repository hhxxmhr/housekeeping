<template>
  <div>
    <el-form :model="reserveForm" :rules="rules" ref="reserveForm" label-width="120px" class="demo-reserveForm"
             style="width: 100%">
      <el-form-item label="所选服务" prop="serviceId" style="width: 1200px">
        <el-radio-group v-model="reserveForm.serviceId" size="mini" v-for="(item,index) in serviceList"
                        :key="item.id" :label="item.id" v-if="reserveForm.employeeId===null"
                        style="margin-left: 5px">
          <div v-if="item.childrenType.length===0">
            <el-radio :label="item.id" style="color: #4ba3f8">{{item.name}}</el-radio>
          </div>
          <div v-else>
            <el-tag>{{item.name}}</el-tag>
            <el-radio v-for="(serviceChild,index) in item.childrenType" :label="serviceChild.id"
                      :key="index">
              {{serviceChild.name}}
            </el-radio>
          </div>
        </el-radio-group>
        <el-radio-group v-model="reserveForm.serviceId">
          <el-radio v-for="(item,index) in originService" :key="index" :label=item.serviceId>
            {{item.serviceName+"-"+item.rankName+"-"+(item.money+item.price)+"元/次"}}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="上门时间" prop="reverseTime" required>
        <el-date-picker
          v-model="reserveForm.reverseTime"
          format="yyyy-MM-dd HH:mm "
          value-format="timestamp"
          type="datetime"
          placeholder="选择日期时间"
          :picker-options="pickerOptions0">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="联系电话" prop="phone" style="margin-left: 310px;margin-top: -62px">
        <el-input v-model="reserveForm.phone" style="width: 20%"></el-input>
      </el-form-item>
      <el-form-item label="所在地区" prop="area" style="margin-left: 580px;margin-top: -62px">
        <v-distpicker :province="reserveForm.prov" :city="reserveForm.city" @province="selectProv" @city="selectCity"
                      hide-area></v-distpicker>
      </el-form-item>
      <el-form-item label="详细地址" prop="address">
        <el-input v-model="reserveForm.address" style="width: 41%"></el-input>
      </el-form-item>
      <el-form-item label="预约人员" prop="user">
        <el-tag v-if="reserveForm.employeeId!==null">{{"name："+employeeInfo.name+"、tel："+employeeInfo.phone}}</el-tag>
        <el-button v-if="reserveForm.employeeId!==null" type="primary" icon="el-icon-edit" size="mini" @click="choose"
                   style="margin-left: 20px">重新选择
        </el-button>
        <el-button v-else type="primary" icon="el-icon-edit" size="mini" @click="choose">选择服务人员</el-button>
      </el-form-item>
      <el-form-item label="相关备注" prop="tip">
        <el-input type="textarea" v-model="reserveForm.tip" style="width: 41%"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="reserveButton">立即预定</el-button>
        <el-button @click="resetForm('reserveForm')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog width="400px" title="预定信息确认" :visible.sync="dialog_visible"
               :close-on-click-modal="false">
      <el-form :model="information" size="small">
        <el-form-item label="所选服务：" :label-width="formLabelWidth">
          {{information.serviceName}}
        </el-form-item>
        <el-form-item label="服务人员：" :label-width="formLabelWidth">
          {{information.employeeName}}
        </el-form-item>
        <el-form-item label="预留电话：" :label-width="formLabelWidth">
          {{reserveForm.phone}}
        </el-form-item>
        <el-form-item label="上门时间：" :label-width="formLabelWidth">
          {{$formatTime(reserveForm.reverseTime/1000)}}
        </el-form-item>
        <el-form-item label="服务地址：" :label-width="formLabelWidth">
          {{reserveForm.prov+'-'+reserveForm.city+'-'+reserveForm.address}}
        </el-form-item>
        <el-form-item label="相关备注：" :label-width="formLabelWidth">
          {{reserveForm.tip?reserveForm.tip:'暂无'}}
        </el-form-item>
        <el-form-item label="服务金额：" :label-width="formLabelWidth">
          {{reserveForm.orderPrice}}元
        </el-form-item>
        <span style="color: #f78989">订单预定后，2小时之内可免费取消。</span>
        <span style="color: #f78989">若超出2小时取消订单，将收取订单价格的50%,即{{reserveForm.orderPrice*0.5}}元</span>

      </el-form>
      <div slot="footer" style="margin-top: -30px">
        <el-button @click="dialog_visible=false">返回</el-button>
        <at-button @click="submit" type="primary">确认</at-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import VDistpicker from 'v-distpicker'

  export default {
    components: {VDistpicker},
    data() {
      let validatePhone = (rule, phone, callback) => {
        if (!phone) {
          return callback(new Error('手机号不能为空'));
        } else {
          if (/^1[34578]\d{9}$/.test(phone)) callback();
          else callback(new Error('手机号码格式错误'));
        }
      };

      return {
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          }
        },
        allService: [],
        originService: [],//页面跳转携带的employeeId查询到的拥有的可选服务
        mineInfo: {},
        serviceList: [],
        employeeInfo: {},
        dialog_visible: false,
        formLabelWidth: "90px",
        information: {
          serviceName: '',
          employeeName: '',
        },
        reserveForm: {
          tip: '',
          city: '',
          prov: '',
          address: '',
          phone: '',
          orderPrice: null,
          reverseTime: this.timestamp() * 1000 + (1000 * 60 * 60 * 24),
          endTime: null,
          serviceId: null,//页面跳转传过来的id或者选中的ids
          employeeId: null,//页面跳转传过来的id
          employerId: window.$mine.id
        },
        rules: {
          serviceId: [
            {required: true, message: '请选择服务', trigger: 'change'},
          ],
          reverseTime: [
            {required: true, message: '请选择上门时间', trigger: 'blur'}
          ],
          phone: [
            {validator: validatePhone, trigger: 'blur'},
          ],
          address: [
            {required: true, message: '请输入详细地址', trigger: 'blur'}
          ],

        },
      }
    },
    async created() {
      this.$emit("updateBalance");//刷新余额
      this.initQuery();
      this.init();
      this.getMineInfo();

    },
    methods: {
      async init() {
        this.serviceList = [];
        let res = await this.$api('Service/getAll', {employeeId: this.reserveForm.employeeId});
        this.allService = res.serviceList;
        //得到所有的父类
        this.allService.forEach(item => {
          if (item.parent === null) this.serviceList.push(item)
        });
        this.serviceList.forEach(it => {
          let childrenType = [];
          this.allService.forEach(item => {
            let row = {};
            if (item.parent === it.id) {
              row.id = item.id;
              row.name = item.name;
              row.price = item.price;
              childrenType.push(row)
            }
          });
          it["childrenType"] = childrenType;
        });
      },
      async initQuery() {
        Object.assign(this.reserveForm, this.$route.query);
        this.reserveForm.serviceId = this.reserveForm.serviceId ? parseInt(this.reserveForm.serviceId) : null;
        this.reserveForm.employeeId = this.reserveForm.employeeId != null ? parseInt(this.reserveForm.employeeId) : null;

        //根据员工id查询信息
        let res = await this.$api('User/getUserById', {id: this.reserveForm.employeeId});
        this.employeeInfo = res.data;
        //根据传来的员工id查询其拥有的service数据
        let resp = await this.$api('User/findUserInfos2', {employeeId: this.reserveForm.employeeId});
        this.originService = resp.data;
      },
      reserveButton() {
        this.$refs['reserveForm'].validate(async valid => {
          if (valid) {
            if (this.reserveForm.city === this.employeeInfo.city) {
              let tmp = this.originService.find(item => item.serviceId === this.reserveForm.serviceId);
              this.information.serviceName = tmp.serviceName + '-' + tmp.rankName;
              this.reserveForm.orderPrice = tmp.money + tmp.price;
              this.information.employeeName = this.employeeInfo.name + '-' + this.employeeInfo.phone;
              this.dialog_visible = true;
            } else this.$message.error("请核实地区重新选择人员")
          }
        });
      },
      choose() {
        if (this.reserveForm.prov === '省' || this.reserveForm.city === '市') {
          this.reserveForm.prov = null;
          this.reserveForm.city = null;
        }
        //首先要选中一个服务以及地址，地区不吻合也是不行的----尚未完成
        this.$refs.reserveForm.validateField('serviceId', (error) => {
          if (!error) {//当校验通过时，这里面写逻辑代码
            if (this.reserveForm.city !== null) {
              //跳转到选人的页面
              this.$router.push({
                path: "/employer/chooseEmployee",
                query: {
                  serviceId: this.reserveForm.serviceId,
                  prov: this.reserveForm.prov,
                  city: this.reserveForm.city,
                }
              });
            } else this.$message.error("请先选择住址所在区域")
          } else this.$message.error("请先选择服务")
        });
      },
      async submit() {
        //检查余额是否充足
        if (this.reserveForm.orderPrice > window.$mine.balance) {
          this.$message.error("余额不足，请先充值");
          this.dialog_visible = false;
        } else {
          this.reserveForm.reverseTime = this.reserveForm.reverseTime / 1000;
          let res = await this.$api('Order/add', this.reserveForm);
          this.dialog_visible = false;
          this.$message({
            type: res.code === 200 ? 'success' : 'error',
            message: res.msg
          });
          this.$emit("updateBalance");//刷新余额
          if (res.code === 200) {
            //跳转到订单列表页面
            this.$router.push({
              path: "/manager/orders",
            });
          }
        }
      },
      selectProv(data) {
        this.reserveForm.prov = data.value;
      },
      selectCity(data) {
        this.reserveForm.city = data.value;
      },
      async getMineInfo() {
        let res = await this.$api('User/getMineInfo', {});
        this.mineInfo = res.data;
        this.reserveForm.address = this.mineInfo.address;
        this.reserveForm.phone = this.mineInfo.phone;
        this.reserveForm.prov = this.mineInfo.prov;
        this.reserveForm.city = this.mineInfo.city;
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style lang="scss" scoped>
  .type {
    color: #606266;
    font-weight: 500;
    margin-top: 13px;
    margin-bottom: 10px;
    margin-left: 20px;
    font-size: 15px;
    cursor: pointer;
    display: block;
    position: relative;
    white-space: nowrap;
  }

  /*.do {
    margin-left: 310px;
    margin-top: -85px;
  }

  .does {
    margin-left: 600px;
    margin-top: -85px;
  }*/

  /*.doing {
    margin-left: 890px;
    margin-top: -85px;
  }*/
</style>
