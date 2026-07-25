@echo off
REM Sequential startup of Spring Boot services with Maven

REM Start Config Server
echo Starting Config Server...
cd config-server
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start Discovery Service
echo Starting Discovery Service...
cd ..\discovery-service
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start API Gateway
echo Starting API Gateway...
cd ..\api-gateway
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start Auth Service
echo Starting Auth Service...
cd ..\auth-service
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start Restaurant Service
echo Starting Restaurant Service...
cd ..\restaurant-service
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start Order Service
echo Starting Order Service...
cd ..\order-service
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start Payment Service
echo Starting Payment Service...
cd ..\payment-service
start cmd /c "mvn spring-boot:run"
timeout /t 15

REM Start Delivery Service
echo Starting Delivery Service...
cd ..\delivery-service
start cmd /c "mvn spring-boot:run"
timeout /t 15

echo ✅ All services started sequentially with Maven on Windows.
pause
