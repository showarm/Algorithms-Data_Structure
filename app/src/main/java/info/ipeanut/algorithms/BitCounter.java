package info.ipeanut.algorithms.sort;

/**
 *
 * http://blog.csdn.net/brillianteagle/article/details/52137236
 *
 * 题目：Int类型整数二进制1的个数，例如3的二进制是11，1的个数是2。
 */
public class BitCounter {

    /**
     * 方法一：判断最后一位是否是1，然后不断右移该整数，继续判断，直到该数为0。
     * 这个算法对于正数有效，但是对于负数无效。因为右移时最左侧会补符号位，正数就是补0，而负数就是补1。
     * 不过，由于java中有右移补0的符号>>>，所以，该算法可行。
     */
    public static int bitCounter2( int n){
        int c =0 ; // 计数器
        while (n !=0)
        {
            if((n &1) ==1) // 当前位是1
                ++c ; // 计数器加1
            n >>>=1 ; // 移位,左侧补0，n改变了！
        }
        return c ;
    }


}