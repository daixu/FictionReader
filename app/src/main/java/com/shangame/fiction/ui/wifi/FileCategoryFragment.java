package com.shangame.fiction.ui.wifi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.Constant;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.net.response.FileStack;
import com.shangame.fiction.net.response.Recommend;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.model.LocalBookBean;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 系统目录
 *
 * @author hhh
 */
public class FileCategoryFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private TextView mTvPath;
    private TextView mTvBackLast;
    private Button mBtnSelectedAll;
    private Button mBtnAddBook;
    private FileStack mFileStack;
    private RecyclerView mRecyclerFileCategory;
    private FileSystemAdapter mAdapter;

    private boolean isSelectAll;

    private List<Recommend> mList = new ArrayList<>();

    public FileCategoryFragment() {
        // Required empty public constructor
    }

    public static FileCategoryFragment newInstance(String param1) {
        FileCategoryFragment fragment = new FileCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_file_category, container, false);
        initView(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFileStack = new FileStack();
        initListener();
        processLogic();

        Log.e("hhh", "mParam1= " + mParam1);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Recommend recommend = mList.get(position);
                File file = recommend.file;
                if (file.isDirectory()) {
                    //保存当前信息。
                    FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                    snapshot.filePath = mTvPath.getText().toString();
                    List<Recommend> list = mAdapter.getItems();
                    List<File> files = new ArrayList<>();
                    for (Recommend recommend1 : list) {
                        File file1 = recommend1.file;
                        files.add(file1);
                    }
                    snapshot.files = files;
                    snapshot.scrollOffset = mRecyclerFileCategory.computeVerticalScrollOffset();
                    mFileStack.push(snapshot);
                    //切换下一个文件
                    toggleFileTree(file);
                } else {
                    //如果是已加载的文件，则点击事件无效。
                    recommend.isClick = !recommend.isClick;
                    mAdapter.notifyDataSetChanged();
                    changeBottomStatus();
                }
            }
        });

        mTvBackLast.setOnClickListener(this);
        mBtnSelectedAll.setOnClickListener(this);
        mBtnAddBook.setOnClickListener(this);
    }

    protected void processLogic() {
        File root = Environment.getExternalStorageDirectory();
        toggleFileTree(root);
    }

    private void toggleFileTree(File file) {
        //路径名
        mTvPath.setText(getString(R.string.file_path, file.getPath()));
        //获取数据
        File[] files = file.listFiles(new SimpleFileFilter());
        List<Recommend> list = new ArrayList<>();
        if (null != files) {
            //转换成List
            List<File> rootFiles = Arrays.asList(files);
            //排序
            Collections.sort(rootFiles, new FileComparator());
            //加入
            for (File file1 : rootFiles) {
                Recommend recommend = new Recommend();
                recommend.file = file1;
                recommend.isClick = false;
                list.add(recommend);
            }
        }

        if (mFileStack.getSize() <= 0) {
            mTvBackLast.setVisibility(View.GONE);
        } else {
            mTvBackLast.setVisibility(View.VISIBLE);
        }

        mBtnSelectedAll.setText("全选");
        isSelectAll = false;
        mBtnAddBook.setText("加入书架");
        mBtnAddBook.setBackgroundColor(Color.parseColor("#E0E0E0"));

        mList.clear();
        mList.addAll(list);
        mAdapter.setNewData(mList);
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
                String strSize = "加入书架" + size;
                mBtnAddBook.setText(strSize);
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

    private LocalBookBean getCollBook(String path) {
        path = MD5Utils.strToMd5By16(path);
        LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
        LocalBookBean bean = dao.queryBuilder().where(LocalBookBeanDao.Properties.StrId.eq(path)).unique();
        Log.e("hhh", "bean= " + bean.bookName);
        return bean;
    }

    private void initView(View contentView) {
        mTvPath = contentView.findViewById(R.id.tv_path);
        mTvBackLast = contentView.findViewById(R.id.tv_back_last);
        mRecyclerFileCategory = contentView.findViewById(R.id.recycler_file_category);
        mBtnSelectedAll = contentView.findViewById(R.id.btn_selected_all);
        mBtnAddBook = contentView.findViewById(R.id.btn_add_book);

        mRecyclerFileCategory.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new FileSystemAdapter(R.layout.item_local_book, mList);
        mRecyclerFileCategory.setAdapter(mAdapter);
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

    private void backLast() {
        FileStack.FileSnapshot snapshot = mFileStack.pop();
        if (mFileStack.getSize() <= 0) {
            mTvBackLast.setVisibility(View.GONE);
        } else {
            mTvBackLast.setVisibility(View.VISIBLE);
        }
        int oldScrollOffset = mRecyclerFileCategory.computeHorizontalScrollOffset();
        if (snapshot == null) {
            return;
        }
        mTvPath.setText(snapshot.filePath);
        List<Recommend> recommends = new ArrayList<>();
        for (File file : snapshot.files) {
            Recommend recommend = new Recommend();
            recommend.file = file;
            recommend.isClick = false;
            recommends.add(recommend);
        }
        mList.clear();
        mList.addAll(recommends);
        mAdapter.setNewData(mList);
        mBtnSelectedAll.setText("全选");
        isSelectAll = false;
        mBtnAddBook.setText("加入书架");
        mBtnAddBook.setBackgroundColor(Color.parseColor("#E0E0E0"));
        mRecyclerFileCategory.scrollBy(0, snapshot.scrollOffset - oldScrollOffset);
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
        if (v.getId() == R.id.tv_back_last) {
            backLast();
        }
        if (v.getId() == R.id.btn_selected_all) {
            selectAll();
        }
        if (v.getId() == R.id.btn_add_book) {
            addBook();
        }
    }

    public class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o2.isDirectory() && o1.isFile()) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public class SimpleFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(".")) {
                return false;
            }
            //文件夹内部数量为0
            if (pathname.isDirectory() && pathname.list().length == 0) {
                return false;
            }

            /*
             * 现在只支持TXT文件的显示
             */
            //文件内容为空,或者不以txt为开头
            if (!pathname.isDirectory() && (pathname.length() == 0 || !pathname.getName().endsWith(Constant.SUFFIX_TXT))) {
                return false;
            }
            return true;
        }
    }
}
