@echo off
setlocal enabledelayedexpansion

REM === ПАРАМЕТРЫ ===
set KEYSTORE_DIR=C:\Users\VVKon\OneDrive\Desktop\Itmo_study\wildfly-38.0.0.Final\standalone\configuration
set KEYSTORE_FILE=wildfly.keystore
set KEYSTORE_PASS=password
set KEY_ALIAS=wildfly
set KEY_VALIDITY=3650
set DNAME=CN=localhost,OU=2,O=SOA,L=SPb,ST=SPb,C=RU

REM === ШАГИ ===
echo [*] Step 1: Генерирую keystore для WildFly...
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
echo [*] Step 2: Экспортирую сертификат WildFly в .crt...
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
echo [*] Step 3: Создаю truststore и импортирую сертификат WildFly...
keytool -import ^
  -alias %KEY_ALIAS% ^
  -file "%KEYSTORE_DIR%\%KEY_ALIAS%.crt" ^
  -keystore "%KEYSTORE_DIR%\truststore.jks" ^
  -storepass %KEYSTORE_PASS% ^
  -noprompt

if !errorlevel! neq 0 (
  echo [!] Ошибка при создании truststore
  exit /b 1
)

echo [+] Truststore создан: %KEYSTORE_DIR%\truststore.jks

echo.
echo [*] Step 4: Проверяю содержимое keystore...
keytool -list -v -keystore "%KEYSTORE_DIR%\%KEYSTORE_FILE%" -storepass %KEYSTORE_PASS%

pause
