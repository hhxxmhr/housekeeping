<!--员工资料编辑-->
<template>
  <div class="wrapper">
    <!--<img :src="photo">-->
    <el-form label-position="right"
             label-width="80px"
             :model="user"
             :rules="rules" :inline="true"
             ref="user">
      <el-upload
        class="avatar-uploader"
        action="/api/upload/uploadPic"
        ref="upload"
        :show-file-list="false" style="margin-left: 40%"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <img v-if="user.photo" :src="'http://localhost:8888/'+user.photo" alt="" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <el-form-item label="账号" style="margin-top: 10px;margin-left: 8%"
                    prop="username">
        <el-input type="text"
                  v-model="user.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" style="margin-top: 10px"
                    prop="password">
        <el-input type="text"
                  v-model="user.password"></el-input>
      </el-form-item>
      <el-form-item label="手机" style="margin-top: 10px"
                    prop="phone">
        <el-input type="text"
                  v-model="user.phone"></el-input>
      </el-form-item>
      <el-form-item label="姓名" style="margin-left: 8%"
                    prop="name">
        <el-input type="text"
                  v-model="user.name"></el-input>
      </el-form-item>
      <el-form-item label="年龄"
                    prop="age">
        <el-input type="text"
                  v-model="user.age"></el-input>
      </el-form-item>
      <el-form-item label="性别"
                    prop="sex">
        <el-radio v-model="user.sex" label="男">男</el-radio>
        <el-radio v-model="user.sex" label="女" style="margin-left: 5px">女</el-radio>
      </el-form-item>

      <el-form-item label="等级" style="margin-left: 8%"
                    prop="grade">
        <el-input type="text" disabled
                  v-model="user.rank"></el-input>
      </el-form-item>
      <el-form-item label="学历"
                    prop="experience">
        <el-select v-model="user.education" placeholder="请选择">
          <el-option v-for="item in Object.entries(UserDegree)"
                     :key="item[0]"
                     :label="item[1]"
                     :value="parseInt(item[0])"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="区域">
        <v-distpicker :province="user.prov" :city="user.city" @province="selectProv" @city="selectCity"
                      hide-area></v-distpicker>
      </el-form-item>
      <el-form-item label="工作经验" style="margin-left: 8%"
                    prop="experience">
        <el-select v-model="user.experience" placeholder="请选择">
          <el-option v-for="item in Object.entries(UserExperience)"
                     :key="item[0]"
                     :label="item[1]"
                     :value="parseInt(item[0])"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="身份证号"
                    prop="idCard">
        <el-input type="text" disabled
                  v-model="user.idCard"></el-input>
      </el-form-item>
      <el-form-item label="婚否"
                    prop="married">
        <el-select v-model="user.married" placeholder="请选择">
          <el-option label="是" value="是"></el-option>
          <el-option label="否" value="否"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自我介绍" style="margin-left: 8%"
                    prop="introduction">
        <el-input type="textarea" style="width: 590px"
                  v-model="user.introduction"></el-input>
      </el-form-item>
      <el-form-item label="银行卡号"
                    prop="bankCard">
        <el-input type="text" disabled
                  v-model="user.bankCard"></el-input>
      </el-form-item>
      <br>
      <el-form-item>
        <el-button type="primary" @click="edit_submit" style="margin-left: 570px">确认</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import VDistpicker from 'v-distpicker'

  export default {

    components: {VDistpicker},
    props: {},
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
      let validatePhone = (rule, phone, callback) => {
        if (!phone) {
          return callback(new Error('手机号不能为空'));
        } else {
          if (/^1[34578]\d{9}$/.test(phone)) callback();
          else callback(new Error('手机号码格式错误'));
        }
      };
      return {
        fileName: '',
        photo: '',
        user: {
          photo: '',
        },
        rules: {
          name: {required: true, message: '姓名不能为空'},
          username: [
            {required: true, message: '请输入帐号名', trigger: 'blur'},
            {min: 4, max: 20, message: '长度在4到20个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z0-9_@]{4,20}$/, message: '帐号只能由字母，数字，_@组成'}
          ],
          password: [
            {required: true, message: '请输入登录密码', trigger: 'blur'},
            {min: 4, max: 20, message: '长度在4到20个字符', trigger: 'blur'},
            {pattern: /^[a-zA-Z0-9_!@#$%^&*.]{4,20}$/, message: '密码只能由字母，数字，特殊符号组成'}
          ],
          age: [{validator: checkAge, trigger: 'blur'}, {pattern: /^[1-9]\d*$/, message: '请输入数字'}],
          phone: {validator: validatePhone, trigger: 'blur'},
        }
      };
    },

    methods: {
      //上传图片
      handleAvatarSuccess(res, file) {
        /*let reader = new FileReader();
        reader.readAsDataURL(file.raw);
        reader.onload = () => {
          this.photo = reader.result;
          console.log(this.photo)
        };*/
        this.photo = URL.createObjectURL(file.raw);
        this.user.photo = res.data;
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      selectProv(data) {
        this.user.prov = data.value;
      },
      selectCity(data) {
        this.user.city = data.value;
      },
      async edit_submit() {
        let able = false;
        this.$refs["user"].validate(valid => {
          if (valid) {
            able = true;
          } else {
            return false;
          }
        });
        if (able) {
          let res = await this.$api("Employee/editInfo", this.user);
          this.$message({
            type: res.code === 200 ? 'success' : 'error',
            message: res.msg
          });
          await this.getInfo();
        }
      },
      async getInfo() {
        let res = await this.$api("User/getMineInfo", {});
        this.user = res.data;
      }
    },
    created() {
      this.getInfo();
    },
  };
</script>
<style lang="scss">
  .el-input {
    width: 250px;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

  /*label:before{
    color: red;
    content: "*";
  }*/
</style>
