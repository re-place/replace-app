### STAGE 1: Build ###
FROM node:18-alpine3.17 as build
WORKDIR /replace
COPY package.json .
COPY yarn.lock .
COPY .yarnrc.yml .
RUN corepack enable
RUN corepack prepare yarn@stable --activate
RUN yarn install
COPY . .
RUN yarn build

### STAGE 2: Run ###
FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /replace/dist/replace-web /usr/share/nginx/html
