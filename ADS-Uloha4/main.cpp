#include <iostream>
#include <cassert>

using namespace std;
struct node {
    int numOfSmallerNodes;
    int value;
    node *left;
    node *right;
};

struct binarySearchTree {
    node *root;
};

void init(binarySearchTree &t) {
    t.root = NULL;
}

node *newNode(int value) {
    node *v = new node;
    v->value = value;
    v->left = NULL;
    v->right = NULL;
    return v;
}

void insert(binarySearchTree &t, int value) {
    if (t.root == NULL) {
        t.root = newNode(value);
    } else {
        node *v;
        node *parent;

        parent = NULL;
        v = t.root;
        while (v != NULL) {
            parent = v;
            if (value < v->value) {
                v->numOfSmallerNodes++;
                v = v->left;
            } else {
                v = v->right;
            }
        }
        assert(v == NULL && parent != NULL);
        if (value < parent->value) {
            assert(parent->left == NULL);
            parent->left = newNode(value);
        } else {
            assert(parent->right == NULL);
            parent->right = newNode(value);
        }
    }
}

void kthSmallest(node *root, int k) {
    if (root != NULL) {
        node *currentNode = root;
        if (currentNode->numOfSmallerNodes == k) cout << currentNode->value << endl;
        else if (k >= currentNode->numOfSmallerNodes)
            kthSmallest(currentNode->right, k - (currentNode->numOfSmallerNodes + 1));
        else kthSmallest(currentNode->left, k);
    }
}

int main() {
    binarySearchTree t;
    init(t);
    int q;
    cin >> q;
    for (int i = 0; i < q; i++) {
        char action;
        int number;
        cin >> action >> number;
        if (action == 'f') {
            kthSmallest(t.root, number);
        } else {
            insert(t, number);
        }
    }
    return 0;
}