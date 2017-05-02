#include <iostream>
#include <queue>

using namespace std;
struct zjazdovka {
    int from;
    int to;
    int col;
};

struct lokalita {
    unsigned long modrych;
    unsigned long cervenych;
    unsigned long jednacierna;
    unsigned long ciernych;
};

struct toComparator {
    bool operator()(const zjazdovka &struct1, const zjazdovka &struct2) {
        return (struct1.to > struct2.to);
    }
};

int lokalit;
int zjazdoviek;

int main() {

    priority_queue<zjazdovka, vector<zjazdovka>, toComparator> zjazdovky;
    cin >> lokalit;
    cin >> zjazdoviek;
    for (int i = 0; i < zjazdoviek; i++) {
        zjazdovka cesta;
        cin >> cesta.from;
        cin >> cesta.to;
        string farba;
        cin >> farba;
        if (farba == "modra") {
            cesta.col = 0;
        } else if (farba ==
                   "cervena") {
            cesta.col = 1;
        } else if (farba ==
                   "cierna") {
            cesta.col = 2;
        }
        zjazdovky.push(cesta);
    }
    vector<lokalita> lokality;
    for (int i = 0; i < lokalit; i++) {
        lokalita current;
        current.modrych = 0;
        current.cervenych = 0;
        current.jednacierna = 0;
        current.ciernych = 0;
        lokality.push_back(current);
    }
    lokality.at(0).cervenych = 1;
    lokality.at(0).ciernych = 1;
    lokality.at(0).modrych = 1;
    for (int i = 0; i < zjazdoviek; i++) {
        zjazdovka current = zjazdovky.top();
        zjazdovky.pop();
        if (current.to > lokalit) {}
        else {
            switch (current.col) {
                case 0:
                    lokality.at(current.to - 1).modrych += lokality.at(current.from - 1).modrych;
                    lokality.at(current.to - 1).cervenych +=
                            lokality.at(current.from - 1).cervenych;
                    lokality.at(current.to - 1).jednacierna += lokality.at(current.from - 1).jednacierna;

                    lokality.at(current.to - 1).ciernych +=
                            lokality.at(current.from - 1).ciernych;
                    break;
                case 1:
                    lokality.at(current.to - 1).cervenych +=
                            lokality.at(current.from - 1).cervenych;
                    lokality.at(current.to - 1).jednacierna += lokality.at(current.from - 1).jednacierna;
                    lokality.at(current.to - 1).ciernych +=
                            lokality.at(current.from - 1).ciernych;
                    break;
                case 2:
                    lokality.at(current.to - 1).ciernych +=
                            lokality.at(current.from - 1).ciernych;
                    lokality.at(current.to - 1).jednacierna += lokality.at(current.from - 1).cervenych;
                    break;

            }
        }
    }
    cout << lokality.at(lokalit - 1).modrych << endl;
    cout << lokality.at(lokalit - 1).jednacierna + lokality.at(lokalit - 1).cervenych << endl;
    cout << lokality.at(lokalit - 1).ciernych << endl;
    return 0;
}