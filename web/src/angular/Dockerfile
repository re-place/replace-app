# Stage 1: Build
FROM node:18-alpine3.17 as build
ENV BASE_PATH=/web/src/angular/
WORKDIR /replace
COPY $BASE_PATH/package.json /package.json
COPY $BASE_PATH/yarn.lock /yarn.lock
COPY $BASE_PATH/.yarnrc.yml /.yarnrc.yml
RUN corepack enable
RUN corepack prepare yarn@stable --activate
RUN yarn install
COPY . .
RUN yarn build

# Stage 2: Run
FROM nginx:stable-alpine
COPY nginx.conf /usr/local/etc/nginx/nginx.conf
COPY --from=build /replace/dist/replace-web /usr/share/nginx/html
