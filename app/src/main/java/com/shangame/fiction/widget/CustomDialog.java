package com.shangame.fiction.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shangame.fiction.R;

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;
        private CharSequence positiveButtonText, negativeButtonText;
        private CharSequence titleText;
        private CharSequence messageText;
        private CharSequence[] items;
        private int positiveColor = 0, negativeColor = 0;
        private int positiveResId = 0, negativeResId = 0;

        private OnClickListener positiveButtonListener, negativeButtonListener,
                itemClickListener, defaultListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(CharSequence sequence) {
            this.titleText = sequence;
            return this;
        }

        public Builder setTitle(int strId) {
            this.titleText = context.getText(strId);
            return this;
        }

        public Builder setMessage(CharSequence sequence) {
            this.messageText = sequence;
            return this;
        }

        public Builder setMessage(int strId) {
            this.messageText = context.getText(strId);
            return this;
        }

        public Builder setItems(CharSequence[] items, OnClickListener listener) {
            this.items = items;
            this.itemClickListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setItems(int itemsId, OnClickListener listener) {
            this.items = context.getResources().getTextArray(itemsId);
            this.itemClickListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            this.positiveButtonText = text;
            this.positiveButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setPositiveButton(int strId, OnClickListener listener) {
            this.positiveButtonText = context.getText(strId);
            this.positiveButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setPositiveButton(int strId, int resId, int colorSelector, OnClickListener listener) {
            this.positiveButtonText = context.getText(strId);
            this.positiveResId = resId;
            this.positiveColor = colorSelector;
            this.positiveButtonListener = listener == null ? defaultListener : listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, int resId, int colorSelector, OnClickListener listener) {
            this.positiveButtonText = text;
            this.positiveResId = resId;
            this.positiveColor = colorSelector;
            this.positiveButtonListener = listener == null ? defaultListener : listener;
            return this;
        }

        public Builder setNegativeButton(int strId, int resId, int colorSelector, OnClickListener listener) {
            this.negativeButtonText = context.getText(strId);
            this.negativeResId = resId;
            this.negativeColor = colorSelector;
            this.negativeButtonListener = listener == null ? defaultListener : listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, int resId, int colorSelector, OnClickListener listener) {
            this.negativeButtonText = text;
            this.negativeResId = resId;
            this.negativeColor = colorSelector;
            this.negativeButtonListener = listener == null ? defaultListener : listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            this.negativeButtonText = text;
            this.negativeButtonListener = listener == null ? defaultListener : listener;
            return this;
        }

        public Builder setNegativeButton(int strId, OnClickListener listener) {
            this.negativeButtonText = context.getText(strId);
            this.negativeButtonListener = listener == null ? defaultListener : listener;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialogStyle);
            LayoutInflater inflater = LayoutInflater.from(context);
            View contentView = inflater.inflate(R.layout.custom_dialog, null);
            dialog.addContentView(contentView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView titleView = contentView.findViewById(R.id.tv_dialogTitle);
            TextView messageView = contentView.findViewById(R.id.tv_dialogMsg);
            RecyclerView recyclerItems = contentView.findViewById(R.id.recycler_items);
            Button positiveButton = contentView.findViewById(R.id.btn_positive);
            Button negativeButton = contentView.findViewById(R.id.btn_negative);

            // 赋值和添加事件

            settings(titleView, messageView, recyclerItems);
            addListener(positiveButton, negativeButton, dialog);
            dialog.setContentView(contentView);
            return dialog;
        }

        private void settings(TextView titleView, TextView messageView, RecyclerView recyclerItems) {
            if (TextUtils.isEmpty(titleText)) {
                ((View) titleView.getParent()).setVisibility(View.GONE);
            } else {
                ((View) titleView.getParent()).setVisibility(View.VISIBLE);
                titleView.setText(titleText);
            }

            if (TextUtils.isEmpty(messageText)) {
                messageView.setVisibility(View.GONE);
            } else {
                messageView.setVisibility(View.VISIBLE);
                messageView.setText(messageText);
            }

            if (items == null || items.length == 0) {
                recyclerItems.setVisibility(View.GONE);
            } else {
                recyclerItems.setVisibility(View.VISIBLE);
                View parent = (View) recyclerItems.getParent();
                parent.setPadding(parent.getPaddingLeft(), 0,
                        parent.getPaddingRight(), 0);
                String[] itemArray = new String[items.length];
                for (int i = 0; i < items.length; i++) {
                    itemArray[i] = items[i].toString();
                }
            }
        }

        private void addListener(Button positiveButton, Button negativeButton, final CustomDialog dialog) {
            if (TextUtils.isEmpty(positiveButtonText) && TextUtils.isEmpty(negativeButtonText)) {
                ((View) (positiveButton.getParent().getParent())).setVisibility(View.GONE);
            } else {
                ((View) (positiveButton.getParent().getParent())).setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(positiveButtonText)) {
                    positiveButton.setVisibility(View.GONE);
                } else {
                    positiveButton.setVisibility(View.VISIBLE);
                    positiveButton.setText(positiveButtonText);
                    if (positiveColor != 0) {
                        positiveButton.setTextColor(ContextCompat.getColor(context, positiveColor));
                    }
                    if (positiveResId == 0) {
                        // positiveButton.setBackgroundResource(R.drawable.button_dialog_green);
                    } else {
                        positiveButton.setBackgroundResource(positiveResId);
                    }
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            positiveButtonListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
                if (TextUtils.isEmpty(negativeButtonText)) {
                    negativeButton.setVisibility(View.GONE);
                } else {
                    negativeButton.setVisibility(View.VISIBLE);
                    negativeButton.setText(negativeButtonText);
                    if (negativeColor != 0) {
                        negativeButton.setTextColor(ContextCompat.getColor(context, negativeColor));
                    }
                    if (negativeResId == 0) {
                        // negativeButton.setBackgroundResource(R.drawable.button_dialog_transparent);
                    } else {
                        negativeButton.setBackgroundResource(negativeResId);
                    }
                    negativeButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            negativeButtonListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setAttributes(layoutParams);
        }
    }
}
