# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set environment variables
ENV SSH_KEY_PATH=/root/.ssh/hetzner.key

# Create SSH directory
RUN mkdir -p /root/.ssh

# Copy your SSH private key into the container
COPY hetzner.key $SSH_KEY_PATH
RUN chmod 600 $SSH_KEY_PATH

# Disable host key checking (so SSH works without prompt)
RUN echo "StrictHostKeyChecking no" > /root/.ssh/config

# Copy your Spring Boot jar file (replace with actual jar name)
COPY target/finetech-api-0.0.1-SNAPSHOT.jar app.jar

# Create SSH tunnel before starting the app
CMD ssh -f -i $SSH_KEY_PATH -N -L 5432:127.0.0.1:5432 moka.admin@116.203.231.254 -p 665 && \
    java -jar app.jar
