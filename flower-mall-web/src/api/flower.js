import request from '@/utils/request'

// 1. 获取鲜花列表 (公共接口)
export function getFlowerList() {
    return request.get('/flower/list')
}

// 2. 上架鲜花 (管理员)
export function addFlower(data) {
    return request.post('/flower/add', data)
}

// 3. 删除鲜花 (管理员)
// 注意：根据你的 Controller，这里是用 delete 方法，参数在 url 上
export function deleteFlower(id) {
    return request.delete('/flower/delete', {
        params: { id }
    })
}