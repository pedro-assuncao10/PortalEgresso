# Etapa 1: Use uma imagem base com Maven e OpenJDK
FROM maven:3.8.4-openjdk-21-slim AS builder

# Defina o diretório de trabalho
WORKDIR /app

# Copie os arquivos do projeto para o contêiner
COPY . /app

# Execute o Maven para gerar o JAR sem rodar os testes
RUN mvn clean package -DskipTests

# Etapa 2: Use uma imagem leve do OpenJDK para rodar a aplicação
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o JAR gerado da etapa anterior
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
