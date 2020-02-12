package com.shangame.fiction.book.cover;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shangame.fiction.R;


/**
 * 图书封面
 * Create by Speedy on 2018/7/24
 */
public class BookCoverView extends FrameLayout {

    private ImageView ivCover;
    private ImageView ivLogo;

    private BookCoverValue startValue;
    private BookCoverValue endValue;

    private BookStateLinstener bookStateLinstener;

    private Activity mActivity;

    private boolean isOpen;

    public BookCoverView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BookCoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BookCoverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.book_cover_layout, null);
        setBackgroundColor(Color.TRANSPARENT);
        ivCover =  contentView.findViewById(R.id.ivCover);
        ivLogo =  contentView.findViewById(R.id.ivLogo);
        addView(contentView);
    }

    /**
     * 绑定封面图片
     * @param activity
     * @param coverImageView
     */
    public void bindCoverImageView(Activity activity,ImageView coverImageView){
        this.mActivity = activity;
        ivCover.setImageDrawable(coverImageView.getDrawable());
        final ViewGroup window = (ViewGroup) activity.getWindow().getDecorView();
        int[] location = new int[2] ;
        coverImageView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标(包括了通知栏的高度)

        startValue = new BookCoverValue(location[0], location[1], coverImageView.getWidth()+location[0], location[1]+coverImageView.getHeight());
        endValue = new BookCoverValue(window.getLeft(), window.getTop(), window.getRight(), window.getBottom());

        window.addView(this);
    }

    public void unBindTargetView(){
        if(mActivity != null){
            final ViewGroup window = (ViewGroup) mActivity.getWindow().getDecorView();
            window.removeView(this);
        }
    }



    /**
     * 开启动画
     */
    public void openBook() {
        if(mActivity == null){
            return;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BookCoverEvaluator(), startValue, endValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BookCoverValue midBookCoverValue = (BookCoverValue) animation.getAnimatedValue();
                ivCover.setX(midBookCoverValue.getLeft());
                ivCover.setY(midBookCoverValue.getTop());
                ViewGroup.LayoutParams layoutParams = ivCover.getLayoutParams();
                layoutParams.width = midBookCoverValue.getRight() - midBookCoverValue.getLeft();
                layoutParams.height = midBookCoverValue.getBottom() - midBookCoverValue.getTop();
                ivCover.setLayoutParams(layoutParams);

                ivLogo.setX(midBookCoverValue.getLeft());
                ivLogo.setY(midBookCoverValue.getTop());
                ViewGroup.LayoutParams layoutParams1 = ivLogo.getLayoutParams();
                layoutParams1.width = midBookCoverValue.getRight() - midBookCoverValue.getLeft();
                layoutParams1.height = midBookCoverValue.getBottom() - midBookCoverValue.getTop();
                ivLogo.setLayoutParams(layoutParams1);

            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(bookStateLinstener != null){
                    bookStateLinstener.onBookOpened();
                }
                isOpen = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ivCover.setPivotX(0);
        ivCover.setPivotY(ivCover.getY()/2);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivCover, "rotationY", 0, -180);
        objectAnimator.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator, objectAnimator);
        animatorSet.start();
    }


    public void closeBook(){

        if(!isOpen){
            return;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BookCoverEvaluator(),endValue,startValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BookCoverValue midBookCoverValue = (BookCoverValue) animation.getAnimatedValue();
                ivCover.setX(midBookCoverValue.getLeft());
                ivCover.setY(midBookCoverValue.getTop());
                ViewGroup.LayoutParams layoutParams = ivCover.getLayoutParams();
                layoutParams.width = midBookCoverValue.getRight() - midBookCoverValue.getLeft();
                layoutParams.height = midBookCoverValue.getBottom() - midBookCoverValue.getTop();
                ivCover.setLayoutParams(layoutParams);

                ivLogo.setX(midBookCoverValue.getLeft());
                ivLogo.setY(midBookCoverValue.getTop());
                ViewGroup.LayoutParams layoutParams1 = ivLogo.getLayoutParams();
                layoutParams1.width = midBookCoverValue.getRight() - midBookCoverValue.getLeft();
                layoutParams1.height = midBookCoverValue.getBottom() - midBookCoverValue.getTop();
                ivLogo.setLayoutParams(layoutParams1);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isOpen = false;
                unBindTargetView();
                if(bookStateLinstener != null){
                    bookStateLinstener.onBookClosed();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ivCover.setPivotX(0);
        ivCover.setPivotY(ivCover.getY()/2);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivCover, "rotationY", -180, 0);
        objectAnimator.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator, objectAnimator);
        animatorSet.start();
    }


    public void setBookStateLinstener(BookStateLinstener bookStateLinstener) {
        this.bookStateLinstener = bookStateLinstener;
    }


    /**
     * 书状态监听器
     */
    public interface BookStateLinstener {
        void onBookOpened();
        void onBookClosed();
    }

}
