FROM node:15.0.1-alpine3.10 as build-stage

WORKDIR /build

COPY package.json .
COPY package-lock.json .
COPY public/ ./public/
COPY src/ ./src/
COPY docker/*.conf ./nginx-configs/

RUN npm run build

FROM bunkerity/bunkerized-nginx:1.1.1

COPY --from=build-stage /build/nginx-configs/ /server-confs/
COPY --from=build-stage /build/build/ /www/
