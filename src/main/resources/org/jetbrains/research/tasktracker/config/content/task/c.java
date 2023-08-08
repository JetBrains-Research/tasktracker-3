import java.util.*;

public class VampireNumbers2626 {
    public static void main(String[] args) {
        for (int i = 1000; i <= 9999; i++) {
            if (isVampireNumber(i)) {
                System.out.println(i);
            }
        }
    }

    public static boolean isVampireNumber(int n) {
        int[] digits = getDigits(n);
        for (int i = 10; i <= 99; i++) {
            if (n % i == 0) {
                int j = n / i;
                int[] fangs = getDigits(i, j);
                if (arraysEqual(digits, fangs)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int[] getDigits(int n) {
        int[] digits = new int[4];
        for (int i = 0; i < 4; i++) {
            digits[i] = n % 10;
            n /= 10;
        }
        return digits;
    }

    public static int[] getDigits(int a, int b) {
        int[] digits = new int[4];
        for (int i = 0; i < 2; i++) {
            int[] temp = getDigits(i == 0 ? a : b);
            digits[i*2] = temp[0];
            digits[i*2+1] = temp[1];
        }
        return digits;
    }

    public static boolean arraysEqual(int[] a, int[] b) {
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }
}