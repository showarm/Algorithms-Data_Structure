package info.ipeanut.algorithms;

/**
 * Created by chenshaosina on 16/10/3.
 */
public class Utils {

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
