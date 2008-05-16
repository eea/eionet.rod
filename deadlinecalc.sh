#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=/var/lib/tomcat4/webapps/webrod/public/WEB-INF
cd $rod/classes
java=/usr/lib/jvm/java/bin/java

libpath=$rod/lib

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=/var/lib/tomcat4/common/lib/mysql-connector-java.jar

cp=$cp:$libpath/rod.jar:$libpath/xmlserver.jar
cp=$cp:$libpath/uit-security.jar
cp=$cp:$libpath/eionet-dir.jar
cp=$cp:$libpath/log4j.jar:$CLASSPATH

$java -cp $cp eionet.rod.DeadlineCalc

