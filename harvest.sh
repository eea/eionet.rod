#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=/prj/webrod
java=/prj/javaserv/jdk1.2.2/bin/java

libpath=$rod/WEB-INF/lib

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=/prj/javaserv/mm.mysql.jdbc-1.2c/mysql_uncomp.jar

cp=$cp:$libpath/rod.jar:$libpath/xmlserver.jar:$libpath/xmlrpc.jar
cp=$cp:$libpath/uit-client.jar:$libpath/log4j.jar:$CLASSPATH

if [ "$1" = "" ] ; then
        i="0"
else
        i=$1
fi;


if [  "$i" = "0" ] || [ "$i" = "1"  ] || [ "$i" = "2"] ; then
	$java -cp $cp eionet.rod.countrysrv.Extractor $1        
else
        echo "Usage: eionet.rod.countrysrv.Extractor {0|1|2} "
	echo "0:all data, 1:deliveries, 2:roles"
fi;





