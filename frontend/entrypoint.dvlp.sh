#!/bin/bash
cp src/index.html.template src/index.html &&
sed -i "s|\${BASE_ADDR}|${BASE_ADDR}|" src/index.html &&
cp src/assets/config.js.template src/assets/config.js &&
sed -i "s|\${HOST_ADDR}|${HOST_ADDR}|" src/assets/config.js &&
npm install  &&
ng serve --public-host ${DOMAIN} --serve-path ${BASE_ADDR} --host 0.0.0.0 --port 80