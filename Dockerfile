FROM eclipse-temurin AS build
VOLUME /tmp

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
#RUN ["./mvnw",  "clean", "package" ]
#RUN ["./mvnw",  "install" ]
RUN ["./mvnw", "clean", "install", "-Dmaven.test.skip=true"]



FROM eclipse-temurin

#ARG JAR_FILE
ARG JAR_FILE=/usr/app/target/*.jar
#COPY ${JAR_FILE} app.jar
COPY --from=build $JAR_FILE app.jar

#ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar /app.jar ${0} ${@}"]
