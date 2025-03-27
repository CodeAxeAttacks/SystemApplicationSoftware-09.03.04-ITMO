#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;


bool compare(int a, int b) {
    return a > b;
}


int main() {
    int amount, number_for_discount, result = 0;
    cin >> amount >> number_for_discount;
    vector <int> prices(amount);
    for (int i = 0; i < amount; i++) {
        cin >> prices[i];
        result += prices[i];
    }
    sort(prices.begin(), prices.end(), compare);
    for (int i = number_for_discount; i <= prices.size(); i += number_for_discount) result -= prices[i - 1];
    cout << result;
}
