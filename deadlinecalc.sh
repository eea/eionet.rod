#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=/prj/webrod
java=/prj/javaserv/jdk1.2.2/bin/java

libpath=$rod/WEB-INF/lib

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=/prj/javaserv/mm.mysql.jdbc-1.2c/mysql_uncomp.jar


cp=$cp:$libpath/rod.jar:$libpath/xmlserver.jar
cp=$cp:$libpath/log4j.jar:$CLASSPATH


$java -cp $cp eionet.rod.DeadlineCalc

