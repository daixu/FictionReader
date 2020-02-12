package com.shangame.fiction.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.shangame.fiction.R;
import com.umeng.commonsdk.debug.D;


/**
 * 录边框导航栏
 * Created by Speedy on 2016/12/19.
 */
public class SideBar extends View{

	private static final String TAG = "SideBar";

	private float currentX;
	private float currentY;

	private float mItemHeight; 		// 索引占位高

	private int itemCount = 1;

	private RecyclerView mRecyclerView;//关联 RecyclerView

	private Bitmap scrollBar;

	private int cententHeight;

	private boolean isMoveing;

	public SideBar(Context context) {
		super(context);

	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		scrollBar = BitmapFactory.decodeResource(getResources(),R.drawable.touch_bar);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scrollBar = BitmapFactory.decodeResource(getResources(),R.drawable.touch_bar);

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec); //获取宽的模式
//		int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式

		int widthSize = MeasureSpec.getSize(widthMeasureSpec); //获取宽的尺寸
		int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高
		int width;
		int height ;
		if(widthMode == MeasureSpec.EXACTLY){
			width = widthSize;
		}else{
			width = scrollBar.getWidth()*2;
		}
		height = heightSize;

		setMeasuredDimension(width,height);

	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		cententHeight = getHeight() - scrollBar.getHeight();

		//修改当前的坐标
		this.currentX = scrollBar.getWidth()/2;

		this.currentY = event.getY();

		float temp = currentY - cententHeight;
		if(temp > 0 ){
			currentY = currentY - temp;
		}else if(currentY < 0){
			currentY = 0;
		}

		this.invalidate();

		// 获取触摸点的Y轴坐标
		int touchY = (int) event.getY();

		int index = computeIndex(touchY);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				isMoveing = true;
				mRecyclerView.scrollToPosition(index);
				break;
			case MotionEvent.ACTION_UP:
				isMoveing = false;
				break;
		}
		return true;
	}



	private int computeIndex(int touchY){
		//计算索引位置
		int index = (int) ((touchY)/mItemHeight);
		if(index >= itemCount){
			index = itemCount-1;
		}else if(index < 0){
			index = 0;
		}
		return index;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		Paint  paint  = new Paint ();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawBitmap(scrollBar,currentX,currentY,paint);
	}


	public void setRecyclerView(RecyclerView recyclerView){
		this.mRecyclerView = recyclerView;
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				if(isMoveing){
					return;
				}
				RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
				if(layoutManager instanceof LinearLayoutManager){
					LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
					int position = linearLayoutManager.findFirstVisibleItemPosition();
					currentY = position * mItemHeight;
					invalidate();
				}
			}
		});
	}






	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
		float h1 = getHeight();
		float h2 = scrollBar.getHeight();
		this.mItemHeight = (h1-h2)/itemCount;
	}

}
