package com.shangame.fiction.ui.contents;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookMark;
import com.shangame.fiction.ui.common.MenuPopupWindow;
import com.shangame.fiction.ui.reader.ReadActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 书签目录
 * Create by Speedy on 2018/8/1
 */
public class BookMarkFragment extends BaseFragment implements BookMarkContacts.View {

    private static final String ARG_PARAM1 = "param1";

    private BookMarkPresenter bookMarkPresenter;

    private SmartRefreshLayout smartRefreshLayout;

    private BookMarkAdapter myAdapter;

    private long bookId;
    private int page = 1;

    private OnBookMarkCheckedLinstener onBookMarkCheckedLinstener;

    public static BookMarkFragment newInstance(long bookid) {
        BookMarkFragment fragment = new BookMarkFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, bookid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getLong(ARG_PARAM1);
        }
        bookMarkPresenter = new BookMarkPresenter(mContext);
        bookMarkPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_mark, container, false);
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getBookMarkList(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getBookMarkList(page);
            }
        });

        ListView listView = view.findViewById(R.id.listView);
        myAdapter = new BookMarkAdapter(mActivity);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookMark bookMark = myAdapter.getItem(position);
                if (onBookMarkCheckedLinstener == null) {
                    ReadActivity.lunchActivity(mActivity, bookId, bookMark.chapterid, bookMark);
                    mActivity.finish();
                } else {
                    onBookMarkCheckedLinstener.checkedBookMark(bookMark);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final List<String> itemList = new ArrayList<>();
                itemList.add("删除");
                final MenuPopupWindow menuPopupWindow = new MenuPopupWindow(mActivity, itemList);
                menuPopupWindow.setOnItemClickListener(new MenuPopupWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        menuPopupWindow.dismiss();
                        BookMark bookMark = myAdapter.getItem(position);
                        bookMarkPresenter.removeBookMark(bookMark);
                        myAdapter.remove(bookMark);
                        myAdapter.notifyDataSetChanged();
                    }
                });
                menuPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                return true;
            }
        });
        return view;
    }

    private void getBookMarkList(int page) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        bookMarkPresenter.getBookMarkList(userId, bookId, page, PAGE_SIZE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookMarkPresenter.detachView();
    }

    @Override
    public void addBookMarkSuccess() {

    }

    @Override
    public void removeBookMarkSuccess() {

    }

    @Override
    public void getBookMarkListSuccess(List<BookMark> bookMarkList) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();

        if (page == 1) {
            myAdapter.clear();
        }
        myAdapter.addAll(bookMarkList);
        myAdapter.notifyDataSetChanged();
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
        if (isVisible()) {
            super.lunchLoginActivity();
        }
    }

    public void setOnBookMarkCheckedLinstener(OnBookMarkCheckedLinstener onBookMarkCheckedLinstener) {
        this.onBookMarkCheckedLinstener = onBookMarkCheckedLinstener;
    }
}
