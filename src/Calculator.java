import parser.Parser;
import parser.ParserImpl;
import evaluator.Evaluator;
import evaluator.EvaluatorImpl;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class Calculator {

    private static final Calculator DEFAULT_CALCULATOR = new Calculator(
            new ParserImpl(),
            new EvaluatorImpl()
    );

    private final Parser parser;
    private final Evaluator evaluator;

    public Calculator(
            Parser parser,
            Evaluator evaluator
    ) {
        this.parser = requireNonNull(parser);
        this.evaluator = requireNonNull(evaluator);
    }

    public static int evaluate(String expression) {
        return DEFAULT_CALCULATOR.calculate(expression);
    }

    public int calculate(String expression) {
        List<String> parsedExpression = parser.parse(expression);
        return evaluator.evaluate(parsedExpression);
    }
}
