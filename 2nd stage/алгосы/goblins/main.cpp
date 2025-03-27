#include <iostream>
#include <deque>
using namespace std;

int main() {
    int amount;
    cin >> amount;
    deque <int> head, tail;
    for (int i = 0; i < amount; i++) {
        char operation;
        int goblin;
        cin >> operation;
        if (operation != '-') cin >> goblin;
        if (operation == '+') {
            if (head.size() - tail.size() == 0) {
                tail.push_back(goblin);
                head.push_back(tail.front());
                tail.pop_front();
            }
            else if (head.size() - tail.size() == 1) tail.push_back(goblin);
        }
        else if (operation == '*') {
            if (head.size() - tail.size() == 0) head.push_back(goblin);
            else if (head.size() - tail.size() == 1) tail.push_front(goblin);
        }
        else if (operation == '-') {
            if (head.size() - tail.size() == 0) {
                cout << head.front() << endl;
                head.pop_front();
                head.push_back(tail.front());
                tail.pop_front();
            }
            else if (head.size() - tail.size() == 1) {
                cout << head.front() << endl;
                head.pop_front();
            }
        }
    }
}
