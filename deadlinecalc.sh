#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=/prj/rod2/public/WEB-INF
cd $rod/classes
java=/usr/lib/jvm/java/bin/java

libpath=$rod/lib

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=/var/lib/tomcat4/common/lib/mysql_uncomp.jar

cp=$cp:$libpath/rod.jar:$libpath/xmlserver.jar
cp=$cp:$libpath/log4j.jar:$CLASSPATH

$java -cp $cp eionet.rod.DeadlineCalc

