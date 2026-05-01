# 快速开始指南

## 一键构建和运行

### Windows 用户

1. **构建项目**
   - 双击运行 `build-all.bat`
   - 等待构建完成（首次构建可能需要10-15分钟）

2. **运行工具**
   - 进入 `tool` 目录
   - 双击运行 `startup-win.cmd`
   - 浏览器会自动打开 http://localhost:8080

### Linux/Mac 用户

1. **构建项目**
   ```bash
   chmod +x build-all.sh
   ./build-all.sh
   ```

2. **运行工具**
   ```bash
   cd tool
   chmod +x startup-linux.sh
   ./startup-linux.sh
   ```
   - 手动打开浏览器访问 http://localhost:8080

## 基本使用

1. **设置盘根目录**
   - 在左侧配置面板中输入iGIX产品盘的根目录
   - 示例：`D:\老电脑备份\worker\iGIX\igix_2508_x86_64_build20250818`

2. **选择语言**
   - 勾选需要提取的语言：
     - 简体中文（zh-CHS）
     - 英文（en）
     - 繁体中文（zh-CHT）

3. **开始提取**
   - 点击"开始提取"按钮
   - 等待提取完成

4. **查看结果**
   - 左侧树形结构显示提取的文件
   - 点击文件节点进行预览
   - 右侧显示文件内容

## 目录说明

```
i18n-tool/
├── tool/                    # 最终工具目录（交付物）
│   ├── config/              # 配置文件
│   ├── runtime/             # 运行时JAR包
│   ├── web/                 # 前端静态资源
│   ├── extensions/          # 扩展脚本目录
│   ├── version/             # 版本信息
│   ├── startup-win.cmd      # Windows启动脚本
│   └── startup-linux.sh     # Linux启动脚本
├── backend/                 # 后端源代码
├── frontend/                # 前端源代码
├── build-all.bat            # Windows构建脚本
└── build-all.sh             # Linux构建脚本
```

## 环境要求

- **JDK 17+**: 已配置为 `D:\老电脑备份\software\OpenJDK17U-jdk_x64_windows_hotspot_17.0.4.1_1\jdk-17.0.4.1+1`
- **Maven 3.6+**: 用于构建后端
- **Node.js 16+**: 用于构建前端

## 故障排除

### 构建失败
- 确保已安装Maven和Node.js
- 检查网络连接（需要下载依赖）
- 查看错误信息获取详细信息

### 启动失败
- 检查JDK路径是否正确
- 检查8080端口是否被占用
- 查看控制台日志获取详细错误信息

### 提取失败
- 检查盘根目录路径是否正确
- 检查目录是否存在对应的资源文件夹
- 确保有足够的磁盘空间

## 高级功能

### 扩展开发
查看 `tool/extensions/README.md` 了解如何开发自定义提取器。

### 配置调整
修改 `tool/config/application.yaml` 来调整应用配置。

### 日志查看
应用日志会在控制台输出，可以配置日志级别。

## 技术支持

如遇到问题，请查看：
- [BUILD.md](BUILD.md) - 详细构建指南
- [README.md](README.md) - 完整用户手册
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - 项目总结

## 版本信息

当前版本：1.0.0
构建日期：2025-08-16
支持平台：Windows、Linux、macOS
