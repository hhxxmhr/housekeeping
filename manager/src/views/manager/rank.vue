<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-button @click="dialog_show()"
                   icon="el-icon-plus" style="float: right"
                   type="primary">添加等级
        </el-button>
        <el-input placeholder="请输入要查询的等级名称" v-model="searchForm.name"
                  style="width: 250px"></el-input>
        <el-button @click="search"
                   style="margin-left: 10px;"
                   icon="el-icon-search"
                   type="primary">搜索
        </el-button>
      </el-row>
      <el-dialog width="400px"
                 :title="dialog_title"
                 :visible.sync="dialog_visible"
                 :before-close="close_dialog"
                 :close-on-click-modal="false">
        <el-form :model="rank"
                 :rules="rules"
                 ref="rank">
          <el-form-item label="等级名称"
                        prop="name"
                        :label-width="formLabelWidth">
            <el-input v-model="rank.name" style="width: 180px"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="额外补贴"
                        prop="money"
                        :label-width="formLabelWidth">
            <el-input v-model="rank.money" style="width: 180px" auto-complete="off">
              <template slot="append">元/次</template>
            </el-input>
          </el-form-item>

        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="close_dialog()">取消</el-button>
          <at-button type="primary"
                     @click="addOrEditUser">确定
          </at-button>
        </div>
      </el-dialog>
      <el-table :data="rankList" border style="width: 100%">
        <el-table-column prop="id" label="ID" align="center"></el-table-column>
        <el-table-column prop="name" label="等级" align="center"></el-table-column>
        <el-table-column prop="money" label="额外补贴(元/次)" align="center"></el-table-column>
        <el-table-column prop="enable" label="操作" header-align="center" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="warning" @click="dialog_show(scope.row.id)">编辑</el-button>
            <at-button size="mini" type="danger" confirmText="确定要删除此等级" @click="delRank(scope.row.id)">删除</at-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        searchForm: {
          id: null,
          name: ''
        },
        rank: {
          id: null,
          name: '',
          money: null,
        },
        rankList: [],
        formLabelWidth: "80px",
        dialog_title: '',
        dialog_visible: false,
        rules: {
          name: [
            {required: true, message: "请输入等级名称", trigger: "blur"},
            {max: 4, message: "长度在4个字符", trigger: "blur"}
          ],
          money: [
            {required: true, message: "请输入基础工资", trigger: "blur"},
          ],
        },
      };
    },
    async created() {
      this.initQuery();
      this.init();
      let res = await this.$api('User/getMineInfo', {});
      console.log(res)
      console.log("rank")
    },
    watch: {
      '$route'() {
        this.searchForm.id = null;
        this.initQuery();
        this.init();
      }
    },
    methods: {
      async init() {
        let res = await this.$api('Rank/getAll', this.searchForm);
        this.rankList = res.rankList;
      },
      async delRank(id) {
        let res = await this.$api('Rank/delRank', {id: id});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
      dialog_show(id) {
        if (id == null) {
          this.dialog_title = "新增等级";
          this.rank.name = '';
          this.rank.money = null;

        } else {
          this.dialog_title = "编辑等级";
          let tmp = this.rankList.find(item => item.id === id);
          this.rank.id = parseInt(tmp.id);
          this.rank.name = tmp.name;
          this.rank.money = parseInt(tmp.money);
        }
        this.dialog_visible = true;
      },
      close_dialog() {
        this.dialog_visible = false;
        setTimeout(() => {
          this.$refs["rank"].resetFields();
        }, 200);
      },
      async addOrEditUser() {
        this.rank.id = this.rank.id ? parseInt(this.rank.id) : null;
        this.$refs["rank"].validate(async valid => {
          if (valid) {
            let res;
            if (this.rank.id == null) {
              res = await this.$api("Rank/add", {
                name: this.rank.name,
                money: parseInt(this.rank.money),
              });
            } else {
              res = await this.$api("Rank/edit", {
                id: this.rank.id,
                name: this.rank.name,
                money: parseInt(this.rank.money),
              });
            }
            this.$message({
              type: res.code === 200 ? 'success' : 'error',
              message: res.msg
            });
            this.close_dialog();
            await this.init();
          }
        });
      },
      search() {
        this.$router.push({
          path: this.$route.path,
          query: this.searchForm
        });
      },
      initQuery() {
        Object.assign(this.searchForm, this.$route.query);
        this.searchForm.id = this.searchForm.id ? parseInt(this.searchForm.id) : null;
      }
    }
  }
</script>

<style scoped>

</style>
