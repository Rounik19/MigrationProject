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
 
#define fi(i,a,b) for(int i=a;i<b;i++)
#define fd(i,a,b) for(int i=a;i>=b;i--)
#define vi vector<int>
#define vvi vector<vi>
#define pii pair<int,int>
#define vpii vector<pii>
#define endl '\n'
#define inf 2e5 + 5
#define pb push_back
#define all(v) v.begin(),v.end()
#define sva(v) sort(all(v))
 
class SegTree{
    // SEGMENT TREE
                            // TYPES:
    // 0 : sum seg tree : to answer querries of type sum(a[l....r])
    // 1 : min seg tree : to answer querries of type min(a[l....r])
    // 2 : max seg tree : to answer querries of type max(a[l....r])
    // 3 : gcd seg tree : to answer querries of type gcd(a[l....r])
    // 4 : xor seg tree : to answer querries of type xor(a[l....r])
 
    vi a,seg,lazy;
    int n;
    public :
    SegTree(){}
    SegTree(vi arr){
        n=arr.size();
        a.resize(n);
        seg.resize(5*n+1);lazy.resize(5*n+1);
        a=arr;
    }
    int basecase(int i){
        if(i==0 or i==3 or i==4) return 0;
        if(i==1) return inf;
        if(i==2) return -inf;
        return 0;
    }
    int func(int val1,int val2,int i){
        if(i==0) return val1+val2; // sum seg-tree
        if(i==1) return min(val1,val2); // min seg-tree
        if(i==2) return max(val1,val2); // max seg-tree
        // if(i==3) return __gcd(val1,val2); // gcd seg_tree
        if(i==4) return val1^val2; // xor seg-tree
        return 0;
    }
    // call function as build(0,0,highest index of array a,i)
    void buildsegtree(int ind,int low,int high,int type){ // i=type: i=0 => sum , i=1 => min , i=2 => max
        if(low==high){
            seg[ind]=a[low];
            return;
        }
        int mid=(low+high)>>1;
        buildsegtree(2*ind+1,low,mid,type);
        buildsegtree(2*ind+2,mid+1,high,type);
        seg[ind]=func(seg[2*ind+1],seg[2*ind+2],type);
    }
    // call function as querry(0,0,highest index of array a,left idx,right idx,i)
    int querry(int ind,int low,int high,int l,int r,int type){ // querry from [l...r](l and r in 0 based indexing)
        if(lazy[ind]!=0){
            int amt=high-low+1;
            if(type) amt=1;
            seg[ind]+=amt*lazy[ind];
            if(low!=high){
                lazy[2*ind+1]+=lazy[ind];
                lazy[2*ind+2]+=lazy[ind];
            }
            lazy[ind]=0;
        }
        if(low>=l and high<=r) return seg[ind];
        if(high<l or low>r or l>r) return basecase(type);
 
        int mid=(low+high)/2;
        int left=querry(2*ind+1,low,mid,l,r,type);
        int right=querry(2*ind+2,mid+1,high,l,r,type);
        return func(left,right,type);
    }
    // call function as pointupdate(0,0,highest index of array a,pos,val)
    void do_point_update(int ind,int low,int high,int node,int val,int type){ // pointupdate : a[node]+=val
        if(low==high)
            seg[ind]+=val;        
        else{
            int mid=(low+high)>>1;
            if(node<=mid and node>=low) do_point_update(2*ind+1,low,mid,node,val,type);
            else do_point_update(2*ind+2,mid+1,high,node,val,type);
            seg[ind]=func(seg[2*ind+1],seg[2*ind+2],type);
        }
    }
    void do_range_update(int ind,int low,int high,int l,int r,int val,int type){
        if(lazy[ind]!=0){
            int amt=high-low+1;
            if(type) amt=1;
            seg[ind]+=amt*lazy[ind];
            if(low!=high){
                lazy[2*ind+1]+=lazy[ind];
                lazy[2*ind+2]+=lazy[ind];
            }
            lazy[ind]=0;
        }
        if(high<l or r<low or l>r)
            return;
        if(l<=low and high<=r){
            int amt=(high-low+1);
            if(type) amt=1;
            seg[ind]+=amt*val;
            if(low!=high){
                lazy[2*ind+1]+=val;
                lazy[2*ind+2]+=val;
            }
            return;
        }
        int mid=(low+high)>>1;
        do_range_update(2*ind+1,low,mid,l,r,val,type);
        do_range_update(2*ind+2,mid+1,high,l,r,val,type);
        seg[ind]=func(seg[2*ind+1],seg[2*ind+2],type);
    }
    int get(int l,int r,int type){ // querry(left idx , right idx , type)
        l=max(l,0ll),r=min(r,n-1);
        return querry(0,0,n-1,l,r,type);
    }
    void pointupdate(int idx,int val,int type){ // a[idx]=val
        int prev_val=get(idx,idx,type);
        do_point_update(0,0,n-1,idx,val-prev_val,type);
    }
    void rangeupdate(int l,int r,int val,int type){ // a[i] = a[i] + val ∀ i ∈ [l,r]
        l=max(l,0ll),r=min(r,n-1);
        do_range_update(0,0,n-1,l,r,val,type);
    }
    void build(int type){ // build(type)
        buildsegtree(0,0,n-1,type);
    }
};

