FROM node:24 AS build
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm ci
RUN npm install -g @angular/cli
COPY . .
#RUN npm run build --prod
RUN ng build
FROM nginx:alpine
COPY --from=build /usr/src/app/dist/secure-service-ui/browser /usr/share/nginx/html
EXPOSE 80

#FROM node:24 AS build
#WORKDIR /app/src
#COPY package*.json ./
#RUN npm ci
#RUN npm install -g @angular/cli
#COPY . ./
#RUN npm run build
#RUN ng build


#RUN addgroup -S exampleusergroup && adduser -S exampleuser -G exampleusergroup
#RUN addgroup --system exampleusergroup
#RUN adduser --system exampleuser --ingroup exampleusergroup
#USER exampleuser
#WORKDIR /usr/app
#COPY --from=build /app/src/dist/secure-service-ui/ ./
#CMD node server/server.mjs
#EXPOSE 4000
