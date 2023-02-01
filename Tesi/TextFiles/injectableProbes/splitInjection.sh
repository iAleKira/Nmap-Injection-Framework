#!/bin/bash
rm toInject/*
for i in "ftp" "ssh" "telnet" "smtp" "http" "dns" "kerberos" "nntp" "netbios" "imap" "snmp" "ldap" "directconnect" "microsoft-d" "rtsp" "nntp" "ipp" "pop3"
do
  grep "match $i" injectable-service-probes.txt > toInject/"$i".txt
  filesize=$(stat -c %s $"toInject/"$i".txt")
  if [[ $filesize == "0" ]] ; then 
  rm "toInject/"$i".txt"
  else 
  lines=$(grep "" -c $"toInject/"$i".txt")
  echo "Service $i has $lines injectable directives."
  fi
done
