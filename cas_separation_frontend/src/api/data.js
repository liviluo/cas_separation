import axios from '@/utils/request.js';

export function getData(data) {
    return axios.get('/api/data', {
        params: data
    })
}

export function sendData(data) {
    return axios.post('/api/data', data)
}