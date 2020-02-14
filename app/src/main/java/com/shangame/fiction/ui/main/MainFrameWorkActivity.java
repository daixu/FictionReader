package com.shangame.fiction.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.AppUtils;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.VersionCheckResponse;
import com.shangame.fiction.statis.AppStatis;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.bookrack.BookRackFragment;
import com.shangame.fiction.ui.bookstore.BookStoreFragment;
import com.shangame.fiction.ui.bookstore.choice.ChoiceFragment;
import com.shangame.fiction.ui.listen.ListenBookFragment;
import com.shangame.fiction.ui.listen.PlayerSong;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.play.MusicPlayerActivity;
import com.shangame.fiction.ui.my.MyFragment;
import com.shangame.fiction.ui.my.about.DownloadAPKDialog;
import com.shangame.fiction.ui.my.about.VersionCheckContracts;
import com.shangame.fiction.ui.my.about.VersionCheckPresenter;
import com.shangame.fiction.ui.my.about.VersionUpdateDialog;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.widget.CircleRotateImageView;
import com.shangame.fiction.widget.TabItemView;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * 主界面 Activity
 * Create by Speedy on 2018/7/19
 */
public class MainFrameWorkActivity extends BaseActivity implements View.OnClickListener, VersionCheckContracts.View {

    private static final int INSTALL_PACKAGES_REQUEST_CODE = 99;
    private static final int GET_UNKNOWN_APP_SOURCES = 88;
    private static final int INISTALL_APP_REQUEST_CODE = 1024;

    private static final String TAG = "MainActivity";

    private TabItemView tabBookRack;
    private TabItemView tabBookChoice;
    private TabItemView tabBookStore;
    private TabItemView tabMy;
//    private TabItemView tabListenBook;

    private MainItemType currentItemType;

    private BookRackFragment bookRackFragment;
    private ChoiceFragment mChoiceFragment;
    private BookStoreFragment bookStoreFragment;
    private MyFragment myFragment;
//    private ListenBookFragment mListenBookFragment;

    private CircleRotateImageView mImagePlayerCover;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private VersionCheckPresenter versionCheckPresenter;

    private File apkFile;//升级下载下来的apk

