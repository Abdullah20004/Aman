#!/bin/bash
# save as start-tunnel.sh
TUNNEL_PORT=5433
SSH_USER=moka.admin
SSH_HOST=116.203.231.254
SSH_PORT=665
SSH_KEY="/c/Users/Mahmoud/.ssh/hetzner.key"

echo "Starting SSH tunnel..."
ssh -i $SSH_KEY -N -L $TUNNEL_PORT:localhost:5432 $SSH_USER@$SSH_HOST -p $SSH_PORT &
TUNNEL_PID=$!
echo "Tunnel PID: $TUNNEL_PID"

# Keep script running
wait $TUNNEL_PID