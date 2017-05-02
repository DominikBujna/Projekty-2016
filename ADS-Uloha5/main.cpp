#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <queue>
#include <numeric>

using namespace std;

int main() {
    int m, p, s, maxDays;
    cin >> m >> p>>s;
    int E[m][p];
    if (m > p) {
        maxDays = p;
    } else {
        maxDays = m;
    }
    int poradie [m][maxDays];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < p; j++) {
            cin >> E[i][j];
        }
    }
    for (int i = 0; i < m; i++) {
        priority_queue<pair<int, int> > q;
        for (int j = 0; j < p; j++) {
            pair<int, int> meh = pair<int, int>(1000001 - E[i][j], j);
            q.push(meh);
        }
        for (int j = 0; j < maxDays; j++) {
            int ki = q.top().second;
            poradie[i][j] = ki;
            q.pop();
        }
    }
    int k=maxDays-1;
    int dnovyrozdiel =2 * k * (k + 1)*(2 * k + 1) / 6 + s * k * (k + 1) / 2;
    int myints[m];
    for (int i=0; i < m; i++) {
        myints[i] = i;
    }
    int minimum = INT_MAX;
    vector<int> bestzostava;
    vector<int> zostava;
    vector<int> bestprisery;
    vector<int> prisery;
    bool zivaprisera[p];
    for (int i=0; i < p; i++) {
        zivaprisera[i] = true;
    }
    std::sort(myints, myints + m);
    do {
        for (int i=0; i < p; i++) {
            zivaprisera[i] = true;
        }
        int suma = 0;
        zostava.clear();
        prisery.clear();
        for (int i = 0; i < maxDays; i++) {
            for (int j = 0; j < maxDays; j++) {
                if (zivaprisera[poradie[myints[i]][j]]) {
                    zivaprisera[poradie[myints[i]][j]] = false;
                    suma += E[myints[i]][poradie[myints[i]][j]];
                    zostava.push_back(myints[i]);
                    prisery.push_back(poradie[myints[i]][j]);
                    break;
                }
            }

        }
        if(suma<minimum){
            bestprisery.clear();
            bestzostava.clear();
            minimum=suma;
            for (int i=0;i<maxDays;i++){
                bestzostava.push_back(zostava[i]);
                bestprisery.push_back(prisery[i]);
            }
        }

    } while (std::next_permutation(myints, myints + m));

    cout<<maxDays<<endl<<minimum+dnovyrozdiel<<endl;
    for (int i=0;i<maxDays;i++){
        cout<<i<<" "<<bestzostava[i]<<" "<<bestprisery[i]<<endl;
    }
    return 0;
}
