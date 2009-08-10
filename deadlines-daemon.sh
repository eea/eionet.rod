#!/bin/sh
#
# Even though this file is called a daemon, it has to be run
# from crontab once a day
#
LOCKFILE=/var/lock/rod-deadlines-daemon.lock
[ -f $LOCKFILE ] && exit 0
trap "rm -f $LOCKFILE" EXIT
touch $LOCKFILE

# place the right path in here !!!
java=/usr/lib/jvm/java/bin/java

# place the right path in here !!!
rod=/var/lib/tomcat5/rod_apps/ROOT/WEB-INF
cd $rod/classes

libpath=$rod/lib

# place the right path in here !!!
cp=/var/lib/tomcat5/common/lib/mysql-connector-java.jar

# place the right path in here !!!
cp=$cp:/usr/share/java/servlet.jar

cp=$cp:$libpath/rod.jar
cp=$cp:$libpath/tomcat-util.jar:$libpath/xmlrpc.jar:$libpath/xmlserver.jar:$libpath/uit-security.jar:$CLASSPATH

$java -cp $cp eionet.rod.DeadlinesDaemon
