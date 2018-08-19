FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/productcatalogue-0.0.1-SNAPSHOT.jar app.jar
ADD product-catalogue.yml app-config.yml
RUN sh -c 'touch /app.jar'
EXPOSE 8020 8025
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar", "server", "app-config.yml"]
