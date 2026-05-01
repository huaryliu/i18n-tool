<template>
  <div class="file-tree">
    <el-tree
      :data="treeData"
      :props="treeProps"
      @node-click="onNodeClick"
      default-expand-all
      :highlight-current="true"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <el-icon v-if="data.isDirectory">
            <Folder />
          </el-icon>
          <el-icon v-else>
            <Document />
          </el-icon>
          <span class="node-label">{{ node.label }}</span>
          <el-tag v-if="data.language" size="small" type="info" style="margin-left: 8px">
            {{ data.language }}
          </el-tag>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { Folder, Document } from '@element-plus/icons-vue'

export default {
  name: 'FileTree',
  components: {
    Folder,
    Document
  },
  props: {
    files: {
      type: Array,
      required: true
    }
  },
  emits: ['file-select'],
  setup(props, { emit }) {
    const treeProps = {
      children: 'children',
      label: 'name'
    }

    const treeData = computed(() => {
      const buildTree = (files) => {
        const root = { name: 'root', children: [], isDirectory: true }

        files.forEach(file => {
          const parts = file.path.split('/')
          let currentNode = root

          parts.forEach((part, index) => {
            let existingNode = currentNode.children?.find(child => child.name === part)

            if (!existingNode) {
              const isFile = index === parts.length - 1
              const newNode = {
                name: part,
                path: file.path,
                originalPath: file.originalPath,
                content: file.content,
                language: file.language,
                fileType: file.fileType,
                size: file.size,
                isDirectory: !isFile,
                children: isFile ? undefined : []
              }

              if (!currentNode.children) {
                currentNode.children = []
              }
              currentNode.children.push(newNode)
              currentNode = newNode
            } else {
              currentNode = existingNode
            }
          })
        })

        return root.children || []
      }

      return buildTree(props.files)
    })

    const onNodeClick = (data) => {
      if (!data.isDirectory) {
        emit('file-select', data)
      }
    }

    return {
      treeProps,
      treeData,
      onNodeClick
    }
  }
}
</script>

<style scoped>
.file-tree {
  height: 100%;
  overflow-y: auto;
}

.custom-tree-node {
  display: flex;
  align-items: center;
  gap: 6px;
  flex: 1;
  padding: 4px 0;
}

.node-label {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.el-tree-node__content) {
  height: 32px;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}
</style>
