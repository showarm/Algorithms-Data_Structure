package info.ipeanut.algorithms.sort;

import info.ipeanut.algorithms.Utils;

/**
 * Created by chenshaosina on 16/10/3.
 *
 * 插入，非交换
 * 插入排序 -- 总是找到最小值再交换/每次左边是排过序的小数
 *
 * 打扑克牌时发完牌后，你是怎么整理牌的？
 *
 0.目的：使数列从左到右是按由小到大排列的
 1 待插入的target与左边所有数比较，如果target小，则比较过的数后移给target让位
 3.时间复杂度 O(n^2)
 *
 */
public class InsertSort {

    public static void InsertSort(int[] arr){
        if(arr == null || arr.length == 0)
        return ;

        for(int i=1; i<arr.length; i++) { //假设第一个数位置时正确的；要往后移，必须要假设第一个。

            int j = i;
            int target = arr[i]; //待插入的

            //target与左边所有数比较，如果target小，则比较过的数后移给target让位置
            while(j > 0 && target < arr[j-1]) {
                arr[j] = arr[j-1];
                j --;
            }

            //插入
            arr[j] = target;
        }
    }

    public static void main(String[] arg){
        int[] arr = {
                5,3,8,2,89,43,7,2,77,0
        };
        InsertSort(arr);
        Utils.logArray("bubbleSort",arr);
    }

}
