<template>
  <section class="content-module-editor">
    <div class="editor-header">
      <div>
        <h3>{{ title }}</h3>
        <p>编辑该页面在公司网站中的展示内容，可按模块控制是否展示。</p>
      </div>
      <div class="editor-actions">
        <el-button :loading="loadingModules" @click="loadModules(true)">刷新</el-button>
        <el-button type="primary" :loading="saving" @click="saveModules">保存展示模块</el-button>
      </div>
    </div>

    <div v-for="definition in visibleDefinitions" :key="definition.key" class="module-card">
      <div class="module-card-header">
        <h4>{{ definition.title }}</h4>
        <el-switch
          v-model="modules[definition.key].enabled"
          active-text="展示"
          inactive-text="隐藏"
        />
      </div>

      <div class="module-card-body">
        <div v-if="definition.fields?.length" class="field-grid">
          <label v-for="field in definition.fields" :key="field.key" class="field" :class="{ wide: field.type === 'textarea' }">
            <span>{{ field.label }}</span>
            <el-input
              v-model="modules[definition.key][field.key]"
              :type="field.type === 'textarea' ? 'textarea' : 'text'"
              :rows="field.type === 'textarea' ? 4 : undefined"
            />
          </label>
        </div>

        <div v-for="arrayDef in definition.arrays || []" :key="arrayDef.key" class="array-editor">
          <div class="array-header">
            <h5>{{ arrayDef.title }}</h5>
            <el-button size="small" type="primary" @click="addArrayItem(definition.key, arrayDef)">新增</el-button>
          </div>

          <div
            v-for="(item, index) in modules[definition.key][arrayDef.key]"
            :key="index"
            class="array-item"
          >
            <template v-if="arrayDef.type === 'paragraphs'">
              <el-input v-model="modules[definition.key][arrayDef.key][index]" type="textarea" :rows="3" />
            </template>
            <div v-else class="array-fields">
              <label v-for="field in arrayDef.fields" :key="field.key" class="field" :class="{ wide: field.type === 'textarea' }">
                <span>{{ field.label }}</span>
                <el-input
                  v-model="item[field.key]"
                  :type="field.type === 'textarea' ? 'textarea' : 'text'"
                  :rows="field.type === 'textarea' ? 3 : undefined"
                />
              </label>
            </div>
            <el-button size="small" type="danger" plain @click="removeArrayItem(definition.key, arrayDef.key, index)">
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { siteApi } from '@/api'

const props = defineProps({
  scope: {
    type: String,
    required: true
  },
  title: {
    type: String,
    default: '官网展示模块'
  }
})

const saving = ref(false)
const loadingModules = ref(false)

const defaultModules = {
  homeSpotlight: {
    enabled: false,
    subtitle: '',
    title: '',
    description: '',
    buttonText: '',
    buttonLink: '',
    metrics: []
  },
  aboutHero: {
    enabled: false,
    eyebrow: '',
    title: '',
    description: '',
    stats: []
  },
  aboutPosition: {
    enabled: false,
    subtitle: '',
    title: '',
    paragraphs: []
  },
  aboutMission: {
    enabled: false,
    items: []
  },
  aboutCapabilities: {
    enabled: false,
    subtitle: '',
    title: '',
    items: []
  },
  aboutTeam: {
    enabled: false,
    subtitle: '',
    title: '',
    description: '',
    proof: []
  },
  aboutMilestones: {
    enabled: false,
    subtitle: '',
    title: '',
    items: []
  },
  productsHero: {
    enabled: false,
    eyebrow: '',
    title: '',
    description: '',
    metricValue: '',
    metricLabel: ''
  },
  productsOverview: {
    enabled: false,
    subtitle: '',
    title: '',
    description: '',
    highlights: []
  },
  productsTechnology: {
    enabled: false,
    subtitle: '',
    title: '',
    items: []
  },
  productsSeries: {
    enabled: false,
    subtitle: '',
    title: '',
    items: []
  },
  productsScenarios: {
    enabled: false,
    subtitle: '',
    title: '',
    items: []
  }
}

