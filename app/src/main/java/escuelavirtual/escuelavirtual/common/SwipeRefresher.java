package escuelavirtual.escuelavirtual.common;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import escuelavirtual.escuelavirtual.R;

public class SwipeRefresher {
    private SwipeRefreshLayout swipeRefreshLayout;

    public SwipeRefreshLayout set(AppCompatActivity activity, int layout) {
        swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        return swipeRefreshLayout;
    }
}
