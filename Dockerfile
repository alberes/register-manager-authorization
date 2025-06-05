FROM maven:3.8.5-openjdk-17 as build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=build ./build/target/*.jar ./register-manager-authorization.jar

expose 8081

ENV DATASOURCE_URL=''
ENV DATASOURCE_USER=''
ENV DATASOURCE_PASSWORD=''
ENV SHOW_SQL=true
ENV DDL_AUTO=update
ENV FORMAT_SQL=true
ENV VIA_CEP_URL='https://viacep.com.br/ws/'
ENV EXPIRATION_TIME=30
ENV APP_PORT=8081
ENV MONITORING_PORT=9090
ENV MONITORING_LIST='*'
ENV LOG_NAME=register-manager.log
ENV LOG_LEVEL=warn

ENTRYPOINT java -jar register-manager-authorization.jar