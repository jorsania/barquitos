#!/bin/sh
command -v envsub   && envsub /frontend/src/assets/config.js.template /frontend/src/assets/config.js
command -v envsubst && envsubst < /usr/share/nginx/html/assets/config.js.template > /usr/share/nginx/html/assets/config.js
command
