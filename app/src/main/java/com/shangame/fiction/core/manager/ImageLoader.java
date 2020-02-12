package com.shangame.fiction.core.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shangame.fiction.R;

/**
 * 图片加载器
 * Create by Speedy on 2018/7/23
 */
public class ImageLoader {

    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;

    private ImageLoader(Activity activity) {
        mActivity = activity;
    }

    private ImageLoader(Fragment fragment) {
        mFragment = fragment;
    }

    private ImageLoader(Context context) {
        mContext = context;
    }

    public static ImageLoader with(Activity activity) {
        return new ImageLoader(activity);
    }

    public static ImageLoader with(Context context) {
        return new ImageLoader(context);
    }

    public static ImageLoader with(Fragment fragment) {
        return new ImageLoader(fragment);
    }

    /**
     * 加载用户头像
     *
     * @param target
     * @param path
     */
    public void loadUserIcon(ImageView target, String path) {
        loadUserIcon(target, path, R.drawable.default_head);
    }

    /**
     * 加载用户头像
     *
     * @param target
     * @param path
     * @param defaultRes
     */
    public void loadUserIcon(ImageView target, String path, @DrawableRes int defaultRes) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(defaultRes)
                .override(100, 100);

        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(target);
        }
    }

    public void loadUserIcon(ImageView target, String path, @DrawableRes int defaultRes, int width, int height) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(defaultRes)
                .override(width, height);

        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(target);
        }
    }

    /**
     * 加载小说封面
     *
     * @param target
     * @param path
     * @param width
     * @param height
     */
    public void loadCover(ImageView target, String path, int width, int height) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.default_cover);
        options.override(width, height);
        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(target);
        }
    }

    /**
     * 加载小说封面
     *
     * @param target
     * @param path
     */
    public void loadCover(ImageView target, String path) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.default_cover);

        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(target);
        }
    }

    /**
     * 加载小说封面
     *
     * @param target
     * @param resId
     */
    public void loadCover(ImageView target, int resId) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.default_cover);

        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(resId)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(resId)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(resId)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(resId)
                    .apply(options)
                    .into(target);
        }
    }

    public void loadPicture(ImageView target, String path, int width, int height) {
        RequestOptions options = new RequestOptions();
        options.override(width, height);

        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(target);
        }
    }

    public void loadPicture(ImageView target, String path, @DrawableRes int defaultRes) {
        RequestOptions options = new RequestOptions();
        options.placeholder(defaultRes);

        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .apply(options)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(target);
        }
    }

    public void loadPicture(ImageView target, String path) {
        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!mActivity.isDestroyed()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .into(target);
                }
            } else {
                if (!mActivity.isFinishing()) {
                    Glide.with(mActivity)
                            .asBitmap()
                            .load(path)
                            .into(target);
                }
            }
        } else if (mFragment != null) {
            if (!mFragment.isDetached()) {
                Glide.with(mFragment)
                        .asBitmap()
                        .load(path)
                        .into(target);
            }
        } else if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .into(target);
        }
    }
}
