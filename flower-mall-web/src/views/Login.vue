<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>鲜花商城登录</h2>
      </template>

      <el-form :model="loginForm" label-width="80px">
        <el-form-item label="账号">
          <el-input v-model="loginForm.account" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>

        <!-- 你的后端需要 role，这里加一个选择 -->
        <el-form-item label="角色">
          <el-radio-group v-model="loginForm.role">
            <el-radio label="user">普通用户</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>

        <div style="display: flex; justify-content: space-between; font-size: 14px;">
          <span style="color: #999">测试管理员账号: testadmin001 / 123456</span>
          <span style="color: #999">测试用户账号: testuser001 / 123</span>
          <el-link type="primary" @click="$router.push('/register')">注册新账号</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import request from '@/utils/request' // 引入我们封装的 axios
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  account: 'testadmin001', // 方便测试先写死
  password: '123456',
  role: 'admin'
})

const handleLogin = async () => {
  if(!loginForm.account || !loginForm.password) {
    ElMessage.warning('请输入完整信息')
    return
  }

  loading.value = true
  try {
    // 因为 request.js 里返回的是 res.data
    // 所以这里的 res 就是： { token: "...", userInfo: {...} }
    const res = await request.post('/user/login', loginForm)

    console.log('登录成功数据:', res)

    // 1. 存 Token
    if (res.token) {
      localStorage.setItem('ACCESS_TOKEN', res.token)
    }

    // 2. 存角色 (为了路由守卫)
    if (res.userInfo && res.userInfo.role) {
      localStorage.setItem('USER_ROLE', res.userInfo.role)
    }

    ElMessage.success('登录成功')

    // 3. 根据角色跳不同的地方
    if (res.userInfo.role === 'admin') {
      // 如果是管理员，直接去后台
      router.push('/admin/products')
    } else {
      // 普通用户去首页
      router.push('/')
    }

  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.login-card {
  width: 400px;
}
</style>