#include <iostream>
#include <map>
#include <vector>
using namespace std;

int main() {
    int cells_amount, blocks_amount;
    cin >> cells_amount >> blocks_amount;
    map <int, int> blocks;
    multimap <int, int> blocks_by_size;
    vector <pair <int, int>> allocations(blocks_amount + 1, {-1, -1});
    blocks[1] = cells_amount;
    blocks_by_size.emplace(cells_amount, 1);
    for (int i = 1; i <= blocks_amount; i++) {
        int query;
        cin >> query;
        if (query > 0) {
            int el_add = query;
            auto it = blocks_by_size.lower_bound(el_add);
            bool found = false;
            while (it != blocks_by_size.end() && !found) {
                int size = it->first;
                int start = it->second;
                if (start == 1 || blocks.find(start - 1) == blocks.end()) {
                    cout << start << endl;
                    allocations[i] = {start, el_add};
                    blocks.erase(start);
                    auto temp_it = it;
                    it++;
                    blocks_by_size.erase(temp_it);
                    if (size > el_add) {
                        blocks[start + el_add] = size - el_add;
                        blocks_by_size.emplace(size - el_add, start + el_add);
                    }
                    found = true;
                }
                else it++;
            }
            if (!found) cout << -1 << endl;
        }
        else {
            int el_del = -query;
            if (allocations[el_del].first != -1) {
                int start = allocations[el_del].first;
                int size = allocations[el_del].second;
                int end = start + size;
                auto find_it = blocks.find(start);
                if (find_it != blocks.end()) blocks.erase(find_it);
                for (auto it = blocks_by_size.lower_bound(size); it != blocks_by_size.upper_bound(size); it++) {
                    if (it->second == start) {
                        blocks_by_size.erase(it);
                        break;
                    }
                }
                auto prev = blocks.lower_bound(start);
                if (prev != blocks.begin()) {
                    prev--;
                    if (prev->first + prev->second == start) {
                        size += prev->second;
                        start = prev->first;
                        for (auto it = blocks_by_size.lower_bound(prev->second); it != blocks_by_size.upper_bound(prev->second); it++) {
                            if (it->second == prev->first) {
                                blocks_by_size.erase(it);
                                break;
                            }
                        }
                        blocks.erase(prev);
                    }
                    else {
                        prev++;
                    }
                }
                auto next = blocks.lower_bound(end);
                if (next != blocks.end() && next->first == end) {
                    size += next->second;
                    for (auto it = blocks_by_size.lower_bound(next->second); it != blocks_by_size.upper_bound(next->second); it++) {
                        if (it->second == next->first) {
                            blocks_by_size.erase(it);
                            break;
                        }
                    }
                    blocks.erase(next);
                }
                blocks[start] = size;
                blocks_by_size.emplace(size, start);
            }
        }
    }
}
