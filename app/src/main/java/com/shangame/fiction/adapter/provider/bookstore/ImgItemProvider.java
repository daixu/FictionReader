package com.shangame.fiction.adapter.provider.bookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.login.LoginActivity;

public class ImgItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_IMG;
    }

    @Override
    public int layout() {
        return R.layout.item_img_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        ImageView imageGo = helper.getView(R.id.image_go);

        imageGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    // startActivityForResult(mIntent, BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
                } else {
                    UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                    int agentGrade = userInfo.agentGrade;
                    if (agentGrade > 0) {
                        ARouter.getInstance()
                                .build("/ss/sales/partner/manage/home")
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build("/ss/sales/partner/upgrade/silver")
                                .navigation();
                    }
                }
            }
        });
    }
}
