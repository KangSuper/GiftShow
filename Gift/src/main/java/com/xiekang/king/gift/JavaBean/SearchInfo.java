package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/17.
 */
public class SearchInfo {

    /**
     * id : 1471396560
     * iconurl : /allimgs/img_iapp/201604/_1460530943863.png
     * giftname : 二测尊享礼包
     * number : 17
     * exchanges : 20
     * type : 1
     * gname : 水晶之歌
     * integral : 0
     * isintegral : 0
     * addtime : 2016-08-17
     * ptype : 1,
     * operators : 雀跃互娱
     * flag : 1
     */


    private String id;
    private String iconurl;
    private String giftname;
    private int number;
    private String gname;
    private String addtime;

    public SearchInfo(String id, String iconurl, String giftname, int number, String gname, String addtime) {
        this.id = id;
        this.iconurl = iconurl;
        this.giftname = giftname;
        this.number = number;
        this.gname = gname;
        this.addtime = addtime;
    }

    public String getId() {
        return id;
    }

    public String getIconurl() {
        return iconurl;
    }

    public String getGiftname() {
        return giftname;
    }

    public int getNumber() {
        return number;
    }

    public String getGname() {
        return gname;
    }

    public String getAddtime() {
        return addtime;
    }
}
