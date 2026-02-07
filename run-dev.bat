@echo off
setlocal

set "ROOT=%~dp0"
if "%ROOT:~-1%"=="\" set "ROOT=%ROOT:~0,-1%"
set "PS1=%ROOT%\scripts\run-dev.ps1"

if not exist "%PS1%" (
  echo [ERRO] Nao encontrei: %PS1%
  pause
  exit /b 1
)

start "Desafio Hotel Dev" powershell -NoLogo -NoProfile -ExecutionPolicy Bypass -File "%PS1%"
exit /b 0
