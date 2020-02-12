package com.shangame.fiction.ui.booklib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.BookLibraryFilterTypeResponse;
import com.shangame.fiction.net.response.LibFilterBookByTypeResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookstore.BookWithContentAdapter;
import com.shangame.fiction.widget.LabelsView;

import java.util.HashMap;
import java.util.Map;

/**
 * 书库详情
 * Create by Speedy on 2018/7/25
 */
public class BookLibraryDetailActivity extends BaseActivity implements View.OnClickListener, BookLibraryDetailContracts.View {

    private SmartRefreshLayout smartRefreshLayout;

    private LabelsView subClassLabelsView;
    private LabelsView wordLabelsView;
    private LabelsView sortLabelsView;
    private LabelsView serLabelsView;

    private BookWithContentAdapter bookWithContentAdapter;

    private int page = 1;

    private BookLibraryDetailPresenter bookLibraryDetailPresenter;

    private int superclassid;
    private String title;

    private int classid = 0;
    private int wordcid = 0;
    private int sortcid = 0;
    private int sercid = 0;

    private TextView tvAllClass;
    private TextView tvAllword;
    private TextView tvAllsort;
    private TextView tvAllser;

    public static final void lunchActivity(Activity activity, int superclassid, String title) {
        Intent intent = new Intent(activity, BookLibraryDetailActivity.class);
        intent.putExtra("superclassid", superclassid);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    public static final void lunchActivity(Context context, int superClassId, String title) {
        Intent intent = new Intent(context, BookLibraryDetailActivity.class);
        intent.putExtra("superclassid", superClassId);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_library_detail);
        initParam();
        initTitle();
        initTagView();
        initSmartRefreshLayout();
        initListView();
        initPresenter();
        loadTags();
    }

    private void initParam() {
        superclassid = getIntent().getIntExtra("superclassid", BookLibraryType.XIAN_YAN);
        title = getIntent().getStringExtra("title");
    }

