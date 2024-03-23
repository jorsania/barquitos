FROM nginxinc/nginx-unprivileged
LABEL org.opencontainers.image.source=https://github.com/jorsania/barquitos
RUN rm /etc/nginx/conf.d/default.conf
RUN chmod ugo+rw /etc/nginx/conf.d
COPY api.loc.template /etc/nginx/templates/.
COPY default.conf.template /etc/nginx/templates/.
# Eliminar phpmyadmin en versión despliegue definitiva
COPY phpmyadmin.loc.template /etc/nginx/templates/.
