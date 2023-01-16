# Stage 1: Build
FROM node:18-alpine3.17 as build
ENV BASE_PATH=/web/src/angular/
WORKDIR /replace
COPY $BASE_PATH/package.json .
COPY $BASE_PATH/yarn.lock .
COPY $BASE_PATH/.yarnrc.yml .
RUN corepack enable
RUN corepack prepare yarn@stable --activate
RUN yarn install
COPY $BASE_PATH/. .
RUN yarn build

# Stage 2: Run
FROM nginx:stable-alpine
ENV BASE_PATH=/web/src/angular/
COPY $BASE_PATH/nginx.conf /usr/local/etc/nginx/nginx.conf
COPY --from=build /replace/dist/replace-web /usr/share/nginx/html