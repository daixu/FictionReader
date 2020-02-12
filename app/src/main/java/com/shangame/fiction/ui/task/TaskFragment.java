package com.shangame.fiction.ui.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.TaskAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.net.response.BindWeChatResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.net.response.TaskItem;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.popup.RedPacketPopupWindow;
import com.shangame.fiction.ui.setting.security.BindPhoneActivity;
import com.shangame.fiction.ui.share.ShareDownloadAppActivity;
import com.shangame.fiction.wxapi.WeChatConstants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends BaseFragment implements TaskAwardContacts.View, BindWeChatContacts.View {

    private static final String TASK_TYPE = "TaskType";
    private static final String TASK_ITEM_LIST = "TaskItemList";

    private int type;

    private TaskAdapter mAdapter;

    private IWXAPI api;

    private List<TaskItem> taskItemList = new ArrayList<>();

    private TaskAwardPresenter taskAwardPresenter;

    private BindWeChatPresenter bindWechatPresenter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BroadcastAction.UPDATE_TASK_LIST.equals(action)) {
                int taskId = intent.getIntExtra(SharedKey.TASK_ID, 0);
                updateTaskList(taskId);
            } else if (BroadcastAction.WECHAT_BIND_ACTION.equals(intent.getAction())) {
                String code = intent.getStringExtra("code");
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                bindWechatPresenter.bindWeChat(userId, code, WeChatConstants.APP_ID);
            }
        }
    };

    public static TaskFragment newInstance(int type, ArrayList<TaskItem> taskItemList) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(TASK_TYPE, type);
        args.putParcelableArrayList(TASK_ITEM_LIST, taskItemList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TASK_TYPE);
            taskItemList = getArguments().getParcelableArrayList(TASK_ITEM_LIST);
        }
        IntentFilter intentFilter = new IntentFilter(BroadcastAction.UPDATE_TASK_LIST);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_task, container, false);
        initRecyclerView(contentView);
        initPresenter();
        return contentView;
    }

    private void initRecyclerView(View contentView) {
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));

        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new TaskAdapter(R.layout.task_item, taskItemList, type);
        recyclerView.setAdapter(mAdapter);

        if (taskItemList != null) {
            mAdapter.setNewData(taskItemList);
        }

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tvState) {
                    TaskItem taskItem = taskItemList.get(position);
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    if (userId == 0) {
                        Intent mIntent = new Intent(mContext, LoginActivity.class);
                        mActivity.startActivityForResult(mIntent, 435);
                    } else {
                        doTask(taskItem);
                    }
                }
            }
        });
    }

    private void initPresenter() {
        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);

        bindWechatPresenter = new BindWeChatPresenter();
        bindWechatPresenter.attachView(this);
    }

    private void doTask(TaskItem taskItem) {
        switch (taskItem.jumptype) {
            case 1:
                Intent intent = new Intent(mActivity, TaskRecommendBookActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(mActivity, ShareWinRedPacketdActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(mActivity, BindPhoneActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(mActivity, ShareDownloadAppActivity.class);
                startActivity(intent);
                break;
            default:
                getTaskAward(taskItem);
                break;

        }
    }

    private void getTaskAward(TaskItem taskItem) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        taskAwardPresenter.getTaskAward(userId, taskItem.taskid, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskAwardPresenter.detachView();
        bindWechatPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        updateTaskList(taskid);

        if (taskAwardResponse.goldtype == 1) {
            RedPacketPopupWindow redPacketPopupWindow = new RedPacketPopupWindow(mActivity, taskAwardResponse);
            redPacketPopupWindow.setBindWechatClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toWeChatLogin();
                }
            });
            redPacketPopupWindow.show();
        } else {
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(getActivity());
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }

    }

    private void updateTaskList(int taskId) {
        TaskItem taskItem;
        int updatePosition = -1;
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            taskItem = mAdapter.getItem(i);
            if (null != taskItem && taskItem.taskid == taskId) {
                taskItem.receive = 1;
                updatePosition = i;
                break;
            }
        }
        if (updatePosition != -1) {
            mAdapter.notifyItemChanged(updatePosition);
        }
    }

    public void toWeChatLogin() {
        api = WXAPIFactory.createWXAPI(getContext(), WeChatConstants.APP_ID, true);
        api.registerApp(WeChatConstants.APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, R.string.not_wechat_app, Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = WeChatConstants.State.LOGIN;
        api.sendReq(req);
    }

    @Override
    public void bindWeChatSuccess(BindWeChatResponse inviteRecordResponse) {
        showToast("绑定微信成功");
    }
}
