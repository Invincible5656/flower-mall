<template>
  <div class="admin-padding">
    <h2>📦 订单管理</h2>

    <div class="header">
      <h2>📦 订单管理</h2>
      <div class="actions">
        <!-- 【新增】跳回首页 -->
        <el-button type="info" plain icon="House" @click="$router.push('/')">去商城首页</el-button>

        <!-- 返回商品管理 -->
        <el-button @click="$router.push('/admin/products')">返回商品管理</el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="orderId" label="订单号" width="180" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="totalAmount" label="总金额" width="100" />

      <!-- 复杂的收货信息，后端最好拼好返回，或者前端拼 -->
      <el-table-column label="收货信息" min-width="200">
        <template #default="scope">
          {{ scope.row.receiverName }} / {{ scope.row.receiverPhone }} <br/>
          {{ scope.row.address }}
        </template>
      </el-table-column>

      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag>{{ scope.row.status === 2 ? '已发货' : '待发货' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button
              v-if="scope.row.status === 1"
              type="primary"
              size="small"
              @click="handleShip(scope.row)">
            发货
          </el-button>
          <span v-else style="color: green">已处理</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const tableData = ref([])

const loadData = async () => {
  const res = await request.get('/order/list')
  tableData.value = res || []
}

const handleShip = async (row) => {
  try {
    await request.post('/order/ship', null, { params: { orderId: row.orderId } })
    ElMessage.success('发货成功')
    loadData() // 刷新状态
  } catch(e) {}
}

onMounted(() => loadData())
</script>