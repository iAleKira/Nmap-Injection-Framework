# Nmap Injection Framework
This project is based upon RevOK prototype developed by Andrea Valenza, Gabriele Costa and Alessandro Armando. You can find the paper online at this link: https://arxiv.org/pdf/2006.09769.pdf

We developed a paper together with prof. Gabriele Costa and PhD Student Silvia De Francisci about this framework. You can find more info at this link: https://www.apps.unirc.it:37137/ITASEC2023/paper06.pdf

This framework is designed to exploit vulnerabilities of the well known scanning tool Nmap.
Developed in Bash and Java, it consists of 2 executables:
1. Nif.jar (Injector_server.jar)
2. Filter.jar (All_payloads_filter.jar)

Nif.jar sets up a server listening on certain ports, waiting for incoming connections by a scanner.
Once it receives a request from the client, the server delivers the answer with a proper nmap output formula and injects the web client reading the output.

# How it works:
![one](https://user-images.githubusercontent.com/89973113/208695942-f899937f-13a0-4695-87f6-5fad4b411e25.png)
In green we can see the scan is launched on the machine that is using the server, on port 22, but you don't need to specify a port 
if you want to scan all ports on the server at the same time. 
Supported ports at the moment are 20-22-25-80.
In blue you can see the reply from the server.
In red you can see the reply to the client, correctly injected.

Example scan all ports:
![three](https://user-images.githubusercontent.com/89973113/208711680-3f6bad71-f797-4352-912e-727196377a97.png)

Filter.jar takes a file named top-services-probes.txt (which contains all the probes from nmap-service-probes that belong to top services we manually filtered for, see: match-topservices.txt and extractTopServices.sh) and looks for match directives that can be injected with a certain payload. 
We use a general payload by default, which contains most of the injection characters known, and we print the output to a file called injectable-service-probes.txt. 
But if you want you can specify a different payload to filter for as the input of the executable.
Here we can see an example usage: 
![two](https://user-images.githubusercontent.com/89973113/208699089-ebb2e6c7-7661-41af-b4c3-919e434188e2.png)
In red we can see the input file, in blue the output file, and in green the payload to filter for.
We obtained a 37 lines file, and we can see some results. This means there are 37 injectable probes that can be injected by the payload "<script>alert(1)</script>".

# Update 25/12/2022

You can now input a payload file to Nif.jar. Server will extract a random payload to deliver from input file.
![servinput](https://user-images.githubusercontent.com/89973113/212331965-290f684c-fa8d-41b8-81c1-b267af5f5a8f.png)
In green you can see the input file, in blue the payload that got selected to be delivered, in red the delivery.

# Update 10/01/2023

You can now filter the default file (top-services-probes.txt) by a payload of choice.
![filtro2](https://user-images.githubusercontent.com/89973113/225991196-5a390267-a7a8-4862-82b7-42c687d81626.png)

# Update 03/02/2023 + 20/02/2023

You can now launch full program (both Filter and Nif at the same time) through Framework.sh.
![shellscript2](https://user-images.githubusercontent.com/89973113/225991324-bad50d57-2176-4671-930a-b98ab4c4feb4.png)

You can specify up to 2 parameters to the shell script: the first being the file where to get the payloads from, the second being the payload you want to filter for.
Need help? Run: ./Framework.sh --help

Nif.jar can now adapt the ports to listen to according to the database file obtained by the filter. 

# BeEF

NIF can work with BeEF by delivering a payload that gets the scanner's browser hooked to the scanned target's BeEF console. As you can see from the image, we replicate the case in an internal environment by using 2 Kali machines in the same network, for educational purpose.

Step 1: The author starts the scan over the IP address and saves the results in an HTML file.

![beef1](https://user-images.githubusercontent.com/89973113/215110126-183451d5-3877-4540-83bc-9b9ece576b5e.png)

Step 2: The author opens the scan file to check results, and its browser gets hooked.

![beef2](https://user-images.githubusercontent.com/89973113/215110165-bba9b7b4-452d-45b9-8535-d18914d50100.png)

Step 3: Beef can now launch commands on the browser: let's prove it.

![beef3](https://user-images.githubusercontent.com/89973113/215110192-9c417778-73c0-4b46-9779-3b16f02ddbfa.png)

# Docker

You can pull the latest version of the docker image of the tool here : https://hub.docker.com/r/alekira/nmap_injection_framework

# Having issues with jar files on Linux? 

Try running: java -jar ./filename.jar or run Framework.sh directly for default settings usage.

# Libraries used: 

AssertJ : credits to: https://github.com/assertj.

regexp-gen : credits to: https://github.com/Cornutum/regexp-gen
