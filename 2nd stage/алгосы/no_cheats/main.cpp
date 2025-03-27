#include <iostream>
#include <vector>
#include <queue>
using namespace std;

int main() {
    int lks, p_lks;
    cin >> lks >> p_lks;
    vector <vector <int>> graph(lks);
    for (int i = 0; i < p_lks; i++) {
        int u, v;
        cin >> u >> v;
        u--; v--;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    vector <int> color(lks, -1);
    queue <int> q;
    for (int i = 0; i < lks; i++) {
        if (color[i] == -1) {
            color[i] = 0;
            q.push(i);
            while (!q.empty()) {
                int node = q.front();
                q.pop();
                for (int neighbor : graph[node]) {
                    if (color[neighbor] == -1) {
                        color[neighbor] = 1 - color[node];
                        q.push(neighbor);
                    }
                    else if (color[neighbor] == color[node]) {
                        cout << "NO";
                        return 0;
                    }
                }
            }
        }
    }
    cout << "YES";
}
