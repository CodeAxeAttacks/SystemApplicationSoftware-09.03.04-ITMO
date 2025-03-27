#include <iostream>
#include <vector>
#include <stack>
using namespace std;

int main() {
    int pigs, cycles = 0;
    cin >> pigs;
    vector <int> graph(pigs + 1);
    vector <bool> visited(pigs + 1, false);
    vector <bool> in_stack(pigs + 1, false);
    for (int i = 1; i <= pigs; i++) cin >> graph[i];
    for (int i = 1; i <= pigs; i++) {
        if (!visited[i]) {
            stack <int> s;
            s.push(i);
            while (!s.empty()) {
                int node = s.top();
                if (!visited[node]) {
                    visited[node] = true;
                    in_stack[node] = true;
                }
                else {
                    s.pop();
                    in_stack[node] = false;
                    continue;
                }
                if (!visited[graph[node]]) s.push(graph[node]);
                else if (in_stack[graph[node]]) cycles++;
            }
        }
    }
    cout << cycles;
}
