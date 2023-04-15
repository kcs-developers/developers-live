FROM openjdk:17-jdk
ADD /build/libs/developers-live-0.0.1-SNAPSHOT.jar springbootApp.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "springbootApp.jar"]