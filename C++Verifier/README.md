# Local verifier

## main.cpp

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


The program creates state machines according the invariants provided and processes them. 
The input packets can come either from file or socket. If packet coming from file, the assumption is to they are delimited by semi-colon and use a specific parser. Ths needs to be changed in the future. Ideally, the delimiter should be an input argument and we are able to use the generic parser.

After receiving the packet, the packet is parsed using the `utils/PacketParser.cpp` class. The packet is passed to `main_process` function. It is responsible to call (or create) the appropriate SFA according the invariant violation specification, and then output the packet (if not suppressed) to file or kafka.

## SFAProcessor.cpp

Takes the invariant (SFA + transformation of the packets) and key for constructor. The function `processPacket` makes the transitions in SFA and returns whether to suppress packet or not. The packet is the function argument.

## utils/PacketParser.cpp

Takes the `packetformat.json` for the constructor. It uses `rapidjson` library to pase the json file. Creates the parse tree with the help of `utils/ParserHelper.cpp`. Parse tree consists of fields and conditional nodes. Field are simple parsing nodes and conditional nodes decides which branch of the tree to take. 

The `parsePacket` function takes the raw data as input and return the Packet. 

## utils/Packet.h

Class to store packet. It contain function to set and get fields in packet too. Also, contains function to change it into string format.

## DSFA/Invariant.cpp

Take the invariant name as input to create the invariant. A single invariant can contain multiple files. To parse each file, it sses `DSFA/SFAParser.cpp` to create the appropriate SFA. 

## DSFA/SFAParser.cpp

Contains two functions `parseGlobalStateMachine` and `parseLocalStateMachine` to parse state machines. It uses `antlr` to build parsing tree for SFA and pass it to `expressions/ExprBuilder.h`.

## expressions/ExprBuilder.h

Contain the functions to create the SFA from the parsed tree. The parsed tree contain many kinds of nodes (binary/boolean operators). This file goes through them to create the SFA.

