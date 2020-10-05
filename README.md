
# Scalable Runtime Verification of Distributed Middleboxes


## Table of Content
* [Technologies](#technologies)
* [Setup](#setup)
* [Run](#run)
* [Authors](#authors)

## Techonologies
* JAVA 9.0.4
* Maven
* z3
* Symbolic Automata
* Cppkafka
* Apache Flink


## Setup
Below are the instructions to run the firewall experiment. To set up verifier for you own cloud, please refer to `aaragog/Setup/cloudSetup.md`.
The firewall experiment information can be found at `aaragog/firewall/README.md`. We have automated the setup using scripts, but details can be found in `aaragog/firewall/firewallconf.md`.

### How to set up cloud lab machines
First click on experiments and then create experiment profile.

Second step is to fill out the name (anything would work). Click on edit source, copy the file `./Setup/cloudlabProfile.txt` and paste it in the textbox. Click Accept. Click Create.

In the next screen. Click Instantiate.

Select APT UTAH as the cluster and finish.

Wait for the experiment to get ready.

### Invariant Compilation

We have already compiled invariants for firewall and put it `out/` folder.  

Invariant compilation was tested on MacOS. It requires JDK and maven to run. Instructions to install maven can be found [here](https://maven.apache.org/install.html). For compiling invariants by yourself, please follow these:
``` 
git clone --recurse-submodules https://github.com/microsoft/aaragog.git
cd aaragog/generateSFA
./install_z3.sh
mvn clean package
```

Example Compilation:
```
cd aaragog/generateSFA
mvn exec:java -Dexec.args="--packet_format ../out/packetformat.json --invar_file ../config/firewall/new_established.invar"
```

### Verifier Setup
Assuming Cloublab is up and running with ssh key in all the servers. Please paste the server list in Setup/servers.txt.
It can executed at both macOS and Linux. The default bash on macOS is still bash v3, the script need `>= 4`. Please follow the instructions [here](https://www.shell-tips.com/mac/upgrade-bash/)

```
cd Setup
./setup.sh
```
For experiment details, please see firewall/README.md

It installs the required software and sets up Apache Flink, Kafka and install the firewall rules accordingly.

## Run
It can executed at both macOS and Linux. The default bash on macOS is still bash v3, the script need `>= 4`. Please follow the instructions [here](https://www.shell-tips.com/mac/upgrade-bash/)
To run the firewall experiment:
```
cd runme
./runme.sh
```

### Output

The output will be `.txt` files. Each file for specific invariant. Each line will show the alerts raised.

## Authors 

This code was mainly written by: 

Nofel Yaseen and Vincent Liu 
 


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

