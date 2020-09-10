#!/bin/bash

if [[ $# -ne 1 ]]; then
    echo "Usage: ./runFirewall.sh <node>"
    exit 1
fi

sudo conntrack -F

echo $1

cd $HOME/aaragog/firewall

sudo conntrack -E conntrack -o timestamp | python3 firewallTracker.py $1 &

sleep 5

cd $HOME/aaragog/C++Verifier
./build/main.out --filesOrKafka kafka --KafkaAddress 10.10.1.10:9092 --numberOfChannels 4 --inputType socket &

cd $HOME/aaragog/firewall

python3 measureCpuMem.py $1 &

echo "global started"

sleep 600
pkill -P $$

echo "global ended, terminating programs"
