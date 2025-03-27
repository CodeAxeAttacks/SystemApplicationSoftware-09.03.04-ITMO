#include <iostream>
using namespace std;

int power(int x, int y) {
    if (y == 0) return 1;
    int res = 1;
    for (int i = 0; i < y; i++) res *= x;
    return res;
}

int main() {
    int number_of_pieces, b_power_of_new, c_for_exp, d_power_of_cont, k_day;
    cin >> number_of_pieces >> b_power_of_new >> c_for_exp >> d_power_of_cont >> k_day;
    int a = 3;
    int b = 4;
    int first_day = number_of_pieces * b_power_of_new - c_for_exp;
    int increase = first_day * b_power_of_new - c_for_exp - first_day;
    cout << "increase: " << increase << endl;
    cout << "k_day: " << k_day << endl;
    cout << "Вызов от чисел: " << power(3, 4) << endl;
    cout << "Вызов от констант: " << power(a, b) << endl;
    cout << "Вызов от не констант: " << power(increase, k_day) << endl;
    return 0;
}
