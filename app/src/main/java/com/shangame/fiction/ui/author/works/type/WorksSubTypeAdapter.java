package com.shangame.fiction.ui.author.works.type;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.ClassAllFigResponse;

import java.util.List;

/**
 * Create by Speedy on 2019/7/24
 */
public class WorksSubTypeAdapter extends BaseQuickAdapter<ClassAllFigResponse.SuperDataBean.SubDataBean, BaseViewHolder> {

    private int mClassId;

    public WorksSubTypeAdapter(int layoutResId, @Nullable List<ClassAllFigResponse.SuperDataBean.SubDataBean> data, int classId) {
        super(layoutResId, data);

        this.mClassId = classId;
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassAllFigResponse.SuperDataBean.SubDataBean item) {
        TextView textType = helper.getView(R.id.text_type);
        textType.setText(item.classname);

        setTextView(textType, mClassId);
    }

    @SuppressLint("ResourceType")
    private void setTextView(TextView textType, int classId) {
        switch (classId) {
            // 都市
            case 1: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_city));
                textType.setBackgroundResource(R.drawable.selector_type_city);
            }
            break;
            // 玄幻
            case 10: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_fantasy));
                textType.setBackgroundResource(R.drawable.selector_type_fantasy);
            }
            break;
            // 灵异
            case 43: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_supernatural));
                textType.setBackgroundResource(R.drawable.selector_type_supernatural);
            }
            break;
            // 仙侠
            case 49: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_xianxia));
                textType.setBackgroundResource(R.drawable.selector_type_xianxia);
            }
            break;
            //科幻
            case 55: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_sci_fi));
                textType.setBackgroundResource(R.drawable.selector_type_sci_fi);
            }
            break;
            //游戏
            case 61: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_game));
                textType.setBackgroundResource(R.drawable.selector_type_game);
            }
            break;
            //古言
            case 3: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_ancient_romance));
                textType.setBackgroundResource(R.drawable.selector_type_ancient_romance);
            }
            break;
            // 现言
            case 15: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_modern_romance));
                textType.setBackgroundResource(R.drawable.selector_type_modern_romance);
            }
            break;
            // 纯爱
            case 66: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_pure_love));
                textType.setBackgroundResource(R.drawable.selector_type_pure_love);
            }
            break;
            // 幻想
            case 70: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_fantasy_1));
                textType.setBackgroundResource(R.drawable.selector_type_fantasy_1);
            }
            break;
            // 同人
            case 75: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_fan_fiction));
                textType.setBackgroundResource(R.drawable.selector_type_fan_fiction);
            }
            break;
            //短篇
            case 80: {
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_short_story));
                textType.setBackgroundResource(R.drawable.selector_type_short_story);
            }
            break;
            default:
                textType.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_color_city));
                textType.setBackgroundResource(R.drawable.selector_type_city);
                break;
        }
    }
}
