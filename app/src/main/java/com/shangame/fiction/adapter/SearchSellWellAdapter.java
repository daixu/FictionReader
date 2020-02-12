package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.SearchInfoResponse;

import java.util.List;

public class SearchSellWellAdapter extends BaseQuickAdapter<SearchInfoResponse.AlbumsDataBean, BaseViewHolder> {
    public SearchSellWellAdapter(int layoutResId, @Nullable List<SearchInfoResponse.AlbumsDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchInfoResponse.AlbumsDataBean item) {
        helper.setText(R.id.tvBookName, item.bookname);
        ImageView imageIndex = helper.getView(R.id.ivIndex);
        int position = helper.getLayoutPosition();
        switch (position) {
            case 0:
                imageIndex.setImageResource(R.drawable.top_1);
                break;
            case 1:
                imageIndex.setImageResource(R.drawable.top_2);
                break;
            case 2:
                imageIndex.setImageResource(R.drawable.top_3);
                break;
            case 3:
                imageIndex.setImageResource(R.drawable.top_4);
                break;
            case 4:
                imageIndex.setImageResource(R.drawable.top_5);
                break;
            case 5:
                imageIndex.setImageResource(R.drawable.top_6);
                break;
            default:
                imageIndex.setVisibility(View.GONE);
        }
    }
}
