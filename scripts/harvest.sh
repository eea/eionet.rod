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
cp=$cp:@EIONETDIR.JAR@:@ACL.JAR@
cp=$cp:@XMLRPC.JAR@:@COMMONSCODEC.JAR@
cp=$cp:@ALIBABA.JAR@:@SESAME.JAR@
cp=$cp:@COMMONSHTTPCLIENT.JAR@
cp=$cp:@COMMONSLOGGING.JAR@
cp=$cp:@COMMONSLANG.JAR@
cp=$cp:@SLF4JLOG.JAR@:@SLF4JAPI.JAR@
cp=$cp:@UITCLIENT.JAR@:@LOG4J.JAR@:$CLASSPATH


#allowed commands
#element position is equal to a numeric code that is position in the array
#################################################
# 0 = all, 1 = deliveries etc
#################################################
cmd=("all" "deliveries" "roles" "parameters")


#finds element position in the array
index_of()    {
    local i=1 S=$1; shift
    while [ $S != $1 ]
    do    ((i++)); shift
        [ -z "$1" ] && { i=0; break; }
    done
    echo $i
}


arrayLen=`echo ${#cmd[@]}`

#max allowed param value
arrayLen=$((`expr $arrayLen`-1))

#default - 0:all if not specified
if [ "$1" = "" ] ; then
        i="0"
else
        i=$1
fi;

#check if command is numeric
if [ -z "${i//[0-9]/}" ]; then
    isNumericOk="true"
else
   isNumericOk="false"
fi;


if [ $isNumericOk = "true" ] ; then
   numValue=`expr $i`

  if [ $numValue -gt $arrayLen ] ; then
     i=-1
  fi;
else
  i=`index_of $i ${cmd[@]}`
  i=$((`expr $i`-1))
fi;

if [ ! $i = -1 ]; then
  $java -cp $cp eionet.rod.countrysrv.Extractor $i
else
  echo "Usage: eionet.rod.countrysrv.Extractor {all|deliveries|roles|parameters} "
fi;
