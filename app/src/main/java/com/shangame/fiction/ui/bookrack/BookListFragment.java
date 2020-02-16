package com.shangame.fiction.ui.bookrack;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.book.cover.BookCoverView;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.Constant;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.core.utils.StringUtils;
import com.shangame.fiction.guide.BookRackGuide;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.BookRackFilterConfigResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.net.response.FreeReadResponse;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.statis.ReadTracer;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.db.BookRecordBeanDao;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBean;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.storage.model.LocalBookBean;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.booklist.DailyRecommendActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.listen.order.ChapterOrderPopWindow;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.play.MusicPlayerActivity;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.main.MainItemType;
import com.shangame.fiction.ui.popup.GoToReadPopupWindow;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.ui.reader.local.BookRepository;
import com.shangame.fiction.ui.reader.local.LocalReadActivity;
import com.shangame.fiction.ui.reader.local.bean.BookRecordBean;
import com.shangame.fiction.ui.reader.local.bean.CollBookBean;
import com.shangame.fiction.ui.signin.SigninPopupWindow;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.widget.SmartViewSwitcher;
import com.shangame.fiction.widget.SpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shangame.fiction.ui.reader.local.LocalReadActivity.EXTRA_COLL_BOOK;
import static com.shangame.fiction.ui.reader.local.LocalReadActivity.EXTRA_IS_COLLECTED;

/**
 * 书架列表Fragment
 * Create by Speedy on 2018/8/1
 */
