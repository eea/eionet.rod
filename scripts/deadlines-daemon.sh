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
java=/usr/bin/java

# place the right path in here !!!
rod=@WEBAPP.HOME@/WEB-INF
cd $rod/classes

# place the right path in here !!!
cp=@MYSQL.JAR@

# place the right path in here !!!
cp=$cp:@SERVLETAPI.JAR@
cp=$cp:@COMMONSCODEC.JAR@
cp=$cp:@EIONETDIR.JAR@
cp=$cp:@TOMCATUTIL.JAR@:@XMLRPC.JAR@:@ACL.JAR@
cp=$cp:@LOG4J.JAR@:$CLASSPATH

$java -cp $cp eionet.rod.DeadlinesDaemon
