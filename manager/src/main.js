import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import {Message} from 'element-ui'
import store from './store'
import Vuex from 'vuex'
import axios from 'axios'
import comp from './components'
import '../theme/index.css'
import './plugins/element.js'
import './common/common'
import './common/aes'
import './style/np.css'
import 'element-ui/lib/theme-chalk/index.css';
// table 的样式需要手动引入
import 'element-ui/lib/theme-chalk/icon.css'
import 'element-ui/lib/theme-chalk/table.css'
import 'element-ui/lib/theme-chalk/table-column.css'
import '@/styles/index.scss' // global css
import '@/style/common.css' // global css
//接口统一管理
import {get, post} from './http'

Vue.prototype.$message = Message;
Vue.prototype.axios = axios;
Vue.prototype.$post = post;
Vue.prototype.$get = get;
Vue.config.productionTip = false;
Vue.use(comp);
Vue.use(ElementUI);
Vue.use(Vuex);

/*axios.interceptors.response.use(response => {
  return response.data;
}, error => {
  return {error: 1, message: error.message.data};
});*/

Vue.prototype.$api = async (method, data) => {
  // let keys2 = Object.keys(data);
  /* 这里就是把json变成url形式，并进行encode */
  // data = encodeURI(keys2.map(name => `${name}=${data[name]}`).join('&'));
  // axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';
  if (data){//默认是这个格式？默认json
    let res = await axios({
      method: 'post',
      url: '/api/' + method,
      data: data,//chao shi gei chang yi dian  jiuhaole??   en
      timeout: 10000
    });
    return res.data;
  } else {
    let resp = await axios({
      method: 'get',
      url: '/api/' + method,
      timeout: 2000
    });
    return resp.data;
  }
};

/* eslint-disable no-new */
let v = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
