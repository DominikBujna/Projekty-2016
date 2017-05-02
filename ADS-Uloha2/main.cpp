#include <iostream>
#include <stack>
#include <queue>
using namespace std;

bool zmrzlinaTest(string s){

    stack<char> st;
    for(int i = 0; i < s.length(); i++){
        char c = s.at(i);
        if(islower(c)){
            st.push(c);
        }

        if(isupper(c)){
            if(st.empty()){
                return false;
            }
            if(tolower(c) != st.top()){
                return false;
            } else{
                st.pop();
            }
        }
    }
    return true;
}

bool obedTest(string s){
    queue<char> q;
    for(int i = 0; i < s.length(); i++){
        char c = s.at(i);
        if(islower(c)){
            if(q.empty()){
            }else{
                int n = q.size();
                for(int i = 0; i < n; i++){
                    if(q.front() == c){
                        return false;
                    }
                    q.push(q.front());
                    q.pop();
                }
            }
            q.push(c);
        }

        if(isupper(c)){
            if(q.empty()) return false;
            if(tolower(c) != q.front()){
                return false;
            } else {
                q.pop();
            }
        }
    }
    return true;
}
string aka(string s){
    bool zmrzlina = zmrzlinaTest(s);

    bool obed = obedTest(s);

    if(zmrzlina && obed){
        return "obojaka";
    }
    if(zmrzlina){
        return "zmrzlinova";
    }
    if(obed){
        return "obedova";
    }
    return "ziadna";
}

int main() {
    int n;
    cin>>n;
    string s;
    for(int i = 0; i < n; i++){
        cin>>s;
        cout<<aka(s)<<endl;
    }


    return 0;
}