package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2018/8/13
 */
public class FriendReadResponse {

    /** 猜你喜欢 */
    public List<BookInfoEntity> pagedata = new ArrayList<>();
}
