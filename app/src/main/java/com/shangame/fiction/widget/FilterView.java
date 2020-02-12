package com.shangame.fiction.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.net.response.BookRackFilterConfigResponse;

/**
 * Create by Speedy on 2018/8/28
 */
public class FilterView extends LinearLayout {

    private View contentView;
    private TextView tvName;
    private TextView tvCount;

    private boolean isSelected;

    private OnStateChangeLinstener onStateChangeLinstener;

    public void setOnStateChangeLinstener(FilterView.OnStateChangeLinstener onStateChangeLinstener) {
        this.onStateChangeLinstener = onStateChangeLinstener;
    }

    private BookRackFilterConfigResponse.FilterItemBean filterItemBean;

    public FilterView(Context context) {
        super(context);
        initView(context);
    }


    public FilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.book_filter_item,this,false);
        tvName = contentView.findViewById(R.id.tvName);
        tvCount = contentView.findViewById(R.id.tvCount);

        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelected = !isSelected;
                setSelected(isSelected);
                if(onStateChangeLinstener != null){
                    int id = (filterItemBean != null)? filterItemBean.cid:0;
                    onStateChangeLinstener.onStateChange(isSelected,id);
                }
            }
        });
        addView(contentView);
    }


    public void setFilterItemBean(BookRackFilterConfigResponse.FilterItemBean filterItemBean){
        this.filterItemBean = filterItemBean;
        tvName.setText(filterItemBean.configname);
        tvCount.setText(String.valueOf(filterItemBean.boookcount));
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
        contentView.setSelected(selected);
        if(isSelected){
            tvName.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvCount.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            tvName.setTextColor(getResources().getColor(R.color.text_normal_color));
            tvCount.setTextColor(getResources().getColor(R.color.text_normal_color));
        }
    };


    @Override
    public boolean isSelected() {
        return isSelected;
    }

    public interface OnStateChangeLinstener{
        void onStateChange(boolean selected,int id);
    }


    public int getFilterId(){
        if(filterItemBean != null){
            return filterItemBean.cid;
        }else{
            return 0;
        }
    }




}
