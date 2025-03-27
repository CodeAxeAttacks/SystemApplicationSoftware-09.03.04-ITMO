#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
#include <set>
using namespace std;

int main() {
    int amount, floor, wishes, result = 0;
    cin >> amount >> floor >> wishes;
    vector <int> petes_wishes(wishes);
    unordered_map <int, queue <int>> next_use;
    for (int i = 0; i < wishes; i++) {
        cin >> petes_wishes[i];
        next_use[petes_wishes[i]].push(i);
    }
    set <pair <int, int>> when_use; // Используем пару вместо вектора
    unordered_map <int, int> on_floor;
    for (int i = 0; i < wishes; i++) {
        int car = petes_wishes[i];
        next_use[car].pop();
        if (on_floor.find(car) != on_floor.end()) {
            when_use.erase(make_pair(on_floor[car], car));
            if (!next_use[car].empty()) {
                on_floor[car] = next_use[car].front();
                when_use.insert(make_pair(on_floor[car], car));
            }
            else {
                on_floor.erase(car);
            }
        }
        else {
            if (when_use.size() < floor) {
                int next_time = next_use[car].empty() ? wishes : next_use[car].front();
                when_use.insert(make_pair(next_time, car));
                on_floor[car] = next_time;
                result++;
            }
            else {
                auto it = --when_use.end();
                on_floor.erase((*it).second);
                when_use.erase(it);
                int next_time = next_use[car].empty() ? wishes : next_use[car].front();
                when_use.insert(make_pair(next_time, car));
                on_floor[car] = next_time;
                result++;
            }
        }
    }
    cout << result;
}
