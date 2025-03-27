#include <iostream>
#include <regex>
#include <map>
#include <stack>
#include <string>
#include <vector>
#include <unordered_map>
using namespace std;


string split_left(string line) {
    string result;
    for (int i = 0; i < line.length(); i++) {
        if (line[i] != '=') result += line[i];
        else return result;
    }
}


string split_right(string line) {
    string result;
    bool after_is = false;
    for (int i = 1; i < line.length(); i++) {
        if (line[i - 1] == '=') after_is = true;
        if (after_is) result += line[i];
    }
    return result;
}


int main() {
    string line;
    regex open_brace("\\{"), close_brace("\\}"), variable_number("^([a-z_]+[0-9]*)=(-?[0-9]+)$"), variable_variable("^([a-z_]+[0-9]*)=([a-z_]+[0-9]*)$");
    unordered_map <string, stack <string>> variables;
    stack <vector <string>> vectors;
    while (getline(cin, line)) {
        // {
        if (regex_match(line, open_brace)) {
            vectors.emplace();
        }
        // }
        else if (regex_match(line, close_brace)) {
            vector <string> prev;
            prev = vectors.top();
            vectors.pop();
            for (int i = 0; i < prev.size(); i++) if (!variables[prev[i]].empty()) variables[prev[i]].pop();

        }
        // <variable>=<number>
        else if (regex_match(line, variable_number)) {
            string left = split_left(line);
            string right = split_right(line);
            if (variables.find(left) == variables.end()) {
                stack <string> value;
                variables[left] = value;
            }
            variables[left].push(right);
            if (!vectors.empty()) vectors.top().push_back(left);
        }
        // <variable1>=<variable2>
        else if (regex_match(line, variable_variable)) {
            string left = split_left(line);
            string right = split_right(line);
            if (variables.find(right) == variables.end()) {
                variables[left].push("0");
            }
            else {
                if (!variables[right].empty()) variables[left].push(variables[right].top());
                else variables[left].push("0");
                if (!vectors.empty()) vectors.top().push_back(left);
            }
            cout << variables[left].top() << endl;
        }
    }
    return 0;
}
