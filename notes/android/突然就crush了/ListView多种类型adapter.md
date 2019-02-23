
ListView.java
    @Override
    public void setAdapter(ListAdapter adapter) {
            mRecycler.setViewTypeCount(mAdapter.getViewTypeCount());
    }

AbsListView.java
        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            //noinspection unchecked
            ArrayList<View>[] scrapViews = new ArrayList[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                scrapViews[i] = new ArrayList<View>();
            }
            mViewTypeCount = viewTypeCount;
            mCurrentScrap = scrapViews[0];
            mScrapViews = scrapViews;
        }

Adapter.java
  public int getItemViewType(int position)  //有5中type
  public int getViewTypeCount()             // 但此处返回4

Crush：
2019-01-22 20:00:40.536 29398-30005/com.lianjia.beike E/LJCrashReport: Fatal Exception: java.lang.ArrayIndexOutOfBoundsException:length=4; index=4
    at android.widget.AbsListView$RecycleBin.addScrapView(AbsListView.java:9880)
    at android.widget.ListView.measureHeightOfChildren(ListView.java:1460)
    at android.widget.ListView.onMeasure(ListView.java:1355)
    at com.homelink.android.newhouse.libcore.ui.NoScrollListView.onMeasure(NoScrollListView.java:23)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.LinearLayout.measureChildBeforeLayout(LinearLayout.java:1514)
    at android.widget.LinearLayout.measureVertical(LinearLayout.java:806)
    at android.widget.LinearLayout.onMeasure(LinearLayout.java:685)
    at android.view.View.measure(View.java:23265)
    at android.widget.ScrollView.measureChildWithMargins(ScrollView.java:2226)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at android.widget.ScrollView.onMeasure(ScrollView.java:566)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.LinearLayout.measureChildBeforeLayout(LinearLayout.java:1514)
    at android.widget.LinearLayout.measureVertical(LinearLayout.java:806)
    at android.widget.LinearLayout.onMeasure(LinearLayout.java:685)
    at android.view.View.measure(View.java:23265)
    at android.widget.RelativeLayout.measureChildHorizontal(RelativeLayout.java:715)
    at android.widget.RelativeLayout.onMeasure(RelativeLayout.java:461)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.LinearLayout.measureChildBeforeLayout(LinearLayout.java:1514)
    at android.widget.LinearLayout.measureVertical(LinearLayout.java:806)
    at android.widget.LinearLayout.onMeasure(LinearLayout.java:685)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.LinearLayout.measureChildBeforeLayout(LinearLayout.java:1514)
    at android.widget.LinearLayout.measureVertical(LinearLayout.java:806)
    at android.widget.LinearLayout.onMeasure(LinearLayout.java:685)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.LinearLayout.measureChildBeforeLayout(LinearLayout.java:1514)
    at android.widget.LinearLayout.measureVertical(LinearLayout.java:806)
    at android.widget.LinearLayout.onMeasure(LinearLayout.java:685)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.LinearLayout.measureChildBeforeLayout(LinearLayout.java:1514)
    at android.widget.LinearLayout.measureVertical(LinearLayout.java:806)
    at android.widget.LinearLayout.onMeasure(LinearLayout.java:685)
    at android.view.View.measure(View.java:23265)
    at android.view.ViewGroup.measureChildWithMargins(ViewGroup.java:6928)
    at android.widget.FrameLayout.onMeasure(FrameLayout.java:185)
    at com.android.internal.policy.DecorView.onMeasure(DecorView.java:898)