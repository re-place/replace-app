# Stage 1: Build
FROM node:18-alpine3.17 as build
WORKDIR /replace
COPY /web/src/angular/package.json ./package.json
COPY /web/src/angular/yarn.lock ./yarn.lock
COPY /web/src/angular/.yarnrc.yml ./.yarnrc.yml
RUN corepack enable
RUN corepack prepare yarn@stable --activate
RUN yarn install
COPY . .
RUN yarn build

# Stage 2: Run
FROM nginx:stable-alpine
COPY nginx.conf /usr/local/etc/nginx/nginx.conf
COPY --from=build /replace/dist/replace-web /usr/share/nginx/html
