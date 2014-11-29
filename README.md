Reporting Obligation Database
=============================

Prerequisites
-------------
ROD runs on Java platform, and has been tested and run on Tomcat Java Servlet Container. The ROD source code is built with Maven.

Please download all of these software and install them according to the instructions found at their websites:
Java, Tomcat, Maven and GIT client.

The necessary versions are as follows:
* Java 1.5 or higher
* Maven 3.0.5 or higher
* Tomcat 5.5 or higher
* GIT 1.8.4 or higher
* MySql 5.1.71 or higher


Unit testing
------------

The unit test mechanism will install its own embedded  database and create the tables when you execute them. Note that the MySQL database will keep running afterwards. You can run individual tests with: -Dtest=DatasetImportHandlerTest
```sh
$ mvn -Denv=unittest test
```
To investigate the database afterwards you can connect to it with the mysql command line tool.

```sh
mysql -S /tmp/rod-mxj/data/mysql.sock -u USER -pPASSWORD rodtestdb
```

Installation
-------------
Copy the unittest.properties to local.properties and modify the properties.

```sh
$ mvn -Dmaven.test.skip=true install
$ cp target/rod.war /var/lib/tomcat6/webapps/ROOT.war
```

