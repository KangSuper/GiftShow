package com.xiekang.king.gift.JavaBean;


/**
 * Created by King on 2016/8/15.
 *
 */
public class LibaoInfo {

    /**
     * id : 1470965609
     * iconurl : /uploads/allimg/140818/20-140QQI3150-L.png
     * giftname : 1688玩4周年礼包
     * number : 82
     * exchanges : 18
     * type : 1
     * gname : 太极熊猫
     * integral : 0
     * isintegral : 0
     * addtime : 2016-08-12
     * ptype : 1,2,
     * operators :
     * flag : 1
     */


    private String id;
    private String iconurl;
    private String giftname;
    private int number;
    private int exchanges;
    private int type;
    private String gname;
    private int integral;
    private int isintegral;
    private String addtime;
    private String ptype;
    private String operators;
    private int flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getExchanges() {
        return exchanges;
    }

    public void setExchanges(int exchanges) {
        this.exchanges = exchanges;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getIsintegral() {
        return isintegral;
    }

    public void setIsintegral(int isintegral) {
        this.isintegral = isintegral;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public LibaoInfo(String id, String iconurl, String giftname, int number, int exchanges, int type, String gname, int integral, int isintegral, String addtime, String ptype, String operators, int flag) {
        this.id = id;
        this.iconurl = iconurl;
        this.giftname = giftname;
        this.number = number;
        this.exchanges = exchanges;
        this.type = type;
        this.gname = gname;
        this.integral = integral;
        this.isintegral = isintegral;
        this.addtime = addtime;
        this.ptype = ptype;
        this.operators = operators;
        this.flag = flag;
    }
}
