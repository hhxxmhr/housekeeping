<template>
  <div>
    <div>
      <el-form ref="form" :model="form" label-width="80px" v-loading="loading">
        <div>
          <img v-if="userInfo.photo" :src="'http://localhost:8888/'+userInfo.photo" alt=""
               style="margin-left: 0%;vertical-align:middle" class="avatar">
          <img src="../../../src/assets/default.jpg" class="avatar" v-else
               style="margin-left: 0%;vertical-align:middle">
          <el-tabs stretch v-model="activeName" @tab-click="handleClick"
                   style="width: 70%;margin-left: 15%;margin-top: -70px;align-content: center">
            <el-tab-pane label="全部评价" name="first" style="margin-left: 45%;"></el-tab-pane>
            <el-tab-pane label="有图评价" name="second" style="margin-left: 45%;"></el-tab-pane>
          </el-tabs>
          <el-button type="primary" plain round size="small" style="float: right;margin-top: -50px"
                     icon="el-icon-edit" v-if="loginRole===300"
                     @click="writeComment">写评价
          </el-button>
        </div>
        <div style="margin-top: 3%;margin-left: 10%;width: 80%">
          <el-timeline>
            <el-timeline-item v-for="(comment,index) in comments" :key="index" placement="top"
                              :timestamp="$formatTimeDay(comment.createTime)+' '+'服务类型：'+comment.serviceName+' '+'服务等级：'+comment.rankName+' '+'服务人员：'+comment.employeeName">
              <el-card>
                <el-rate v-model="comment.rate" disabled show-score text-color="#ff9900" score-template="{value}"
                         style="float: right"></el-rate>
                <p>{{comment.comment}}</p>
                <img v-if="comment.photo" v-for="(photo,index) in getPhoto(comment.photo)" :key="index"
                     :src="'http://localhost:8888/'+photo" alt="" class="commentPhoto">
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>

      </el-form>
    </div>

  </div>

</template>

<script>
  export default {
    name: "myComment",
    data() {
      return {
        loginRole: window.$mine.role,
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
        searchForm: {
          eid: window.$mine.id,
          role: window.$mine.role,
        }
      }
    },
    created() {
      this.init();
      this.initQuery();
    },

    watch: {},
    methods: {
      //将数据库里面的图片字符串以逗号分隔
      getPhoto(photo) {
        return (photo || "").split(",");
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
      writeComment() {
        //评论成功跳转到我的订单列表
        this.$router.push({
          path: "/manager/orders",
          query: {eid: window.$mine.id}
        });
      },
      initQuery() {
        Object.assign(this.searchForm, this.$route.query);
        this.searchForm.eid = this.searchForm.eid != null ? parseInt(this.searchForm.eid) : null;
        this.searchForm.role = this.searchForm.role != null ? parseInt(this.searchForm.role) : null;
      },
      async init() {
        this.loading = true;
        //根据id获得员工信息
        let res = await this.$api("User/findUserById", {id: window.$mine.id});
        this.userInfo = res.data;
        let resp = await this.$api("Comment/getAllDetail", this.searchForm);
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