const definitions = {
  home: [
    {
      key: 'homeSpotlight',
      title: '首页 XRE 技术亮点',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' },
        { key: 'description', label: '描述', type: 'textarea' },
        { key: 'buttonText', label: '按钮文字' },
        { key: 'buttonLink', label: '按钮链接' }
      ],
      arrays: [
        {
          key: 'metrics',
          title: '指标',
          template: { value: '', label: '' },
          fields: [
            { key: 'value', label: '数值' },
            { key: 'label', label: '说明' }
          ]
        }
      ]
    }
  ],
  about: [
    {
      key: 'aboutHero',
      title: '关于我们首屏',
      fields: [
        { key: 'eyebrow', label: '英文标识' },
        { key: 'title', label: '主标题' },
        { key: 'description', label: '描述', type: 'textarea' }
      ],
      arrays: [
        {
          key: 'stats',
          title: '首屏指标',
          template: { value: '', label: '' },
          fields: [
            { key: 'value', label: '数值' },
            { key: 'label', label: '说明' }
          ]
        }
      ]
    },
    {
      key: 'aboutPosition',
      title: '公司定位',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' }
      ],
      arrays: [{ key: 'paragraphs', title: '段落', type: 'paragraphs', template: '' }]
    },
    {
      key: 'aboutMission',
      title: '愿景使命价值观',
      arrays: [
        {
          key: 'items',
          title: '条目',
          template: { title: '', text: '' },
          fields: [
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' }
          ]
        }
      ]
    },
    {
      key: 'aboutCapabilities',
      title: '核心能力',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' }
      ],
      arrays: [
        {
          key: 'items',
          title: '能力卡片',
          template: { index: '', title: '', text: '' },
          fields: [
            { key: 'index', label: '序号' },
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' }
          ]
        }
      ]
    },
    {
      key: 'aboutTeam',
      title: '核心团队',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' },
        { key: 'description', label: '描述', type: 'textarea' }
      ],
      arrays: [
        {
          key: 'proof',
          title: '团队指标',
          template: { value: '', text: '' },
          fields: [
            { key: 'value', label: '数值' },
            { key: 'text', label: '说明' }
          ]
        }
      ]
    },
    {
      key: 'aboutMilestones',
      title: '荣誉与进展',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' }
      ],
      arrays: [
        {
          key: 'items',
          title: '进展卡片',
          template: { time: '', title: '', text: '' },
          fields: [
            { key: 'time', label: '时间' },
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' }
          ]
        }
      ]
    }
  ],
  products: [
    {
      key: 'productsHero',
      title: '产品服务首屏',
      fields: [
        { key: 'eyebrow', label: '英文标识' },
        { key: 'title', label: '主标题' },
        { key: 'description', label: '描述', type: 'textarea' },
        { key: 'metricValue', label: '指标数值' },
        { key: 'metricLabel', label: '指标说明' }
      ]
    },
    {
      key: 'productsOverview',
      title: '产品定位',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' },
        { key: 'description', label: '描述', type: 'textarea' }
      ],
      arrays: [
        {
          key: 'highlights',
          title: '亮点',
          template: { title: '', text: '' },
          fields: [
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' }
          ]
        }
      ]
    },
    {
      key: 'productsTechnology',
      title: '技术亮点',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' }
      ],
      arrays: [
        {
          key: 'items',
          title: '技术卡片',
          template: { index: '', title: '', text: '' },
          fields: [
            { key: 'index', label: '序号' },
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' }
          ]
        }
      ]
    },
    {
      key: 'productsSeries',
      title: '产品系列',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' }
      ],
      arrays: [
        {
          key: 'items',
          title: '系列卡片',
          template: { title: '', text: '', tag: '' },
          fields: [
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' },
            { key: 'tag', label: '标签' }
          ]
        }
      ]
    },
    {
      key: 'productsScenarios',
      title: '应用场景',
      fields: [
        { key: 'subtitle', label: '小标题' },
        { key: 'title', label: '主标题' }
      ],
      arrays: [
        {
          key: 'items',
          title: '场景卡片',
          template: { title: '', text: '', image: '' },
          fields: [
            { key: 'title', label: '标题' },
            { key: 'text', label: '说明', type: 'textarea' },
            { key: 'image', label: '图片地址' }
          ]
        }
      ]
    }
  ]
}

