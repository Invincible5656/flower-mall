<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>🌸 新用户注册</h2>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">

        <el-form-item label="账号" prop="account">
          <el-input v-model="form.account" placeholder="请输入登录账号" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="设置密码" />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input v-model="form.confirmPwd" type="password" show-password placeholder="再次输入密码" />
        </el-form-item>

        <el-form-item label="昵称" prop="name">
          <el-input v-model="form.name" placeholder="例如：张三" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="11位手机号" maxlength="11" />
        </el-form-item>

        <el-form-item label="收货地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="默认收货地址" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">立即注册</el-button>
        </el-form-item>

        <div style="text-align: center">
          <el-link type="info" @click="$router.push('/login')">已有账号？去登录</el-link>
        </div>

      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/user' // 引入刚才封装的API
import request from '@/utils/request' // 或者直接用这个
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  account: '',
  password: '',
  confirmPwd: '',
  name: '',
  phone: '',
  address: ''
})

// 表单校验规则
const validatePass2 = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPwd: [{ validator: validatePass2, trigger: 'blur' }],
  name: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const handleRegister = () => {
  // 1. 校验表单
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 2. 发送请求 (注意：要把 confirmPwd 这种多余字段去掉，或者后端不接收就行)
        // 你的 UserRegisterRequest 只有 account, password, name, phone, address
        await request.post('/user/create', {
          account: form.account,
          password: form.password,
          name: form.name,
          phone: form.phone,
          address: form.address
        })

        ElMessage.success('注册成功！请登录')

        // 3. 跳转回登录页
        router.push('/login')

      } catch (e) {
        // request.js 会处理报错
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
  /* 可以加个背景图，更好看 */
  /* background-image: url('...'); */
}
.login-card {
  width: 450px;
}
</style>