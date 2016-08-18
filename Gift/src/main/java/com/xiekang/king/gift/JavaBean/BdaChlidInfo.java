package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/17.
 *
 */
public class BdaChlidInfo {

    /**
     * id : 1203
     * cid : 112
     * appid : 1458787671
     * appname : 被错过的天堂
     * appicon : /allimgs/img_iapp/201603/_1458787584145.jpg
     * typename : 冒险解密
     * appdesc :
     * appvtype : 1,2
     */


    private String appid;
    private String appname;
    private String appicon;

    public BdaChlidInfo(String appid, String appname, String appicon) {
        this.appid = appid;
        this.appname = appname;
        this.appicon = appicon;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppname() {
        return appname;
    }

    public String getAppicon() {
        return appicon;
    }
}
