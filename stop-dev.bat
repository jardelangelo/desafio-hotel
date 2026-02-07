@echo off
echo Matando processos nas portas 4200 e 8080...
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr ":4200 "') do taskkill /PID %%p /T /F >nul 2>nul
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr ":8080 "') do taskkill /PID %%p /T /F >nul 2>nul

echo Derrubando docker compose...
docker compose down --remove-orphans
docker-compose down --remove-orphans

echo OK.
pause