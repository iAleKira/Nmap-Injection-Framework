Please do not remove this file from the directory as it contains a brief of the steps to take.

- injectableProbes: where there are stored probes that can be injected.
- extractTopServices.sh: shell script that filters nmap-service-probes.txt based on match_topservices.txt content. 
- nmap-service-probes.txt: file containing all the probes from nmap.org.
- match-topservices.txt: file containing main services we are filtering for.
- top-services-probes.txt: file containing rules to filter the main services we plan to inject.
- All_payloads_filter.jar: the filter itself. 

WHAT TO DO: 

1. Use "extractTopServices" script in the same directory as "nmap-service-probes.txt" and "match-topservices.txt" files. The product of the file is "top-services-probes.txt".

2. Make sure "All_payloads_filter.jar" and "top-services-probes.txt" are both in this directory, for the filter to work with default settings. When running All_payloads_filter.jar you can specify a different input file to filter and output file to print output to. You can also specify a different filtering payload than the default one, so you can filter only for the desired payload string.
If "All_payloads_filter.jar" does not work: 
follow the instructions provided by the error report and include both files in this directory.
Usage: ./All_payloads_filter.jar inputFile outputFile payload
