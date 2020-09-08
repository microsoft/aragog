#! /bin/bash

if [[ $# -ne 3 ]]; then
    echo "Usage: sudo ./start_background.sh <current_server> <total_servers> <file_initial>"
    exit 1
fi

socat - TCP-LISTEN:5058,fork &> /dev/null &

sleep 5

cd $HOME/traffic
python time_walking.py $1 $2 $3 &

wait