const clone = (value) => JSON.parse(JSON.stringify(value))
const modules = ref(clone(defaultModules))

const visibleDefinitions = computed(() => definitions[props.scope] || [])

const mergeDefaults = (defaults, saved) => {
  const result = clone(defaults)
  Object.keys(saved || {}).forEach((key) => {
    if (result[key] && typeof saved[key] === 'object' && !Array.isArray(saved[key])) {
      result[key] = { ...result[key], ...saved[key] }
    }
  })
  return result
}

const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms))

const shouldRetry = (error) => {
  const status = error.response?.status
  return !status || status >= 500 || status === 429
}

const requestModulesWithRetry = async () => {
  let lastError
  for (let attempt = 0; attempt < 3; attempt += 1) {
    try {
      return await siteApi.getModules()
    } catch (error) {
      lastError = error
      if (!shouldRetry(error) || attempt === 2) {
        break
      }
      await delay(500 * (attempt + 1))
    }
  }
  throw lastError
}

const loadModules = async (showMessage = false) => {
  if (loadingModules.value) return
  loadingModules.value = true
  try {
    const response = await requestModulesWithRetry()
    modules.value = mergeDefaults(defaultModules, response.data.data || {})
    if (showMessage) {
      ElMessage.success('展示模块已刷新')
    }
  } catch (error) {
    console.warn('加载展示模块失败，已保留默认内容', error)
    if (showMessage) {
      ElMessage.warning('暂时无法刷新展示模块，请稍后再试')
    }
  } finally {
    loadingModules.value = false
  }
}

const saveModules = async () => {
  saving.value = true
  try {
    const payload = {}
    visibleDefinitions.value.forEach((definition) => {
      payload[definition.key] = modules.value[definition.key]
    })
    await siteApi.saveModules(payload)
    ElMessage.success('展示模块已保存')
  } catch (error) {
    ElMessage.error('保存失败，请稍后重试')
  } finally {
    saving.value = false
  }
}

const addArrayItem = (moduleKey, arrayDef) => {
  modules.value[moduleKey][arrayDef.key].push(clone(arrayDef.template))
}

const removeArrayItem = (moduleKey, arrayKey, index) => {
  modules.value[moduleKey][arrayKey].splice(index, 1)
}

onMounted(loadModules)
</script>

<style scoped>
.content-module-editor {
  background: white;
  border-radius: 10px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.editor-header,
.module-card-header,
.array-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.editor-header {
  padding-bottom: 1rem;
  border-bottom: 1px solid #edf2f7;
  margin-bottom: 1rem;
}

.editor-header h3,
.module-card-header h4,
.array-header h5 {
  color: #2c3e50;
  margin: 0;
}

.editor-header p {
  margin-top: 0.35rem;
  color: #7f8c8d;
}

.editor-actions {
  display: flex;
  gap: 0.75rem;
}

.module-card {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
  margin-top: 1rem;
}

.module-card-header {
  padding: 1rem 1.2rem;
  background: #f8fafc;
}

.module-card-body {
  padding: 1.2rem;
}

.field-grid,
.array-fields {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1rem;
}

.field {
  display: block;
}

.field.wide {
  grid-column: 1 / -1;
}

.field span {
  display: block;
  color: #475569;
  font-weight: 600;
  margin-bottom: 0.45rem;
}

.array-editor {
  margin-top: 1.2rem;
}

.array-item {
  background: #fbfdff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 1rem;
  margin-top: 0.8rem;
}

.array-item .el-button {
  margin-top: 0.75rem;
}

@media (max-width: 768px) {
  .editor-header,
  .module-card-header,
  .array-header {
    align-items: stretch;
    flex-direction: column;
  }

  .field-grid,
  .array-fields {
    grid-template-columns: 1fr;
  }
}
</style>