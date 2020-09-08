#ifndef SFAPARSER_H
#define SFAPARSER_H

#include <fstream>
#include "../antlr4/SimpleSMTLIBLexer.h"

#include "DSFA.h"
#include "../expressions/ExprBuilder.h"

using namespace std;

inline void parseGlobalStateMachine(std::shared_ptr<BoolExpr> & negatedLocExpr, std::shared_ptr<BoolExpr> & filter, std::vector<std::string> & groupByFields, std::string filename) {
    // cout << "In parseGlobalStateMachine" << endl;

    std::ifstream infile(filename);

    // cout << "reading file: " << filename << endl;

    std::string line;

    SimpleSMTLIBLexer *lexer;
    antlr4::CommonTokenStream *tokens;
    SimpleSMTLIBParser *parser;
    SimpleSMTLIBParser::StartBoolContext *tree;

    // Line 1: Negative location filter
    std::getline(infile, line);
    antlr4::ANTLRInputStream * stream = new antlr4::ANTLRInputStream(line);

    lexer = new SimpleSMTLIBLexer(stream);
    tokens = new antlr4::CommonTokenStream(lexer);
    parser = new SimpleSMTLIBParser(tokens);
    tree = parser -> startBool();
    negatedLocExpr = ExprBuilder::buildBoolExpr(tree, NULL, NULL);

    // Line 2: initial state. Skip for local
    std::getline(infile, line);

    // Line 3: final state(s). Skip for local
    std::getline(infile, line);


    // Line 4: filter
    std::getline(infile, line);
    if (line.empty()) {
        filter = nullptr;
    } else {
        delete lexer;
        delete tokens;
        delete parser;
        delete stream;

        stream = new antlr4::ANTLRInputStream(line);
        lexer = new SimpleSMTLIBLexer(stream);
        tokens = new antlr4::CommonTokenStream(lexer);
        parser = new SimpleSMTLIBParser(tokens);
        tree = parser -> startBool();
        // cout << "filter line: " << line << endl;
        filter = ExprBuilder::buildBoolExpr(tree, nullptr, nullptr);
        // cout << "filter created: " << filter -> toString() << endl;
    }

    
    std::getline(infile, line);
    groupByFields = split(line, ',');

    delete stream;
    delete lexer;
    delete tokens;
    delete parser;

    // cout << "parseGlobalStateMachine complete" << endl;
    return;
}

inline bool string2bool (const std::string & v) {
    return !v.empty () &&
        (strcasecmp (v.c_str (), "true") == 0 ||
         atoi (v.c_str ()) != 0);
}


inline std::unique_ptr<DSFA> parseLocalStateMachine(std::string filename) {
    std::ifstream infile(filename);
    std::string line;

    SimpleSMTLIBLexer *lexer;
    antlr4::CommonTokenStream *tokens;
    SimpleSMTLIBParser *parser;
    antlr4::ANTLRInputStream * stream;
    SimpleSMTLIBParser::StartBoolContext *tree;

    // cout << "reading line one" << endl;
    // Line 1: initial state
    std::getline(infile, line);
    
    // cout << "line one output: " << line << endl;
    int startState = stoi(line);
    // cout << "line one parsed output: " << startState << endl;

    // Line 2: final state(s)
    vector<int> finalStates;

    // cout << "reading line two" << endl;
    std::getline(infile, line);
    // cout << "line two output: " << line << endl;
    std::vector<string> toks = split(line, ',');
    for (string s : toks) {
        finalStates.push_back(stoi(s));
    }

    // cout << "line two parsed output: " << endl;
    // for (int n: finalStates) {
    //     cout << n << endl;
    // }

    // Line 3: variables
    std::getline(infile, line);
    std::vector<string> variableList = split(line, ',');

    // Line 4+: transitions
    unordered_map<int, vector<DSFAMove>> moves;


    while(std::getline(infile, line)) {
        // cout << "line 4+: " << line << endl;
        std::vector<string> toks = split(line, ';');

        // cout << "Antlr input: " << toks[3] << endl;
        stream = new antlr4::ANTLRInputStream(toks[3]);
        lexer = new SimpleSMTLIBLexer(stream);
        tokens = new antlr4::CommonTokenStream(lexer);
        parser = new SimpleSMTLIBParser(tokens);

        // cout << "making tree" << endl;
        tree = parser -> startBool();

        shared_ptr<vector<string>> locationReferences(new vector<string>());
        shared_ptr<unordered_map<string, string>> variableComparisons(new unordered_map<string, string>());
        // cout << "going in ExprBuilder" << endl;
        shared_ptr<BoolExpr> condition = ExprBuilder::buildBoolExpr(tree, locationReferences, variableComparisons);
        // cout << "out of ExprBuilder" << endl;
        bool supress = string2bool(toks[0]);
        int from = stoi(toks[1]);
        int to = stoi(toks[2]);
        // cout << "line: " << line << endl;
        // cout << "supress: " << supress << endl;
        // cout << "making currentMove" << endl;

        // cout << "currentMove made" << endl;
        if (!contains(moves, from)) {
            // cout << "no issue here" << endl;
            moves[from] = vector<DSFAMove>();
        }
        moves.at(from).emplace_back(DSFAMove(condition, supress, to, contains(finalStates, to), *locationReferences, *variableComparisons));
        // cout << "pushed to vector" << endl;

        // delete parser;
        delete tokens;
        delete lexer;
        delete stream;
        // cout << "going to next iteration" << endl;
    }
    // cout << "out of while loop in SFAPARSER parseLocalStateMachine" << endl;
    return std::unique_ptr<DSFA>(new DSFA(startState, finalStates, moves, vector<string>(), variableList));
}

#endif
