import request from '@/utils/request'

// 1. 获取列表
export function getSpeciesList(searchKey = '') {
    return request.get('/species/list', {
        params: { searchKey }
    })
}

// 2. 新增
export function addSpecies(name) {
    return request.post('/species/create', { name })
}

// 3. 修改
export function updateSpecies(id, name) {
    return request.post('/species/update', { id, name })
}

// 4. 删除
export function deleteSpecies(id) {
    return request.delete('/species/delete', {
        params: { id }
    })
}