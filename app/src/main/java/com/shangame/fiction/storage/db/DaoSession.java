package com.shangame.fiction.storage.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.shangame.fiction.net.response.LocalBookContentBean;
import com.shangame.fiction.net.response.LocalChapterListBean;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookMark;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.storage.model.BooksBean;
import com.shangame.fiction.storage.model.ChapterInfo;
import com.shangame.fiction.storage.model.LocalBookBean;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.reader.local.bean.BookChapterBean;
import com.shangame.fiction.ui.reader.local.bean.BookRecordBean;
import com.shangame.fiction.ui.reader.local.bean.CollBookBean;

import com.shangame.fiction.storage.db.LocalBookContentBeanDao;
import com.shangame.fiction.storage.db.LocalChapterListBeanDao;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.db.BookMarkDao;
import com.shangame.fiction.storage.db.BookParagraphDao;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.db.BooksBeanDao;
import com.shangame.fiction.storage.db.ChapterInfoDao;
import com.shangame.fiction.storage.db.LocalBookBeanDao;
import com.shangame.fiction.storage.db.UserInfoDao;
import com.shangame.fiction.storage.db.BookChapterBeanDao;
import com.shangame.fiction.storage.db.BookRecordBeanDao;
import com.shangame.fiction.storage.db.CollBookBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig localBookContentBeanDaoConfig;
    private final DaoConfig localChapterListBeanDaoConfig;
    private final DaoConfig bookBrowseHistoryDaoConfig;
    private final DaoConfig bookMarkDaoConfig;
    private final DaoConfig bookParagraphDaoConfig;
    private final DaoConfig bookReadProgressDaoConfig;
    private final DaoConfig booksBeanDaoConfig;
    private final DaoConfig chapterInfoDaoConfig;
    private final DaoConfig localBookBeanDaoConfig;
    private final DaoConfig userInfoDaoConfig;
    private final DaoConfig bookChapterBeanDaoConfig;
    private final DaoConfig bookRecordBeanDaoConfig;
    private final DaoConfig collBookBeanDaoConfig;

    private final LocalBookContentBeanDao localBookContentBeanDao;
    private final LocalChapterListBeanDao localChapterListBeanDao;
    private final BookBrowseHistoryDao bookBrowseHistoryDao;
    private final BookMarkDao bookMarkDao;
    private final BookParagraphDao bookParagraphDao;
    private final BookReadProgressDao bookReadProgressDao;
    private final BooksBeanDao booksBeanDao;
    private final ChapterInfoDao chapterInfoDao;
    private final LocalBookBeanDao localBookBeanDao;
    private final UserInfoDao userInfoDao;
    private final BookChapterBeanDao bookChapterBeanDao;
    private final BookRecordBeanDao bookRecordBeanDao;
    private final CollBookBeanDao collBookBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        localBookContentBeanDaoConfig = daoConfigMap.get(LocalBookContentBeanDao.class).clone();
        localBookContentBeanDaoConfig.initIdentityScope(type);

        localChapterListBeanDaoConfig = daoConfigMap.get(LocalChapterListBeanDao.class).clone();
        localChapterListBeanDaoConfig.initIdentityScope(type);

        bookBrowseHistoryDaoConfig = daoConfigMap.get(BookBrowseHistoryDao.class).clone();
        bookBrowseHistoryDaoConfig.initIdentityScope(type);

        bookMarkDaoConfig = daoConfigMap.get(BookMarkDao.class).clone();
        bookMarkDaoConfig.initIdentityScope(type);

        bookParagraphDaoConfig = daoConfigMap.get(BookParagraphDao.class).clone();
        bookParagraphDaoConfig.initIdentityScope(type);

        bookReadProgressDaoConfig = daoConfigMap.get(BookReadProgressDao.class).clone();
        bookReadProgressDaoConfig.initIdentityScope(type);

        booksBeanDaoConfig = daoConfigMap.get(BooksBeanDao.class).clone();
        booksBeanDaoConfig.initIdentityScope(type);

        chapterInfoDaoConfig = daoConfigMap.get(ChapterInfoDao.class).clone();
        chapterInfoDaoConfig.initIdentityScope(type);

        localBookBeanDaoConfig = daoConfigMap.get(LocalBookBeanDao.class).clone();
        localBookBeanDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        bookChapterBeanDaoConfig = daoConfigMap.get(BookChapterBeanDao.class).clone();
        bookChapterBeanDaoConfig.initIdentityScope(type);

        bookRecordBeanDaoConfig = daoConfigMap.get(BookRecordBeanDao.class).clone();
        bookRecordBeanDaoConfig.initIdentityScope(type);

        collBookBeanDaoConfig = daoConfigMap.get(CollBookBeanDao.class).clone();
        collBookBeanDaoConfig.initIdentityScope(type);

        localBookContentBeanDao = new LocalBookContentBeanDao(localBookContentBeanDaoConfig, this);
        localChapterListBeanDao = new LocalChapterListBeanDao(localChapterListBeanDaoConfig, this);
        bookBrowseHistoryDao = new BookBrowseHistoryDao(bookBrowseHistoryDaoConfig, this);
        bookMarkDao = new BookMarkDao(bookMarkDaoConfig, this);
        bookParagraphDao = new BookParagraphDao(bookParagraphDaoConfig, this);
        bookReadProgressDao = new BookReadProgressDao(bookReadProgressDaoConfig, this);
        booksBeanDao = new BooksBeanDao(booksBeanDaoConfig, this);
        chapterInfoDao = new ChapterInfoDao(chapterInfoDaoConfig, this);
        localBookBeanDao = new LocalBookBeanDao(localBookBeanDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        bookChapterBeanDao = new BookChapterBeanDao(bookChapterBeanDaoConfig, this);
        bookRecordBeanDao = new BookRecordBeanDao(bookRecordBeanDaoConfig, this);
        collBookBeanDao = new CollBookBeanDao(collBookBeanDaoConfig, this);

        registerDao(LocalBookContentBean.class, localBookContentBeanDao);
        registerDao(LocalChapterListBean.class, localChapterListBeanDao);
        registerDao(BookBrowseHistory.class, bookBrowseHistoryDao);
        registerDao(BookMark.class, bookMarkDao);
        registerDao(BookParagraph.class, bookParagraphDao);
        registerDao(BookReadProgress.class, bookReadProgressDao);
        registerDao(BooksBean.class, booksBeanDao);
        registerDao(ChapterInfo.class, chapterInfoDao);
        registerDao(LocalBookBean.class, localBookBeanDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(BookChapterBean.class, bookChapterBeanDao);
        registerDao(BookRecordBean.class, bookRecordBeanDao);
        registerDao(CollBookBean.class, collBookBeanDao);
    }
    
    public void clear() {
        localBookContentBeanDaoConfig.clearIdentityScope();
        localChapterListBeanDaoConfig.clearIdentityScope();
        bookBrowseHistoryDaoConfig.clearIdentityScope();
        bookMarkDaoConfig.clearIdentityScope();
        bookParagraphDaoConfig.clearIdentityScope();
        bookReadProgressDaoConfig.clearIdentityScope();
        booksBeanDaoConfig.clearIdentityScope();
        chapterInfoDaoConfig.clearIdentityScope();
        localBookBeanDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        bookChapterBeanDaoConfig.clearIdentityScope();
        bookRecordBeanDaoConfig.clearIdentityScope();
        collBookBeanDaoConfig.clearIdentityScope();
    }

    public LocalBookContentBeanDao getLocalBookContentBeanDao() {
        return localBookContentBeanDao;
    }

    public LocalChapterListBeanDao getLocalChapterListBeanDao() {
        return localChapterListBeanDao;
    }

    public BookBrowseHistoryDao getBookBrowseHistoryDao() {
        return bookBrowseHistoryDao;
    }

    public BookMarkDao getBookMarkDao() {
        return bookMarkDao;
    }

    public BookParagraphDao getBookParagraphDao() {
        return bookParagraphDao;
    }

    public BookReadProgressDao getBookReadProgressDao() {
        return bookReadProgressDao;
    }

    public BooksBeanDao getBooksBeanDao() {
        return booksBeanDao;
    }

    public ChapterInfoDao getChapterInfoDao() {
        return chapterInfoDao;
    }

    public LocalBookBeanDao getLocalBookBeanDao() {
        return localBookBeanDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public BookChapterBeanDao getBookChapterBeanDao() {
        return bookChapterBeanDao;
    }

    public BookRecordBeanDao getBookRecordBeanDao() {
        return bookRecordBeanDao;
    }

    public CollBookBeanDao getCollBookBeanDao() {
        return collBookBeanDao;
    }

}
