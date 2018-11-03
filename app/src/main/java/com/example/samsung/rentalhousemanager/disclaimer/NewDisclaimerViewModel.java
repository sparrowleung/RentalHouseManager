package com.example.samsung.rentalhousemanager.disclaimer;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.roomsetting.OnTextChangedCallback;
import com.example.samsung.rentalhousemanager.server.FloorObject;
import com.example.samsung.rentalhousemanager.server.RentalUserObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onFindResultsListener;
import com.example.samsung.rentalhousemanager.server.onSaveResultsListener;
import com.example.samsung.rentalhousemanager.server.onUpdateResultsListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by yuyang.liang on 2018/8/24.
 */

public class NewDisclaimerViewModel extends BaseObservable {
    private final static String TAG = NewDisclaimerViewModel.class.getSimpleName();

    public final static int TYPE_LOGIN = 0;
    public final static int TYPE_LOGIN_FAIL = 1;

    public final static int TYPE_FORGET = 2;
    public final static int TYPE_FORGET_FAIL = 3;
    public final static int TYPE_FORGET_CLICK = 4;
    public final static int TYPE_FORGET_SUCCESS = 5;

    public final static int TYPE_REGISTER = 6;
    public final static int TYPE_REGISTER_FAIL = 7;
    public final static int TYPE_REGISTER_CLICK = 8;

    public final static int TYPE_VERIFY_CODE = 9;
    public final static int TYPE_MESSAGE_EMPTY = 10;
    public final static int TYPE_SETFLOOR_FINISH = 11;

    private String account = "";
    private String password = "";
    private String objectId = "";
    private String fourFloor = "";
    private String fourRoom = "";
    private String verifyCode = "";

    private int mCurrentType;
    private Observable<Integer> loginOb;
    private Observable<Integer> registerOb;

    private NewDisclaimerModel mDisclaimerModel;
    public final PublishSubject<Integer> mPublishSubject = PublishSubject.create();
    public PublishSubject<Map<String, Integer>> mPubSubject = PublishSubject.create();

