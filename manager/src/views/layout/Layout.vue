<template>
  <div class="app-wrapper" :class="classObj">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"></div>
    <sidebar class="sidebar-container" :sidebar="sidebar" :route-map="routeMap"></sidebar>
    <div class="main-container">
      <el-menu class="navbar" mode="horizontal">
        <hamburger class="hamburger-container" :toggleClick="toggleSideBar"
                   :isActive="sidebar.opened"></hamburger>
        <div style="float: left">{{$route.name}}</div>
        <div style="float: right;padding-right: 10px">
          <el-dropdown>
            <span class="el-dropdown-link">
              {{username+' '+(rankName!=null?rankName:'')}}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <span @click="$router.push('/manager/editPassword')" style="display:block;">修改密码</span>
              </el-dropdown-item>
              <el-dropdown-item>
                <span @click="logout" style="display:block;">注销</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </el-menu>

      <section class="app-main">
        <transition name="fade" mode="out-in">
          <router-view @logout="logout"></router-view>
        </transition>
      </section>
    </div>
  </div>
</template>

<script>
  import Hamburger from '@/components/Hamburger'
  import Sidebar from './Sidebar'
  import Cookie from 'js-cookie';

  export default {
    name: 'layout',
    components: {
      Hamburger,
      Sidebar,
    },
    beforeMount() {
      window.addEventListener('resize', this.resizeHandler);
      this.device = this.isMobile() ? 'mobile' : 'desktop';
      if (localStorage.getItem('sidebarOpened') !== 'false') {
        this.sidebar.opened = true;
      }
    },
    data() {
      return {
        device: 'desktop',
        sidebar: {opened: true, withoutAnimation: false},
        routeMap: [],
        rankName: null
      }
    },
    computed: {
      classObj() {
        return {
          hideSidebar: !this.sidebar.opened,
          openSidebar: this.sidebar.opened,
          withoutAnimation: this.sidebar.withoutAnimation,
          mobile: this.device === 'mobile'
        }
      },
      username() {
        return window.$mine.username;
      },
    },
    methods: {
      isMobile() {
        const rect = document.body.getBoundingClientRect();
        return rect.width < rect.height
      },
      resizeHandler() {
        if (!document.hidden) {
          this.device = this.isMobile() ? 'mobile' : 'desktop';
        }
      },
      handleClickOutside() {
        this.sidebar.opened = false;
        localStorage.setItem('sidebarOpened', this.sidebar.opened);
      },
      toggleSideBar() {
        this.sidebar.opened = !this.sidebar.opened;
        localStorage.setItem('sidebarOpened', this.sidebar.opened);
      },
      //退出登录
      async logout() {
        const res = await this.$api('User/logout',{});
        if (!res) return false;
        location.replace('/login');
      }
    },
    async created() {
      if (!window.$mine) {
        const res = await this.$api('User/getMineInfo',{});
        if (!res) return false;
        window.$mine = res.data;
      }

      // 根据role来渲染菜单
      let role = window.$mine.role;
      let routes = this.$router.options.routes;
      routes.forEach(route => {
        if (route.hidden) return;
        else {
          if (!route.roles || route.roles.indexOf(role) !== -1) {
            // 拥有权限
            let cs = route.children;
            if (!cs) {
              let p = route.path;
              let hidden = route.hidden ? route.hidden : false;
              if (!hidden) {
                this.routeMap.push("push2" + route);
              }
            } else {
              let child = [];
              cs.forEach(c => {
                if (!c.roles || c.roles.indexOf(role) !== -1) {
                  let hidden = c.hidden ? c.hidden : false;
                  if (!hidden) child.push(c);
                } else return
              });
              if (child.length > 0) {
                if (child.length === 1) {
                  route.leaf = true;
                }
                route.children = child;
                this.routeMap.push(route);
              }
            }
          } else return
        }
      });
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .navbar {
    height: 50px;
    line-height: 50px;
    border-radius: 0px !important;

    .hamburger-container {
      line-height: 58px;
      height: 50px;
      float: left;
      padding: 0 10px;
    }

    .screenfull {
      position: absolute;
      right: 90px;
      top: 16px;
      color: red;
    }

    .avatar-container {
      height: 50px;
      display: inline-block;
      position: absolute;
      right: 35px;

      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;

        .admin-avatar {
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }

  .app-main {
    margin: 15px;
    /*50 = navbar  */
    min-height: calc(100vh - 50px);
    position: relative;
    overflow: hidden;
  }
</style>
