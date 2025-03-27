#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <tuple>
#include <climits>
using namespace std;

const int INF = INT_MAX;
const int dx[] = {-1, 1, 0, 0};
const int dy[] = {0, 0, -1, 1};
const char dir[] = {'N', 'S', 'W', 'E'};

int main() {
    int lines, columns;
    cin >> lines >> columns;
    int startX, startY, endX, endY;
    cin >> startX >> startY >> endX >> endY;
    startX--; startY--; endX--; endY--;
    vector <string> map(lines);
    for (int i = 0; i < lines; i++) cin >> map[i];
    vector <vector <int>> dist(lines, vector <int>(columns, INF));
    vector <vector <pair <int, int>>> parent(lines, vector <pair <int, int>>(columns, {-1, -1}));
    priority_queue <tuple <int, int, int>, vector <tuple <int, int, int>>, greater <tuple <int, int, int>>> pq;
    dist[startX][startY] = 0;
    pq.push(make_tuple(0, startX, startY));
    while (!pq.empty()) {
        auto [current_cost, x, y] = pq.top();
        pq.pop();
        if (x == endX && y == endY) break;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < lines && ny >= 0 && ny < columns && map[nx][ny] != '#') {
                int new_cost = current_cost + (map[nx][ny] == '.' ? 1 : 2);
                if (new_cost < dist[nx][ny]) {
                    dist[nx][ny] = new_cost;
                    parent[nx][ny] = {x, y};
                    pq.push(make_tuple(new_cost, nx, ny));
                }
            }
        }
    }
    if (dist[endX][endY] == INF) cout << -1 << endl;
    else {
        cout << dist[endX][endY] << endl;
        string path;
        int x = endX, y = endY;
        while (x != startX || y != startY) {
            int px = parent[x][y].first;
            int py = parent[x][y].second;
            for (int i = 0; i < 4; i++) {
                if (x == px + dx[i] && y == py + dy[i]) {
                    path += dir[i];
                    break;
                }
            }
            x = px;
            y = py;
        }
        reverse(path.begin(), path.end());
        cout << path;
    }
}
