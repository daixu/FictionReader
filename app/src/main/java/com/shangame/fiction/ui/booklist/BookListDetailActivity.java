package com.shangame.fiction.ui.booklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.storage.model.BookListDetailResponse;
import com.shangame.fiction.storage.model.BookListEnitiy;
import com.shangame.fiction.storage.model.BookListResponse;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookrack.AddToBookRackContacts;
import com.shangame.fiction.ui.bookrack.AddToBookRackPresenter;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;

public class BookListDetailActivity extends BaseActivity implements View.OnClickListener
        , BookListContact.View, AddToBookRackContacts.View, TaskAwardContacts.View {

    private static final int FROM_ADD_TO_BOOK_RACK = 601;

    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    private int page = 1;

    private BookListEnitiy bookListEnitiy;

    private BookListPresenter bookListPresenter;

    private TextView tvContent;
    private ImageView ivArrow;

    private AddToBookRackPresenter addToBookRackPresenter;

    private TaskAwardPresenter taskAwardPresenter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.ADD_BOOK_TO_RACK_ACTION.equals(action)) {
                long bookId = intent.getLongExtra("bookId", 0);
                updataList(bookId);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.book_list_header);
        setContentView(R.layout.activity_book_list_detail);
        findViewById(R.id.ivBack).setOnClickListener(this);
        initTitle();
        initSmartRefreshLayout();
        initRecyclerView();
        initReceiver();
        initPresenter();
        mSmartRefreshLayout.autoRefresh();
    }

    private void initTitle() {
        bookListEnitiy = getIntent().getParcelableExtra("BookListEntity");

        TextView tvName = findViewById(R.id.tvName);
        tvContent = findViewById(R.id.tvContent);
        ivArrow = findViewById(R.id.ivArrow);

        TextView tvKind1 = findViewById(R.id.tvKind1);
        TextView tvKind2 = findViewById(R.id.tvKind2);
        TextView tvKind3 = findViewById(R.id.tvKind3);
        TextView tvKind4 = findViewById(R.id.tvKind4);

        tvName.setText(bookListEnitiy.title);
        tvContent.setText(bookListEnitiy.contents);
        String kindString = bookListEnitiy.classname + "," + bookListEnitiy.bookcount + "本";
        String[] kinds = kindString.split(",");
        tvKind1.setText(kinds[0]);
        tvKind2.setText(kinds[1]);
        tvKind3.setText(kinds[2]);

        tvKind1.setVisibility(View.GONE);
        tvKind2.setVisibility(View.GONE);
        tvKind3.setVisibility(View.GONE);
        tvKind4.setVisibility(View.GONE);

        for (int i = 0; i < kinds.length; i++) {
            switch (i) {
                case 0:
                    tvKind1.setText(kinds[0]);
                    tvKind1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvKind2.setText(kinds[1]);
                    tvKind2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvKind3.setText(kinds[2]);
                    tvKind3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    tvKind4.setText(kinds[3]);
                    tvKind4.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        tvContent.setOnClickListener(this);
        ivArrow.setOnClickListener(this);
    }

    private void initSmartRefreshLayout() {
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData(page);
            }
        });

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(page);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void initPresenter() {
        bookListPresenter = new BookListPresenter();
        bookListPresenter.attachView(this);

        addToBookRackPresenter = new AddToBookRackPresenter();
        addToBookRackPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);
    }

    private void loadData(int page) {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        bookListPresenter.getBookListDetail(userid, bookListEnitiy.mid, page, PAGE_SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookListPresenter.detachView();
        addToBookRackPresenter.detachView();
        taskAwardPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivBack) {
            finish();
        } else if (v.getId() == R.id.ivArrow) {
            if (tvContent.getMaxLines() > 2) {
                tvContent.setMaxLines(2);
                ivArrow.setImageResource(R.drawable.down_icon2);
            } else {
                tvContent.setMaxLines(8);
                ivArrow.setImageResource(R.drawable.up_icon2);
            }
        }
    }

    @Override
    public void getBookListSuccess(BookListResponse bookListResponse) {
    }

    @Override
    public void getBookListDetailSuccess(BookListDetailResponse bookListDetailResponse) {
        if (page == 1) {
            myAdapter.clear();
        }
        page++;
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
        myAdapter.addAll(bookListDetailResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void addToBookRackSuccess(boolean finishActivity, long bookid, int receive) {
        showToast(getString(R.string.add_to_bookrack_success));
        updataList(bookid);
        Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intent.putExtra("bookId", bookid);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (receive == 0) {
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            taskAwardPresenter.getTaskAward(userid, TaskId.ADD_BOOK_TO_RACK, true);
        }
    }

    private void updataList(long bookid) {
        BookInfoEntity bookInfoEntity;
        for (int i = 0; i < myAdapter.getItemCount(); i++) {
            bookInfoEntity = myAdapter.getItem(i);
            if (bookInfoEntity.bookid == bookid) {
                bookInfoEntity.bookshelves = 1;
                myAdapter.notifyItemChanged(i);
                break;
            }
        }

    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        if (taskAwardResponse.number > 0) {
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.book_list_detail_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            final BookInfoEntity bookInfoEntity = getItem(position);

            holder.tvRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadActivity.lunchActivity(mActivity, bookInfoEntity.bookid, bookInfoEntity.chapterid);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            });
            holder.tvAddToBookrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userid = UserInfoManager.getInstance(mContext).getUserid();
                    if (userid == 0) {
                        BookListDetailActivity.super.lunchLoginActivity(FROM_ADD_TO_BOOK_RACK);
                    } else {
                        addToBookRackPresenter.addToBookRack(userid, bookInfoEntity.bookid, false);
                    }
                }
            });

            if (bookInfoEntity.bookshelves == 0) {
                holder.tvAddToBookrack.setText("加入书架");
                holder.tvAddToBookrack.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                holder.tvAddToBookrack.setText("已加入书架");
                holder.tvAddToBookrack.setEnabled(false);
                holder.tvAddToBookrack.setTextColor(getResources().getColor(R.color.author_color));
            }

            ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
            holder.tvBookName.setText(bookInfoEntity.bookname);
            holder.tvContent.setText(bookInfoEntity.synopsis);
            holder.tvBookAuthor.setText(bookInfoEntity.author + "·" + bookInfoEntity.classname + "·" + bookInfoEntity.serstatus + "·" + bookInfoEntity.wordnumbers);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFlag;
        public ImageView ivCover;
        public TextView tvBookName;
        public TextView tvContent;
        public TextView tvBookAuthor;
        public TextView tvRead;
        public TextView tvAddToBookrack;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.ivFlag = itemView.findViewById(R.id.ivFlag);
            this.ivCover = itemView.findViewById(R.id.ivCover);
            this.tvBookName = itemView.findViewById(R.id.tvBookName);
            this.tvContent = itemView.findViewById(R.id.tvContent);
            this.tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            this.tvRead = itemView.findViewById(R.id.tvRead);
            this.tvAddToBookrack = itemView.findViewById(R.id.tvAddToBookrack);
        }
    }
}