vector<vector<vector<int> > > ST;

void point_update(int i, int j, int val){
    // dp[i][j] = val;
    // we have to update ST
    
}

int get(int j, int l, int r){
    int len = r - l + 1;
    int N = 1;
    while((1 << N) <= len)
        N++;
    N--;
    int ans = -inf;
    ans = max(ans, ST[j][l][N]);
    ans = max(ans, ST[j][r - (1 << N) + 1][N]);
    return ans;
}

void solve(){
    int n, m, k; cin >> n >> m >> k;
    vector<int> start[n + 1], end[n + 1];
    for(int i = 0; i < m; i++){
        int l, r; cin >> l >> r;
        start[l].push_back(l);
        end[r].push_back(l);
    }
    vector<vector<int> > dp(n + 1, vector<int>(k + 1, -inf));
    dp[0][0] = 0;
    // SegTree segtrees[k + 1];
    // for(int j = 0; j <= k; j++){
    //     vector<int> arr(n + 1);
    //     for(int i = 0; i < n; i++)
    //         arr[i] = dp[i][j];
    //     segtrees[j] = SegTree(arr);
    //     segtrees[j].build(2);
    // }
    int N = 1;
    while((1 << N) <= n)
        N++;
    ST = vector<vector<vector<int> > >(k + 1, vector<vector<int> >(n + 1, vector<int>(N, -inf)));
    for(int j = 0; j <= k; j++){
        for(int i = 0; i <= n; i++)
            ST[j][i][0] = dp[i][j];
        for(int l = 1; l < N; l++)
            for(int i = 0; i <= n; i++){
                ST[j][i][l] = ST[j][i][l - 1];
                if(i + (1 << (l - 1)) <= n)
                    ST[j][i][l] = max(ST[j][i][l - 1], ST[j][i + (1 << (l - 1))][l - 1]);
            }
    }
    vector<int> freq(n + 1, 0);
    set<int, greater<int> > open;
    int ans = 0;
    for(int i = 1; i <= n; i++){
        for(auto x : start[i]){
            freq[x]++;
            open.insert(x);
        }
        for(int j = 0; j <= k; j++){
            int l = i - 1, r = i - 1;
            int cnt = freq[i];
            auto it = open.begin();
            if(cnt > 0)
                it++;
            while(cnt <= j and it != open.end()){
                l = *it;
                if(l <= r){
                    // int val = segtrees[j - cnt].get(l, r, 2);
                    int val = get(j - cnt, l, r);
                    dp[i][j] = max(dp[i][j], val + 1);
                    ans = max(ans, dp[i][j]);
                    segtrees[j].pointupdate(i, dp[i][j], 2);
                }
                r = l - 1;
                cnt += freq[l];
                it++;
            }
            if(cnt <= j){
                int l = 0;
                // int val = segtrees[j - cnt].get(l, r, 2);
                int val = get(j - cnt, l, r);
                dp[i][j] = max(dp[i][j], val + 1);
                ans = max(ans, dp[i][j]);
                segtrees[j].pointupdate(i, dp[i][j], 2);
            }
        }
        for(auto x : end[i]){
            freq[x]--;
            if(freq[x] == 0)
                open.erase(x);      
        }
    }
    cout << ans << endl;
} 

int32_t main(){
    ios::sync_with_stdio(0);
    cin.tie(0);
    int t;
    cin>>t;
    while(t--)
        solve();
}