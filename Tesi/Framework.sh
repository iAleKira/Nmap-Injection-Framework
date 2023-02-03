#!/bin/bash
if [[ $1 == "--help" ]] ; then
	echo "Usage: ./Framework.sh payload_file payload_to_filter_for";
	exit
fi
echo "Hello $USER. Welcome to "
figlet nmap injection framework
cd TextFiles
echo -e "\e[1;36mFiltering services by match-topservices.txt.\n\e[1;32m./extractTopServices.sh\e[1;36m now executing.\e[1;0m\n"
./extractTopServices.sh
echo -e "\e[1;33mDone. \e[1;36mtop-services-probes.txt obtained.\nNow filtering top-services-probes.txt by all payloads.\n\e[1;32m./All_payloads_filter.jar \e[1;36mnow executing.\e[1;0m\n"
java -jar ./All_payloads_filter.jar $2
echo -e "\n\e[1;33mDone. \e[1;36mSplitting injectable-services-probes.txt into multiple files.\n\e[1;32msplitInjection.sh \e[1;36mnow executing.\e[1;0m"
cd injectableProbes
./splitInjection.sh
cd ..
cd ..
echo -e "\e[1;33mDone.\e[1;36m Server is ready to be executed.\e[1;32m\n./Nif \e[1;36mnow executing.\e[1;0m\n"
java -jar ./Nif.jar $1
