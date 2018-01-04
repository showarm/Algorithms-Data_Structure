package info.ipeanut.algorithms.Leetcode;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

/**
 * Created by chenshao on 2018/1/4.
 *
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 You may assume that each input would have exactly one solution, and you may not use the same element twice.

 Example:
 Given nums = [2, 7, 11, 15], target = 9,
 Because nums[0] + nums[1] = 2 + 7 = 9,
 return [0, 1].
 */

public class FindTwoSum {

    /*
    简单粗暴  O(n^2)
     */
    public int[] twoSum1(int[] nums, int target) {
        if (nums.length < 2) {
            throw new IllegalArgumentException();
        }
        int[] result = new int[2];

        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    /*
    空间换时间   O(n)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int[] twoSum2(int[] arr, int target){

        int[] result = new int[2];
        ArrayMap<Integer,Integer> map = new ArrayMap<>(arr.length);

        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(target - arr[i])){
                result[0] = map.get(target - arr[i]);
                result[1] = i;
            } else {
                map.put(arr[i],i);
            }
        }
        return result;
    }

}
