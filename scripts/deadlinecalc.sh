#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=@WEBAPP.HOME@/WEB-INF
cd $rod/classes
java=/usr/bin/java

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=@MYSQL.JAR@

cp=$cp:@ACL.JAR@
cp=$cp:@EIONETDIR.JAR@
cp=$cp:@LOG4J.JAR@:$CLASSPATH

$java -cp $cp eionet.rod.DeadlineCalc

