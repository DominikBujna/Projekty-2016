#include <iostream>
#include <stdio.h>
using namespace std;


struct pismeno {
    char horne;
    char dolne;
};

struct help {
    int horne;
    int dolne;
};
help *najdiPodpostupnst(pismeno *slovo, int dlzkaPrveho, int dlzkaDruheho, int dlzka, int aktPozicia, int x, help *pole) {
    if(aktPozicia < dlzkaDruheho) {
        char znak = slovo[aktPozicia].dolne;
        for(int i=x;i<dlzkaPrveho;i++) {
            if(znak == slovo[i].horne) {
                dlzka = dlzka + 1;
                pole[aktPozicia].dolne = 1;
                pole[i].horne = 1;
                if(i == dlzkaPrveho-1) {
                    pole[dlzkaDruheho].dolne = dlzka;
                    return pole;
                }
                najdiPodpostupnst(slovo,dlzkaPrveho,dlzkaDruheho,dlzka,aktPozicia+1,i+1,pole);
                break;
            } else if(i == dlzkaPrveho-1) {
                najdiPodpostupnst(slovo,dlzkaPrveho,dlzkaDruheho,dlzka,aktPozicia+1,x,pole);
            }
        }
    } else {
        pole[dlzkaDruheho].dolne = dlzka;
        return pole;
    }


}

int celkovaCena(help *pole, int d1, int d2, int cena) {
    cena = 0;
    int kdesomvdolnom = 0;
    int kdesomvhornom = 0;
    while(true) {
        if(pole[kdesomvdolnom].dolne == 1) {
            if(pole[kdesomvhornom].horne == 1) {
                kdesomvdolnom++;
                kdesomvhornom++;
            } else if (pole[kdesomvhornom].horne == 0) {
                kdesomvhornom++;
                cena = cena + 10;
            }
        } else if(pole[kdesomvdolnom].dolne == 0) {
            if(pole[kdesomvhornom].horne == 1) {
                kdesomvdolnom++;
                cena = cena + 11;
            } else if (pole[kdesomvhornom].horne == 0) {
                kdesomvhornom++;
                kdesomvdolnom++;
                cena = cena + 15;
            }
        }

        if(kdesomvdolnom == d2 || kdesomvhornom == d1) break;
    }
    for(int i=kdesomvdolnom;i<d2;i++) {
        cena = cena + 11;
    }
    for(int i=kdesomvhornom;i<d1;i++) {
        cena = cena + 10;
    }

    return cena;

}


int main() {
   // while(true) {
        string s1 = "dsadasd";
        string s2 = "";
        cin >> s1 >> s2;
        int dlzka1 = s1.length();
        int dlzka2 = s2.length();
        int vzdialenost[dlzka1 + 1];
        int line[dlzka1 + 1];
        for (int i = 0; i <= dlzka1; i++) {
            vzdialenost[i] = i * 10;
        }
        for (int j = 1; j <= dlzka2; j++) {
            line[0] = j * 11;
            for (int i = 1; i <= dlzka1; i++) {
                if (s1.at(i - 1) == s2.at(j - 1)) {
                    line[i] = vzdialenost[i - 1];
                } else {
                    int vymaz = line[i - 1] + 10;
                    int vloz = vzdialenost[i] + 11;
                    int vymen = vzdialenost[i - 1] + 15;
                    line[i] = min(min(vloz, vymaz), vymen);
                }
            }
            for (int a = 0; a <= dlzka1; a++) {
                vzdialenost[a] = line[a];
            }
        }
        cout << vzdialenost[dlzka1] << endl;

        int dlzkaPrveho = 0;
        int dlzkaDruheho = 0;
        int maxDlzka = 0;
        pismeno slovo[500];
        pismeno slovoReverz[500];
        int cena = 0;
        for (int i = 0; i < dlzka1; i++) {
            slovo[dlzkaPrveho].horne = s1.at(i);
            dlzkaPrveho++;
        }
        for (int i = 0; i < dlzka2; i++) {
            slovo[dlzkaDruheho].dolne = s2.at(i);
            dlzkaDruheho++;
        }

        help pole[501];
        help najlepsie[501];

        for (int i = 0; i < dlzkaDruheho + 1; i++) {
            najlepsie[i].dolne = 0;
        }
        for (int i = 0; i < dlzkaPrveho; i++) {
            najlepsie[i].horne = 0;
        }

        int x;
        int minCena = 0;
        for (int i = 0; i < dlzkaDruheho; i++) {
            if (dlzkaDruheho - i <= maxDlzka) break;
            x = 0;
            for (int r = 0; r < dlzkaDruheho + 1; r++) {
                pole[r].dolne = 0;
            }
            for (int r = 0; r < dlzkaPrveho; r++) {
                pole[r].horne = 0;
            }
            najdiPodpostupnst(slovo, dlzkaPrveho, dlzkaDruheho, 0, i, x, pole);
            cena = celkovaCena(pole, dlzkaPrveho, dlzkaDruheho, 0);

            if (i == 0) minCena = cena;
            else if (cena < minCena) minCena = cena;

        }
        // pokus o reverz kvoli typu aaaaa,baa
        int helper1 = dlzkaPrveho - 1;
        int helper2 = dlzkaDruheho - 1;
        for (int i = 0; i < dlzkaPrveho; i++) {
            if (helper1 == -1) break;
            slovoReverz[i].horne = slovo[helper1].horne;
            helper1--;
        }

        for (int i = 0; i < dlzkaDruheho; i++) {
            if (helper2 == -1) break;
            slovoReverz[i].dolne = slovo[helper2].dolne;
            helper2--;
        }
        maxDlzka = 0;
        for (int i = 0; i < dlzkaDruheho + 1; i++) {
            najlepsie[i].dolne = 0;
        }
        for (int i = 0; i < dlzkaPrveho; i++) {
            najlepsie[i].horne = 0;
        }

        int cena2 = 0;

        for (int i = 0; i < dlzkaDruheho; i++) {
            if (dlzkaDruheho - i <= maxDlzka) break;
            x = 0;
            for (int r = 0; r < dlzkaDruheho + 1; r++) {
                pole[r].dolne = 0;
            }
            for (int r = 0; r < dlzkaPrveho; r++) {
                pole[r].horne = 0;
            }
            najdiPodpostupnst(slovoReverz, dlzkaPrveho, dlzkaDruheho, 0, i, x, pole);
            cena2 = celkovaCena(pole, dlzkaPrveho, dlzkaDruheho, 0);
            if (cena2 < minCena) minCena = cena2;
        }
        cout<<minCena<<endl;
  //  }
    return 0;
}