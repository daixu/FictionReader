package com.shangame.fiction.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;


import com.shangame.fiction.R;
import com.shangame.fiction.book.page.Line;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Create by Speedy on 2018/12/14
 */
public class ShareImageBuilder {

    private static final String TAG = "ShareImageBuilder";

    private Context mContext;

    private Bitmap mBitmap;

    private Canvas mCanvas;

    // 画笔－－画图片
    private Paint mPicturePaint = new Paint();

    private Paint mHeadPaint = new Paint();

    // 画笔－－写字
    private Paint mTextPaint = new Paint();

    // 画笔－－写运动数据
    private Paint mDataPaint = new Paint();

    private ImageView mImageView;

    private int mWidth;

    private int mHeight;

    private int padding;

    private int startX;
    private int startY;

    private int textSize;
    private int textSpace;

    private final int radius = 30;

    private String mChapterName;
    private List<Line> mLines = new LinkedList<>();

    private Bitmap logoBitmap;
    private Bitmap coverBitmap;
    private Bitmap qrBitmap;

    private String imagePath;

    private String bookName = "妖瞳逆世：帝君盛宠小狂妃帝君盛宠小狂妃安一宁";
    private String authorName = "作者：安一宁";
    private String content = "游灵儿难得重生，竟然被污蔑引来各方 成怪，差点魂归天外。一双异瞳，引来各方觊 觎。那又如何？她游灵儿神挡杀佛佛阻废...";

    private final int coverHeight = 300;

    public ShareImageBuilder(Context context) {
        mContext = context;
        padding = dpToPxInt(context, 16);
        textSize = (int) spToPx(context,17);
        textSpace = textSize;
    }




    public final String buildImage() {
        initCanvas();
        drawBackgroud();
        drawLogo();
        drawBookInfo();
        drawContent();
        drawFooder();
        closeCanvas();
        return saveImage();
    }

    public void initBookInfo(String bookName,String authorName,String content,Bitmap coverBitmap){
        this.bookName = bookName;
        this.authorName = authorName;
        this.content = content;
        this.coverBitmap = coverBitmap;
        imagePath = Environment.getExternalStorageDirectory()
                .getPath() + "/share.png";
    }



    public void initData(String chapterName,List<Line> lines) {

        mChapterName = chapterName;
        mLines.addAll(lines);

        logoBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.share_logo);

        qrBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.share_qr);

        mWidth = getScreenWidth(mContext);

        int textHeight = (textSize + textSpace)+(textSize + textSpace)*(lines.size());

        mHeight = (padding + logoBitmap.getHeight())    //logo高度
                 + (padding+ coverHeight + padding+radius)   //书本简介高度
                 + (padding +textHeight)//文章
                 +( padding + textSize + textSpace)//
                 + (padding+qrBitmap.getHeight())
                 + (2*(textSize + textSpace))
                 + padding;

    }

    private void initCanvas() {
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        mCanvas = new Canvas(mBitmap);


        mTextPaint.setColor(Color.BLACK);// 白色画笔
        mTextPaint.setTextSize(textSize);// 设置字体大小
        // 绘制文字
        mDataPaint.setColor(Color.BLUE);
        mDataPaint.setTextSize(textSize);// 设置字体大小

    }

    private void drawBackgroud() {
        mCanvas.drawColor(Color.parseColor("#f9f8f4"));
    }


    private void drawLogo() {
        startX = mWidth - padding - logoBitmap.getWidth();
        startY = padding;
        mCanvas.drawBitmap(logoBitmap, startX, startY, mPicturePaint);
        startY = startY+ logoBitmap.getHeight();
    }

    private void drawBookInfo() {

        //绘制背景
        int left = padding;
        int top = startY+padding;
        int right = mWidth - padding;
        int bottom = top + coverHeight+padding+radius;
        RectF rectf = new RectF(left,top,right,bottom);
        mHeadPaint.setColor(Color.WHITE);
        mHeadPaint.setStyle(Paint.Style.FILL);
        mHeadPaint.setShadowLayer(100,0,0,Color.parseColor("#10000000"));
        mCanvas.drawRoundRect(rectf,radius,20,mHeadPaint);


        mPicturePaint.setShadowLayer(10,0,0,Color.parseColor("#10000000"));
        //绘制封面
        mCanvas.drawBitmap(coverBitmap, left+padding, top+padding, mPicturePaint);


        //计算标题文本宽度
        int txtWidth = (right - padding) - (left+coverBitmap.getWidth()+padding);




        //绘制书名
        int tempY = top+padding+radius+10;
        mTextPaint.setFakeBoldText(true);
        Rect bookNameRect = measureText(bookName,mTextPaint);

        //测量书本名称长度，过长则截断
        int size = mTextPaint.breakText(bookName, true, txtWidth, null);
        if(size < bookName.length()){
            bookName = bookName.substring(0,size);
        }

        mCanvas.drawText(bookName,left+coverBitmap.getWidth()+radius+padding,tempY,mTextPaint);
        mTextPaint.setFakeBoldText(false);


        //绘制作者
        mTextPaint.setTextSize(spToPx(mContext,12));
        tempY = tempY+ bookNameRect.height()+textSpace/2;
        mCanvas.drawText(authorName,left+coverBitmap.getWidth()+radius+padding,tempY,mTextPaint);


        //绘制简介(最多显示3行)
        mTextPaint.setColor(Color.parseColor("#8B8A8F"));

        int lenght = mTextPaint.breakText(content, true, txtWidth, null);

        for (int i = 0; i < 3; i++) {

            tempY = tempY+ bookNameRect.height()+textSpace/2;

            if(lenght < content.length()){
                String intro = content.substring(0,lenght);
                mCanvas.drawText(intro,left+coverBitmap.getWidth()+radius+padding,tempY,mTextPaint);
                content = content.substring(lenght);
            }else{
                mCanvas.drawText(content,left+coverBitmap.getWidth()+radius+padding,tempY,mTextPaint);
                break;
            }
        }


        //计算最后Y坐标位置
        startY = startY + bottom ;
    }

    private void drawContent() {
        mTextPaint.setColor(Color.parseColor("#505050"));

        //绘制章节名
        startX = padding;
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextSize(textSize);
        mCanvas.drawText(mChapterName, startX, startY, mTextPaint);

        //绘制章节内容
        mTextPaint.setFakeBoldText(false);
        for (Line line :mLines) {
            startY = startY + textSize+textSpace;
            mCanvas.drawText(line.text, startX, startY, mTextPaint);
        }
        startY = startY + textSize;
    }


    private void drawFooder() {
        startX = padding;
        startY = startY + 2*padding;
        mTextPaint.setColor(Color.parseColor("#CAB69D"));
        mCanvas.drawText("更多内容下载安马文学APP", startX, startY, mTextPaint);

        startY = startY + textSize+padding;
        startX = (mWidth - qrBitmap.getWidth())/2;
        mCanvas.drawBitmap(qrBitmap,startX,startY,mPicturePaint);

        startY = startY + qrBitmap.getHeight();

        mTextPaint.setColor(Color.parseColor("#8B8A8F"));
        mTextPaint.setTextSize(spToPx(mContext,10));

        String str1 = "识别二维码下载「安马文学」";
        Rect rect1 = measureText(str1,mTextPaint);
        startX = (mWidth - rect1.width())/2;
        startY = startY + padding;
        mCanvas.drawText(str1, startX, startY, mTextPaint);

        String str2 = "免费领取7天全场畅读";
        Rect rect2 = measureText(str2,mTextPaint);
        startX = (mWidth - rect2.width())/2;
        startY = startY + rect1.height()*2;
        mCanvas.drawText(str2, startX, startY, mTextPaint);

    }


    private void closeCanvas() {
        mCanvas.save();
        mCanvas.restore();
    }


    private String saveImage() {
        File file = new File(imagePath);// 保存到sdcard根目录下，文件名为share_pic.png
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 30, fos);
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;

    }


    private static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue
     * @return
     */
    public static float spToPx(Context context,float spValue) {
        return spValue * context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static final Rect measureText(String text, Paint paint){
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setTextSize(int textSize) {
        Log.i(TAG, "setTextSize: "+this.textSize +"  new "+textSize);
        this.textSize = textSize;
        this.padding = textSize;
    }
}
