package com.shangame.fiction.ui.wifi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.core.utils.media.MediaStoreHelper;
import com.shangame.fiction.net.response.Recommend;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.model.LocalBookBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能扫描
 */
public class LocalBookFragment extends BaseFragment implements View.OnClickListener {
    private FileSystemAdapter mAdapter;
    private List<Recommend> mList = new ArrayList<>();
    private Button mBtnSelectedAll;
    private Button mBtnAddBook;
    private int mImportSize;

    private boolean isSelectAll;

    public LocalBookFragment() {
        // Required empty public constructor
    }

    public static LocalBookFragment newInstance(String arg1) {
        LocalBookFragment fragment = new LocalBookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", arg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_local_book, container, false);
        initView(contentView);
        initListener();
        processLogic();
        return contentView;
    }

    private void initListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Recommend recommend = mList.get(position);
                switch (view.getId()) {
                    case R.id.image_check: {
                        File file = recommend.file;
                        String path = file.getAbsolutePath();
                        if (getCollBook(path) != null) {
                            return;
                        }
                        recommend.isClick = !recommend.isClick;
                        mAdapter.notifyDataSetChanged();
                        changeBottomStatus();
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Recommend recommend = mList.get(position);
                File file = recommend.file;
                String path = file.getAbsolutePath();
                if (getCollBook(path) != null) {
                    return;
                }
                recommend.isClick = !recommend.isClick;
                mAdapter.notifyDataSetChanged();
                changeBottomStatus();
            }
        });
        mBtnSelectedAll.setOnClickListener(this);
        mBtnAddBook.setOnClickListener(this);
    }

    private LocalBookBean getCollBook(String path) {
        path = MD5Utils.strToMd5By16(path);
        LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
        LocalBookBean bean = dao.queryBuilder().where(LocalBookBeanDao.Properties.StrId.eq(path)).unique();
        return bean;
    }

    private void initView(View contentView) {
        RecyclerView recyclerLocal = contentView.findViewById(R.id.recycler_local);
        recyclerLocal.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new FileSystemAdapter(R.layout.item_local_book, mList);
        recyclerLocal.setAdapter(mAdapter);

        mBtnSelectedAll = contentView.findViewById(R.id.btn_selected_all);
        mBtnAddBook = contentView.findViewById(R.id.btn_add_book);
    }

    private void changeBottomStatus() {
        if (null != mList && mList.size() > 0) {
            int size = 0;
            int total = 0;
            for (int i = 0; i < mList.size(); i++) {
                Recommend bean = mList.get(i);
                File file = bean.file;
                if (!file.isDirectory()) {
                    String path = file.getAbsolutePath();
                    if (getCollBook(path) == null) {
                        total++;
                        if (bean.isClick) {
                            size++;
                        }
                    }
                }
            }
            if (size > 0) {
                mBtnAddBook.setText("加入书架" + size);
                mBtnAddBook.setBackgroundColor(Color.parseColor("#F46464"));
                if (total == size) {
                    isSelectAll = true;
                    mBtnSelectedAll.setText("取消全选");
                } else {
                    isSelectAll = false;
                    mBtnSelectedAll.setText("全选");
                }
            } else {
                mBtnSelectedAll.setText("全选");
                mBtnAddBook.setText("加入书架");
                mBtnAddBook.setBackgroundColor(Color.parseColor("#E0E0E0"));
            }
        } else {
            mBtnSelectedAll.setText("全选");
            mBtnAddBook.setText("加入书架");
            mBtnAddBook.setBackgroundColor(Color.parseColor("#E0E0E0"));
        }
    }

    private void processLogic() {
        if (null != getActivity()) {
            MediaStoreHelper.getAllBookFile(getActivity(), new MediaStoreHelper.MediaResultCallback() {
                @Override
                public void onResultCallback(List<File> files) {
                    List<Recommend> list = new ArrayList<>();
                    if (files.isEmpty()) {
                        Log.e("hhh", "files.isEmpty()");
                    } else {
                        for (File file : files) {
                            Recommend recommend = new Recommend();
                            recommend.file = file;
                            recommend.isClick = false;
                            list.add(recommend);
                        }
                        mImportSize = 0;
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.setNewData(mList);
                    }
                }
            });
        }
    }

    private List<LocalBookBean> convertBooksBean(List<Recommend> recommends) {
        List<LocalBookBean> beanList = new ArrayList<>(recommends.size());
        for (Recommend recommend : recommends) {
            File file = recommend.file;
            if (!file.exists()) {
                continue;
            }
            LocalBookBean booksBean = new LocalBookBean();
            booksBean.strId = MD5Utils.strToMd5By16(file.getAbsolutePath());
            booksBean.bookName = file.getName().replace(".txt", "");
            booksBean.bookId = -1;
            booksBean.author = "";
            booksBean.bookCover = "";
            booksBean.isLocal = true;
            booksBean.lastModifyTime = System.currentTimeMillis();
            booksBean.path = file.getAbsolutePath();
            beanList.add(booksBean);
        }
        return beanList;
    }

    private void selectAll() {
        if (null != mList && !mList.isEmpty()) {
            isSelectAll = !isSelectAll;
            for (int i = 0; i < mList.size(); i++) {
                Recommend bean = mList.get(i);
                File file = bean.file;
                if (!file.isDirectory()) {
                    String path = file.getAbsolutePath();
                    if (getCollBook(path) == null) {
                        bean.isClick = isSelectAll;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        changeBottomStatus();
    }

    private void addBook() {
        List<Recommend> recommends = new ArrayList<>();
        if (null != mList && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                Recommend recommend = mList.get(i);
                if (recommend.isClick) {
                    File file = recommend.file;
                    String path = file.getAbsolutePath();
                    if (getCollBook(path) == null) {
                        recommends.add(recommend);
                    }
                }
            }

            if (recommends.size() > 0) {
                List<LocalBookBean> list = convertBooksBean(recommends);
                LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
                dao.insertOrReplaceInTx(list);

                Toast.makeText(mContext, getResources().getString(R.string.file_add_succeed, recommends.size()), Toast.LENGTH_SHORT).show();
                changeBottomStatus();

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(BroadcastAction.UPDATE_LOCAL_BOOK));
            } else {
                Toast.makeText(mContext, "请选择导入的书籍", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_selected_all) {
            selectAll();
        }
        if (v.getId() == R.id.btn_add_book) {
            addBook();
        }
    }
}
