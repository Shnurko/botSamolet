# Используем минимальный образ с JDK 17
FROM openjdk:17-jdk-slim-buster

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY build/libs/botSamolet-0.0.1-SNAPSHOT.jar app.jar

# Указываем команду для запуска приложения
CMD ["java", "-jar", "app.jar"]