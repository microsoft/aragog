#ifndef UTILS_H
#define UTILS_H

#include <unordered_map>
#include <iomanip>
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <vector>
#include "../expressions/BoolExpr.cpp"


using std::chrono::system_clock;
using std::chrono::duration_cast;

inline system_clock::time_point getTimePoint(std::string strTime) {
    std::tm myTm = {};
    std::stringstream ss(strTime.c_str());
    ss >> std::get_time(&myTm, "%Y/%m/%d %H:%M");
    return system_clock::from_time_t(std::mktime(&myTm));
}

inline void outputTime(const char *desc, system_clock::time_point &tp) {
    std::time_t now = system_clock::to_time_t(tp);
    std::cout << desc
        << std::put_time(std::localtime(&now), "%Y-%m-%d %H:%M") << "\n";
}

inline std::string getSystemTime() {
    using namespace std::chrono;

    // get current time
    auto now = system_clock::now();

    // get number of milliseconds for the current second
    // (remainder after division into seconds)
    auto ms = duration_cast<milliseconds>(now.time_since_epoch()) % 1000;

    // convert to std::time_t in order to convert to std::tm (broken time)
    auto timer = system_clock::to_time_t(now);

    // convert to broken time
    std::tm bt = *std::localtime(&timer);

    std::ostringstream oss;

    oss << std::put_time(&bt, "%H:%M:%S"); // HH:MM:SS
    oss << '.' << ms.count();

    return oss.str();
}

inline static bool simplifyEvaluate(shared_ptr<BoolExpr> condition, shared_ptr<Packet> p) {
    // cout << "Packet: \n" << *p << endl;
    // cout << "condition: " << (condition -> toString()) << endl;
    bool* temp = static_cast<bool*> (condition -> evaluate(p));
    bool output = *temp;
    delete temp;
    // cout << "condition output: " << output << endl;
    return output;
}

template <typename Out>
inline void split(const std::string &s, char delim, Out result) {
    std::istringstream iss(s);
    std::string item;
    while (std::getline(iss, item, delim)) {
        *result++ = item;
    }
}

template <typename T>
inline int getIndexInVector(std::vector<T> vec, T element) {
    auto it = std::find (vec.begin(), vec.end(), element);
    if (it == vec.end())
    {
        throw std::runtime_error("Index not found in vector");
    } else
    {
        return std::distance(vec.begin(), it);
    }
}

template <typename T>
inline bool contains(std::vector<T> vec, T element) {
    auto it = std::find (vec.begin(), vec.end(), element);
    if (it == vec.end())
    {
        return false;
    } else
    {
        return true;
    }
}

inline string joinVectorString(std::vector<string> v, const char * delim) {
    std::ostringstream imploded;
    std::copy(v.begin(), v.end(),
               std::ostream_iterator<std::string>(imploded, delim));

    return imploded.str();
}



inline std::vector<std::string> split(const std::string &s, char delim) {
    std::vector<std::string> elems;
    split(s, delim, std::back_inserter(elems));
    return elems;
}

inline bool endsWith(const std::string& str, const char* suffix, unsigned suffixLen)
{
    return str.size() >= suffixLen && 0 == str.compare(str.size()-suffixLen, suffixLen, suffix, suffixLen);
}

inline bool endsWith(const std::string& str, const char* suffix)
{
    return endsWith(str, suffix, std::string::traits_type::length(suffix));
    // cout << "endsWith is ok " << ret << endl;
    // return ret;
}

inline bool startsWith(const std::string& str, const char* prefix, unsigned prefixLen)
{
    return str.size() >= prefixLen && 0 == str.compare(0, prefixLen, prefix, prefixLen);
}

inline bool startsWith(const std::string& str, const char* prefix)
{
    return startsWith(str, prefix, std::string::traits_type::length(prefix));
}

template <typename T, typename U>
inline bool contains(const unordered_map<T, U> &m, T key)  { 
    if (m.find(key) == m.end()) 
        return false;
  
    return true;
}

inline string getNameWOExt(string filename) {
    const size_t last_slash_idx = filename.find_last_of("\\/");
    if (std::string::npos != last_slash_idx)
    {
        filename.erase(0, last_slash_idx + 1);
    }

    // Remove extension if present.
    const size_t period_idx = filename.rfind('.');
    if (std::string::npos != period_idx)
    {
        filename.erase(period_idx);
    }
    return filename;
}

inline int getChannelNum(string key, int maxChannels) {
    return hash<string>{}(key) % maxChannels;
}

inline void appendLineToFile(string filepath, string line)
{
    std::ofstream file;
    //can't enable exception now because of gcc bug that raises ios_base::failure with useless message
    //file.exceptions(file.exceptions() | std::ios::failbit);
    // cout << "in appendLineToFile" << endl;
    file.open(filepath,fstream::out | fstream::app);
    if (file.fail()) {
        // cout << "PRoblem here" << endl;
        throw std::ios_base::failure(std::strerror(errno));
    }

    //make sure write fails with exception if something is wrong
    file.exceptions(file.exceptions() | std::ios::failbit | std::ifstream::badbit);

    file << line << std::endl;
}


inline int getposition(const char *array, size_t size, char c)
{
     const char* end = array + size;
     const char* match = std::find(array, end, c);
     return (end == match)? -1 : (match-array);
}

template <typename T, size_t N>
inline int getposition(const T (&array)[N], const T c)
{
     const T* match = std::find(array, array+N, c);
     return (array+N==match)? -1 : std::distance(array, match);
}

inline bool iequals(const string& a, const string& b)
{
    return std::equal(a.begin(), a.end(),
                      b.begin(), b.end(),
                      [](char a, char b) {
                          return tolower(a) == tolower(b);
                      });
}

inline string convertByteToString(string field, unsigned char data[], int start, int end) {
    if (end - start == 1) {
        return to_string((uint8_t) data[start]);
    } else if (end - start == 2 && (field.find("Port") != std::string::npos)) {
        return to_string((uint16_t) ((data[start+1] << 8) | data[start]));
    } else if (end - start == 4 && (field.find("Ip") != std::string::npos)) {
        return to_string((uint8_t) data[start]) + "." + to_string((uint8_t) data[start+1]) + "." + to_string((uint8_t) data[start+2]) + "." + to_string((uint8_t) data[start+3]);
    } else if (end - start == 8 && (field.find("time") != std::string::npos)) {
        double f;
        unsigned char b[] = {data[start], data[start+1], data[start+2], data[start+3], data[start+4], data[start+5], data[start+6], data[start+7]};
        memcpy(&f, b, sizeof(f));
        return to_string(f);
    } else {
        string s(reinterpret_cast<char*>(data + start), end-start);
        return s;
    }
}

#endif