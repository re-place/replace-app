# Stage 1: Build
FROM node:18-alpine3.17 as build
ENV BASE_PATH=src/angular
WORKDIR /replace
COPY $BASE_PATH/* /replace
RUN corepack enable
RUN corepack prepare yarn@stable --activate
RUN yarn install

RUN yarn build

# Stage 2: Run
FROM nginx:stable-alpine
ENV BASE_PATH=src/angular
COPY nginx.conf /usr/local/etc/nginx/nginx.conf
COPY --from=build /replace/dist/replace-web /usr/share/nginx/html
