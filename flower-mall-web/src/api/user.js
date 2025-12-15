import request from '@/utils/request'

// 登录
export function login(data) {
    return request.post('/user/login', data)
}

// 注册
export function register(data) {
    return request.post('/user/create', data)
}