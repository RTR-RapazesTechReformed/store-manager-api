FROM amazoncorretto:21-alpine-jdk AS build
WORKDIR /build
RUN apk --no-cache add maven

COPY pom.xml ./

COPY . .
RUN mvn -B -e clean install -DskipTests=true

FROM amazoncorretto:21-alpine-jdk AS runner
WORKDIR /app
COPY --from=build /build/target/store-manager-api-0.0.1-SNAPSHOT.jar ./store-manager-api.jar

EXPOSE 8080

CMD [ "java", "-jar", "store-manager-api.jar" ]
