package com.shangame.fiction.ui.contents;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapBaseAdapter;
import com.shangame.fiction.storage.model.BookMark;

/**
 * Create by Speedy on 2018/9/10
 */
public class BookMarkAdapter extends WrapBaseAdapter<BookMark,BookMarkViewHolder> {

    LayoutInflater layoutInflater;

    public BookMarkAdapter(Activity activity) {
        super(activity);
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookMarkViewHolder myViewHolder;
        if(convertView == null){
            convertView =  layoutInflater.inflate(R.layout.book_mark_item,parent,false);
            myViewHolder = new BookMarkViewHolder(convertView);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (BookMarkViewHolder) convertView.getTag();
        }

        BookMark bookMark = getItem(position);
        myViewHolder.tvChapterName.setText(bookMark.title);
        myViewHolder.tvTime.setText(bookMark.createtime);
        myViewHolder.tvbookMark.setText(bookMark.content);

        return convertView;
    }
}
