package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/16.
 */
public class HotListInfo {


    /**
     * id : 8
     * appid : 1443409159
     * type : 1
     * clicks : 13653
     * flag : 1
     * platform : 0
     * name : 穿越火线：枪战王者
     * typename : 射击空战
     * logo : /allimgs/img_iapp/201608/_1470385078583.jpg
     * size : 255M
     * addtime : 2016-08-09 12:37:27.0
     */


    private String appid;
    private String clicks;
    private String name;
    private String typename;
    private String logo;
    private String size;

    public HotListInfo(String appid, String clicks, String name, String typename, String logo, String size) {
        this.appid = appid;
        this.clicks = clicks;
        this.name = name;
        this.typename = typename;
        this.logo = logo;
        this.size = size;
    }

    public String getAppid() {
        return appid;
    }

    public String getClicks() {
        return clicks;
    }

    public String getName() {
        return name;
    }

    public String getTypename() {
        return typename;
    }

    public String getLogo() {
        return logo;
    }

    public String getSize() {
        return size;
    }
}
