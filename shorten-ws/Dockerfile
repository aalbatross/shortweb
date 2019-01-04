FROM williamyeh/java8

COPY target /app

COPY runtime.properties /app/.

WORKDIR /app

RUN mv runtime.properties application.properties

CMD java -jar shorten-ws-0.0.1-SNAPSHOT.jar