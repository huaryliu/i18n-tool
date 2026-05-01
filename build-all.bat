@echo off
echo =====================================
echo Building iGIX I18n Resource Extractor
echo =====================================
echo.

echo Step 1: Building Backend...
cd backend
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo Backend build failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Copying JAR to tool/runtime...
copy target\i18n-extractor-1.0.0.jar ..\tool\runtime\
if %errorlevel% neq 0 (
    echo Failed to copy JAR file!
    pause
    exit /b 1
)

cd ..

echo.
echo Step 3: Building Frontend...
cd frontend
call npm install
if %errorlevel% neq 0 (
    echo Frontend npm install failed!
    pause
    exit /b 1
)

call npm run build:prod
if %errorlevel% neq 0 (
    echo Frontend build failed!
    pause
    exit /b 1
)

cd ..

echo.
echo =====================================
echo Build completed successfully!
echo =====================================
echo.
echo Tool directory is ready at: tool\
echo Backend JAR: tool\runtime\i18n-extractor-1.0.0.jar
echo Frontend files: tool\web\
echo.
echo You can now run the tool using:
echo - tool\startup-win.cmd (Windows)
echo - tool\startup-linux.sh (Linux/Mac)
echo.
pause
