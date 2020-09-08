# Scalable Runtime Verification of Distributed Middleboxes


## Table of Content
* [Technologies](#technologies)
* [Setup](#setup)
* [Run](#run)
* [Files](#files)
* [Todo](#todo)
* [Timeline](#timeline)

## Techonologies
* JAVA 9.0.4
* Maven
* z3
* Symbolic Automata
* Cppkafka
* Apache Flink


## Setup
First step is to set up topology at cloudlab. If you do not access Cloudlab. Please reach out to me (Nofel Yaseen). I will add your ssh key to the required machines.

### How to set up cloud lab machines
First click on experiments and then create experiment profile.

Second step is to fill out the name (anything would work). Click on edit source, copy the file `./Setup/cloudlabProfile.txt` and paste it in the textbox. Click Accept. Click Create.

In the next screen. Click Instantiate.

Select APT UTAH as the cluster and finish.

Wait for the experiment to get ready.

``` 
git clone --recurse-submodules https://github.com/NofelYaseen/MBVerifier.git
cd MBVerifier
./install.sh
cd generateSFA
./install_z3.sh
```

## Run
```
cd generateSFA
mvn clean install
mvn exec:java
cd ../
./run_verify.sh
```

### Todo
1. Find bug in SLB trace
1. Turn tree duplicate checking in to hashset
1. Implement support for MAP
1. Add support for timers in language.
1. Think about firewall topology and invariants.
 