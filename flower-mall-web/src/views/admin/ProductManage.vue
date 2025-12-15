<template>
  <div class="admin-container">
    <div class="header">
      <h2>商品管理后台</h2>
      <div>
        <el-button type="info" plain icon="House" @click="$router.push('/')">
          去商城首页
        </el-button>
        <!-- 新增：跳往订单管理 -->
        <el-button type="primary" @click="openDialog">上架新商品</el-button>
        <el-button @click="$router.push('/admin/orders')">去管理订单</el-button>
        <el-button @click="$router.push('/admin/species')">分类管理</el-button>

      </div>

    </div>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">💰 总销售额</div>
          <div class="stat-value sales">￥{{ Number(stats.sales).toFixed(2) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">📦 总订单数</div>
          <div class="stat-value orders">{{ stats.orders }} 单</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 商品表格 -->
    <el-table :data="tableData" style="width: 100%; margin-top: 20px" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 上架/新增弹窗 -->
    <el-dialog v-model="dialogVisible" title="上架新鲜花" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" placeholder="例如：卡布奇诺玫瑰" />
        </el-form-item>

        <el-form-item label="分类名称">
          <el-input v-model="form.speciesName" placeholder="例如：玫瑰" />
        </el-form-item>

        <el-form-item label="价格">
          <el-input-number v-model="form.price" :precision="2" :step="10" />
        </el-form-item>

        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="1" />
        </el-form-item>

        <el-form-item label="详情/花语">
          <el-input v-model="form.detail" type="textarea" placeholder="花语：..." />
        </el-form-item>

        <!-- 图片暂时用 URL 字符串代替 -->
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" placeholder="http://..." />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定上架</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.actions {
  display: flex;
  gap: 10px;
}
</style>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getFlowerList, addFlower, deleteFlower } from '@/api/flower'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSalesStats } from '@/api/order'

const tableData = ref([])
const dialogVisible = ref(false)

const stats = reactive({
  sales: 0,
  orders: 0
})

const loadStats = async () => {
  try {
    const res = await getSalesStats()
    stats.sales = res.sales || 0
    stats.orders = res.orders || 0
  } catch (e) {
    console.error('加载统计失败', e)
  }
}

// 表单数据
const form = reactive({
  name: '',
  speciesName: '',
  price: 99.00,
  stock: 100,
  detail: '',
  imageUrl: ''
})

// 1. 加载列表
const loadData = async () => {
  try {
    const res = await getFlowerList()
    // 假设后端返回的数据结构是 { items: [...], len: ... } 或者直接是 [...]
    // 请根据实际情况调整，如果是分页的可能在 res.items
    tableData.value = Array.isArray(res) ? res : (res.items || [])
  } catch (e) {
    console.error(e)
  }
}

// 2. 打开弹窗
const openDialog = () => {
  dialogVisible.value = true
  form.name = ''
  form.speciesName = '' // 重置
  form.detail = ''      // 重置
  form.price = 99
}

// 3. 提交上架请求
const handleSubmit = async () => {
  try {
    await addFlower(form)
    ElMessage.success('上架成功')
    dialogVisible.value = false
    loadData() // 刷新列表
  } catch (e) {
    // 错误会被 request.js 拦截并弹窗，这里不用管
  }
}

// 4. 下架/删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要下架这个商品吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteFlower(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (e) {}
  })
}

// 页面加载时自动拉取数据
onMounted(() => {
  loadData()
  loadStats()
})
</script>

<style scoped>
.admin-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-card {
  text-align: center;
  background-color: #fcfcfc;
}
.stat-title {
  font-size: 14px;
  color: #999;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
}
.sales { color: #f56c6c; } /* 红色 */
.orders { color: #409eff; } /* 蓝色 */
</style>