    private Context mContext;
    private EventHandler eventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;

                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Log.e(TAG, "send VerifyCode Success");
                        if (result != SMSSDK.RESULT_COMPLETE) {
                            ((Throwable) data).printStackTrace();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            if (mCurrentType == TYPE_REGISTER_CLICK) {
                                RegisterMsgSubmit();
                            } else if (mCurrentType == TYPE_FORGET_CLICK) {
                                ForgetMsgSubmit();
                            }
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    return false;
                }
            }).sendMessage(msg);
        }
    };

    public NewDisclaimerViewModel(Context context) {
        mContext = context;
        mDisclaimerModel = new NewDisclaimerModel();
    }

    @BindingAdapter({"loginMessage"})
    public static void setLoginMessage(final EditText editText, final OnTextChangedCallback changedCallback) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changedCallback.onTextChanged(editText, s.toString());
            }
        });
    }

    @BindingAdapter({"AccountLoginClick"})
    public static void setAccountLoginButtonClick(Button button, final IWelComeClickCallBack clickCallBack) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.welcome_next_login:
                        clickCallBack.clickResponse(TYPE_LOGIN);
                        break;
                    case R.id.welcome_next_forgetPass:
                        clickCallBack.clickResponse(TYPE_FORGET);
                        break;
                    case R.id.welcome_next_register:
                        clickCallBack.clickResponse(TYPE_REGISTER);
                        break;
                    case R.id.welcome_register_click:
                        clickCallBack.clickResponse(TYPE_REGISTER_CLICK);
                        break;
                    case R.id.welcome_forget_click:
                        clickCallBack.clickResponse(TYPE_FORGET_CLICK);
                        break;
                    case R.id.welcome_forget_getCode:
                    case R.id.welcome_register_getCode:
                        clickCallBack.clickResponse(TYPE_VERIFY_CODE);
                        break;
                    case R.id.setFloor_next:
                        clickCallBack.clickResponse(TYPE_SETFLOOR_FINISH);
                        break;
                }
            }
        });
    }

    @BindingAdapter({"FloorCheckBox"})
    public static void setCheckBoxClick(RadioGroup radioGroup, final  IWelComeClickCallBack clickCallBack) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                clickCallBack.clickResponse(checkedId);
            }
        });
    }

    public final IWelComeClickCallBack clickCallBack = new IWelComeClickCallBack() {
        @Override
        public void clickResponse(final int type) {
            switch (type) {
                case TYPE_LOGIN:
                    mCurrentType = type;
                    account = mDisclaimerModel.getAccount();
                    password = mDisclaimerModel.getPassword();

                    if (account == null || account.isEmpty() || account.equals("")
                            || password == null || password.isEmpty() || password.equals("")) {
                        mPublishSubject.onNext(TYPE_MESSAGE_EMPTY);
                        break;
                    } else {
                        Login(account, password);
                        loginOb.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                switch (integer) {
                                    case 3:
                                        mPublishSubject.onNext(TYPE_LOGIN);
                                        break;
                                    case 4:
                                        mPublishSubject.onNext(TYPE_LOGIN_FAIL);
                                        break;
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
                    }
                    break;
                case TYPE_FORGET:
                case TYPE_REGISTER:
                    mPublishSubject.onNext(type);
                    break;
                case TYPE_FORGET_CLICK:
                    mCurrentType = type;
                    account = mDisclaimerModel.getAccount();
                    password = mDisclaimerModel.getPassword();
                    verifyCode = mDisclaimerModel.getVerifyCode();

                    if (account == null || account.isEmpty() || account.equals("")
                            || password == null || password.isEmpty() || password.equals("")
                            || verifyCode == null || verifyCode.isEmpty() || verifyCode.equals("")) {
                        mPublishSubject.onNext(TYPE_MESSAGE_EMPTY);
                        break;
                    } else {
                        checkAccount();
                    }
                    break;
                case TYPE_REGISTER_CLICK:
                    mCurrentType = type;
                    account = mDisclaimerModel.getAccount();
                    password = mDisclaimerModel.getPassword();
                    verifyCode = mDisclaimerModel.getVerifyCode();

                    if (account == null || account.isEmpty() || account.equals("")
                            || password == null || password.isEmpty() || password.equals("")
                            || verifyCode == null || verifyCode.isEmpty() || verifyCode.equals("")) {
                        mPublishSubject.onNext(TYPE_MESSAGE_EMPTY);
                        break;
                    } else {
                        SMSSDK.submitVerificationCode("86", account, verifyCode);
                        SMSSDK.registerEventHandler(eventHandler);
                    }
                    break;
                case TYPE_VERIFY_CODE:
                    account = mDisclaimerModel.getAccount();

                    if (account == null || account.isEmpty() || account.equals("")) {
                        mPublishSubject.onNext(TYPE_MESSAGE_EMPTY);
                        break;
                    } else {
                        SMSSDK.getVerificationCode("86", account);
                        SMSSDK.registerEventHandler(eventHandler);
                    }
                    break;
                case TYPE_SETFLOOR_FINISH:
                    String floorNum = mDisclaimerModel.getFloorNum();
                    String floorRoom = mDisclaimerModel.getFloorRoom();

                    if (floorNum == null || floorNum.isEmpty() || floorNum.equals("")
                            || floorRoom == null || floorRoom.isEmpty() || floorRoom.equals("")
                            || fourFloor.equals("") || fourRoom.equals("")) {
                        mPublishSubject.onNext(TYPE_MESSAGE_EMPTY);
                        break;
                    } else {
                        FloorSetMsgSubmit(Integer.parseInt(floorNum), Integer.parseInt(floorRoom), Integer.parseInt(fourFloor), Integer.parseInt(fourRoom));
                    }
                    break;
                case R.id.four_checkbox1:
                    fourFloor = "1";
                    Log.e(TAG, "checkBox 1");
                    break;
                case R.id.four_checkbox2:
                    fourFloor = "2";
                    Log.e(TAG, "checkBox 2");
                    break;
                case R.id.four_checkbox3:
                    fourRoom = "3";
                    Log.e(TAG, "checkBox 3");
                    break;
                case R.id.four_checkbox4:
                    fourRoom = "4";
                    Log.e(TAG, "checkBox 4");
                    break;
                case R.id.four_checkbox5:
                    fourRoom = "5";
                    Log.e(TAG, "checkBox 5");
                    break;
            }
        }
    };

    public void RegisterMsgSubmit() {
        Login(account, password);

        registerOb = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Integer> emitter) throws Exception {
                BmobUser user = new BmobUser();
                user.setUsername(account);
                user.setPassword(password);
                user.signUp(new SaveListener<RentalUserObject>() {
                    @Override
                    public void done(RentalUserObject user, BmobException e) {
                        if (e == null) {
                            emitter.onNext(1);
                            emitter.onComplete();
                        } else {
                            emitter.onNext(2);
                            emitter.onError(e);
                        }
                    }
                });
            }
        });

        Observable.concat(registerOb, loginOb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        switch (integer){
                            case 1:
                                onDestroy();
                                break;
                            case 2:
                                mPublishSubject.onNext(TYPE_REGISTER_FAIL);
                                break;
                            case 3:
                                mPublishSubject.onNext(TYPE_LOGIN);
                                break;
                            case 4:
                                mPublishSubject.onNext(TYPE_LOGIN_FAIL);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void ForgetMsgSubmit() {
        BmobUser user = new BmobUser();
        user.setPassword(password);
        Log.e(TAG, "ForgetMsgSubmit ObjectId -> " + objectId);
        ServerRequest.onUpdateRequest(user, objectId, new onUpdateResultsListener() {
            @Override
            public void onSuccess(String objectId) {
                mPublishSubject.onNext(TYPE_FORGET_SUCCESS);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "ForgetMsgSubmit errorCode -> " + errorCode + " , errorMessage -> " + errorMessage);
                mPublishSubject.onNext(TYPE_FORGET_FAIL);
            }
        });
    }

    public void FloorSetMsgSubmit(final int fN, final int fR, final int fourF, final int fourR ) {
        FloorObject object = new FloorObject();
        object.setFloorNum(fN);
        object.setRoomNum(fR);
        object.setFourFloor(fourF);
        object.setFourRoom(fourR);
        object.setUserName(BmobUser.getCurrentUser().getUsername());

        ServerRequest.onSaveRequest(object, new onSaveResultsListener() {
            @Override
            public void onSuccess(String objectId) {
                Map<String, Integer> map = new HashMap<>();
                map.put("floorNum", fN);
                map.put("floorRoom", fR);
                map.put("fourFloor", fourF);
                map.put("fourRoom", fourR);

                mPubSubject.onNext(map);
                mPubSubject.onComplete();
                mPublishSubject.onNext(TYPE_SETFLOOR_FINISH);
                mPublishSubject.onComplete();
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "FloorSetMsgSubmit errorCode -> " + errorCode + " , errorMessage -> " + errorMessage);
            }
        });

    }

    public void checkAccount() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", account);
        ServerRequest.onFindRequest("-username", 1, map, new onFindResultsListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> list) {
                objectId = list.get(0).getObjectId();
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "ForgetMsg errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                if (normal) {
                    SMSSDK.submitVerificationCode("86", account, verifyCode);
                    SMSSDK.registerEventHandler(eventHandler);
                } else {
                    mPublishSubject.onNext(TYPE_FORGET_FAIL);
                }
            }
        });
    }

    public void Login(final String a, final String p) {
        loginOb = Observable.create(new ObservableOnSubscribe<Integer>() {
           @Override
           public void subscribe(@NonNull final ObservableEmitter<Integer> emitter) throws Exception {
               BmobUser user = new BmobUser();
               user.setUsername(a);
               user.setPassword(p);
               user.login(new SaveListener<BmobUser>() {
                   @Override
                   public void done(BmobUser bmobUser, BmobException e) {
                       if (e == null) {
                           emitter.onNext(3);
                           emitter.onComplete();
                       } else {
                           emitter.onNext(4);
                           emitter.onError(e);
                       }
                   }
               });
           }
       });

    }

    public final OnTextChangedCallback callback = new OnTextChangedCallback() {
        @Override
        public void onTextChanged(EditText editText, String text) {
            switch (editText.getId()) {
                case R.id.welcome_next_account:
                case R.id.welcome_forget_account:
                case R.id.welcome_register_account:
                    mDisclaimerModel.setAccount(text);
                    break;
                case R.id.welcome_next_password:
                case R.id.welcome_forget_password:
                case R.id.welcome_register_password:
                    mDisclaimerModel.setPassword(text);
                    break;
                case R.id.welcome_forget_verify:
                case R.id.welcome_register_verify:
                    mDisclaimerModel.setVerifyCode(text);
                    break;
                case R.id.setFloor_floorNum:
                    mDisclaimerModel.setFloorNum(text);
                    break;
                case R.id.setFloor_roomNum:
                    mDisclaimerModel.setFloorRoom(text);
                    break;
            }

        }
    };

    public void onDestroy() {
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
