<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-button @click="dialog_show()"
                   icon="el-icon-plus" style="float: right"
                   type="primary">添加服务类别
        </el-button>
        <el-input placeholder="请输入要查询的服务名称" v-model="searchForm.name"
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
        <el-form :model="service"
                 :rules="rules"
                 ref="service">
          <el-form-item label="服务名称"
                        prop="name"
                        :label-width="formLabelWidth">
            <el-input v-model="service.name" style="width: 180px"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="基础工资"
                        prop="price"
                        :label-width="formLabelWidth">
            <el-input v-model="service.price" style="width: 180px" auto-complete="off">
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
      <el-table :data="serviceList" border style="width: 100%">
        <el-table-column prop="id" label="ID" align="center" width="150px"></el-table-column>
        <el-table-column prop="name" label="类别" align="center"></el-table-column>
        <el-table-column prop="childrenType" label="包含服务" align="center" width="400px">
          <template slot-scope="scope">
            <el-tag :key="tag" v-for="tag in scope.row.childrenType" closable
                    @close="handleClose(tag,scope.row.childrenType)"
                    :disable-transitions="false" size="medium" slot="reference">
              {{tag}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="基础服务金额(元/次)" align="center" width="150px"></el-table-column>
        <el-table-column prop="enable" label="操作" header-align="center" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="warning" @click="dialog_show(scope.row.id)">编辑</el-button>
            <at-button size="mini" type="danger" confirmText="确定要删除此服务类别" @click="delService(scope.row.id)">删除</at-button>
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
        service: {
          id: null,
          name: '',
          price: null,
          parent: null
        },
        serviceList: [],
        formLabelWidth: "80px",
        dialog_title: '',
        dialog_visible: false,
        rules: {
          name: [
            {required: true, message: "请输入服务名称", trigger: "blur"},
            {max: 4, message: "长度在4个字符", trigger: "blur"}
          ],
          price: [
            {required: true, message: "请输入基础工资", trigger: "blur"},
          ],
        },
      };
    },
    async created() {
      this.initQuery();
      this.init();
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
        let res = await this.$api('Service/getAll', this.searchForm);
        //得到所有的父类
        res.serviceList.forEach(item => {
          if (item.parent === null) this.serviceList.push(item)
        });
        this.serviceList.forEach(it => {
          let childrenType = [];
          res.serviceList.forEach(item => {
            if (item.parent === it.id) {
              childrenType.push(item.name)
            }
          });
          it["childrenType"] = childrenType;
        });
      },
      async delService(id) {
        let res = await this.$api('Service/delParService', {id: id});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
      handleClose(tag, dynamicTags) {
        dynamicTags.splice(dynamicTags.indexOf(tag), 1);
      },
      dialog_show(id) {
        if (id == null) {
          this.dialog_title = "新增服务类别";
          this.service.name = '';
          this.service.price = null;

        } else {
          this.dialog_title = "编辑服务类别";
          let tmp = this.serviceList.find(item => item.id === id);
          this.service.id = parseInt(tmp.id);
          this.service.name = tmp.name;
          this.service.price = parseInt(tmp.price);
        }
        this.dialog_visible = true;
      },
      close_dialog() {
        this.dialog_visible = false;
        setTimeout(() => {
          this.$refs["service"].resetFields();
        }, 200);
      },
      async addOrEditUser() {
        this.service.id = this.service.id === null ? null : parseInt(this.service.id);
        this.$refs["service"].validate(async valid => {
          if (valid) {
            let res;
            if (this.service.id == null) {
              res = await this.$api("Service/addParent", {
                name: this.service.name,
                price: parseInt(this.service.price),
              });
            } else {
              res = await this.$api("Service/editParent", {
                id: this.service.id,
                name: this.service.name,
                price: parseInt(this.service.price),
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
        console.log(123)
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
  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>
