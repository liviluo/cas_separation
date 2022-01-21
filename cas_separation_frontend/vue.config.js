// vue.config.js
module.exports = {
  // see https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9001',
        changeOrigin: true, // 允许跨域
        ws: true
      }
    }
  }
};