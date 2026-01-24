# my-market-app

## Сборка

1. Соберите проект через команду 
```bash
./mvnw clean package
```
Итоговый артефакт (executable fat jar) будет в папке "target/".

Текущая версия проекта указана в pom.xml:
```xml
<version>0.0.1-SNAPSHOT</version>
```
Итоговый артефакт в этом случае назывался бы так:
```
   my-market-app-0.0.1-SNAPSHOT.jar
```

## Запуск
### Без docker
2. Запустите executable fat jar командой:
```bash
   java -jar my-market-app-<version>.jar
```
### С docker без compose или buildpacks
2. Соберем образ с определенным именем и запустим его в detached режиме с проброшенным портом 8080:
```
   docker build -t mma-app .
   docker run -d --name mma-app-container -p 8080:8080 mma-app   
```

### С помощью Spring Boot + Buildpacks
2. Соберем образ через:
```
   ./mvnw spring-boot:build-image
```
и запустим контейнер на основании собранного образа с проброшенным портом 8080:
```
   docker run -p 8080:8080 mma-app:0.0.1-SNAPSHOT
```
### С помощью Docker Compose
2. Соберем образы и запустим контейнеры в detached режиме (порт 8080 проброшенным):
```
   docker compose up -d
```

## Использование
3. Приложение стартует по умолчанию на порте 8080.