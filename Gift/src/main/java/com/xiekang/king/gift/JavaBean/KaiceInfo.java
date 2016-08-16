package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/16.
 */
public class KaiceInfo {

    /**
     * id : 1470968364
     * iconurl : /allimgs/img_iapp/201607/_1467783388067.png
     * gname : 数码宝贝大冒险
     * testtype : 2
     * score : 5
     * linkurl : 10:00
     * istop : 0
     * colors : 0
     * platform : 1
     * operators : 未知
     * state : 1
     * addtime : 08-16 10:00
     * teststarttime : 2016-08-16 10:00:00
     * gift : 角色扮演
     * keyword :
     * uid : 402881d254be7c330154e57e676a0926
     * gid : 1467783450
     * indexpy : 0
     * isdel : 2
     * openflag : 1
     * openflagname : 内测
     * vtypeimage : <i class='android'></i>
     */
    private String id;
    private String iconurl;
    private String gname;
    private String operators;
    private String addtime;

    public String getId() {
        return id;
    }


    public String getIconurl() {
        return iconurl;
    }


    public String getGname() {
        return gname;
    }


    public String getOperators() {
        return operators;
    }


    public String getAddtime() {
        return addtime;
    }

    public KaiceInfo(String id, String iconurl, String gname, String operators, String addtime) {
        this.id = id;
        this.iconurl = iconurl;
        this.gname = gname;
        this.operators = operators;
        this.addtime = addtime;
    }
}
