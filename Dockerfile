FROM eclipse-temurin
VOLUME /tmp
#RUN ["./mvnw",  "install" ]
#ARG JAR_FILE
#COPY ${JAR_FILE} app.jar
COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["sh", "-c", "java -jar /app.jar ${0} ${@}"]
