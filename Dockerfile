# ベースイメージとしてOpenJDK 17を使用
FROM openjdk:17-jdk-slim

# 作業ディレクトリを設定
WORKDIR /app

# Gradle Wrapperと設定ファイルをコピー
COPY gradlew .
COPY .gradle .gradle
COPY build.gradle .
COPY settings.gradle .

# gradlewに実行権限を付与 (dependenciesタスク実行前に必要)
RUN chmod +x ./gradlew

# 依存関係をダウンロードし、キャッシュを構築
RUN ./gradlew dependencies --write-locks || true

# アプリケーションのソースコードをコピー
COPY . .

# gradlewに実行権限を付与 (bootJarタスク実行前に念のため)
RUN chmod +x ./gradlew

# Spring BootアプリケーションのJARファイルをビルド
RUN ./gradlew bootJar

# ビルドされたJARファイルを実行
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]