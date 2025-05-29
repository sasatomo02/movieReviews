# ベースイメージとしてOpenJDKを使用
FROM openjdk:21-jdk-slim

# 作業ディレクトリを設定
WORKDIR /app

# Gradleキャッシュレイヤー
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ ./gradle/
RUN chmod +x ./gradlew
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --write-locks || true
COPY . .
RUN ./gradlew bootJar

# Spring BootアプリケーションのJARファイルを指定
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 起動コマンド
ENTRYPOINT ["java","-jar","/app.jar"]

# ポート公開 (アプリケーションがリッスンするポートに合わせてください。デフォルトは8080が多いです)
EXPOSE 8080