package com.shangame.fiction.ui.contents;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.ChapterDetailResponse;
import com.shangame.fiction.net.response.ChapterListResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.Chapter;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.widget.SideBar;

import java.util.Collections;

/**
 * 书籍目录
 * Create by Speedy on 2018/8/1
 */
public class BookChapterListFragment extends BaseFragment implements View.OnClickListener, BookChapterContact.View {

    private static final String ARG_PARAM1 = "bookid";
    private static final String ARG_PARAM2 = "currentChapterId";
    private static final int PAGE_SIZE = 100;
    private RecyclerView recyclerView;
    private ContentsAdapter myAdapter;
    private LinearLayoutManager linearLayoutManager;
    private BookChapterPresenter mBookChapterPresenter;
    private long bookid;
    private long chapterId;
    private int mPage = 1;
    private TextView tvBookState;
    private TextView tvVolumeSize;

    private OnChapterCheckedListener onChapterCheckedListener;

    private View contentLayout;

    private SideBar sideBar;

    private long currentChapterId;

    public static BookChapterListFragment newInstance(long bookid, long currentChapterId) {
        BookChapterListFragment fragment = new BookChapterListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, bookid);
        args.putLong(ARG_PARAM2, currentChapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE && resultCode == ReadActivity.RESULT_OK) {
            ReadActivity.lunchActivity(mActivity, bookid, chapterId);
            if (null != getActivity()) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookid = getArguments().getLong(ARG_PARAM1);
            currentChapterId = getArguments().getLong(ARG_PARAM2);
        }
        mBookChapterPresenter = new BookChapterPresenter();
        mBookChapterPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_book_chapter_list, container, false);
        tvBookState = contentView.findViewById(R.id.tvBookState);
        tvVolumeSize = contentView.findViewById(R.id.tvVolumeSize);
        contentLayout = contentView.findViewById(R.id.contentLayout);
        contentView.findViewById(R.id.tvOrder).setOnClickListener(this);
        initRecyclerView(contentView);
        initSideBar(contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestChapterList(1, 0);
    }

    private void requestChapterList(int page, int pageSize) {
        mBookChapterPresenter.getChapterList(UserInfoManager.getInstance(mContext).getUserid(), bookid, page, pageSize);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBookChapterPresenter.detachView();
    }

    private void initRecyclerView(View contentView) {
        recyclerView = contentView.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new ContentsAdapter(mActivity, currentChapterId);
        myAdapter.setOnItemClickListener(new ContentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chapter chapter) {
                if (onChapterCheckedListener == null) {
                    if (chapter.chargingmode == ApiConstant.ChapterAuth.FREEE_READ) {
                        ReadActivity.lunchActivity(mActivity, bookid, (int) chapter.cid);
                    } else {
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        if (userId == 0) {
                            chapterId = chapter.cid;
                            Intent mIntent = new Intent(mContext, LoginActivity.class);
                            startActivityForResult(mIntent, BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
                        } else {
                            ReadActivity.lunchActivity(mActivity, bookid, (int) chapter.cid);
                        }
                    }
                } else {
                    onChapterCheckedListener.checkedChapter(chapter, 1);
                }
            }
        });
        recyclerView.setAdapter(myAdapter);
    }

    private void initSideBar(View contentView) {
        sideBar = contentView.findViewById(R.id.sideBar);
        sideBar.setRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvOrder) {
            Collections.reverse(myAdapter.getData());
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getChapterListSuccess(ChapterListResponse response) {
        tvVolumeSize.setText(getString(R.string.total_chapter, response.chapternumber));
        tvBookState.setText(response.serstatus);

        contentLayout.setVisibility(View.VISIBLE);

        if (mPage == 1) {
            myAdapter.clear();
        }

        myAdapter.addAll(response.pagedata);
        myAdapter.notifyDataSetChanged();

        int currentPosition = myAdapter.getCurrentPosition();

        recyclerView.scrollToPosition(currentPosition);

        if (response.chapternumber < 30) {
            sideBar.setVisibility(View.GONE);
        } else {
            sideBar.setVisibility(View.VISIBLE);
        }
        sideBar.setItemCount(myAdapter.getItemCount());
    }

    @Override
    public void getChapterDetailSuccess(ChapterDetailResponse chapterDetailResponse) {
        AppSetting.getInstance(mContext).putInt(SharedKey.NO_AD_TIME, chapterDetailResponse.advertmin);
    }

    public void setOnChapterCheckedListener(OnChapterCheckedListener onChapterCheckedListener) {
        this.onChapterCheckedListener = onChapterCheckedListener;
    }
}
