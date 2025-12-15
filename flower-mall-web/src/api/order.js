import request from '@/utils/request'


export function getMyOrders() {
    return request.get('/order/my')
}

export function getAllOrders() {
    return request.get('/order/list')
}
export function shipOrder(orderId) {
    return request.post('/order/ship', null, { params: { orderId } })
}

export function getSalesStats() {
    return request.get('/order/stats')
}