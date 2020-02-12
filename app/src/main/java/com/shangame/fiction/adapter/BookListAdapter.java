package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.storage.model.BookListEnitiy;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class BookListAdapter extends BaseQuickAdapter<BookListEnitiy, BaseViewHolder> {
    public BookListAdapter(int layoutResId, @Nullable List<BookListEnitiy> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BookListEnitiy item) {
        ImageView ivCover1 = helper.getView(R.id.ivCover1);
        ImageView ivCover2 = helper.getView(R.id.ivCover2);
        ImageView ivCover3 = helper.getView(R.id.ivCover3);
        GlideApp.with(mContext)
                .load(item.bookcover.get(0).bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(ivCover1);
        GlideApp.with(mContext)
                .load(item.bookcover.get(1).bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(ivCover2);
        GlideApp.with(mContext)
                .load(item.bookcover.get(2).bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(ivCover3);

        helper.setText(R.id.tvName, item.title);
        helper.setText(R.id.tvContent, item.contents);
        String kindString = item.classname + "," + item.bookcount + "本";
        String[] kinds = kindString.split(",");

        TextView tvKind1 = helper.getView(R.id.tvKind1);
        TextView tvKind2 = helper.getView(R.id.tvKind2);
        TextView tvKind3 = helper.getView(R.id.tvKind3);
        TextView tvKind4 = helper.getView(R.id.tvKind4);
        tvKind1.setVisibility(View.GONE);
        tvKind2.setVisibility(View.GONE);
        tvKind3.setVisibility(View.GONE);
        tvKind4.setVisibility(View.GONE);

        for (int i = 0; i < kinds.length; i++) {
            switch (i) {
                case 0:
                    tvKind1.setText(kinds[0]);
                    tvKind1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvKind2.setText(kinds[1]);
                    tvKind2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvKind3.setText(kinds[2]);
                    tvKind3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    tvKind4.setText(kinds[3]);
                    tvKind4.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }
}
