@echo off

rem #################################################
rem Habitat4J Version and Location

set HABITAT4J_VERSION=0.9.29
set HABITAT4J_JAR=..\..\..\lib\habitat4j-%HABITAT4J_VERSION%.jar

rem #################################################
rem JAVA and Third-Party JAR Location specifics

rem set JAVA_HOME=c:\jdk1.3.1_12
set JAVA_HOME=C:\j2sdk1.4.2_04

set XERCES_JAR=C:\eclipse\jboss\3.2.3\lib\xercesImpl.jar
set XMLAPIS_JAR=C:\eclipse\jboss\3.2.3\lib\xml-apis.jar

rem #################################################
rem YOU SHOULD NOT NEED TO EDIT BELOW THIS POINT

set CLASSPATH=%CLASSPATH%;%HABITAT4J_JAR%
set CLASSPATH=%CLASSPATH%;%XERCES_JAR%
set CLASSPATH=%CLASSPATH%;%XMLAPIS_JAR%

set JAVA_BIN=%JAVA_HOME%\bin\java
set VALIDATOR_CLASSPATH=org.productivity.java.habitat4j.util.ServerIdentityValidator
set SI_FILE=%1

rem ##################################################
rem Execution follows
%JAVA_BIN% %VALIDATOR_CLASSPATH% %SI_FILE%
