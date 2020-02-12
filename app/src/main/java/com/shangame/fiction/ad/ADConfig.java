package com.shangame.fiction.ad;

/**
 * Create by Speedy on 2019/5/7
 */
public class ADConfig {

    public static String APPID = "1108863793";
    public static String CSJAPPID = "5021459";

    public static int AD_INTERVAL_PAGE = 3;// 广告间隔页数

    public static boolean removeAD = false;//免广告，默认显示广告

    public interface ADPositionID {
        String SPLASH_ID = "3010763192799849";
        String BOOK_STORE_GIRL_ID1 = "9080860426993254";
        String BOOK_STORE_GIRL_ID2 = "2030961153305053";
        String BOOK_STORE_BOY_ID1 = "5040464436899285";
        String BOOK_STORE_BOY_ID2 = "7060667466695216";
        String BOOK_STORE_CHOICENESS_ID1 = "3080963416191231";
        String BOOK_STORE_CHOICENESS_ID2 = "2040265466697253";

        String SEARCH_ID = "5040163102392919";
        String READ_PAGE_ID = "4090162143205007";
        String READ_BOTTOM_ID = "3010361173503069";
        String READ_DETAIL_ID = "5050567596044248";
        String TASK_CENTER_ID = "7040369183701065";
    }

    public interface CSJAdPositionId {
        String SPLASH_ID = "821459244";
        String BOOK_STORE_CHOICENESS_ID1 = "921459019";
        String BOOK_STORE_CHOICENESS_ID2 = "921459639";
        String BOOK_STORE_GIRL_ID1 = "921459288";
        String BOOK_STORE_GIRL_ID2= "921459766";
        String BOOK_STORE_BOY_ID1= "921459090";
        String BOOK_STORE_BOY_ID2= "921459458";

        String BOOK_RACK_LIST_ID1 = "921459559";
        String READ_PAGE_ID = "921459759";
        String READ_PAGE_BOTTOM_ID = "921459568";
        String READ_DETAIL_ID = "921459049";
        String TASK_CENTER_ID= "921459485";
    }
}
