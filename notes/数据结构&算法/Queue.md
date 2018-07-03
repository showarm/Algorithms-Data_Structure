
0 用两个栈来实现一个队列

Stack<Integer> in = new Stack<Integer>();
Stack<Integer> out = new Stack<Integer>();

public void push(int node) {
    in.push(node);
}

public int pop() throws Exception {
    if (out.isEmpty())
        while (!in.isEmpty())
            out.push(in.pop());

    if (out.isEmpty())
        throw new Exception("queue is empty");

    return out.pop();
}

1 阻塞队列实现 生产者消费者模型

public class CachePool {
    public static final int TOP_SIZE = 100;
    private BlockingQueue<Integer> queue = new ArrayBlockingQueue();

    //单例
    private CachePool CachePool(){}
    public static class CachePoolHolder{
        public static CachePool getInstance(){
            return new CachePool();
        }
    }

    public void in(int i){
        queue.put(i);
    }

    public int out(){
        return queue.take();
    }
}

public class CachePool {
    public static final int TOP_SIZE = 100;
    private PriorityQueue<Integer> cachePool = new PriorityQueue();

    public void in(int i){
        sync(Pool.class){
            while(cachePool.size >= TOP_SIZE){
                wait();
            }
            list.push(i);
            notify();
        }
    }

    public int out(){
        int result = -1;
        sync(Pool.class){
            while(cachePool.size == 0){
                wait();
            }
            result = list.pop(0);
            notify();
        }
        return result;
    }
}

2 PriorityQueue

优先级队列实现大顶堆
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1);