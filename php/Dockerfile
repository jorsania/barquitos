FROM php:fpm-alpine AS barquitos_php_dev
RUN docker-php-ext-install pdo pdo_mysql

COPY msmtprc /templates/msmtprc.template
RUN touch /etc/msmtprc
RUN chmod 777 /etc/msmtprc

RUN apk add msmtp envsubst
RUN ln -sf /usr/bin/msmtp /usr/bin/sendmail
RUN ln -sf /usr/bin/msmtp /usr/sbin/sendmail

COPY cmd.sh /usr/local/sbin
CMD cmd.sh

FROM barquitos_php_dev AS barquitos_php_prod
LABEL org.opencontainers.image.source https://github.com/jorsania/barquitos
COPY api /var/www/html/api
