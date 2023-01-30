FROM khipu/openjdk17-alpine
 
WORKDIR /usr/local/NIF/ 
COPY Nif.jar /usr/local/NIF
RUN mkdir Payloads
COPY Payloads /usr/local/NIF/Payloads
RUN mkdir TextFiles
COPY TextFiles /usr/local/NIF/TextFiles
RUN chmod +x Nif.jar
EXPOSE 21
EXPOSE 22
EXPOSE 25
EXPOSE 80
