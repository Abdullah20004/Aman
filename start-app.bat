@echo off
setlocal

REM Set tunnel port and SSH info
set TUNNEL_PORT=5433
set SSH_USER=moka.admin
set SSH_HOST=116.203.231.254
set SSH_PORT=665
set SSH_KEY="C:\Users\Mahmoud\.ssh\hetzner.key"

REM Start SSH tunnel and save the PID
echo Starting SSH tunnel...
start "SSH Tunnel" /B ssh -i %SSH_KEY% -N -L %TUNNEL_PORT%:localhost:5432 %SSH_USER%@%SSH_HOST% -p %SSH_PORT%
set "TUNNEL_PID=%!"

REM Wait a moment to ensure the tunnel is ready
timeout /t 3 > nul

REM Run Spring Boot application
echo Starting Spring Boot app...
call mvnw spring-boot:run

REM After app exits, kill the SSH tunnel
echo Stopping SSH tunnel...
taskkill /FI "WINDOWTITLE eq SSH Tunnel" > nul 2>&1

endlocal
