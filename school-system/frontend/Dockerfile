# Stage 1: Build the React application
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./

# This is working correctly now and installing react-scripts
RUN npm ci

# Copy the entire project source, including the 'public' folder
COPY . .

# Now that 'react-scripts' is installed and '/app/public/index.html' exists,
# this command will work.
RUN npm run build


# Stage 2: Serve the application
FROM node:20-alpine
WORKDIR /app
RUN npm install -g serve
# Copy the built files from the 'build' stage
COPY --from=build /app/build ./build
EXPOSE 3000
CMD ["serve", "-s", "build"]