    private long mExitTime;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.STOP_PLAY_ACTION.equals(action)) {
                if (null != mImagePlayerCover) {
                    mImagePlayerCover.setVisibility(View.GONE);
                    mImagePlayerCover.cancelRotateAnimation();
                }
            } else if (BroadcastAction.START_PLAY_ACTION.equals(action)) {
                if (null != mImagePlayerCover) {
                    mImagePlayerCover.setVisibility(View.VISIBLE);
                    mImagePlayerCover.startRotateAnimation();
                }
            } else if (BroadcastAction.PAUSE_PLAY_ACTION.equals(action)) {
                if (null != mImagePlayerCover) {
                    mImagePlayerCover.cancelRotateAnimation();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame_work);
        setStatusBar();
        AppStatis.getInstance(mContext).appLunch();
        initView();
        initReceiver();
        initFragment(savedInstanceState);
        initCurrentItem();

        initAlias();
//        verifyQQLoginExpiresTime();
        checkNewVersion();
        restoreReadActivity();
    }

    private void setStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(MainFrameWorkActivity.this, null);
    }

    private void initView() {
        tabBookRack = findViewById(R.id.tabBookRack);
        tabBookChoice = findViewById(R.id.tab_book_choice);
        tabBookStore = findViewById(R.id.tabBookStore);
        tabMy = findViewById(R.id.tabMy);
//        tabListenBook = findViewById(R.id.tab_listen_book);

        mImagePlayerCover = findViewById(R.id.image_player_cover);

        tabBookRack.setOnClickListener(this);
        tabBookChoice.setOnClickListener(this);
        tabBookStore.setOnClickListener(this);
        tabMy.setOnClickListener(this);
//        tabListenBook.setOnClickListener(this);
        mImagePlayerCover.setOnClickListener(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.STOP_PLAY_ACTION);
        intentFilter.addAction(BroadcastAction.START_PLAY_ACTION);
        intentFilter.addAction(BroadcastAction.PAUSE_PLAY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            bookRackFragment = BookRackFragment.newInstance();
            bookStoreFragment = BookStoreFragment.newInstance();
            myFragment = MyFragment.newInstance();
//            mListenBookFragment = ListenBookFragment.newInstance(1);
            mChoiceFragment = ChoiceFragment.newInstance(1);

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, bookRackFragment, "BookRackFragment");
            fragmentTransaction.add(R.id.container, mChoiceFragment, "ChoiceFragment");
            fragmentTransaction.add(R.id.container, bookStoreFragment, "BookStoreFragment");
//            fragmentTransaction.add(R.id.container, mListenBookFragment, "ListenBookFragment");
            fragmentTransaction.add(R.id.container, myFragment, "MyFragment");
            fragmentTransaction.hide(bookRackFragment);
            fragmentTransaction.show(mChoiceFragment);
            fragmentTransaction.hide(bookStoreFragment);
//            fragmentTransaction.show(mListenBookFragment);
            fragmentTransaction.hide(myFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            bookRackFragment = (BookRackFragment) getSupportFragmentManager().getFragment(savedInstanceState, "BookRackFragment");
            mChoiceFragment = (ChoiceFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ChoiceFragment");
            bookStoreFragment = (BookStoreFragment) getSupportFragmentManager().getFragment(savedInstanceState, "BookStoreFragment");
//            mListenBookFragment = (ListenBookFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mListenBookFragment");
            myFragment = (MyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MyFragment");

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(bookRackFragment);
            fragmentTransaction.show(mChoiceFragment);
            fragmentTransaction.hide(bookStoreFragment);
//            fragmentTransaction.show(mListenBookFragment);
            fragmentTransaction.hide(myFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void initCurrentItem() {
        if (getIntent().hasExtra("index")) {
            int index = getIntent().getIntExtra("index", 1);
            switch (index) {
                case 0:
                    setCurrentItem(MainItemType.BOOK_RACK);
                    break;
                case 1:
                    setCurrentItem(MainItemType.BOOK_CHOICE);
                    break;
                case 2:
                    setCurrentItem(MainItemType.BOOK_STORE);
                    break;
                case 3:
                    setCurrentItem(MainItemType.MY);
                    break;
                default:
                    break;
            }
        } else {
            setCurrentItem(MainItemType.BOOK_CHOICE);
        }
    }

    private void initAlias() {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        JPushInterface.setAlias(mContext, (int) userInfo.userid, String.valueOf(userInfo.userid));
        JPushInterface.resumePush(mContext);
    }

    private void checkNewVersion() {
        versionCheckPresenter = new VersionCheckPresenter();
        versionCheckPresenter.attachView(this);

        long userId = UserInfoManager.getInstance(mContext).getUserid();
        int version = AppUtils.getAppVersionCode();
        versionCheckPresenter.checkNewVersion(userId, version);
    }

    private void restoreReadActivity() {
        AppSetting appSetting = AppSetting.getInstance(mContext);
        boolean isReadActivityNormalFinish = appSetting.getBoolean(SharedKey.IS_READ_ACTIVITY_NORMAL_FINISH, true);
        if (!isReadActivityNormalFinish) {
            long bookId = appSetting.getLong(SharedKey.BOOK_ID, 0);
            if (bookId != 0) {
                long chapterId = appSetting.getLong(SharedKey.CHAPTER_ID, 0);
                int pageIndex = appSetting.getInt(SharedKey.PAGE_INDEX, 0);
                ReadActivity.lunchActivity(mActivity, bookId, chapterId, pageIndex, 1);
            }
        }
    }

    public void setCurrentItem(MainItemType mainItemType) {
        currentItemType = mainItemType;
        switch (mainItemType) {
            case BOOK_RACK:
                updateTabItemState(R.id.tabBookRack);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(bookRackFragment);
                fragmentTransaction.hide(mChoiceFragment);
                fragmentTransaction.hide(bookStoreFragment);
                fragmentTransaction.hide(myFragment);
//                fragmentTransaction.hide(mListenBookFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case BOOK_CHOICE:
                updateTabItemState(R.id.tab_book_choice);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(bookRackFragment);
                fragmentTransaction.show(mChoiceFragment);
                fragmentTransaction.hide(bookStoreFragment);
                fragmentTransaction.hide(myFragment);
//                fragmentTransaction.hide(mListenBookFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case BOOK_STORE:
                updateTabItemState(R.id.tabBookStore);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(bookRackFragment);
                fragmentTransaction.hide(mChoiceFragment);
                fragmentTransaction.show(bookStoreFragment);
                fragmentTransaction.hide(myFragment);
//                fragmentTransaction.hide(mListenBookFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case MY:
                updateTabItemState(R.id.tabMy);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(bookRackFragment);
                fragmentTransaction.hide(mChoiceFragment);
                fragmentTransaction.hide(bookStoreFragment);
                fragmentTransaction.show(myFragment);
//                fragmentTransaction.hide(mListenBookFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
//            case LISTEN_BOOK:
//                updateTabItemState(R.id.tab_listen_book);
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.hide(bookRackFragment);
//                fragmentTransaction.hide(bookStoreFragment);
//                fragmentTransaction.hide(myFragment);
//                fragmentTransaction.show(mListenBookFragment);
//                fragmentTransaction.commitAllowingStateLoss();
//                break;
            default:
                break;
        }
    }

    private void updateTabItemState(int selectTabId) {
        switch (selectTabId) {
            case R.id.tabBookRack:
                tabBookRack.setSelected(true);
                tabBookChoice.setSelected(false);
                tabBookStore.setSelected(false);
                tabMy.setSelected(false);
//                tabListenBook.setSelected(false);
                break;
            case R.id.tab_book_choice:
                tabBookRack.setSelected(false);
                tabBookChoice.setSelected(true);
                tabBookStore.setSelected(false);
                tabMy.setSelected(false);
//                tabBookStore.setSelected(false);
                break;
            case R.id.tabBookStore:
                tabBookRack.setSelected(false);
                tabBookChoice.setSelected(false);
                tabBookStore.setSelected(true);
                tabMy.setSelected(false);
//                tabListenBook.setSelected(false);
                break;
            case R.id.tabMy:
                tabBookRack.setSelected(false);
                tabBookChoice.setSelected(false);
                tabBookStore.setSelected(false);
                tabMy.setSelected(true);
//                tabListenBook.setSelected(false);
                break;
//            case R.id.tab_listen_book:
//                tabBookRack.setSelected(false);
//                tabBookStore.setSelected(false);
//                tabMy.setSelected(false);
//                tabListenBook.setSelected(true);
//                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (SharedKey.SYS_AGENT_GRADE) {
            case 1:
                UpgradePopupWindow customPopup = new UpgradePopupWindow(this, 1);
                new XPopup.Builder(this)
                        .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                        .asCustom(customPopup)
                        .show();
                break;
            case 2:
                UpgradePopupWindow customPopup2 = new UpgradePopupWindow(this, 2);
                new XPopup.Builder(this)
                        .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                        .asCustom(customPopup2)
                        .show();
                break;
            case 3:
                UpgradePopupWindow customPopup3 = new UpgradePopupWindow(this, 3);
                new XPopup.Builder(this)
                        .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                        .asCustom(customPopup3)
                        .show();
                break;
            default:
                break;
        }
        if (null != PlayerSong.getInstance().getPlayerSong()) {
            Song song = PlayerSong.getInstance().getPlayerSong();

            if (null != mImagePlayerCover) {
                RequestOptions playerOptions = new RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.default_cover)
                        .override(54, 54);
                Glide.with(mContext).load(song.showCover).apply(playerOptions).into(mImagePlayerCover);

                mImagePlayerCover.setVisibility(View.VISIBLE);
                boolean isPlay = PlayerSong.getInstance().isPlay();
                if (isPlay) {
                    mImagePlayerCover.setVisibility(View.VISIBLE);
                    mImagePlayerCover.startRotateAnimation();
                } else {
                    mImagePlayerCover.cancelRotateAnimation();
                }
            }
        } else {
            if (null != mImagePlayerCover) {
                mImagePlayerCover.setVisibility(View.GONE);
                mImagePlayerCover.cancelRotateAnimation();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        versionCheckPresenter.detachView();
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        AppStatis.getInstance(mContext).sendAppLunchDurationTime(userId);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);

        if (null != mImagePlayerCover) {
            mImagePlayerCover.cancelRotateAnimation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, "BookRackFragment", bookRackFragment);
        getSupportFragmentManager().putFragment(outState, "ChoiceFragment", mChoiceFragment);
        getSupportFragmentManager().putFragment(outState, "BookStoreFragment", bookStoreFragment);
//        getSupportFragmentManager().putFragment(outState, "ListenBookFragment", mListenBookFragment);
        getSupportFragmentManager().putFragment(outState, "MyFragment", myFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        updateTabItemState(view.getId());
        switch (view.getId()) {
            case R.id.tabBookRack:
                setCurrentItem(MainItemType.BOOK_RACK);
                break;
            case R.id.tab_book_choice:
                setCurrentItem(MainItemType.BOOK_CHOICE);
                break;
            case R.id.tabBookStore:
                if (currentItemType == MainItemType.BOOK_STORE) {
                    bookStoreFragment.scrollToTop();
                } else {
                    setCurrentItem(MainItemType.BOOK_STORE);
                }
                break;
            case R.id.tabMy:
                setCurrentItem(MainItemType.MY);
                break;
//            case R.id.tab_listen_book:
//                setCurrentItem(MainItemType.LISTEN_BOOK);
//                break;
            case R.id.image_player_cover: {
                Intent intent = new Intent(mContext, MusicPlayerActivity.class);
                Song song = PlayerSong.getInstance().getPlayerSong();
                if (null != song) {
                    intent.putExtra("song", song);
                }
                intent.putExtra("type", 1);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mExitTime < 2000) {
            super.onBackPressed();
        } else {
            mExitTime = currentTime;
            showToast(getString(R.string.exit_hint));
        }
    }

    @Override
    public void checkNewVersionSuccess(final VersionCheckResponse versionCheckResponse) {
        if (versionCheckResponse.updatetype != 0) {
            showVersionUpdateDialog(versionCheckResponse);
        }
        AdBean.getInstance().setVerify(versionCheckResponse.verify);
    }

    @Override
    public void checkNewVersionFailure(String msg) {
        Log.e("hhh", "checkNewVersionFailure msg= " + msg);
    }

    private void showVersionUpdateDialog(VersionCheckResponse versionCheckResponse) {
        VersionUpdateDialog versionUpdateDialog = VersionUpdateDialog.newInstance(mActivity, versionCheckResponse);
        final String fileName = versionCheckResponse.remark + ".apk";
        versionUpdateDialog.setUpdateVersionListener(new VersionUpdateDialog.UpdateVersionListener() {
            @Override
            public void updateVersion(String url) {
                DownloadAPKDialog downloadAPKDialog = DownloadAPKDialog.newIntance(url, fileName);
                downloadAPKDialog.setDownloadLinstener(new DownloadAPKDialog.DownloadLinstener() {
                    @Override
                    public void onFinish(File file) {
                        AppUtils.installApp(file);
                    }
                });
                downloadAPKDialog.show(getFragmentManager(), "DownloadAPKDialog");
            }
        });
        versionUpdateDialog.show(getFragmentManager(), "VersionUpdateDialog");
    }
}
