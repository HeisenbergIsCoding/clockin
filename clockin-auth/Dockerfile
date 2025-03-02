FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 複製整個專案
COPY . .

# 先構建父項目
RUN mvn clean install -pl clockin-common -am -DskipTests

# 構建 auth 模塊
RUN mvn clean package -pl clockin-auth -am -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/clockin-auth/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
