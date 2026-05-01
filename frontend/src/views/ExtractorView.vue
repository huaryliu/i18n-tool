<template>
  <div class="extractor-view">
    <el-container>
      <el-header class="header">
        <h1>abc 国际化资源文件提取工具</h1>
      </el-header>

      <el-container class="vertical-container">
        <!-- 顶部配置区域 -->
        <el-card class="config-panel">
          <template #header>
            <span>提取配置</span>
          </template>

          <el-form :model="config" label-width="100px" class="config-form">
            <el-row :gutter="20">
              <el-col :span="16">
                <el-form-item label="盘根目录">
                  <el-input
                    v-model="config.rootPath"
                    placeholder="输入盘根目录路径，例如: D:\worker\abc\abc_2508_x86_64_build20250818"
                    type="textarea"
                    :rows="2"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item label="快速选择">
                  <el-space wrap>
                    <el-button size="small" @click="setPath('abc')">abc默认路径</el-button>
                    <el-button size="small" @click="setPath('custom')">自定义路径</el-button>
                  </el-space>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="16">
                <el-form-item label="语言选择">
                  <el-checkbox-group v-model="config.languages">
                    <el-checkbox label="zh-CHS">简体中文</el-checkbox>
                    <el-checkbox label="en">英文</el-checkbox>
                    <el-checkbox label="zh-CHT">繁体中文</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item>
                  <el-button
                    type="primary"
                    @click="startExtraction"
                    :loading="extracting"
                    :disabled="!canExtract"
                    style="width: 100%"
                  >
                    {{ extracting ? '提取中...' : '开始提取' }}
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>

          <el-alert
            v-if="result"
            :title="`提取完成，共找到 ${result.count} 个文件`"
            type="success"
            :closable="false"
            show-icon
            style="margin-top: 10px"
          />
        </el-card>

        <!-- 底部结果区域 -->
        <el-main class="result-content">
          <el-card v-if="result" class="result-panel">
            <template #header>
              <span>提取结果</span>
            </template>

            <el-container class="result-container">
              <el-aside width="300px">
                <FileTree
                  :files="result.files"
                  @file-select="onFileSelect"
                />
              </el-aside>

              <el-main>
                <FilePreview
                  :file="selectedFile"
                  v-if="selectedFile"
                />
                <el-empty v-else description="请选择文件进行预览" />
              </el-main>
            </el-container>
          </el-card>

          <el-card v-else class="empty-state">
            <el-empty description="请配置提取选项并点击开始提取" />
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { extractorApi } from '../api/extractor'
import FileTree from '../components/FileTree.vue'
import FilePreview from '../components/FilePreview.vue'

export default {
  name: 'ExtractorView',
  components: {
    FileTree,
    FilePreview
  },
  setup() {
    const config = ref({
      rootPath: 'D:\\backup\\worker\\abc\\abc_2508_x86_64_build20250818',
      languages: ['zh-CHS']
    })

    const extracting = ref(false)
    const result = ref(null)
    const selectedFile = ref(null)

    const canExtract = computed(() => {
      return config.value.rootPath && config.value.languages.length > 0
    })

    const selectDirectory = () => {
      // 由于浏览器安全限制，无法直接选择目录
      // 用户需要手动输入路径或使用快速选择功能
      ElMessage.info('请在上方输入框中输入盘根目录路径，或使用快速选择功能')
    }

    const setPath = (type) => {
      switch (type) {
        case 'abc':
          // 设置默认的abc路径
          config.value.rootPath = 'D:\\backup\\worker\\abc\\abc_2508_x86_64_build20250818'
          ElMessage.success('已设置为默认abc路径')
          break
        case 'custom':
          // 提示用户输入自定义路径
          ElMessageBox.prompt('请输入完整的盘根目录路径:', '自定义路径', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputValue: config.value.rootPath,
            inputPattern: /.+/,
            inputErrorMessage: '路径不能为空'
          }).then(({ value }) => {
            config.value.rootPath = value
            ElMessage.success('路径已更新')
          }).catch(() => {
            ElMessage.info('已取消')
          })
          break
        default:
          ElMessage.warning('未知的路径类型')
      }
    }

    const startExtraction = async () => {
      if (!canExtract.value) {
        ElMessage.warning('请先配置提取选项')
        return
      }

      if (config.value.languages.length === 0) {
        ElMessage.warning('请至少选择一种语言')
        return
      }

      extracting.value = true
      result.value = null
      selectedFile.value = null

      try {
        const requestData = {
          rootPath: config.value.rootPath,
          languages: Array.from(config.value.languages)
        }

        console.log('Sending extraction request:', requestData)

        const response = await extractorApi.extract(requestData)

        result.value = response
        ElMessage.success(`提取完成，共找到 ${response.count} 个文件`)
      } catch (error) {
        console.error('Extraction failed:', error)
        ElMessage.error('提取失败：' + (error.response?.data?.message || error.message))
      } finally {
        extracting.value = false
      }
    }

    const onFileSelect = (file) => {
      selectedFile.value = file
    }

    return {
      config,
      extracting,
      result,
      selectedFile,
      canExtract,
      selectDirectory,
      setPath,
      startExtraction,
      onFileSelect
    }
  }
}
</script>

<style scoped>
.extractor-view {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.header h1 {
  margin: 0;
  font-size: 24px;
}

.vertical-container {
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.config-panel {
  margin: 20px;
  flex-shrink: 0;
}

.config-form {
  margin-bottom: 0;
}

.result-content {
  flex: 1;
  padding: 0 20px 20px 20px;
  overflow: hidden;
}

.result-panel {
  height: 100%;
}

.empty-state {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.result-container {
  height: calc(100% - 60px);
}

:deep(.el-checkbox-group) {
  display: flex;
  gap: 15px;
}

:deep(.el-textarea__inner) {
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 13px;
  resize: vertical;
}

:deep(.el-empty) {
  padding: 40px 0;
}
</style>
