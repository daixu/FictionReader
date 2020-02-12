package com.shangame.fiction.ui.wifi;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.core.constant.Constant;
import com.shangame.fiction.core.utils.FileUtils;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.core.utils.StringUtils;
import com.shangame.fiction.net.response.Recommend;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.model.LocalBookBean;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileSystemAdapter extends BaseQuickAdapter<Recommend, BaseViewHolder> {
    private List<Recommend> mList;

    public FileSystemAdapter(int layoutResId, @Nullable List<Recommend> data) {
        super(layoutResId, data);
        mList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, Recommend item) {
        ImageView imageIcon = helper.getView(R.id.image_icon);
        File file = item.file;
        helper.setText(R.id.tv_name, file.getName());

        ImageView imageCheck = helper.getView(R.id.image_check);
        LinearLayout layoutBrief = helper.getView(R.id.layout_brief);
        TextView tvSubCount = helper.getView(R.id.tv_sub_count);

        if (file.isDirectory()) {
            imageCheck.setVisibility(View.GONE);
            imageIcon.setImageResource(R.drawable.icon_dir);
            layoutBrief.setVisibility(View.GONE);
            tvSubCount.setVisibility(View.VISIBLE);

            String[] fileList = file.list();
            if (null != fileList) {
                tvSubCount.setText(mContext.getString(R.string.file_sub_count, file.list().length));
            } else {
                tvSubCount.setText(mContext.getString(R.string.file_sub_count, 0));
            }
        } else {
            imageIcon.setImageResource(R.drawable.icon_txt);
            imageCheck.setVisibility(View.VISIBLE);

            String path = file.getAbsolutePath();
            if (isFileLoaded(path)) {
                imageCheck.setImageResource(R.drawable.ic_file_loaded);
            } else {
                if (item.isClick) {
                    imageCheck.setImageResource(R.drawable.pay_checked);
                } else {
                    imageCheck.setImageResource(R.drawable.pay_uncheck);
                }
            }
            layoutBrief.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_size, FileUtils.getFileSize(file.length()));
            helper.setText(R.id.tv_date, StringUtils.dateConvert(file.lastModified(), Constant.FORMAT_FILE_DATE));

            tvSubCount.setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.image_check);
    }

    private LocalBookBean getCollBook(String path) {
        path = MD5Utils.strToMd5By16(path);
        LocalBookBeanDao dao = DbManager.getDaoSession(mContext.getApplicationContext()).getLocalBookBeanDao();
        LocalBookBean bean = dao.queryBuilder().where(LocalBookBeanDao.Properties.StrId.eq(path)).unique();
        return bean;
    }

    public List<Recommend> getItems() {
        return Collections.unmodifiableList(mList);
    }

    private boolean isFileLoaded(String path) {
        if (getCollBook(path) != null) {
            return true;
        }
        return false;
    }
}
