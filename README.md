# 国际化资源文件提取工具

一个用于从abc产品盘中提取前后端国际化资源文件的独立工具。

## ✅ 功能特性

- **后端提取**: .properties文件（简体中文、英文、繁体中文）
- **前端提取**: .json文件（en.json, zh-CHS.json, zh-CHT.json）
- **语言选择**: 可选择需要提取的语言
- **目录浏览**: 树形展示提取的文件
- **文件预览**: 支持.properties和.json文件预览
- **扩展机制**: 支持通过JAR包扩展自定义提取器
- **独立部署**: 内嵌Tomcat服务器，开箱即用

## 🚀 快速开始

### 1. 构建项目
```bash
build-all.bat
```

### 2. 启动应用
```bash
cd tool
startup-win.cmd
```

### 3. 访问应用
```
http://localhost:8080
```

## 📋 环境要求

- **JDK 17+**
- **Maven 3.6+**
- **Node.js 16+**

## 📁 项目结构

```
i18n-tool/
├── backend/              # Spring Boot后端
├── frontend/             # Vue 3前端
├── tool/                 # 最终工具目录
│   ├── config/          # 配置文件
│   ├── runtime/         # JAR文件
│   ├── web/             # 前端静态资源
│   ├── extensions/      # 扩展目录
│   ├── version/         # 版本信息
│   ├── startup-win.cmd  # Windows启动脚本
│   └── startup-linux.sh # Linux启动脚本
├── build-all.bat        # 一键构建脚本
├── verify-fix.bat       # 编译验证脚本
└── check-before-start.bat # 启动前检查
```

## 🎯 使用说明

### 1. 设置盘根目录
- 方法1: 点击"abc默认路径"快速设置
- 方法2: 点击"自定义路径"输入自定义路径
- 方法3: 直接在文本框中输入路径

### 2. 选择语言
勾选需要提取的语言：
- 简体中文（zh-CHS）
- 英文（en）
- 繁体中文（zh-CHT）

### 3. 开始提取
点击"开始提取"按钮，等待提取完成

### 4. 查看结果
- 左侧显示提取文件的树形结构
- 点击文件节点进行预览
- 右侧显示文件内容

## 🔧 技术栈

- **后端**: Java 17 + Spring Boot 3.2
- **前端**: Vue 3 + Element Plus + Vite
- **构建工具**: Maven + npm
- **服务器**: 内嵌Tomcat

## 📖 详细文档

- [QUICKSTART.md](QUICKSTART.md) - 快速开始指南
- [EXTRACTOR_FIX.md](EXTRACTOR_FIX.md) - 提取器修复说明
- [API_400_FIX.md](API_400_FIX.md) - API错误修复
- [COMPILE_FIX_FINAL.md](COMPILE_FIX_FINAL.md) - 编译问题修复
- [ENCODING_FIX.md](ENCODING_FIX.md) - 中文乱码修复
- [FRONTEND_FIX.md](FRONTEND_FIX.md) - 前端功能更新
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - 故障排除

## ⚠️ 常见问题

### 编译问题
如果遇到 `invalid flag: --release` 错误：
```bash
verify-fix.bat
```

### API 400错误
如果提取时返回400错误：
- 确保已选择至少一种语言
- 重新构建前后端：`build-all.bat`
- 参考：[API_400_FIX.md](API_400_FIX.md)

### 中文乱码
如果启动时出现中文乱码：
- 已修复，确保使用最新的启动脚本
- 检查系统区域设置
- 参考：[ENCODING_FIX.md](ENCODING_FIX.md)

### 功能更新
如果需要最新的功能改进：
- 完整重新构建：`build-all.bat`
- 参考：[REBUILD_ALL.md](REBUILD_ALL.md)

## 🔍 提取规则

### 后端资源
- 目录: `server/resources/`, `server/resources/en/`, `server/resources/zh-CHT/`
- 格式: `.properties` 文件
- 语言: 默认中文、英文、繁体中文

### 前端资源
- 目录: `web/i18n/`
- 格式: `.json` 文件
- 文件: `en.json`, `zh-CHS.json`, `zh-CHT.json`

## 🚀 扩展开发

支持通过JAR包扩展自定义提取器：

1. 实现 `ResourceExtractor` 接口
2. 打包为JAR文件
3. 放入 `tool/extensions/` 目录
4. 在 `config/application.yaml` 中配置

详见 `tool/extensions/README.md`

## 📞 获取帮助

- 运行 `check-before-start.bat` 检查环境
- 查看 `TROUBLESHOOTING.md` 故障排除
- 检查控制台日志获取详细错误信息

## 🎯 版本信息

- **版本**: 1.0.0
- **构建日期**: 2025-08-16
- **支持平台**: Windows、Linux、macOS

---

**所有已知问题已修复，功能完整！** ✅
