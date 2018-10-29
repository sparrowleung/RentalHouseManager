package com.example.samsung.rentalhousemanager.toolclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.samsung.rentalhousemanager.main.MainActivity;

/**
 * Created by yuyang.liang on 2018/7/9.
 */

public class ActionLinkManager {
    private static final String TAG = ActionLinkManager.class.getSimpleName();

    private ActionLinkManager() {

    }

    static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static void performActionLink(Activity activity, String actionLink, Bundle bundle) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed() || TextUtils.isEmpty(actionLink)) {
            return;
        }

        performContextActionLink(activity, actionLink, bundle);
    }

    public static void performContextActionLink(Context context, String actionLink) {
        performContextActionLink(context, actionLink, null);
    }

    public static void performContextActionLink(Context context, String actionLink, Bundle bundle) {
        if (context == null || TextUtils.isEmpty(actionLink)) {
            return;
        }

        Uri uri = Uri.parse(actionLink.trim());
        if (uri == null) {
            return;
        }

        int matched = uriMatcher.match(uri);

        String scheme = uri.getScheme() != null ? uri.getScheme() : "";
        String host = uri.getHost() != null ? uri.getHost() : "";
        String path = uri.getPath() != null ? uri.getPath() : "";

        if (path.length() > 1) {
            path = path.substring(1);
        }

        switch (scheme) {
            case "voc": {
                switch (host) {
                    case "view": {
                        switch (path) {
                            case "main":
                                Intent intent;
                                String tabName = uri.getQueryParameter(MainActivity.KEY_QUERY_TAB);
                                if (!TextUtils.isEmpty(tabName)) {
                                    intent = new Intent(context, MainActivity.class);
                                    intent.putExtra(MainActivity.KEY_QUERY_TAB, tabName);
                                } else {
                                    intent = new Intent(context, MainActivity.class);
                                }

                                if (bundle != null) {
                                    intent.putExtras(bundle);
                                }
                                context.startActivity(intent);
                                break;
                        }
                    }
                }
            }
        }
    }
}
