#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=@WEBAPP.HOME@/WEB-INF
cd $rod/classes
java=/usr/bin/java

libpath=$rod/lib

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=@MYSQL.JAR@

cp=$cp:$libpath/rod.jar:$libpath/xmlserver.jar
cp=$cp:$libpath/uit-security.jar
cp=$cp:$libpath/eionet-dir.jar
cp=$cp:$libpath/log4j.jar:$CLASSPATH

$java -cp $cp eionet.rod.DeadlineCalc

