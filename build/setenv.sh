install -m 400 <(echo -n $DB_PASSWORD)   .db_password
install -m 400 <(echo -n $SMTP_PASSWORD) .mail_password

export HOST_ADDR=$(echo $HOST_ADDR | sed 's|[^/]$|&/|')
export BASE_ADDR=$(echo $HOST_ADDR | cut -d '/' -f 4)/
export    DOMAIN=$(echo $HOST_ADDR | cut -d '/' -f 3 | cut -d ':' -f 1)
export     USRID=$(id -u)
export     GRPID=$(id -g)
export      PORT=$PORT
[ $BASE_ADDR = "/" ] && export BASE_ADDR=""
