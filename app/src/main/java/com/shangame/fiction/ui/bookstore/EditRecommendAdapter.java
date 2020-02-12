package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.storage.model.EditRecommend;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;

import java.util.List;

/**
 * Create by Speedy on 2018/7/27
 */
public class EditRecommendAdapter extends WrapRecyclerViewAdapter<EditRecommend,EditRecommendHolder > {

    private Activity mActivity;

    public EditRecommendAdapter(Activity activity) {
        mActivity = activity;
    }



    @NonNull
    @Override
    public EditRecommendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_editor_recommend_item,null);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.setMargins(16,16,16,16);
        view.setLayoutParams(layoutParams);
        return new EditRecommendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditRecommendHolder holder, int position) {
        final EditRecommend editRecommend = getItem(position);

        for (int i = 0; i < editRecommend.bookInfoEntityList.size() ; i++) {
            switch (i){
                case 0:
                    final BookInfoEntity bookInfoEntity1 = editRecommend.bookInfoEntityList.get(0);

                    holder.ivCover1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BookDetailActivity.lunchActivity(mActivity, bookInfoEntity1.bookid,ApiConstant.ClickType.FROM_CLICK);
                        }
                    });

                    ImageLoader.with(mActivity).loadCover(holder.ivCover1, bookInfoEntity1.bookcover);
                    holder.tvBookName1.setText(bookInfoEntity1.bookname);
                    holder.tvBookAuthor1.setText(bookInfoEntity1.author);
                    break;
                case 1:
                    final BookInfoEntity bookInfoEntity2 = editRecommend.bookInfoEntityList.get(1);
                    holder.ivCover2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BookDetailActivity.lunchActivity(mActivity, bookInfoEntity2.bookid,ApiConstant.ClickType.FROM_CLICK);
                        }
                    });
                    ImageLoader.with(mActivity).loadCover(holder.ivCover2, bookInfoEntity2.bookcover);
                    holder.tvBookName2.setText(bookInfoEntity2.bookname);
                    holder.tvBookAuthor2.setText(bookInfoEntity2.author);
                    break;
                case 2:
                    final BookInfoEntity bookInfoEntity3 = editRecommend.bookInfoEntityList.get(2);
                    holder.ivCover3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BookDetailActivity.lunchActivity(mActivity, bookInfoEntity3.bookid,ApiConstant.ClickType.FROM_CLICK);
                        }
                    });
                    ImageLoader.with(mActivity).loadCover(holder.ivCover3, bookInfoEntity3.bookcover);
                    holder.tvBookName3.setText(bookInfoEntity3.bookname);
                    holder.tvBookAuthor3.setText(bookInfoEntity3.author);
                    break;
            }
        }
    }

    public void setBookInfoEntityList(List<BookInfoEntity> list) {
        EditRecommend editRecommend = new EditRecommend();
        for (int i = 0; i < list.size(); i++) {
            if(editRecommend == null || i%3 ==0){
               editRecommend = new EditRecommend();
               add(editRecommend);
            }
            editRecommend.bookInfoEntityList.add(list.get(i));
        }
    }
}
