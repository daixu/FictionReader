package com.shangame.fiction.ui.booklib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * 书库
 * Create by Speedy on 2018/8/1
 */
public class BookLibraryActivity extends BaseActivity implements View.OnClickListener {
    private List<Fragment> list = new ArrayList<>();
    private int bookStoreChannel = BookStoreChannel.BOY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_library);

        if (getIntent().hasExtra("BookStoreChannel")) {
            bookStoreChannel = getIntent().getIntExtra("BookStoreChannel", BookStoreChannel.BOY);
        }

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.book_library);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        list.add(BookLibraryFragment.newInstance(BookStoreChannel.BOY));
        list.add(BookLibraryFragment.newInstance(BookStoreChannel.GIRL));
        list.add(BookLibraryFragment.newInstance(BookStoreChannel.LISTEN));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return getChannelName(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        if (bookStoreChannel == BookStoreChannel.GIRL) {
            viewPager.setCurrentItem(1);
        } else if (bookStoreChannel == BookStoreChannel.LISTEN) {
            viewPager.setCurrentItem(2);
        }
    }

    public String getChannelName(int position) {
        switch (position) {
            case 0:
                return getString(R.string.boy);
            case 1:
                return getString(R.string.girl);
            case 2:
                return "听书";
            default:
                return "";
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        }
    }
}
