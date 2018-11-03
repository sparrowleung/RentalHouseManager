package com.example.samsung.rentalhousemanager.message;

import android.util.Log;

import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.server.RentalMoneyObject;
import com.example.samsung.rentalhousemanager.server.RenterMessageObject;
import com.example.samsung.rentalhousemanager.server.RoomMessageObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onFindResultsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yuyang.liang on 2018/10/11.
 */

public class MsgSendData {
    private final static String TAG = MsgSendData.class.getSimpleName();

    private int finishFlag = 0;
    private IResponse mResponse;

    public static final int MSG_REQUEST_FAIL = 1;
    public static final int MSG_REQUEST_SUCCESS = 2;

    private Map<String, Object> lessMap = new HashMap<>();
    private Map<String, Object> equalMap = new HashMap<>();
    private Map<String, Object> greatMap = new HashMap<>();

    private Observable<Boolean> roomMsgObservable;
    private Observable<Boolean> rentalMsgObservable;
    private Observable<Boolean> renterMsgObservable;

    public MsgSendData(IResponse response, Map<String, Object> map, Map<String, Object> map1) {
        lessMap = map;
        greatMap = map1;
        mResponse = response;
        equalMap.put("userName", BmobUser.getCurrentUser().getUsername());
    }

    public List<List<Map<String, String>>> requestAllMsg() {
        final List<List<Map<String, String>>> allMsgList = new ArrayList<>();

        renterMsgObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                final List<Map<String, String>> renterMsgList = new ArrayList<>();
                ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, null, new onFindResultsListener<RenterMessageObject>() {
                    @Override
                    public void onSuccess(List<RenterMessageObject> list) {
                        String num = "";
                        for (RenterMessageObject object : list) {
                            if (!num.equals(object.getRoomNum())) {
                                Map<String, String> msgRenter = new HashMap<String, String>();
                                msgRenter.put("roomNum", object.getRoomNum());
                                msgRenter.put("name", object.getRenterName());
                                msgRenter.put("phoneNum", object.getRenterPhone());
                                renterMsgList.add(msgRenter);
                                num = object.getRoomNum();
                            }
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMessage) {
                        Log.e(TAG, "RenterMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
                    }

                    @Override
                    public void onComplete(boolean normal) {
                        if (normal) {
                            allMsgList.add(0, renterMsgList);
                            emitter.onComplete();
                        } else {
                            emitter.onNext(false);
                        }
                    }
                });
            }
        });

        rentalMsgObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                final List<Map<String, String>> rentalMsgList = new ArrayList<>();
                ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, greatMap, new onFindResultsListener<RentalMoneyObject>() {
                    @Override
                    public void onSuccess(List<RentalMoneyObject> list) {
                        String num = "";
                        for (RentalMoneyObject object : list) {
                            if (!num.equals(object.getRoomNum())) {
                                Map<String, String> msgRental = new HashMap<String, String>();
                                msgRental.put("waterUse", object.getWaterUse());
                                msgRental.put("electricUse", object.getElectricUse());
                                msgRental.put("airUse", object.getAirUse());
                                rentalMsgList.add(msgRental);
                                num = object.getRoomNum();
                            }
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMessage) {
                        Log.e(TAG, "RentalMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
                    }

                    @Override
                    public void onComplete(boolean normal) {
                        if (normal) {
                            allMsgList.add(1, rentalMsgList);
                            emitter.onComplete();
                        } else {
                            emitter.onNext(false);
                        }
                    }
                });
            }
        });

        roomMsgObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                final List<Map<String, String>> roomMsgList = new ArrayList<>();
                ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, null, new onFindResultsListener<RoomMessageObject>() {
                    @Override
                    public void onSuccess(List<RoomMessageObject> list) {
                        String num = "";
                        for (RoomMessageObject object : list) {
                            if (!num.equals(object.getRoomNum())) {
                                Map<String, String> msgRental = new HashMap<String, String>();
                                msgRental.put("air", object.getRoomAir());
                                msgRental.put("monthly", object.getRoomMonthly());
                                msgRental.put("water", object.getRoomWater());
                                msgRental.put("electric", object.getRoomElectric());
                                msgRental.put("manage", object.getRoomManage());
                                msgRental.put("other", object.getRoomOther());
                                roomMsgList.add(msgRental);
                                num = object.getRoomNum();
                            }
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMessage) {
                        Log.e(TAG, "RoomMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
                    }

                    @Override
                    public void onComplete(boolean normal) {
                        if (normal) {
                            allMsgList.add(2, roomMsgList);
                            emitter.onComplete();
                        } else {
                            emitter.onNext(false);
                        }
                    }
                });
            }
        });

        Observable.concat(renterMsgObservable, rentalMsgObservable, roomMsgObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    private Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            mResponse.onFail(MSG_REQUEST_FAIL);
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mResponse.onSuccess(MSG_REQUEST_SUCCESS);
                    }
                });

        return allMsgList;
    }

