package com.shangame.fiction.core.manager;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/8/13
 */
public class SmartRefreshLayoutManager {

    public static void initRefreshLayout(){

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary);
//                layout.setHeaderHeight(60);
                return  new AnmaHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter2(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    private static class AnmaHeader extends InternalAbstract implements RefreshHeader{

        private Context mContext;
        private AnimationDrawable animationDrawable;


        public AnmaHeader(Context context) {
            super(context, null, 0);
            mContext = context;

            View view = LayoutInflater.from(mContext).inflate(R.layout.anma_header,this,false);
            ImageView ivHorse = view.findViewById(R.id.ivHorse);
            animationDrawable = (AnimationDrawable) ivHorse.getDrawable();
            addView(view);
        }

        @NonNull
        @Override
        public View getView() {
            return this;
        }


        @NonNull
        @Override
        public SpinnerStyle getSpinnerStyle() {
            return SpinnerStyle.Translate;
        }

        @Override
        public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

            animationDrawable.start();
        }

        public int onFinish(@NonNull RefreshLayout layout, boolean success) {
            animationDrawable.stop();
            return 500;
        }
    }







}
