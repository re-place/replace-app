# Stage 1: Build
FROM node:18-alpine3.17 as build
ENV BASE_PATH=src/angular
RUN ls -la /$BASE_PATH
WORKDIR /replace
COPY $BASE_PATH/package.json .
COPY $BASE_PATH/yarn.lock .
COPY $BASE_PATH/.yarnrc.yml .
COPY $BASE_PATH/.yarn/ .
RUN corepack enable
RUN corepack prepare yarn@stable --activate
RUN yarn install
COPY $BASE_PATH/. .
RUN yarn build

# Stage 2: Run
FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /replace/dist/replace-web /usr/share/nginx/html
