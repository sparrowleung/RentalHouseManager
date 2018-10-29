package com.example.samsung.rentalhousemanager.toolclass;

import android.content.UriMatcher;
import android.net.Uri;

import static com.example.samsung.rentalhousemanager.toolclass.Constants.DEFAULT_SCHEME;
import static com.example.samsung.rentalhousemanager.toolclass.Constants.HOST_VIEW;

/**
 * Created by yuyang.liang on 2018/7/9.
 */

class Constants {
    static final String DEFAULT_SCHEME = "voc";
    static final String CALL_SCHEME = "tel";
    static final String HOST_VIEW = "view";
    static final String HOST_ACTIVITY = "activity";
}

public enum  ActionLink {
    MAIN_ACTIVITY(HOST_VIEW, "main"),
    NOTIFICATION_ACTIVITY(HOST_VIEW, "setting/notification"),
    NEWS_AND_TIPS_ACTIVITY(HOST_VIEW, "newsAndTips");

    public final String authority;
    public final String path;
    public final String fullPath;

    ActionLink(String authority, String path) {
        this.authority = authority;
        this.path = path;
        fullPath = build(authority, path);
    }

    @Override
    public String toString() {
        return fullPath;
    }

    public void addUri(UriMatcher uriMatcher) {
        uriMatcher.addURI(authority, path, ordinal());
    }

    static String build(String host, String path) {
        return new Uri.Builder().scheme(DEFAULT_SCHEME).authority(host).path(path).toString();
    }
}
