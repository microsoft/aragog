#include "Packet.h"
#include "utils.cpp"


ostream& operator<< (ostream& out, const Packet& obj) {
    string output = "";
    output = "(t" + obj.time + "), location: " + obj.location;
    for (std::pair<std::string, string> element : obj.fieldMap) {
        output += ", " + element.first + ": " + element.second;
    }
    out << output << endl;
    return out;
}

string Packet::originalInput() {
    return inputLine;
}

void Packet::FirewallParser(string line) {
    if(startsWith(line, "#") || line.length() == 0 || startsWith(line, "time")) {
        return;
    }

    boost::to_lower(line);
    std::vector<string> tokens = split(line, ';');
    
    int index = 0;
    setTime(tokens[index++]);
    fieldMap["event_type"] = tokens[index++];
    fieldMap["transport_protocol"] = tokens[index++];

    if (tokens[index].length() > 0) {
        fieldMap["flow_state"] = tokens[index++];
    } else {
        index++;
    }
    fieldMap["srcIp"] = tokens[index++];
    fieldMap["dstIp"] = tokens[index++];

    if (tokens[index].length() > 0) {
        fieldMap["srcL4Port"] = tokens[index++];
    } else {
        index++;
    }

    if (tokens[index].length() > 0) {
        fieldMap["dstL4Port"] = tokens[index++];
    } else {
        index++;
    }
        
    setLocation(tokens[index++]);
}