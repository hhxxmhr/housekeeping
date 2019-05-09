<template>
  <div>
    <div>
      <el-form ref="form" :model="form" label-width="80px" v-loading="loading" :inline="true">
        <div>
          <img v-if="userInfo.photo" :src="'http://localhost:8888/'+userInfo.photo" alt=""
               style="margin-left: 0%;vertical-align:middle" class="avatar">
          <img src="../../../src/assets/default.jpg" class="avatar" v-else
               style="margin-left: 0%;vertical-align:middle">
          <div>
            <el-tabs stretch v-model="activeName" @tab-click="handleClick"
                     style="width: 70%;margin-left: 12%;margin-top: -70px;align-content: center">
              <el-tab-pane label="全部评价" name="first" style="margin-left: 45%;"></el-tab-pane>
              <el-tab-pane label="有图评价" name="second" style="margin-left: 45%;"></el-tab-pane>
            </el-tabs>
            <div>
              <el-button type="text" style="float: right;margin-top: -50px">共{{comments.length}}条评论</el-button>
              <el-button type="text" @click="deleteComment()" style="float: right;margin-right:100px;margin-top: -50px">
                删除评论
              </el-button>
            </div>
          </div>
        </div>
        <div style="margin-top: 3%;margin-left: 10%;width: 80%">
          <el-checkbox-group v-model="checkedComments">
            <el-timeline>
              <el-timeline-item v-for="(comment,index) in comments" :key="index" placement="top"
                                :timestamp="$formatTimeDay(comment.createTime)+' '+'雇员：'+comment.employeeName+' -->'+'雇主：'+comment.employerName+' -->'+'服务类型：'+comment.serviceName+' -->'+'服务等级：'+comment.rankName+' '">

                <el-card>
                  <el-rate v-model="comment.rate" disabled show-score text-color="#ff9900" score-template="{value}"
                           style="float: right"></el-rate>
                  <p>{{comment.comment}}</p>
                  <img v-if="comment.photo" v-for="(photo,index) in getPhoto(comment.photo)" :key="index"
                       :src="'http://localhost:8888/'+photo" alt="" class="commentPhoto">
                  <el-checkbox :label="comment.id" style="position: absolute;right: 0px;bottom: 0px"></el-checkbox>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </el-checkbox-group>
        </div>

      </el-form>
    </div>

  </div>

</template>

<script>
  export default {
    name: "allComment",
    data() {
      return {
        checkedComments: [],
        comments: [],
        commentsWithPhoto: [],//带图片的数据
        activeName: 'first',
        loading: true,
        dialogImageUrl: '',// 图片预览地址
        dialogVisible: false,// 上传图片预览
        uploadComplete: true, // 图片上传完成状态
        userInfo: {},//当前登陆人员的信息
        form: {
          photo: '',  // 活动图片
          comment: '',
          rate: null,
          createTime: this.timestamp(),//当前时间戳
          orderId: null,//从订单页面跳转过来的
        },
      }
    },
    created() {
      this.init();
    },

    watch: {},
    methods: {
      //将数据库里面的图片字符串以逗号分隔
      getPhoto(photo) {
        return (photo || "").split(",");
      },
      //删除评论
      async deleteComment() {
        if (this.checkedComments.length === 0) {
          this.$message.warning("请选择要删除的评论");
        } else {
          let res = await this.$api("Comment/delete", {ids: this.checkedComments});
          this.$message({
            type: res.code === 200 ? 'success' : 'error',
            message: res.msg
          });
          this.init();
        }
      },
      handleClick(tab) {
        if (tab.name === 'first') {
          this.init();
        } else {
          //筛选有图片的数据
          this.commentsWithPhoto = [];
          this.comments.forEach(comment => {
            if (comment.photo != null) {
              this.commentsWithPhoto.push(comment);
            }
          });
          this.comments = this.commentsWithPhoto;
        }
      },

      async init() {
        this.loading = true;
        //根据id获得员工信息
        let res = await this.$api("User/findUserById", {id: window.$mine.id});
        this.userInfo = res.data;
        let resp = await this.$api("Comment/getAllDetail", {eid: window.$mine.id, role: window.$mine.role});
        this.comments = resp.data;
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

  .commentPhoto {
    width: 85px;
    height: 80px;
    display: inline-block;
    vertical-align: middle;
    margin-top: 2%;
    margin-left: 1%;
    /*border-radius: 80%*/
  }
</style>
