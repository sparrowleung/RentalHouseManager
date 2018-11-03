package com.example.samsung.rentalhousemanager.server;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by yuyang.liang on 2018/7/31.
 */

public class ServerRequest {
    private final static String TAG = ServerRequest.class.getSimpleName();

    public ServerRequest() {}

    public static synchronized ServerRequest getInstance() {
        return new ServerRequest();
    }

    public static <E> void onFindRequest(@NonNull String order,
                                         @NonNull onFindResultsListener<E> listener) {
        BmobQuery<E> query = new BmobQuery<>();
        query.order(order).findObjects(listener);
    }

    public static <E> void onFindRequest(@NonNull String order,
                                         @NonNull int limit,
                                         @NonNull onFindResultsListener<E> listener) {
        BmobQuery<E> query = new BmobQuery<>();
        query.order(order).setLimit(limit).findObjects(listener);
    }

    public static <E> void onFindRequest(@NonNull String order,
                                         @NonNull int limit,
                                         @NonNull Map<String, Object> args,
                                         @NonNull onFindResultsListener<E> listener) {
        BmobQuery<E> query = new BmobQuery<>();
        for (Map.Entry<String, Object> map : args.entrySet()) {
            query.addWhereEqualTo(map.getKey(), map.getValue());
        }
        query.order(order).setLimit(limit).findObjects(listener);
    }

    public static <E> void onFindRequest(@NonNull String order,
                                         @NonNull int limit,
                                         Map<String, Object> equalMap,
                                         Map<String, Object> lessMap,
                                         Map<String, Object> greaterMap,
                                         @NonNull onFindResultsListener<E> listener) {
        BmobQuery<E> query = new BmobQuery<>();
        BmobQuery<E> query1 = new BmobQuery<>();
        BmobQuery<E> query2 = new BmobQuery<>();
        BmobQuery<E> query3 = new BmobQuery<>();
        List<BmobQuery<E>> queryList = new ArrayList<>();

        if (equalMap != null && !equalMap.isEmpty()) {
            for (Map.Entry<String, Object> map : equalMap.entrySet()) {
                query1.addWhereEqualTo(map.getKey(), map.getValue());
            }
            queryList.add(query1);
        }

        if (lessMap != null && !lessMap.isEmpty()) {
            for (Map.Entry<String, Object> map : lessMap.entrySet()) {
                if (map.getKey().equals("uploadAt")) {
                    query2.addWhereLessThanOrEqualTo(map.getKey(), new BmobDate((Date) map.getValue()));
                } else if (map.getKey().equals("uploadAtNight")) {
                    query2.addWhereLessThanOrEqualTo("uploadAt", new BmobDate((Date) map.getValue()));
                } else {
                    query2.addWhereLessThanOrEqualTo(map.getKey(), map.getValue());
                }
            }
            queryList.add(query2);
        }

        if (greaterMap != null && !greaterMap.isEmpty()) {
            for (Map.Entry<String, Object> map : greaterMap.entrySet()) {
                if (map.getKey().equals("uploadAt")) {
                    query3.addWhereGreaterThanOrEqualTo(map.getKey(), new BmobDate((Date) map.getValue()));
                } else if (map.getKey().equals("uploadAtNight")) {
                    query3.addWhereGreaterThanOrEqualTo("uploadAt", new BmobDate((Date) map.getValue()));
                } else{
                    query3.addWhereGreaterThanOrEqualTo(map.getKey(), map.getValue());
                }
            }
            queryList.add(query3);
        }

        query.order(order).setLimit(limit).and(queryList).findObjects(listener);

    }

    public static <E> void onQueryRequset(@NonNull String objectId,
                                          @NonNull onQueryResultsListener<E> listener) {
        BmobQuery<E> query = new BmobQuery<>();
        query.getObject(objectId, listener);
    }

    public static <E> void onQueryRequset(@NonNull String objectId,
                                          @NonNull Map<String, Object> args,
                                          @NonNull onQueryResultsListener<E> listener) {
        BmobQuery<E> query = new BmobQuery<>();
        for (Map.Entry<String, Object> map : args.entrySet()) {
            query.addWhereEqualTo(map.getKey(), map.getValue());
        }
        query.getObject(objectId, listener);
    }

    public static <E extends BmobObject> void onSaveRequest(@NonNull E object,
                                                            @NonNull final onSaveResultsListener listener) {
        object.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    listener.onSuccess(s);
                } else {
                    listener.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public static <E extends BmobObject> void onUpdateRequest(@NonNull E object,
                                                              @NonNull final String objectId,
                                                              @NonNull final onUpdateResultsListener listener) {
        object.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.onSuccess(objectId);
                } else {
                    listener.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public static <E extends BmobObject> void onUpdateRequestByValaue(@NonNull E object,
                                                                      @NonNull final String objectId,
                                                                      @NonNull Map<String, Object> args,
                                                                      @NonNull final onUpdateResultsListener listener) {
        for (Map.Entry<String, Object> map : args.entrySet()) {
            object.setValue(map.getKey(), map.getValue());
        }

        object.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.onSuccess(objectId);
                } else {
                    listener.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public static <E extends BmobObject> void onDeleteRequest(@NonNull E object,
                                                              @NonNull final String objectId,
                                                              @NonNull final onUpdateResultsListener listener) {
        object.delete(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.onSuccess(objectId);
                } else {
                    listener.onFail(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }
}
