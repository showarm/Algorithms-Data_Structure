你为什么敢在Volley回调的onResponse(BaseParser response)里更新UI？
因为Volley使用UI线程的Handler调用的回调onResponse()。
```
// 1 一个Thread只对应一个Looper,Looper构造器中new MessageQueue()
class LooperThread extends Thread {
    public Handler mHandler;

    public void run() {
         Looper.prepare();
         mHandler = new Handler() {
            public void handleMessage(Message msg) {
                // process incoming messages here
            }
        };

        Looper.loop();
    }
}

// 2 handler.sendMessage() 把Message扔进MessageQueue
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
            msg.target = this; //
            if (mAsynchronous) {
                msg.setAsynchronous(true);
            }
            return queue.enqueueMessage(msg, uptimeMillis);
        }
// 3 从 MessageQueue 中取出 Message 分发给Handler
public static void loop() {
            final Looper me = myLooper();
            final MessageQueue queue = me.mQueue;
    
            final long ident = Binder.clearCallingIdentity();
    
            for (;;) {
                Message msg = queue.next(); // might block
                msg.target.dispatchMessage(msg);
            }

            // Make sure that during the course of dispatching the
            // identity of the thread wasn't corrupted.
            final long newIdent = Binder.clearCallingIdentity();
            if (ident != newIdent) {
                Log.wtf(TAG, "Thread identity changed from 0x"
                        + Long.toHexString(ident) + " to 0x"
                        + Long.toHexString(newIdent) + " while dispatching to "
                        + msg.target.getClass().getName() + " "
                        + msg.callback + " what=" + msg.what);
            }
}
// 4 handleMessage(Message msg)
```

单线程模型里，Handler MessageQueue才有意义嘛，否则，同步 死锁 问题。

2 Android子线程真的不能更新UI么
http://www.cnblogs.com/lao-liang/p/5108745.html
https://www.jianshu.com/p/5d1cb4548630 
```
android.view.ViewRoot$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
```
    1 onResume之后，WindowManagerGlobal会在addView()的时候 new ViewRootImpl(view.getContext(), display)，
    2 ViewRootImpl会检查 创建View对线程和更新对线程是不是一个，不是则抛异常，
    3 也就是说 如果是同一个线程，就不会抛异常，
    所以，
    在一个子线程，对应一个Looper，用WindowManager.addView() 起一个ViewRootImpl 构造一个TextView，更新它是没有问题的。

```
button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        TextView tv = new TextView(MainActivity.this);
                        tv.setText("not from UI Thread");
                        WindowManager windowManager = MainActivity.this.getWindowManager();
                        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                200, 200, 200, 200, WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                                WindowManager.LayoutParams.TYPE_TOAST, PixelFormat.OPAQUE);
                        windowManager.addView(tv, params);
                        Looper.loop();
                    }
                }.start();
            }
        });

```

## HandlerThread