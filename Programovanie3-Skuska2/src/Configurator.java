import java.io.*;
import java.util.*;

/**
 * Created by Dominik on 19/01/2017.
 */

class Config {
    Optional<Node> root = Optional.empty();
    Optional<ArrayList> actions = Optional.empty();
    Optional<HashMap<String, Integer>> evaluation = Optional.empty();

    public Config(Optional<Node> root, Optional<ArrayList> actions, Optional<HashMap<String, Integer>> evaluation) {
        this.root = root;
        this.actions = actions;
        this.evaluation = evaluation;
    }

    public Config() {
    }

    public ArrayList<String> getActions() {
        return actions.orElse( new ArrayList<String>(Arrays.asList("print")));
    }
    public HashMap<String, Integer> getMap(){
        return evaluation.orElse( new HashMap<String, Integer>());
    }
}

public class Configurator {
    Stack<Node> addedElements = new Stack<>();
    Config config = new Config();

    ParState state;

    //states
    interface ParState {
        void process(String line);
    }

    class ExpState implements ParState {
        @Override
        public void process(String line) {
            if (line.charAt(0) == '"') {
                addedElements.push(new VariableNode(line.replaceAll("\"", "")));
            } else if (line.charAt(0) == '*') {
                MultiplyNode m = new MultiplyNode(addedElements.pop(), addedElements.pop());
                addedElements.push(m);
            } else if (line.charAt(0) == '+') {
                PlusNode p = new PlusNode(addedElements.pop(), addedElements.pop());
                addedElements.push(p);
            } else {
                int value = Integer.parseInt(line);
                ValueNode node = new ValueNode(value);
                addedElements.push(node);
            }
        }
    }

    class ActState implements ParState {
        @Override
        public void process(String line) {
            config.actions.get().add(line);
        }
    }

    class EvalState implements ParState {
        @Override
        public void process(String line) {
            line = line.replaceAll("\"", "");
            Scanner scanner = new Scanner(line);
            config.evaluation.get().put(scanner.next(), scanner.nextInt());
        }
    }

    Config readInput() {
        BufferedReader br = null;
        state = line -> System.out.println("\"" + line + "\"");
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) continue;
                if (line.charAt(0) == '[') {
                    if (line.toLowerCase().equals("[action]")) {
                        config.actions = Optional.of(new ArrayList());
                        state = new ActState();
                    } else if (line.toLowerCase().equals("[expression]")) {
                        state = new ExpState();
                    } else if (line.toLowerCase().equals("[evaluation]")) {
                        config.evaluation = Optional.of(new HashMap<>());
                        state = new EvalState();
                    }
                } else {
                    state.process(line);
                }
            }
            br.close();
            config.root = Optional.of(addedElements.pop());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}


