// #include "bits/stdc++.h"
#include <vector>
#include <iostream>
#include <map>
#include <string>
#include <algorithm>
#include <set>
#include <queue>
#include <stack>
#include <cmath>
#include <climits>
#include <cstring>
#include <iomanip>
using namespace std;
#define int long long
#define mp(a, b) make_pair(a, b)

const int N = 2e5 + 5;
int s[N];
vector<int> adj[N];
vector<int> dp[N];

pair<int, int> dfs(int u, int cnt){
    int sons = (int)adj[u].size();
    if(sons == 0)
        return mp(s[u] * cnt, s[u]);
    vector<int> extra;
    int sum = 0;
    for(auto v : adj[u]){
        pair<int, int> p = dfs(v, cnt / sons);
        sum += p.first + s[u] * (cnt/ sons);
        extra.push_back(p.second);
    }
    sort(extra.begin(), extra.end());
    for(int i = 0; i < (cnt % sons); i++)
        sum += extra[i] + s[u];
    int mx = s[u];
    if(sons)
        mx += extra[0];
    return mp(sum, extra[0] + mx);
}

int solve(){   
    int n, k; cin >> n >> k;
    for(int i = 0; i <= n; i++)
        adj[i].clear();
    for(int i = 2; i <= n; i++){
        int x; cin >> x;
        adj[x].push_back(i);
    }
    for(int i = 1; i <= n; i++)
        cin >> s[i];
    return dfs1(1);
} 

int32_t main(){
    ios::sync_with_stdio(0);
    cin.tie(0);
    int t = 1;
    cin >> t;
    while(t--)
        cout << solve() << endl;
}