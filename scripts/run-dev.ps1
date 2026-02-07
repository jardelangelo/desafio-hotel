$ErrorActionPreference = "Stop"

# raiz do projeto = pasta pai de /scripts
$rootPath = Split-Path -Parent $PSScriptRoot
Set-Location $rootPath

function Get-ComposeCmd {
  try { docker compose version *> $null; return "docker compose" } catch { return "docker-compose" }
}
$compose = Get-ComposeCmd

function Compose-Up {
  if ($compose -eq "docker compose") { docker compose up -d } else { docker-compose up -d }
}
function Compose-Down {
  if ($compose -eq "docker compose") { docker compose down --remove-orphans } else { docker-compose down --remove-orphans }
}
#function Compose-Stop {
#  if ($compose -eq "docker compose") { docker compose stop } else { docker-compose stop }
#}

function Kill-Tree([int]$procId) {
  if (-not $procId) { return }
  try { taskkill /PID $procId /T /F | Out-Null } catch { }
}

function Kill-ByPort([int]$port) {
  try {
    $lines = (netstat -ano | findstr "LISTENING" | findstr ":$port ")
    foreach ($l in $lines) {
      $parts = ($l -split "\s+") | Where-Object { $_ -ne "" }
      $procId = [int]$parts[-1]
      if ($procId -gt 0) { Kill-Tree $procId }
    }
  } catch { }
}

$backendProc = $null
$frontendProc = $null
$global:stop = $false

# Ctrl+C -> não encerra o powershell "na marra"; a gente só sinaliza stop e cai no finally
$handler = [ConsoleCancelEventHandler]{
  param($sender, $e)
  $global:stop = $true
  $e.Cancel = $true
}
[Console]::add_CancelKeyPress($handler)

try {
  Write-Host "== Desafio Hotel Dev ==" -ForegroundColor Green

  Write-Host "`n[1/3] Subindo Docker (Postgres/Flyway)..." -ForegroundColor Cyan
  Compose-Up

  # Backend
  $backendDir = Join-Path $rootPath "backend"
  if (!(Test-Path $backendDir)) { throw "Pasta backend nao encontrada: $backendDir" }

  $mvnw = Join-Path $backendDir "mvnw.cmd"
  $backendRun = if (Test-Path $mvnw) { "mvnw.cmd spring-boot:run" } else { "mvn spring-boot:run" }

  Write-Host "[2/3] Iniciando Backend..." -ForegroundColor Cyan
  $backendProc = Start-Process -FilePath "cmd.exe" -ArgumentList "/c", $backendRun `
    -WorkingDirectory $backendDir -NoNewWindow -PassThru

  # Frontend
  $frontendDir = Join-Path $rootPath "frontend"
  if (!(Test-Path $frontendDir)) { throw "Pasta frontend nao encontrada: $frontendDir" }

  Write-Host "[3/3] Iniciando Frontend..." -ForegroundColor Cyan
  $frontendProc = Start-Process -FilePath "cmd.exe" -ArgumentList "/c", "npm start" `
    -WorkingDirectory $frontendDir -NoNewWindow -PassThru

  Write-Host "`n=====================================" -ForegroundColor Green
  Write-Host "Front: http://localhost:4200"
  Write-Host "Back : http://localhost:8080"
  Write-Host "Para parar tudo: Ctrl+C NESTA janela (ou pressione Q)" -ForegroundColor Yellow
  Write-Host "=====================================" -ForegroundColor Green
	
	try {
	  while (-not $global:stop) {

		  # fallback: Q para parar (se Ctrl+C ficar “capturado”)
		  if ([Console]::KeyAvailable) {
			$k = [Console]::ReadKey($true)
			if ($k.Key -eq 'Q') { $global:stop = $true }
		  }

		  # Checagem REAL (OS) — se backend ou frontend morrer, encerra tudo
		  $backendAlive  = $backendProc  -and (Get-Process -Id $backendProc.Id  -ErrorAction SilentlyContinue)
		  $frontendAlive = $frontendProc -and (Get-Process -Id $frontendProc.Id -ErrorAction SilentlyContinue)

		  if (-not $backendAlive -or -not $frontendAlive) {
			$global:stop = $true
		  }

		  Start-Sleep -Milliseconds 250
		}
	} catch [System.Management.Automation.Host.ControlCException] {
	  $global:stop = $true
	}
} finally {
  Write-Host "`nEncerrando Front/Back..." -ForegroundColor Yellow

  try { if ($frontendProc) { Kill-Tree $frontendProc.Id } } catch { }
  try { if ($backendProc)  { Kill-Tree $backendProc.Id } } catch { }

  # fallback por porta (se algo ficou órfão)
  try { Kill-ByPort 4200 } catch { }
  try { Kill-ByPort 8080 } catch { }

  Write-Host "Encerrando Docker ($compose down)..." -ForegroundColor Yellow
  try { Compose-Down } catch { }
  #try { Compose-Stop } catch { }


  try { [Console]::remove_CancelKeyPress($handler) } catch { }
  Write-Host "Tudo parado." -ForegroundColor Green
}

