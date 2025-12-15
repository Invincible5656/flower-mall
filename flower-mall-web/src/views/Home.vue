<template>
  <div class="home-container">
    <!-- 顶部导航栏 (简单版) -->
    <div class="nav-bar">
      <h2>🌸 鲜花商城</h2>
      <div class="right-menu">
        <!-- 新增：我的订单按钮 -->
        <el-button
            v-if="isAdmin"
            type="warning"
            icon="Setting"
            @click="$router.push('/admin/products')"
            style="margin-right: 10px;"
        >
          后台管理
        </el-button>
        <el-button
            type="primary"
            plain
            icon="List"
            @click="$router.push('/my-orders')"
        >
          我的订单
        </el-button>

        <!-- 之前的购物车按钮 -->
        <el-button
            type="success"
            icon="ShoppingCart"
            @click="$router.push('/cart')"
        >
          我的购物车
        </el-button>

        <el-button type="danger" @click="handleLogout">退出</el-button>
      </div>
    </div>

    <!-- 商品列表区域 -->
    <div class="flower-list">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in flowerList" :key="item.id" style="margin-bottom: 20px;">
          <el-card :body-style="{ padding: '0px' }" shadow="hover">
            <!-- 图片 -->
            <img :src="item.imageUrl" class="image" alt="鲜花图片" />
            <div style="padding: 14px">
              <!-- 标题和价格 -->
              <div class="title">{{ item.name }}</div>
              <div class="desc">{{ item.speciesName }} | {{ item.detail }}</div>

              <div class="bottom">
                <span class="price">￥{{ item.price }}</span>
                <!-- 加入购物车按钮 -->
                <el-button type="primary" size="small" icon="Plus" @click="addToCart(item)">加入购物车</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getFlowerList } from '@/api/flower'
import request from '@/utils/request' // 为了发购物车请求，临时直接引入
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Plus } from '@element-plus/icons-vue' // 记得导入图标

const router = useRouter()
const flowerList = ref([])
const isAdmin = ref(false)

// 1. 加载商品
const loadFlowers = async () => {
  try {
    const res = await getFlowerList()
    // 兼容后端可能返回 { items: [...] } 或直接 [...]
    flowerList.value = Array.isArray(res) ? res : (res.items || [])
  } catch (e) {
    console.error(e)
  }
}

// 2. 加入购物车
const addToCart = async (flower) => {
  // 先判断有没有登录
  if (!localStorage.getItem('ACCESS_TOKEN')) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    // 调用后端 /api/cart/add
    // 注意：你的后端 CartController 改过了，不需要 userId，只要 flowerId 和 count
    await request.post('/cart/add', {
      flowerId: flower.id,
      count: 1
    })
    ElMessage.success(`成功将 ${flower.name} 加入购物车`)
  } catch (e) {
    // request.js 会处理报错
  }
}

// 3. 退出登录
const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}

onMounted(() => {
  loadFlowers() // 原有的加载商品

  // 【新增】检查是否是管理员
  const role = localStorage.getItem('USER_ROLE')
  if (role === 'admin') {
    isAdmin.value = true
  }
})
</script>

<style scoped>
.home-container {
  padding: 20px 50px;
}
.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}
.image {
  width: 100%;
  height: 200px;
  object-fit: cover; /* 保持图片比例不拉伸 */
  display: block;
}
.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}
.desc {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
  height: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}
</style>