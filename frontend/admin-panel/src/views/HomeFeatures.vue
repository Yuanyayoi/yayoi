<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>核心优势管理</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>页面文案</h3>
            <el-button v-if="!isEditing" type="primary" @click="toggleEdit">修改</el-button>
          </div>
          <div v-if="!isEditing" class="text-display">
            <div class="text-item">
              <span class="text-label">主标题</span>
              <span class="text-value">{{ featuresText.sectionTitle }}</span>
            </div>
            <div class="text-item">
              <span class="text-label">副标题</span>
              <span class="text-value">{{ featuresText.sectionSubtitle }}</span>
            </div>
          </div>
          <el-form v-else :model="featuresText" label-width="100px">
            <el-form-item label="主标题">
              <el-input v-model="featuresText.sectionTitle" />
            </el-form-item>
            <el-form-item label="副标题">
              <el-input v-model="featuresText.sectionSubtitle" />
            </el-form-item>
            <div class="form-actions">
              <el-button @click="toggleEdit">取消</el-button>
              <el-button type="primary" @click="saveFeaturesText">保存</el-button>
            </div>
          </el-form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>核心优势列表</h3>
            <el-button type="primary" @click="openDialog(null)">+ 新增核心优势</el-button>
          </div>
          <el-table :data="features" border v-loading="loading">
            <el-table-column prop="title" label="标题" min-width="140" />
            <el-table-column prop="description" label="描述" min-width="300">
              <template #default="scope">
                <span class="desc-text">{{ scope.row.description }}</span>
              </template>
            </el-table-column>
            <el-table-column label="图标" width="80" align="center">
              <template #default="scope">
                <img v-if="getIconImage(scope.row.style)" :src="getIconImage(scope.row.style)" class="table-icon" />
              </template>
            </el-table-column>
            <el-table-column label="排序" width="150" align="center">
              <template #default="scope">
                <el-input-number 
                  v-model="scope.row.sortOrder" 
                  :min="0" 
                  :max="999" 
                  size="small"
                  controls-position="right"
                  style="width: 120px"
                  @change="updateSortOrder(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140" align="center">
              <template #default="scope">
                <el-button size="small" type="primary" link @click="openDialog(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" link @click="del(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <p v-if="!loading && features.length === 0" style="text-align:center;color:#999;padding:2rem">暂无核心优势</p>
        </div>

        <div class="card">
          <div class="card-header">
            <h3>图标管理</h3>
            <el-button type="primary" @click="openIconDialog(null)">+ 新增图标</el-button>
          </div>
          <p class="card-desc">管理核心优势可用的图标，支持上传图片、编辑和删除</p>
          <div class="icons-grid">
            <div 
              v-for="icon in icons" 
              :key="icon.id"
              :class="['icon-item', { active: selectedIconId === icon.id }]"
              @click="selectIcon(icon)"
            >
              <img :src="icon.imageUrl" class="icon-image" />
              <span class="icon-name">{{ icon.name }}</span>
              <span class="icon-desc">{{ icon.description }}</span>
              <div class="icon-actions">
                <el-button size="small" type="primary" link @click.stop="openIconDialog(icon)">编辑</el-button>
                <el-button size="small" type="danger" link @click.stop="deleteIcon(icon.id)">删除</el-button>
              </div>
            </div>
          </div>
          <p v-if="icons.length === 0" style="text-align:center;color:#999;padding:2rem">暂无图标，请上传图标</p>
        </div>
      </div>

      <el-dialog v-model="dialogVisible" :title="form.id ? '编辑核心优势' : '新增核心优势'" width="500px" destroy-on-close>
        <el-form :model="form" label-width="100px">
          <el-form-item label="优势标题">
            <el-input v-model="form.title" placeholder="请输入优势标题" />
          </el-form-item>
          <el-form-item label="优势描述">
            <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入优势描述" />
          </el-form-item>
          <el-form-item label="选择图标">
            <el-select v-model="form.style" placeholder="选择图标">
              <el-option 
                v-for="icon in iconOptions" 
                :key="icon.id" 
                :value="icon.id" 
                :label="icon.name" 
              >
                <img :src="icon.imageUrl" class="option-icon" />
                <span>{{ icon.name }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="显示排序">
            <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
            <span class="form-hint">数值越小越靠前</span>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="save">{{ form.id ? '更新' : '保存' }}</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="iconDialogVisible" :title="iconForm.id ? '编辑图标' : '新增图标'" width="500px" destroy-on-close>
        <el-form :model="iconForm" label-width="100px">
          <el-form-item label="图标图片">
            <div class="upload-area">
              <img v-if="iconForm.imageUrl" :src="iconForm.imageUrl" class="preview-img" />
              <el-upload
                class="icon-upload"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :data="{ folder: 'common' }"
                name="file"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :on-success="onUploadSuccess"
                :on-error="onUploadError"
                accept=".jpg,.jpeg,.png,.gif,.svg,.webp"
              >
                <el-button type="primary" :loading="uploading">
                  {{ iconForm.imageUrl ? '更换图片' : '上传图片' }}
                </el-button>
              </el-upload>
            </div>
            <div class="form-hint">支持 jpg、png、gif、svg、webp 格式，建议尺寸 64x64</div>
          </el-form-item>
          <el-form-item label="图标名称">
            <el-input v-model="iconForm.name" placeholder="请输入图标名称" />
          </el-form-item>
          <el-form-item label="图标描述">
            <el-input v-model="iconForm.description" type="textarea" :rows="3" placeholder="请输入图标描述" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="iconDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="iconSaving" @click="saveIcon">{{ iconForm.id ? '更新' : '保存' }}</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref, computed } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const saving = ref(false)
const iconSaving = ref(false)
const loading = ref(false)
const uploading = ref(false)
const features = ref([])
const icons = ref([])
const dialogVisible = ref(false)
const iconDialogVisible = ref(false)
const selectedIconId = ref(null)
const featuresText = ref({ sectionSubtitle: '', sectionTitle: '' })
const isEditing = ref(false)

const uploadUrl = '/api/icons/upload'
const uploadHeaders = computed(() => {
  const token = sessionStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const iconOptions = computed(() => {
  return icons.value.filter(i => i.enabled)
})

const emptyForm = () => ({ id: null, title: '', description: '', style: null, sortOrder: 0 })
const form = ref(emptyForm())

const emptyIconForm = () => ({ id: null, imageUrl: '', name: '', description: '' })
const iconForm = ref(emptyIconForm())

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const getIconImage = (style) => {
  const icon = icons.value.find(i => i.id === style)
  return icon ? icon.imageUrl : null
}

const selectIcon = (icon) => {
  selectedIconId.value = icon.id
}

const openDialog = (row) => {
  if (row) {
    form.value = { ...row }
  } else {
    form.value = emptyForm()
  }
  dialogVisible.value = true
}

const openIconDialog = (row) => {
  if (row) {
    iconForm.value = { ...row }
  } else {
    iconForm.value = emptyIconForm()
  }
  iconDialogVisible.value = true
}

const loadFeatures = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/features')
    if (res.data.success) {
      // API 不返回 sortOrder，从现有数据保留排序值
      const oldMap = new Map(features.value.map(f => [f.id, f.sortOrder]))
      features.value = res.data.data.map(item => ({
        ...item,
        sortOrder: oldMap.get(item.id) ?? item.sortOrder ?? 0
      })).sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    }
  } catch (e) { console.error('加载失败', e) }
  finally { loading.value = false }
}

const loadIcons = async () => {
  try {
    const res = await axios.get('/api/icons')
    if (res.data.success) icons.value = res.data.data
  } catch (e) { console.error('加载图标失败', e) }
}

const save = async () => {
  if (!form.value.title.trim()) { ElMessage.warning('请输入标题'); return }
  saving.value = true
  try {
    const data = { 
      title: form.value.title, 
      description: form.value.description, 
      style: form.value.style?.toString(),
      sortOrder: form.value.sortOrder || 0
    }
    if (form.value.id) {
      await axios.put(`/api/features/${form.value.id}`, data)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/features', data)
      ElMessage.success('保存成功')
    }
    dialogVisible.value = false
    await loadFeatures()
  } catch (e) { ElMessage.error('保存失败') }
  finally { saving.value = false }
}

const updateSortOrder = async (row) => {
  try {
    await axios.put(`/api/features/${row.id}`, {
      title: row.title,
      description: row.description,
      style: row.style?.toString(),
      sortOrder: row.sortOrder
    })
    // 本地重排，立即生效
    features.value.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    ElMessage.success('排序已更新')
  } catch (e) {
    ElMessage.error('排序更新失败')
  }
}

const del = async (id) => {
  await ElMessageBox.confirm('确定删除吗？', '确认', { type: 'warning' })
  try {
    await axios.delete(`/api/features/${id}`)
    ElMessage.success('删除成功')
    await loadFeatures()
  } catch (e) { /* 取消删除 */ }
}

const beforeUpload = (file) => {
  const validTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/svg+xml', 'image/webp']
  if (!validTypes.includes(file.type)) {
    ElMessage.error('仅支持 jpg、png、gif、svg、webp 格式')
    return false
  }
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }
  uploading.value = true
  return true
}

