FROM nginxinc/nginx-unprivileged:alpine
LABEL org.opencontainers.image.source=https://github.com/jorsania/barquitos
RUN rm /etc/nginx/conf.d/default.conf
RUN chmod ugo+rw /etc/nginx/conf.d
COPY php.loc.template /etc/nginx/templates/.
COPY tomcat.loc.template /etc/nginx/templates/.
COPY default.conf.template /etc/nginx/templates/.

# Eliminar phpmyadmin en versión despliegue definitiva
COPY --from=phpmyadmin/phpmyadmin:fpm /var/www/html /usr/share/nginx/html/phpmyadmin
COPY phpmyadmin.loc.template /etc/nginx/templates/.
