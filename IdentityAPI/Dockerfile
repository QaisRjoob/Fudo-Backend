# Build context is the repository root (see docker-compose.yml) so this stage
# can install FudoSharedKernel into the build container's local Maven repo
# before building IdentityAPI itself.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY FudoSharedKernel ./FudoSharedKernel
RUN mvn -q -B -f FudoSharedKernel/pom.xml install

COPY IdentityAPI ./IdentityAPI
RUN mvn -q -B -f IdentityAPI/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/IdentityAPI/target/identity-api-1.0.0.jar app.jar
EXPOSE 5001
ENTRYPOINT ["java", "-jar", "app.jar"]
