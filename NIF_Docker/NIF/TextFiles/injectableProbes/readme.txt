Please do not remove this file from the directory as it contains a brief of the steps to take.

- toInject: directory that contains injectable probes from different services.
- injectable-service-probes.txt: the file containing all the probes that can be injected by a payload.
- splitInjections.sh: shell script that splits probes from injectable-service-probes.txt into multiple files according to their service.

WHAT TO DO:
1. Use script "splitInjection.sh" to filter "injectable-service-probes.txt" into multiple service files in the "toInject" directory.

NOTE: Some of the services are not split by default, because by default they are supposed to be blank outputs.
If you filter by a certain payload (eg. <script>alert(1)</script>) and your service output is not not supported by
splitInjection.sh just uncomment (remove #) from the line. Or add your own line to the file with the name of the output
file. If you wish to get Nif to listen on that port just edit the Database.java file, adding to the method "fillMap()"
the port number and the output file name. 
Example: You wanna add pop3. You will add: "map.put(110, "pop3")" to fillMap() 
method in Database.java class. Then you must uncomment "grep match pop3..." in the splitInjection.sh file in Tesi/TextFiles/injectableProbes.