    private void initTitle() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        if (TextUtils.isEmpty(title)) {
            tvPublicTitle.setText(R.string.book_library);
        } else {
            tvPublicTitle.setText(title);
        }
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
    }

    private void initTagView() {
        tvAllClass = findViewById(R.id.tvAllClass);
        tvAllClass.setOnClickListener(this);

        tvAllword = findViewById(R.id.tvAllword);
        tvAllword.setOnClickListener(this);

        tvAllsort = findViewById(R.id.tvAllsort);
        tvAllsort.setOnClickListener(this);

        tvAllser = findViewById(R.id.tvAllser);
        tvAllser.setOnClickListener(this);

        subClassLabelsView = findViewById(R.id.subClassLabelsView);
        wordLabelsView = findViewById(R.id.wordLabelsView);
        sortLabelsView = findViewById(R.id.sortLabelsView);
        serLabelsView = findViewById(R.id.serLabelsView);

        subClassLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.SubclassdataBean) {
                    BookLibraryFilterTypeResponse.SubclassdataBean subclassdataBean = (BookLibraryFilterTypeResponse.SubclassdataBean) data;
                    classid = subclassdataBean.classid;
                    changeAllState(tvAllClass, false);
                    loadBook();
                }
            }
        });

        wordLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.WorddataBean) {
                    BookLibraryFilterTypeResponse.WorddataBean worddataBean = (BookLibraryFilterTypeResponse.WorddataBean) data;
                    wordcid = worddataBean.cid;
                    changeAllState(tvAllword, false);
                    loadBook();
                }
            }
        });

        sortLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.SortdataBean) {
                    BookLibraryFilterTypeResponse.SortdataBean sortdataBean = (BookLibraryFilterTypeResponse.SortdataBean) data;
                    sortcid = sortdataBean.cid;
                    changeAllState(tvAllsort, false);
                    loadBook();
                }
            }
        });

        serLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.SerdataBean) {
                    BookLibraryFilterTypeResponse.SerdataBean serdataBean = (BookLibraryFilterTypeResponse.SerdataBean) data;
                    sercid = serdataBean.cid;
                    changeAllState(tvAllser, false);
                    loadBook();
                }
            }
        });
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                filterBookByType(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                filterBookByType(page);
            }
        });
    }

    private void initListView() {
        RecyclerView listView = findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        listView.addItemDecoration(dividerItemDecoration);

        bookWithContentAdapter = new BookWithContentAdapter(mActivity);
        listView.setAdapter(bookWithContentAdapter);
    }

    private void initPresenter() {
        bookLibraryDetailPresenter = new BookLibraryDetailPresenter();
        bookLibraryDetailPresenter.attachView(this);
    }

    private void loadTags() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        bookLibraryDetailPresenter.getFilterType(userId, superclassid);
    }

    private void changeAllState(TextView textView, boolean checked) {
        if (checked) {
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            textView.setBackgroundResource(R.drawable.border_theme_color_bg);
        } else {
            textView.setTextColor(getResources().getColor(R.color.text_normal_color));
            textView.setBackground(null);
        }
    }

    private void loadBook() {
        bookWithContentAdapter.clear();
        bookWithContentAdapter.notifyDataSetChanged();
        smartRefreshLayout.autoRefresh();
    }

    private void filterBookByType(int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
        map.put("superclassid", superclassid);
        map.put("classid", classid);
        map.put("wordcid", wordcid);
        map.put("sortcid", sortcid);
        map.put("sercid", sercid);
        map.put("page", page);
        map.put("pagesize", PAGE_SIZE);
        map.put("platform", ApiConstant.Platform.APP);
        map.put("channel", ApiConstant.Channel.ANDROID);
        bookLibraryDetailPresenter.filterBookByType(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookLibraryDetailPresenter.detachView();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if (id == R.id.ivPublicBack) {
            finish();
        } else if (id == R.id.tvAllClass) {
            subClassLabelsView.clearAllSelect();
            classid = 0;
            changeAllState(tvAllClass, true);
            loadBook();
        } else if (id == R.id.tvAllword) {
            wordLabelsView.clearAllSelect();
            wordcid = 0;
            changeAllState(tvAllword, true);
            loadBook();
        } else if (id == R.id.tvAllsort) {
            sortLabelsView.clearAllSelect();
            sortcid = 0;
            changeAllState(tvAllsort, true);
            loadBook();
        } else if (id == R.id.tvAllser) {
            serLabelsView.clearAllSelect();
            sercid = 0;
            changeAllState(tvAllser, true);
            loadBook();
        }
    }

    @Override
    public void getFilterTypeSuccess(BookLibraryFilterTypeResponse bookLibraryFilterTypeResponse) {
        subClassLabelsView.setLabels(bookLibraryFilterTypeResponse.subclassdata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.SubclassdataBean>() {

            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.SubclassdataBean data) {
                return data.classname;
            }
        });

        wordLabelsView.setLabels(bookLibraryFilterTypeResponse.worddata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.WorddataBean>() {

            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.WorddataBean data) {
                return data.configname;
            }
        });

        sortLabelsView.setLabels(bookLibraryFilterTypeResponse.sortdata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.SortdataBean>() {

            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.SortdataBean data) {
                return data.configname;
            }
        });

        serLabelsView.setLabels(bookLibraryFilterTypeResponse.serdata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.SerdataBean>() {

            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.SerdataBean data) {
                return data.configname;
            }
        });

        subClassLabelsView.clearAllSelect();
        wordLabelsView.clearAllSelect();
        sortLabelsView.clearAllSelect();
        serLabelsView.clearAllSelect();
        loadBook();
    }

    @Override
    public void filterBookByTypeSuccess(LibFilterBookByTypeResponse libFilterBookByTypeResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            bookWithContentAdapter.clear();
        }
        bookWithContentAdapter.addAll(libFilterBookByTypeResponse.pagedata);
        bookWithContentAdapter.notifyDataSetChanged();
    }
}
