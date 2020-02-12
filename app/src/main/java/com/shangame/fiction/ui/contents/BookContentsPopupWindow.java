package com.shangame.fiction.ui.contents;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.utils.ScreenUtils;


/**
 * Create by Speedy on 2018/8/16
 */
public class BookContentsPopupWindow extends DialogFragment {

    private static final String TAG = "BookContentsPopupWindow";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private BookMenuFragmentPagerAdapter mAdapter;

    private long bookid;

    private OnChapterCheckedListener onChapterCheckedListener;

    private OnBookMarkCheckedLinstener onBookMarkCheckedLinstener;

    private DialogInterface.OnDismissListener onDismissListener;

    private long currentChapterId;

    public BookContentsPopupWindow() {
    }

    public static BookContentsPopupWindow newInstance(long bookid, long currentChapterId) {
        BookContentsPopupWindow bookContentsPopupWindow = new BookContentsPopupWindow();
        Bundle bundle = new Bundle();
        bundle.putLong("bookid", bookid);
        bundle.putLong("currentChapterId", currentChapterId);
        bookContentsPopupWindow.setArguments(bundle);
        return bookContentsPopupWindow;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.popup_window_book_menu, container, false);

        mViewPager = (ViewPager) contentView.findViewById(R.id.viewPager);

        mAdapter = new BookMenuFragmentPagerAdapter(getChildFragmentManager(), getActivity(), bookid, currentChapterId);
        mAdapter.setOnChapterCheckedLinstener(onChapterCheckedListener);
        mAdapter.setOnBookMarkCheckedLinstener(onBookMarkCheckedLinstener);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout = (TabLayout) contentView.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        return contentView;
    }

    public void setOnChapterCheckedListener(OnChapterCheckedListener onChapterCheckedListener) {
        this.onChapterCheckedListener = onChapterCheckedListener;
    }

    public void setOnBookMarkCheckedLinstener(OnBookMarkCheckedLinstener onBookMarkCheckedLinstener) {
        this.onBookMarkCheckedLinstener = onBookMarkCheckedLinstener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onDismissListener != null) {
            onDismissListener.onDismiss(getDialog());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bookid = getArguments().getLong("bookid");
        currentChapterId = getArguments().getLong("currentChapterId");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (null != window) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        super.onActivityCreated(savedInstanceState);

        if (null != window) {
            window.setGravity(Gravity.TOP | Gravity.LEFT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ScreenUtils.getScreenWidth(getActivity()) - 200, LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
