import AButton from './AButton'
import AutoLoadingBtn from './AutoLoadingButton'
import VerifyCode from './VerifyCode'

export default {
  install: function (Vue) {
    Vue.component("AtButton", AutoLoadingBtn);
    Vue.component("AButton", AButton);
    Vue.component("VerifyCode", VerifyCode);
  }
}
