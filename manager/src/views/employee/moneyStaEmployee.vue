<template>
  <div>
    <div>
      <el-row class="margin-bottom">
        <el-form :model="searchForm" :inline="true">
          <el-form-item label="类型">
            <el-select v-model="searchForm.type" style="width: 180px">
              <el-option label="全部" :value=null></el-option>
              <el-option v-for="item in Object.entries(FundType)"
                         :key="item[0]"
                         :label="item[1]"
                         :value="parseInt(item[0])"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker
              style="width: 200px"
              v-model="searchForm.startTime"
              type="datetime"
              value-format="timestamp"
              placeholder="选择时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker
              style="width: 200px"
              v-model="searchForm.endTime"
              type="datetime"
              value-format="timestamp"
              placeholder="选择时间">
            </el-date-picker>
          </el-form-item>
          <el-button @click="search" style="margin-left: 10px;" icon="el-icon-search" type="primary">搜索</el-button>
          <el-button type="primary" @click="exportExcel" style="float: right" icon="el-icon-download">导出为Excel
          </el-button>
        </el-form>
      </el-row>
      <el-table :data="fundList" border style="width: 100%" v-loading="loading" id="out-table">
        <el-table-column prop="id" align="center" label="ID" width="100px" type="index"></el-table-column>
        <el-table-column prop="userDO.username" align="center" label="我的账号"></el-table-column>

        <el-table-column prop="changeMoney" align="center" label="操作金额(元)"></el-table-column>
        <el-table-column prop="balance" align="center" label="操作后余额(元)"></el-table-column>
        <el-table-column prop="type" align="center" label="操作类型">
          <template slot-scope="scope">
            {{FundType[scope.row.type]}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" align="center" label="创建时间">
          <template slot-scope="scope">
            {{$formatTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="ordersVO" align="center" label="订单详情" width="380px">
          <template slot-scope="scope">
            {{scope.row.employeeName?scope.row.employeeName+'为'+scope.row.employerName+'提供'+scope.row.rankName+'-'+scope.row.serviceName+'服务':'用户充值'}}
          </template>
        </el-table-column>

      </el-table>
    </div>
    <div style="text-align: right ;margin-top: 10px">
      <el-pagination style="margin: auto"
                     background
                     :current-page="searchForm.page"
                     :page-size="searchForm.size"
                     :page-sizes="[15,30,45]"
                     layout="sizes, prev, pager, next, jumper"
                     :total="total"
                     @current-change="currentChange"
                     @size-change="handleSizeChange">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import FileSaver from 'file-saver'
  import XLSX from 'xlsx'

  export default {
    name: "moneyStaEmployee",
    data() {
      return {
        searchForm: {
          type: null,//记录类型
          startTime: this.timestamp() * 1000 - (1000 * 60 * 60 * 24) * 6,
          endTime: this.timestamp() * 1000,
          page: 1,
          size: 15,
          eid: window.$mine.id,//当前登陆的id

        },
        loading: true,
        fundList: [],
        total: 0,
        role: window.$mine.role,
      }
    },
    async created() {
      //初始化查询条件
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
      exportExcel() {
        let wb = XLSX.utils.table_to_book(document.querySelector('#out-table'));
        let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
        try {
          FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), '资金记录.xlsx')
        } catch (e) {
          if (typeof console !== 'undefined') console.log(e, wbout)
        }
        return wbout
      },
      initQuery() {
        Object.assign(this.searchForm, this.$route.query);
        this.searchForm.type = this.searchForm.type != null ? parseInt(this.searchForm.type) : null;
        this.searchForm.page = this.searchForm.page ? parseInt(this.searchForm.page) : 1;
        this.searchForm.size = this.searchForm.size ? parseInt(this.searchForm.size) : 15;
        this.searchForm.startTime = parseInt(this.searchForm.startTime);
        this.searchForm.endTime = parseInt(this.searchForm.endTime);
      },
      async init() {
        this.loading = true;
        //查询所有的记录信息
        let res = await this.$api('Fund/getAll', {
          type: this.searchForm.type,
          startTime: this.searchForm.startTime / 1000,
          endTime: this.searchForm.endTime / 1000,
          page: this.searchForm.page,
          size: this.searchForm.size,
          eid: this.searchForm.eid,
        });
        this.loading = false;
        this.fundList = res.list;
        console.log(this.fundList)
        this.total = res.total;
      },
      search() {
        this.searchForm.page = 1;
        this.searchForm.size = 15;
        //携带查询的参数再次查询一下列表
        this.$router.push({
          path: "/employee/moneyStaEmployee",
          query: this.searchForm
        });
      },
      async currentChange(current) {
        this.searchForm.page = current;
        await this.init();
      },
      async handleSizeChange(val) {
        this.searchForm.size = val;
        await this.currentChange(1);
      },
    }
  }
</script>

<style scoped>

</style>
