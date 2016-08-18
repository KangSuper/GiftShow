package com.xiekang.king.gift.JavaBean;

import java.io.Serializable;

/**
 * Created by King on 2016/8/17.
 */
public class BdaInfo implements Serializable{

    /**
     * sid : 112
     * name : 炎炎夏日 一秒透心
     * state : 0
     * addtime : 2016-08-16
     * clicks : 32
     * iconurl : /allimgs/img_iapp/201608/_1471346567926.jpg
     * istop : 0
     * good : 0
     * bad : 0
     * keyword :
     * descs : 马上就快到中元节了，中元节又俗称鬼节，这个传统节日总觉得有点瘆的慌呀，胆小的小编在写这篇恐怖手游盘点时，内心其实是拒绝的@.@！不过据说恐怖游戏会令人脉搏加快、心率提升，从而增强血液循环，让压力在瞬间得到释放，同时消耗热量，对健康很有益（能减肥？）……！
     * orderno : 25
     * nums : 9
     */


    private String sid;
    private String name;
    private String addtime;
    private String iconurl;
    private String descs;

    public BdaInfo(String sid, String name, String addtime, String iconurl, String descs) {
        this.sid = sid;
        this.name = name;
        this.addtime = addtime;
        this.iconurl = iconurl;
        this.descs = descs;
    }

    public String getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public String getAddtime() {
        return addtime;
    }

    public String getIconurl() {
        return iconurl;
    }

    public String getDescs() {
        return descs;
    }
}
