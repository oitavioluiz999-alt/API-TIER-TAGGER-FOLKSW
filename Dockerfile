# ============================================================
# TierTagger API - Dockerfile multi-stage (Java 21)
# ============================================================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copia apenas o pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código e compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Imagem final (runtime)
# ============================================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usuário não-root (boa prática de segurança)
RUN groupadd -r tiertagger && useradd -r -g tiertagger -u 1001 tiertagger

COPY --from=build /workspace/target/tiertagger-api.jar app.jar

USER tiertagger
EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

