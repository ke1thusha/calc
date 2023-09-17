import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);

        System.out.println("Введите выражение:");
        String input = in.nextLine();

        String[] inputArray = splitInput(input);
        String result = calc(inputArray);

        System.out.println(result);
    }

    public static String[] splitInput(String i) throws IOException {
        String[] inputArray;

        if (i.contains("/") || i.contains("*") || i.contains("+") || i.contains("-")) {
            inputArray = i.split(" ");
        } else {
            throw new IOException("Неверный формат записи.");
        }
        return inputArray;
    }

    public static String calc(String[] array) throws IOException {

        boolean isRoman = false;
        String[] romanNumeral = {"I", "IV", "V", "IX", "X",
                "Xl", "L", "XC", "C", "CD",
                "D", "CM", "M"};
        int result = 0;

        for (String number : romanNumeral) {
            if (array[0].toUpperCase().contains(number)) {
                array[0] = String.valueOf(romanToArabic(array[0]));
                array[2] = String.valueOf(romanToArabic(array[2]));
                isRoman = true;
            } else if (array[2].toUpperCase().contains(number)) {
                throw new IOException("Неверный формат записи.");
            }
        }

        if (Integer.parseInt(array[0]) > 10 || Integer.parseInt(array[2]) > 10) {
            throw new IOException("Ни один из операндов не должен быть больше 10-ти.");
        }

        switch (array[1]) {
            case "/":
                result = Integer.parseInt(array[0]) / Integer.parseInt(array[2]);
                break;
            case "*":
                result = Integer.parseInt(array[0]) * Integer.parseInt(array[2]);
                break;
            case "+":
                result = Integer.parseInt(array[0]) + Integer.parseInt(array[2]);
                break;
            case "-":
                result = Integer.parseInt(array[0]) - Integer.parseInt(array[2]);
                break;
        }

        if (isRoman) {
            return arabicToRoman(result);
        }

        return String.valueOf(result);
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException("Неверный формат записи");
        }

        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException("Римские числа не могут быть отрицательными или быть равными нулю");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}