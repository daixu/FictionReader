package com.shangame.fiction.adapter.provider.bookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.entity.NormalMultipleEntity;

/**
 * 男生女生
 */
public class BoyGirlItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> implements View.OnClickListener {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_BOY_GIRL;
    }

    @Override
    public int layout() {
        return R.layout.item_boy_girl_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        helper.getView(R.id.girlNovelLayout).setOnClickListener(this);
        helper.getView(R.id.boyNovelLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.girlNovelLayout:
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(BroadcastAction.JUMP_GIRL_FRAGMENT));
                break;
            case R.id.boyNovelLayout:
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(BroadcastAction.JUMP_BOY_FRAGMENT));
                break;
            default:
                break;
        }
    }
}
