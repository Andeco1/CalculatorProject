public class CalculatorTest {

    public static void main(String[] args) {
        test("2+3*4", 14);
        test("(2+3)*4", 20);
        test("10/2-3", 2);
        test("2*(3+4)-5", 9);
        test(" 2 + 2 ", 4);

        expectException("10/0");
        expectException("2+");
        expectException("(2+3");
        expectException("2+a");

        System.out.println("Все тесты пройдены");
    }

    private static void test(String expression, int expected) {
        int actual = Calculator.evaluate(expression);

        if (actual != expected) {
            throw new AssertionError(
                    expression + " = " + actual
                            + ", ожидалось " + expected
            );
        }

        System.out.println(expression + " = " + actual);
    }

    private static void expectException(String expression) {
        try {
            int value = Calculator.evaluate(expression);
            throw new AssertionError(
                    expression + ": ожидалось IllegalArgumentException,"
                            + " получено " + value
            );
        } catch (IllegalArgumentException e) {
            System.out.println(expression
                    + " -> IllegalArgumentException: "
                    + e.getMessage());
        }
    }
}
