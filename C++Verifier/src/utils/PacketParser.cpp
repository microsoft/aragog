#ifndef PACKETPARSER_H
#define PACKETPARSER_H

#include "../rapidjson/document.h"
#include "../rapidjson/filereadstream.h"
#include "../rapidjson/istreamwrapper.h"
#include "../rapidjson/writer.h"
#include "../rapidjson/stringbuffer.h"
#include "ParserHelper.cpp"

class PacketParser
{
private:
    unique_ptr<ParseNode> root;
    std::vector<string> fieldSequeunce;
public:
    int packetLength = 0;
    PacketParser() {
        root = nullptr;
        packetLength = 0;
    }
    PacketParser(string formatFile) {

        ifstream ifs(formatFile);
        rapidjson::IStreamWrapper isw(ifs);
        rapidjson::Document config;
        config.ParseStream(isw);
        rapidjson::Value& fieldFormat = config["fields"];
        // cout << printValue(fieldFormat) << endl;
        assert(fieldFormat.IsArray());
        packetLength = 0;
        root = parseFormat(fieldFormat, nullptr);
        // for (int i = 0; i <  fieldSequeunce.size(); i++) {
        //     cout << fieldSequeunce[i] << endl;
        // }
    }
    unique_ptr<ParseNode> parseFormat(rapidjson::Value& fieldFormat, ParseNode* last) {
        unique_ptr<ParseNode> first = nullptr;
        // cout << "In parseFormat" << endl;
        for (rapidjson::SizeType i = 0; i < fieldFormat.Size(); i++) {
        
            rapidjson::Value& node = fieldFormat[i];
            int count = 0;
            for (rapidjson::Value::ConstMemberIterator itr = node.MemberBegin(); itr != node.MemberEnd(); ++itr) {
                count += 1;
            }
            if (count > 1) {
                unique_ptr<ConditionalNode> cn = unique_ptr<ConditionalNode>(new ConditionalNode(last));
                for (rapidjson::Value::MemberIterator iter = node.MemberBegin(); iter != node.MemberEnd(); ++iter){
                    string conditionStr = iter -> name.GetString();
                    unique_ptr<Condition> cond = unique_ptr<Condition>(new Condition(conditionStr));
                    rapidjson::Value& child = iter -> value;
                    cn -> children[cond.get()] = parseFormat(child, nullptr);
                }
            } else if (count == 1) {
                last = new Field(node.MemberBegin() -> name.GetString(), node.MemberBegin() -> value.GetInt(), last);
                packetLength += node.MemberBegin() -> value.GetInt();
                fieldSequeunce.push_back(node.MemberBegin() -> name.GetString());
            } else {
                throw std::invalid_argument("Empty field object?");
            }
            if (first == nullptr && last == nullptr) {
                first = nullptr;
            } else if (first == nullptr) {
                first = std::unique_ptr<ParseNode>(last);
            }
        }
        return first;
    }

    ~PacketParser() {
    }

    shared_ptr<Packet> parsePacket(string line) {
        if(startsWith(line, "#") || line.length() == 0 || startsWith(line, "time")) {
            return nullptr;
        }
        return shared_ptr<Packet>(new Packet(line));
    }

    shared_ptr<Packet> parsePacket(unsigned char data[]) {
        shared_ptr<Packet> p(new Packet());
        parsePacketHelper(data, 0, p, root.get());
        return p;
    }

    int parsePacketHelper(unsigned char data[], int index, shared_ptr<Packet> p, ParseNode* current) {
        int nextIndex = index;
        if (Field* f = dynamic_cast<Field*>(current)) {
            nextIndex = index + f-> length;
            p -> set(f -> name, convertByteToString(f -> name, data, index, nextIndex));
            
            // cout << f-> name << endl;
        } else {
            // If it's a conditional, traverse the correct child
            ConditionalNode* cn = dynamic_cast<ConditionalNode*>(current);
            ParseNode* child = cn -> getChildNode(p);

            if (child != nullptr) {
                nextIndex = parsePacketHelper(data, index, p, child);
            }
        }

        if (current -> next != nullptr) {
            nextIndex = parsePacketHelper(data, nextIndex, p, (current -> next).get());
        }
        return nextIndex;
    }

    string printDocument(rapidjson::Document &d) {
        using namespace rapidjson;
        StringBuffer buffer;
        Writer<StringBuffer> writer(buffer);
        d.Accept(writer);

        return buffer.GetString();
    }

    string printValue(rapidjson::Value &val) {
        using namespace rapidjson;
        StringBuffer buffer;
        Writer<StringBuffer> writer(buffer);
        val.Accept(writer);

        return buffer.GetString();
    }

    string getPacketString(shared_ptr<Packet> p) {
        if (root == nullptr) {
            return p -> originalInput();
        }
        std::vector<string> outputVec;
        // cout << "\n vector size initial: " << outputVec.size() << endl;
        // cout << "Sequeunce size: " << fieldSequeunce.size() << endl;
        for (string field: fieldSequeunce) {
            outputVec.push_back(p -> get(field));
            // cout << "Field: " << field << ", Value: " << p -> get(field) << endl;
        }
        // cout << "\n vector size after: " << outputVec.size() << endl;
        return joinVectorString(outputVec, ";");
    }
};


#endif