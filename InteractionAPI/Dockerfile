# Build context is the repository root (see docker-compose.yml) so this stage
# can install FudoSharedKernel into the build container's local Maven repo
# before building InteractionAPI itself.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY FudoSharedKernel ./FudoSharedKernel
RUN mvn -q -B -f FudoSharedKernel/pom.xml install

COPY InteractionAPI ./InteractionAPI
RUN mvn -q -B -f InteractionAPI/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/InteractionAPI/target/interaction-api-1.0.0.jar app.jar
EXPOSE 5006
ENTRYPOINT ["java", "-jar", "app.jar"]
