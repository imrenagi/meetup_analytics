FROM java:8-jre
MAINTAINER Imre Nagi <imre.nagi2812@gmail.com>

EXPOSE 8080

ADD ./config.yml /app/
ADD ./target/meetup-api-1.0-SNAPSHOT.jar /app/

CMD ["java", "-Xmx200m", "-jar", "/app/meetup-api-1.0-SNAPSHOT.jar", "server", "/app/config.yml"]
