FROM ghcr.io/graalvm/native-image-community:22 AS build

WORKDIR /usr/local/maven
ADD https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz /usr/local/maven
RUN tar -xvf apache-maven-3.9.9-bin.tar.gz
ENV M2_HOME=/usr/local/maven/apache-maven-3.9.9
ENV M2=$M2_HOME/bin
ENV PATH=$M2:$PATH
WORKDIR /usr/local/trivy-rest-api
COPY pom.xml pom.xml
RUN mvn dependency:resolve dependency:resolve-plugins
COPY src src
RUN mvn -Pnative clean native:compile

FROM aquasec/trivy:latest
WORKDIR /usr/local/trivry-rest-api
COPY --from=build /usr/local/trivy-rest-api/target/trivy-rest-api  trivy-rest-api
# C Library compatabliliy tool as GraalVM compiles with libc and alpine with musl
RUN apk add gcompat
RUN adduser \
    --disabled-password \
    --gecos "" \
    --shell "/sbin/nologin" \
    --uid "10001" \
    "appuser"
USER appuser:appuser
ENTRYPOINT ["./trivy-rest-api"]
