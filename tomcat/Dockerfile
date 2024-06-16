FROM maven AS barquitos_maven_build
COPY partida partida
WORKDIR /partida
RUN mvn package

FROM tomcat:9.0
LABEL org.opencontainers.image.source https://github.com/jorsania/barquitos
COPY --from=barquitos_maven_build /partida/target/partida.war /usr/local/tomcat/webapps
