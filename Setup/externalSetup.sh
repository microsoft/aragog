#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

LOCAL=$(ifconfig | grep -B1 10.10 | grep -o "^\w*")

mkdir traffic
sudo ifconfig $LOCAL 10.10.$1.$2
sudo ip route add 10.10.1.0/24 via 10.10.$1.100 dev $LOCAL

sudo apt update
sudo apt install -y socat
