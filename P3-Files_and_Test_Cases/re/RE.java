package re;

import fa.nfa.NFA;
import fa.nfa.NFAState;

import java.util.*;

public class RE implements REInterface {
    // regular expression to be converted
    private String regEx;
    // current state ID
    private int currentState;
    // stack to store NFA states
    private final Stack<ArrayList<NFAState>> stateStack = new Stack<ArrayList<NFAState>>();
    // stack to store order of operators
    private final Stack<Character> operatorStack = new Stack<Character>();

    /**
     * Constructor
     *
     * @param regEx regular expression to be converted
     */
    public RE(String regEx) {
        this.regEx = regEx;
        currentState = 0;
    }

    /**
     * Creates an equivalent NFA based on regEx
     *
     * @return Completed NFA object
     */
    @Override
    public NFA getNFA() {
        while (more()) {
            if (inAlphabet(peek()))             // if next char is in the alphabet, create a new transition for it
                addState(next());
            else if (peek() == '*') {           // if the next character is a *, push it onto the stack and operate
                operatorStack.push(next());
                operate();
            } else if (operatorStack.isEmpty()) // if there are currently no operators in the stack, add next char
                operatorStack.push(next());
            else if (peek() == '(')             // if next char is parentheses, add it tot he operator stack
                operatorStack.push(next());
            else if (peek() == ')') {           // if next char is a close parentheses, work backwards until the open parentheses is hit
                while (operatorStack.get(operatorStack.size() - 1) != '(')
                    operate();
                operatorStack.pop();
                next();
            } else {                            // else if there are more operations and the current character takes priority, operate
                while (!operatorStack.isEmpty() && orderOP(peek(), operatorStack.get(operatorStack.size() - 1)))
                    operate();
                operatorStack.push(next());
            }
        }
        while (!operatorStack.isEmpty())    // while there are more operations to be completed, complete them
            operate();
        while (stateStack.size() > 1) {     // combine all states to a single array
            ArrayList<NFAState> n2 = stateStack.pop();
            ArrayList<NFAState> n1 = stateStack.pop();
            // create transition from last state in n1 to first state in n2
            n1.get(n1.size() - 1).addTransition('e', n2.get(0));
            // append n2 to n1
            n1.addAll(n2);
            // push n1 back onto stateStack
            stateStack.push(n1);
        }
        return arrayToNFA(stateStack.pop());
    }

    /**
     * Converts a given ArrayList list to an NFA object
     *
     * @param list ArrayList of all NFAState objects
     * @return Completed NFA object
     */
    private NFA arrayToNFA(ArrayList<NFAState> list) {
        NFA nfa = new NFA();
        LinkedHashSet<Character> alphabet = new LinkedHashSet<>();
        alphabet.add('a');
        alphabet.add('b');
        nfa.addAbc(alphabet);
        nfa.addNFAStates(new LinkedHashSet<>(list));
        nfa.addStartState(list.get(0).getName());
        nfa.addFinalState(list.get(list.size() - 1).getName());
        return nfa;
    }

    /**
     * Checks whether c1 comes first in the order of operations or not
     * @param c1 First character to be checked
     * @param c2 Second character to be checked
     * @return True if c1 is first in the OOP, otherwise false
     */
    private boolean orderOP(char c1, char c2) {
        return c1 != '*' && c1 != '|';
    }

    /**
     * Complete the next operation on top of operatorStack
     */
    private void operate() {
        if (!operatorStack.isEmpty()) {
            switch (operatorStack.pop()) {
                case ('|'):
                    or();
                    break;
                case ('*'):
                    star();
                    break;
                default:
                    System.out.println("Uh oh idk what that symbol is lol");
                    break;
            }
        }
    }

    /**
     * Create a new start and end state to allow for looping of
     * previous states
     */
    private void star() {
        // get array of states on top of stateStack
        ArrayList<NFAState> n = stateStack.pop();
        // create new start and end states
        NFAState start = new NFAState(Integer.toString(currentState++));
        NFAState end = new NFAState(Integer.toString(currentState++));
        // add start and end transitions to allow for looping in state machine
        start.addTransition('e', end);
        start.addTransition('e', n.get(0));
        n.get(n.size() - 1).addTransition('e', end);
        n.get(n.size() - 1).addTransition('e', n.get(0));
        // add start and end to ArrayList
        n.add(0, start);
        n.add(end);
        // push n back onto stateStack
        stateStack.push(n);
    }

    /**
     * Create two new states with empty transitions to and from
     * previous arrays of states to allow for travel down either path
     */
    private void or() {
        // get two character being or'd
        ArrayList<NFAState> n2 = stateStack.pop();
        ArrayList<NFAState> n1 = stateStack.pop();
        // create new start and end states
        NFAState start = new NFAState(Integer.toString(currentState++));
        NFAState end = new NFAState(Integer.toString(currentState++));
        // add new start and end transitions to create a fork allowing travel on either path
        start.addTransition('e', n1.get(0));
        start.addTransition('e', n2.get(0));
        n1.get(n1.size() - 1).addTransition('e', end);
        n2.get(n2.size() - 1).addTransition('e', end);
        // insert new start state into ArrayList
        n1.add(0, start);
        // append all states from n2 to n1
        n1.addAll(n2);
        // append end state to n1
        n1.add(end);
        // push n1 back onto state stack
        stateStack.push(n1);
    }

    /**
     * Add two new states and a transition for a standalone symbol in the regex
     *
     * @param c Symbol from the regEx to be added on a transition
     */
    private void addState(char c) {
        ArrayList<NFAState> s = new ArrayList<>();
        s.add(new NFAState(Integer.toString(currentState++)));
        s.add(new NFAState(Integer.toString(currentState++)));
        s.get(0).addTransition(c, s.get(1));
        stateStack.push(s);
    }

    /**
     * Check whether c is an alphabetical character or an operand
     *
     * @param c Character to be checked
     * @return True if not an operand, false if not
     */
    private boolean inAlphabet(char c) {
        return c != '(' && c != ')' && c != '*' && c != '|';
    }

    /**
     * Get the next symbol without consuming it
     *
     * @return Next symbol at start of regEx
     */
    private char peek() {
        return regEx.charAt(0);
    }

    /**
     * Remove the next symbol from regEx
     *
     * @param c Symbol to be consumed
     */
    private void eat(char c) {
        if (peek() == c)
            regEx = regEx.substring(1);
        else
            throw new RuntimeException("Expected: " + c + "; got: " + peek());
    }

    /**
     * Eats the next symbol and returns it
     *
     * @return Next symbol
     */
    private char next() {
        char c = peek();
        eat(c);
        return c;
    }

    /**
     * Checks if there are more symbols to be parsed
     *
     * @return True if regEx is not empty
     */
    private boolean more() {
        return regEx.length() > 0;
    }
}
