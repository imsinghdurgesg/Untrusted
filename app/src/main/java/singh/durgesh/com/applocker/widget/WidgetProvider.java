package singh.durgesh.com.applocker.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import singh.durgesh.com.applocker.R;

/**
 * Created by DarshanG on 6/19/2017.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(ctxt, WidgetService.class);

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews widget = new RemoteViews(ctxt.getPackageName(),
                    R.layout.widget);

            widget.setRemoteAdapter(appWidgetIds[i], R.id.words,
                    intent);


            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }
}