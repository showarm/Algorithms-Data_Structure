package info.ipeanut.algorithms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[][] arr = new int[][]{
            {1,2,3,4,5,6},
            {7,8,9,10,11,12},
            {13,14,15,16,17,18},
            {19,20,21,22,23,24},
            {25,26,27,28,29,30},
        };

        boolean isfind = findK_while(arr,5,6,10);
        Log.e("tatata","hhh"+isfind);
    }

    boolean findK_while(int[][] arr,int m,int n,int k) {
        int i = 0;
        int j = n-1;
        while (i < m && j >= 0){
            if (k > arr[i][j]){
                i++;
            } else if (k < arr[i][j]){
                j--;
            } else {
                return true;
            }
        }
        return false;
    }

    // for循环无法直接实现，除非借助两个List记录排除的行列
    boolean findK_for(int[][] arr,int m,int n,int k) {

        for (int i = 0; i <= m - 1; i++) { //第一个比较a[0][n-1]
            for (int j = n - 1; j >= 0; j--) {
                if (k > arr[i][j]) {
                    i++;                //把第i行排除，但j列无法排除
                    j++;                //排除后进入下一次j循环，执行了j--，这不对，+1补偿回来
                } else if (k < arr[i][j]) {
                    j--;                // 排除j列，无法排除i行
                    i--;                //这里如果做补偿，下次i循环就又回到上一步了，等于上面排除i行白做了
                } else {
                    return true;
                }
            }
        }
        return false;
    }

}
