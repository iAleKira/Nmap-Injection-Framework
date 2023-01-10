FROM khipu/openjdk17-alpine
 
COPY NIF /usr/local/NIF
WORKDIR /usr/local/NIF/
RUN chmod +x Injector_server.jar
EXPOSE 20
EXPOSE 22
EXPOSE 25
EXPOSE 80
CMD ["java", "-jar", "./Injector_server.jar"]
