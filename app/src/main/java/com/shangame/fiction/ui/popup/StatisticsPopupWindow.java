package com.shangame.fiction.ui.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.shangame.fiction.R;

import static com.shangame.fiction.ui.author.statistics.StatisticsType.TYPE_CLICK;
import static com.shangame.fiction.ui.author.statistics.StatisticsType.TYPE_COLLECT;
import static com.shangame.fiction.ui.author.statistics.StatisticsType.TYPE_COMMENT;

/**
 * Create by Speedy on 2018/8/1
 */
public class StatisticsPopupWindow extends PartShadowPopupView implements View.OnClickListener {

    private OnStatisticsListener onStatisticsListener;
    private int type;

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_type;
    }

    public StatisticsPopupWindow(@NonNull Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        TextView tvClickCount = findViewById(R.id.tv_click_count);
        TextView tvCollectCount = findViewById(R.id.tv_collect_count);
        TextView tvCommentCount = findViewById(R.id.tv_comment_count);
        tvClickCount.setOnClickListener(this);
        tvCollectCount.setOnClickListener(this);
        tvCommentCount.setOnClickListener(this);

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.icon_type_checked);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        switch (type) {
            case TYPE_CLICK:
                tvClickCount.setCompoundDrawables(null, null, drawable, null);
                tvCollectCount.setCompoundDrawables(null, null, null, null);
                tvCommentCount.setCompoundDrawables(null, null, null, null);
                break;
            case TYPE_COLLECT:
                tvClickCount.setCompoundDrawables(null, null, null, null);
                tvCollectCount.setCompoundDrawables(null, null, drawable, null);
                tvCommentCount.setCompoundDrawables(null, null, null, null);
                break;
            case TYPE_COMMENT:
                tvClickCount.setCompoundDrawables(null, null, null, null);
                tvCollectCount.setCompoundDrawables(null, null, null, null);
                tvCommentCount.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (onStatisticsListener == null) {
            return;
        }
        dismiss();
        switch (view.getId()) {
            case R.id.tv_click_count:
                onStatisticsListener.onClickCount();
                break;
            case R.id.tv_collect_count:
                onStatisticsListener.onCollectCount();
                break;
            case R.id.tv_comment_count:
                onStatisticsListener.onCommentCount();
                break;
            default:
                break;
        }
    }

    public void setOnStatisticsListener(OnStatisticsListener onStatisticsListener) {
        this.onStatisticsListener = onStatisticsListener;
    }

    public interface OnStatisticsListener {
        void onClickCount();

        void onCollectCount();

        void onCommentCount();
    }
}
