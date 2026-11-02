package parser;

import java.util.Arrays;
import java.util.Optional;

public enum Operator {

    ADD('+', 1, 2),
    SUBTRACT('-', 1, 2),
    MULTIPLY('*', 2, 2),
    DIVIDE('/', 2, 2),
    UNARY_SUBTRACT('~', 3, 1);

    private final char symbol;
    private final int priority;
    private final int arity;

    Operator(char symbol, int priority, int arity) {
        this.symbol = symbol;
        this.priority = priority;
        this.arity = arity;
    }

    public int priority() {
        return priority;
    }

    public int arity() {
        return arity;
    }

    public char symbol() {
        return symbol;
    }

    public static Optional<Operator> fromInputSymbol(char symbol) {
        return Arrays.stream(values())
                .filter(operator -> operator.symbol == symbol
                        && operator.arity() == 2)
                .findFirst();
    }

    public static Optional<Operator> fromToken(String token) {
        return Arrays.stream(values())
                .filter(operator -> token.length() == 1
                        && operator.symbol == token.charAt(0))
                .findFirst();
    }
}
