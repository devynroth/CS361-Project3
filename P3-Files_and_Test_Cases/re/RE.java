package re;

import fa.nfa.NFA;

import java.util.Stack;

public class RE implements REInterface {
    // incremented and used to name a state each time one is added
    private int currentState;
    private String regEx;

    private Stack<NFA> nfaStack = new Stack<>();

    public RE(String regEx) {
        this.regEx = regEx;
    }

    @Override
    public NFA getNFA() {
        currentState = 0;
        return null;
    }

    private String getNextFactor() {
        return null;
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
