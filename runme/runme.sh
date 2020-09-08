#!/bin/bash

if [ -z "${BASH_VERSINFO}" ] || [ -z "${BASH_VERSINFO[0]}" ] || [ ${BASH_VERSINFO[0]} -lt 4 ]
then
    echo "This script requires Bash version >= 4"
    exit 1
fi

mapfile -t myArray < servers.txt

# printf '%s\n' "${myArray[@]}"

NUMSERVERS=${#myArray[@]}


if [ $NUMSERVERS -lt 16 ]; then
    echo "Not enough servers"
    exit 1
fi

ssh ${myArray[12]} "nohup ./MBVerifier/runme/runGlobal.sh"

sleep 10

for i in 4 5 6 7
do
    ssh ${myArray[$i]} "nohup ./MBVerifier/runme/runFirewall.sh $(($i-3)) > /dev/null 2> /dev/null &"
done

for i in 0 1 2 3
do
    ssh ${myArray[$i]} "cd traffic; sudo nohup ./start_background.sh $i 8 traffic_5e9_ </dev/null >/dev/null 2>&1 &" 
done

for i in 8 9 10 11
do
    ssh ${myArray[$i]} "cd traffic; sudo nohup ./start_background.sh $(($i-4)) 8 traffic_5e9_ </dev/null >/dev/null 2>&1 &" 
done

sleep 5m

for i in 4 5 6 7
do
    ssh ${myArray[$i]} "sudo iptables -A FORWARD -s 10.10.4.0/24 -d 10.10.1.0/24 -m conntrack --ctstate NEW -j ACCEPT"
done

sleep 5m

for i in 0 1 2 3
do
    ssh ${myArray[$i]} "sudo killall socat; sudo killall start_background; sudo killall python"
done

for i in 8 9 10 11
do
    ssh ${myArray[$i]} "sudo killall socat; sudo killall start_background; sudo killall python"
done

for i in 4 5 6 7
do
    ssh ${myArray[$i]} "sudo killall runFirewall; sudo killall start_background && sudo killall python"
done

scp ${myArray[12]}:./MBVerifier/out/*.txt .


