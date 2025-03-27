#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

void dfs(int v, const vector <vector <int>>& graph, vector <bool>& visited) {
    visited[v] = true;
    for (int u : graph[v]) if (!visited[u]) dfs(u, graph, visited);
}

int main() {
    int cities, max_cost = 0;
    cin >> cities;
    vector <vector <int>> cost(cities, vector <int> (cities));
    for (int i = 0; i < cities; i++) {
        for (int j = 0; j < cities; j++) {
            cin >> cost[i][j];
            if (i != j) {
                max_cost = max(max_cost, cost[i][j]);
            }
        }
    }
    int left = 0, right = max_cost;
    while (left < right) {
        int mid = (left + right) / 2;
        vector <vector <int>> graph(cities);
        vector <vector <int>> reverseGraph(cities);
        for (int i = 0; i < cities; i++) {
            for (int j = 0; j < cities; j++) {
                if (i != j && cost[i][j] <= mid) {
                    graph[i].push_back(j);
                    reverseGraph[j].push_back(i);
                }
            }
        }
        vector <bool> visited(cities, false);
        dfs(0, graph, visited);
        bool canReach = true;
        for (bool v : visited) {
            if (!v) {
                canReach = false;
                break;
            }
        }
        if (canReach) {
            visited.assign(cities, false);
            dfs(0, reverseGraph, visited);
            for (bool v : visited) {
                if (!v) {
                    canReach = false;
                    break;
                }
            }
        }
        if (canReach) right = mid;
        else left = mid + 1;
    }
    cout << left;
}
