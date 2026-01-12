@echo off
setlocal enabledelayedexpansion

REM === ПАРАМЕТРЫ ===
set WILDFLY_CONF=C:\Users\VVKon\OneDrive\Desktop\Itmo_study\wildfly-38.0.0.Final\standalone\configuration
set TOMCAT_CONF=C:\Users\VVKon\OneDrive\Desktop\Itmo_study\apache-tomcat-9.0.112\conf
set KEYSTORE_PASS=password

echo.
echo ============================================================
echo  ШАГИ ДЛЯ ВЗАИМНОЙ КОММУНИКАЦИИ WildFly ^<-^> Tomcat
echo ============================================================
echo.

REM === ШАГИ 1-2: Они уже сделаны выше (генерация + export сертификатов) ===

echo [*] Step 1: Импортирую сертификат Tomcat в truststore WildFly...
keytool -import ^
  -alias tomcat ^
  -file "%TOMCAT_CONF%\tomcat.crt" ^
  -keystore "%WILDFLY_CONF%\truststore.jks" ^
  -storepass %KEYSTORE_PASS% ^
  -noprompt

if !errorlevel! neq 0 (
  echo [!] Ошибка при импорте сертификата Tomcat в truststore WildFly
  exit /b 1
)

echo [+] Сертификат Tomcat импортирован в truststore WildFly

echo.
echo [*] Step 2: Создаю truststore Tomcat и импортирую сертификат WildFly...
keytool -import ^
  -alias wildfly ^
  -file "%WILDFLY_CONF%\wildfly.crt" ^
  -keystore "%TOMCAT_CONF%\truststore.jks" ^
  -storepass %KEYSTORE_PASS% ^
  -noprompt

if !errorlevel! neq 0 (
  echo [!] Ошибка при создании truststore Tomcat
  exit /b 1
)

echo [+] Truststore Tomcat создан и содержит сертификат WildFly

echo.
echo [*] Step 3: Проверяю содержимое truststore WildFly...
keytool -list -v -keystore "%WILDFLY_CONF%\truststore.jks" -storepass %KEYSTORE_PASS%

echo.
echo [*] Step 4: Проверяю содержимое truststore Tomcat...
keytool -list -v -keystore "%TOMCAT_CONF%\truststore.jks" -storepass %KEYSTORE_PASS%

echo.
echo [+] ВСЕ ГОТОВО! Оба сервера имеют друг друга в truststore.

pause
