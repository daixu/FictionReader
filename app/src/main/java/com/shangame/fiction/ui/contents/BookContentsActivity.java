package com.shangame.fiction.ui.contents;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;


/**
 * 书籍目录
 * Create by Speedy on 2018/7/25
 */
public class BookContentsActivity extends BaseActivity implements View.OnClickListener {

    private long bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_menu);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        bookid = getIntent().getLongExtra("bookid", 0);

        ViewPager viewPager = findViewById(R.id.viewPager);

        FragmentPagerAdapter adapter = new BookMenuFragmentPagerAdapter(getSupportFragmentManager(), mActivity, bookid, 0);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            default:
                break;
        }
    }

}
