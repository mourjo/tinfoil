#!/usr/bin/env bash

trap "exit" INT TERM
trap "kill 0" EXIT

#mvn clean package
java -cp tinfoil-web/target/tinfoil-web-1.0-SNAPSHOT.jar me.mourjo.main.Server > /dev/null &

PID=$!
sleep 5;

curl -s -X 'POST' 'http://localhost:7002/visit/albert-heijn/mourjo' && curl -s -X 'GET' 'http://localhost:7002/visits/all/mourjo'
