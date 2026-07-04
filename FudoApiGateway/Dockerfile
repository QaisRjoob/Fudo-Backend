# Build context is the repository root (see docker-compose.yml) so this stage
# can install FudoSharedKernel into the build container's local Maven repo
# before building FudoApiGateway itself.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY FudoSharedKernel ./FudoSharedKernel
RUN mvn -q -B -f FudoSharedKernel/pom.xml install

COPY FudoApiGateway ./FudoApiGateway
RUN mvn -q -B -f FudoApiGateway/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/FudoApiGateway/target/fudo-api-gateway-1.0.0.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "app.jar"]
