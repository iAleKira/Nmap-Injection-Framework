#!/bin/bash
cd TextFiles 
echo -e "Filtering services by match-topservices.txt.\n"
./extractTopServices.sh
echo -e "Done. top-services-probes.txt obtained.\n\nNow filtering top-services-probes.txt by all payloads.\n"
java -jar ./All_payloads_filter.jar
echo -e "\nDone. Splitting injectable-services-probes.txt into multiple files.\n"
cd injectableProbes
./splitInjection.sh
cd ..
cd ..
echo -e "Done.Launching Injector_server.\n"
java -jar ./Injector_server.jar
