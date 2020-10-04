#!/bin/bash

if [ -z "${BASH_VERSINFO}" ] || [ -z "${BASH_VERSINFO[0]}" ] || [ ${BASH_VERSINFO[0]} -lt 4 ]
then
    echo "This script requires Bash version >= 4"
    exit 1
fi

# Get the cloudlab server list.
mapfile -t myArray < servers.txt

# printf '%s\n' "${myArray[@]}"

NUMSERVERS=${#myArray[@]}


if [ $NUMSERVERS -lt 13 ]; then
    echo "Not enough servers"
    exit 1
fi


# Setup for internal traffic servers
for i in 0 1 2 3
do
    scp internalSetup.sh ${myArray[$i]}:./ 
    ssh ${myArray[$i]} "chmod u+x internalSetup.sh; ./internalSetup.sh $(($i+1))"
    scp ../traffic/* ${myArray[$i]}:./traffic/
    ssh ${myArray[$i]} "cd traffic; chmod u+x start_background.sh"
done

# Setup for firewall servers
for i in 4 5 6 7
do
    scp firewallSetup.sh ${myArray[$i]}:./ 
    ssh ${myArray[$i]} "chmod u+x firewallSetup.sh; ./firewallSetup.sh $(($i+1))"
done

# Setup for external traffic servers with IP 10.10.4.X
for i in 8 9
do
    scp externalSetup.sh ${myArray[$i]}:./ 
    ssh ${myArray[$i]} "chmod u+x externalSetup.sh; ./externalSetup.sh 4 $(($i-7))"
    scp ../traffic/* ${myArray[$i]}:./traffic/
    ssh ${myArray[$i]} "cd traffic; chmod u+x start_background.sh"
done

# Setup for external traffic servers with IP 10.10.5.X
for i in 10 11
do
    scp externalSetup.sh ${myArray[$i]}:./ 
    ssh ${myArray[$i]} "chmod u+x externalSetup.sh; ./externalSetup.sh 5 $(($i-9))"
    scp ../traffic/* ${myArray[$i]}:./traffic/
    ssh ${myArray[$i]} "cd traffic; chmod u+x start_background.sh"
done

# Setup for the global verfier
scp globalSetup.sh ${myArray[12]}:./ 
ssh ${myArray[12]} "chmod u+x globalSetup.sh; ./globalSetup.sh 10"

