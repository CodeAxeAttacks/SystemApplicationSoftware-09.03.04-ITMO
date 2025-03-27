#include <iostream>
#include <vector>
using namespace std;

int main() {
    int houses, cows;
    cin >> houses >> cows;
    vector <int> coords(houses);
    for (int i = 0; i < houses; i++) cin >> coords[i];
    int left = 0, right = coords[houses - 1] - coords[0], mm_distance = 1;
    while (left <= right) {
        int counter_for_cows = 1, last_cow_coord = coords[0];
        for (int i = 1; i < coords.size(); i++) {
            if (coords[i] - last_cow_coord >= left + (right - left) / 2) {
                counter_for_cows++;
                last_cow_coord = coords[i];
            }
        }
        if (counter_for_cows >= cows) {
            mm_distance = left + (right - left) / 2;
            left = left + (right - left) / 2 + 1;
        }
        else right = left + (right - left) / 2 - 1;
    }
    cout << mm_distance;
}
