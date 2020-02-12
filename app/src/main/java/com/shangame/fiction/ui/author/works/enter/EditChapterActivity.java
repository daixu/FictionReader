package com.shangame.fiction.ui.author.works.enter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.widget.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

public class EditChapterActivity extends BaseActivity implements View.OnClickListener {
    private MagicIndicator mMagicIndicator;
    private NoScrollViewPager mViewPager;

    private int mBookId;
    private String mBookName;
    private ImageView mImgOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chapter);

        mBookId = getIntent().getIntExtra("bookId", 0);
        Log.e("hhh", "bookId= " + mBookId);

        initView();
        initListener();

        initMagicIndicator();
    }

    private void initView() {
        mMagicIndicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.view_pager);

        mImgOption = findViewById(R.id.img_option);

        mImgOption.setVisibility(View.VISIBLE);

        mBookName = getIntent().getStringExtra("bookName");

        TextView tvTitle = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mBookName)) {
            tvTitle.setText(mBookName);
        }

        DraftBoxFragment draftBoxFragment = DraftBoxFragment.newInstance(mBookId, mBookName);
        PublishedFragment publishedFragment = PublishedFragment.newInstance(mBookId, mBookName);

        final List<Fragment> list = new ArrayList<>(2);
        list.add(draftBoxFragment);
        list.add(publishedFragment);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "草稿箱";
                } else {
                    return "已发布章节";
                }
            }
        };
        mViewPager.setAdapter(adapter);
    }

    private void initListener() {
        mImgOption.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    private void initMagicIndicator() {
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("草稿箱");
        titleList.add("已发布章节");
        CommonNavigator navigator = new CommonNavigator(mContext);
        MagicIndicatorAdapter adapter = new MagicIndicatorAdapter(mViewPager);
        adapter.setTitleList(titleList);
        navigator.setAdapter(adapter);
        navigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(navigator);

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_option: {
                Intent intent = new Intent(EditChapterActivity.this, EditContentActivity.class);
                intent.putExtra("bookName", mBookName);
                intent.putExtra("bookId", mBookId);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}
