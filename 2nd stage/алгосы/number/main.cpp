#include <iostream>
#include <vector>
using namespace std;


int main() {
    string number;
    vector <string> parts;
    while (cin >> number) parts.push_back(number);
    int steps = parts.size();
    while (steps != 0) {
        for (int i = 0; i < parts.size() - 1; i++) if (parts[i] + parts[i + 1] < parts[i + 1] + parts[i]) swap(parts[i], parts[i + 1]);
        steps--;
    }
    for (string i : parts) cout << i;
}
