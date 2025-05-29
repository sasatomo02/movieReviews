# ベースイメージとしてOpenJDK 17を使用
FROM openjdk:17-jdk-slim

# 作業ディレクトリを設定
WORKDIR /app

# Gradle Wrapperと設定ファイルをコピー
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# gradlewに実行権限を付与
RUN chmod +x ./gradlew

# 依存関係をダウンロードし、キャッシュを構築
RUN ./gradlew dependencies --write-locks || true

# .gradle ディレクトリをコピー (依存関係ダウンロード後)
COPY .gradle .gradle

# アプリケーションのソースコードをコピー
COPY . .

# Spring BootアプリケーションのJARファイルをビルド
RUN ./gradlew bootJar

# ビルドされたJARファイルを実行
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]