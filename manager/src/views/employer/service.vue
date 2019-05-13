<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-input placeholder="请输入要查询的服务名称" v-model="searchForm.name"
                  style="width: 250px"></el-input>
        <el-button @click="search"
                   style="margin-left: 10px;"
                   icon="el-icon-search"
                   type="primary">搜索
        </el-button>
      </el-row>
      <el-table :data="serviceList" border style="width: 100%">
        <el-table-column prop="name" label="类别" align="center"></el-table-column>
        <el-table-column prop="childrenType" label="可选服务" align="center" width="400px">
          <template slot-scope="scope">
            <el-radio v-model="serviceId" :label="tag.id" :key="tag.id" v-for="tag in scope.row.childrenType"
                      v-if="scope.row.childrenType.length>0">
              {{tag.name+':'+tag.price+'元/'+tag.type}}

            </el-radio>
            <el-tag :disable-transitions="false" size="medium" slot="reference"
                    v-if="scope.row.childrenType.length===0">
              {{'暂无'}}
            </el-tag>

          </template>
        </el-table-column>
        <el-table-column prop="price" label="基础服务金额(元)" align="center" width="150px"></el-table-column>
        <el-table-column prop="enable" label="操作" header-align="center" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="choose(scope.row.id)">选择雇员</el-button>
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
        serviceId: null,
        searchForm: {
          name: ''
        },
        serviceList: [],
        formLabelWidth: "80px",
        dialog_title: '',
        dialog_visible: false,
        dialog_visibleChild: false,
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
        this.initQuery();
        this.init();
      }
    },
    methods: {
      async init() {
        this.serviceList = [];
        let res = await this.$api('Service/getAll', this.searchForm);
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
              row.price = item.price;
              row.type = item.type;
              childrenType.push(row)
            }
          });
          it["childrenType"] = childrenType;
        });
        // console.log(this.serviceList)
      },
      choose(id) {
        //跳转到选人的页面
        this.$router.push({
          path: "/employer/chooseEmployee",
          query: {serviceId: this.serviceId != null ? parseInt(this.serviceId) : id}
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
        // this.searchForm.id = this.searchForm.id ? parseInt(this.searchForm.id) : null;
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
