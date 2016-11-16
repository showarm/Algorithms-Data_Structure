package info.ipeanut.algorithms.sort;

import info.ipeanut.algorithms.Utils;

/**
 * Created by chenshaosina on 16/10/3.
 *
 * 总结快速排序的思想：冒泡+二分+递归分治，慢慢体会。。
 *
 * 快速排序是比较和交换小数和大数，这样一来不仅把小数冒泡到上面同时也把大数沉到下面。
 *
 0.目的：使数列从左到右是按由小到大排列的
 3.表现最好的排序算法。 快速排序是不稳定的,平均时间复杂度是O(nlgn)
 *
 */
public class QuickSort2 {
    /**
     *
     * @param arr
     * @param left
     * @param right
     * @return
     */
    public static int partition(int[] arr, int left, int right) {
        int pivotKey = arr[left];

        while(left < right) {
            while(left < right && arr[right] >= pivotKey)
                right --;
            arr[left] = arr[right]; //把小的移动到左边
            while(left < right && arr[left] <= pivotKey)
                left ++;
            arr[right] = arr[left]; //把大的移动到右边
        }
        arr[left] = pivotKey; //最后把pivot赋值到中间
        return left;
    }

    /**
     * 递
     * @param arr
     * @param left
     * @param right
     */
    public static void quickSort(int[] arr, int left, int right) {
        if(left >= right)
            return ;
        int pivotPos = partition(arr, left, right);
        quickSort(arr, left, pivotPos-1);
        quickSort(arr, pivotPos+1, right);
    }

    public static void sort(int[] arr) {
        if(arr == null || arr.length == 0)
            return ;
        quickSort(arr, 0, arr.length-1);
    }

    public static void main(String[] arg){
        int[] arr = {
                5,3,8,2,89,43,7,2,77,0
        };
        sort(arr);
        Utils.logArray("bubbleSort",arr);
    }

}
