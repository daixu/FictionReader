package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/7/27
 */
public class BookDataPageResponse {
    /**
     * records : 1
     * total : 2
     * pagedata : [{"bookid":1,"bookname":"sample string 2","author":"sample string 3","verifyname":"sample string 4","classname":"sample string 5","bookcover":"sample string 6","clicknumber":7,"collectnumber":8,"wordnumber":9,"lastmodifytimes":"sample string 10"},{"bookid":1,"bookname":"sample string 2","author":"sample string 3","verifyname":"sample string 4","classname":"sample string 5","bookcover":"sample string 6","clicknumber":7,"collectnumber":8,"wordnumber":9,"lastmodifytimes":"sample string 10"}]
     */

    public int records;
    public int total;
    public List<PageDataBean> pagedata;
}
