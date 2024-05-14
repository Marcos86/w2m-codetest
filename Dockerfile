FROM openjdk:21-jdk
MAINTAINER marcosdelamo86
COPY spacecraft-infrastructure/target/spacecraft-infrastructure-0.0.1-SNAPSHOT.jar spacecraft-infrastructure/target/spacecraft-infrastructure-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/spacecraft-infrastructure/target/spacecraft-infrastructure-0.0.1-SNAPSHOT.jar"]