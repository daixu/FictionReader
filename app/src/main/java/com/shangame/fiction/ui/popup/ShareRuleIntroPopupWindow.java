package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;

/**
 * Create by Speedy on 2019/3/20
 */
public class ShareRuleIntroPopupWindow extends BasePopupWindow {

    private String step;
    private String rule;

    public ShareRuleIntroPopupWindow(Activity activity,String step,String rule) {
        super(activity);
        this.step = step;
        this.rule = rule;
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popup_anim_style);
    }

    private void initView() {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_share_rule_intro, null);
        setContentView(contentView);
        TextView tvStep = contentView.findViewById(R.id.tvStep);
        TextView tvRule = contentView.findViewById(R.id.tvRule);
        tvStep.setText(step);
        tvRule.setText(Html.fromHtml(rule));

    }


}
