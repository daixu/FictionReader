package com.shangame.fiction.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 用户
 * <p>
 * Create by Speedy on 2018/7/17
 */
@Entity
public class UserInfo {

    @Id
    public long userid;         //用户ID

    public String account;      //用户账号
    public String nickname;     //用户昵称
    public long money;        //闪闪币
    public String headimgurl;   //头像Url地址
    public String mobilephone;  //手机号码
    public int sex;             //性别 0，男，1女
    public int channel;         //渠道

    public String birthday;
    public String synopsis;
    public String province;
    public String city;
    public int coin;
    public int newvip;
    public int regtype;
    public double cashmoney;
    public int receive;
    public long readTime;

    @Transient
    public int advertopen;
    public int authorid;
    public int agentId;
    public int agentGrade;
    public int sysAgentGrade;

    @Generated(hash = 1228023823)
    public UserInfo(long userid, String account, String nickname, long money,
            String headimgurl, String mobilephone, int sex, int channel, String birthday,
            String synopsis, String province, String city, int coin, int newvip, int regtype,
            double cashmoney, int receive, long readTime, int authorid, int agentId,
            int agentGrade, int sysAgentGrade) {
        this.userid = userid;
        this.account = account;
        this.nickname = nickname;
        this.money = money;
        this.headimgurl = headimgurl;
        this.mobilephone = mobilephone;
        this.sex = sex;
        this.channel = channel;
        this.birthday = birthday;
        this.synopsis = synopsis;
        this.province = province;
        this.city = city;
        this.coin = coin;
        this.newvip = newvip;
        this.regtype = regtype;
        this.cashmoney = cashmoney;
        this.receive = receive;
        this.readTime = readTime;
        this.authorid = authorid;
        this.agentId = agentId;
        this.agentGrade = agentGrade;
        this.sysAgentGrade = sysAgentGrade;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public long getUserid() {
        return this.userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getMoney() {
        return this.money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getHeadimgurl() {
        return this.headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getMobilephone() {
        return this.mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getChannel() {
        return this.channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCoin() {
        return this.coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getNewvip() {
        return this.newvip;
    }

    public void setNewvip(int newvip) {
        this.newvip = newvip;
    }

    public int getRegtype() {
        return this.regtype;
    }

    public void setRegtype(int regtype) {
        this.regtype = regtype;
    }

    public double getCashmoney() {
        return this.cashmoney;
    }

    public void setCashmoney(double cashmoney) {
        this.cashmoney = cashmoney;
    }

    public int getReceive() {
        return this.receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public long getReadTime() {
        return this.readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public int getAuthorid() {
        return this.authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public int getAgentId() {
        return this.agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getAgentGrade() {
        return this.agentGrade;
    }

    public void setAgentGrade(int agentGrade) {
        this.agentGrade = agentGrade;
    }

    public int getSysAgentGrade() {
        return this.sysAgentGrade;
    }

    public void setSysAgentGrade(int sysAgentGrade) {
        this.sysAgentGrade = sysAgentGrade;
    }
}
