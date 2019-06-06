<template>
  <div>
    <div class="page-content">
      <div class="register__progress-wrap">
        <ul class="register__progress step1" :class="{step2:step_2}">
          <li class="register__item1">设置账号密码</li>
          <li class="register__item2">完善个人信息</li>
          <li class="register__item3">创建账号成功</li>
        </ul>
      </div>
      <div>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="员工注册账户" name="employee"></el-tab-pane>
          <el-tab-pane label="雇主注册账户" name="employer"></el-tab-pane>
        </el-tabs>
      </div>
      <form v-show="step_1">
        <div class="form-wrap form-step1">
          <div class="register__bd">
            <h2 class="register__form-title">设置账号密码</h2>
            <el-form size="medium" ref="registerForm_1" label-width="85px" :model="registerForm"
                     :rules="rules" class="register__register-form">
              <el-form-item label="登录帐号" prop="username">
                <el-input v-model="registerForm.username"
                          placeholder="请输入账号名，字数6-20字，支持英文或数字"></el-input>
              </el-form-item>
              <el-form-item label="登录密码" prop="password">
                <el-input v-model="registerForm.password"
                          placeholder="请输入登录密码，字数6-20字，区分大小写" type="password"></el-input>
              </el-form-item>
              <el-form-item label="确认密码" prop="pwd2">
                <el-input v-model="registerForm.pwd2"
                          placeholder="请再次输入密码，字数6-20字，区分大小写" type="password"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="register__ft">
            <at-button type="primary"
                       :disabled="!registerForm.username || !registerForm.password || !registerForm.pwd2"
                       @click="step1Fn">保存并下一步
            </at-button>
          </div>
        </div>
      </form>
      <form v-show="step_2">
        <div class="form-wrap">
          <div class="register__bd">
            <h2 class="register__form-title">完善个人信息</h2>
            <el-form ref="registerForm_2" label-width="85px" :model="registerForm"
                     :rules="rules" :inline="true" class="register__register-form">

              <el-form-item label="姓名" prop="name">
                <el-input v-model="registerForm.name" placeholder="请输入真实姓名"></el-input>
              </el-form-item>
              <el-form-item label="年龄" prop="age">
                <el-input v-model="registerForm.age" placeholder="请输入实际年龄"></el-input>
              </el-form-item>
              <el-form-item label="学历" prop="education" v-if="registerForm.role==='employee'||registerForm.role===200">
                <el-select v-model="registerForm.education" placeholder="请选择" style="width: 189px">
                  <el-option v-for="item in Object.entries(UserDegree)"
                             :key="item[0]"
                             :label="item[1]"
                             :value="parseInt(item[0])"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="性别" prop="sex">
                <el-radio v-model="registerForm.sex" label="男">男</el-radio>
                <el-radio v-model="registerForm.sex" label="女">女</el-radio>
              </el-form-item>
              <el-form-item label="手机号码" prop="phone"
                            :class="{ style1:registerForm.role==='employer'||registerForm.role!==200 }">
                <el-input v-model="registerForm.phone" placeholder="手机号码用于找回密码、接收通知短信"></el-input>
              </el-form-item>
              <el-form-item label="工作经验" prop="experience"
                            v-if="registerForm.role==='employee'||registerForm.role===200">
                <el-select v-model="registerForm.experience" placeholder="请选择" style="width: 189px">
                  <el-option v-for="item in Object.entries(UserExperience)"
                             :key="item[0]"
                             :label="item[1]"
                             :value="parseInt(item[0])"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="验证码" prop="code">
                <el-input v-model="registerForm.code" class="code" placeholder="短信验证码"></el-input>
                <el-button class="code-button" v-if="coding <= 0" @click="get_code" style="margin-left: 5px">获取验证码
                </el-button>
                <el-button class="code-button" type="warning" v-else disabled>{{coding}} 秒</el-button>
              </el-form-item>
              <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="registerForm.idCard" placeholder="一旦确认不可更改"></el-input>
              </el-form-item>
              <el-form-item label="银行卡号" prop="bankCard" v-if="registerForm.role==='employee'||registerForm.role===200">
                <el-input v-model="registerForm.bankCard" placeholder="一旦确认不可更改"></el-input>
              </el-form-item>

              <el-form-item label="所在区域">
                <v-distpicker :province="registerForm.prov" :city="registerForm.city" @province="selectProv"
                              @city="selectCity"
                              hide-area></v-distpicker>
              </el-form-item>
              <el-form-item label="详细地址" prop="address" v-if="registerForm.role!=='employee'||registerForm.role===200">
                <el-input type="textarea" autosize :rows="2" v-model="registerForm.address"
                          placeholder="请输入详细地址"></el-input>
              </el-form-item>
              <el-form-item label="是否婚配" prop="married" v-if="registerForm.role==='employee'||registerForm.role===200">
                <el-select v-model="registerForm.married" placeholder="请选择" style="width: 189px">
                  <el-option label="是" value="是"></el-option>
                  <el-option label="否" value="否"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="自我介绍" prop="introduction"
                            v-if="registerForm.role==='employee'||registerForm.role===200">
                <el-input type="textarea" v-model="registerForm.introduction" autosize :rows="2"
                          style="width: 400px"></el-input>
              </el-form-item>
              <el-form-item label="可做服务" prop="service" v-if="registerForm.role==='employee'||registerForm.role===200">
                <el-checkbox-group v-model="registerForm.service" size="mini" v-for="(serviceItem,index) in serviceList"
                                   :key="serviceItem.id" :label="serviceItem.id"
                                   :class="{'do':index%3===1,'does':index%3===2}">
                  <div v-if="serviceItem.childrenType.length===0">
                    <el-checkbox v-if style="color: #EC8C47" :label="serviceItem.id">{{serviceItem.name}}</el-checkbox>
                  </div>
                  <span class="type" v-else style="color: #EC8C47">{{serviceItem.name}}</span>
                  <el-checkbox v-for="(serviceChild,index) in serviceItem.childrenType" :label="serviceChild.id"
                               :key="index">
                    {{serviceChild.name}}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-form>
          </div>
            <at-button type="primary" @click="submit" style="margin-left: 40%;margin-top: -20px">创建账号</at-button>
        </div>
      </form>
    </div>

    <el-dialog title="账号属性与结算方式相关详情" :visible.sync="dialog" width="30%" center>
      <span>提示内容</span>
    </el-dialog>
  </div>

