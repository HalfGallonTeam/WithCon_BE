FROM khipu/openjdk17-alpine
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-Dspring.config.location=file:/config/","-Dspring.profiles.active=prod","-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=Asia/Seoul", "-jar","/app.jar"]
