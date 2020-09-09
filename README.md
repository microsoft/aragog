
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
git clone --recurse-submodules https://github.com/microsoft/aaragog.git
cd aaragog
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


### Authors 

This code was mainly written by: 

Nofel Yaseen and Vincent Liu and the authors of the OSDI 2020 paper:Scalable Runtime Verification of Distributed Middleboxes
 


# Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

