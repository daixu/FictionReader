package com.shangame.fiction.ui.my.message;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapBaseAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.SystemMessageResponse;
import com.shangame.fiction.net.response.UpdateMessagetResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BaseActivity implements View.OnClickListener,MessageCenterContacts.View{

    private SmartRefreshLayout smartRefreshLayout;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<View> viewList = new ArrayList<>();
    private ListView updateRemindListView;
    private ListView systemMessagetListView;

    private UpdateRemindAdapter updateRemindAdapter;
    private SystemMessageAdapter systemMessageAdapter;

    private MessagetCenterPresenter messagetCenterPresenter;

    private int updatePage;
    private int systemPage;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);

        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.message_center);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        initSmartRefreshLayout();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        View updateRemindLayout= LayoutInflater.from(mContext).inflate(R.layout.public_list_view,null);
        View systemMessagetLayout= LayoutInflater.from(mContext).inflate(R.layout.public_list_view,null);
        viewList.add(updateRemindLayout);
        viewList.add(systemMessagetLayout);

        updateRemindListView = systemMessagetLayout.findViewById(R.id.listView);
        updateRemindAdapter = new UpdateRemindAdapter(mActivity);
        updateRemindListView.setAdapter(updateRemindAdapter);

        systemMessagetListView = systemMessagetLayout.findViewById(R.id.listView);
        systemMessageAdapter = new SystemMessageAdapter(mActivity);
        systemMessagetListView.setAdapter(systemMessageAdapter);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            /**
             * 返回要显示的view,即要显示的视图
             */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if(position == 0){
                    return getString(R.string.update_remind);
                }else{
                    return getString(R.string.system_remind);
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        messagetCenterPresenter = new MessagetCenterPresenter();
        messagetCenterPresenter.attachView(this);

        smartRefreshLayout.autoRefresh();
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(viewPager.getCurrentItem() == 0){
                    updatePage = 1;
                    loadUpdateMesage(updatePage);
                }else{
                    systemPage = 1;
                    loadSystemMesage(systemPage);
                }
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(viewPager.getCurrentItem() == 0){
                    updatePage++;
                    loadUpdateMesage(updatePage);
                }else{
                    systemPage++;
                    loadSystemMesage(systemPage);
                }
            }
        });
    }

    private void loadUpdateMesage(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        messagetCenterPresenter.getUpdateMessage(userid,page,PAGE_SIZE);
    }

    private void loadSystemMesage(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        messagetCenterPresenter.getSystemMessage(userid,page,PAGE_SIZE);
    }


    @Override
    public void getUpdateMessageSuccess(UpdateMessagetResponse updateMessagetResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        updateRemindAdapter.addAll(updateMessagetResponse.upentity);
        updateRemindAdapter.notifyDataSetChanged();

    }

    @Override
    public void getSystemMessageSuccess(SystemMessageResponse messageResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        systemMessageAdapter.addAll(messageResponse.sysentity);
        systemMessageAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    class UpdateRemindViewHolder{
        TextView tvTitle;
        TextView tvTime;
        ImageView ivCover;
        TextView tvBookName;
        TextView tvAuthorName;
    }
    class UpdateRemindAdapter extends WrapBaseAdapter<UpdateMessagetResponse.UpdateReminBean,UpdateRemindViewHolder>{

        public UpdateRemindAdapter(Activity activity) {
            super(activity);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            UpdateRemindViewHolder updateRemindViewHolder;
            if(view == null){
                updateRemindViewHolder = new UpdateRemindViewHolder();
                updateRemindViewHolder.tvTitle = view.findViewById(R.id.tvTitle);
                updateRemindViewHolder.tvTime = view.findViewById(R.id.tvTime);
                updateRemindViewHolder.ivCover = view.findViewById(R.id.ivCover);
                updateRemindViewHolder.tvBookName = view.findViewById(R.id.tvBookName);
                updateRemindViewHolder.tvAuthorName = view.findViewById(R.id.tvAuthorName);
                view.setTag(updateRemindViewHolder);
            }else {
                updateRemindViewHolder = (UpdateRemindViewHolder) view.getTag();
            }

            UpdateMessagetResponse.UpdateReminBean updateReminBean = getItem(i);

            updateRemindViewHolder.tvTitle.setText(updateReminBean.title);
            updateRemindViewHolder.tvTime.setText(updateReminBean.addtime);
            updateRemindViewHolder.tvBookName.setText(updateReminBean.bookname);
            updateRemindViewHolder.tvAuthorName.setText(updateReminBean.chaptername);

            ImageLoader.with(mActivity).loadCover(updateRemindViewHolder.ivCover,updateReminBean.bookimage);
            return view;
        }
    }

    class SystemMessageViewHolder{
        TextView tvTitle;
        TextView tvTime;
        TextView tvContent;
    }


    class SystemMessageAdapter extends WrapBaseAdapter<SystemMessageResponse.SystemMessageBean,SystemMessageViewHolder>{

        public SystemMessageAdapter(Activity activity) {
            super(activity);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SystemMessageViewHolder systemMessageViewHolder;
            if(view == null){
                systemMessageViewHolder = new SystemMessageViewHolder();
                systemMessageViewHolder.tvTitle = view.findViewById(R.id.tvTitle);
                systemMessageViewHolder.tvTime = view.findViewById(R.id.tvTime);
                systemMessageViewHolder.tvContent = view.findViewById(R.id.tvContent);
                view.setTag(systemMessageViewHolder);
            }else {
                systemMessageViewHolder = (SystemMessageViewHolder) view.getTag();
            }

            SystemMessageResponse.SystemMessageBean systemMessageBean = getItem(i);

            systemMessageViewHolder.tvTitle.setText(systemMessageBean.title);
            systemMessageViewHolder.tvTime.setText(systemMessageBean.addtime);
            systemMessageViewHolder.tvContent.setText(systemMessageBean.msgcontent);
            return view;
        }
    }


    @Override
    public void showToast(String msg) {
        super.showToast(msg);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        messagetCenterPresenter.detachView();
    }
}
