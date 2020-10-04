# Global verifier

## Verifier.java

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


The program creates state machines according the invariants provided and processes them. 
The input packets can come either from file, socket or kafka. It parses the packet, processes and raises alert if required. Everything is done in streams to allow parallelism.

Uses the `parser/ParserFactory.Java` to create the packet parser. Uses the `GlobalSFAParser` to process the packets and SFA. Finally, outputs the alerts to file.

## GlobalSFAProcessor.Java

Takes the invariant (SFA + transformation of the packets) and key for constructor. The function `processElement` takes the packets as input, stores the packet to makes sure packets are processed in the right order,  makes the transitions in SFA and outputs the alerts (Notification).

## parser/ParserFactory.Java

It contains a generic way to parse packets according to `packetformat.json`, but you can add custom parsers as well. 

## parser/GenericParser.Java

Takes the `packetformat.json` and delimiter for the constructor. Creates the parse tree. Parse tree consists of fields and conditional nodes. Field are simple parsing nodes and conditional nodes decides which branch of the tree to take.

## utils/Packet.Java

Class to store packet. It contain function to set and get fields in packet too. Also, contains function to change it into string format.

## dsfa/GlobalSFAParser.Java

Creates the SFA and transformation and return GlobalSFAProcessor.

## expressions/ExprBuilder.Java

Contain the functions to create the SFA from the parsed tree. The parsed tree contain many kinds of nodes (binary/boolean operators). This file goes through them to create the SFA.