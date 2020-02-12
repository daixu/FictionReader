package com.shangame.fiction.core.constant;

import com.shangame.fiction.core.utils.FileUtils;

import java.io.File;

/**
 * Create by Speedy on 2019/8/7
 */
public interface Constant {

    String FORMAT_FILE_DATE = "yyyy-MM-dd";

    String SUFFIX_TXT = ".txt";
    String SUFFIX_PDF = ".pdf";
    String SUFFIX_EPUB = ".epub";
    String SUFFIX_ZIP = ".zip";
    String SUFFIX_CHM = ".chm";

    //Book Date Convert Format
    String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    String FORMAT_TIME = "HH:mm";

    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    String BOOK_CACHE_PATH = FileUtils.getCachePath() + File.separator
            + "book_cache" + File.separator;
}
