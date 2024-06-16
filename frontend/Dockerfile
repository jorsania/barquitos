FROM node:14 AS barquitos_frontend_dev
RUN npm install -g @angular/cli@^14.0.0
COPY entrypoint.dvlp.sh /usr/local/sbin/entrypoint.sh

FROM barquitos_frontend_dev as barquitos_frontend_build
COPY angular angular
WORKDIR /angular
RUN npm install
RUN cp src/index.html.template src/index.html
RUN ng build
RUN touch dist/assets/config.js 
RUN chmod 666 dist/assets/config.js
RUN cp dist/index.html dist/index.html.template
RUN chmod 666 dist/index.html

FROM nginxinc/nginx-unprivileged:alpine AS barquitos_frontend_prod
LABEL org.opencontainers.image.source https://github.com/jorsania/barquitos
COPY --from=barquitos_frontend_build /angular/dist /usr/share/nginx/html
COPY default.conf.template /etc/nginx/templates/default.conf.template
COPY envsubst.sh /docker-entrypoint.d/21-envsubst-custom.sh
RUN rm /etc/nginx/conf.d/default.conf
RUN chmod ugo+rw /etc/nginx/conf.d
