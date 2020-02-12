package com.shangame.fiction.ui.task;

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
import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.net.response.TaskRecommendBookResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookrack.AddToBookRackContacts;
import com.shangame.fiction.ui.bookrack.AddToBookRackPresenter;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.reader.ReadActivity;

public class TaskRecommendBookActivity extends BaseActivity implements AddToBookRackContacts.View, View.OnClickListener
        , TaskRecommendBookContacts.View, TaskAwardContacts.View {

    private static final int FROM_ADD_TO_BOOK_RACK = 601;

    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    private int page = 1;

    private AddToBookRackPresenter addToBookRackPresenter;
    private TaskRecommendPresenter taskRecommendPresenter;

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
        setContentView(R.layout.activity_task_recommend_book);
        initTitle();
        initSmartRefreshLayout();
        initRecyclerView();
        initReceiver();
        initPresenter();
        mSmartRefreshLayout.autoRefresh();
    }

    private void initTitle() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.tvHome).setOnClickListener(this);
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
        addToBookRackPresenter = new AddToBookRackPresenter();
        addToBookRackPresenter.attachView(this);

        taskRecommendPresenter = new TaskRecommendPresenter();
        taskRecommendPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);
    }

    private void loadData(int page) {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        taskRecommendPresenter.getTaskRecommendBook(userid, 10, page, 20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addToBookRackPresenter.detachView();
        taskRecommendPresenter.detachView();
        taskAwardPresenter.detachView();
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
        TaskRecommendBookResponse.TaskRecommendBook taskRecommendBook;
        for (int i = 0; i < myAdapter.getItemCount(); i++) {
            taskRecommendBook = myAdapter.getItem(i);
            if (taskRecommendBook.bookid == bookid) {
                taskRecommendBook.bookshelves = 1;
                myAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if (R.id.ivPublicBack == id) {
            finish();
        } else if (R.id.tvHome == id) {
            ActivityStack.popToSpecifyActivity(MainFrameWorkActivity.class);
        }
    }

    @Override
    public void getTaskRecommendBookSuccess(TaskRecommendBookResponse taskRecommendBookResponse) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
        page++;
        myAdapter.addAll(taskRecommendBookResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        if (taskAwardResponse.number > 0) {
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<TaskRecommendBookResponse.TaskRecommendBook, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.book_list_detail_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final TaskRecommendBookResponse.TaskRecommendBook taskRecommendBook = getItem(position);

            holder.tvRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadActivity.lunchActivity(mActivity, taskRecommendBook.bookid, taskRecommendBook.chapterid);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookDetailActivity.lunchActivity(mActivity, taskRecommendBook.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            });
            holder.tvAddToBookrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userid = UserInfoManager.getInstance(mContext).getUserid();
                    if (userid == 0) {
                        lunchLoginActivity(FROM_ADD_TO_BOOK_RACK);
                    } else {
                        addToBookRackPresenter.addToBookRack(userid, taskRecommendBook.bookid, false);
                    }
                }
            });

            if (taskRecommendBook.bookshelves == 0) {
                holder.tvAddToBookrack.setText("加入书架");
                holder.tvAddToBookrack.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                holder.tvAddToBookrack.setText("已加入书架");
                holder.tvAddToBookrack.setEnabled(false);
                holder.tvAddToBookrack.setTextColor(getResources().getColor(R.color.author_color));
            }

            ImageLoader.with(mActivity).loadCover(holder.ivCover, taskRecommendBook.bookcover);
            holder.tvBookName.setText(taskRecommendBook.bookname);
            holder.tvContent.setText(taskRecommendBook.synopsis);
            holder.tvBookAuthor.setText(taskRecommendBook.author + "·" + taskRecommendBook.classname + "·" + taskRecommendBook.serstatus + "·" + taskRecommendBook.wordnumbers);
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
