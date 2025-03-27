#include <iostream>
#include <cctype>
#include <stack>
#include <cstdlib>
#include <vector>
using namespace std;


int main() {
    string result, chain;
    getline(cin, chain);
    stack <vector<int>> paths;
    int len_chain = chain.length(), half_len_chain = len_chain / 2, tr_counter = 0, an_counter = 0;
    vector <int> mas_result(half_len_chain);
    for (int i = 0; i < len_chain; i++) {
        vector <int> path(3);
        path[0] = static_cast<int>(chain[i]);
        if (isupper(chain[i])) {
            tr_counter++;
            path[1] = tr_counter;
            path[2] = an_counter;
        }
        else {
            an_counter++;
            path[1] = tr_counter;
            path[2] = an_counter;
        }
        if (paths.empty()) paths.push(path);
        else if (abs(static_cast<int>(paths.top()[0]) - static_cast<int>(path[0])) == 32 && paths.top()[0] != path[0]) {
            if (isupper(chain[i])) mas_result[path[1] - 1] = paths.top()[2];
            else mas_result[paths.top()[1] - 1] = path[2];
            paths.pop();
        }
        else {
            paths.push(path);
        }
    }
    for (int i = 0; i < half_len_chain; i++) {
        if (i != half_len_chain - 1) result += to_string(mas_result[i]) + " ";
        else result += to_string(mas_result[i]);
    }
    if (paths.empty()) cout << "Possible\n" + result;
    else cout << "Impossible";
}
