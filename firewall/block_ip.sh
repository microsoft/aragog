#!/bin/bash

sudo iptables -A INPUT -s $1 -j DROP
echo "$1" | netcat localhost 10001

# echo $1