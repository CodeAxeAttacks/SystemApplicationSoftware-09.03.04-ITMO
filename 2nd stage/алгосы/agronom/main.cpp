#include <iostream>
#include <vector>
using namespace std;

int main() {
    int power_of_flowers, longest_party = 0;
    cin >> power_of_flowers;
    vector <int> flowers_type(power_of_flowers);
    vector <int> triplets;
    for (int i = 0; i < power_of_flowers; i++) cin >> flowers_type[i];
    for (int i = 0; i < power_of_flowers - 2; i++) if (flowers_type[i + 2] == flowers_type[i + 1] && flowers_type[i + 1] == flowers_type[i]) triplets.push_back(i + 1);
    if (size(triplets) >= 2 && size(triplets) != power_of_flowers - 2) {
        for (int i = 0; i < size(triplets) - 1; i++) {
            int current_value = triplets[i + 1] - triplets[i];
            if (current_value > longest_party) longest_party = current_value;
        }
        for (int i = 0; i < size(triplets) - 1; i++) {
            if (triplets[i + 1] - triplets[i] == longest_party) {
                if (longest_party < triplets[0]) cout << 1 << " " << triplets[0] + 1;
                else if (longest_party < power_of_flowers - triplets[size(triplets) - 1]) cout << triplets[size(triplets) - 1] + 1 << " " << power_of_flowers;
                else cout << triplets[i] + 1 << " " << triplets[i + 1] + 1;
                break;
            }
        }
    }
    else if (size(triplets) == 1) {
        if (power_of_flowers != 3 && power_of_flowers - triplets[0] - 1 > triplets[0]) cout << triplets[0] + 1 << " " << power_of_flowers;
        else if (power_of_flowers != 3 && power_of_flowers - triplets[0] - 1 <= triplets[0]) cout << 1 << " " << triplets[0] + 1;
        else cout << 1 << " " << 2;
    }
    else if (power_of_flowers == 0 && size(flowers_type) == 0) cout << "1 0";
    else if (power_of_flowers == 0 && size(flowers_type) == 1) cout << "1 1";
    else {
        if (power_of_flowers == 2) cout << 1 << " " << 2;
        else if (size(triplets) == power_of_flowers - 2) cout << 1 << " " << 2;
        else cout << 1 << " " << power_of_flowers;
    }
    return 0;
}
