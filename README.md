# Calculator

- целые числа и операции `+`, `-`, `*`, `/`
- скобки произвольной вложенности
- унарный минус: `-5`, `2*(-3)`, `-(2+3)`
- пробелы в строке игнорируются

Некорректный ввод (деление на ноль, незакрытая скобка, два числа подряд,
посторонний символ) выбрасывает `IllegalArgumentException`


## Алгоритмы

- Парсер из строкового выражения в обратную польскую нотацию [Wikipedia: Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
- Вычисление на основе обратной польской нотации [Wikipedia: Reverse Polish notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation)
