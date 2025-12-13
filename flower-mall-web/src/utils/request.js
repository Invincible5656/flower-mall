import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service = axios.create({
    baseURL: '/api', // 这里走我们刚才配置的代理
    timeout: 5000
})

// 1. 请求拦截器：每次请求自动带 Token
service.interceptors.request.use(config => {
    const token = localStorage.getItem('ACCESS_TOKEN')
    if (token) {
        // 对应后端的 "Bearer " + token
        config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
}, error => {
    return Promise.reject(error)
})

// 2. 响应拦截器：统一处理报错
service.interceptors.response.use(response => {
    const res = response.data

    // 假设后端成功是 code "0000" 或 200
    if (res.code === '0000' || res.code === 200 ||res.code === '200') {
        return res.data // 直接把数据剥离出来
    } else {
        // 显示错误提示
        ElMessage.error(res.info || res.msg || '系统错误')
        return Promise.reject(new Error(res.msg || 'Error'))
    }
}, error => {
    // 处理 401, 403 等 HTTP 状态码错误
    if (error.response && error.response.status === 401) {
        ElMessage.error('未登录或Token过期，请重新登录')
        localStorage.removeItem('ACCESS_TOKEN')
        // 这里可以加跳转登录页逻辑
        window.location.href = '/login'
    } else {
        ElMessage.error(error.message)
    }
    return Promise.reject(error)
})

export default service