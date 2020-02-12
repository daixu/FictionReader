package com.shangame.fiction.ui.booklist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookrack.AddToBookRackContacts;
import com.shangame.fiction.ui.bookrack.AddToBookRackPresenter;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;


public class DailyRecommendFragment extends BaseFragment implements View.OnClickListener,
        AddToBookRackContacts.View,TaskAwardContacts.View{

    private static final String ARG_PARAM1 = "RecdataBean";

    private static final int FROM_ADD_TO_BOOK_RACK =601;

    private RecommendBookResponse.RecdataBean recdataBean;

    private ImageView ivCover;
    private ImageView ivLove;
    private TextView tvBookName;
    private TextView tvAuthorName;
    private TextView tvIntro;
    private TextView tvFreeRead;
    private TextView tvAddToBookrack;

    private AddToBookRackPresenter addToBookRackPresenter;

    private TaskAwardPresenter taskAwardPresenter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION.equals(action)){
                Toast.makeText(mActivity,getString(R.string.share_success),Toast.LENGTH_SHORT).show();
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                taskAwardPresenter.getTaskAward(userid, TaskId.SHARE_BOOK,true);
            }else if(BroadcastAction.ADD_BOOK_TO_RACK_ACTION.equals(action)){
                long bookId = intent.getLongExtra("bookId",0);
                if(bookId == recdataBean.bookid){
                    ivLove.setVisibility(View.GONE);
                    tvAddToBookrack.setText(R.string.added_to_bookrack);
                    tvAddToBookrack.setTextColor(getResources().getColor(R.color.secondary_text));
                }
            }
        }
    };

    public DailyRecommendFragment() {
    }

    public static DailyRecommendFragment newInstance(RecommendBookResponse.RecdataBean param1) {
        DailyRecommendFragment fragment = new DailyRecommendFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recdataBean = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_daily_recommend, container, false);
        ivCover = contentView.findViewById(R.id.ivCover);
        ivLove = contentView.findViewById(R.id.ivLove);
        tvBookName = contentView.findViewById(R.id.tvBookName);
        tvAuthorName = contentView.findViewById(R.id.tvAuthorName);
        tvIntro = contentView.findViewById(R.id.tvIntro);
        tvFreeRead = contentView.findViewById(R.id.tvFreeRead);
        tvAddToBookrack = contentView.findViewById(R.id.tvAddToBookrack);

        contentView.findViewById(R.id.itemLayout).setOnClickListener(this);
        contentView.findViewById(R.id.addLayout).setOnClickListener(this);
        tvFreeRead.setOnClickListener(this);
        tvAddToBookrack.setOnClickListener(this);

        addToBookRackPresenter = new AddToBookRackPresenter();
        addToBookRackPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);

        initReceiver();

        return contentView;
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver,intentFilter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageLoader.with(getActivity()).loadCover(ivCover,recdataBean.bookcover);
        tvBookName.setText(recdataBean.bookname);
        if(recdataBean.status == 0){
            tvAuthorName.setText(recdataBean.author +"   连载中");
        }else{
            tvAuthorName.setText(recdataBean.author+"    完结");
        }
        tvIntro.setText(recdataBean.synopsis);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvFreeRead){
            ReadActivity.lunchActivity(getActivity(),recdataBean.bookid, recdataBean.chapterid);
        }else if(v.getId() == R.id.itemLayout){
            BookDetailActivity.lunchActivity(getActivity(),recdataBean.bookid, ApiConstant.ClickType.FROM_CLICK);
        }else if(v.getId() == R.id.addLayout){
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            if(userid == 0){
                super.lunchLoginActivity(FROM_ADD_TO_BOOK_RACK);
            }else{
                addToBookRackPresenter.addToBookRack(userid,recdataBean.bookid,false);
            }
        }
    }

    @Override
    public void addToBookRackSuccess(boolean finishActivity, long bookid, int receive) {
        ivLove.setVisibility(View.GONE);
        tvAddToBookrack.setText(R.string.added_to_bookrack);
        tvAddToBookrack.setTextColor(getResources().getColor(R.color.secondary_text));
        showToast(getString(R.string.add_to_bookrack_success));

        Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intent.putExtra("bookId",bookid);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if(receive == 0){
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            taskAwardPresenter.getTaskAward(userid, TaskId.ADD_BOOK_TO_RACK,true);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE){
            if(getActivity().RESULT_OK == resultCode){
                int formCode = data.getIntExtra(SharedKey.FROM_CODE,0);
                if(formCode == FROM_ADD_TO_BOOK_RACK){
                    long userid = UserInfoManager.getInstance(mContext).getUserid();
                    addToBookRackPresenter.addToBookRack(userid,recdataBean.bookid,false);
                }
            }
        }


    }


    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        if(taskAwardResponse.number > 0){
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            userInfo.money = (long) taskAwardResponse.number;
            UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

            Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
            intent.putExtra(SharedKey.TASK_ID,taskid);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc,taskAwardResponse.number+"");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addToBookRackPresenter.detachView();
        taskAwardPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }
}
