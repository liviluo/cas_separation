import { getUser } from '@/api/user'
import { getToken, removeToken } from '@/auth/token'

const user = {
  state: {
    token: getToken(),
    account: '',
  },

  mutations: {
    SET_ACCOUNT: (state, account) => {
      state.account = account
    },
  },

  actions: {

    // 获取用户信息
    GetUser({ commit }) {
      return new Promise((resolve, reject) => {
        getUser().then(res => {
          const account = res.data
          commit('SET_ACCOUNT', account)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut() {
      removeToken()
    }

  }
}

export default user
