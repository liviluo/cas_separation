import axios from '@/utils/request.js';

export function getUser() {
  return axios.get('auth/user')
}