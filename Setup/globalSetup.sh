#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

INT=$(ifconfig | grep -B1 128.* | grep -o "^\w*")
LOCAL=$(ifconfig | grep -B1 10.10 | grep -o "^\w*")

sudo ifconfig $LOCAL 10.10.1.$1

sudo apt update 
sudo apt install -y maven
sudo apt install -y openjdk-8-jdk-headless
sudo apt install -y python3-pip
pip3 install psutil

cd

wget https://archive.apache.org/dist/flink/flink-1.9.3/flink-1.9.3-bin-scala_2.11.tgz
tar -xzf flink-1.9.3-bin-scala_2.11.tgz


cd
wget https://ftp.wayne.edu/apache/kafka/2.5.0/kafka_2.12-2.5.0.tgz
tar -xzf kafka_2.12-2.5.0.tgz

git clone --recurse-submodules https://github.com/microsoft/aaragog.git

cd aaragog/verification
mvn clean package

cd

cd kafka_2.12-2.5.0/

sed -i -e s@\#listeners=PLAINTEXT://@listeners=PLAINTEXT://10.10.1.$1@g $HOME/kafka_2.12-2.5.0/config/server.properties

cd 

cd aaragog/runme/
chmod u+x runGlobal.sh
