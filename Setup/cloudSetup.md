# Run the verifier for your cloud

Below are the instructions to set up all parts of running the verifier in your own cloud/middlebox:

1. Middlebox config file: Similar to `aragog/config/firewall/packetformat.json`, define the packet format of the packets exported by the middlebox. Please refer to the section 5.1 for details.

1. Invariant Violation (IV) Specifications: Similar to `aragog/config/firewall/*.invar`, define the IV specifications for the middlebox. Please refer to the section 5.2 for details.

1. Compile the invariants using generateSFA. Example compilation: 

```
cd aragog/generateSFA
mvn exec:java -Dexec.args="--packet_format ../out/packetformat.json --invar_file ../config/firewall/new_established.invar"
```
Arguments in generating the SFA.

|     Argument    |    options    |                                                                      |
|-----------------|:-------------:|:---------------------------------------------------------------------|
| -h, --help      |               | show this help message and exit                                      |
| --packet_format | PACKET_FORMAT | Packet format config file (default: ../config/SLB/packetformat.json) |
| --invar_file    | INVAR_FILE    | Input .invar file (default: ../config/SLB/primary_single.invar)      |
| --debug         |               | Packet format config file (default: false)                           |
| --out_dir       | OUT_DIR       | Output directory (default: ../out/)                                  |
| --examples      |               |  Run examples only (default: false)                                  |


4. Compile the local verifer at (or close to) middlbox location. Need to do this for each middlebox location.

```
git clone --recurse-submodules https://github.com/microsoft/aragog.git
cd $HOME/aragog/C++Verifier/
chmod u+x setup.sh
./setup.sh
make
```

5. Set up Zokeeper and Apache Kafka Cluster. All local verifier should be able to communicate with the Kafka cluster. It is fine to co-locate zookeeper and kafka at the same machine. At each Zookeepr and Kafka machine:
```
cd
wget https://ftp.wayne.edu/apache/kafka/2.5.0/kafka_2.12-2.5.0.tgz
tar -xzf kafka_2.12-2.5.0.tgz
```
Set up Zookeeper configuration file: `kafka_*/config/zookeeper.properties`. Example configuration:
```
tickTime=2000
dataDir=/data/zookeeper
clientPort=2181
maxClientCnxns=60
initLimit=10
syncLimit=5
server.1=your_zookeeper_node_1:2888:3888
server.2=your_zookeeper_node_2:2888:3888
server.3=your_zookeeper_node_3:2888:3888
```

On each zookeeper node `data/zookeeper` folder, run `echo <ID> > myid`, replace `<ID>` with the node ID. Zookeeper need odd number of nodes. 

Set the listener IP, zookeeper IPs and replication factor in Kafka configuration file `kafka_*/config/server.properties`. Please keep the replication factor of at least 3 in production. The configuration file is easy to follow.

Once the configuration is done, start the zookeeper and kafka cluster on each machine.

```
cd kafka_*/
./bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
./bin/kafka-server-start.sh -daemon config/server.properties
```

In case any zookeeper or kafka broker goes down, you can start it again with the same command. It will automatically sync with other nodes to get the latest data.  

6. Set up and start the Apache Flink at the global verifier. Have at least 3 nodes for the global verifier for fault tolerance and scalibility. Flink has Master-Worker model. 

```
wget http://mirror.cc.columbia.edu/pub/software/apache/flink/flink-1.11.2/flink-1.11.2-bin-scala_2.11.tgz
tar -xzf flink-1.11.2-bin-scala_2.11.tgz
```
Write the IP of masters and worker in `flink*/conf/masters` and `flink*/conf/workers` at the flink job manager node.

Update `jobmanager.rpc.address` in `flink*/conf/flink-conf.yaml` witht the job manager address at all the flink nodes.

Make sure job manager node has ssh access to all the workers.

Finally, start Flink from the job manager:

```
cd flink*
./bin/start-cluster.sh
```

7. Compile and start the global verifier from the Flink job manager node.

