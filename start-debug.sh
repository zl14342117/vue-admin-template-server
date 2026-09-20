#!/bin/bash
# 以调试模式启动后端，供 Cursor Attach 断点调试（端口 5005）

export JAVA_HOME="${JAVA_HOME:-$HOME/.local/java/amazon-corretto-8.jdk/Contents/Home}"
export PATH="$JAVA_HOME/bin:$HOME/.local/apache-maven-3.9.6/bin:$PATH"

cd "$(dirname "$0")" || exit 1

if lsof -ti :5005 >/dev/null 2>&1 || lsof -ti :8090 >/dev/null 2>&1; then
  echo "⚠️  端口 5005 或 8090 已被占用，正在释放..."
  lsof -ti :5005 -ti :8090 2>/dev/null | sort -u | xargs kill -9 2>/dev/null
  sleep 1
fi

echo "=========================================="
echo " 调试模式启动中..."
echo " 业务端口: 8090"
echo " 调试端口: 5005  (Attach 连这个)"
echo " 看到 Started VueAdminTemplateApplication 后"
echo " 再去 Run and Debug → ① Attach → F5"
echo "=========================================="

mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"
