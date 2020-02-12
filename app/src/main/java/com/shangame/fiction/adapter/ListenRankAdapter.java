package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.net.response.AlbumRankingResponse;

import java.util.List;

public class ListenRankAdapter extends BaseQuickAdapter<AlbumRankingResponse.DataBean, BaseViewHolder> {

    public ListenRankAdapter(int layoutResId, @Nullable List<AlbumRankingResponse.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumRankingResponse.DataBean item) {

    }
}
