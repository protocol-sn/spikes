FROM node:24 AS build
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm ci
RUN npm install -g @angular/cli
COPY . .
RUN npm run build --prod
FROM nginx:alpine
COPY --from=build /usr/src/app/dist/angular-ui/browser /usr/share/nginx/html
EXPOSE 80
