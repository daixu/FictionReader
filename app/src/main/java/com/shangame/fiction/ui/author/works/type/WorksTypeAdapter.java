package com.shangame.fiction.ui.author.works.type;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.ClassAllFigResponse;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Create by Speedy on 2019/7/24
 */
public class WorksTypeAdapter extends BaseQuickAdapter<ClassAllFigResponse.SuperDataBean, BaseViewHolder> {

    private Activity mActivity;

    public WorksTypeAdapter(int layoutResId, @Nullable List<ClassAllFigResponse.SuperDataBean> data, Activity activity) {
        super(layoutResId, data);
        this.mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ClassAllFigResponse.SuperDataBean item) {
        ImageView imgTypeIcon = helper.getView(R.id.img_type_icon);
        TextView textType = helper.getView(R.id.text_type);
        textType.setText(item.classname);

        setImageIcon(item, imgTypeIcon, textType);

        RecyclerView recyclerView = helper.getView(R.id.recycler_sub_type);
        WorksSubTypeAdapter adapter = new WorksSubTypeAdapter(R.layout.item_works_sub_type, item.subdata, item.classid);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                ClassAllFigResponse.SuperDataBean.SubDataBean subDataBean = item.subdata.get(position);
                intent.putExtra("subBean", subDataBean);
                intent.putExtra("superBean", item);
                mActivity.setResult(RESULT_OK, intent);
                mActivity.finish();
            }
        });
    }

    private void setImageIcon(ClassAllFigResponse.SuperDataBean item, ImageView imgTypeIcon, TextView textType) {
        switch (item.classid) {
            // 都市
            case 1: {
                Glide.with(mContext).load(R.drawable.icon_city).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#72B0D9"));
            }
            break;
            // 玄幻
            case 10: {
                Glide.with(mContext).load(R.drawable.icon_fantasy).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#7C9ABC"));
            }
            break;
            // 灵异
            case 43: {
                Glide.with(mContext).load(R.drawable.icon_supernatural).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#7897A5"));
            }
            break;
            // 仙侠
            case 49: {
                Glide.with(mContext).load(R.drawable.icon_xianxia).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#6FABA9"));
            }
            break;
            //科幻
            case 55: {
                Glide.with(mContext).load(R.drawable.icon_sci_fi).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#7A91BA"));
            }
            break;
            //游戏
            case 61: {
                Glide.with(mContext).load(R.drawable.icon_game).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#4C97BE"));
            }
            break;
            //古言
            case 3: {
                Glide.with(mContext).load(R.drawable.icon_ancient_romance).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#5B9C99"));
            }
            break;
            // 现言
            case 15: {
                Glide.with(mContext).load(R.drawable.icon_modern_romance).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#EB897C"));
            }
            break;
            // 纯爱
            case 66: {
                Glide.with(mContext).load(R.drawable.icon_pure_love).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#EC8DA6"));
            }
            break;
            // 幻想
            case 70: {
                Glide.with(mContext).load(R.drawable.icon_fantasy_1).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#F1A37A"));
            }
            break;
            // 同人
            case 75: {
                Glide.with(mContext).load(R.drawable.icon_fan_fiction).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#7897A5"));
            }
            break;
            //短篇
            case 80: {
                Glide.with(mContext).load(R.drawable.icon_short_story).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#7897A5"));
            }
            break;
            default:
                Glide.with(mContext).load(R.drawable.icon_city).into(imgTypeIcon);
                textType.setTextColor(Color.parseColor("#72B0D9"));
                break;
        }
    }
}
