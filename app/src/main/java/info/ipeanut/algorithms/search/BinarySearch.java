package info.ipeanut.algorithms.search;

/**
 * http://www.cnblogs.com/ider/archive/2012/04/01/binary_search.html
 *
 */
public class BinarySearch {

    /**
     * 递归实现
     *
     * @param array 有序的
     * @param low
     * @param height
     * @param target
     * @return
     */
    int bSearch(int[] array,int low,int height,int target){

        if (low > height){
            return -1;
        }

        int mid = (low + height)/2;

        if (array[mid] > target){
            bSearch(array,low,mid - 1,target);
        }
        if (array[mid] < target){
            bSearch(array,mid + 1,height,target);
        }

        return mid;

    }

    /**
     * 非递归实现
     * 二分的递归其实是尾递归，它不关心递归前的所有信息。
     *
     * @param array
     * @param low
     * @param high
     * @param target
     * @return
     */
    int bsearchWithoutRecursion(int array[], int low, int high, int target)
    {
        while(low <= high)
        {
            int mid = (low + high)/2;
            if (array[mid] > target)
                high = mid - 1;
            else if (array[mid] < target)
                low = mid + 1;
            else //find the target
                return mid;
        }
        return -1;
    }

    /**
     * 所以while循环的条件正好是递归的边界条件的逆。
     */

}