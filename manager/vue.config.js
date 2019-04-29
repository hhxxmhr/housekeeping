module.exports = {
  lintOnSave:false,//关闭eslint错误提示
  runtimeCompiler: true,
  publicPath: '/', // 设置打包文件相对路径
  devServer: {
    // open: process.platform === 'darwin',
    // host: 'localhost',
    // open: true, //配置自动启动浏览器
    proxy: {
      '/api': {
        target: 'http://localhost:8888', //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },

}
