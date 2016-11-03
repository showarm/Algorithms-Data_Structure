package info.ipeanut.algorithms.dataStructure;

import android.app.Activity;

import java.util.LinkedList;

/**
 * 先进先出
 * 控制activity栈内元素数量，到警戒线的时候把旧的元素弹出。
 * 比如，新闻图集，打开推荐进入图集，再打开推荐进入图集。。。
 *
 * Created by chenshao on 16/11/3.
 */
public class LimitedActivityQueue {
    private static final int size = 5;

    private static LinkedList<Activity> queue = new LinkedList<>();


    /**
     * 进队 自动检测
     * @param activity
     */
    public static synchronized void enqueue(Activity activity){
        if (size == queue.size()){
            dequeue().finish();
        }
        queue.addLast(activity);

    }

    /**
     * 移除队列的第一个元素，并返回被移除的元素。
     * @return
     */
    public static Activity dequeue(){
        return queue.pop();
    }


}
