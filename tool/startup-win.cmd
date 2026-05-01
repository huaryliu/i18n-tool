@echo off
chcp 65001 >nul
title iGIX I18n Resource Extractor

:: Optional values: prod|dev.
:: prod:Production Environment; dev:Development Environment
set SERVER_MODE=prod

:: Optional values: true|false. true:logs are printed on the console; false:Logs are not printed at the console.
if {%SERVER_MODE%}=={prod} (
  set ENABLE_CONSOLE_LOGGING=false
) else (
  set ENABLE_CONSOLE_LOGGING=true
)

:: JAVA_HOME configuration
set JAVA_HOME=D:\backup\software\OpenJDK17U-jdk_x64_windows_hotspot_17.0.4.1_1\jdk-17.0.4.1+1

:: Fallback to embedded JRE if JAVA_HOME doesn't exist
if not exist "%JAVA_HOME%" (
  echo Warning: JAVA_HOME not found at %JAVA_HOME%
  echo Please set JAVA_HOME to JDK 17+ directory
  pause
  exit
)

:: JVM Debugging options
set JVM_DEBUG_OPTS=-Dspring.profiles.active=prod

if {%SERVER_MODE%}=={dev} (
  set JVM_DEBUG_OPTS=-Dspring.profiles.active=dev -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
)

:: Runtime and configuration paths
set RTF_BOOT_HOME=%~dp0runtime
set CONFIG_FILE=%~dp0\config\application.yaml

:: Find the bootstrap JAR file
@setlocal enabledelayedexpansion
for /r "%RTF_BOOT_HOME%\" %%k in (i18n-extractor*.jar) do (
        @set RTF_BOOTSTRAP="%%k"
   )

echo =====================================
echo iGIX I18N Resource Extractor
echo =====================================
echo.
echo JAVA_HOME: %JAVA_HOME%
echo Tool Path: %~dp0
echo Config File: %CONFIG_FILE%
echo Server Mode: %SERVER_MODE%
echo.

if not exist "%RTF_BOOTSTRAP%" (
    echo Error: Bootstrap JAR not found in %RTF_BOOT_HOME%
    echo Please build the project first
    pause
    exit
)

echo Starting I18N Extractor...
echo Bootstrap JAR: %RTF_BOOTSTRAP%
echo.
echo Application will be available at: http://localhost:8080
echo.

"%JAVA_HOME%\bin\java" -server %JVM_DEBUG_OPTS% -Dspring.config.location=%CONFIG_FILE% -jar %RTF_BOOTSTRAP%

pause
