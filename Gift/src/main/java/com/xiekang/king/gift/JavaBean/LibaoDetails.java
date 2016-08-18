package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/15.
 */
public class LibaoDetails {

    /**
     * id : 1471490401
     * iconurl : /allimgs/img_iapp/201508/_1439365416187.png
     * giftname : 独家夏末礼包
     * gcode :
     * number : 30
     * exchanges : 30
     * flag : 1
     * explains : 28钻+金币*5W+培养*20+水之石*5
     * descs : 点击头像-兑换礼包-输入兑换码
     * addtime : 2016-08-18
     * sendtime : 1970-01-01
     * overtime : 2016-10-18
     * type : 1
     * typename : 礼包卡
     * operators : 金华星秀
     * surplus : 0
     * gid : 1409298252
     * gname : 宠物小精灵官方版
     * uid : 402881d2554284e2015547751122012a
     * integral : 0
     * isintegral : 0
     * vtypeimg : <i class='android'></i>
     * vtype : 2
     * marks : 0
     * vtypename : 手机客户端
     * ptype : 1,
     * indexpy : 0
     * gtype : 2
     * onclicks : 0
     * turl :
     * showimg :
     */


    private String iconurl;
    private String giftname;
    private int exchanges;
    private String explains;
    private String descs;
    private String overtime;
    private String gname;

    public LibaoDetails(String iconurl, String giftname, int exchanges, String explains, String descs, String overtime, String gname) {
        this.iconurl = iconurl;
        this.giftname = giftname;
        this.exchanges = exchanges;
        this.explains = explains;
        this.descs = descs;
        this.overtime = overtime;
        this.gname = gname;
    }

    public String getIconurl() {
        return iconurl;
    }

    public String getGiftname() {
        return giftname;
    }

    public int getExchanges() {
        return exchanges;
    }

    public String getExplains() {
        return explains;
    }

    public String getDescs() {
        return descs;
    }

    public String getOvertime() {
        return overtime;
    }

    public String getGname() {
        return gname;
    }
}
