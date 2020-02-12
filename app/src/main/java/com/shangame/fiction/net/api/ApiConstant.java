package com.shangame.fiction.net.api;

import com.shangame.fiction.BuildConfig;

/**
 * Create by Speedy on 2018/8/6
 */
public class ApiConstant {

    /**
     * 注册渠道
     */
    public static final class Channel{
        public static final int WEB = 0;
        public static final int ANDROID = BuildConfig.CHANNEL_ID;
        public static final int IOS = 2001;
    }

    public static final class Platform{
        public static final int WAP = 1;
        public static final int PC = 2;
        public static final int APP = 3;
    }

    /**
     * 注册平台
     */
    public static final class RegisterPlatform{
        public static final int PHONE = 0;
        public static final int WECHAT = 1;
        public static final int QQ = 2;
    }

    /**
     * 事件来源
     */
    public static final class ClickType{
        public static final int FROM_CLICK = 101;//点击,
        public static final int FROM_COOLECT= 102;//收藏，
        public static final int FROM_SEARCH =  103;//搜索
    }

    public static final class ChapterAuth{
        public static final int FREEE_READ = 0;//点击,
        public static final int VIP_READ = 1;//点击,
    }

    public static final class PayStatus{
        public static final int NOT_PAID = 0;//点击,
        public static final int PAID = 1;//点击,
    }

    public static final class PayMethod{
        //110公众号支付， 111官方微信SDK,112 苹果支付，113贝支付（wap微信），114贝支付（wap支付宝）
        public static final int VIP_CN = 110;
        public static final int WECHAT_SDK =  111;
        public static final int APPLE = 112;
        public static final int BEI_WAP_WECHAT = 113;
        public static final int BEI_WAP_ALIPAY = 114;
    }



}
