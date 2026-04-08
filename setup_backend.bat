@echo off
echo ========================================
echo ERP Backend Setup Script
echo Spring Boot + Hibernate + MySQL
echo ========================================

echo.
echo Step 1: Installing Maven...
echo.

REM Try to download Maven
powershell -Command "& { try { Invoke-WebRequest -Uri 'https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip' -OutFile 'maven.zip'; Write-Host 'Maven downloaded successfully' } catch { Write-Host 'Auto-download failed, please download manually from https://maven.apache.org/download.cgi' } }"

REM Extract Maven if downloaded
if exist maven.zip (
    echo Extracting Maven...
    powershell -Command "& { try { Expand-Archive -Path 'maven.zip' -DestinationPath 'C:\maven' -Force; Write-Host 'Maven extracted successfully' } catch { Write-Host 'Extraction failed, please extract manually to C:\maven' } }"
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo Maven not found in PATH.
    echo Please add C:\maven\bin to your system PATH environment variable.
    echo Then restart this script or open a new command prompt.
    echo.
    pause
    exit /b 1
)

echo.
echo Step 2: Checking MySQL...
echo.

REM Check if MySQL is running
net start | findstr mysql >nul 2>&1
if %errorlevel% neq 0 (
    echo MySQL service not running.
    echo Please start MySQL service or install MySQL if not installed.
    echo MySQL installer available in Downloads folder.
    echo.
    pause
    exit /b 1
)

echo MySQL is running.

echo.
echo Step 3: Starting Spring Boot Backend...
echo.

cd "C:\Users\SATHWIK NALLURI\Downloads\Telegram Desktop\ERPMANAGEMENT\ERP-Backend"
mvn spring-boot:run

echo.
echo Backend setup complete!
echo API available at: http://localhost:8080
echo.