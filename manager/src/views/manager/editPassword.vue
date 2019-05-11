<template>
  <div style="margin-left: 32%;margin-top: 5%">
    <el-form label-position="right"
             label-width="80px"
             :model="user"
             :rules="rules"
             ref="user">
      <el-form-item label="原始密码"
                    prop="old_pwd">
        <el-input type="password"
                  v-model="user.old_pwd"></el-input>
      </el-form-item>
      <el-form-item label="新的密码"
                    prop="new_pwd">
        <el-input type="password"
                  v-model="user.new_pwd"></el-input>
      </el-form-item>
      <el-form-item label="确认密码"
                    prop="again_pwd">
        <el-input type="password"
                  v-model="user.again_pwd"></el-input>
      </el-form-item>
      <el-form-item>
        <at-button @click="edit_submit" type="primary" auto-disable-as-loading style="margin-left: 66px">确认</at-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    components: {},
    props: {},
    data() {
      var validateAgainPwd = (rule, value, callback) => {
        if (value !== this.user.new_pwd) callback(new Error("两次密码不一致"));
        else callback();
      };
      return {
        user: {
          old_pwd: "",
          new_pwd: "",
          again_pwd: ""
        },
        rules: {
          old_pwd: [
            {required: true, message: "请输入原密码", trigger: "blur"},
            {max: 20, message: "长度不超过20个字符", trigger: "blur"}
          ],
          new_pwd: [
            {required: true, message: "请输入新密码", trigger: "blur"},
            {max: 20, message: "长度不超过20个字符", trigger: "blur"}
          ],
          again_pwd: [
            {required: true, message: "请再次输入新密码", trigger: "blur"},
            {validator: validateAgainPwd, trigger: "blur"},
            {max: 20, message: "长度不超过20个字符", trigger: "blur"}
          ]
        }
      };
    },

    watch: {},
    computed: {},
    methods: {
      async edit_submit(com) {
        let able = false;
        this.$refs["user"].validate(valid => {
          if (valid) {
            able = true;
          } else {
            return false;
          }
        });
        if (able) {
          let res = await this.$api("User/editPwd", {
            old_pwd: this.user.old_pwd,
            new_pwd: this.user.new_pwd
          }, com);
            this.$message({
              message: res.msg,
              type: res.code===200?"success":"error"
            });
            this.$emit("logout");
        }
      }
    },
    created() {
    },
    mounted() {
    }
  };
</script>
<style lang="scss" scoped>
  .el-input {
    width: 250px;
  }
</style>
