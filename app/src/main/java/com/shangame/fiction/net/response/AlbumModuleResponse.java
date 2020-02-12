package com.shangame.fiction.net.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class AlbumModuleResponse {
    public List<CarDataBean> cardata;
    public List<DisDataBean> disdata;
    public List<BoyDataBean> boydata;
    public List<GirlDataBean> grildata;
    public List<HotDataBean> hotdata;
    public List<StateDataBean> statedata;

    public static class CarDataBean {
        /**
         * albumid : 10000060
         * albumName : 炎武战神
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000060.jpg
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String author;
    }

    public static class DisDataBean implements MultiItemEntity {
        /**
         * albumid : 10000069
         * albumName : 吞噬苍穹
         * synopsis : 孤儿轩辕因为痛失至亲，转世重生，来到了以武为尊的异界从一个平凡的少年，逐渐成长为九界至尊!
         * purprice : 20
         * disprice : 0
         * classname : null
         * chapternumber : 1142
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000069.jpg
         */

        public int albumid;
        public String albumName;
        public String synopsis;
        public double purprice;
        public double disprice;
        public String classname;
        public int chapternumber;
        public String bookcover;
        public int isvip;
        public int type;

        @Override
        public int getItemType() {
            return type;
        }
    }

    public static class BoyDataBean {
        /**
         * albumid : 10000422
         * albumName : 网游之模拟城市
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000422.jpg
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String author;
    }

    public static class GirlDataBean {
        /**
         * albumid : 10000564
         * albumName : 权力巅峰
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000564.jpg
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String author;
    }

    public static class HotDataBean implements MultiItemEntity {
        /**
         * albumid : 10000073
         * albumName : 炼神领域
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000073.jpg
         * synopsis : 游戏第一人的林沐雨在删号之后下线失败，却离奇穿越到了另一个世界。身为炼器宗师、封号武神的巅峰高手，一入乱世便如狂龙蹈海！
         * classname : 科幻奇幻
         * chapternumber : 870
         * serstatus : 连载中
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String synopsis;
        public String classname;
        public int chapternumber;
        public String serstatus;
        public String author;
        public int type;

        @Override
        public int getItemType() {
            return type;
        }
    }

    public static class StateDataBean {
        /**
         * albumid : 10000285
         * albumName : 都市狂仙（精配）
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000285.jpg
         * synopsis : 都市小说，情节引人入胜，播讲意味深长
         * classname : 科幻奇幻
         * chapternumber : 724
         * serstatus : 已完结
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String synopsis;
        public String classname;
        public int chapternumber;
        public String serstatus;
        public String author;
    }
}
