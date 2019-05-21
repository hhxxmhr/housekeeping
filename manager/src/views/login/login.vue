<template>
  <el-form :model="loginForm"
           :rules="rules"
           ref="loginForm"
           label-position="left"
           label-width="0px"
           class="login-container"
           @keyup.enter.native="loginSubmit()">
    <div>
      <h3 class="title">登录</h3>
      <a href="/register" style="margin-top:-58px;float: right;color: #409EFF">注册账号</a>
    </div>
    <div>
      <el-form-item prop="username">
        <el-input type="text"
                  v-model="loginForm.username"
                  placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="loginForm.password"
                  placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item>
        <verify-code @verifyFn="verifyFn"></verify-code>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button type="primary" style="width: 100%" :full="true"
                   :disabled="!verify || !loginForm.username || !loginForm.password"
                   @click="loginSubmit()">登录
        </el-button>
      </el-form-item>
      <a href="/forget" style="float: right;color: #409EFF;margin-top: -5px;">忘记密码</a>
    </div>
  </el-form>

</template>

<script>
  import Cookie from 'js-cookie';

  export default {
    data() {
      return {
        verify: false,
        loginForm: {
          username: '',
          password: '',
        },
        rules: {
          username: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'}
          ]
        },
      }
    },
    async created() {
    },
    methods: {
      verifyFn() {
        this.verify = true
      },
      async loginSubmit() {
        this.$refs.loginForm.validate(async valid => {
          if (valid) {
            let res = await this.$api('User/login', this.loginForm);
            window.$mine = res.data;
            if (res.code === 200) {
              let query = this.$route.query;
              if (query.target !== undefined && query.target !== '') {
                this.$router.push(query.target);
              } else {
                if (res.data.role === this.$role.Manager) {
                  this.$router.push('/manager/serviceSta');
                } else if (res.data.role === this.$role.Employee) {
                  this.$router.push('/employee/index');
                } else if (res.data.role === this.$role.Employer) {
                  this.$router.push('/employer/index');
                } else if (res.data.role === this.$role.Root) {
                  this.$router.push('/manager/managerList');
                }
              }
            }
            this.$message({
              type: res.code === 200 ? 'success' : 'error',
              message: res.msg
            });
          }
        });
      },
    }
  }
</script>

<style lang="scss"
       scoped>
  .login-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 30px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;

    .title {
      margin: 0 auto 40px auto;
      text-align: center;
      color: #505458;
      width: 100px;
    }

    .remember {
      margin: 0 0 35px 0;
    }
  }
</style>
