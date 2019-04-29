/*
import axios from 'axios';
// 引入qs模块，用来序列化post类型的数据
import QS from 'qs';
// 导入vuex,因为要使用到里面的状态对象
import store from './store';
import router from './router'
import {Message} from 'element-ui'

const Axios = axios.create({
  baseURL: "http://localhost:8888", // 基础URL
  timeout: 10000,
  // responseType: "json",
  withCredentials: true, // 是否允许带cookie这些
  headers: {
    //request中发送json数据用post方式发送Content-type用application/json;charset=utf-8方式发送的话，直接用springMVC的@RequestBody标签接收后面跟实体对象就行了，
    // spring会帮你自动拼装成对象，如果Content-type设置成application/x-www-form-urlencoded;charset=utf-8就不能用spring的东西了，只能以常规的方式获取json串了
    //   "Content-Type": "application/x-www-form-urlencoded;charset=utf-8"
    "Content-Type": "application/json;charset=utf-8"
  }
});

//POST传参序列化(添加请求拦截器)
Axios.interceptors.request.use(
  config => {
    // 在发送请求之前做某件事
    if (config.method === "post") {
      // 序列化
      config.data = QS.stringify(config.data);
      config.data = JSON.stringify(config.data);
      // 若是提交能直接接受json 格式,可以不用 qs 来序列化的
    }
    // 若是有做鉴权token , 就给头部带上token
    // 若是需要跨站点,存放到 cookie 会好一点,限制也没那么多,有些浏览环境限制了 localstorage 的使用
    if (localStorage.token) {
      config.headers.Authorization = localStorage.token;
    }
    return config;
  },

  error => {
    // error 的回调信息,自定义
    Message({
      showClose: true,
      message: error && error.data.error.message,
      type: "error"
    });
    return Promise.reject(error.data.error.message);
  }
);

//返回状态判断(添加响应拦截器)
Axios.interceptors.response.use(
  res => {
    //对响应数据做些事
    if (res.data && !res.data.success) {
      Message({
        showClose: true,
        message: "1",/!*res.data.error.message.message
          ? res.data.error.message.message
          : res.data.error.message,*!/
        type: "error"
      });
      return Promise.reject("1"/!*res.data.error.message*!/);
    }
    return res;
  },
  error => {
    // console.log(error);
    if (error.data) {
      switch (error.data.code) {
        case 401:
          // 返回 401 清除token信息并跳转到登录页面
          store.commit("del_token");
          router.push({
            path: "/login",
            query: {
              redirect: router.currentRoute.fullPath
            }
          });
          break;
      }
    }

    // 用户登录的时候会拿到一个基础信息,比如用户名,token,过期时间戳
    // 直接丢localStorage或者sessionStorage
    if (!window.localStorage.getItem("token")) {
      // 若是接口访问的时候没有发现有鉴权的基础信息,直接返回登录页
      router.push({
        path: "/login"
      });
    } else {
      // 若是有基础信息的情况下,判断时间戳和当前的时间,若是当前的时间大于服务器过期的时间
      // 乖乖的返回去登录页重新登录
      let lifeTime =
        JSON.parse(window.localStorage.getItem("token")).lifeTime * 1000;
      let nowTime = new Date().getTime(); // 当前时间的时间戳
      console.log(nowTime, lifeTime);
      console.log(nowTime > lifeTime);
      if (nowTime > lifeTime) {
        Message({
          showClose: true,
          message: "登录状态信息过期,请重新登录",
          type: "error"
        });
        router.push({
          path: "/login"
        });
      } else {
        // 下面是接口回调的status ,因为我做了一些错误页面,所以都会指向对应的报错页面
        // if (error.response.status === 403) {
        //     this.$router.push({
        //         path: "/error/403"
        //     });
        // }
        // if (error.response.status === 500) {
        //     this.$router.push({
        //         path: "/error/500"
        //     });
        // }
        // if (error.response.status === 502) {
        //     this.$router.push({
        //         path: "/error/502"
        //     });
        // }
        if (error.response.status === 404) {
            router.push({
                path: "/error/404"
            });
        }
      }
    }
    // 返回 response 里的错误信息
    // let errorInfo = error.data.error ? error.data.error.message : error.data;
    // return Promise.reject(errorInfo);
    return Promise.reject(error);
  });


// 对axios的实例重新封装成一个plugin ,方便 Vue.use(xxxx)
export default {
  install: function(Vue, Option) {
    Object.defineProperty(Vue.prototype, "$http", { value: Axios });
  }
};

//封装get、post方法
/!**
 * get方法，对应get请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 *!/
export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: params
    }).then(res => {
      resolve(res.data);
    }).catch(err => {
      reject(err.data)
    })
  });
}

//但是要注意的是，post方法必须要使用对提交从参数对象进行序列化的操作，所以这里我们通过node的qs模块来序列化我们的参数。
// 这个很重要，如果没有序列化操作，后台是拿不到提交的数据的。这就是开头我们import QS from 'qs';的原因。
/!**
 * post方法，对应post请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 *!/
export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios.post(url, QS.stringify(params))
      .then(res => {
        resolve(res.data);
      })
      .catch(err => {
        reject(err.data)
      })
  });
}
*/
