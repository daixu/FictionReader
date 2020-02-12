package com.shangame.fiction.ui.listen.timing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.TimingAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimingPopupWindow extends BottomPopupView {
    private Context mContext;
    private TimingBean mBean;
    private TimingAdapter mAdapter;
    private List<TimingBean> mList = new ArrayList<>();

    private OnClickItemListener onClickItemListener;
    public TimingPopupWindow(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public TimingPopupWindow(@NonNull Context context, TimingBean bean) {
        super(context);
        mContext = context;
        this.mBean = bean;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initData();
    }

    private void initData() {
        mList.clear();
        for (int i = 1; i < 5; i++) {
            TimingBean bean = new TimingBean();
            bean.value = i * 15;
            bean.time = (i * 15) + "分钟";
            mList.add(bean);
        }
        mAdapter.setNewData(mList);
    }

    private void initView() {
        RecyclerView recyclerTiming = findViewById(R.id.recycler_timing);
        recyclerTiming.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TimingAdapter(R.layout.item_timing, mList);
        mAdapter.setTimingBean(mBean);
        recyclerTiming.setAdapter(mAdapter);

        findViewById(R.id.tv_close_timing).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.onCloseTiming();
                dismiss();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TimingBean bean = mList.get(position);
                onClickItemListener.onItemClick(bean);
                dismiss();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_timing;
    }

    public interface OnClickItemListener {
        void onItemClick(TimingBean bean);

        void onCloseTiming();
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }
}
