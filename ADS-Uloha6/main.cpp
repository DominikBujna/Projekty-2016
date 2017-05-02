#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>

using namespace std;

struct lesson {
    int start;
    int end;
};

struct beginComparator {
    inline bool operator()(const lesson &struct1, const lesson &struct2) {
        return (struct1.start < struct2.start);
    }
};

struct endComparator
{
    bool operator() (lesson const &a, lesson const &b) { return a.end > b.end; }
};

int maxLessons = 0;

int main() {
    std::ios::sync_with_stdio(false);
    int numberOfLessons = 0;
    cin >> numberOfLessons;
    vector<lesson> inputLessons;
    for (int i = 0; i < numberOfLessons; i++) {
        lesson current;
        cin >> current.start;
        cin >> current.end;
        inputLessons.push_back(current);
    }
    sort(inputLessons.begin(), inputLessons.end(), beginComparator());
    priority_queue<lesson, std::vector<lesson>, endComparator> ongoing;
    for (int i = 0; i < numberOfLessons; i++) {
        int parallelLessons = 0;
        int current = inputLessons.at(i).start;

        while (ongoing.size() > 0 && ongoing.top().end < inputLessons.at(i).start) {
            ongoing.pop();
        }
        ongoing.push(inputLessons.at(i));
        parallelLessons = ongoing.size();
        if (parallelLessons > maxLessons) {
            maxLessons = parallelLessons;
        }
    }
    cout << maxLessons << endl;
    return 0;
}