</template>

<script>
  import VDistpicker from 'v-distpicker'

  export default {
    components: {VDistpicker},
    data() {
      let checkAge = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('年龄不能为空'));
        } else {
          if (parseInt(value) < 18) {
            callback(new Error('必须年满18岁'));
          } else {
            callback();
          }
        }
      };
      let validatePwd2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        serviceList: [],
        phoneCode: '',
        activeName: 'employer',
        dialog: false,
        step_1: true,
        step_2: false,
        coding: 0,
        registerForm: {
          service: [],
          role: null,
          username: '',
          password: '',
          pwd2: '',
          name: '',
          phone: '',
          prov: '',
          city: '',
          introduction: '',
          married: '',
          bankCard: '',
          idCard: '',
          experience: '',
          education: '',
          sex: '',
          age: '',
          code: '',
        },
        rules: {
          username: [
            {required: true, message: '请输入帐号名', trigger: 'blur'},
            {min: 6, max: 20, message: '长度在6到20个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z0-9_@]{6,20}$/, message: '帐号只能由字母，数字，_@组成'}
          ],
          password: [
            {required: true, message: '请输入登录密码', trigger: 'blur'},
            {min: 6, max: 20, message: '长度在6到20个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z0-9_!@#$%^&*.]{6,20}$/, message: '密码只能由字母，数字，特殊符号组成'}
          ],
          pwd2: [
            {required: true, message: '请再次输入密码', trigger: 'blur'},
            {validator: validatePwd2, trigger: 'blur'}
          ],
          name: [
            {required: true, message: '请输入真实姓名', trigger: 'blur'},
          ],
          age: [{validator: checkAge, trigger: 'blur'}, {pattern: /^[1-9]\d*$/, message: '请输入数字'}],
          sex: [
            {required: true, message: '请选择性别', trigger: 'change'},
          ],
          address: [
            {required: true, message: '请输入详细地址', trigger: 'blur'},
          ],
          education: [
            {required: true, message: '请选择学历', trigger: 'change'},
          ],
          service: [
            {required: true, message: '请至少选择一项', trigger: 'change'},
          ],
          experience: [
            {required: true, message: '请选择工作年限', trigger: 'change'},
          ],
          idCard: [
            {required: true, message: '请输入正确卡号，提交后不可修改', trigger: 'blur'},
            {pattern: /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/, message: '请输入正确格式的身份证号'}
          ],
          bankCard: [
            {required: true, message: '请输入正确卡号，提交后不可修改', trigger: 'blur'},
            {pattern: /^([1-9])(\d{14}|\d{18})$/, message: '请输入正确格式的银行卡号'}
          ],
          phone: [
            {required: true, message: '请输入手机号码', trigger: 'blur'},
            {pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号码'}
          ],
          code: [
            {required: true, message: '请输入短信验证码', trigger: 'blur'},
            {pattern: /^[0-9]{6}$/, message: '请输入正确的短信验证码'}
          ],
        }
      }
    },
    async created() {
      this.init();
    },
    methods: {
      async init() {
        this.serviceList = [];
        let res = await this.$api('Service/getAll', {});
        //得到所有的父类
        res.serviceList.forEach(item => {
          if (item.parent === null) this.serviceList.push(item)
        });
        this.serviceList.forEach(it => {
          let childrenType = [];

          res.serviceList.forEach(item => {
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
      handleClick(tab) {
        this.registerForm.role = tab.name
      },
      step1Fn() {
        this.$refs['registerForm_1'].validate((valid) => {
          //可以在这里进行验证用户名是否重复

          if (valid) {
            this.step_1 = false;
            this.step_2 = true;
          }
        });
      },
      async get_code() {
        this.$refs.registerForm_2.validateField('phone');
        let reg = /^1[34578]\d{9}$/;
        if (!this.registerForm.phone || !reg.test(this.registerForm.phone)) {
          return false;
        }
        let res = await this.$api('User/getCode', {phone: this.registerForm.phone});
        //give  me 15895345237
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.phoneCode = res.data;
        this.coding = 60;
        let reduce = setInterval(() => {
          this.coding--;
          if (this.coding <= 0) {
            clearInterval(reduce);
          }
        }, 1000);
      },
      async submit() {
        this.$refs['registerForm_2'].validate(async valid => {
          this.registerForm.role = this.registerForm.role === 'employee' ? 200 : 300;
          if (valid) {
            if (parseInt(this.registerForm.code) === this.phoneCode) {
              let res = await this.$api('User/register', this.registerForm);
              if (res.code === 200) {
                this.$message.success('注册成功，请重新登录');
                // localStorage.setItem('username', res.username);
                this.$router.push('/login');
              } else {
                if (res.msg === '此账号已存在') {
                  this.step_1 = true;
                  this.step_2 = false;
                }
                this.$message({
                  type: 'error',
                  message: res.msg
                });
              }
            } else {
              this.$message({
                type: 'error',
                message: "验证码错误"
              });
            }

          }
        });
        this.activeName = this.registerForm.role === 200 ? 'employee' : 'employer';
      },
      selectProv(data) {
        this.registerForm.prov = data.value;
      },
      selectCity(data) {
        this.registerForm.city = data.value;
      },
    }
  }
</script>

<style>
  .type {
    color: #606266;
    font-weight: 500;
    font-size: 14px;
    cursor: pointer;
    display: block;
    position: relative;
    white-space: nowrap;
  }

  .do {
    margin-left: 340px;
    margin-top: -85px;
  }

  .does {
    margin-left: 650px;
    margin-top: -85px;
  }

  .register__register-form {
    /*width: 400px;*/
  }

  .code {
    width: 192px;
  }

  .code-button {
    margin-left: 10px;
    width: 112px;
  }

  .form-wrap .register__bd {
    border: 1px solid #ebebeb;
    margin-bottom: 30px;
    padding: 20px
  }

  .form-wrap .register__form-title {
    font-size: 26px;
    color: #333;
    margin: 40px 0 20px
  }

  .form-wrap .register__form-title:first-of-type {
    margin-top: 0
  }

  .register__progress {
    *zoom: 1
  }

  .register__progress:after {
    content: ".";
    display: block;
    height: 0;
    visibility: hidden;
    clear: both
  }

  .register__progress-wrap {
    padding: 20px 0
  }

  .register__progress {
    position: relative;
    background: #f7f8fa
  }

  .register__progress:before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 33.333%;
    background-image: -webkit-linear-gradient(left, #f8f6f6, #ffd9c3);
    background-image: linear-gradient(90deg, #f8f6f6, #ffd9c3)
  }

  .register__progress li {
    position: relative;
    z-index: 1;
    float: left;
    width: 33.333%;
    height: 80px;
    line-height: 22px;
    text-align: center;
    font-size: 14px;
    color: #95a7b2
  }

  .register__progress li:before {
    content: "";
    display: block;
    width: 26px;
    height: 29px;
    margin: 15px auto 0;
    background: url("//image.9game.cn/s/uae/g/1m/ugroup/images/public/progress-num.png") no-repeat 0 0
  }

  .register__progress .register__item1 {
    color: #ec8c47
  }

  .register__progress .register__item2:before {
    background-position: 0 -30px
  }

  .register__progress .register__item3:before {
    background-position: 0 -90px
  }

  .register__progress.step2:before {
    width: 66.666%
  }

  .register__progress.step2 .register__item2 {
    color: #ec8c47
  }

  .register__progress.step2 .register__item2:before {
    background-position: 0 -60px
  }

  .register__progress.step3:before {
    width: 100%
  }

  .register__progress.step3 .register__item2 {
    color: #ec8c47
  }

  .register__progress.step3 .register__item2:before {
    background-position: 0 -60px
  }

  .register__progress.step3 .register__item3 {
    color: #ec8c47
  }

  .register__progress.step3 .register__item3:before {
    background-position: 0 -120px
  }

  .register__progress {
    *zoom: 1
  }

  .register__progress:after {
    content: ".";
    display: block;
    height: 0;
    visibility: hidden;
    clear: both
  }

  .progress-wrap {
    padding: 20px 0
  }

  .register__progress {
    position: relative;
    background: #f7f8fa
  }

  .register__progress:before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 33.333%;
    background-image: -webkit-linear-gradient(left, #f8f6f6, #ffd9c3);
    background-image: linear-gradient(90deg, #f8f6f6, #ffd9c3)
  }

  .register__progress li {
    position: relative;
    z-index: 1;
    float: left;
    width: 33.333%;
    height: 80px;
    line-height: 22px;
    text-align: center;
    font-size: 14px;
    color: #95a7b2
  }

  .register__progress li:before {
    content: "";
    display: block;
    width: 26px;
    height: 29px;
    margin: 15px auto 0;
    background: url("//image.9game.cn/s/uae/g/1m/ugroup/images/public/progress-num.png") no-repeat 0 0
  }

  .register__progress .register__item1 {
    color: #ec8c47
  }

  .register__progress .register__item2:before {
    background-position: 0 -30px
  }

  .register__progress .register__item3:before {
    background-position: 0 -90px
  }

  .progress.step2:before {
    width: 66.666%
  }

  .register__progress.step2 .register__item2 {
    color: #ec8c47
  }

  .register__progress.step2 .register__item2:before {
    background-position: 0 -60px
  }

  .register__progress.step3:before {
    width: 100%
  }

  .register__progress.step3 .register__item2 {
    color: #ec8c47
  }

  .register__progress.step3 .register__item2:before {
    background-position: 0 -60px
  }

  .register__progress.step3 .register__item3 {
    color: #ec8c47
  }

  .register__progress.step3 .register__item3:before {
    background-position: 0 -120px
  }

  .register-content {
    width: 1200px;
    margin: 0 auto
  }

  .form-wrap .register__bd {
    min-height: 320px
  }

  .form-step2 {
    display: none
  }

  .form-step2 .label {
    width: 84px
  }
</style>
