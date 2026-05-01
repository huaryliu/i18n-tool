#!/bin/bash
export LANG=zh_CN.UTF-8
export LC_ALL=zh_CN.UTF-8

# abc I18n Resource Extractor Linux/Mac Startup Script

# Optional values: prod|dev.
# prod:Production Environment; dev:Development Environment
SERVER_MODE="prod"

# Optional values: true|false. true:logs are printed on the console; false:Logs are not printed at the console.
if [ "$SERVER_MODE" = "prod" ]; then
  ENABLE_CONSOLE_LOGGING=false
else
  ENABLE_CONSOLE_LOGGING=true
fi

# JAVA_HOME configuration
JAVA_HOME="/usr/lib/jvm/java-17-openjdk"

# Fallback to common JRE locations if JAVA_HOME doesn't exist
if [ ! -d "$JAVA_HOME" ]; then
  JAVA_HOME="/usr/java/default"
fi

if [ ! -d "$JAVA_HOME" ]; then
  echo "Warning: JAVA_HOME not found at $JAVA_HOME"
  echo "Please set JAVA_HOME to JDK 17+ directory"
  exit 1
fi

# JVM Debugging options
JVM_DEBUG_OPTS="-Dspring.profiles.active=prod"

if [ "$SERVER_MODE" = "dev" ]; then
  JVM_DEBUG_OPTS="-Dspring.profiles.active=dev -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
fi

# Runtime and configuration paths
RTF_BOOT_HOME="$(dirname "$0")/runtime"
CONFIG_FILE="$(dirname "$0")/config/application.yaml"

# Find the bootstrap JAR file
RTF_BOOTSTRAP=$(find "$RTF_BOOT_HOME" -name "i18n-extractor*.jar" | head -1)

echo "====================================="
echo "abc I18N Resource Extractor"
echo "====================================="
echo ""
echo "JAVA_HOME: $JAVA_HOME"
echo "Tool Path: $(dirname "$0")"
echo "Config File: $CONFIG_FILE"
echo "Server Mode: $SERVER_MODE"
echo ""

if [ ! -f "$RTF_BOOTSTRAP" ]; then
    echo "Error: Bootstrap JAR not found in $RTF_BOOT_HOME"
    echo "Please build the project first"
    exit 1
fi

echo "Starting I18N Extractor..."
echo "Bootstrap JAR: $RTF_BOOTSTRAP"
echo ""
echo "Application will be available at: http://localhost:8080"
echo ""

"$JAVA_HOME/bin/java" -server $JVM_DEBUG_OPTS -Dspring.config.location="$CONFIG_FILE" -jar "$RTF_BOOTSTRAP"

echo "I18N Extractor stopped"
