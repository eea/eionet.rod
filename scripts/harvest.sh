#!/bin/sh

# !!!!!!!!!!!!!!!!!! ADJUST THESE !!!!!!!!!!!!!!!!!!
rod=@WEBAPP.HOME@/WEB-INF
cd $rod/classes
java=/usr/bin/java

# !!!!!!!!!!!!!!!!! CHECK, if mysql JAR is correct !!!!!!!!!!!!!!
cp=@MYSQL.JAR@

cp=$cp:@LDAP.JAR@
cp=$cp:@JNDI.JAR@
cp=$cp:@PROVIDERUTIL.JAR@
cp=$cp:@EIONETDIR.JAR@:@UITSECURITY.JAR@
cp=$cp:@XMLRPC.JAR@
cp=$cp:@UITCLIENT.JAR@:@LOG4J.JAR@:$CLASSPATH

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
