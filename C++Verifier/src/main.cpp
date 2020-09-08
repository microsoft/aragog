#include <boost/program_options.hpp>
#include <experimental/filesystem>
#include <cppkafka/cppkafka.h>

namespace po = boost::program_options;
namespace fs = std::experimental::filesystem;

#include <iterator>
#include <sys/socket.h> 
#include <arpa/inet.h> 
#include <regex>
#include "SFAProcessor.cpp"
#include "utils/cpuMeasure.h"
#include "utils/PacketParser.cpp"

using namespace std;
using namespace cppkafka;

shared_ptr<Packet> parseLine(string line);
void testKafka();
void main_process(shared_ptr<Packet> p,
    vector<shared_ptr<Invariant>> &invList,
    unordered_map<string, unordered_map< string ,unique_ptr<SFAProcessor> >> &invTokeyToProcessor,
    shared_ptr<Producer> producer,
    shared_ptr<int> counter);


static string prefix;
static string filesOrKafka;
static int filesPerInvariant;

int main(int argc, char const *argv[]) {
    string configDir;
    string configFile;
    string inputPath;    
    string KafkaAddress;
    string inputType;
    string local_socket_ip;
    string local_socket_port;


    po::options_description desc("Allowed options");
    desc.add_options()
        ("help", "Arguments for the program")
        ("configDir", po::value<string>(&configDir)->default_value("../out/"), "Set configDir")
        ("configFile", po::value<string>(&configFile)->default_value("../out/packetformat.json"), "Set configDir")
        ("inputPath", po::value<string>(&inputPath)->default_value("../datafiles/38-38.0.001000.csv"), "Set inputPath")
        ("filesOrKafka", po::value<string>(&filesOrKafka)->default_value("files"), "Write to file or kafka")
        ("KafkaAddress", po::value<string>(&KafkaAddress)->default_value("localhost:9092"), "IP and Port of kafka")
        ("numberOfChannels", po::value<int>()->default_value(4), "Number of files/topics to create for Global input")
        ("prefix", po::value<string>(&prefix)->default_value("../out/globalInput/"), "Set inputPath")
        ("inputType", po::value<string>(&inputType)->default_value("file"), "Input is file or socket")
        ("local_socket_ip", po::value<string>(&local_socket_ip)->default_value("127.0.0.1"), "Socket IP where to read events from")
        ("local_socket_port", po::value<string>(&local_socket_port)->default_value("10001"), "Socket IP where to read events from")
    ;

    po::variables_map vm;
    po::store(po::parse_command_line(argc, argv, desc), vm);
    po::notify(vm);

    // init_cpu_measure();

    if (vm.count("help")) {
        cout << desc << "\n";
        return 0;
    }

    pid_t pid = getpid();
    string defaultPath = "/tmp/localVerifier.pid";
    ofstream pidFile(defaultPath);
    pidFile << pid  << endl;
    pidFile.close();


    int nFiles = vm["numberOfChannels"].as<int>(); 

    fs::path configDirPath(configDir);
    configDirPath = fs::absolute(configDirPath);
    Configuration config;
    shared_ptr<Producer> producer;

    cout << "configDir = " << configDirPath << "\n"
               "inputPath = " << inputPath << std::endl;

    if (filesOrKafka == "files") {
        cout << "Output set to files" << endl;
    } else if (filesOrKafka == "kafka") {
        cout << "Output set to kafka on " << KafkaAddress << endl;
        config = {
            { "metadata.broker.list",  KafkaAddress}
            // ,{"debug","all"}
        };
        producer = shared_ptr<Producer>(new Producer(config));


    } else {
        throw std::runtime_error("Unknown output. Only kafka or files are output options");
    }

    unordered_map<string, shared_ptr<vector<string>> > smFiles;
    std::regex e (".*\\.sm\\.([0-9]+|g)");
    for (const auto& dirEntry : fs::recursive_directory_iterator(configDirPath)) {
        string filename = dirEntry.path().string();
        if (!(std::regex_match(filename,e))) {
            continue;
        }

        string basename = filename.substr(0, filename.length() - 5);
        if (not contains(smFiles, basename)) {
            smFiles[basename] = shared_ptr<vector<string>>(new vector<string>());
        }
        shared_ptr<vector<string>> v = smFiles.at(basename);
        v -> push_back(filename);
    }

    unordered_map<string, unordered_map< string ,unique_ptr<SFAProcessor> >> invTokeyToProcessor;

    vector<shared_ptr<Invariant>> invList;

    for (const auto & [ key, value ] : smFiles) {
        invList.push_back(shared_ptr<Invariant>(new Invariant(*value)));
    }

    int numInvariants = invList.size();

    cout << "Number of Invariants: " << numInvariants << endl;

    filesPerInvariant = nFiles / numInvariants;

    shared_ptr<int> counter(new int(0));

    int packetLength = 0;
    shared_ptr<PacketParser> packetParser = shared_ptr<PacketParser>(new PacketParser(configFile));
    packetLength = packetParser -> packetLength;

    cout << "Going to process packets now" << endl;

    if (inputType == "file") {
        string line;
        ifstream inputFile(inputPath);
        while (std::getline(inputFile, line)) {
            shared_ptr<Packet> p = packetParser -> parsePacket(line);
            if (p != nullptr) {
                main_process(p, invList, invTokeyToProcessor, producer, counter);
            }
        }

    } else {
        int sock = 0, valread; 
        struct sockaddr_in serv_addr; 
        unsigned char buffer[1024] = {0}; 
        if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) 
        { 
            printf("\n Socket creation error \n"); 
            return -1; 
        }

        serv_addr.sin_family = AF_INET; 
        serv_addr.sin_port = htons(stoi(local_socket_port)); 
        
        // Convert IPv4 and IPv6 addresses from text to binary form 
        if(inet_pton(AF_INET, local_socket_ip.c_str(), &serv_addr.sin_addr)<=0) 
        { 
            printf("\nInvalid address/ Address not supported \n"); 
            return -1; 
        } 

        if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) 
        { 
            printf("\nConnection Failed \n"); 
            return -1; 
        }

        size_t pos = 0; // Start of socket buffer
        std::clock_t c_start = std::clock();
        std::clock_t c_end;
        double time_elapsed_ms;
        unsigned char readBuffer[packetLength];
        memset( readBuffer, 0, packetLength*sizeof(unsigned char));
        int startIndex = 0; // Start of read buffer
        while ((valread = read(sock, buffer, (sizeof buffer)))) {
            pos = 0;
            while(valread - pos >= packetLength) {
                
                memcpy(readBuffer + startIndex, buffer + pos, packetLength - startIndex);
                // for(int i=0; i<packetLength; ++i) {
                //     std::cout << (int)readBuffer[i] << " ";
                // }

                shared_ptr<Packet> p = packetParser -> parsePacket(readBuffer);
                
                p -> inputLine = packetParser -> getPacketString(p);
                // cout << "Packet: " << p -> inputLine << endl;
                pos += packetLength;
                main_process(p, invList, invTokeyToProcessor, producer, counter);
                startIndex = 0;
            }
            int bytesLeft = valread - pos;
            if (bytesLeft > 0) {
                memcpy(readBuffer + startIndex, buffer + pos, valread - pos);
                startIndex = valread - pos;
            }

            if (filesOrKafka == "kafka") {
                c_end = std::clock();
                time_elapsed_ms = 1000.0 * (c_end-c_start) / CLOCKS_PER_SEC;
                if (time_elapsed_ms > 3000) {
                    producer -> flush();
                    c_start = c_end;
                }
            }
        }
    }
    
    if (filesOrKafka == "kafka") {
        sleep(5);
        producer -> flush();   
    }

    cout << "Program finished" << endl;
    return 0;   
}

