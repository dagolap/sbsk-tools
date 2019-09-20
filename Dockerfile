FROM clojure:openjdk-8-lein-2.9.1 AS builder

ADD . /data
WORKDIR /data
RUN lein uberjar



FROM openjdk:8u222-jre

COPY --from=builder /data/target/uberjar/sbsk-tools.jar /sbsk-tools.jar
EXPOSE 3000
CMD ["java", "-jar", "/sbsk-tools.jar"]
