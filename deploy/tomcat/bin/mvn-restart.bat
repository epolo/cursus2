setlocal
call env.bat

call %CATALINA_HOME%\bin\shutdown.bat

call C:\Java\apache-maven-3.3.9\bin\mvn.cmd -f ../../../ clean package

if ERRORLEVEL 1 goto ERR

call %CATALINA_HOME%\bin\startup.bat
goto FIN

:ERR
echo --- ERRORS on build

:FIN
