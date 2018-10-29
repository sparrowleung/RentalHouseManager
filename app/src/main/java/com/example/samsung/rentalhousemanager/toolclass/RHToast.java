package com.example.samsung.rentalhousemanager.toolclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;

/**
 * Created by yuyang.liang on 2018/10/27.
 */

public class RHToast {

    private Toast mToast;
    private Context mContext;

    public RHToast(@NonNull Context context, @NonNull CharSequence text, @NonNull int duration) {
        mContext = context.getApplicationContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast, null);
        TextView textView = view.findViewById(R.id.center_text_view);
        textView.setText(text);

        mToast = new Toast(mContext);
        mToast.setDuration(duration);
        mToast.setView(view);
    }


    public static RHToast makeText(@NonNull Context context, @NonNull CharSequence text) {
        return new RHToast(context, text, Toast.LENGTH_SHORT);
    }

    public static RHToast makeText(@NonNull Context context, @NonNull CharSequence text, @NonNull int duration) {
        return new RHToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

}
