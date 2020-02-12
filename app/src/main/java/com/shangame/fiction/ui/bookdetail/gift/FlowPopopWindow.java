package com.shangame.fiction.ui.bookdetail.gift;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;

public class FlowPopopWindow extends BasePopupWindow {

        String msg;
        public FlowPopopWindow(Activity activity, String msg) {
            super(activity);
            this.msg = msg;
            initContentView(activity);
            setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            setAnimationStyle(R.style.signin_style);
            setBackgroundAlpha(0.6f);
        }

        void initContentView(Activity activity) {
            View view = LayoutInflater.from(activity).inflate(R.layout.signin_success,null);
            setContentView(view);
            TextView tvMessage = view.findViewById(R.id.tvMessage);
            tvMessage.setText(msg);
            Button btnKnow = view.findViewById(R.id.btnKnow);
            btnKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }