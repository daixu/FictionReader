package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.storage.model.NewBookInforankingEntity;

import java.util.List;

/**
 * Create by Speedy on 2018/8/13
 */
public class MaleChannelResponse {

    public List<BookInfoEntity> choicedata;
    public List<BookInfoEntity> recdata;
    public List<BookInfoEntity> searchdata;
    public List<BookInfoEntity> completedata;
    public List<BookInfoEntity> class1data;
    public List<BookInfoEntity> class2data;
    public List<NewBookInforankingEntity> subdata;
    public List<NewBookInforankingEntity> clickdata;
    public List<NewBookInforankingEntity> collectdata;
}
