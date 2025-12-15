<template>
  <div class="admin-padding">
    <div class="header">
      <h2>🏷️ 分类管理</h2>
      <div class="actions">
        <!-- 【新增】跳回首页 -->
        <el-button type="info" plain icon="House" @click="$router.push('/')" style="margin-right: 10px">
          去商城首页
        </el-button>

        <!-- 返回商品管理 -->
        <el-button @click="$router.push('/admin/products')" style="margin-right: 10px">
          返回商品管理
        </el-button>
        <el-input
            v-model="searchKey"
            placeholder="搜索分类..."
            style="width: 200px; margin-right: 10px"
            clearable
            @clear="loadData"
            @keyup.enter="loadData"
        >
          <template #append>
            <el-button icon="Search" @click="loadData" />
          </template>
        </el-input>
        <el-button type="primary" icon="Plus" @click="openDialog()">新增分类</el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe style="margin-top: 20px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗：新增/编辑共用 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" @submit.prevent>
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="例如：春日限定" @keyup.enter="handleSubmit" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getSpeciesList, addSpecies, updateSpecies, deleteSpecies } from '@/api/species'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'

const tableData = ref([])
const loading = ref(false)
const searchKey = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: ''
})

// 1. 加载列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await getSpeciesList(searchKey.value)
    tableData.value = res || []
  } catch(e) {}
  finally {
    loading.value = false
  }
}

// 2. 打开弹窗 (兼容新增和编辑)
const openDialog = (row = null) => {
  dialogVisible.value = true
  if (row) {
    // 编辑模式
    isEdit.value = true
    form.id = row.id
    form.name = row.name
  } else {
    // 新增模式
    isEdit.value = false
    form.id = null
    form.name = ''
  }
}

// 3. 提交表单
const handleSubmit = async () => {
  if(!form.name) return ElMessage.warning('请输入名称')

  try {
    if (isEdit.value) {
      await updateSpecies(form.id, form.name)
      ElMessage.success('修改成功')
    } else {
      await addSpecies(form.name)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch(e) {}
}

// 4. 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除分类【${row.name}】吗？`, '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSpecies(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch(e) {}
  })
}

onMounted(() => loadData())
</script>

<style scoped>
.admin-padding { padding: 20px; }
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.actions {
  display: flex;
  align-items: center;
}
</style>