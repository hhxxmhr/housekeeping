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
              <template slot="append">元</template>
            </el-input>
          </el-form-item>
          <el-form-item label="服务类型" :label-width="formLabelWidth">
            <el-select v-model="service.type" :disabled="service.id!=null" style="width: 180px">
              <el-option label="次结" value="次"></el-option>
              <el-option label="月结" value="月"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="close_dialog()">取消</el-button>
          <at-button type="primary"
                     @click="addOrEditService">确定
          </at-button>
        </div>
      </el-dialog>
      <el-dialog width="420px"
                 :title="dialog_title"
                 :visible.sync="dialog_visibleChild"
                 :before-close="close_dialog"
                 :close-on-click-modal="false">
        <el-form :model="service"
                 :rules="rules"
                 ref="service">
          <el-form-item label="子服务名称"
                        prop="name"
                        label-width="100px">
            <el-input v-model="service.name" style="width: 180px"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="子服务工资"
                        prop="price"
                        label-width="100px">
            <el-input v-model="service.price" style="width: 180px" auto-complete="off">
              <template slot="append">元</template>
            </el-input>
            <span style="font-size: 12px;size: 12px;color: #f78989;margin-left: 5px">不小于基础工资</span>
          </el-form-item>
          <el-form-item label="服务类型" label-width="100px">
            <el-select v-model="service.type" disabled style="width: 180px">
              <el-option label="次结" value="次"></el-option>
              <el-option label="月结" value="月"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="close_dialog()">取消</el-button>
          <at-button type="primary"
                     @click="addOrEditService">确定
          </at-button>
        </div>
      </el-dialog>
      <el-table :data="serviceList" border style="width: 100%">
        <el-table-column prop="id" label="ID" align="center" width="150px"></el-table-column>
        <el-table-column prop="name" label="类别" align="center" width="180px"></el-table-column>
        <el-table-column prop="childrenType" label="可选服务" align="center" width="400px">
          <template slot-scope="scope">
            <el-tag :key="tag.id" v-for="tag in scope.row.childrenType" closable
                    @close="handleClose(tag,scope.row)" @click="editChild(tag)"
                    :disable-transitions="false" size="medium" slot="reference">
              {{tag.name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="基础服务金额(元)" align="center" width="150px">
          <template slot-scope="scope">
            {{scope.row.price+'/'+scope.row.type}}
          </template>
        </el-table-column>
        <el-table-column prop="enable" label="操作" header-align="center" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="warning" @click="dialog_show(scope.row.id)">编辑</el-button>
            <at-button size="mini" type="danger" confirmText="确定要删除此服务类别" @click="delService(scope.row.id)">删除
            </at-button>
            <el-button size="mini" type="success" @click="dialog_showChild(scope.row)">添加子服务</el-button>
            <at-button size="mini" type="info" @click="filterEmployee(scope.row)">从事人员</at-button>
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
        allService: [],
        serviceIds: [],
        searchForm: {
          name: ''
        },
        service: {
          id: null,
          name: '',
          price: null,
          parent: null,
          type: '次',
        },
        serviceList: [],
        formLabelWidth: "80px",
        dialog_title: '',
        dialog_visible: false,
        dialog_visibleChild: false,
        rules: {
          name: [
            {required: true, message: "请输入服务名称", trigger: "blur"},
            {max: 8, message: "长度在8个字符", trigger: "blur"}
          ],
          price: [
            {required: true, message: "请输入基础工资", trigger: "blur"},
            {pattern: /^\d*[02468]$/, message: '请输入偶数'},
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
        this.initQuery();
        this.init();
      }
    },
    methods: {
      async init() {
        this.serviceList = [];
        let res = await this.$api('Service/getAll', this.searchForm);
        this.allService = res.serviceList;
        //得到所有的父类
        res.serviceList.forEach(item => {
          if (item.parent === null) this.serviceList.push(item)
        });
        this.serviceList.forEach(it => {
          let childrenType = [];

          res.serviceList.forEach(item => {
            let row = {};
            if (item.parent === it.id) {
              row.id = item.id;
              row.name = item.name;
              row.type = item.type;
              row.price = item.price;
              childrenType.push(row)
            }
          });
          it["childrenType"] = childrenType;
        });
      },
      filterEmployee(data) {
        this.serviceIds = [];
        data.childrenType.forEach(it => {
          this.serviceIds.push(it.id)
        });
        this.serviceIds.push(data.id);
        //跳转到选人的页面
        this.$router.push({
          path: "/manager/doEmployee",
          query: {serviceIds: this.serviceIds}
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
      async handleClose(tag, row) {
        row.childrenType.splice(row.childrenType.indexOf(tag), 1);
        let res = await this.$api("Service/deleteChild", {id: tag.id});
        this.$message({
          type: res.code === 200 ? 'success' : 'error',
          message: res.msg
        });
        this.init();
      },
      async editChild(tag) {
        this.dialog_title = "编辑子服务";
        this.service.name = tag.name;
        this.service.id = tag.id;
        this.service.price = tag.price;
        this.service.type = tag.type;
        this.dialog_visibleChild = true;
      },
      dialog_showChild(row) {
        this.dialog_title = "新增子服务";
        this.service.name = '';
        this.service.price = null;
        this.service.type = row.type;
        this.service.parent = row.id;
        this.dialog_visibleChild = true;
      },
      dialog_show(id) {
        if (id == null) {
          this.dialog_title = "新增服务类别";
          this.service.name = '';
          this.service.price = null;
          this.service.parent = null;
          this.service.type = '次';

        } else {
          this.dialog_title = "编辑服务类别";
          let tmp = this.serviceList.find(item => item.id === id);
          this.service.id = id;
          this.service.name = tmp.name;
          this.service.type = tmp.type;
          this.service.parent = null;
          this.service.price = parseInt(tmp.price);
        }
        this.dialog_visible = true;
      },
      close_dialog() {
        this.dialog_visible = false;
        this.dialog_visibleChild = false;
        setTimeout(() => {
          this.$refs["service"].resetFields();
        }, 200);
      },
      async addOrEditService() {
        this.service.id = this.service.id === null ? null : parseInt(this.service.id);
        this.$refs["service"].validate(async valid => {
          if (valid) {
            let res;
            if (this.service.id == null) {//添加
              console.log(this.service)
              if (this.service.parent !== null) {//添加子类的时候
                //子类的工资要大于等于父类的基础工资
                let tmp = this.serviceList.find(item => item.id === this.service.parent);
                if (this.service.price < tmp.price) {
                  this.$message.error("子类的工资不能小于基础工资");
                  return
                }
              }

              res = await this.$api("Service/addService", {
                parent: this.service.parent ? parseInt(this.service.parent) : null,
                name: this.service.name,
                type: this.service.type,
                price: parseInt(this.service.price),
              });
            } else {
              //还有编辑的时候也需要进行判断---根据编辑的id查询它是否是子类别，如果是的话，就需要进行判断
              let tmp = this.allService.find(item => item.id === this.service.id);
              if (tmp.parent != null) {
                //子类的父类id查询父类的基础工资
                let tmpParent = this.serviceList.find(item => item.id === tmp.parent);
                if (this.service.price < tmpParent.price) {
                  this.$message.error("子类的工资不能小于基础工资");
                  return false
                }
              } else {
                //当编辑的是父类信息的时候，需要提示子类工资也要更改，大于等于基础工资
                //根据父类的id 比较他子类的工资
                let arr = [];
                this.allService.forEach(service => {
                  if (service.parent === this.service.id) {//找到他的子类
                    if (this.service.price > service.price) {//找到比他调整价格低的子类
                      arr.push(service);
                    }
                  }
                });
                if (arr.length > 0) {
                  this.$message.error("基础工资提高，子服务工资也应相应的提高");
                  return false
                }
              }

              res = await this.$api("Service/editService", {
                id: this.service.id,
                name: this.service.name,
                type: this.service.type,
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
        this.$router.push({
          path: this.$route.path,
          query: this.searchForm
        });
      },
      initQuery() {
        Object.assign(this.searchForm, this.$route.query);
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
