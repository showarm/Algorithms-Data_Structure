
com.lianjia.router2.Router
com.lianjia.router2.RouterImpl
com.lianjia.router2.RouterHandle
  private RequestHandle mNext;
责任链模型，内置了PluginRouterHandle为最后一层处理器

```
//RouterHandle 这里是核心逻辑
  @Override public boolean navigate(Context context) {
    Intent intent = generateIntent(context);
    if (intent == null) return mNext.navigate(context);

    if (context instanceof Activity) {
      if (mRequest.mRequestCode < 0) {
        ActivityCompat.startActivity((Activity) context, intent, null);
      } else {
        ActivityCompat.startActivityForResult((Activity) context, intent, mRequest.mRequestCode,
            null);
      }
    } else {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      ContextCompat.startActivity(context, intent, null);
    }

    return true;
  }
```
总之，
先 RouterHandle：  ActivityCompat.startActivity((Activity) context, intent, null);
最后一层，PluginRouterHandle： LjPlugin.startActivity(context, intent, pluginPackageName, activityName);

com.lianjia.router2.RouteRequest

  public AbsRouter(Uri uri) {
    mRequest = new RouteRequest(uri);
    parseParams(uri);
  }
  把uri解析为RouteRequest