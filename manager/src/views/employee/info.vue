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
      <!--可做服务-->
      <!--<el-form-item label="可做服务"
                    prop="sex">
        <el-radio v-model="user.sex" label="男">男</el-radio>
        <el-radio v-model="user.sex" label="女" style="margin-left: 5px">女</el-radio>
      </el-form-item>-->
      <el-form-item label="学历"
                    prop="experience" style="margin-left: 8%">
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
      <el-form-item label="工作经验"
                    prop="experience">
        <el-select v-model="user.experience" placeholder="请选择">
          <el-option v-for="item in Object.entries(UserExperience)"
                     :key="item[0]"
                     :label="item[1]"
                     :value="parseInt(item[0])"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="婚否" style="margin-left: 8%"
                    prop="married">
        <el-select v-model="user.married" placeholder="请选择">
          <el-option label="是" value="是"></el-option>
          <el-option label="否" value="否"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="身份证号"
                    prop="idCard">
        <el-input type="text" disabled
                  v-model="user.idCard"></el-input>
      </el-form-item>
      <el-form-item label="银行卡号"
                    prop="bankCard">
        <el-input type="text" disabled
                  v-model="user.bankCard"></el-input>
      </el-form-item>
      <el-form-item label="当前服务" style="margin-left: 8%">
        <el-tag type="info" v-for="(item,index) in user.serviceRank" :key="index">{{item.service+'-'+item.rank}}
        </el-tag>
      </el-form-item>
      <el-form-item label="添加服务" >
        <el-select v-model="user.service" placeholder="请选择">
          <el-option
            v-for="item in noService"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自我介绍" style="margin-left: 8%"
                    prop="introduction">
        <el-input type="textarea" style="width: 590px"
                  v-model="user.introduction"></el-input>
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
        serviceList: [],
        fileName: '',
        photo: '',
        serviceRank: [],
        noService: [],//没有选择的服务，下拉框
        user: {
          photo: '',
          service: null,//添加服务
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
          this.user.age = parseInt(this.user.age);
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
        let r = await this.$api("User/findUserInfos", {});
        let all = r.data;
        this.serviceRank = [];
        all.forEach(it => {
          let service_rank = {};
          service_rank.service = it.serviceName;
          service_rank.serviceId = it.serviceId;
          service_rank.rank = it.rankName;
          this.serviceRank.push(service_rank);
        });
        this.user["serviceRank"] = this.serviceRank;
        let amp = this.serviceRank;
        //获得所有的服务
        let tmp = await this.$api('Service/getAll', {});
        //获得已选择的id数组
        let ids = [];
        this.noService = [];
        amp.forEach(it => {
          ids.push(it.serviceId);
        });
        tmp.serviceList.forEach(item => {
          if (!ids.includes(item.id) && item.parent !== null) {
            this.noService.push(item);
          }
        });
      },
    },
    async created() {
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
