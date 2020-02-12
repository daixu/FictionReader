package com.shangame.fiction.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class DiscountAreaAdapter extends BaseMultiItemQuickAdapter<AlbumModuleResponse.DisDataBean, BaseViewHolder> {

    public DiscountAreaAdapter(List<AlbumModuleResponse.DisDataBean> data) {
        super(data);

        addItemType(1, R.layout.item_discount_area_1);
        addItemType(2, R.layout.item_discount_area_2);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumModuleResponse.DisDataBean item) {
        switch (helper.getItemViewType()) {
            case 1:
                displayView1(helper, item);
                break;
            case 2:
                displayView2(helper, item);
                break;
            default:
                break;
        }
    }

    private void displayView1(BaseViewHolder helper, AlbumModuleResponse.DisDataBean item) {
        helper.setText(R.id.text_book_name, item.albumName);
        helper.setText(R.id.text_synopsis, item.synopsis);

        StringBuilder type = new StringBuilder();
        if (!TextUtils.isEmpty(item.classname)) {
            type.append(item.classname);
            type.append("·");
        }
        type.append(item.chapternumber);
        type.append("集");
        helper.setText(R.id.text_book_type, type);

        StringBuilder price = new StringBuilder();
        price.append(item.purprice);
        if (item.isvip == 1) {
            price.append("闪闪币/本");
        } else {
            price.append("闪闪币/集");
        }

        TextView textOriginalPrice = helper.getView(R.id.text_book_price);
        textOriginalPrice.setText(price);
        TextView textOriginalDisPrice = helper.getView(R.id.text_book_dis_price);

        if (item.disprice > 0) {
            textOriginalDisPrice.setVisibility(View.VISIBLE);
            textOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            StringBuilder disPrice = new StringBuilder();
            disPrice.append(item.disprice);
            if (item.isvip == 1) {
                disPrice.append("闪闪币/本");
            } else {
                disPrice.append("闪闪币/集");
            }
            textOriginalDisPrice.setText(disPrice);
        } else {
            textOriginalDisPrice.setVisibility(View.GONE);
            textOriginalPrice.getPaint().setFlags(0);
        }

        ImageView imageCover = helper.getView(R.id.image_cover);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);
    }

    private void displayView2(BaseViewHolder helper, AlbumModuleResponse.DisDataBean item) {
        helper.setText(R.id.text_book_name_2, item.albumName);

        StringBuilder price = new StringBuilder();
        price.append(item.purprice);
        if (item.isvip == 1) {
            price.append("闪闪币/本");
        } else {
            price.append("闪闪币/集");
        }
        TextView textOriginalPrice2 = helper.getView(R.id.text_original_price_2);
        textOriginalPrice2.setText(price);
        if (item.disprice > 0) {
            textOriginalPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }

        TextView textDisPrice2 = helper.getView(R.id.text_dis_price_2);
        if (item.disprice > 0) {
            StringBuilder disPrice = new StringBuilder();
            disPrice.append(item.disprice);
            if (item.isvip == 1) {
                disPrice.append("闪闪币/本");
            } else {
                disPrice.append("闪闪币/集");
            }
            textDisPrice2.setVisibility(View.VISIBLE);
            textDisPrice2.setText(disPrice);
        } else {
            textDisPrice2.setVisibility(View.GONE);
        }

        ImageView imageCover2 = helper.getView(R.id.image_cover_2);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover2);
    }
}
