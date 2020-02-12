package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.net.response.ShareResponse;
import com.shangame.fiction.storage.db.BookParagraphDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.model.BookParagraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Create by Speedy on 2019/1/10
 */
public class ShareReadImagePreviewPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private View.OnClickListener onShareClickListener;
    private View.OnClickListener onDownloadClickListener;

    private View previewLayout;
    private ImageView ivCover;
    private TextView tvBookName;
    private TextView tvAuthorName;
    private TextView tvIntro;
    private TextView tvChapterName;
    private TextView tvContent;

    private String imagePath;

    public ShareReadImagePreviewPopupWindow(Activity activity) {
        super(activity);
        initView();
        imagePath = FileManager.getInstance(mActivity).getCacheDir() + File.separator + "share.jpeg";
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private void initView() {
        View contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_share_image_preview, null);
        previewLayout = contentView.findViewById(R.id.previewLayout);
        ivCover = contentView.findViewById(R.id.ivCover);
        tvBookName = contentView.findViewById(R.id.tvBookName);
        tvAuthorName = contentView.findViewById(R.id.tvAuthorName);
        tvIntro = contentView.findViewById(R.id.tvIntro);
        tvChapterName = contentView.findViewById(R.id.tvChapterName);
        tvContent = contentView.findViewById(R.id.tvContent);

        contentView.findViewById(R.id.tvShare).setOnClickListener(this);
        contentView.findViewById(R.id.tvDownload).setOnClickListener(this);
        contentView.findViewById(R.id.tvCancel).setOnClickListener(this);
        setContentView(contentView);
    }

    public void setShareResponse(ShareResponse shareResponse) {
        ImageLoader.with(mActivity).loadCover(ivCover, shareResponse.imgurl);
        tvBookName.setText(shareResponse.titile);
        String strAuthor = "作者:" + shareResponse.author;
        tvAuthorName.setText(strAuthor);
        String strContent = "简介:" + shareResponse.content;
        tvIntro.setText(strContent);
    }

    public void setChapterInfo(String chapterName, long bookid, long chapterId) {
        tvChapterName.setText(chapterName);

        BookParagraphDao bookParagraphDao = DbManager.getDaoSession(mActivity).getBookParagraphDao();
        List<BookParagraph> bookParagraphList = bookParagraphDao.queryBuilder()
                .where(BookParagraphDao.Properties.BookId.eq(bookid), BookParagraphDao.Properties.ChapterId.eq(chapterId))
                .orderAsc(BookParagraphDao.Properties.Pid)
                .list();

        StringBuffer sb = new StringBuffer();
        for (BookParagraph bookParagraph : bookParagraphList) {
            sb.append("\u3000\u3000")//添加缩进
                    .append(bookParagraph.paragraph)
                    .append("<br/>");//添加换行
        }
        tvContent.setText(Html.fromHtml(sb.toString()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvShare:
                if (onShareClickListener != null) {
                    downloadImage(onShareClickListener);
                }
                break;
            case R.id.tvDownload:
                if (onDownloadClickListener != null) {
                    downloadImage(onDownloadClickListener);
                }
                break;
            case R.id.tvCancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void downloadImage(final View.OnClickListener onClickListener) {
        String msg = "正在生成图片……";
        final ProgressDialog mProgressDialog = ProgressDialog.show(mActivity, null, msg, true, false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Horizontal);
        mProgressDialog.setCancelable(false);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(new File(imagePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapUtils.loadBitmapFromView(previewLayout);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                bitmap.recycle();
                return null;
            }

            @Override
            protected void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mProgressDialog.dismiss();
                onClickListener.onClick(null);
            }
        }.execute();
    }

    public void setOnDownloadClickListener(View.OnClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void setOnShareClickListener(View.OnClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public String getImagePath() {
        return imagePath;
    }
}
