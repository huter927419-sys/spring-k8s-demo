# 使用多阶段构建
# 阶段1: 构建应用
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 阶段2: 运行应用
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/spring-k8s-demo-*.jar app.jar

# 创建非root用户
RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

