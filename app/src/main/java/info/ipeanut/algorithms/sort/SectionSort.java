package info.ipeanut.algorithms.sort;

import info.ipeanut.algorithms.Utils;

/**
 * Created by chenshaosina on 16/10/3.
 *
 * 交换
 * 直接选择排序 -- 总是找到最小值再交换/每次左边是排过序的小数
 *
 0.目的：使数列从左到右是按由小到大排列的
 1.左一，剩下数列中找到最小值，二者比较，左一大则交换
 2.左二，剩下数列中找到最小值，二者比较，...
 3.空间复杂度O(1)，时间复杂度 O(n^2)
 *
 */
public class SectionSort {


    public static void sectionSort(int[] arr){
        if (null == arr || arr.length < 2)
            return;
        int minIndex ;
        for (int i = 0; i < arr.length - 1; i++) {
            minIndex = i;
            //找到未排序的最小的
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]){
                    minIndex = j;
                }
            }
            //如果它比已排序的最大还小
            if (arr[minIndex] < arr[i]){
                Utils.swap(arr,i,minIndex);
            }
        }
    }

    public static void main(String[] arg){
        int[] arr = {
                5,3,8,2,89,43,7,2,77,0
        };
        sectionSort(arr);
        Utils.logArray("bubbleSort",arr);
    }

}
