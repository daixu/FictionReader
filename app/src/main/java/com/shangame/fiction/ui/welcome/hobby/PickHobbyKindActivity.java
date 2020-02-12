package com.shangame.fiction.ui.welcome.hobby;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.net.response.PickHobbyKindResponse;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.main.MainItemType;

public class PickHobbyKindActivity extends BaseActivity implements View.OnClickListener, PickHobbyKindContacts.View {

    private PickHobbyKindPresenter pickHobbyKindPresenter;

    private int malechannel = BookStoreChannel.All;

    private TextView tvGirl;
    private TextView tvGoy;
    private TextView btnNextStep;
    private TextView tvInfo;
    private View bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_hobby_kind);
        initView();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pickHobbyKindPresenter.detachView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        findViewById(R.id.ivBoyLayout).setOnClickListener(this);
        findViewById(R.id.ivGirlLayout).setOnClickListener(this);

        btnNextStep = findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(this);

        tvGoy = findViewById(R.id.tvGoy);
        tvGirl = findViewById(R.id.tvGirl);
        tvInfo = findViewById(R.id.tvInfo);

        bottomLayout = findViewById(R.id.bottomLayout);
    }

    private void initPresenter() {
        pickHobbyKindPresenter = new PickHobbyKindPresenter();
        pickHobbyKindPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.btnNextStep) {
            if (malechannel == BookStoreChannel.All) {
                showToast("请先选中类型");
            } else {
                commitPickHobbyKind(malechannel);

                AppSetting appSetting = AppSetting.getInstance(mContext);
                appSetting.putBoolean(SharedKey.FIRST_LUNCH, false);

                Intent intent = new Intent(mActivity, MainFrameWorkActivity.class);
                intent.putExtra("CurrentItem", MainItemType.BOOK_STORE);
                intent.putExtra("malechannel", malechannel);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.ivBoyLayout) {
            malechannel = BookStoreChannel.BOY;
            tvGoy.setSelected(true);
            tvGirl.setSelected(false);
            bottomLayout.setVisibility(View.VISIBLE);
            tvInfo.setText("优先为您推荐男生喜欢的书籍");
        } else if (id == R.id.ivGirlLayout) {
            malechannel = BookStoreChannel.GIRL;
            tvGoy.setSelected(false);
            tvGirl.setSelected(true);
            bottomLayout.setVisibility(View.VISIBLE);
            tvInfo.setText("优先为您推荐女生喜欢的书籍");
        }
    }

    private void commitPickHobbyKind(int malechannel) {
        AppSetting appSetting = AppSetting.getInstance(mContext);
        appSetting.putInt(SharedKey.MALE_CHANNEL, malechannel);
        pickHobbyKindPresenter.commitMaleChannel(malechannel);
    }

    @Override
    public void commitPickHobbyKindSuccess(String kinds, PickHobbyKindResponse pickHobbyKindResponse) {
//        AppSetting appSetting = AppSetting.getInstance(mContext);
//        appSetting.putString(SharedKey.HOBBY_KINDS,kinds);
//        appSetting.putBoolean(SharedKey.FIRST_LUNCH,false);
//
//        if(pickHobbyKindResponse.bookcount > 0 ){
//            CommitHobbyPopupWindow readGuide = new CommitHobbyPopupWindow(mActivity,pickHobbyKindResponse.bookcount);
//            readGuide.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    Intent intent = new Intent(mActivity,MainFrameWorkActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//            readGuide.show();
//        }
    }

    @Override
    public void commitMaleChannelSuccess() {
    }
}
