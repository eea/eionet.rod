#!/bin/sh


# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=@WEBAPP.HOME@/WEB-INF
cd $rod/classes
java=/usr/bin/java

libpath=$rod/lib

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=@MYSQL.JAR@

cp=$cp:$libpath/ldap.jar
cp=$cp:$libpath/jndi.jar
cp=$cp:$libpath/providerutil.jar
cp=$cp:$libpath/eionet-dir.jar:$libpath/uit-security.jar
cp=$cp:$libpath/rod.jar:$libpath/xmlrpc.jar
cp=$cp:$libpath/uit-client.jar:$libpath/log4j.jar:$CLASSPATH

if [ "$1" = "" ] ; then
        i="0"
else
        i=$1
fi;


if [ "$i" = "0" ] || [ "$i" = "1" ] || [ "$i" = "2" ] || [ "$i" = "3" ] ; then
	$java -cp $cp eionet.rod.countrysrv.Extractor $1        
else
  echo "Usage: eionet.rod.countrysrv.Extractor {0|1|2|3} "
	echo "0:all data, 1:deliveries, 2:roles, 3:parameters"
fi;
