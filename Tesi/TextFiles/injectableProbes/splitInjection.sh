#!/bin/bash
grep "match ftp" injectable-service-probes.txt > toInject/ftp.txt
grep "match ssh" injectable-service-probes.txt > toInject/ssh.txt
grep "match smtp" injectable-service-probes.txt > toInject/smtp.txt
grep "match http" injectable-service-probes.txt > toInject/http.txt
