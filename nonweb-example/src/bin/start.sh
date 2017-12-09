#!/bin/sh
echo -------------------------------------------
echo start server
echo -------------------------------------------
# 设置项目代码路径
export CODE_HOME="/usr/getd/nonweb-example-1.0.0-SNAPSHOT-package"
#日志路径
export LOG_PATH="D:\work\project\myself\java-bootstrap\nonweb-example\src\log"
mkdir -p $LOG_PATH
# 设置依赖路径
export CLASSPATH="$CODE_HOME/classes:$CODE_HOME/lib/*"
# java可执行文件位置
export _EXECJAVA="$JAVA_HOME/bin/java"
# JVM启动参数
export JAVA_OPTS="-server -Xms128m -Xmx256m -Xss256k -XX:MaxDirectMemorySize=128m"
# 启动类
export MAIN_CLASS=org.lql.startup.Bootstrap

$_EXECJAVA $JAVA_OPTS -classpath $CLASSPATH $MAIN_CLASS &
tail -f $LOG_PATH/stdout.log