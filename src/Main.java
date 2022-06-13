import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        System.out.print("""
                \t\tТЕСТОВАЯ ЗАДАЧА "КАЛЬКУЛЯТОР"
                1.\tКалькулятор выполняет операции сложения, вычитания, умножения и деления с двумя числами.
                2.\tКалькулятор работает как с арабскими (1,2,3,4,5…), так и с римскими (I,II,III,IV,V…) числами.
                3.\tКалькулятор может принимать на вход целые числа от 1 до 10 включительно, не более.
                \tВНИМАНЕЕ! Калькулятор работает одновременно либо с арабскими либо с римскими цифрами.
                """);
        System.out.print("\nВведите выражение: ");

        // присваиваем переменной input введенное значение из командной строки
        String input = in.nextLine();

        // удаляем все пробелы из строки
        input = input.replaceAll("\\s*","");

        // проверяем осталось хоть что-то ))
        if (input.isEmpty()) throw new IOException("Вы ввели пустую строку!");

        // передаем строку в метод calc
        // и выводим результат на консоль
        System.out.println("Результат: " + calc(input));
    }

    public static String calc(String input) throws IOException {

        final String REGEX_NUM_ARAB = "[0123456789]*";
        final String REGEX_NUM_ROME = "[IVX]*";

        // разделяем строку на подстроки по разделителям
        String[] tmpStr = input.split("([+\\-*/])");

        // проверяем выражение на корректность
        if (tmpStr.length != 2) throw new IOException("Введено не верное количество операндов или не верно указан математический оператор!");

        // получаем оператор из выражения
        char op = input.charAt(tmpStr[0].length());

        int lha, rha; // правый и левый операнды

        // проверяем соответствуют ли строки регулярным выражениям
        if (tmpStr[0].matches(REGEX_NUM_ARAB) && tmpStr[1].matches(REGEX_NUM_ARAB)){
            lha = Integer.parseInt(tmpStr[0]);
            rha = Integer.parseInt(tmpStr[1]);
            if (inRangeNumbers(lha, rha)){
                return Integer.toString(calculate(lha, rha, op));
            } else throw new IOException("Калькулятор может принимать на вход целые числа от 1 до 10 включительно!");

        } else if (tmpStr[0].matches(REGEX_NUM_ROME) && tmpStr[1].matches(REGEX_NUM_ROME)) {
            lha = ConverterNumberRomanAndArabic.romanToArabic(tmpStr[0]);
            rha = ConverterNumberRomanAndArabic.romanToArabic(tmpStr[1]);
            if (inRangeNumbers(lha, rha)){
                int result = calculate(lha, rha, op);
                if (result < 1){
                    throw new ArithmeticException("Результат вычисления меньше или равен нулю! Не допустимо в Римской системе исчисления.");
                }else return ConverterNumberRomanAndArabic.arabicToRoman(result);

            } else throw new IOException("Калькулятор может принимать на вход целые числа от 1 до 10 включительно!");

        }else {
            throw new IOException("Введены разные типы операндов или римские числа введены не корректно!");
        }
    }

    static boolean inRangeNumbers(int lha, int rha) {
        return (lha > 0 && lha <= 10) && (rha > 0 && rha <= 10);
    }

    static int calculate(int lha, int rha, char op){

        // Собственно выбор операции
        return switch (op){
            case '+' -> lha + rha;
            case '-' -> lha - rha;
            case '*' -> lha * rha;
            case '/' -> lha / rha;
            default -> throw new IllegalStateException("Неожиданное значение: " + op);
        };
    }

    static class ConverterNumberRomanAndArabic {

        private static final int[] numbers = { 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        private static final String[] letters = { "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

        private static int letterToNumber(char letter) {
            // Числовые эквиваленты букв.
            return switch (letter) {
                case 'I' -> 1;
                case 'V' -> 5;
                case 'X' -> 10;
                case 'L' -> 50;
                case 'C' -> 100;
                default -> throw new IllegalStateException("Неожиданное значение: " + letter);
            };
        }

        static int romanToArabic(String roman) throws IOException {

            int posInRoman = 0; // Позиция в строке римского числа;
            int result = 0;

            while (posInRoman < roman.length()) {

                char letter = roman.charAt(posInRoman); // Буква в текущей позиции.
                int number = letterToNumber(letter); // Числовой эквивалент буквы.

                posInRoman++; // Переходим к следующей позиции в строке

                if (posInRoman == roman.length()) {

                    // Последний символ в строке?
                    // Добавляем число к результату.
                    result += number;
                }
                else {
                    // Проверяем следующий символ в строке. Если он имеет больший эквивалент римской цифры,
                    // то две буквы считаются вместе как римская цифра.
                    int nextNumber = letterToNumber(roman.charAt(posInRoman));

                    // Если перед большим эквивалентом числа присутствует более одного числа меньшего по значению - не корректно!
                    if (nextNumber > number && roman.length() > 2) throw new IOException("Римское число введено не корректно!");

                    //Если он имеет больший эквивалент римской цифры, то две буквы считаются вместе как римская цифра.
                    if (nextNumber > number) {

                        // Объединяем две буквы, чтобы получить одно значение, и переходим к следующей позиции в строке.
                        result += (nextNumber - number);
                        posInRoman++;
                    }
                    else {
                        // Просто добавляем значение одной буквы к числу.
                        result += number;
                    }
                }
            }
            return result;
        }

        static String arabicToRoman(int arabic){

            String result = "";
            for (int i = 0; i < numbers.length; i++) {
                while (arabic >= numbers[i]) {
                    result += letters[i];
                    arabic -= numbers[i];
                }
            }
            return result;
        }
    }
}