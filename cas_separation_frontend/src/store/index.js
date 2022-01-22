import { createStore } from 'vuex'
import user from './modules/user'
import getters from './getters'

const store = new createStore({
  modules: {
    user
  },
  getters
})

export default store