//    public List<Map<String,String>> requestForRenterMsg() {
//        final List<Map<String,String>> renterMsgList = new ArrayList<>();
//        ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, null, new onFindResultsListener<RenterMessageObject>() {
//            @Override
//            public void onSuccess(List<RenterMessageObject> list) {
//                String num = "";
//                for (RenterMessageObject object : list) {
//                    if (!num.equals(object.getRoomNum())) {
//                        Map<String, String> msgRenter = new HashMap<String, String>();
//                        msgRenter.put("roomNum", object.getRoomNum());
//                        msgRenter.put("name", object.getRenterName());
//                        msgRenter.put("phoneNum", object.getRenterPhone());
//                        renterMsgList.add(msgRenter);
//                        num = object.getRoomNum();
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(int errorCode, String errorMessage) {
//                Log.e(TAG, "RenterMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
//            }
//
//            @Override
//            public void onComplete(boolean normal) {
//                if (normal) {
//                    getProgress(1);
//                } else {
//                    mResponse.onFail(MSG_REQUEST_FAIL);
//                }
//            }
//        });
//        return renterMsgList;
//    }
//
//    public List<Map<String,String>> requestForRentalMsg() {
//        final List<Map<String,String>> rentalMsgList = new ArrayList<>();
//        ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, greatMap, new onFindResultsListener<RentalMoneyObject>() {
//            @Override
//            public void onSuccess(List<RentalMoneyObject> list) {
//                String num = "";
//                for (RentalMoneyObject object : list) {
//                    if (!num.equals(object.getRoomNum())) {
//                        Map<String, String> msgRental = new HashMap<String, String>();
//                        msgRental.put("waterUse", object.getWaterUse());
//                        msgRental.put("electricUse", object.getElectricUse());
//                        msgRental.put("airUse", object.getAirUse());
//                        rentalMsgList.add(msgRental);
//                        num = object.getRoomNum();
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(int errorCode, String errorMessage) {
//                Log.e(TAG, "RentalMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
//            }
//
//            @Override
//            public void onComplete(boolean normal) {
//                if (normal) {
//                    getProgress(2);
//                } else {
//                    mResponse.onFail(MSG_REQUEST_FAIL);
//                }
//            }
//        });
//        return rentalMsgList;
//    }
//
//    public List<Map<String,String>> requestForRoomMsg() {
//        final List<Map<String,String>> roomMsgList = new ArrayList<>();
//        ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, null, new onFindResultsListener<RoomMessageObject>() {
//            @Override
//            public void onSuccess(List<RoomMessageObject> list) {
//                String num = "";
//                for (RoomMessageObject object : list) {
//                    if (!num.equals(object.getRoomNum())) {
//                        Map<String, String> msgRental = new HashMap<String, String>();
//                        msgRental.put("air", object.getRoomAir());
//                        msgRental.put("monthly", object.getRoomMonthly());
//                        msgRental.put("water", object.getRoomWater());
//                        msgRental.put("electric", object.getRoomElectric());
//                        msgRental.put("manage", object.getRoomManage());
//                        msgRental.put("other", object.getRoomOther());
//                        roomMsgList.add(msgRental);
//                        num = object.getRoomNum();
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(int errorCode, String errorMessage) {
//                Log.e(TAG, "RoomMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
//            }
//
//            @Override
//            public void onComplete(boolean normal) {
//                if (normal) {
//                    getProgress(3);
//                } else {
//                    mResponse.onFail(MSG_REQUEST_FAIL);
//                }
//            }
//        });
//        return roomMsgList;
//    }
//
//    public void getProgress(int flag) {
//        finishFlag += flag;
//        if (finishFlag == 6) {
//            mResponse.onSuccess(MSG_REQUEST_SUCCESS);
//        }
//    }

}
