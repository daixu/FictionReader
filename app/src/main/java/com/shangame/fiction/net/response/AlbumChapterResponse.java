package com.shangame.fiction.net.response;

import java.util.List;

public class AlbumChapterResponse {
    /**
     * records : 1760
     * total : 176
     * pagedata : [{"cid":1001,"title":"第1集","chargingmode":0,"duration":1072},{"cid":1002,"title":"第2集","chargingmode":0,"duration":988},{"cid":1003,"title":"第3集","chargingmode":0,"duration":956},{"cid":1004,"title":"第4集","chargingmode":0,"duration":950},{"cid":1005,"title":"第5集","chargingmode":0,"duration":883},{"cid":1006,"title":"第6集","chargingmode":0,"duration":1074},{"cid":1007,"title":"第7集","chargingmode":0,"duration":922},{"cid":1008,"title":"第8集","chargingmode":0,"duration":871},{"cid":1009,"title":"第9集","chargingmode":0,"duration":885},{"cid":1010,"title":"第10集","chargingmode":0,"duration":941}]
     */

    public int records;
    public int total;
    public List<PageDataBean> pagedata;

    public static class PageDataBean {
        /**
         * cid : 1001
         * title : 第1集
         * chargingmode : 0
         * duration : 1072
         */

        public int cid;
        public String title;
        public int chargingmode;
        public int duration;
    }
}
