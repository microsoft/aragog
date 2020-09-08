#ifndef PACKET_H
#define PACKET_H

#include <chrono>
#include <string>
#include <iostream>
#include <boost/algorithm/string.hpp>
#include <unordered_map>

using namespace std;

class Packet {
    unordered_map<string, string> fieldMap;
    string time;
    string location;

    
    public:
        string inputLine;
        Packet(string line) {
            FirewallParser(line);
            inputLine = line;
        }

        Packet() {
        }
        void FirewallParser(string line);

        void setTime(string _time) {
            time = _time;
        }
        void setLocation(string loc) {
            location = loc;
        }

        void set(string field, string value) {
            if (field.find("time") != std::string::npos) {
                setTime(value);
                return;
            } else if (field.find("location") != std::string::npos) {
                setLocation(value);
                return;
            }
            fieldMap[field] = value;
        }

        string getTime() {
            return time;
        }

        string getLocation() {
            return location;
        }

        string get(string key) {
            if (key == "time") {
                return getTime();
            } else if (key == "location") {
                return getLocation();
            }
            try {
                return fieldMap.at(key);
            }
            catch (const std::out_of_range& e) {
                // cout << "WARNING: Field not in packet" << endl;
            }
            return "";
        }
        string originalInput();
        friend ostream& operator<< (ostream& out, const Packet& obj); 
};

#endif