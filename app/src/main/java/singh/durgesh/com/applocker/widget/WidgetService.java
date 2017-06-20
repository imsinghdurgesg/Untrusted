/**
 * Created by DarshanG on 6/19/2017.
 */
package singh.durgesh.com.applocker.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return(new BlockedAppsViewsFactory(this.getApplicationContext(),
                                 intent));
  }
}