@echo off
echo =====================================
echo abc Internationalization Resource
echo Extraction Tool
echo =====================================
echo.

echo Starting backend server...
cd /d "%~dp0..\backend"
start "I18n Backend" cmd /k "mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 15 /nobreak >nul

echo.
echo Starting frontend server...
cd /d "%~dp0..\frontend"
start "I18n Frontend" cmd /k "npm run dev"

echo.
echo Waiting for frontend to start...
timeout /t 5 /nobreak >nul

echo.
echo =====================================
echo Both servers should be running now.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:5173
echo =====================================
echo.

start http://localhost:5173

echo Press any key to close this window...
pause >nul
