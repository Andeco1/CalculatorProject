package parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.lang.String.valueOf;
import static parser.Operator.SUBTRACT;
import static parser.Operator.fromInputSymbol;
import static parser.Parenthesis.CLOSE;
import static parser.Parenthesis.OPEN;

public final class ParserImpl
        implements Parser {

    @Override
    public List<String> parse(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Выражение не должно быть пустым");
        }

        List<String> output = new ArrayList<>();
        Deque<Operator> operators = new ArrayDeque<>();
        Deque<Integer> parenthesisGroups = new ArrayDeque<>();

        boolean expectingOperand = true;
        int currentIdx = 0;

        while (currentIdx < expression.length()) {
            char current = expression.charAt(currentIdx);

            if (isWhitespace(current)) {
                currentIdx++;
                continue;
            }

            if (isDigit(current)) {
                if (!expectingOperand) {
                    throw new IllegalArgumentException(
                            "Пропущен оператор (позиция " + currentIdx + ")"
                    );
                }

                currentIdx = readNumber(expression, currentIdx, output);
                expectingOperand = false;
                continue;
            }

            if (current == OPEN.symbol()) {
                if (!expectingOperand) {
                    throw new IllegalArgumentException(
                            "Пропущен оператор перед '"
                                    + OPEN.symbol()
                                    + "' (позиция " + currentIdx + ")"
                    );
                }

                parenthesisGroups.push(operators.size());
                currentIdx++;
                continue;
            }

            if (current == CLOSE.symbol()) {
                if (expectingOperand) {
                    throw new IllegalArgumentException(
                            "Пропущен операнд перед '"
                                    + CLOSE.symbol()
                                    + "' (позиция " + currentIdx + ")"
                    );
                }

                if (parenthesisGroups.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Неожиданная '"
                                    + CLOSE.symbol()
                                    + "' (позиция " + currentIdx + ")"
                    );
                }

                moveOperators(output, operators, parenthesisGroups.pop());
                currentIdx++;
                continue;
            }

            if (current == SUBTRACT.symbol()
                    && expectingOperand) {
                operators.push(Operator.UNARY_SUBTRACT);
                currentIdx++;
                continue;
            }

            Optional<Operator> parsedOperator =
                    fromInputSymbol(current);

            if (parsedOperator.isEmpty()) {
                throw new IllegalArgumentException(
                        "Неожиданный символ '" + current
                                + "' (позиция " + currentIdx + ")"
                );
            }

            if (expectingOperand) {
                throw new IllegalArgumentException(
                        "Пропущен левый операнд у оператора '"
                                + current + "'"
                );
            }

            currentIdx = pushBinaryOperator(
                    parsedOperator.get(),
                    currentIdx,
                    output,
                    operators,
                    parenthesisGroups
            );
            expectingOperand = true;
        }

        if (!parenthesisGroups.isEmpty()) {
            throw new IllegalArgumentException(
                    "Не хватает закрывающей скобки"
            );
        }

        if (expectingOperand) {
            throw new IllegalArgumentException(
                    "Выражение не может заканчиваться оператором"
            );
        }

        moveOperators(output, operators, 0);

        return output;
    }

    private static int readNumber(
            String expression,
            int currentIdx,
            List<String> output
    ) {
        int start = currentIdx;

        while (currentIdx < expression.length()
                && isDigit(expression.charAt(currentIdx))) {
            currentIdx++;
        }

        output.add(expression.substring(start, currentIdx));

        return currentIdx;
    }

    private static int pushBinaryOperator(
            Operator currentOperator,
            int currentIdx,
            List<String> output,
            Deque<Operator> operators,
            Deque<Integer> parenthesisGroups
    ) {
        int groupStart = parenthesisGroups.isEmpty()
                ? 0
                : parenthesisGroups.peek();

        while (operators.size() > groupStart
                && operators.getFirst().priority()
                >= currentOperator.priority()) {
            output.add(
                    valueOf(operators.pop().symbol())
            );
        }

        operators.push(currentOperator);

        return currentIdx + 1;
    }

    private static void moveOperators(
            List<String> output,
            Deque<Operator> operators,
            int stopAtSize
    ) {
        while (operators.size() > stopAtSize) {
            output.add(
                    valueOf(operators.pop().symbol())
            );
        }
    }
}
