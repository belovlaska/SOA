@echo off
setlocal enabledelayedexpansion

REM === ПАРАМЕТРЫ ===
set TOMCAT_HOME=C:\Users\VVKon\OneDrive\Desktop\Itmo_study\apache-tomcat-9.0.112
set KEYSTORE_DIR=%TOMCAT_HOME%\conf
set KEYSTORE_FILE=tomcat.keystore
set KEYSTORE_PASS=password
set KEY_ALIAS=tomcat
set KEY_VALIDITY=3650
set DNAME=CN=localhost,OU=2,O=SOA,L=SPb,ST=SPb,C=RU

REM === ШАГИ ===
echo [*] Step 1: Генирую keystore для Tomcat...
keytool -genkeypair ^
  -alias %KEY_ALIAS% ^
  -keyalg RSA -keysize 2048 ^
  -validity %KEY_VALIDITY% ^
  -keystore "%KEYSTORE_DIR%\%KEYSTORE_FILE%" ^
  -storepass %KEYSTORE_PASS% ^
  -keypass %KEYSTORE_PASS% ^
  -dname "%DNAME%"

if !errorlevel! neq 0 (
  echo [!] Ошибка при генерации keystore
  exit /b 1
)

echo [+] Keystore создан: %KEYSTORE_DIR%\%KEYSTORE_FILE%

echo.
echo [*] Step 2: Экспортирую сертификат Tomcat в .crt...
keytool -export ^
  -alias %KEY_ALIAS% ^
  -keystore "%KEYSTORE_DIR%\%KEYSTORE_FILE%" ^
  -storepass %KEYSTORE_PASS% ^
  -file "%KEYSTORE_DIR%\%KEY_ALIAS%.crt"

if !errorlevel! neq 0 (
  echo [!] Ошибка при экспорте сертификата
  exit /b 1
)

echo [+] Сертификат экспортирован: %KEYSTORE_DIR%\%KEY_ALIAS%.crt

echo.
echo [*] Step 3: Проверяю содержимое keystore...
keytool -list -v -keystore "%KEYSTORE_DIR%\%KEYSTORE_FILE%" -storepass %KEYSTORE_PASS%

pause
