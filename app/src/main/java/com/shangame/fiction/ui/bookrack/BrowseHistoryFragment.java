package com.shangame.fiction.ui.bookrack;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.core.utils.TimeUtils;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.db.ChapterInfoDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.storage.model.ChapterInfo;
import com.shangame.fiction.ui.listen.order.ChapterOrderPopWindow;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.play.MusicPlayerActivity;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.RemindFrameLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 浏览记录Fragment
 * Create by Speedy on 2018/8/1
 */
public class BrowseHistoryFragment extends BaseFragment implements View.OnClickListener, AddToBookRackContacts.View, TaskAwardContacts.View, BrowseHistoryContacts.View {

    private RefreshLayout refreshLayout;
    private MyAdapter myAdapter;
    private AddToBookRackPresenter addToBookRackPresenter;
    private int currentAddToBookRackPosition;
    private RemindFrameLayout remindFrameLayout;
    private TaskAwardPresenter taskAwardPresenter;
    private BrowseHistoryPresenter mPresenter;
    private ChapterOrderPopWindow chapterOrderPopWindow;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.ADD_BOOK_TO_RACK_ACTION.equals(action)) {
                refreshLayout.autoRefresh();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_browse_history, container, false);
        contentView.findViewById(R.id.tvClear).setOnClickListener(this);
        remindFrameLayout = contentView.findViewById(R.id.remindFrameLayout);
        initRecyclerView(contentView);
        initRefreshLayout(contentView);

        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addToBookRackPresenter = new AddToBookRackPresenter();
        addToBookRackPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);

        mPresenter = new BrowseHistoryPresenter();
        mPresenter.attachView(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addToBookRackPresenter.detachView();
        taskAwardPresenter.detachView();
        mPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void initRecyclerView(View contentView) {
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        myAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (myAdapter.getItemCount() > 0) {
                    remindFrameLayout.showContentView();
                } else {
                    remindFrameLayout.showRemindView();
                }
            }
        });
    }

    private void initRefreshLayout(View contentView) {
        refreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                queryHistory();
            }
        });
    }

    private void queryHistory() {
        Observable.create(new ObservableOnSubscribe<List<BookBrowseHistory>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookBrowseHistory>> emitter) throws Exception {
                BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
                List<BookBrowseHistory> list = bookBrowseHistoryDao.queryBuilder().orderDesc(BookBrowseHistoryDao.Properties.ReadTime).list();
                if (list != null) {
                    emitter.onNext(list);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BookBrowseHistory>>() {
                    @Override
                    public void accept(List<BookBrowseHistory> readBookHistories) throws Exception {
                        addToAdapter(readBookHistories);
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void addToAdapter(List<BookBrowseHistory> list) {
        myAdapter.clear();
        myAdapter.addAll(list);
        BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
        for (BookBrowseHistory bookBean : myAdapter.getData()) {
            BookReadProgress bookReadProgress = bookReadProgressDao.loadByRowId(bookBean.bookid);
            if (bookReadProgress != null) {
                bookBean.chapternumber = bookReadProgress.chapterIndex;
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvClear) {
            clearAllHistory();
        }
    }

    private void clearAllHistory() {
        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
        bookBrowseHistoryDao.deleteAll();
        myAdapter.clear();
        myAdapter.notifyDataSetChanged();
        showToast(getString(R.string.clear_success));
    }

    @Override
    public void addToBookRackSuccess(boolean finishActivity, long bookid, int receive) {
        BookBrowseHistory bookBrowseHistory = myAdapter.getItem(currentAddToBookRackPosition);
        if (null != bookBrowseHistory) {
            bookBrowseHistory.bookshelves = 1;
            myAdapter.notifyItemChanged(currentAddToBookRackPosition);
            BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
            bookBrowseHistoryDao.update(bookBrowseHistory);

            updateBookInfo(bookid);

            showToast(getString(R.string.add_to_bookrack_success));
            Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
            intent.putExtra("bookId", bookid);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            if (receive == 0) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                taskAwardPresenter.getTaskAward(userId, TaskId.ADD_BOOK_TO_RACK, true);
            }
        }
    }

    private void updateBookInfo(long bookId) {
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        List<ChapterInfo> chapterInfoList = chapterInfoDao.queryBuilder()
                .where(ChapterInfoDao.Properties.Bookid.eq(bookId)).list();
        for (ChapterInfo info : chapterInfoList) {
            info.bookshelves = 1;
            chapterInfoDao.update(info);
        }
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId) {
        if (taskAwardResponse.number > 0) {
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    @Override
    public void getAlbumChapterDetailSuccess(final AlbumChapterDetailResponse response, final int albumId, final int cid) {
        if (null == response) {
            return;
        }
        if (null == response.play_url) {
            return;
        }
        if (TextUtils.isEmpty(response.play_url.small)) {
            return;
        }
        // 免费章节
        if (response.chargingmode == 0) {
            checkNetType(response, albumId, cid);
        } else {
            // 1已订阅
            if (response.buystatus == 1) {
                checkNetType(response, albumId, cid);
            } else {
                showChapterOrderPopWindow(albumId, cid, new ChapterOrderPopWindow.OnOrderPayListener() {
                    @Override
                    public void onPaySuccess() {
                        Log.e("hhh", "onPaySuccess");
                        showToast(mActivity.getString(R.string.book_order_success));
                        checkNetType(response, albumId, cid);
                    }

                    @Override
                    public void onCancelPay() {
                        Log.e("hhh", "onCancelPay");
                    }
                });
            }
        }
    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    private void checkNetType(final AlbumChapterDetailResponse response, final int chapterId, final int cid) {
        NetworkUtils.NetworkType netWorkType = NetworkUtils.getNetworkType();
        switch (netWorkType) {
            case NETWORK_2G:
            case NETWORK_3G:
            case NETWORK_4G:
            case NETWORK_UNKNOWN:
            case NETWORK_ETHERNET:
                Log.e("hhh", "non wifi");
                alertNonWifi(response, chapterId, cid);
                break;
            case NETWORK_WIFI:
                Log.e("hhh", "wifi");
                jumpToPlay(response, chapterId, cid);
                break;
            default:
                break;
        }
    }

    private void showChapterOrderPopWindow(long albumId, long chapterId, ChapterOrderPopWindow.OnOrderPayListener onOrderPayListener) {
        if (chapterOrderPopWindow == null || !chapterOrderPopWindow.isShow()) {

            if (null != this.getContext() && null != this.getActivity()) {
                chapterOrderPopWindow = new ChapterOrderPopWindow(this.getContext(), this.getActivity(), albumId, chapterId);
                chapterOrderPopWindow.setOnOrderPayListener(onOrderPayListener);

                new XPopup.Builder(this.getContext())
                        .moveUpToKeyboard(false)
                        .asCustom(chapterOrderPopWindow)
                        .show();
            }
        }
    }

    private void alertNonWifi(final AlbumChapterDetailResponse response, final int chapterId, final int cid) {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("当前为非WIFI环境，是否继续播放？")
                .setPositiveButton("继续播放", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        jumpToPlay(response, chapterId, cid);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void jumpToPlay(AlbumChapterDetailResponse response, int albumId, int cid) {
        Intent intent = new Intent(mContext, MusicPlayerActivity.class);
        Song song = new Song();
        song.albumid = albumId;
        song.id = cid;
        song.lastcid = response.lastcid;
        song.nextcid = response.nextcid;
        song.buyStatus = response.buystatus;
        song.albumName = response.albumName;
        song.showCover = response.showCover;
        song.showName = response.showName;
        song.url = response.play_url.small;
        song.duration = response.duration * 1000;
        song.autoRenew = response.autorenew;
        song.bookShelves = response.bookshelves;
        song.readMoney = response.readmoney;
        song.chargingMode = response.chargingmode;
        song.chapterPrice = response.chapterprice;
        song.isVip = response.isvip;
        song.chapterNumber = response.sort;
        intent.putExtra("song", song);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    private void listenBook(BookBrowseHistory book) {
        BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
        BookReadProgress bookReadProgress = bookReadProgressDao.loadByRowId(book.bookid);
        if (bookReadProgress != null) {
            book.chapteId = bookReadProgress.chapterId;
        }

        long userId = UserInfoManager.getInstance(mContext).getUserid();
        String deviceId = DeviceUtils.getAndroidID();
        int bookId = (int) book.bookid;
        int chapterId = (int) book.chapteId;
        mPresenter.getAlbumChapterDetail(userId, bookId, chapterId, deviceId);
    }

    private void openBook(BookBrowseHistory book) {
        int pageIndex = 0;
        BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
        BookReadProgress bookReadProgress = bookReadProgressDao.loadByRowId(book.bookid);
        if (bookReadProgress != null) {
            pageIndex = bookReadProgress.pageIndex;
            book.chapteId = bookReadProgress.chapterId;
        }
        ReadActivity.lunchActivity(mActivity, book.bookid, book.chapteId, pageIndex, 1);
    }

    private void saveReadHistory(BookBrowseHistory bookBrowseHistory) {
        bookBrowseHistory.readTime = System.currentTimeMillis();

        VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao().insertOrReplace(bookBrowseHistory);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBookCover;
        TextView ivBookName;
        TextView tvChapter;
        TextView ivBrowseTime;
        TextView tvAddToBookRack;
        ImageView ivDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            ivBookName = itemView.findViewById(R.id.ivBookName);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivBrowseTime = itemView.findViewById(R.id.ivBrowseTime);
            tvAddToBookRack = itemView.findViewById(R.id.tvAddToBookRack);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<BookBrowseHistory, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.browse_history_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final BookBrowseHistory bookBrowseHistory = getItem(position);
            if (null != bookBrowseHistory) {
                holder.ivBookName.setText(bookBrowseHistory.bookname);
                holder.ivBrowseTime.setText(TimeUtils.getFriendlyTimeSpanByNow(bookBrowseHistory.readTime));
                if (bookBrowseHistory.bookshelves == 1) {
                    holder.tvAddToBookRack.setVisibility(View.GONE);
                } else {
                    holder.tvAddToBookRack.setVisibility(View.VISIBLE);
                }

                holder.tvAddToBookRack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int userId = UserInfoManager.getInstance(mContext).getUserid();
                        currentAddToBookRackPosition = holder.getAdapterPosition();
                        if (bookBrowseHistory.booktype == 1) {
                            addToBookRackPresenter.addToAlbumRack(userId, (int) bookBrowseHistory.bookid, false);
                        } else {
                            addToBookRackPresenter.addToBookRack(userId, (int) bookBrowseHistory.bookid, false);
                        }
                    }
                });
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
                        bookBrowseHistoryDao.delete(bookBrowseHistory);
                        myAdapter.remove(bookBrowseHistory);
                        myAdapter.notifyDataSetChanged();
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bookBrowseHistory.booktype == 1) {
                            listenBook(bookBrowseHistory);
                        } else {
                            openBook(bookBrowseHistory);
                        }
                        saveReadHistory(bookBrowseHistory);
                    }
                });
                if (bookBrowseHistory.chapternumber == 0) {
                    if (bookBrowseHistory.booktype == 1) {
                        holder.tvChapter.setText(R.string.un_listen);
                    } else {
                        holder.tvChapter.setText(R.string.unread2);
                    }
                } else {
                    if (bookBrowseHistory.booktype == 1) {
                        holder.tvChapter.setText(getString(R.string.listen_chapter_num, bookBrowseHistory.chapternumber));
                    } else {
                        holder.tvChapter.setText(getString(R.string.read_charpter_num, bookBrowseHistory.chapternumber));
                    }
                }
                ImageLoader.with(mActivity).loadCover(holder.ivBookCover, bookBrowseHistory.bookcover);
            }
        }
    }
}
