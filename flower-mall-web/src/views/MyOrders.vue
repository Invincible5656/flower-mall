<template>
  <div class="order-container">
    <h2>📦 我的订单</h2>

    <div v-if="loading" style="padding: 20px;">加载中...</div>

    <div v-else-if="orders.length === 0" class="empty">
      暂无订单，快去买买买！
    </div>

    <!-- 订单列表 -->
    <div v-else class="order-list">
      <el-card v-for="order in orders" :key="order.orderId" class="order-card">
        <template #header>
          <div class="card-header">
            <span>订单号: {{ order.orderId }}</span>
            <!-- 状态显示 (根据后端返回的状态码判断) -->
            <el-tag :type="getStatusType(order.status)">
              {{ getStatusText(order.status) }}
            </el-tag>
          </div>
        </template>

        <!-- 订单里的商品 -->
        <el-table :data="order.items" style="width: 100%" :show-header="false">
          <el-table-column width="80">
            <template #default="scope">
              <!-- 这里放图片，如果没有就显示个图标 -->
              <el-icon><Goods /></el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="flowerName" label="商品名" />
          <el-table-column prop="price" label="单价" width="100">
            <template #default="scope">￥{{ scope.row.price }}</template>
          </el-table-column>
          <el-table-column prop="count" label="数量" width="80">
            <template #default="scope">x {{ scope.row.count }}</template>
          </el-table-column>
        </el-table>

        <div class="card-footer">
          <div class="info">
            下单时间: {{ order.createTime || '刚刚' }}
          </div>
          <div class="total">
            实付: <span class="price">￥{{ order.totalAmount }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const orders = ref([])
const loading = ref(true)

const loadOrders = async () => {
  try {
    const res = await request.get('/order/my')
    orders.value = res || []
  } catch (e) {
  } finally {
    loading.value = false
  }
}

// 辅助函数：状态转换
const getStatusText = (status) => {
  // 假设后端: 1-待发货, 2-已发货, 3-已完成
  const map = { 1: '待发货', 2: '已发货', 3: '已完成' }
  return map[status] || '未知状态'
}
const getStatusType = (status) => {
  if (status === 2) return 'success'
  if (status === 3) return 'info'
  return 'warning' // 待发货
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-container { padding: 20px 50px; }
.order-card { margin-bottom: 20px; }
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-footer {
  margin-top: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #eee;
  padding-top: 10px;
}
.price { color: #f56c6c; font-size: 20px; font-weight: bold; }
.info { color: #999; font-size: 12px; }
</style>