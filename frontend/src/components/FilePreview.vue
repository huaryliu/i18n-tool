<template>
  <div class="file-preview">
    <el-card class="preview-card">
      <template #header>
        <div class="preview-header">
          <div class="file-info">
            <el-icon><Document /></el-icon>
            <span class="file-name">{{ file?.name }}</span>
            <el-tag size="small" type="info">{{ file?.language }}</el-tag>
            <el-tag size="small" type="success">{{ file?.fileType }}</el-tag>
          </div>
          <div class="file-meta">
            <span>{{ formatFileSize(file?.size) }}</span>
          </div>
        </div>
      </template>

      <div class="preview-content">
        <div v-if="file?.fileType === 'properties'" class="properties-preview">
          <el-table :data="parseProperties(file.content)" stripe border>
            <el-table-column prop="key" label="键" width="300" />
            <el-table-column prop="value" label="值" />
          </el-table>
        </div>

        <div v-else-if="file?.fileType === 'json'" class="json-preview">
          <pre class="json-content">{{ formatJson(file.content) }}</pre>
        </div>

        <div v-else class="text-preview">
          <pre class="text-content">{{ file.content }}</pre>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { computed } from 'vue'
import { Document } from '@element-plus/icons-vue'

export default {
  name: 'FilePreview',
  components: {
    Document
  },
  props: {
    file: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const parseProperties = (content) => {
      if (!content) return []

      return content.split('\n')
        .filter(line => line.trim() && !line.trim().startsWith('#'))
        .map(line => {
          const separatorIndex = line.indexOf('=')
          if (separatorIndex === -1) {
            return { key: line.trim(), value: '' }
          }
          return {
            key: line.substring(0, separatorIndex).trim(),
            value: line.substring(separatorIndex + 1).trim()
          }
        })
    }

    const formatJson = (content) => {
      if (!content) return ''
      try {
        return JSON.stringify(JSON.parse(content), null, 2)
      } catch (e) {
        return content
      }
    }

    const formatFileSize = (bytes) => {
      if (!bytes) return '0 B'
      const k = 1024
      const sizes = ['B', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
    }

    return {
      parseProperties,
      formatJson,
      formatFileSize
    }
  }
}
</script>

<style scoped>
.file-preview {
  height: 100%;
}

.preview-card {
  height: 100%;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.file-name {
  font-weight: bold;
  font-size: 16px;
}

.file-meta {
  color: #666;
  font-size: 14px;
}

.preview-content {
  height: calc(100vh - 250px);
  overflow-y: auto;
}

.properties-preview {
  height: 100%;
}

.json-preview,
.text-preview {
  height: 100%;
}

.json-content,
.text-content {
  margin: 0;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
  overflow-x: auto;
}

.json-content {
  color: #333;
}

:deep(.el-table) {
  font-size: 14px;
}
</style>
