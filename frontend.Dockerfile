# Frontend Dockerfile
FROM node:20

WORKDIR /app

# Copy package.json first and install deps
COPY frontend/package*.json ./
RUN npm install

# Source code will be mounted via docker-compose for hot reload
CMD ["npm", "run", "dev"]
