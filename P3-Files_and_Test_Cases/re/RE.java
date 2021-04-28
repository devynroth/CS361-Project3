package re;

import fa.nfa.NFA;

public class RE implements REInterface {
    private String regEx;

    /**
     * Constructor
     * @param regEx Regular expression to be parsed
     */
    public RE(String regEx) {
        this.regEx = regEx;
    }

    /**
     * Get the NFA from a regEx
     * @return NFA based on the given regEx
     */
    @Override
    public NFA getNFA() {
        return null;
    }

    /**
     * TODO: Do some factoring and implement this method
     * @return
     */
    private NFA factor() {
        NFA baseNFA = base();
        return baseNFA;
    }

    /**
     * TODO: Figure out why tf this method is so based and implement it
     * @return
     */
    private NFA base() {
        return new NFA();
    }

    /**
     * Get the next symbol without consuming it
     * @return Next symbol at start of regEx
     */
    private char peek() {
        return regEx.charAt(0);
    }

    /**
     * Remove the next symbol from regEx
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
     * @return Next symbol
     */
    private char next() {
        char c = peek();
        eat(c);
        return c;
    }

    /**
     * Checks if there are more symbols to be parsed
     * @return True if regEx is not empty
     */
    private boolean more() {
        return regEx.length() > 0;
    }
}
