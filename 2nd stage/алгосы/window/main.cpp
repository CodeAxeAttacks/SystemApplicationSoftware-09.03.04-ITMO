#include <iostream>
#include <vector>
#include <deque>
using namespace std;

int main() {
    int line_len, window;
    cin >> line_len; cin >> window;
    vector <int> line(line_len);
    for (int i = 0; i < line_len; i++) cin >> line[i];
    deque <int> data, mins;
    for (int i = 0; i < window; i++) {
        int num = line[i];
        data.push_back(num);
        while (!mins.empty() && mins.back() > num) mins.pop_back();
        mins.push_back(num);
    }
    cout << mins.front() << " ";
    for (int i = window; i < line_len; i++) {
        int num = line[i];
        data.push_back(num);
        if (data.front() == mins.front()) mins.pop_front();
        data.pop_front();
        while (!mins.empty() && mins.back() > num) mins.pop_back();
        mins.push_back(num);
        cout << mins.front() << " ";
    }
}
