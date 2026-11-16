@echo off
setlocal
set "VERSION=8.12.1"
where gradle >nul 2>nul
if %ERRORLEVEL%==0 (
  gradle %*
  exit /b %ERRORLEVEL%
)
if not defined GRADLE_USER_HOME set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
set "ROOT=%GRADLE_USER_HOME%\wrapper\dists\tommyclient\%VERSION%"
set "BIN=%ROOT%\gradle-%VERSION%\bin\gradle.bat"
if not exist "%BIN%" (
  if not exist "%ROOT%" mkdir "%ROOT%"
  echo Downloading Gradle %VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%VERSION%-bin.zip' -OutFile '%ROOT%\gradle-%VERSION%-bin.zip'; Expand-Archive -Force '%ROOT%\gradle-%VERSION%-bin.zip' '%ROOT%'"
  if errorlevel 1 exit /b %ERRORLEVEL%
)
call "%BIN%" %*
exit /b %ERRORLEVEL%
