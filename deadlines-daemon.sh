#!/bin/sh
LOCKFILE=/var/lock/rod-deadlines-daemon.lock
[ -f $LOCKFILE ] && exit 0
trap "rm -f $LOCKFILE" EXIT
touch $LOCKFILE

# place the right path in here !!!
java=/usr/lib/jvm/java/bin/java

# place the right path in here !!!
ad=/var/lib/tomcat4/webapps/webrod/public/WEB-INF

# place the right path in here !!!
cp=/usr/share/java/mysql-connector-java.jar

# place the right path in here !!!
cp=$cp:/usr/share/java/servlet.jar

cp=$cp:$ad/classes
cp=$cp:$ad/lib/tomcat-util.jar:$ad/lib/xmlrpc.jar:$ad/lib/xmlserver.jar:$CLASSPATH

$java -cp $cp eionet.rod.DeadlinesDaemon