#!/bin/bash

echo "Setting up Java ERP Backend..."
echo

echo "Step 1: Installing Maven (if not already installed)"
echo "On Ubuntu/Debian: sudo apt-get install maven"
echo "On macOS: brew install maven"
echo "On other systems: Download from https://maven.apache.org/download.cgi"
echo

echo "Step 2: Setting up MySQL Database"
echo "Please ensure MySQL is running and create the database:"
echo "mysql -u root -p"
echo "CREATE DATABASE java_erp_db;"
echo

echo "Step 3: Update database credentials in src/main/resources/application.properties"
echo "Current settings:"
echo "spring.datasource.username=root"
echo "spring.datasource.password=2007"
echo

echo "Step 4: Build and run the backend"
echo "After installing Maven, run these commands:"
echo
echo "cd JavaERP-Backend"
echo "mvn clean install"
echo "mvn spring-boot:run"
echo

read -p "Press Enter to continue..."