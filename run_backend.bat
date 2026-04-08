@echo off
echo ========================================
echo Starting ERP Backend
echo Spring Boot + Hibernate
echo ========================================

echo Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Maven not found!
    echo Please install Maven and add to PATH:
    echo 1. Download from: https://maven.apache.org/download.cgi
    echo 2. Extract to C:\maven
    echo 3. Add C:\maven\bin to PATH
    echo 4. Restart command prompt and run this script again
    echo.
    pause
    exit /b 1
)

echo.
echo Checking MySQL...
net start | findstr mysql >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo WARNING: MySQL service not running.
    echo Backend may fail to start without database.
    echo Please start MySQL service.
    echo.
)

echo.
echo Starting Spring Boot Backend...
echo Location: C:\Users\SATHWIK NALLURI\Downloads\Telegram Desktop\ERPMANAGEMENT\ERP-Backend
echo.

cd "C:\Users\SATHWIK NALLURI\Downloads\Telegram Desktop\ERPMANAGEMENT\ERP-Backend"
mvn spring-boot:run

echo.
echo Backend stopped.
pause