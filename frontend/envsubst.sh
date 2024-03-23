#!/bin/sh
envsubst < /usr/share/nginx/html/index.html.template > /usr/share/nginx/html/index.html
envsubst < /usr/share/nginx/html/assets/config.js.template > /usr/share/nginx/html/assets/config.js
