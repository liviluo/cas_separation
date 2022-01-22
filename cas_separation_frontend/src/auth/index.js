import router from '@/router'
import store from '@/store'

const whiteList = ['/login', '/logout']

router.beforeEach((to, from, next) => {

  if (store.getters.token) {
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      // 检查用户信息是否存在
      if (store.getters.account === '') {
        // 用户信息不存在，call api获取用户信息
        store.dispatch('GetUser');
        next()
      } else {
        // 用户信息存在，直接进入
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      // 否则全部重定向到登录页
      next(`/login?redirect=${to.fullPath}`)
    }
  }
})