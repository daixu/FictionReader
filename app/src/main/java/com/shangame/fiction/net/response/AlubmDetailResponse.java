package com.shangame.fiction.net.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class AlubmDetailResponse {
    /**
     * detailsdata : {"albumid":10000571,"albumName":"花豹突击队","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000571.jpg","synopsis":"世代隐居山林的武术世家子弟万林机缘巧合地参加了军队特战部队，并以他们为核心组建了花豹突击队。主人公一人一兽绝世的武功和奇异的兽能，统领山间猛兽解救战友于危难，与一切邪恶势力展开了一系列殊死搏斗。","classname":"历史军事","chapternumber":1580,"serstatus":"连载中","chapterid":2761,"allbumshelves":0,"author":"paul"}
     * likedata : [{"albumid":10000572,"albumName":"带着仓库到大明","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000572.jpg","synopsis":"打工仔带着仓库来到大明，过上了要啥有啥的幸福生活。","classname":"历史军事","chapternumber":600,"serstatus":"已完结","author":"冰城大鹏"},{"albumid":10000573,"albumName":"仗剑权臣","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000573.jpg","synopsis":"江湖，朝堂，男儿大风歌！","classname":"历史军事","chapternumber":394,"serstatus":"连载中","author":"叨马澹VV"},{"albumid":10000574,"albumName":"寒门崛起（雪莉演播）","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000574.jpg","synopsis":"演播：汐颜雪莉\\n? 声音：女生偏中性；演播水平：一般；特色：勤奋努力的录书；白穷美一枚。\\n? Q群：241371179\\n? 新浪：汐颜_雪莉\\n \\n? 自我安慰：你只要比昨天的自己更好就行了，跟别人攀比是人生的悲剧；再说了，这世间人这么多，跟别人比，哪里比得过来。\\n作者：朱郎才尽\\n?作品：《寒门崛起》；2016年累计收藏量过十万；。\\n?内容简介：\\n?这是一个就业路上屡被蹂躏的古汉语专业研究生，回到了明朝中叶，进入了山村一家幼童身体后的故事。\\n木讷父亲泼辣娘，一水的极品亲戚，农家","classname":"历史军事","chapternumber":624,"serstatus":"已完结","author":"汐颜雪莉"},{"albumid":10000575,"albumName":"开艘航母去抗日","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000575.jpg","synopsis":"中国下一代航空母舰北京号，在一次执行任务途中，回到了甲午战争时期，俘虏日本舰队，全歼入侵朝鲜的日军，轰炸东京，抗击八国联军，强占沙俄领土\u2026\u2026舰上的将士，用来自未来的尖端的科学技术，改变着一个民族的命运\u2026\u2026 正当中华民族蒸蒸日上的时候，舰上将士没有想到，他们的穿越，促进了西方列强的飞速发展，突然有一天，世界大战骤然来临，而他们，不得不以一艘航空母舰，对抗整个西方世界的入侵\u2026\u2026","classname":"历史军事","chapternumber":700,"serstatus":"已完结","author":"有声故事汇"}]
     * clickbook : [{"albumid":10000002,"albumName":"黄河古事","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000002.jpg","author":"星魂故事"},{"albumid":10000060,"albumName":"炎武战神","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000060.jpg","author":"慕诚"},{"albumid":10000138,"albumName":"灭天剑神","bookcover":"https://opsys.anmaa.com/uploadimg/Albumcover/10000138.jpg","author":"可以这样斗斗龙"}]
     */

    public DetailsDataBean detailsdata;
    public List<LikeDataBean> likedata;
    public List<ClickBookBean> clickbook;

    public static class DetailsDataBean {
        /**
         * albumid : 10000571
         * albumName : 花豹突击队
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000571.jpg
         * synopsis : 世代隐居山林的武术世家子弟万林机缘巧合地参加了军队特战部队，并以他们为核心组建了花豹突击队。主人公一人一兽绝世的武功和奇异的兽能，统领山间猛兽解救战友于危难，与一切邪恶势力展开了一系列殊死搏斗。
         * classname : 历史军事
         * chapternumber : 1580
         * serstatus : 连载中
         * chapterid : 2761
         * allbumshelves : 0
         * author : paul
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String synopsis;
        public String classname;
        public int chapternumber;
        public String serstatus;
        public int chapterid;
        public int allbumshelves;
        public int chargingmode;
        public String author;
    }

    public static class LikeDataBean implements MultiItemEntity {
        /**
         * albumid : 10000572
         * albumName : 带着仓库到大明
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000572.jpg
         * synopsis : 打工仔带着仓库来到大明，过上了要啥有啥的幸福生活。
         * classname : 历史军事
         * chapternumber : 600
         * serstatus : 已完结
         * author : 冰城大鹏
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

    public static class ClickBookBean {
        /**
         * albumid : 10000002
         * albumName : 黄河古事
         * bookcover : https://opsys.anmaa.com/uploadimg/Albumcover/10000002.jpg
         * author : 星魂故事
         */

        public int albumid;
        public String albumName;
        public String bookcover;
        public String author;
    }
}
