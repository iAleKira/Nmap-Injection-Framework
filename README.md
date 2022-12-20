# Injector_server
Injector_server is a tool developed to search for vulnerabilities in network scanning tools.
Developed in Java it sets up a server listening on certain ports, waiting for incoming connection by a scanner.
Once it receives a request from the client, the answer replies with a proper nmap output formula and injects the web client reading the output.

How does it work:
![one](https://user-images.githubusercontent.com/89973113/208695942-f899937f-13a0-4695-87f6-5fad4b411e25.png)
In green we can see the scan is launched on the machine that is using the server, on port 22, but you don't need to specifit a port 
if you want to scan all ports of the server at the same time. 
Supported ports are 20-22-25-80.
In blue you can see the reply from the server.
In red you can see the reply to the client, correctly injected.

The package comes with a filter as well, which takes a file named top-services-probest.txt (which contains all the probes from nmap-service-probes that belong to top services we manually filtered for, see:match-topservices.txt) and looks for match directives that can be injected with a certain payload. We use a general payload by default, which contains most of the injection characters known, and we print the output to a file called injectable-service-probes.txt. 
But if you want you can specify a different payload to filter for.
Here we can see an example usage: 
![two](https://user-images.githubusercontent.com/89973113/208699089-ebb2e6c7-7661-41af-b4c3-919e434188e2.png)
In red we can see the input file, in blue the output file, and in green the payload to filter for.
We obtained a 37 lines file, and we can see some results. This means there are 37 injectable probes that can be injected by the payload "<script>alert(1)</script>".
