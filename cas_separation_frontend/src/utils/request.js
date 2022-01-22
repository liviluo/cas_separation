import axios from 'axios';
import store from '@/store';

const request = axios.create({
  timeout: 5000,
  baseURL: 'api',
  withCredentials: true //跨域时携带cookie
})

request.interceptors.response.use(res => {
      return Promise.resolve(res);
  },
  error => {
    console.log('error：' + error);
    if (error.message.includes("401")) {
      store.dispatch('LogOut').then(() => {
        location.href = '/home';
      })
      alert('无效的会话，或者会话已过期，请重新登录。');
    }
    return Promise.reject(error.message);
  }
)

export default request;