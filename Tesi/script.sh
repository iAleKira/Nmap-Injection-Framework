#!/bin/bash
cd TextFiles 
echo -e "Filtering services by match-topservices.txt.\n./extractTopServices.sh now executing.\n"
./extractTopServices.sh
echo -e "Done. top-services-probes.txt obtained.\n\nNow filtering top-services-probes.txt by all payloads.\n./All_payloads_filter.jar now executing.\n"
java -jar ./All_payloads_filter.jar
echo -e "\nDone. Splitting injectable-services-probes.txt into multiple files.\nsplitInjection.sh now executing.\n"
cd injectableProbes
./splitInjection.sh
cd ..
cd ..
echo -e "Done. Injector_server now executing.\n"
java -jar ./Injector_server.jar
