@echo off
rem /**
rem  */
echo.
echo [信息] 打包工程，生成war包文件。
echo.
pause
echo.

cd %~dp0
cd..

::set MAVEN_OPTS=%MAVEN_OPTS% -Xmx5120M -Xms5120M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled
call mvn clean package -Dmaven.test.skip=true -Dmaven.compile.fork=true

cd bin
pause