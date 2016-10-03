package info.ipeanut.algorithms.sort;

import info.ipeanut.algorithms.Utils;

/**
 * Created by chenshaosina on 16/10/3.
 *
 * 直接选择排序--记录最小索引,前面是排过序的
 *
 1.拿未排序的序列中第一个位置的元素与后面比较，如果发现更小者，与之交换。一直假想未排序的序列中第一个位置是最小。
 2.空间复杂度O(1)，时间复杂度 O(n^2)
 *
 */
public class SectionSort {


    public void sectionSort(int[] arr){
        if (null == arr || arr.length < 2)
            return;
        int minIndex ;
        for (int i = 0; i < arr.length - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]){
                    minIndex = j;
                }
            }
            if (minIndex != i){
                Utils.swap(arr,i,minIndex);
            }
        }
    }

}
