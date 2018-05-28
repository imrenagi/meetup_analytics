FROM java:8-jre
MAINTAINER Imre Nagi <imre.nagi2812@gmail.com>

ADD ./target/event-delivery.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/event-delivery.jar", ""]

EXPOSE 7000