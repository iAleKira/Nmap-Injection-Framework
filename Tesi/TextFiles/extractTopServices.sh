#!/bin/bash
grep -F -f match-topservices.txt nmap-service-probes.txt > top-services-probes.txt
