package com.shangame.fiction.ui.reader.local;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shangame.fiction.R;
import com.shangame.fiction.AppContext;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.Constant;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.BrightnessUtils;
import com.shangame.fiction.core.utils.LogUtils;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.core.utils.StringUtils;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.statis.ReadTracer;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.LocalBookBean;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.popup.OfflineReadRedPacketPopupWindow;
import com.shangame.fiction.ui.popup.ReadRedPacketPopupWindow;
import com.shangame.fiction.ui.popup.RedTaskPopupWindow;
import com.shangame.fiction.ui.reader.PageSettingPopupWindow;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.ui.reader.local.bean.BookChapterBean;
import com.shangame.fiction.ui.reader.local.bean.CollBookBean;
import com.shangame.fiction.ui.reader.local.page.PageLoader;
import com.shangame.fiction.ui.reader.local.page.PageView;
import com.shangame.fiction.ui.reader.local.page.TxtChapter;
import com.shangame.fiction.ui.reader.local.page.TxtPage;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import notchtools.geek.com.notchtools.NotchTools;

import static android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LocalReadActivity extends BaseActivity implements ReadContract.View {

    private static final String TAG = "ReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

    // 注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");

    private static final int WHAT_CATEGORY = 1;
    private static final int WHAT_CHAPTER = 2;

    private ReadTracer readTracer;

    private ImageView ivRed;

    private DrawerLayout mDlSlide;
    private PageView mPvPage;
    private TextView mTvPageTip;
    private ListView mLvCategory;
    private TextView mTvChapterTotal;

    private TextView mBeforeChapter;
    private SeekBar mSeekBar;
    private TextView mNextChapter;
    private TextView mTvMenu;
    private TextView mTvDayNight;
    private TextView mTvSetting;

    private PageConfig mPageConfig;

    private View readHeadLayout;
    private View mLayoutHeadTop;
    private View mLayoutReadTop;
    private View mLayoutRedTop;
    private View readSettingLayout;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CategoryAdapter mCategoryAdapter;
    private CollBookBean mCollBook;

    private ReadPresenter mPresenter;

    private TextView tvBookName;

    private static int unUserTaskId;

    private int baColor;

    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_CATEGORY:
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
                    break;
                case WHAT_CHAPTER:
                    mPageLoader.openChapter();
                    break;
                default:
                    break;
            }
        }
    };

    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent && null != intent.getAction()) {
                if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra("level", 0);
                    int total = intent.getIntExtra("scale", 100);// 获得总电量
                    float batterPercent = (float) level / total;
                    mPageLoader.updateBattery(batterPercent);
                } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    // 监听分钟的变化
                    mPageLoader.updateTime();
                }
            }
        }
    };

    private BroadcastReceiver mReadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent && null != intent.getAction()) {
                if (BroadcastAction.OFFLINE_READ_RED_PACKET.equals(intent.getAction())) {
                    String desc = intent.getStringExtra("desc");
                    int taskId = intent.getIntExtra(SharedKey.TASK_ID, TaskId.READ_30);
                    unUserTaskId = taskId;
                    OfflineReadRedPacketPopupWindow redPacketPopupWindow = new OfflineReadRedPacketPopupWindow(mActivity, desc);
                    redPacketPopupWindow.show(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lunchLoginActivity(FROM_READ_PACKAGE);
                        }
                    });
                } else if (BroadcastAction.READ_RED_PACKET.equals(intent.getAction())) {
                    int taskId = intent.getIntExtra(SharedKey.TASK_ID, TaskId.READ_30);
                    unUserTaskId = taskId;
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    mPresenter.getTaskAward(userId, taskId, false);
                }
            }
        }
    };

    // 亮度调节监听
    // 由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            // 如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(LocalReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(LocalReadActivity.this, BrightnessUtils.getScreenBrightness(LocalReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(LocalReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setDefaultBrightness(LocalReadActivity.this);
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    /***************params*****************/
    private boolean isCollected = false; // isFromSDCard
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private boolean isRegistered = false;

    private String mBookId;

    public static void startActivity(Context context, CollBookBean collBook, boolean isCollected) {
        context.startActivity(new Intent(context, ReadActivity.class)
                .putExtra(EXTRA_IS_COLLECTED, isCollected)
                .putExtra(EXTRA_COLL_BOOK, collBook));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_read);

        init();
    }

    private void init() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = getIntent().getData();

            String path = getFilePath(mContext, uri);
            if (!TextUtils.isEmpty(path)) {
                Log.e("hhh", "data path= " + path);
                File file = new File(path);
                addBook(file);

                CollBookBean collBookBean = new CollBookBean();
                String bookId = MD5Utils.strToMd5By16(file.getAbsolutePath());
                collBookBean.set_id(bookId);
                collBookBean.setCover(file.getAbsolutePath());
                collBookBean.setIsLocal(true);
                CollBookBean bookBean = BookRepository.getInstance().getCollBook(bookId);
                if (null != bookBean) {
                    collBookBean.setIsUpdate(bookBean.getIsUpdate());
                }
                collBookBean.setLatelyFollower(0);
                collBookBean.setRetentionRatio(0.0);
                collBookBean.setTitle(file.getName().replace(".txt", ""));

                collBookBean.setUpdated(StringUtils.dateConvert(file.lastModified(), Constant.FORMAT_BOOK_DATE));
                collBookBean.setShortIntro("无");

                mCollBook = collBookBean;
                isCollected = true;
                mBookId = mCollBook.get_id();
            } else {
                Toast.makeText(mContext, "文本打开失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            initData();
        }

        initWindow();
        initView();
        initPageConfig();
        initPresenter();
        initWidget();
        initListener();
        processLogic();
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT > 16 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 28) {
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                getWindow().setAttributes(attributes);
            }
            // 设置页面全屏显示
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void addBook(File file) {
        LocalBookBean booksBean = convertBooksBean(file);
        LocalBookBeanDao dao = DbManager.getDaoSession(AppContext.getContext()).getLocalBookBeanDao();
        dao.insertOrReplaceInTx(booksBean);

        LocalBroadcastManager.getInstance(AppContext.getContext()).sendBroadcast(new Intent(BroadcastAction.UPDATE_LOCAL_BOOK));
    }

    private LocalBookBean convertBooksBean(File file) {
        LocalBookBean booksBean = new LocalBookBean();
        booksBean.strId = MD5Utils.strToMd5By16(file.getAbsolutePath());
        booksBean.bookName = file.getName().replace(".txt", "");
        booksBean.bookId = -1;
        booksBean.author = "";
        booksBean.bookCover = "";
        booksBean.isLocal = true;
        booksBean.lastModifyTime = System.currentTimeMillis();
        booksBean.path = file.getAbsolutePath();
        return booksBean;
    }

    public String getFilePath(final Context context, final Uri uri) {
        String data = "";
        if (null != uri) {
            final String scheme = uri.getScheme();
            if (scheme == null) {
                data = uri.getPath();
            } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                data = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1) {
                            data = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }

                if (data == null || data.length() <= 0) {
                    if (uri.getPath() != null && uri.getPath().contains("/storage/emulated/")) {
                        data = uri.getPath().substring(uri.getPath().indexOf("/storage/emulated/"));
                    } else if (uri.getPath() != null && uri.getPath().contains("/external/")) {
                        String path = uri.getPath();
                        data = uri.getPath().substring(path.indexOf("/external/") + 9, path.length());
                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        data = filePath + data;
                    }
                }
            }
        }
        return data;
    }

    public Observable<String> getRealFilePath(final Context context, final Uri uri) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String data = "";
                if (null != uri) {
                    final String scheme = uri.getScheme();
                    if (scheme == null) {
                        data = uri.getPath();
                    } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                        data = uri.getPath();
                    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        if (null != cursor) {
                            if (cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                                if (index > -1) {
                                    data = cursor.getString(index);
                                }
                            }
                            cursor.close();
                        }

                        if (data == null || data.length() <= 0) {
                            if (uri.getPath() != null && uri.getPath().contains("/storage/emulated/")) {
                                data = uri.getPath().substring(uri.getPath().indexOf("/storage/emulated/"));
                            } else if (uri.getPath() != null && uri.getPath().contains("/external/")) {
                                data = uri.getPath().substring(uri.getPath().indexOf("/external/"));
                            }
                        }
                    }
                }
                Log.e("hhh", "data= " + data);
                e.onNext(data == null ? "" : data);
                e.onComplete();
            }
        });
    }

    private void initPresenter() {
        mPresenter = new ReadPresenter();
        mPresenter.attachView(this);
    }

    private void initPageConfig() {
        mPageConfig = PageConfig.getInstance(mContext);
        baColor = mPageConfig.backgroundColor;
    }

    private void initView() {
        ivRed = findViewById(R.id.ivRed);
        mDlSlide = findViewById(R.id.read_dl_slide);
        mPvPage = findViewById(R.id.read_pv_page);
        mTvPageTip = findViewById(R.id.read_tv_page_tip);

        mBeforeChapter = findViewById(R.id.beforeChapter);
        mSeekBar = findViewById(R.id.seekBar);
        mNextChapter = findViewById(R.id.nextChapter);
        mTvMenu = findViewById(R.id.tvMenu);
        mTvDayNight = findViewById(R.id.tvDayMode);
        mTvSetting = findViewById(R.id.tvFontSetting);

        mLvCategory = findViewById(R.id.read_iv_category);
        mTvChapterTotal = findViewById(R.id.tv_chapter_total);

        readTracer = new ReadTracer(mContext);
    }

    protected void initData() {
        mCollBook = getIntent().getParcelableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED, false);

        mBookId = mCollBook.get_id();
    }

    protected void initWidget() {
        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        //获取页面加载器
        mPageLoader = mPvPage.getPageLoader(mCollBook);
        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //侧边打开后，返回键能够起作用
        mDlSlide.setFocusableInTouchMode(false);

        setUpAdapter();

        //夜间模式按钮的状态
        toggleNightMode();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.READ_RED_PACKET);
        filter.addAction(BroadcastAction.OFFLINE_READ_RED_PACKET);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReadReceiver, filter);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");

        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();
    }

    private void initTopMenu() {
        readHeadLayout = findViewById(R.id.readHeadLayout);
        tvBookName = readHeadLayout.findViewById(R.id.tvBookName);

        mLayoutHeadTop = readHeadLayout.findViewById(R.id.layout_head_top);

        mLayoutReadTop = findViewById(R.id.layout_read_top);
        mLayoutRedTop = findViewById(R.id.layout_red_top);

        if (Build.VERSION.SDK_INT > 20) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @SuppressLint("NewApi")
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    boolean isNotchScreen = NotchTools.getFullScreenTools().isNotchScreen(getWindow());
                    Log.e("hhh", "isNotchScreen= " + isNotchScreen);
                    if (isNotchScreen) {
                        mLayoutHeadTop.setVisibility(View.VISIBLE);
                        mLayoutRedTop.setVisibility(View.VISIBLE);
                        mLayoutReadTop.setVisibility(View.VISIBLE);
                        mLayoutReadTop.setBackgroundResource(mPageConfig.backgroundColor);
                    } else {
                        mLayoutHeadTop.setVisibility(View.GONE);
                        mLayoutRedTop.setVisibility(View.GONE);
                        mLayoutReadTop.setVisibility(View.GONE);
                    }
                    getWindow().getDecorView().setOnApplyWindowInsetsListener(null);
                    return view.onApplyWindowInsets(windowInsets);
                }
            });
        } else {
            mLayoutHeadTop.setVisibility(View.GONE);
            mLayoutRedTop.setVisibility(View.GONE);
            mLayoutReadTop.setVisibility(View.GONE);
        }
    }

    private void initBottomMenu() {
        readSettingLayout = findViewById(R.id.readSettingLayout);
    }

    private void toggleNightMode() {
        if (mPageConfig.dayMode == PageConfig.DayMode.SUN_MODE) {
            mTvDayNight.setText(R.string.sun_mode);
            Drawable drawable = getResources().getDrawable(R.drawable.moon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvDayNight.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            mTvDayNight.setText(R.string.night_mode);
            Drawable drawable = getResources().getDrawable(R.drawable.sun);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvDayNight.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void setUpAdapter() {
        mCategoryAdapter = new CategoryAdapter();
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);
    }

    // 注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }

    protected void initListener() {
        ivRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long totalReadTime = readTracer.getTotalReadTime();
                RedTaskPopupWindow redTaskPopupWindow = new RedTaskPopupWindow(mActivity, totalReadTime);
                redTaskPopupWindow.show();
            }
        });
        readHeadLayout.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPageLoader.setOnPageChangeListener(
                new PageLoader.OnPageChangeListener() {

                    @Override
                    public void onChapterChange(int pos) {
                        mCategoryAdapter.setChapter(pos);
                    }

                    @Override
                    public void requestChapters(List<TxtChapter> requestChapters) {
                        mPresenter.loadChapter(mBookId, requestChapters);
                        mHandler.sendEmptyMessage(WHAT_CATEGORY);
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                    }

                    @Override
                    public void onCategoryFinish(List<TxtChapter> chapters) {
                        for (TxtChapter chapter : chapters) {
                            chapter.setTitle(chapter.getTitle());
                        }
                        mTvChapterTotal.setText(getString(R.string.total_chapter, chapters.size()));
                        mCategoryAdapter.refreshItems(chapters);
                    }

                    @Override
                    public void onPageCountChange(int count) {
                        mSeekBar.setMax(Math.max(0, count - 1));
                        mSeekBar.setProgress(0);
                        // 如果处于错误状态，那么就冻结使用
                        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING
                                || mPageLoader.getPageStatus() == PageLoader.STATUS_ERROR) {
                            mSeekBar.setEnabled(false);
                        } else {
                            mSeekBar.setEnabled(true);
                        }
                    }

                    @Override
                    public void onPageChange(final int pos, final TxtPage txtPage) {
                        mSeekBar.post(new Runnable() {
                            @Override
                            public void run() {
                                readTracer.flipPage();
                                mSeekBar.setProgress(pos);
                            }
                        });
                    }
                }
        );

        mSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (readSettingLayout.getVisibility() == VISIBLE) {
                            //显示标题
                            mTvPageTip.setText((progress + 1) + "/" + (mSeekBar.getMax() + 1));
                            mTvPageTip.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //进行切换
                        int pagePos = mSeekBar.getProgress();
                        if (pagePos != mPageLoader.getPagePos()) {
                            mPageLoader.skipToPage(pagePos);
                        }
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                    }
                }
        );

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public void prePage() {
            }

            @Override
            public void nextPage() {
            }

            @Override
            public void cancel() {
            }
        });

        mLvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDlSlide.closeDrawer(Gravity.START);
                mPageLoader.skipToChapter(position);
            }
        });

        mTvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //移动到指定位置
                if (mCategoryAdapter.getCount() > 0) {
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
                }
                //切换菜单
                toggleMenu(true);
                //打开侧滑动栏
                mDlSlide.openDrawer(Gravity.START);
            }
        });

        mTvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu(false);
                showPageSettingPopupWindow(v);
            }
        });

        mBeforeChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageLoader.skipPreChapter()) {
                    mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                }
            }
        });

        mNextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageLoader.skipNextChapter()) {
                    mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                }
            }
        });

        mTvDayNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDayMode();
            }
        });
    }

    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        if (readHeadLayout.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        }
        return false;
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (readHeadLayout.getVisibility() == View.VISIBLE) {
            //关闭
            readHeadLayout.startAnimation(mTopOutAnim);
            readSettingLayout.startAnimation(mBottomOutAnim);
            readHeadLayout.setVisibility(GONE);
            readSettingLayout.setVisibility(GONE);
            mTvPageTip.setVisibility(GONE);
            ivRed.setVisibility(VISIBLE);
        } else {
            readHeadLayout.setVisibility(View.VISIBLE);
            readSettingLayout.setVisibility(View.VISIBLE);
            readHeadLayout.startAnimation(mTopInAnim);
            readSettingLayout.startAnimation(mBottomInAnim);
            if (null != mCollBook) {
                tvBookName.setText(mCollBook.getTitle());
            }

            ivRed.setVisibility(GONE);
        }
    }

    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) {
            return;
        }
        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);

        mTopInAnim.setDuration(200);
        mBottomInAnim.setDuration(200);
    }

    protected void processLogic() {
        // 如果是已经收藏的，那么就从数据库中获取目录
        BookRepository.getInstance().getBookChaptersInRx(mBookId).subscribe(new SingleObserver<List<BookChapterBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<BookChapterBean> bookChapterBeen) {
                mPageLoader.getCollBook().setBookChapters(bookChapterBeen);
                // 刷新章节列表
                mPageLoader.refreshChapterList();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e);
            }
        });
    }

    /***************************view************************************/

    @Override
    public void showCategory(List<BookChapterBean> bookChapters) {
        mPageLoader.getCollBook().setBookChapters(bookChapters);
        mPageLoader.refreshChapterList();

        // 如果是目录更新的情况，那么就需要存储更新数据
        if (mCollBook.getIsUpdate() && isCollected) {
            BookRepository.getInstance()
                    .saveBookChaptersWithAsync(bookChapters);
        }
    }

    @Override
    public void finishChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mHandler.sendEmptyMessage(WHAT_CHAPTER);
        }
        // 当完成章节的时候，刷新列表
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
        }
    }

    private void showPageSettingPopupWindow(View view) {
        PageSettingPopupWindow pageSettingPopupWindow = new PageSettingPopupWindow(this);
        pageSettingPopupWindow.setSettingCallback(new PageSettingPopupWindow.SettingCallback() {

            @Override
            public void setActivityLight(int light) {
                ScreenUtils.setActivityLight(mActivity, light);
                mPageConfig.saveSceenLight(light);
            }

            @Override
            public void setBackgroud(int color) {
                if (mPageConfig.dayMode == PageConfig.DayMode.NIGHT_MODE) {
                    changeDayMode();
                }
                setStatusBarColor(color);
                mLayoutReadTop.setBackgroundResource(color);
                // AppSetting.getInstance(mActivity).putInt(SharedKey.READ_BG, color);
                mPageConfig.saveBackgroundColor(color);
                mPageLoader.setPageStyle(color);
            }

            @Override
            public void setLineSpace(int space) {
                mPageConfig.saveLineSpace(space);
                mPageLoader.setLineSpace(space);
            }

            @Override
            public void setFontSize(int size) {
                mPageConfig.saveFontSize(size);
                mPageLoader.setTextSize(size);
            }
        });
        pageSettingPopupWindow.initConfig(mPageConfig);

        pageSettingPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void changeDayMode() {
        if (mPageConfig.dayMode == PageConfig.DayMode.SUN_MODE) {
            isNightMode = true;
            mPageConfig.dayMode = PageConfig.DayMode.NIGHT_MODE;
            mPageConfig.saveDayMode(PageConfig.DayMode.NIGHT_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.ACTIVITY_BRIGHTNESS, PageConfig.DayMode.NIGHT_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.READ_DAY_TPYE, AppConfig.NIGHT_BRIGHTNESS);

            AppSetting.getInstance(mActivity).putInt(SharedKey.READ_BG, mPageConfig.getBackgroundColor());

            mPageConfig.saveBackgroundColor(PageConfig.BackgroundColor.NIGHT_COLOR);
            baColor = PageConfig.BackgroundColor.NIGHT_COLOR;
            setStatusBarColor(baColor);
            mLayoutReadTop.setBackgroundResource(baColor);
        } else {
            isNightMode = false;
            mPageConfig.dayMode = PageConfig.DayMode.SUN_MODE;
            mPageConfig.saveDayMode(PageConfig.DayMode.SUN_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.ACTIVITY_BRIGHTNESS, PageConfig.DayMode.SUN_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.ACTIVITY_BRIGHTNESS, AppConfig.SUN_BRIGHTNESS);

            baColor = AppSetting.getInstance(mActivity).getInt(SharedKey.READ_BG, PageConfig.BackgroundColor.COLOR_5);
            mPageConfig.saveBackgroundColor(baColor);
            setStatusBarColor(baColor);
            mLayoutReadTop.setBackgroundResource(baColor);
        }
        mPageLoader.setNightMode(isNightMode);
        toggleNightMode();
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId) {
        Logger.i(TAG, "getTaskAwardSuccess goldType= " + taskAwardResponse.goldtype);
        if (taskAwardResponse.goldtype == 1) {
            ReadRedPacketPopupWindow redPacketPopupWindow = new ReadRedPacketPopupWindow(mActivity, taskAwardResponse);
            redPacketPopupWindow.show();
        } else {
            Logger.i(TAG, "getTaskAwardSuccess number= " + taskAwardResponse.number);
            Logger.i(TAG, "getTaskAwardSuccess regType= " + taskAwardResponse.regtype);
            if (taskAwardResponse.number > 0) {
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                userInfo.money = (long) taskAwardResponse.number;
                UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

                Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
                intent.putExtra(SharedKey.TASK_ID, taskId);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
                taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
            } else if (taskAwardResponse.regtype == 0) {
                showToast("已经领取");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (readHeadLayout.getVisibility() == View.VISIBLE) {
            // 非全屏下才收缩，全屏下直接退出
            if (!isFullScreen) {
                toggleMenu(true);
                return;
            }
        } else if (mDlSlide.isDrawerOpen(Gravity.START)) {
            mDlSlide.closeDrawer(Gravity.START);
            return;
        }

        exit();
    }

    // 退出
    private void exit() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
        if (null != readTracer) {
            readTracer.startRead();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWakeLock) {
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWakeLock) {
            mWakeLock.release();
        }
        if (isCollected) {
            mPageLoader.saveRecord();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
        if (null != readTracer) {
            readTracer.stopRead();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReadReceiver);

        mHandler.removeMessages(WHAT_CATEGORY);
        mHandler.removeMessages(WHAT_CHAPTER);

        mPageLoader.closeBook();
        mPageLoader = null;

        mPresenter.detachView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager
                .getInstance().isVolumeTurnPage();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToPrePage();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToNextPage();
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final int FROM_READ_PACKAGE = 258;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SystemBarUtils.hideStableStatusBar(this);
        if (requestCode == REQUEST_MORE_SETTING) {
            initBottomMenu();
            // 设置显示状态
            if (isFullScreen) {
                SystemBarUtils.hideStableNavBar(this);
            } else {
                SystemBarUtils.showStableNavBar(this);
            }
        } else if (requestCode == LUNCH_LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                readTracer.updateReadTracer();
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                int formCode = data.getIntExtra(SharedKey.FROM_CODE, 0);
                if (formCode == FROM_READ_PACKAGE) {
                    mPresenter.getTaskAward(userId, unUserTaskId, false);
                }
            }
        }
    }
}
