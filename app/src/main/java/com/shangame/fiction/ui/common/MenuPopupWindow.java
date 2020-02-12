package com.shangame.fiction.ui.common;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2018/8/7
 */
public class MenuPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private View contentView;
    private ListView listView;
    private MyAdapter myAdapter;

    private OnItemClickListener onItemClickListener;

    private List<String> itemList = new ArrayList<>();

    public MenuPopupWindow(Activity activity,List<String> list) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popup_anim_style);
        if(list != null){
            this.itemList.addAll(list);
        }
    }

    private void initView() {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_menu,null);
        setContentView(contentView);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        listView = (ListView) contentView.findViewById(R.id.listView);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setDivider(mActivity.getResources().getDrawable(R.drawable.public_line_horizontal));
        listView.setDividerHeight(2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        contentView.findViewById(R.id.tvCancel).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tvCancel){
            dismiss();
        }
    }


    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public String getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            TextView itemView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1,null);
            itemView.setGravity(Gravity.CENTER);
            itemView.setBackgroundResource(R.drawable.item_bg);
            int padding = ScreenUtils.dpToPxInt(mActivity,10);
            itemView.setPadding(padding,padding,padding,padding);
            itemView.setTextColor(Color.parseColor("#3478F6"));
            itemView.setText(getItem(position));
            return itemView;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
