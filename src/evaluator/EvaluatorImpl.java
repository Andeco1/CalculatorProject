package evaluator;

import parser.Operator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import static parser.Operator.fromToken;
import static java.lang.Integer.parseInt;

public final class EvaluatorImpl
        implements Evaluator {

    @Override
    public int evaluate(List<String> expression) {
        Deque<Integer> operands = new ArrayDeque<>();

        for (String token : expression) {
            applyToken(operands, token);
        }

        if (operands.size() != 1) {
            throw new IllegalArgumentException(
                    "Некорректное выражение"
            );
        }

        return operands.pop();
    }

    private void applyToken(
            Deque<Integer> operands,
            String token
    ) {
        Optional<Operator> operator = fromToken(token);

        if (operator.isPresent()) {
            operands.push(
                    applyOperator(operator.get(), operands)
            );
        } else {
            operands.push(parseOperand(token));
        }

    }

    private int parseOperand(String token) {
        try {
            return parseInt(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Некорректный токен: " + token, e
            );
        }
    }

    private int applyOperator(
            Operator operator,
            Deque<Integer> operands
    ) {
        if (operands.size() < operator.arity()) {
            throw new IllegalArgumentException(
                    "Недостаточно операндов для: "
                            + operator.symbol()
            );
        }

        if (operator.arity() == 1) {
            return -operands.pop();
        }

        int right = operands.pop();
        int left = operands.pop();

        return calculate(operator, left, right);
    }

    private int calculate(
            Operator operator,
            int left,
            int right
    ) {
        return switch (operator) {
            case ADD -> left + right;
            case SUBTRACT -> left - right;
            case MULTIPLY -> left * right;

            case DIVIDE -> {
                if (right == 0) {
                    throw new IllegalArgumentException(
                            "Ошибка деления на ноль"
                    );
                }

                yield left / right;
            }

            default -> throw new AssertionError(
                    "Неожиданный оператор в бинарном вычислении: "
                            + operator
            );
        };
    }
}
