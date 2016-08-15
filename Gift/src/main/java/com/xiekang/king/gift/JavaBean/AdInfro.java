package com.xiekang.king.gift.JavaBean;

import java.util.List;

/**
 * Created by King on 2016/8/15.
 */
public class AdInfro {

    /**
     * id : 230
     * title : 1688玩四周年活动
     * flag : 1
     * iconurl : /allimgs/img_iad/_1471082588565.jpg
     * addtime : 2016-08-18
     * linkurl : http://www.1688wan.com/szn/
     * giftid : 1470998731
     * appName : 航海王强者之路
     * appLogo : /allimgs/img_iapp/201511/_1448446144213.png
     * appId : 1448446177
     */

    private int id;
    private String title;
    private int flag;
    private String iconurl;
    private String addtime;
    private String linkurl;
    private String giftid;
    private String appName;
    private String appLogo;
    private int appId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public AdInfro(int id, String title, int flag, String iconurl, String addtime, String linkurl, String giftid, String appName, String appLogo, int appId) {
        this.id = id;
        this.title = title;
        this.flag = flag;
        this.iconurl = iconurl;
        this.addtime = addtime;
        this.linkurl = linkurl;
        this.giftid = giftid;
        this.appName = appName;
        this.appLogo = appLogo;
        this.appId = appId;
    }
}
