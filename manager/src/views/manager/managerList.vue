<template>
  <div>
    <el-row class="margin-bottom">
      <el-button @click="dialog_show"
                 icon="el-icon-plus" style="float: right"
                 type="primary">添加管理员
      </el-button>
    </el-row>
    <el-dialog width="400px"
               title="新增管理员"
               :visible.sync="dialog_visible"
               :before-close="close_dialog"
               :close-on-click-modal="false">
      <el-form :model="user"
               :rules="rules"
               ref="user">
        <el-form-item label="用户名"
                      prop="username"
                      :label-width="formLabelWidth">
          <el-input v-model="user.username" style="width: 180px"
                    auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码"
                      prop="password"
                      :label-width="formLabelWidth">
          <el-col :span="15">
            <el-input v-model="user.password"
                      :disabled="isDisabled"
                      auto-complete="off"></el-input>
          </el-col>
          <el-col :span="4">
            <el-button style="background-color: #9da0a4;" @click="RandomPassword">随机密码</el-button>
          </el-col>
        </el-form-item>

        <el-form-item label="启用"
                      prop="state"
                      :label-width="formLabelWidth">
          <el-switch v-model="user.state"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button @click="close_dialog">取消</el-button>
        <at-button type="primary"
                   @click="addUser">确定
        </at-button>
      </div>
    </el-dialog>
    <el-table :data="users" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" align="center" width="100"
                       label="ID">
      </el-table-column>
      <el-table-column prop="username" align="center"
                       label="用户名">
      </el-table-column>
      <el-table-column prop="password" align="center"
                       label="密码">
      </el-table-column>
      <el-table-column prop="state" align="center"
                       label="状态" width="120">
        <template slot-scope="scope">
          <div :class="'font-color-'+(scope.row.state)">
            {{ManagerState[scope.row.state]}}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="enable" align="center"
                       v-if="checkRole()"
                       label="操作">
        <template slot-scope="scope">
          <at-button confirmText="确定启用此账号?" size="mini" type="success" v-if="scope.row.state===1"
                     @click="changeState(scope.row,0)">启用
          </at-button>
          <at-button confirmText="确定停用此账号?" size="mini" type="danger" v-if="scope.row.state===0"
                     @click="changeState(scope.row,1)">禁用
          </at-button>
          <at-button size="mini" type="primary" confirmText="确定要删除此用户"
                     @click="delUser(scope.row.id)">删除
          </at-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        rules: {
          username: [
            {required: true, message: "请输入用户名", trigger: "blur"},
            {max: 20, message: "长度在20个字符", trigger: "blur"}
          ],
          password: [
            {required: true, message: "请输入密码", trigger: "blur"},
            {max: 20, message: "长度不超过20个字符", trigger: "blur"}
          ],
        },
        user: {},
        formLabelWidth: "70px",
        isDisabled: false,
        users: [],
        dialog_visible: false,
      };
    },
    methods: {
      async delUser(id) {
        let resp = await this.$api('Manager/deleteAdmin', {id: id});
        if (resp.code === 200) {
          this.$message.success("删除成功");
        }
        this.getUsers();
      },
      checkRole() {
        return window.$mine.role === 1;
      },
      initUser() {
        this.user = {
          username: "",
          password: "",
          role: 100,
          state: false,
        };
      },
      async getUsers() {
        this.loading = true;
        let res = await this.$api("Manager/getAdminList", {});
        this.users = res.data;
        this.loading = false;
      },
      async changeState(row, state) {
        let resp = await this.$api('Manager/changeState', {id: row.id, state: state});
        if (resp.code === 200) {
          this.$message.success('操作成功');
          this.getUsers();
        }
      },
      dialog_show() {
        this.initUser();
        this.dialog_visible = true;
      },
      close_dialog() {
        this.dialog_visible = false;
        setTimeout(() => {
          this.$refs["user"].resetFields();
        }, 200);
      },
      async addUser() {
        this.$refs["user"].validate(async valid => {
          if (valid) {
            let res = await this.$api("Manager/addAdmin", {
              username: this.user.username,
              password: this.user.password,
              state: this.user.state === true ? 0 : 1,
              role: this.user.role,
              createTime: this.timestamp(),
            });
            this.$message({
              type: res.code === 200 ? 'success' : 'error',
              message: res.msg
            });
            this.close_dialog();
            await this.getUsers();
          }
        });
      },
      RandomPassword() {
        this.user.password = Math.random().toString(36).slice(2, 10);
      },
    },
    created() {
      this.loading = true;
      this.getUsers();
      this.initUser();
      this.loading = false;
    }
  };
</script>

<style scoped
       lang="scss">
</style>
