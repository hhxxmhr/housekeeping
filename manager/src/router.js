import Vue from 'vue'
import Router from 'vue-router'
import {Message} from 'element-ui'
import layout from './views/layout/Layout'

Vue.use(Router);

const routes = [
  {path: '/', redirect: '/login', hidden: true},
  {path: '*', redirect: '/404', hidden: true},
  {
    path: '/404', component: layout, name: '', hidden: true, children: [
      {path: '/404', component: () => import('./views/404'), hidden: true, name: 404}
    ]
  },

  {path: '/login', name: '登录', hidden: true, component: () => import('./views/login/login')},
  {path: '/register', name: '注册', hidden: true, component: () => import('./views/login/register')},
  {path: '/forget', name: '忘记密码', hidden: true, component: () => import('./views/login/forget')},
  {
    path: '/', component: layout, name: '', meta: {icon: 'el-icon-menu'},
    children: [{path: '/index', name: '首页', component: () => import('./views/manager/index'), meta: {login: true}}]
  },
  {
    path: '/', component: layout, name: '', roles: [1], meta: {icon: 'el-icon-menu'},
    children: [{
      path: '/manager/managerList',
      name: '管理人员',
      component: () => import('./views/manager/managerList'),
      meta: {login: true}
    }]
  },
  {
    path: '/', name: '系统管理', component: layout, meta: {icon: 'el-icon-setting', login: true},
    children: [
      {
        path: '/employee/info',
        name: '资料编辑',
        roles: [200],
        component: () => import('./views/employee/info'),
        meta: {login: true}
      },
      {
        path: '/manager/editPassword',
        name: '修改密码',
        component: () => import('./views/manager/editPassword'),
        meta: {login: true},
        hidden: true
      },
    ]
  },
  {
    path: '/', name: '服务中心', roles: [100], component: layout, meta: {icon: 'el-icon-setting', login: true},
    children: [
      {path: '/manager/rank', name: '等级管理', component: () => import('./views/manager/rank'), meta: {login: true}},
      {path: '/manager/service', name: '服务管理', component: () => import('./views/manager/service'), meta: {login: true}},
    ]
  },
  {
    path: '/', name: '人员管理', roles: [100], component: layout, meta: {icon: 'el-icon-setting', login: true},
    children: [
      {
        path: '/manager/employeeList',
        name: '雇员列表',
        component: () => import('./views/manager/employeeList'),
        meta: {login: true}
      },
      {
        path: '/manager/employerList',
        name: '雇主列表',
        component: () => import('./views/manager/employerList'),
        meta: {login: true}
      },
    ]
  },

  {
    path: '/', name: '家政服务', roles:[300], component: layout, meta: {icon: 'el-icon-setting', login: true},
    children: [
      {path: '/employer/service', name: '服务中心', component: () => import('./views/employer/service'), meta: {login: true}},
      {path: '/employer/chooseEmployee', name: '人员浏览', component: () => import('./views/employer/chooseEmployee'), meta: {login: true}},
      {path: '/employer/reserve', name: '家政预定', component: () => import('./views/employer/reserve'), meta: {login: true}},
    ]
  },
  {
    path: '/', name: '订单管理', component: layout, meta: {icon: 'el-icon-setting', login: true},
    children: [
      {path: '/manager/orders', name: '订单列表', component: () => import('./views/manager/orders'), meta: {login: true}},
      {path: '/manager/comment', name: '查看评论', component: () => import('./views/manager/comment'), meta: {login: true}},
    ]
  },

  {
    path: '/', name: '数据统计', component: layout, meta: {icon: 'el-icon-setting', login: true},
    children: [

      {path: '/manager/orders', name: '服务统计', component: () => import('./views/manager/orders'), meta: {login: true}},
      {path: '/manager/comment', name: '资金统计', component: () => import('./views/manager/comment'), meta: {login: true}},
    ]
  },
];
let router = new Router({
  mode: 'history',
  routes
});

router.beforeEach(async (to, from, next) => {
  window.document.title = '你家我洁-' + to.name;
  if (to.path === '/login' || to.path === '/404' || to.path === '/register') {
    next();
  } else {
    let vm = Vue.prototype;
    if (!window.$mine) {
      let res = await vm.$api('User/getMineInfo', {});
      window.$mine = res.data;
      if (to.meta.login && res.data.id == null) {//需要登录的权限并且没有存域=》没有登录
        Message({
          message: '请先登录!',
          type: 'error',
        });
        next({path: '/login?target=' + encodeURI(to.path)});
      } else {
        next();
      }
    }
    next();
  }
});

export default router
