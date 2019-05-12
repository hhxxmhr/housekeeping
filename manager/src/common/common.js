import Vue from 'vue'

Vue.prototype.pages = [20, 50, 100, 500];

Vue.prototype.$role = {
  Root: 1,//系统管理员
  Manager: 100,//普通管理员
  Employee: 200,//雇员
  Employer: 300//雇主
};

Vue.prototype.EmployeeState = {
  //员工的状态操作，可以是1-4，禁用变成启用2->3，如果员工正在值岗，那么状态是不可改变的
  //用户的状态操作，可以是0-2
  1: "禁用",
  2: "待审核",
  3: "待岗",
  4: "值岗"
};
Vue.prototype.EmployerState = {
  //员工的状态操作，可以是1-4，禁用变成启用2->3，如果员工正在值岗，那么状态是不可改变的
  //用户的状态操作，可以是0-2
  0: "正常",
  1: "禁用",
  2: "待审核",
};

Vue.prototype.UserState = {
  //员工的状态操作，可以是1-4，禁用变成启用2->3，如果员工正在值岗，那么状态是不可改变的
  //用户的状态操作，可以是0-2
  0: "正常",
  1: "禁用",
  2: "待审核",
  3: "待岗",
  4: "值岗"
};
Vue.prototype.ManagerState = {
  //员工的状态操作，可以是1-4，禁用变成启用2->3，如果员工正在值岗，那么状态是不可改变的
  //用户的状态操作，可以是0-2
  0: "正常",
  1: "禁用",
};

Vue.prototype.UserDegree = {
  1: "小学",
  2: "初中",
  3: "大专",
  4: "高中",
  5: "大学",
};

Vue.prototype.UserExperience = {
  0: "半年以下",
  1: "半年经验",
  2: "一年经验",
  3: "两年经验",
  4: "三年经验",
  5: "四年经验以上",
};

Vue.prototype.OrderState = {
  0: "待确认",//等待雇员确认
  1: "已取消",//在两个小时之内可以免费取消，超出时间取消则收取服务费的50%
  2: "已确认",
  3: "待评论",
  4: "已评论",
  5: "退款中",
  6: "已退款",
};


Vue.prototype.FundType = {
  0: "账号充值",
  1: "服务支出",
  2: "超时取消",
  3: "获得退款",
  4: "额外取消费",
  5: "服务收入",
  6: "给予退款",
};

//雇主
Vue.prototype.FundTypeByEmployer = {
  0: "账号充值",
  1: "服务支出",
  2: "超时取消",
  3: "获得退款",
};

//雇员
Vue.prototype.FundTypeByEmployee = {
  4: "额外取消费",
  5: "服务收入",
  6: "给予退款",
};

Vue.prototype.timestamp = function () {
  let time = Date.parse(new Date());
  return time / 1000;
};

Vue.prototype.contains = function (array, needle) {
  for (let i in array) {
    if (!array.hasOwnProperty(i)) continue;
    if (array[i] === needle) return true;
  }
  return false;
};

Vue.prototype.propertyCount = function (obj) {
  let count = 0;
  for (let i in obj) {
    if (obj.hasOwnProperty(i)) {  // 建议加上判断,如果没有扩展对象属性可以不加
      count++;
    }
  }
  return count;
};

Vue.prototype.isEmpty = function (obj) {
  for (let i in obj) {
    if (obj.hasOwnProperty(i)) {
      return false;
    }
  }
  return true;
};

Vue.prototype.$formatTime = function (inputTime) {
  let date = new Date(inputTime * 1000);
  let y = date.getFullYear();
  let m = date.getMonth() + 1;
  m = m < 10 ? ('0' + m) : m;
  let d = date.getDate();
  d = d < 10 ? ('0' + d) : d;
  let h = date.getHours();
  h = h < 10 ? ('0' + h) : h;
  let minute = date.getMinutes();
  let second = date.getSeconds();
  minute = minute < 10 ? ('0' + minute) : minute;
  second = second < 10 ? ('0' + second) : second;
  return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
};

Vue.prototype.$formatTimeDay = function (inputTime) {
  let date = new Date(inputTime * 1000);
  let y = date.getFullYear();
  let m = date.getMonth() + 1;
  m = m < 10 ? ('0' + m) : m;
  let d = date.getDate();
  d = d < 10 ? ('0' + d) : d;
  return y + '-' + m + '-' + d;
};

Vue.prototype.$formatTimeXs = function (inputTime) {
  let date = new Date(inputTime * 1000);
  let m = date.getMonth() + 1;
  m = m < 10 ? ('0' + m) : m;
  let d = date.getDate();
  d = d < 10 ? ('0' + d) : d;
  let h = date.getHours();
  h = h < 10 ? ('0' + h) : h;
  let minute = date.getMinutes();
  minute = minute < 10 ? ('0' + minute) : minute;
  return m + '/' + d + ' ' + h + ':' + minute;
};
