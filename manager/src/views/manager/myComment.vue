<template>
  <div>
    <div>
      <el-form ref="form" :model="form" label-width="80px" v-loading="loading">
        <div>
          <img v-if="employerInfo.photo" :src="'http://localhost:8888/'+employerInfo.photo" alt=""
               style="margin-left: 0%;vertical-align:middle" class="avatar">
          <img src="../../../src/assets/xiao.jpg" class="avatar" v-else style="margin-left: 0%;vertical-align:middle">
          <el-tabs stretch v-model="activeName" @tab-click="handleClick"
                   style="width: 70%;margin-left: 15%;margin-top: -70px;align-content: center">
            <el-tab-pane label="全部评价" name="first" style="margin-left: 45%;"></el-tab-pane>
            <el-tab-pane label="有图评价" name="second" style="margin-left: 45%;"></el-tab-pane>
          </el-tabs>
          <el-button type="primary" plain round size="small" style="float: right;margin-top: -50px"
                     icon="el-icon-edit"
                     @click="writeComment">写评价
          </el-button>
        </div>
        <div style="margin-top: 3%">
          <el-timeline>
            <el-timeline-item timestamp="时间---服务打星---服务内容" placement="top">
              <el-card>
                <h4></h4>
                <p>评论内容/图片</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="时间---服务内容---服务打星" placement="top">
              <el-card>
                <h4></h4>
                <p>评论内容/图片</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item timestamp="时间---服务内容---服务打星" placement="top">
              <el-card>
                <h4></h4>
                <p>评论内容/图片</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
        <!--<div style="margin-top: 20px;margin-left: 20%">
          <el-upload
            :limit="5"
            action="/api/upload/uploadPic" multiple
            accept="image/png, image/jpeg"
            list-type="picture-card"
            :before-upload="beforeUploadPicture"
            :on-preview="handlePictureCardPreview"
            :on-progress="uploadProgress"
            :on-remove="handleRemove"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :show-file-list="true">
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" append-to-body size="tiny">
            <img width="100%" :src="dialogImageUrl" alt="photo">
          </el-dialog>
        </div>-->
      </el-form>
    </div>

  </div>

</template>

<script>
  export default {
    name: "doComment",
    data() {
      return {
        activeName: 'first',
        loading: true,
        dialogImageUrl: '',// 图片预览地址
        dialogVisible: false,// 上传图片预览
        uploadComplete: true, // 图片上传完成状态
        rate: null,
        employerInfo: {},//此单服务人员的信息
        serviceInfo: {},//此项服务的信息
        form: {
          photo: '',  // 活动图片
          comment: '',
          rate: null,
          orderId: null,//从订单页面跳转过来的
        },
        remnant: 200,//剩余评论字数
      }
    },
    created() {
      this.initQuery();
      this.init();
      this.initForm();
    },

    watch: {},
    methods: {
      countWords() {
        const txtVal = this.form.comment.length;
        this.remnant = 200 - txtVal;
      },
      handleClick(tab) {
        if (tab.name === 'first') {

        } else {

        }
      },
      writeComment() {
        //评论成功跳转到我的订单列表
        this.$router.push({
          path: "/manager/orders",
          query: {eid: window.$mine.id}
        });
      },
      async onSubmit() {
        //发表评论
        let res = await this.$api("Comment/add", this.form);
        //更新订单状态
        await this.$api("Order/edit", {id: this.form.orderId, state: 4, rate: this.form.rate});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        //评论成功跳转到我的评论列表
        this.$router.push("/manager/myComment")
      },
      initForm() {
        if (this.$refs.form) {
          this.$refs.form.resetFields();
        }
      },
      // 上传图片前调用方法
      beforeUploadPicture(file) {
        if (file.size > 2 * 1024 * 1024) {
          this.$message.error("上传图片不能大于2M");
          return false;
        }
      },
      // 上传图片时调用
      uploadProgress(event, file, fileList) {
        this.uploadComplete = false;
      },
      // 上传图片成功
      uploadSuccess(res, file, fileList) {
        console.log(fileList)
        this.uploadComplete = true;
        this.fileChange(fileList);
      },
      // 上传图片出错
      uploadError(err, file, fileList) {
        this.$message.error("上传出错");
      },
      // 移除图片
      handleRemove(file, fileList) {
        this.fileChange(fileList);
      },
      // 设置photo值
      fileChange(fileList) {
        let temp_str = '';
        if (fileList.length > 0) {
          for (let i = 0; i < fileList.length; i++) {
            if (fileList[i].response) {
              if (fileList[i].response.code === 200) {
                if (i === 0) {
                  temp_str += fileList[i].response.data;
                } else {
                  // 最终photo的格式是所有已上传的图片的url拼接的字符串（逗号隔开）
                  temp_str += ',' + fileList[i].response.data;
                }
              }
            }
          }
        }
        this.form.photo = temp_str;
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      initQuery() {
        Object.assign(this.form, this.$route.query);
        this.form.orderId = this.form.orderId != null ? parseInt(this.form.orderId) : null;
      },
      async init() {
        this.loading = true;
        //根据id获得员工信息
        let res = await this.$api("User/findUserById", {id: window.$mine.id});
        this.employerInfo = res.data;
        //根据订单id获得服务信息
        this.serviceInfo = await this.$api("Service/findServiceByOrder", {orderId: this.form.orderId});
        this.loading = false;
      }
    },
  }
</script>

<style scoped>
  .avatar {
    width: 85px;
    height: 80px;
    display: inline-block;
    vertical-align: middle;
    border-radius: 80%
  }
</style>
