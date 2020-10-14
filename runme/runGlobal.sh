#!/bin/bash

cd
cd kafka_2.12-2.5.0/
./bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
./bin/kafka-server-start.sh -daemon config/server.properties

cd

cd flink-1.9.3
./bin/start-cluster.sh

sleep 10

cd $HOME/aragog
$HOME/flink-1.9.3/bin/flink run verification/target/verification-0.1.jar --config_dir out/ --out_path out/ --mode GLOBAL_KAFKA --broker_address 10.10.1.10:9092 --channel_per_invariant 1 &

cd $HOME/aragog/firewall
python3 measureCpuMem.py g &

echo "global started"

sleep 600
pkill -P $$

cd 

cd kafka_2.12-2.5.0/
./bin/kafka-server-stop.sh 
./bin/zookeeper-server-stop.sh

cd /tmp/
sudo rm -r kafka-logs/
sudo rm -r zookeeper

cd
cd flink-1.9.3/
./bin/stop-cluster.sh
cd log
rm *

echo "global ended"
