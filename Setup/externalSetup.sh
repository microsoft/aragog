#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Get interface for port communicating locally
LOCAL=$(ifconfig | grep -B1 10.10 | grep -o "^\w*")

mkdir traffic
# Update IP
sudo ifconfig $LOCAL 10.10.$1.$2
# Add rules to make sure traffic is routed through firewall
sudo ip route add 10.10.1.0/24 via 10.10.$1.100 dev $LOCAL

# softwares required for traffic generation and measurements.
sudo apt update
sudo apt install -y socat
