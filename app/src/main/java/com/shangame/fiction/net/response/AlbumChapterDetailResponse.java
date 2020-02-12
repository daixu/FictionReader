package com.shangame.fiction.net.response;

import java.math.BigDecimal;

public class AlbumChapterDetailResponse {
    /**
     * lastcid : 0
     * nextcid : 1002
     * buystatus : 0
     * albumName : 炎武战神
     * showCover : http://imgcache.qq.com/fm/photo/album/rmid_album_360/j/d/000pIBJy3kXijd.jpg?time=1505993712
     * showName : 第1集
     * bookshelves : 0
     * readmoney : 0
     * chargingmode : 0
     * chapterprice : 20
     * isvip : 0
     * play_url : {"high":"http://ws.stream.fm.qq.com/R196003lpmoJ3r1Tpi.m4a?fromtag=36&guid=0&vkey=82F7A28FC45D6D8E9F58EF89EBE56A72579A4E14B9ABAAFEB516E9E5DC2189E88C72DD8136D144DD8223EBA8BE42F8218212D9826D3596C1","medium":"http://ws.stream.fm.qq.com/R148003lpmoJ3r1Tpi.m4a?fromtag=36&guid=0&vkey=82F7A28FC45D6D8E9F58EF89EBE56A72579A4E14B9ABAAFEB516E9E5DC2189E88C72DD8136D144DD8223EBA8BE42F8218212D9826D3596C1","small":"http://ws.stream.fm.qq.com/R148003lpmoJ3r1Tpi.m4a?fromtag=36&guid=0&vkey=82F7A28FC45D6D8E9F58EF89EBE56A72579A4E14B9ABAAFEB516E9E5DC2189E88C72DD8136D144DD8223EBA8BE42F8218212D9826D3596C1"}
     */

    public int lastcid;
    public int nextcid;
    public int buystatus;
    public String albumName;
    public String showCover;
    public String showName;
    public int bookshelves;
    public BigDecimal readmoney;
    public int chargingmode;
    public BigDecimal chapterprice;
    public int isvip;
    public int autorenew;
    public int duration;
    public int advertState;
    public PlayUrlBean play_url;
    public int sort;

    public static class PlayUrlBean {
        /**
         * high : http://ws.stream.fm.qq.com/R196003lpmoJ3r1Tpi.m4a?fromtag=36&guid=0&vkey=82F7A28FC45D6D8E9F58EF89EBE56A72579A4E14B9ABAAFEB516E9E5DC2189E88C72DD8136D144DD8223EBA8BE42F8218212D9826D3596C1
         * medium : http://ws.stream.fm.qq.com/R148003lpmoJ3r1Tpi.m4a?fromtag=36&guid=0&vkey=82F7A28FC45D6D8E9F58EF89EBE56A72579A4E14B9ABAAFEB516E9E5DC2189E88C72DD8136D144DD8223EBA8BE42F8218212D9826D3596C1
         * small : http://ws.stream.fm.qq.com/R148003lpmoJ3r1Tpi.m4a?fromtag=36&guid=0&vkey=82F7A28FC45D6D8E9F58EF89EBE56A72579A4E14B9ABAAFEB516E9E5DC2189E88C72DD8136D144DD8223EBA8BE42F8218212D9826D3596C1
         */

        public String high;
        public String medium;
        public String small;
    }
}