Compilation
```
git clone --recurse-submodules https://github.com/microsoft/aragog.git
cd aragog/verification
mvn clean package
```
Place the compiled invariants and `packetformat.json` in config folder. Default is `out/`.
Running the global verifier.  
Example Run:
```
cd aragog
$HOME/flink-*/bin/flink run verification/target/verification-0.1.jar --config_dir out/ --out_path out/ --mode GLOBAL_KAFKA --broker_address 10.10.1.10:9092 --channel_per_invariant 10 
```

Arguments in running the global verifer


|         Argument        |         options       |                                                                                                                            |
|-------------------------|:---------------------:|:---------------------------------------------------------------------------------------------------------------------------|
| -h, --help              |                       | show this help message and exit                                                                                            |
| --config_dir            | CONFIG_DIR            | config directory (default: out/)                                                                                           |
| --out_path              | OUT_PATH              | Output file (default: ../out/)                                                                                             |
| --mode                  | MODE                  | Mode: LOCAL_FILE, LOCAL_SOCKET, LOCAL_FOLDER,  GLOBAL_KAFKA, STANDALONE_FILE, STANDALONE_FOLDER (default: STANDALONE_FILE) |
| --input_file            | INPUT_FILE            | Input file needed for LOCAL or STANDALONE (default: ../notebook/38-38.0.001000.csv)                                        |
| --input_folder          | INPUT_FOLDER          | Input folder needed for global (default: out/globalInput/)                                                                 |
| --broker_address        | BROKER_ADDRESS        | IP:Port,... for kafka broker (default: localhost:9092)                                                                     | 
| --local_socket_ip       | LOCAL_SOCKET_IP       | IP for reading from a local socket (default: localhost)                                                                    |
| --local_socket_port     | LOCAL_SOCKET_PORT     | Port for reading from a local socket (default: 10000)                                                                      | 
| --parser                | PARSER                | Select parser from generic or firewall (default: firewall)                                                                 |
| --channel_per_invariant | CHANNEL_PER_INVARIANT | Channel Per Invariant (default: 10)                                                                                        |


The global verifier is now running.

8. Please follow the instructions to run PTP [here](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/6/html/deployment_guide/ch-configuring_ptp_using_ptp4l). PTP needs to running at all the local verfiers and global verifier.

9. Local verifier was compiled in step 4. Now, we need to just run it. Place the compiled invariants and `packetformat.json` in config folder. Default is `out/`.
Running the local verifier.  
Example Run:
```
cd $HOME/aragog/C++Verifier
./build/main.out --filesOrKafka kafka --KafkaAddress 10.10.1.10:9092 --numberOfChannels 10 --inputType socket &
```

Arguments in running the local verifer

|         Argument        |         options       |                                                                         |
|-------------------------|:---------------------:|:------------------------------------------------------------------------|
| -h, --help              |                       | show this help message and exit                                         |
| --configDir             | CONFIG_DIR            | Set configDir                                                           |
| --configFile            | CONFIG_FILE           | config file (default: ../out/packetformat.json)                         |
| --inputPath             | INPUT_FILE            | Input file needed for LOCAL  (default: ../datafiles/38-38.0.001000.csv) |    
| --filesOrKafka          | OUTPUT_TYPE           | Write to file or kafka (default: file)                                  |
| --KafkaAddress          | BROKER_ADDRESS        | IP:Port,... for kafka broker (default: localhost:9092)                  |
| --channel_per_invariant | CHANNEL_PER_INVARIANT | Channel Per Invariant (default: 10)                                     |
| --prefix                | OUTPUT_FOLDER_PATH    | Set Path if the output is folder (default: ../out/globalInput/)         |
| --inputType             | INPUT_TYPE            | Input is file or socket                                                 |
| --local_socket_ip       | LOCAL_SOCKET_IP       | IP for reading from a local socket (default: localhost)                 |
| --local_socket_port     | LOCAL_SOCKET_PORT     | Port for reading from a local socket (default: 10001)                   |

