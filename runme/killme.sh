#!/bin/bash

if [ -z "${BASH_VERSINFO}" ] || [ -z "${BASH_VERSINFO[0]}" ] || [ ${BASH_VERSINFO[0]} -lt 4 ]
then
    echo "This script requires Bash version >= 4"
    exit 1
fi

mapfile -t myArray < ../Setup/servers.txt

# printf '%s\n' "${myArray[@]}"

NUMSERVERS=${#myArray[@]}


if [ $NUMSERVERS -lt 16 ]; then
    echo "Not enough servers"
    exit 1
fi

for i in 0 1 2 3
do
    ssh ${myArray[$i]} "sudo killall socat; sudo killall start_background; sudo killall python; sudo killall python3"
done

for i in 8 9 10 11
do
    ssh ${myArray[$i]} "sudo killall socat; sudo killall start_background; sudo killall python; sudo killall python3"
done

for i in 4 5 6 7
do
    ssh ${myArray[$i]} "sudo killall runFirewall; sudo killall start_background && sudo killall python; sudo killall python3 ; cd /tmp ; rm localVerifier.pid"
done

for i in 4 5 6 7
do
    ssh ${myArray[$i]} "sudo iptables -D FORWARD -s 10.10.4.0/24 -d 10.10.1.0/24 -m conntrack --ctstate NEW -j ACCEPT"
done


ssh ${myArray[12]} "cd; cd kafka_2.12-2.5.0/ ; ./bin/kafka-server-stop.sh ; ./bin/zookeeper-server-stop.sh; cd /tmp/ ; sudo rm -r kafka-logs/ ; sudo rm -r zookeeper ; cd ; cd flink-1.9.3/ ; ./bin/stop-cluster.sh ; cd log ; rm *"

sleep 10

scp ${myArray[12]}:./aaragog/out/*.txt .
