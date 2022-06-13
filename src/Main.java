import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        System.out.print("""
                \t\t�������� ������ "�����������"
                1.\t����������� ��������� �������� ��������, ���������, ��������� � ������� � ����� �������.
                2.\t����������� �������� ��� � ��������� (1,2,3,4,5�), ��� � � �������� (I,II,III,IV,V�) �������.
                3.\t����������� ����� ��������� �� ���� ����� ����� �� 1 �� 10 ������������, �� �����.
                \t��������! ����������� �������� ������������ ���� � ��������� ���� � �������� �������.
                """);
        System.out.print("\n������� ���������: ");

        // ����������� ���������� input ��������� �������� �� ��������� ������
        String input = in.nextLine();

        // ������� ��� ������� �� ������
        input = input.replaceAll("\\s*","");

        // ��������� �������� ���� ���-�� ))
        if (input.isEmpty()) throw new IOException("�� ����� ������ ������!");

        // �������� ������ � ����� calc
        // � ������� ��������� �� �������
        System.out.println("���������: " + calc(input));
    }

    public static String calc(String input) throws IOException {

        final String REGEX_NUM_ARAB = "[0123456789]*";
        final String REGEX_NUM_ROME = "[IVX]*";

        // ��������� ������ �� ��������� �� ������������
        String[] tmpStr = input.split("([+\\-*/])");

        // ��������� ��������� �� ������������
        if (tmpStr.length != 2) throw new IOException("������� �� ������ ���������� ��������� ��� �� ����� ������ �������������� ��������!");

        // �������� �������� �� ���������
        char op = input.charAt(tmpStr[0].length());

        int lha, rha; // ������ � ����� ��������

        // ��������� ������������� �� ������ ���������� ����������
        if (tmpStr[0].matches(REGEX_NUM_ARAB) && tmpStr[1].matches(REGEX_NUM_ARAB)){
            lha = Integer.parseInt(tmpStr[0]);
            rha = Integer.parseInt(tmpStr[1]);
            if (inRangeNumbers(lha, rha)){
                return Integer.toString(calculate(lha, rha, op));
            } else throw new IOException("����������� ����� ��������� �� ���� ����� ����� �� 1 �� 10 ������������!");

        } else if (tmpStr[0].matches(REGEX_NUM_ROME) && tmpStr[1].matches(REGEX_NUM_ROME)) {
            lha = ConverterNumberRomanAndArabic.romanToArabic(tmpStr[0]);
            rha = ConverterNumberRomanAndArabic.romanToArabic(tmpStr[1]);
            if (inRangeNumbers(lha, rha)){
                int result = calculate(lha, rha, op);
                if (result < 1){
                    throw new ArithmeticException("��������� ���������� ������ ��� ����� ����! �� ��������� � ������� ������� ����������.");
                }else return ConverterNumberRomanAndArabic.arabicToRoman(result);

            } else throw new IOException("����������� ����� ��������� �� ���� ����� ����� �� 1 �� 10 ������������!");

        }else {
            throw new IOException("������� ������ ���� ��������� ��� ������� ����� ������� �� ���������!");
        }
    }

    static boolean inRangeNumbers(int lha, int rha) {
        return (lha > 0 && lha <= 10) && (rha > 0 && rha <= 10);
    }

    static int calculate(int lha, int rha, char op){

        // ���������� ����� ��������
        return switch (op){
            case '+' -> lha + rha;
            case '-' -> lha - rha;
            case '*' -> lha * rha;
            case '/' -> lha / rha;
            default -> throw new IllegalStateException("����������� ��������: " + op);
        };
    }

    static class ConverterNumberRomanAndArabic {

        private static final int[] numbers = { 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        private static final String[] letters = { "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

        private static int letterToNumber(char letter) {
            // �������� ����������� ����.
            return switch (letter) {
                case 'I' -> 1;
                case 'V' -> 5;
                case 'X' -> 10;
                case 'L' -> 50;
                case 'C' -> 100;
                default -> throw new IllegalStateException("����������� ��������: " + letter);
            };
        }

        static int romanToArabic(String roman) throws IOException {

            int posInRoman = 0; // ������� � ������ �������� �����;
            int result = 0;

            while (posInRoman < roman.length()) {

                char letter = roman.charAt(posInRoman); // ����� � ������� �������.
                int number = letterToNumber(letter); // �������� ���������� �����.

                posInRoman++; // ��������� � ��������� ������� � ������

                if (posInRoman == roman.length()) {

                    // ��������� ������ � ������?
                    // ��������� ����� � ����������.
                    result += number;
                }
                else {
                    // ��������� ��������� ������ � ������. ���� �� ����� ������� ���������� ������� �����,
                    // �� ��� ����� ��������� ������ ��� ������� �����.
                    int nextNumber = letterToNumber(roman.charAt(posInRoman));

                    // ���� ����� ������� ������������ ����� ������������ ����� ������ ����� �������� �� �������� - �� ���������!
                    if (nextNumber > number && roman.length() > 2) throw new IOException("������� ����� ������� �� ���������!");

                    //���� �� ����� ������� ���������� ������� �����, �� ��� ����� ��������� ������ ��� ������� �����.
                    if (nextNumber > number) {

                        // ���������� ��� �����, ����� �������� ���� ��������, � ��������� � ��������� ������� � ������.
                        result += (nextNumber - number);
                        posInRoman++;
                    }
                    else {
                        // ������ ��������� �������� ����� ����� � �����.
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