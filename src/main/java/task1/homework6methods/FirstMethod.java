package task1.homework6methods;

import java.util.Arrays;

public class FirstMethod {

    public static int[] getNewArrayAfterLastFourFromArray(int[] arr) throws RuntimeException {

        int lastFour = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 4) {
                lastFour = i;
            }
        }
        if (lastFour == -1) {
            throw new RuntimeException();
        }
        if (lastFour == arr.length - 1) {
            return new int[0];
        }
        return Arrays.copyOfRange(arr, lastFour + 1, arr.length);
    }
}
