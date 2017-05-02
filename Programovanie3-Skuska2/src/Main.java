import java.util.HashMap;
import java.util.Optional;
import java.util.Stack;

interface Node {
    void accept(Visitor v);
}

//nodes
class ValueNode implements Node {
    int value;

    public ValueNode(int value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

class VariableNode implements Node {
    String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

abstract class OperatorNode implements Node {
    Node left;
    Node right;

    public OperatorNode(Node right, Node left) {
        this.left = left;
        this.right = right;
    }
}

class MultiplyNode extends OperatorNode {
    public MultiplyNode(Node right, Node left) {
        super(right, left);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

class PlusNode extends OperatorNode {
    public PlusNode(Node right, Node left) {
        super(right, left);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

//visitor
interface Visitor {
    void visit(ValueNode n);

    void visit(PlusNode n);

    void visit(MultiplyNode n);

    void visit(VariableNode n);
}

class PrintVisitor implements Visitor {
    StringBuilder sb = new StringBuilder();

    public String getString() {
        return sb.toString();
    }


    @Override
    public void visit(ValueNode n) {
        sb.append(n.value);
    }

    @Override
    public void visit(PlusNode n) {
        sb.append("(");
        n.left.accept(this);
        sb.append(" + ");
        n.right.accept(this);
        sb.append(")");
    }

    @Override
    public void visit(MultiplyNode n) {
        sb.append("(");
        n.left.accept(this);
        sb.append(" * ");
        n.right.accept(this);
        sb.append(")");
    }

    @Override
    public void visit(VariableNode n) {
        sb.append(n.name);
    }
}

class EvalVisitor extends SimplifyVisitor {
    Optional<HashMap<String, Integer>> variableValues = Optional.empty();

    public EvalVisitor(Optional<HashMap<String, Integer>> variableValues) {
        this.variableValues = variableValues;
    }

    @Override
    public void visit(VariableNode n) {

        if (variableValues.get().containsKey(n.name)) {
            lastVisited.push(new ValueNode(variableValues.get().get(n.name)));
        } else {
            lastVisited.push(n);
        }
    }
}

class SimplifyVisitor implements Visitor {
    Stack<Node> lastVisited = new Stack<>();

    public Node getTree() {
        return lastVisited.peek();
    }


    public SimplifyVisitor() {
    }


    @Override
    public void visit(ValueNode n) {
        lastVisited.push(n);
    }

    @Override
    public void visit(PlusNode n) {
        n.left.accept(this);
        n.right.accept(this);
        PlusNode newNode = new PlusNode(lastVisited.pop(), lastVisited.pop());
        if (newNode.left instanceof ValueNode && newNode.right instanceof ValueNode) {
            lastVisited.push(new ValueNode(((ValueNode) newNode.left).value + ((ValueNode) newNode.right).value));
        } else {
            lastVisited.push(newNode);
        }
    }

    @Override
    public void visit(MultiplyNode n) {
        n.left.accept(this);
        n.right.accept(this);

        MultiplyNode newNode = new MultiplyNode(lastVisited.pop(), lastVisited.pop());

        if (newNode.left instanceof ValueNode) {
            if (newNode.right instanceof ValueNode) {
                lastVisited.push(new ValueNode(((ValueNode) newNode.left).value * ((ValueNode) newNode.right).value));
            } else if (((ValueNode) newNode.left).value == 1) {
                lastVisited.push(new ValueNode(((ValueNode) newNode.right).value));
            } else if (((ValueNode) newNode.left).value == 0) {
                lastVisited.push(new ValueNode(0));
            } else if (newNode.right instanceof ValueNode) {
                if (((ValueNode) newNode.right).value == 1) {
                    lastVisited.push(new ValueNode(((ValueNode) newNode.left).value));
                } else if (((ValueNode) newNode.right).value == 0) {
                    lastVisited.push(new ValueNode(0));
                }
            }
        } else {
            lastVisited.push(newNode);
        }


//        if (newNode.left instanceof ValueNode && newNode.right instanceof ValueNode) {
//            lastVisited.push(new ValueNode(((ValueNode) newNode.left).value * ((ValueNode) newNode.right).value));
//        } else {
//
//            lastVisited.push(newNode);
//        }
    }

    @Override
    public void visit(VariableNode n) {
        lastVisited.push(n);
    }
}


public class Main {
    public static void main(String[] args) {
        Configurator c = new Configurator();
        Config config = c.readInput();
        for (String s : config.getActions()) {
            if (s.toLowerCase().equals("print")) {
                Visitor v = new PrintVisitor();
                config.root.get().accept(v);
                System.out.println(((PrintVisitor) v).getString());
            } else if (s.toLowerCase().equals("simplify")) {
                Visitor v = new SimplifyVisitor();
                config.root.get().accept(v);
                Visitor pr = new PrintVisitor();
                ((SimplifyVisitor) v).getTree().accept(pr);
                System.out.println(((PrintVisitor) pr).getString());
            } else if (s.toLowerCase().equals("eval")) {
                EvalVisitor v = new EvalVisitor(config.evaluation);
                config.root.get().accept(v);
                Visitor pr = new PrintVisitor();
                v.getTree().accept(pr);
                System.out.println(((PrintVisitor) pr).getString());
            }
        }


    }
}

