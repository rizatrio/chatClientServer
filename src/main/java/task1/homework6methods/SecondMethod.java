package task1.homework6methods;



public class SecondMethod {

    public static boolean checkOnesAndFours(int[] arr) {
        
        int countOfOnes = 0;
        int countOfFours = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                countOfOnes++;
            }
            if (arr[i] == 4) {
                countOfFours++;
            }
        }
        return ((countOfOnes) > 0 && (countOfFours > 0));
    }
}
