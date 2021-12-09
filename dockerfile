FROM openjdk:8-jdk-alpine
EXPOSE 8086
ADD /target/Timesheet-main-1.0.war Timesheet-main-1.0.jar
ENTRYPOINT ["java","-jar","Timesheet-main-1.0.war"]