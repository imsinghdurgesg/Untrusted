package singh.durgesh.com.applocker.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import singh.durgesh.com.applocker.R;

/**
 * Created by DarshanG on 6/19/2017.
 */
public class BlockedAppsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

  private static final String[] items = {"Facebook", "OLX", "Twitter",
                                        "YouTube"};
  private Context context = null;

  private int appWidgetId;

  public BlockedAppsViewsFactory(Context context, Intent intent) {
      this.context = context;
      appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                      AppWidgetManager.INVALID_APPWIDGET_ID);
  }
  
  @Override
  public void onCreate() {
  }
  
  @Override
  public void onDestroy() {
  }

  @Override
  public int getCount() {
    return(items.length);
  }

  @Override
  public RemoteViews getViewAt(int position) {
    RemoteViews row=new RemoteViews(context.getPackageName(),
                                     R.layout.row);
        row.setTextViewText(android.R.id.text1, items[position]);
    return(row);
  }

  @Override
  public RemoteViews getLoadingView() {
    return(null);
  }
  
  @Override
  public int getViewTypeCount() {
    return(1);
  }

  @Override
  public long getItemId(int position) {
    return(position);
  }

  @Override
  public boolean hasStableIds() {
    return(true);
  }

  @Override
  public void onDataSetChanged() {
  }
}