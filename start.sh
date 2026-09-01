#!/bin/bash
# 一键启动 vue-admin-template-server

export JAVA_HOME="${JAVA_HOME:-$HOME/.local/java/amazon-corretto-8.jdk/Contents/Home}"
export PATH="$JAVA_HOME/bin:$HOME/.local/apache-maven-3.9.6/bin:$PATH"

cd "$(dirname "$0")" || exit 1

if [ ! -f "target/vue-admin-template-server-1.0.0-SNAPSHOT.jar" ]; then
  echo "正在编译..."
  mvn clean package -DskipTests -q || exit 1
fi

echo "启动后端 http://127.0.0.1:8090"
java -jar target/vue-admin-template-server-1.0.0-SNAPSHOT.jar
