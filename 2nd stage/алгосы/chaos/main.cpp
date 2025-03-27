#include <iostream>
using namespace std;


int main() {
    int a_start, b_power_of_new, c_for_exp, d_power_of_cont, k_day;
    cin >> a_start >> b_power_of_new >> c_for_exp >> d_power_of_cont >> k_day;
    int number_of_pieces = a_start;
    for (int i = 0; i < k_day; i++) {
        number_of_pieces = number_of_pieces * b_power_of_new - c_for_exp;
        number_of_pieces = (number_of_pieces <= 0) ? 0 : number_of_pieces;
        number_of_pieces = (number_of_pieces >= d_power_of_cont) ? d_power_of_cont : number_of_pieces;
        number_of_pieces = (number_of_pieces * b_power_of_new - c_for_exp == number_of_pieces) ? a_start : number_of_pieces;
        if (number_of_pieces == 0 || number_of_pieces == d_power_of_cont || number_of_pieces == a_start) k_day = i;
    }
    cout << number_of_pieces;
    return 0;
}
