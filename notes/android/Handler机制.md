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

## HandlerThread