void main_process(shared_ptr<Packet> p,
    vector<shared_ptr<Invariant>> &invList,
    unordered_map<string, unordered_map< string ,unique_ptr<SFAProcessor> >> &invTokeyToProcessor,
    shared_ptr<Producer> producer,
    shared_ptr<int> counter) {
    bool out = false;

    for (shared_ptr<Invariant> inv : invList) {
        string key = inv -> getKeyString(p);
        string invName = inv -> name;

        // Check if we have processors related to key
        if (invTokeyToProcessor.find(invName) == invTokeyToProcessor.end()) {
            invTokeyToProcessor[invName] = unordered_map<string, unique_ptr<SFAProcessor>>();
        }

        if (invTokeyToProcessor[invName].find(key) == invTokeyToProcessor[invName].end()) {
            invTokeyToProcessor[invName][key] = unique_ptr<SFAProcessor>(new SFAProcessor(inv, key));
        }
        out = out || (invTokeyToProcessor[invName][key] -> processPacket(p));
        if (out) {
            int channel = getChannelNum(key, filesPerInvariant);
            string channelName = (inv ->name) + to_string(channel);
            string output = p -> originalInput();

            if (filesOrKafka == "files") {
                appendLineToFile(prefix + channelName + ".txt", output);
            } else {
                producer -> produce(MessageBuilder(channelName).payload(output));
                // (*counter)++;
            }
            
        }
        out = false;

    }
}

// Replace  parseline with packetParser parseline
// Goals: Read strings from file or bytes from socket should have the same output.

shared_ptr<Packet> parseLine(string line) {
    if(startsWith(line, "#") || line.length() == 0 || startsWith(line, "time")) {
        return nullptr;
    }
    return shared_ptr<Packet>(new Packet(line));
}


    


    
