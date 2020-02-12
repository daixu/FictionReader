package com.shangame.fiction.ui.wifi.nanohttpd;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.shangame.fiction.AppContext;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.model.LocalBookBean;
import com.shangame.fiction.ui.wifi.WifiBean;

import org.nanohttpd.protocols.http.tempfiles.DefaultTempFileManager;
import org.nanohttpd.protocols.http.tempfiles.ITempFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadFileManager extends DefaultTempFileManager {
    private final File dir;
    private final List<ITempFile> files;
    public static final String DIR_IN_SDCARD = "wifiBook";

    public UploadFileManager() {
        this.dir = new File(Environment.getExternalStorageDirectory() + File.separator + DIR_IN_SDCARD);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.files = new ArrayList<>();
    }

    @Override
    public void clear() {
        super.clear();
        this.files.clear();
    }

    @Override
    public ITempFile createTempFile(String fileNameHint) throws Exception {
        if (!TextUtils.isEmpty(fileNameHint)) {
            UploadFile uploadFile = new UploadFile(this.dir, fileNameHint);
            File file = new File(this.dir, fileNameHint);
            addBook(file);
            this.files.add(uploadFile);
            return uploadFile;
        }
        return super.createTempFile(fileNameHint);
    }

    private void addBook(File file) {
        WifiBean.getInstance().fileTotal += 1;
        LocalBookBean booksBean = convertBooksBean(file);
        LocalBookBeanDao dao = DbManager.getDaoSession(AppContext.getContext()).getLocalBookBeanDao();
        dao.insertOrReplaceInTx(booksBean);

        Intent intent = new Intent(BroadcastAction.UPLOAD_WIFI_BOOK);
        String bookName = file.getName().replace(".txt", "");
        intent.putExtra("bookName", bookName);
        LocalBroadcastManager.getInstance(AppContext.getContext()).sendBroadcast(intent);
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
}
