<template >
  <!-- 内容区 -->
  <div style="background-color: #F2F6FC;width: 100%;height: 100%">

    <div class="page-content">
      <form>
        <div class="form-wrap">
          <div class="forget__bd">
            <h2 class="forget__form-title">忘记密码</h2>
            <hr style=" height:2px;border:none;border-top: 2px ridge cornflowerblue;">
            <el-form size="large" ref="form" label-width="85px" :model="form"
                     :rules="rules" class="forget__forget-form">
              <el-form-item label="帐号" prop="username">
                <el-input v-model="form.username" placeholder="帐号"></el-input>
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="手机号码用于找回密码、接收通知短信"></el-input>
              </el-form-item>
              <el-form-item label="验证码" prop="code">
                <el-input v-model="form.code" class="code" placeholder="短信验证码"></el-input>
                <el-button class="code-button" v-if="coding <= 0" @click="get_code">获取验证码</el-button>
                <el-button class="code-button" type="warning" v-else disabled>{{coding}} 秒</el-button>
              </el-form-item>
              <el-form-item label="登录密码" prop="password">
                <el-input v-model="form.password"
                          placeholder="请输入登录密码，字数6-20字，区分大小写" type="password"></el-input>
              </el-form-item>
              <el-form-item label="确认密码" prop="pwd2">
                <el-input v-model="form.pwd2"
                          placeholder="请再次输入密码，字数6-20字，区分大小写" type="password"></el-input>
              </el-form-item>
              <el-form-item>
                <at-button type="primary" @click="submit">修改密码</at-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </form>
    </div>
  </div>

</template>

<script>
  export default {
    data() {
      let validatePwd2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.form.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        coding: 0,
        phoneCode: '',
        form: {
          username: '',
          password: '',
          pwd2: '',
          phone: '',
          code: ''
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
          phone: [
            {required: true, message: '请输入手机号码', trigger: 'blur'},
            {pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号码'}
          ],
          code: [
            {required: true, message: '请输入短信验证码', trigger: 'blur'},
            {pattern: /^[0-9]{6}$/, message: '请输入正确的短信验证码'}
          ]
        }
      }
    },
    methods: {
      async get_code() {
        this.$refs.form.validateField('username');
        this.$refs.form.validateField('phone');
        let reg = /^1[34578]\d{9}$/;
        if (!this.form.phone || !reg.test(this.form.phone)) {
          return false;
        }
        let res = await this.$api('User/getForgetCode', {username: this.form.username, phone: this.form.phone});
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
        this.$refs['form'].validate(async valid => {
          if (valid) {
            if (parseInt(this.form.code) === this.phoneCode) {
              let res = await this.$api('User/forgetPwd', this.form);
              this.$message({
                type: res.code === 200 ? 'success' : 'error',
                message: res.msg,
                duration:5000
              });
              this.$router.push('/login');
            } else {
              this.$message({
                type: 'error',
                message: "验证码输入错误"
              });
            }
          }
        });
      }
    }
  }
</script>

<style>
  .forget__forget-form {
    width: 400px;
    margin-top: 20px;
  }

  .code {
    width: 192px;
  }

  .code-button {
    margin-left: 10px;
    width: 112px;
  }

  .form-wrap .forget__bd {
    border: 1px solid #ebebeb;
    margin-bottom: 30px;
    padding: 20px;
  }

  .form-wrap .forget__form-title {
    font-size: 26px;
    color: #333;
    margin: 40px 0 20px
  }

  .form-wrap .forget__form-title:first-of-type {
    margin-top: 0
  }
</style>
