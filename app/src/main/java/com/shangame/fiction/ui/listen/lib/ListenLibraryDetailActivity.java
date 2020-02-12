package com.shangame.fiction.ui.listen.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ListenNormalAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.BookLibraryFilterTypeResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.booklib.BookLibraryType;
import com.shangame.fiction.widget.LabelsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 听书书库详情
 * Create by Speedy on 2018/7/25
 *
 * @author hhh
 */
public class ListenLibraryDetailActivity extends BaseActivity implements View.OnClickListener, ListenLibraryDetailContracts.View {
    private SmartRefreshLayout smartRefreshLayout;

    private LabelsView subClassLabelsView;
    private LabelsView sortLabelsView;
    private LabelsView serLabelsView;

    private ListenNormalAdapter mAdapter;
    private List<AlbumDataResponse.PageDataBean> mList = new ArrayList<>();

    private int page = 1;

    private ListenLibraryDetailPresenter mPresenter;

    private int superClassId;
    private String title;

    private int classId = 0;
    private int sortCid = 0;
    private int serCid = 0;

    private TextView tvAllClass;
    private TextView tvAllSort;
    private TextView tvAllSer;


    public static void lunchActivity(Activity activity, int superClassId, String title) {
        Intent intent = new Intent(activity, ListenLibraryDetailActivity.class);
        intent.putExtra("superClassId", superClassId);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_library_detail);
        initParam();
        initTitle();
        initTagView();
        initSmartRefreshLayout();
        initListView();
        initPresenter();
        loadTags();
    }

    private void initParam() {
        superClassId = getIntent().getIntExtra("superClassId", BookLibraryType.XIAN_YAN);
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

        tvAllSort = findViewById(R.id.tvAllsort);
        tvAllSort.setOnClickListener(this);

        tvAllSer = findViewById(R.id.tvAllser);
        tvAllSer.setOnClickListener(this);

        subClassLabelsView = findViewById(R.id.subClassLabelsView);
        sortLabelsView = findViewById(R.id.sortLabelsView);
        serLabelsView = findViewById(R.id.serLabelsView);

        subClassLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.SubclassdataBean) {
                    BookLibraryFilterTypeResponse.SubclassdataBean bean = (BookLibraryFilterTypeResponse.SubclassdataBean) data;
                    classId = bean.classid;
                    changeAllState(tvAllClass, false);
                    loadBook();
                }
            }
        });

        sortLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.SortdataBean) {
                    BookLibraryFilterTypeResponse.SortdataBean bean = (BookLibraryFilterTypeResponse.SortdataBean) data;
                    sortCid = bean.cid;
                    changeAllState(tvAllSort, false);
                    loadBook();
                }
            }
        });

        serLabelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (data instanceof BookLibraryFilterTypeResponse.SerdataBean) {
                    BookLibraryFilterTypeResponse.SerdataBean bean = (BookLibraryFilterTypeResponse.SerdataBean) data;
                    serCid = bean.cid;
                    changeAllState(tvAllSer, false);
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

        mAdapter = new ListenNormalAdapter(R.layout.item_end_listen, mList);
        listView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlbumDataResponse.PageDataBean bean = mList.get(position);
                if (null != bean) {
                    ARouter.getInstance()
                            .build("/ss/listen/detail")
                            .withInt("albumId", bean.albumid)
                            .navigation();
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new ListenLibraryDetailPresenter();
        mPresenter.attachView(this);
    }

    private void loadTags() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumLibraryType(userId, superClassId);
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
        mList.clear();
        mAdapter.setNewData(mList);
        smartRefreshLayout.autoRefresh();
    }

    private void filterBookByType(int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
        map.put("superclassid", superClassId);
        map.put("classid", classId);
        map.put("sortcid", sortCid);
        map.put("sercid", serCid);
        map.put("page", page);
        map.put("pagesize", PAGE_SIZE);
        map.put("platform", ApiConstant.Platform.APP);
        map.put("channel", ApiConstant.Channel.ANDROID);
        mPresenter.filterAlbumByType(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
        }
        if (id == R.id.tvAllClass) {
            subClassLabelsView.clearAllSelect();
            classId = 0;
            changeAllState(tvAllClass, true);
            loadBook();
        } else if (id == R.id.tvAllsort) {
            sortLabelsView.clearAllSelect();
            sortCid = 0;
            changeAllState(tvAllSort, true);
            loadBook();
        } else if (id == R.id.tvAllser) {
            serLabelsView.clearAllSelect();
            serCid = 0;
            changeAllState(tvAllSer, true);
            loadBook();
        }
    }

    @Override
    public void getAlbumLibraryTypeSuccess(BookLibraryFilterTypeResponse response) {
        subClassLabelsView.setLabels(response.subclassdata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.SubclassdataBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.SubclassdataBean data) {
                return data.classname;
            }
        });

        sortLabelsView.setLabels(response.sortdata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.SortdataBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.SortdataBean data) {
                return data.configname;
            }
        });

        serLabelsView.setLabels(response.serdata, new LabelsView.LabelTextProvider<BookLibraryFilterTypeResponse.SerdataBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, BookLibraryFilterTypeResponse.SerdataBean data) {
                return data.configname;
            }
        });

        subClassLabelsView.clearAllSelect();
        sortLabelsView.clearAllSelect();
        serLabelsView.clearAllSelect();

        for (int i = 0; i < response.subclassdata.size(); i++) {
            if (response.classid == response.subclassdata.get(i).classid) {
                subClassLabelsView.setSelects(i);
            }
        }
        loadBook();
    }

    @Override
    public void filterAlbumByTypeSuccess(AlbumDataResponse response) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            mList.clear();
        }
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }
}
