package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/16.
 */
public class HotGridInfo {


    /**
     * id : 28
     * appid : 1451971043
     * type : 0
     * clicks : 2598
     * flag : 1
     * platform : 0
     * name : 部落冲突:皇室战争
     * typename : 卡牌游戏
     * logo : /allimgs/img_iapp/201601/_1451970639500.jpg
     * size : 98.1M
     * addtime : 2016-04-03 17:40:23.0
     */

    private String appid;
    private String name;
    private String logo;

    public HotGridInfo(String appid, String name, String logo) {
        this.appid = appid;
        this.name = name;
        this.logo = logo;
    }

    public String getAppid() {
        return appid;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }


}
