#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
using namespace std;

int main() {
    string line;
    cin >> line;
    vector <char> sec;
    for (char i : line) sec.push_back(i);
    map <char, int> alphabet;
    for (char letter = 'a'; letter <= 'z'; ++letter) {
        int weight;
        cin >> weight;
        alphabet[letter] = weight;
    }
    vector <pair <char, int>> temp(alphabet.begin(), alphabet.end());
    sort(temp.begin(), temp.end(), [](const pair <char, int>& a, const pair <char, int>& b) {
        return a.second > b.second;
    });
    vector <char> once, twice;
    for (const pair <char, int>& pair : temp) {
        char letter = pair.first;
        int letter_count = count(sec.begin(), sec.end(), letter);
        if (letter_count == 1) once.push_back(letter);
        else if (letter_count >= 2) {
            twice.push_back(letter);
            for (int i = 0; i < letter_count - 2; i++) once.push_back(letter);
        }
    }
    for (char i : twice) cout << i;
    for (char i : once) cout << i;
    for (int i = twice.size() - 1; i >= 0; i--) cout << twice[i];
}
