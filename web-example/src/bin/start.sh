#!/bin/sh
echo -------------------------------------------
echo start server
echo -------------------------------------------
# 设置项目代码路径
export CODE_HOME="/export/App/web-example-web-package"
#日志路径
export LOG_PATH="/export/Logs/web.example.jd.local"
mkdir -p $LOG_PATH
# 设置依赖路径
export CLASSPATH="$CODE_HOME/WEB-INF/classes:$CODE_HOME/WEB-INF/lib/*"
# java可执行文件位置
export _EXECJAVA="$JAVA_HOME/bin/java"
# JVM启动参数
export JAVA_OPTS="-server -Xms128m -Xmx256m -Xss256k-XX:MaxDirectMemorySize=128m"
# 服务端端口、上下文、项目根配置
export SERVER_INFO="-Dserver.port=8090 -Dserver.contextPath= -Dserver.docBase=$CODE_HOME"
# 启动类
export MAIN_CLASS=org.lql.startup.TomcatBootstrap

nohup $_EXECJAVA $JAVA_OPTS -classpath $CLASSPATH $SERVER_INFO $MAIN_CLASS &
tail -f $LOG_PATH/stdout.log