const onUploadSuccess = (response) => {
  uploading.value = false
  if (response.success) {
    iconForm.value.imageUrl = response.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

const onUploadError = () => {
  uploading.value = false
  ElMessage.error('图片上传失败，请重试')
}

const saveIcon = async () => {
  if (!iconForm.value.imageUrl) { ElMessage.warning('请上传图标图片'); return }
  if (!iconForm.value.name.trim()) { ElMessage.warning('请输入图标名称'); return }
  iconSaving.value = true
  try {
    const data = { imageUrl: iconForm.value.imageUrl, name: iconForm.value.name, description: iconForm.value.description }
    let res
    if (iconForm.value.id) {
      res = await axios.put(`/api/icons/${iconForm.value.id}`, data)
    } else {
      res = await axios.post('/api/icons', data)
    }
    if (res.data.success) {
      ElMessage.success(iconForm.value.id ? '图标更新成功' : '图标创建成功')
      iconDialogVisible.value = false
      await loadIcons()
    } else {
      ElMessage.error(res.data.message || '保存失败')
    }
  } catch (e) { 
    ElMessage.error(e.response?.data?.message || '保存失败') 
  }
  finally { iconSaving.value = false }
}

const deleteIcon = async (id) => {
  await ElMessageBox.confirm('确定删除此图标吗？删除后可能影响已使用该图标的核心优势。', '确认', { type: 'warning' })
  try {
    const res = await axios.delete(`/api/icons/${id}`)
    if (res.data.success) {
      ElMessage.success('删除成功')
      await loadIcons()
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) { 
    ElMessage.error(e.response?.data?.message || '删除失败') 
  }
}

const loadFeaturesText = async () => {
  try {
    const res = await axios.get('/api/site/features')
    if (res.data.code === 200) {
      featuresText.value = res.data.data
    }
  } catch (e) {
    console.error('加载核心优势文本失败:', e)
  }
}

const saveFeaturesText = async () => {
  saving.value = true
  try {
    const res = await axios.put('/api/site/features', featuresText.value)
    if (res.data.code === 200) {
      featuresText.value = res.data.data
      ElMessage.success('保存成功')
      isEditing.value = false
    } else {
      ElMessage.error(res.data.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadFeatures()
  loadIcons()
  loadFeaturesText()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; }
.content-wrapper { padding: 2rem; display: grid; gap: 1.5rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
.card-header h3 { color: #2c3e50; margin: 0; }
.card-desc { color: #7f8c8d; font-size: 0.85rem; margin-bottom: 1.5rem; }
.icons-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); gap: 12px; }
.icon-item { padding: 12px; border-radius: 8px; border: 2px solid transparent; cursor: pointer; text-align: center; transition: all 0.3s ease; position: relative; }
.icon-item:hover { border-color: rgba(0, 168, 89, 0.3); background: rgba(0, 168, 89, 0.02); }
.icon-item.active { border-color: #00a859; background: rgba(0, 168, 89, 0.08); }
.icon-image { width: 48px; height: 48px; object-fit: contain; display: block; margin: 0 auto 8px; }
.icon-name { font-size: 0.85rem; font-weight: 600; color: #2c3e50; display: block; margin-bottom: 2px; }
.icon-desc { font-size: 0.75rem; color: #7f8c8d; display: block; margin-bottom: 8px; }
.icon-actions { display: flex; justify-content: center; gap: 8px; }
.table-icon { width: 32px; height: 32px; object-fit: contain; }
.option-icon { width: 20px; height: 20px; object-fit: contain; vertical-align: middle; margin-right: 6px; }
.upload-area { display: flex; align-items: center; gap: 12px; }
.preview-img { width: 64px; height: 64px; object-fit: contain; border: 1px solid #e5e7eb; border-radius: 6px; }
.form-hint { font-size: 0.75rem; color: #999; margin-top: 4px; }
.text-display { padding: 0; border: 1px solid #e5e7eb; border-radius: 4px; overflow: hidden; }
.text-item { display: flex; align-items: center; border-bottom: 1px solid #e5e7eb; }
.text-item:last-child { border-bottom: none; }
.text-label { width: 100px; padding: 0.75rem 1rem; color: #7f8c8d; font-weight: 600; background: #f8fafc; border-right: 1px solid #e5e7eb; }
.text-value { flex: 1; padding: 0.75rem 1rem; color: #2c3e50; font-size: 1rem; }
.form-actions { display: flex; justify-content: flex-end; gap: 0.5rem; margin-top: 1rem; }
.desc-text { white-space: pre-wrap; word-break: break-word; line-height: 1.6; }
@media (max-width: 768px) { .admin-page { flex-direction: column; } .icons-grid { grid-template-columns: repeat(3, 1fr); } }
</style>