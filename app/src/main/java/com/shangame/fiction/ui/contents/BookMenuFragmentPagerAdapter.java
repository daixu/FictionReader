package com.shangame.fiction.ui.contents;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shangame.fiction.R;

import java.util.ArrayList;
import java.util.List;

public class BookMenuFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private Context mContext;

    private BookChapterListFragment bookChapterListFragment;
    private BookMarkFragment bookMarkFragment;

    public BookMenuFragmentPagerAdapter(FragmentManager fm, Context context, long bookid,long currentChapterId) {
        super(fm);
        mContext = context;
        list = new ArrayList<>(2);
        bookChapterListFragment = BookChapterListFragment.newInstance(bookid,currentChapterId);
        bookMarkFragment = BookMarkFragment.newInstance(bookid);
        list.add(bookChapterListFragment);
        list.add(bookMarkFragment);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.book_menu);
        } else {
            return mContext.getString(R.string.book_mark);
        }
    }


    public void setOnChapterCheckedLinstener(OnChapterCheckedListener onChapterCheckedListener) {
        bookChapterListFragment.setOnChapterCheckedListener(onChapterCheckedListener);
    }

    public void setOnBookMarkCheckedLinstener(OnBookMarkCheckedLinstener onBookMarkCheckedLinstener) {
        bookMarkFragment.setOnBookMarkCheckedLinstener(onBookMarkCheckedLinstener);
    }
}