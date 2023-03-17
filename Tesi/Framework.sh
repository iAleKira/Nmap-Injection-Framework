#!/bin/bash
if [[ $1 == "--help" ]] ; then
	echo "Usage: ./Framework.sh payload_file payload_to_filter_for";
	exit
fi
echo "Hello $USER. Welcome to "
figlet nmap injection framework
cd TextFiles
echo -e "\e[1;33mStep 1: \e[1;36mfiltering services by match-topservices.txt.\n\e[1;32m./extractTopServices.sh\e[1;36m now executing.\e[1;0m\n"
./extractTopServices.sh
echo -e "\e[1;33mDone. \e[1;36mtop-services-probes.txt obtained.\n\e\e[1;33mStep 2: \e[1;36mnow filtering top-services-probes.txt by all payloads.\n\e[1;32m./Filter.jar \e[1;36mnow executing.\e[1;0m\n"
java -jar ./Filter.jar $2
echo -e "\n\e[1;33mDone. \e[1;36minjectable-service-probes.txt obtained.\n\e[1;33mStep 3: \e[1;36mnow splitting injectable-services-probes.txt into multiple files.\n\e[1;32msplitInjection.sh \e[1;36mnow executing.\e[1;0m"
cd injectableProbes
./splitInjection.sh
cd ..
cd ..
echo -e "\e[1;33mDone.\e[1;36m Probes split successfully.\n\e[1;33mStep 4: \e[1;36mserver is now ready to be executed.\e[1;32m\n./Nif.jar \e[1;36mnow executing.\e[1;0m\n"
java -jar ./Nif.jar $1
