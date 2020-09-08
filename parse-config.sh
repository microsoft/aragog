#!/bin/bash

set -e

if [ -z "$1" ]; then
    echo "Defaulting to SLB..."
    SYSTEM=SLB
else
    echo "Parsing $1 invariants..."
    SYSTEM=$1
fi

rm -f out/*
cd generateSFA
mvn clean install

FORMAT=../config/${SYSTEM}/packetformat.json
FILES=../config/${SYSTEM}/*.invar

for f in ${FILES}; do
    echo "Processing $f"
    mvn exec:java -Dexec.args="--packet_format ${FORMAT} --invar_file $f"
done

cp ../config/${SYSTEM}/packetformat.json ../out/

