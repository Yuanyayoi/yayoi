<template>
  <div class="admin-page">
    <AdminSidebar @logout="handleLogout" />
    <main class="main-content">
      <header class="top-bar">
        <h1>核心团队</h1>
        <TopUserInfo />
      </header>
      <div class="content-wrapper">
        <div class="card">
          <div class="card-header">
            <h3>团队成员管理</h3>
            <el-button type="primary" @click="showAddDialog">添加成员</el-button>
          </div>
          <div class="team-grid">
            <div v-for="member in teamMembers" :key="member.id" class="team-card">
              <div class="card-actions">
                <el-button size="small" @click="editMember(member)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteMember(member.id)">删除</el-button>
              </div>
              <div class="avatar-wrapper">
                <img v-if="member.avatar" :src="member.avatar" :alt="member.name" />
                <div v-else class="avatar-placeholder">{{ member.name?.charAt(0) || '?' }}</div>
              </div>
              <div class="member-info">
                <h4>{{ member.name }}</h4>
                <p class="position">{{ member.position }}</p>
                <p class="bio">{{ member.bio }}</p>
                <div class="member-meta">
                  <el-tag :type="member.enabled ? 'success' : 'info'" size="small">
                    {{ member.enabled ? '启用' : '停用' }}
                  </el-tag>
                  <span class="sort">排序: {{ member.sortOrder }}</span>
                </div>
              </div>
            </div>
            <div v-if="teamMembers.length === 0" class="empty-state">
              <p>暂无团队成员，点击"添加成员"开始</p>
            </div>
          </div>
        </div>
      </div>
    </main>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑成员' : '添加成员'" width="600px">
      <el-form :model="teamForm" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="teamForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="teamForm.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="teamForm.bio" type="textarea" :rows="3" placeholder="请输入简介" />
        </el-form-item>
        <el-form-item label="头像">
          <div class="upload-wrapper">
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :http-request="handleUpload"
              accept="image/*"
            >
              <img v-if="teamForm.avatar" :src="teamForm.avatar" class="avatar-preview" />
              <el-icon v-else class="uploader-icon"><Plus /></el-icon>
            </el-upload>
            <el-button v-if="teamForm.avatar" type="danger" size="small" @click="removeAvatar" class="remove-btn">移除</el-button>
          </div>
          <div class="form-hint">支持 jpg、png、gif 格式，建议尺寸 200x200</div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="teamForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="启用">
          <el-switch v-model="teamForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveMember">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/AdminSidebar.vue'
import TopUserInfo from '@/components/TopUserInfo.vue'
import { onMounted, ref } from 'vue'
import api, { siteApi, uploadApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'


const saving = ref(false)
const teamMembers = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const emptyForm = () => ({ id: null, name: '', position: '', bio: '', avatar: '', sortOrder: 0, enabled: true })
const teamForm = ref(emptyForm())

const handleLogout = () => {
  sessionStorage.removeItem('token')
  sessionStorage.removeItem('user')
  window.location.href = '/admin/login'
}

const handleUpload = async (options) => {
  const file = options.file
  try {
    const res = await uploadApi.uploadImage(file, 'team')
    if (res.data?.code === 200 && res.data.data?.url) {
      teamForm.value.avatar = res.data.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.data?.message || '上传失败')
    }
  } catch (e) {
    console.error('上传失败:', e)
    ElMessage.error(e.response?.data?.message || '上传失败')
  }
}

const removeAvatar = async () => {
  if (!teamForm.value.avatar) return
  try {
    await api.delete('/api/upload/delete', { params: { fileUrl: teamForm.value.avatar } })
  } catch (e) {
    // 忽略删除文件失败
  }
  teamForm.value.avatar = ''
  ElMessage.success('图片已移除')
}

const showAddDialog = () => {
  teamForm.value = emptyForm()
  isEdit.value = false
  dialogVisible.value = true
}

const editMember = (member) => {
  teamForm.value = { ...member }
  isEdit.value = true
  dialogVisible.value = true
}

const loadTeam = async () => {
  try {
    const res = await siteApi.getTeamMembers()
    teamMembers.value = res.data.data || []
  } catch (e) { console.error('加载失败', e) }
}

const saveMember = async () => {
  if (!teamForm.value.name.trim()) { ElMessage.warning('请输入姓名'); return }
  saving.value = true
  try {
    if (teamForm.value.id) {
      await siteApi.updateTeamMember(teamForm.value.id, teamForm.value)
    } else {
      await siteApi.createTeamMember(teamForm.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await loadTeam()
  } catch (e) { ElMessage.error('保存失败') }
  finally { saving.value = false }
}

const deleteMember = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该成员吗？', '提示', { type: 'warning' })
    await siteApi.deleteTeamMember(id)
    ElMessage.success('删除成功')
    await loadTeam()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => {
  loadTeam()
})
</script>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.main-content { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.top-bar { background: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.top-bar h1 { color: #2c3e50; }
.content-wrapper { padding: 2rem; }
.card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
.card-header h3 { color: #2c3e50; margin: 0; }

.team-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.5rem; }
.team-card { background: #f8fafc; border-radius: 12px; padding: 1.5rem; position: relative; transition: all 0.3s; border: 1px solid #e5e7eb; }
.team-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.08); transform: translateY(-2px); }
.card-actions { position: absolute; top: 1rem; right: 1rem; display: flex; gap: 0.5rem; }
.avatar-wrapper { width: 100px; height: 100px; margin: 0 auto 1rem; }
.avatar-wrapper img { width: 100%; height: 100%; object-fit: cover; border-radius: 50%; border: 3px solid white; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.avatar-placeholder { width: 100%; height: 100%; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-size: 2rem; font-weight: 600; }
.member-info { text-align: center; }
.member-info h4 { color: #2c3e50; margin: 0 0 0.5rem; font-size: 1.1rem; }
.member-info .position { color: #00a859; font-weight: 600; margin: 0 0 0.75rem; font-size: 0.9rem; }
.member-info .bio { color: #718096; font-size: 0.85rem; line-height: 1.5; margin: 0 0 1rem; }
.member-meta { display: flex; justify-content: center; align-items: center; gap: 1rem; }
.member-meta .sort { color: #a0aec0; font-size: 0.8rem; }
.empty-state { grid-column: 1 / -1; text-align: center; padding: 3rem; color: #a0aec0; }

.form-hint { font-size: 0.75rem; color: #999; margin-top: 4px; }
.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-uploader :deep(.el-upload:hover) { border-color: #00a859; }
.uploader-icon { font-size: 28px; color: #8c939d; }
.avatar-preview { width: 100px; height: 100px; object-fit: cover; display: block; border-radius: 6px; }
.upload-wrapper { display: flex; align-items: flex-start; gap: 0.75rem; }
.remove-btn { margin-top: 0.5rem; }

@media (max-width: 768px) { .admin-page { flex-direction: column; } .team-grid { grid-template-columns: 1fr; } }
</style>
