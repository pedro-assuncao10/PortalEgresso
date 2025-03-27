# Etapa 1: Build da aplicação com Maven e OpenJDK 21
FROM maven:3.9.6-eclipse-temurin AS builder

WORKDIR /app

# Copia apenas os arquivos necessários para dependências primeiro (cache otimizado)
COPY pom.xml ./
RUN mvn dependency:go-offline

# Agora copia o código e gera o JAR
COPY . . 
RUN mvn clean package -DskipTests

# Etapa 2: Runtime otimizado com JDK 21 slim
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia apenas o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
