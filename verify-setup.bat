@echo off
setlocal enabledelayedexpansion

REM === ПАРАМЕТРЫ ===
set WILDFLY_CONF=C:\Users\VVKon\OneDrive\Desktop\Itmo_study\wildfly-38.0.0.Final\standalone\configuration
set TOMCAT_CONF=C:\Users\VVKon\OneDrive\Desktop\Itmo_study\apache-tomcat-9.0.112\conf
set KEYSTORE_PASS=password

echo.
echo ============================================================
echo  ПРОВЕРКА КОНФИГУРАЦИИ
echo ============================================================
echo.

echo [*] Проверка WildFly keystore...
if exist "%WILDFLY_CONF%\wildfly.keystore" (
  echo [+] Файл найден: %WILDFLY_CONF%\wildfly.keystore
  keytool -list -v -keystore "%WILDFLY_CONF%\wildfly.keystore" -storepass %KEYSTORE_PASS%
) else (
  echo [-] Файл НЕ найден: %WILDFLY_CONF%\wildfly.keystore
)

echo.
echo [*] Проверка WildFly truststore...
if exist "%WILDFLY_CONF%\truststore.jks" (
  echo [+] Файл найден: %WILDFLY_CONF%\truststore.jks
  keytool -list -v -keystore "%WILDFLY_CONF%\truststore.jks" -storepass %KEYSTORE_PASS%
) else (
  echo [-] Файл НЕ найден: %WILDFLY_CONF%\truststore.jks
)

echo.
echo [*] Проверка Tomcat keystore...
if exist "%TOMCAT_CONF%\tomcat.keystore" (
  echo [+] Файл найден: %TOMCAT_CONF%\tomcat.keystore
  keytool -list -v -keystore "%TOMCAT_CONF%\tomcat.keystore" -storepass %KEYSTORE_PASS%
) else (
  echo [-] Файл НЕ найден: %TOMCAT_CONF%\tomcat.keystore
)

echo.
echo [*] Проверка Tomcat truststore...
if exist "%TOMCAT_CONF%\truststore.jks" (
  echo [+] Файл найден: %TOMCAT_CONF%\truststore.jks
  keytool -list -v -keystore "%TOMCAT_CONF%\truststore.jks" -storepass %KEYSTORE_PASS%
) else (
  echo [-] Файл НЕ найден: %TOMCAT_CONF%\truststore.jks
)

echo.
echo [*] Список сертификатов (.crt):
dir "%WILDFLY_CONF%\*.crt"
dir "%TOMCAT_CONF%\*.crt"

pause
