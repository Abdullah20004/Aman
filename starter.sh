#!/bin/bash

# Set tunnel port and SSH info
TUNNEL_PORT=5433
SSH_USER=moka.admin
SSH_HOST=116.203.231.254
SSH_PORT=665
SSH_KEY=~/.ssh/hetzner.key  # Adjust if saved elsewhere

# Start SSH tunnel in background
echo "Starting SSH tunnel..."
ssh -i "$SSH_KEY" -N -L "$TUNNEL_PORT":localhost:5432 "$SSH_USER"@"$SSH_HOST" -p "$SSH_PORT" &
TUNNEL_PID=$!

# Wait a moment to ensure the tunnel is ready
sleep 3

# Start Spring Boot app
echo "Starting Spring Boot application..."
./mvnw spring-boot:run

# After app exits, stop the tunnel
echo "Stopping SSH tunnel..."
kill $TUNNEL_PID
