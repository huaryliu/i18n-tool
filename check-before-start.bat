@echo off
chcp 65001 >nul
echo =====================================
echo 启动前检查
echo =====================================
echo.

echo [1/5] 检查Java安装...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java未安装或未正确配置
    pause
    exit /b 1
)
echo ✅ Java已安装
java -version 2>&1 | findstr /i "version"
echo.

echo [2/5] 检查JAVA_HOME设置...
set JAVA_HOME=D:\老电脑备份\software\OpenJDK17U-jdk_x64_windows_hotspot_17.0.4.1_1\jdk-17.0.4.1+1
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo ❌ JAVA_HOME路径不正确: %JAVA_HOME%
    echo 请检查路径或修改startup-win.cmd中的JAVA_HOME设置
    pause
    exit /b 1
)
echo ✅ JAVA_HOME配置正确
echo.

echo [3/5] 检查配置文件...
if not exist "tool\config\application.yaml" (
    echo ❌ 配置文件不存在: tool\config\application.yaml
    pause
    exit /b 1
)
echo ✅ 配置文件存在
echo.

echo [4/5] 检查JAR文件...
if not exist "tool\runtime\i18n-extractor*.jar" (
    echo ❌ JAR文件不存在: tool\runtime\i18n-extractor*.jar
    echo.
    echo 请先运行以下命令构建项目:
    echo   build-all.bat
    echo.
    pause
    exit /b 1
)
echo ✅ JAR文件存在
echo.

echo [5/5] 检查端口占用...
netstat -ano | findstr ":8080" >nul 2>&1
if %errorlevel% equ 0 (
    echo ⚠️  端口8080已被占用
    echo 可能会导致启动失败，请检查是否有其他程序在使用该端口
) else (
    echo ✅ 端口8080可用
)
echo.

echo =====================================
echo ✅ 所有检查通过！
echo =====================================
echo.
echo 现在可以启动应用:
echo   cd tool
echo   startup-win.cmd
echo.
echo 或者直接运行:
echo   tool\startup-win.cmd
echo.
pause
