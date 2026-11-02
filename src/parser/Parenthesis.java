package parser;

public enum Parenthesis {

    OPEN('('),
    CLOSE(')');

    private final char symbol;

    Parenthesis(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }
}