public class BookListFragment extends BaseFragment implements View.OnClickListener, BookRackContacts.View,
        BookFilterContacts.View, FreeReadContacts.View {

    public static final int VIEW_TYPE_GRID_LAYOUT = 0;
    public static final int VIEW_TYPE_LINEAR_LAYOUT = 1;
    protected static final Object mLocked = new Object();
    private static final int OPEN_BOOK_REQUEST_CODE = 10;
    private static final int LOGIN_GET_FREE_READ_REQUEST_CODE = 20;
    private static final String TAG = "BookListFragment";
    private int currentViewType = VIEW_TYPE_GRID_LAYOUT;
    private TextView tvSign;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView bookrackRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;
    private BookCoverView bookCoverView;
    private View mRemindFrameLayout;
    private Button btnFindBook;
    private int page = 1;
    private BookRackPresenter bookRackPresenter;
    private BookFilterPresenter bookFilterPresenter;
    private boolean isFilter;
    private int statusid;
    private int readecid;
    private int numcid;
    private int malecid;
    private View bookInfoLayout;
    private TextView tvBookName;
    private TextView tvBookIntro;
    private ImageView ivRecomBook;
    private SmartViewSwitcher smartViewSwitcher;
    private FreeReadPresenter freeReadPresenter;

    private List<LocalBookBean> mList = new ArrayList<>();
    private List<LocalBookBean> mNetList = new ArrayList<>();

    private FrameLayout adContainer1;
    // 头条穿山甲
    private List<TTNativeExpressAd> mTTAdList;

    private ReadTracer readTracer;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.ADD_BOOK_TO_RACK_ACTION.equals(action)) {
                refresh();
            } else if (BroadcastAction.REFRESH_BOOK_RACK_ACTION.equals(action)) {
                long bookId = intent.getLongExtra("bookId", 0);
                if (bookId != 0) {
                    updateBook(bookId);
                }
            } else if (BroadcastAction.REFRESH_BOOK_RACK_RANK.equals(action)) {
                long bookId = intent.getLongExtra("bookId", 0);
                if (bookId != 0) {
                    // refreshBookRank(bookId);
                }
            } else if (BroadcastAction.DELETE_BOOK_FROM_RACK.equals(action)) {
                refresh();
            } else if (BroadcastAction.UPDATE_SIAN_INFO.equals(action)) {
                String signInfo = intent.getStringExtra("signInfo");
                tvSign.setText(signInfo);
            } else if (BroadcastAction.UPDATE_READ_PROGRESS.equals(action)) {
                String bookId = intent.getStringExtra("bookId");
                if (!TextUtils.isEmpty(bookId)) {
                    updateReadProgress(bookId);
                }
            } else if (BroadcastAction.UPDATE_LOCAL_BOOK.equals(action)) {
                refresh();
            } else if (BroadcastAction.UPLOAD_WIFI_BOOK.equals(action)) {
                String bookName = intent.getStringExtra("bookName");
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (null != freeReadPresenter) {
                    freeReadPresenter.setWifiBook(userId, bookName);
                }
            }
        }
    };
    private ChapterOrderPopWindow chapterOrderPopWindow;

    private void updateReadProgress(String bookId) {
        BookRecordBeanDao recordBeanDao = DbManager.getDaoSession(mContext.getApplicationContext()).getBookRecordBeanDao();
        BookRecordBean recordBean = recordBeanDao.queryBuilder().where(BookRecordBeanDao.Properties.BookId.eq(bookId)).unique();
        if (null != recordBean) {
            for (int i = 0; i < myAdapter.getItemCount(); i++) {
                LocalBookBean bookBean = myAdapter.getItem(i);
                if (null != bookBean && bookBean.strId.equals(bookId)) {
                    bookBean.chapterNumber = recordBean.getChapter();
                    myAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    private void updateBook(long bookId) {
        BookReadProgress bookReadProgress = DbManager.getDaoSession(mContext).getBookReadProgressDao().loadByRowId(bookId);
        if (bookReadProgress != null) {
            LocalBookBean bookBean;
            for (int i = 0; i < myAdapter.getItemCount(); i++) {
                bookBean = myAdapter.getItem(i);
                if (bookBean.bookId == bookId) {
                    bookBean.chapterNumber = bookReadProgress.chapterIndex;
                    myAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    private void refreshBookRank(long bookId) {
        LocalBookBean bookBean;
        for (int i = 0; i < myAdapter.getItemCount(); i++) {
            bookBean = myAdapter.getItem(i);
            if (bookBean.bookId == bookId) {
                bookBean.lastModifyTime = System.currentTimeMillis();
                myAdapter.remove(bookBean);
                myAdapter.addToHeader(bookBean);
                myAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void changeMode() {
        if (currentViewType == VIEW_TYPE_GRID_LAYOUT) {
            currentViewType = VIEW_TYPE_LINEAR_LAYOUT;
            bookrackRecyclerView.setLayoutManager(linearLayoutManager);
            AppSetting.getInstance(mContext).putInt(SharedKey.BOOK_RACK_VIEW_TYPE, VIEW_TYPE_LINEAR_LAYOUT);
        } else {
            currentViewType = VIEW_TYPE_GRID_LAYOUT;
            bookrackRecyclerView.setLayoutManager(gridLayoutManager);
            AppSetting.getInstance(mContext).putInt(SharedKey.BOOK_RACK_VIEW_TYPE, VIEW_TYPE_GRID_LAYOUT);
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getBookListSuccess(BookRackResponse bookRackResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();

        if (bookRackResponse.pagedata != null && bookRackResponse.pagedata.size() > 0) {
            if (page == 1) {
                myAdapter.clear();
                mList.clear();
                mNetList.clear();
                List<LocalBookBean> list = convertBooksBean(bookRackResponse.pagedata);
                LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
                List<LocalBookBean> localList = dao.queryBuilder().orderDesc(LocalBookBeanDao.Properties.LastModifyTime).list();
                // List<BooksBean> beanList = new ArrayList<>();
                if (null != localList) {
                    BookRecordBeanDao recordBeanDao = DbManager.getDaoSession(mContext.getApplicationContext()).getBookRecordBeanDao();
                    for (LocalBookBean bean : localList) {
                        BookRecordBean recordBean = recordBeanDao.queryBuilder().where(BookRecordBeanDao.Properties.BookId.eq(bean.strId)).unique();
                        if (null != recordBean) {
                            bean.chapterNumber = recordBean.getChapter();
                        } else {
                            bean.chapterNumber = 0;
                        }
                    }
                    mList.addAll(localList);
                }
                if (null != list) {
                    mList.addAll(list);
                    mNetList.addAll(list);
                }
                addToAdapter(mList);
            } else {
                List<LocalBookBean> list = convertBooksBean(bookRackResponse.pagedata);
                mList.addAll(list);
                addToAdapter(mList);
            }
            page++;
        }
    }

    private List<LocalBookBean> convertBooksBean(List<BookBean> bookBeans) {
        List<LocalBookBean> beanList = new ArrayList<>(bookBeans.size());
        for (BookBean bookBean : bookBeans) {
            LocalBookBean booksBean = new LocalBookBean();
            if (!TextUtils.isEmpty(bookBean.bookname)) {
                booksBean.strId = MD5Utils.strToMd5By16(bookBean.bookname);
                booksBean.bookId = bookBean.bookid;
                booksBean.chapterNumber = bookBean.chapternumber;
                booksBean.chapterId = bookBean.chapterid;
                booksBean.bookCover = bookBean.bookcover;
                booksBean.bookName = bookBean.bookname;
                booksBean.author = bookBean.author;
                booksBean.updState = bookBean.updstate;
                booksBean.recState = bookBean.recstate;
                booksBean.isPicked = bookBean.isPicked;
                booksBean.updChapter = bookBean.updchapter;
                booksBean.updating = bookBean.updating;
                booksBean.lastModifyTime = bookBean.lastmodifyTime;
                booksBean.booktype = bookBean.booktype;

                booksBean.isLocal = false;
                booksBean.path = "";

                beanList.add(booksBean);
            }
        }
        return beanList;
    }

    private void addToAdapter(List<LocalBookBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        myAdapter.addAll(list);
        BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
        for (LocalBookBean bookBean : myAdapter.getData()) {
            BookReadProgress bookReadProgress = bookReadProgressDao.loadByRowId(bookBean.bookId);
            if (bookReadProgress != null) {
                bookBean.chapterNumber = bookReadProgress.chapterIndex;
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getRecommendBookSuccess(RecommendBookResponse recommendBookResponse) {
        if (recommendBookResponse.recdata != null && recommendBookResponse.recdata.size() > 0) {
            RecommendBookResponse.RecdataBean recdataBean = recommendBookResponse.recdata.get(0);
            tvBookName.setText(recdataBean.bookname);
            tvBookIntro.setText(recdataBean.synopsis);
            ImageLoader.with(mActivity).loadCover(ivRecomBook, recdataBean.bookcover);
            bookInfoLayout.setVisibility(View.VISIBLE);
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
                        //.enableDrag(false)
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
        song.bookShelves = 1;
        song.readMoney = response.readmoney;
        song.chargingMode = response.chargingmode;
        song.chapterPrice = response.chapterprice;
        song.isVip = response.isvip;
        song.chapterNumber = response.sort;
        intent.putExtra("song", song);
        startActivity(intent);
    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    public List<LocalBookBean> getBookList() {
        return myAdapter.getData();
    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void lunchLoginActivity() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();

        Intent mIntent = new Intent(mActivity, LoginActivity.class);
        startActivityForResult(mIntent, LOGIN_GET_FREE_READ_REQUEST_CODE);
//        showToast(getString(R.string.not_login));
    }

    @Override
    public void getFreeReadInfoSuccess(FreeReadResponse freeReadResponse) {
        for (FreeReadResponse.FreeReadItem freeReadItem : freeReadResponse.newdata) {
            switch (freeReadItem.newtype) {
                case 1:
                    TextView tvFreeInfo = smartViewSwitcher.findViewById(R.id.tvFreeInfo);
                    tvFreeInfo.setText(freeReadItem.newviptext);
                    smartViewSwitcher.startLooping();
                    break;
                case 2:
                    // tvSign.setText(freeReadItem.newviptext);

                    long totalReadTime = readTracer.getTotalReadTime();

                    Intent intent = new Intent(BroadcastAction.UPDATE_SIAN_INFO);
                    if (totalReadTime > ReadTracer.READ_200) {

                    } else if (totalReadTime >= ReadTracer.READ_100) {
                        long time = (ReadTracer.READ_200 - totalReadTime) / 60;
                        intent.putExtra("signInfo", "再读" + time + "分钟领取现金红包");
                    } else if (totalReadTime >= ReadTracer.READ_30) {
                        long time = (ReadTracer.READ_100 - totalReadTime) / 60;
                        intent.putExtra("signInfo", "再读" + time + "分钟领取现金红包");
                    } else if (totalReadTime >= ReadTracer.READ_10) {
                        long time = (ReadTracer.READ_30 - totalReadTime) / 60;
                        intent.putExtra("signInfo", "再读" + time + "分钟领取现金红包");
                    } else {
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        if (userId == 0) {
                            long time = (ReadTracer.READ_10 - totalReadTime) / 60;
                            intent.putExtra("signInfo", "再读" + time + "分钟领取现金红包");
                        } else {
                            UserSetting userSetting = UserSetting.getInstance(mActivity);
                            int nextTaskId = userSetting.getInt(SharedKey.NEXT_TASK_ID, 0);
                            if (nextTaskId == TaskId.READ_10) {
                                long time = (ReadTracer.READ_10 - totalReadTime) / 60;
                                intent.putExtra("signInfo", "再读" + time + "分钟领取现金红包");
                            } else {
                                long time = (ReadTracer.READ_30 - totalReadTime) / 60;
                                intent.putExtra("signInfo", "再读" + time + "分钟领取现金红包");
                            }
                        }
                    }

                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void getFreeReadPermissionSuccess(UserInfo userInfo) {
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        freeReadPresenter.getFreeReadInfo(userInfo.userid);
        GoToReadPopupWindow goToReadPopupWindow = new GoToReadPopupWindow(mActivity);
        goToReadPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                refresh();
            }
        });
        goToReadPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void setWifiBookSuccess() {

    }

    @Override
    public void setWifiBookFailure(String msg) {

    }

    public void refresh() {
        if (null != smartRefreshLayout) {
            smartRefreshLayout.autoRefresh();
        }
    }

    private void refreshData(LocalBookBean book) {
        book.lastModifyTime = System.currentTimeMillis();
        book.updating = 0;

        if (!book.isLocal) {
            removeNet(book);
            addToNetHeader(book);
            saveReadHistory(book);
        } else {
            updateBooksBean(book);
        }

        myAdapter.clear();
        mList.clear();
        List<LocalBookBean> list = mNetList;
        LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
        List<LocalBookBean> localList = dao.queryBuilder().orderDesc(LocalBookBeanDao.Properties.LastModifyTime).list();
        if (null != localList) {
            BookRecordBeanDao recordBeanDao = DbManager.getDaoSession(mContext.getApplicationContext()).getBookRecordBeanDao();
            for (LocalBookBean bean : localList) {
                BookRecordBean recordBean = recordBeanDao.queryBuilder().where(BookRecordBeanDao.Properties.BookId.eq(bean.strId)).unique();
                if (null != recordBean) {
                    bean.chapterNumber = recordBean.getChapter();
                } else {
                    bean.chapterNumber = 0;
                }
            }
            mList.addAll(localList);
        }
        if (null != list) {
            mList.addAll(list);
        }
        addToAdapter(mList);
    }

    private void removeNet(LocalBookBean book) {
        synchronized (mLocked) {
            mNetList.remove(book);
        }
    }

    private void addToNetHeader(LocalBookBean book) {
        synchronized (mLocked) {
            mNetList.add(0, book);
        }
    }

    private void saveReadHistory(LocalBookBean book) {
        BookBrowseHistory bookBrowseHistory = new BookBrowseHistory();
        bookBrowseHistory.bookid = book.bookId;
        bookBrowseHistory.bookname = book.bookName;
        bookBrowseHistory.bookcover = book.bookCover;
        bookBrowseHistory.bookshelves = 1;
        bookBrowseHistory.readTime = System.currentTimeMillis();

        VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao().insertOrReplace(bookBrowseHistory);
    }

    private void updateBooksBean(LocalBookBean book) {
        LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
        dao.insertOrReplace(book);
    }

    private void listenBook(LocalBookBean book) {
        BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
        BookReadProgress bookReadProgress = bookReadProgressDao.loadByRowId(book.bookId);
        if (bookReadProgress != null) {
            book.chapterId = bookReadProgress.chapterId;
        }

        long userId = UserInfoManager.getInstance(mContext).getUserid();
        String deviceId = DeviceUtils.getAndroidID();
        int bookId = (int) book.bookId;
        int chapterId = (int) book.chapterId;
        bookRackPresenter.getAlbumChapterDetail(userId, bookId, chapterId, deviceId);
    }

    private void openBook(LocalBookBean book) {
        if (book.isLocal) {
            File file = new File(book.path);
            if (!file.exists()) {
                Toast.makeText(mContext, "书籍不存在", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(mContext, LocalReadActivity.class);
            CollBookBean collBookBean = new CollBookBean();
            collBookBean.set_id(book.strId);
            collBookBean.setCover(book.path);
            collBookBean.setIsLocal(true);
            CollBookBean bookBean = BookRepository.getInstance().getCollBook(book.strId);
            if (null != bookBean) {
                collBookBean.setIsUpdate(bookBean.getIsUpdate());
            }
            // collBookBean.setLastChapter("开始阅读");
            // collBookBean.setLastRead("201908-12T11:30:46");
            collBookBean.setLatelyFollower(0);
            collBookBean.setRetentionRatio(0.0);
            collBookBean.setTitle(book.bookName);

            collBookBean.setUpdated(StringUtils.dateConvert(file.lastModified(), Constant.FORMAT_BOOK_DATE));
            collBookBean.setShortIntro("无");
            intent.putExtra(EXTRA_IS_COLLECTED, true);
            intent.putExtra(EXTRA_COLL_BOOK, collBookBean);
            startActivity(intent);
        } else {
            int pageIndex = 0;
            BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
            BookReadProgress bookReadProgress = bookReadProgressDao.loadByRowId(book.bookId);
            if (bookReadProgress != null) {
                pageIndex = bookReadProgress.pageIndex;
                book.chapterId = bookReadProgress.chapterId;
            }
//            Intent intent = new Intent(mContext, FileSystemActivity.class);
//            startActivity(intent);
            ReadActivity.lunchActivity(mActivity, book.bookId, book.chapterId, pageIndex, 1);
        }
    }

    public void setFilteParam(int statusid, int readecid, int numcid, int malecid) {
        isFilter = true;
        this.statusid = statusid;
        this.readecid = readecid;
        this.numcid = numcid;
        this.malecid = malecid;
    }

    @Override
    public void filterBookSuccess(BookRackResponse bookRackResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        myAdapter.clear();
        List<LocalBookBean> list = convertBooksBean(bookRackResponse.pagedata);
        myAdapter.addAll(list);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFilterConfig(BookRackFilterConfigResponse bookRackFilterConfigResponse) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSign:
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                } else if (tvSign.getVisibility() == View.VISIBLE) {
                    SigninPopupWindow signinPopupWindow = new SigninPopupWindow(mActivity);
                    signinPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.recommendLayout:
                startActivity(new Intent(mActivity, DailyRecommendActivity.class));
                break;
            case R.id.btnFindBook:
                ((MainFrameWorkActivity) mActivity).setCurrentItem(MainItemType.BOOK_STORE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_GET_FREE_READ_REQUEST_CODE) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            freeReadPresenter.getFreeReadPermission(userId);
        } else if (requestCode == BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_book_list, container, false);
        initRefresh(contentView);
        initRemind(contentView);
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd(contentView);
        }
        initRecyclerView(contentView);
        initRecommend(contentView);
        refresh();
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showGuide(mActivity);
    }

    public void showGuide(final Activity activity) {
        final AppSetting appSetting = AppSetting.getInstance(activity.getApplicationContext());
        boolean hasShowGuide = appSetting.getBoolean(TAG, false);
        if (!hasShowGuide) {
            appSetting.putBoolean(TAG, true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BookRackGuide readGuide = new BookRackGuide(activity);
                    readGuide.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

                }
            }, 100);
        }
    }

    private void initRefresh(View contentView) {
        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                if (isFilter) {
                    filterBook(page);
                } else {
                    loadBookList(page);
                }
                loadRemind();
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isFilter) {
                    filterBook(page);
                } else {
                    loadBookList(page);
                }
            }
        });
    }

    private void initRemind(View contentView) {
        mRemindFrameLayout = contentView.findViewById(R.id.remindFrameLayout);

        bookInfoLayout = contentView.findViewById(R.id.bookInfoLayout);
        tvBookName = contentView.findViewById(R.id.tvBookName);
        tvBookIntro = contentView.findViewById(R.id.tvBookIntro);
        ivRecomBook = contentView.findViewById(R.id.ivRecomBook);


        btnFindBook = contentView.findViewById(R.id.btnFindBook);
        btnFindBook.setOnClickListener(this);

        smartViewSwitcher = contentView.findViewById(R.id.smartViewSwitcher);

        tvSign = smartViewSwitcher.findViewById(R.id.tvSign);
        tvSign.setOnClickListener(this);
    }

    private void initCsjAd(View view) {
        adContainer1 = view.findViewById(R.id.adContainer1);

        initCsjAd(adContainer1);
    }

    private void initRecyclerView(View contentView) {
        bookrackRecyclerView = contentView.findViewById(R.id.bookrackRecyclerView);
        gridLayoutManager = new GridLayoutManager(mContext, 3);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_empty));
//        bookrackRecyclerView.addItemDecoration(dividerItemDecoration);

        currentViewType = AppSetting.getInstance(mContext).getInt(SharedKey.BOOK_RACK_VIEW_TYPE, VIEW_TYPE_GRID_LAYOUT);

        if (currentViewType == VIEW_TYPE_GRID_LAYOUT) {
            bookrackRecyclerView.setLayoutManager(gridLayoutManager);
            bookrackRecyclerView.addItemDecoration(new SpaceItemDecoration(35));
        } else {
            bookrackRecyclerView.setLayoutManager(linearLayoutManager);
        }

        myAdapter = new MyAdapter();
        bookrackRecyclerView.setAdapter(myAdapter);

        myAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (myAdapter.getItemCount() == 0) {
                    mRemindFrameLayout.setVisibility(View.VISIBLE);
                } else {
                    mRemindFrameLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initRecommend(View contentView) {
        contentView.findViewById(R.id.recommendLayout).setOnClickListener(this);
    }

    public void filterBook(int page) {
        AppSetting appSetting = AppSetting.getInstance(mContext);
        String kinds = appSetting.getString(SharedKey.HOBBY_KINDS, "");

        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
        map.put("statusid", statusid);
        map.put("readecid", readecid);
        map.put("numcid", numcid);
        map.put("malecid", malecid);
        map.put("classarr ", kinds);
        map.put("page", page);
        map.put("pagesize", PAGE_SIZE);
        map.put("channel", ApiConstant.Channel.ANDROID);
        bookFilterPresenter.filterBook(map);
    }

    private void loadBookList(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        int malechannel = AppSetting.getInstance(mContext).getInt(SharedKey.MALE_CHANNEL, BookStoreChannel.GIRL);
        bookRackPresenter.getBookList(userid, malechannel, page, 50);
    }

    private void loadRemind() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        freeReadPresenter.getFreeReadInfo(userId);
        bookRackPresenter.getRecommendBook(userId, 1);
    }

    private void initCsjAd(final FrameLayout frameLayout) {
        frameLayout.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(getContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());

        mTTAdList = new ArrayList<>();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ADConfig.CSJAdPositionId.BOOK_RACK_LIST_ID1) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        adNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 2 : " + code + ", " + message);
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAdList.add(mTTAd);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd, frameLayout);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("hhh", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("hhh", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("hhh", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("hhh", "渲染成功");
                frameLayout.removeAllViews();
                frameLayout.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, frameLayout);
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     */
    private void bindDislike(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.i("hhh", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                frameLayout.removeAllViews();
            }

            @Override
            public void onCancel() {
                Log.i("hhh", "点击取消");
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookFilterPresenter = new BookFilterPresenter();
        bookFilterPresenter.attachView(this);

        bookRackPresenter = new BookRackPresenter();
        bookRackPresenter.attachView(this);

        freeReadPresenter = new FreeReadPresenter();
        freeReadPresenter.attachView(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intentFilter.addAction(BroadcastAction.UPDATE_SIAN_INFO);
        intentFilter.addAction(BroadcastAction.REFRESH_BOOK_RACK_RANK);
        intentFilter.addAction(BroadcastAction.DELETE_BOOK_FROM_RACK);
        intentFilter.addAction(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
        intentFilter.addAction(BroadcastAction.UPDATE_READ_PROGRESS);
        intentFilter.addAction(BroadcastAction.UPDATE_LOCAL_BOOK);
        intentFilter.addAction(BroadcastAction.UPLOAD_WIFI_BOOK);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);

        readTracer = new ReadTracer(mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smartViewSwitcher.stopLooping();
        bookRackPresenter.detachView();
        bookFilterPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    public void refreshData() {
        refresh();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        ImageView ivMark;
        ImageView imgMusic;
        TextView tvBookName;
        TextView tvAuthorName;
        TextView tvReadInfo;
        TextView mTextBookName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivMark = itemView.findViewById(R.id.ivMark);
            imgMusic = itemView.findViewById(R.id.img_music);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvReadInfo = itemView.findViewById(R.id.tvReadInfo);
            mTextBookName = itemView.findViewById(R.id.text_book_name);
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<LocalBookBean, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == VIEW_TYPE_GRID_LAYOUT) {
                view = getLayoutInflater().inflate(R.layout.book_rack_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.book_rack_item_linear, parent, false);
            }
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final LocalBookBean book = getItem(position);
            if (null != book) {
                if (book.recState == 1) {
                    holder.ivMark.setImageResource(R.drawable.recommend);
                } else if (book.updating == 1) {
                    holder.ivMark.setImageResource(R.drawable.update);
                } else {
                    holder.ivMark.setVisibility(View.GONE);
                }

                if (book.booktype == 1) {
                    holder.imgMusic.setVisibility(View.VISIBLE);
                } else {
                    holder.imgMusic.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (book.booktype == 1) {
                            listenBook(book);
                        } else {
                            openBook(book);
                        }
                        refreshData(book);
                    }
                });
                holder.tvBookName.setText(book.bookName);
                if (holder.tvAuthorName != null) {
                    holder.tvAuthorName.setText(getString(R.string.author_zhu, book.author));
                }
                if (book.chapterNumber == 0) {
                    if (book.booktype == 1) {
                        holder.tvReadInfo.setText(R.string.un_listen);
                    } else {
                        holder.tvReadInfo.setText(R.string.unread2);
                    }
                } else {
                    if (book.booktype == 1) {
                        holder.tvReadInfo.setText(getString(R.string.listen_chapter_num, book.chapterNumber));
                    } else {
                        holder.tvReadInfo.setText(getString(R.string.read_charpter_num, book.chapterNumber));
                    }
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
        }

        @Override
        public int getItemViewType(int position) {
            return currentViewType;
        }
    }
}
