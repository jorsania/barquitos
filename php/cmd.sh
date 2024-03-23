envsubst < /templates/msmtprc.template > /etc/msmtprc
envsubst < /templates/000-default.conf.template > /etc/apache2/sites-available/000-default.conf
apache2-foreground