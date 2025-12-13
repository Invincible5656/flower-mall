<template>
  <div class="cart-container">
    <h2>🛒 我的购物车</h2>

    <el-table :data="cartItems" style="width: 100%; margin-top: 20px" border stripe>
      <!-- 商品信息列 -->
      <el-table-column label="商品信息" min-width="200">
        <template #default="scope">
          <div style="display: flex; align-items: center; gap: 10px;">
            <!-- 如果后端返回了图片，可以加上 -->
            <!-- <el-avatar shape="square" :size="50" :src="scope.row.flowerCover" /> -->
            <div>
              <!-- 注意：确认后端返回的商品名是 flowerName 还是 name -->
              <h4>{{ scope.row.flowerName || scope.row.name || '商品ID:'+scope.row.flowerId }}</h4>
              <span style="font-size: 12px; color: #999">ID: {{ scope.row.flowerId }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 单价列 -->
      <el-table-column label="单价" width="120">
        <template #default="scope">
          ￥{{ scope.row.price }}
        </template>
      </el-table-column>

      <!-- 数量列 (新增交互) -->
      <el-table-column label="数量" width="180">
        <template #default="scope">
          <!-- change 事件：当数字改变时触发更新 -->
          <el-input-number
              v-model="scope.row.count"
              :min="1"
              :max="99"
              size="small"
              @change="(val) => handleCountChange(scope.row, val)"
          />
        </template>
      </el-table-column>

      <!-- 小计列 -->
      <el-table-column label="小计" width="150">
        <template #default="scope">
          <span style="color: red; font-weight: bold;">
            <!-- 加上 || 0 防止 NaN -->
            ￥{{ ((scope.row.price || 0) * (scope.row.count || 0)).toFixed(2) }}
          </span>
        </template>
      </el-table-column>

      <!-- 操作列 (新增) -->
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部结算栏 -->
    <div class="footer" v-if="cartItems.length > 0">
      <div class="total">
        总计：<span>￥{{ totalPrice }}</span>
      </div>
      <el-button type="primary" size="large" @click="handleCheckout">去下单</el-button>
    </div>
    <div v-else class="empty-tip">
      购物车是空的，快去选购吧~
    </div>

    <!-- 下单弹窗 (保持不变) -->
    <el-dialog v-model="dialogVisible" title="填写收货信息" width="400px">
      <el-form :model="orderForm" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="orderForm.receiverName" placeholder="请填写姓名"/>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="orderForm.receiverPhone" placeholder="请填写手机号"/>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="orderForm.address" type="textarea" placeholder="请填写详细地址"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">确认支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const cartItems = ref([])
const dialogVisible = ref(false)
const orderForm = ref({
  receiverName: '',
  receiverPhone: '',
  address: ''
})

// 1. 查询购物车
const loadCart = async () => {
  try {
    const res = await request.get('/cart/list')
    console.log('购物车原始数据:', res)
    // 此时 res 应该是 { userId: 14, items: [...] }

    // 1. 取出 items 数组 (你的数据结构里 items 在外层对象里)
    const items = res.items || []

    // 2. 【关键修复】进行数据映射
    // 把后端的 amount 赋值给前端的 count
    cartItems.value = items.map(item => ({
      ...item,
      // 如果后端有 flowerName 就用，没有就尝试拼凑
      flowerName: item.flowerName || '商品-' + item.flowerId,
      // 【核心】：后端叫 amount，前端叫 count
      count: Number(item.amount),
      // 确保价格也是数字
      price: Number(item.price)
    }))

  } catch (e) {
    console.error('加载购物车失败:', e)
  }
}

// 2. 计算总价 (Vue 的 computed 属性会自动计算)
const totalPrice = computed(() => {
  if (!cartItems.value || cartItems.value.length === 0) return '0.00'
  const total = cartItems.value.reduce((sum, item) => {
    return sum + (item.price || 0) * (item.count || 0)
  }, 0)
  return total.toFixed(2)
})

const handleCountChange = async (row, newVal) => {
  try {
    // 调用后端的 /update 接口
    await request.post('/cart/update', {
      flowerId: row.flowerId,
      count: newVal
    })
    console.log('数量更新同步成功')
    //更新成功后，重新拉取一次购物车，确保金额计算也是后端最新的
    await loadCart()
  } catch(e) {
    // 如果失败，最好把数量改回去 (这里略)
    console.error('更新失败')
  }
}

// 2. 删除商品
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      // 调用后端的 /delete 接口
      await request.post('/cart/delete', {
        flowerId: row.flowerId
      })

      ElMessage.success('删除成功')
      await loadCart()

    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}

// 5. 点击下单
const handleCheckout = () => {
  if (cartItems.value.length === 0) {
    ElMessage.warning('购物车是空的')
    return
  }
  dialogVisible.value = true
}

// 6. 提交订单
const submitOrder = async () => {
  if(!orderForm.value.receiverName || !orderForm.value.receiverPhone) {
    ElMessage.warning('请填写完整信息')
    return
  }
  try {
    await request.post('/order/create', orderForm.value)
    ElMessage.success('下单成功！')
    dialogVisible.value = false
    await loadCart() // 刷新（清空）
  } catch (e) {}
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.cart-container { padding: 20px 50px; }
.footer {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background: #f8f8f8;
  border-radius: 4px;
}
.total span {
  font-size: 28px;
  color: #f56c6c;
  font-weight: bold;
}
.empty-tip {
  text-align: center;
  padding: 50px;
  color: #999;
  font-size: 16px;
}
</style>