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

    public static void logArray(String tag , int[] arr){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(String.valueOf(arr[i]));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.printf("%s : %s",tag,sb.toString());
    }

}
