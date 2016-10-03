package info.ipeanut.algorithms.sort;

import info.ipeanut.algorithms.Utils;

/**
 * Created by chenshaosina on 16/10/3.
 *
 * 冒泡排序--相邻比较，后面是排过序的

 1.相邻的2各元素比较，大的向后移，经过一轮比较，最大的元素排在最后
 2.第二轮，第二大的元素排倒数第二个位置
 3.直到全部排好
 时间复杂度O(n^2)
 *
 */
public class BubbleSort {


    public void bubbleSort(int[] arr){
        if (null == arr || arr.length < 2)
            return;
        boolean swapBefore = true;//如果arr是有序的，这就有很大用了

        for (int i = arr.length; i >0 && swapBefore; i--) {//控制次数
            swapBefore = false;
            for (int k = 0; k < i; k++) {//k < i，因为i后面是有序的了
                if (arr[k] > arr[k+1]){
                    Utils.swap(arr,i,k);
                    swapBefore = true;
                }
            }
        }
    }

}
