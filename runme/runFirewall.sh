#!/bin/bash

cd $HOME/MBVerifier/firewall

sudo conntrack -E conntrack -o timestamp | python3 firewallTracker.py $1 &

cd $HOME/MBVerifier/C++Verifier
./build/main.out --filesOrKafka kafka --KafkaAddress 10.10.1.10:9092 --numberOfChannels 4 --inputType socket &

python3 measureCpuMem.py $1 &

echo "global started"

sleep 10m
pkill -P $$

echo "global ended, terminating programs"
