FROM khipu/openjdk17-alpine
 
COPY NIF /usr/local/NIF
WORKDIR /usr/local/NIF/
RUN chmod +x Nif.jar
EXPOSE 21
EXPOSE 22
EXPOSE 25
EXPOSE 80
CMD ["java", "-jar", "./Nif.jar"]
