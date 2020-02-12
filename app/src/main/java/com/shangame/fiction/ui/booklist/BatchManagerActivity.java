package com.shangame.fiction.ui.booklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.db.ChapterInfoDao;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.ChapterInfo;
import com.shangame.fiction.storage.model.LocalBookBean;
import com.shangame.fiction.ui.bookrack.BookHandlerContacts;
import com.shangame.fiction.ui.bookrack.BookHandlerPresenter;
import com.shangame.fiction.ui.bookrack.BookListFragment;
import com.shangame.fiction.ui.listen.PlayerSong;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.popup.DeleteBookPopupWindow;
import com.shangame.fiction.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class BatchManagerActivity extends BaseActivity implements View.OnClickListener, BookHandlerContacts.View {

    private MyAdapter myAdapter;
    private int currentViewType;
    private TextView tvDelete;
    private TextView tvAll;
    private boolean pickAll;
    private BookHandlerPresenter mPresenter;
    private ArrayList<LocalBookBean> pickList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_manager);
        findViewById(R.id.tvClose).setOnClickListener(this);
        tvAll = findViewById(R.id.tvAll);
        tvDelete = findViewById(R.id.tvDelete);
        tvAll.setOnClickListener(this);
        tvDelete.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        currentViewType = AppSetting.getInstance(mContext).getInt(SharedKey.BOOK_RACK_VIEW_TYPE, BookListFragment.VIEW_TYPE_GRID_LAYOUT);
        if (currentViewType == BookListFragment.VIEW_TYPE_GRID_LAYOUT) {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.addItemDecoration(new SpaceItemDecoration(35));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        }
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        mPresenter = new BookHandlerPresenter();
        mPresenter.attachView(this);

        ArrayList list = getIntent().getParcelableArrayListExtra("BookList");

        myAdapter.addAll(list);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.tvClose) {
            finish();
        } else if (id == R.id.tvAll) {
            pickAll = !pickAll;
            if (pickAll) {
                tvAll.setText("取消全选");
                myAdapter.checkAll(true);
            } else {
                tvAll.setText("全选");
                myAdapter.checkAll(false);
            }
            tvDelete.setText("点此删除（" + myAdapter.getPickCount() + " )");
        } else if (id == R.id.tvDelete) {
            DeleteBookPopupWindow deleteBookPopupWindow = new DeleteBookPopupWindow(mActivity);
            deleteBookPopupWindow.setDeleteOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<LocalBookBean> list = myAdapter.getLocalBooks();
                    LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
                    if (null != list && !list.isEmpty()) {
                        dao.deleteInTx(list);
                    }
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    if (myAdapter.getNetList() > 0) {
                        String bookIdArr = myAdapter.getPickList();
                        mPresenter.removeFromBookRack(userId, bookIdArr);
                    } else {
                        removeFromBookRackSuccess();
                    }
                }
            });
            int count = myAdapter.getPickCount();
            deleteBookPopupWindow.show(count);
        }
    }

    @Override
    public void removeFromBookRackSuccess() {
        Intent intent = new Intent(BroadcastAction.DELETE_BOOK_FROM_RACK);
        intent.putParcelableArrayListExtra("PickList", pickList);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        finish();
    }

    class MyAdapter extends WrapRecyclerViewAdapter<LocalBookBean, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = null;
            if (viewType == BookListFragment.VIEW_TYPE_GRID_LAYOUT) {
                view = getLayoutInflater().inflate(R.layout.batch_book_rack_item, viewGroup, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.batch_book_rack_item_linear, viewGroup, false);
            }
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            final LocalBookBean book = getItem(position);
            if (book.recState == 1) {
                holder.ivMark.setImageResource(R.drawable.recommend);
            } else if (book.updState == 1) {
                holder.ivMark.setImageResource(R.drawable.update);
            } else {
                holder.ivMark.setVisibility(View.GONE);
            }

            if (book.booktype == 1) {
                holder.imgMusic.setVisibility(View.VISIBLE);
            } else {
                holder.imgMusic.setVisibility(View.GONE);
            }

            holder.checkBox.setChecked(book.isPicked);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    book.isPicked = !book.isPicked;
                    holder.checkBox.setChecked(book.isPicked);
                    if (book.isPicked) {
                        pickList.add(book);
                    } else {
                        pickAll = false;
                        tvAll.setText("全选");
                        pickList.remove(book);
                    }
                    tvDelete.setText("点此删除（" + pickList.size() + " )");
                }
            });
            holder.tvBookName.setText(book.bookName);

            if (holder.tvAuthorName != null) {
                holder.tvAuthorName.setText(getString(R.string.author_zhu, book.author));
            }
            if (book.chapterNumber == 0) {
                holder.tvReadInfo.setText(R.string.unread2);
            } else {
                holder.tvReadInfo.setText(getString(R.string.read_charpter_num, book.chapterNumber));
            }

            if (book.isLocal) {
                holder.mTextBookName.setVisibility(View.VISIBLE);
                holder.mTextBookName.setText(book.bookName);
                ImageLoader.with(mActivity).loadCover(holder.ivCover, R.drawable.icon_txt_cover);
            } else {
                holder.mTextBookName.setVisibility(View.GONE);
                ImageLoader.with(mActivity).loadCover(holder.ivCover, book.bookCover);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return currentViewType;
        }

        public void checkAll(boolean checked) {
            pickList.clear();
            for (LocalBookBean book : getData()) {
                book.isPicked = checked;
                if (checked) {
                    pickList.add(book);
                }
            }
            notifyDataSetChanged();
        }

        private List<LocalBookBean> getLocalBooks() {
            List<LocalBookBean> list = new ArrayList<>();
            for (LocalBookBean book : pickList) {
                if (book.isPicked && book.isLocal) {
                    list.add(book);
                }
            }
            return list;
        }

        private int getNetList() {
            int count = 0;
            for (LocalBookBean book : pickList) {
                if (book.isPicked && !book.isLocal) {
                    count++;
                }
            }
            return count;
        }

        public String getPickList() {
            StringBuilder sb = new StringBuilder();
            for (LocalBookBean book : pickList) {
                if (book.isPicked && !book.isLocal) {
                    sb.append(book.bookId).append(",");
                    Song song = PlayerSong.getInstance().getPlayerSong();
                    if (null != song) {
                        if (book.bookId == song.albumid) {
                            song.bookShelves = 0;
                        }
                    } else {
                        if (book.booktype == 0) {
                            updateBookInfo(book.bookId);
                        }
                        updateHistoryInfo(book.bookId);
                    }
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }

        private void updateBookInfo(long bookId) {
            ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
            List<ChapterInfo> chapterInfoList = chapterInfoDao.queryBuilder()
                    .where(ChapterInfoDao.Properties.Bookid.eq(bookId)).list();
            for (ChapterInfo info : chapterInfoList) {
                info.bookshelves = 0;
                chapterInfoDao.update(info);
            }
        }

        private void updateHistoryInfo(long bookId) {
            BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
            List<BookBrowseHistory> list = bookBrowseHistoryDao.queryBuilder()
                    .where(BookBrowseHistoryDao.Properties.Bookid.eq(bookId)).list();
            for (BookBrowseHistory info : list) {
                info.bookshelves = 0;
                bookBrowseHistoryDao.update(info);
            }
        }

        public int getPickCount() {
            return pickList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        ImageView ivMark;
        ImageView imgMusic;
        TextView tvBookName;
        TextView tvAuthorName;
        TextView tvReadInfo;
        CheckBox checkBox;
        TextView mTextBookName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivMark = itemView.findViewById(R.id.ivMark);
            imgMusic = itemView.findViewById(R.id.img_music);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvReadInfo = itemView.findViewById(R.id.tvReadInfo);
            checkBox = itemView.findViewById(R.id.checkbox);
            mTextBookName = itemView.findViewById(R.id.text_book_name);
        }
    }
}
