package com.company;

import java.util.ArrayList;
import java.util.List;



//decoratorova cast







// printing cast
interface printingStrategy {
    public String printLine(String s, int p);
}

class printNormal implements printingStrategy {
    @Override
    public String printLine(String s, int p) {
        return s + " (" + p + ")";
    }
}

class printReverse implements printingStrategy {
    @Override
    public String printLine(String s, int p) {
        return "(" + p + ") " + s;
    }
}

class printAllCaps implements printingStrategy {
    @Override
    public String printLine(String s, int p) {
        return s.toUpperCase() + " (" + p + ")";
    }
}

//taskmanager cast

interface Task {
    public int priority();

    public List<Task> subtasks();

    public String toString();

    public void printAll(int depth);
}

class Category implements Task {
    public int getPriority() {
        return priority;
    }

    private int priority;
    private String name;
    private List<Task> subtasks = new ArrayList<>();
    public printingStrategy strategy;

    public int priority() {
        return this.priority;
    }

    public Category(String name, int priority) {
        this.priority = priority;
        this.strategy = new printNormal();
        this.name = name;
    }

    public Category(String name, int priority, List<Task> subtasks) {
        this.priority = priority;
        this.name = name;
        this.subtasks = subtasks;
        this.strategy = new printNormal();
    }

    public void addSubtask(Task t) {
        int n = t.priority();
        for (int i = 0; i < this.subtasks.size(); i++) {
            if (this.subtasks.get(i).priority() < n) {
                this.subtasks.add(i, t);
                return;
            }
        }
        this.subtasks.add(t);

    }

    public void setPrintStrategy(printingStrategy s) {
        this.strategy = s;
    }

    public List<Task> subtasks() {
        List<Task> total = new ArrayList<Task>();
        for (Task t : subtasks) {
            total.add(t);
            for (Task x : t.subtasks()) total.add(x);
        }
        return total;
    }

    public void printRoot() {
        System.out.println(strategy.printLine(this.name, this.priority));
        for (Task x : subtasks) {
            x.printAll(1);
        }
    }

    public void printAll(int depth) {
        System.out.println("-> " + strategy.printLine(this.name, this.priority));
        for (Task x : subtasks) {
            for (int i = 0; i < depth; i++) {
                System.out.print("\t");
            }
            x.printAll(depth + 1);
        }
    }

    @Override
    public String toString() {
        return strategy.printLine(this.name, this.priority);
    }
}

class Entry implements Task {
    public List<Task> subtasks() {
        return null;
    }

    private int priority;
    private String name;
    public printingStrategy strategy;

    public Entry(String name, int priority) {
        this.priority = priority;
        this.name = name;
    }

    @Override
    public int priority() {
        return this.priority;
    }

    public void printAll(int depth) {
        System.out.println("-> " + this.name + "(" + this.priority + ")");
    }

    @Override
    public String toString() {
        return strategy.printLine(this.name, this.priority);
    }
}

public class Main {
    public static void main(String[] args) {
        Category skola = new Category("Skola", 10);
        Category unix = new Category("Unix", 9);
        Category programovanie = new Category("Programovanie 3", 9);
        Entry zapis = new Entry("Zapisat sa do zoznamu", 9);
        Entry prednaska = new Entry("Pozriet prednasku", 9);
        Entry uloha = new Entry("Odovzdat domacu ulohu", 7);
        Entry opakovanie = new Entry("Preopakovat generics", 3);
        skola.addSubtask(unix);
        skola.addSubtask(programovanie);
        unix.addSubtask(zapis);
        programovanie.addSubtask(prednaska);
        programovanie.addSubtask(uloha);
        programovanie.addSubtask(opakovanie);
        skola.printRoot();
    }
}
