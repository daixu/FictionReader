package com.shangame.fiction.entity;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class NormalMultipleEntity {

    public static final int BANNER = 1;
    public static final int MENU = 2;
    public static final int IMG = 3;
    public static final int RECOMMEND = 4;
    public static final int BOUTIQUE = 5;
    public static final int HIGHLY_RECOMMEND = 6;
    public static final int SERIAL = 7;
    public static final int BOY_GIRL = 8;
    public static final int HOT_SEARCH = 9;
    public static final int COMPLETE = 10;
    public static final int LABEL = 11;
    public static final int OTHER = 12;
    public static final int AD1 = 13;
    public static final int AD2 = 14;

    public int type;
    public String content;
    public List<PictureConfigResponse.PicItem> mBannerData;
    public List<BookInfoEntity> mHeavyData;
    public List<BookInfoEntity> mChoiceData;
    public List<BookInfoEntity> mRecData;
    public List<BookInfoEntity> mHotData;
    public List<BookInfoEntity> mSearchData;
    public List<BookInfoEntity> mCompleteData;
    public List<TTFeedAd> mAds;
    public List<ChoicenessResponse.ClassdataBean> mClassData;
    public List<BookInfoEntity> mOtherData = new ArrayList<>();

    public NormalMultipleEntity(int type) {
        this.type = type;
    }

    public NormalMultipleEntity(int type, String content) {
        this.type = type;
        this.content = content;
    }
}