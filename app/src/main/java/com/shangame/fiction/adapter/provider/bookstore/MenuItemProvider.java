package com.shangame.fiction.adapter.provider.bookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.ui.booklib.BookLibraryActivity;

/**
 * 菜单栏
 */
public class MenuItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> implements View.OnClickListener {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_MENU;
    }

    @Override
    public int layout() {
        return R.layout.item_menu_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        TextView menuClassify = helper.getView(R.id.menu_classify);
        TextView menuRanking = helper.getView(R.id.menu_ranking);
        TextView menuNewBook = helper.getView(R.id.menu_new_book);
        TextView menuEnd = helper.getView(R.id.menu_end);
        TextView menuRed = helper.getView(R.id.menu_red);

        menuClassify.setOnClickListener(this);
        menuRanking.setOnClickListener(this);
        menuNewBook.setOnClickListener(this);
        menuEnd.setOnClickListener(this);
        menuRed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_classify:
            case R.id.menu_ranking:
            case R.id.menu_new_book:
            case R.id.menu_end:
            case R.id.menu_red:
                mContext.startActivity(new Intent(mContext, BookLibraryActivity.class));
                break;
            default:
                break;
        }
    }
}
