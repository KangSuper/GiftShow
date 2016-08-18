package com.xiekang.king.gift.JavaBean;

/**
 * Created by King on 2016/8/17.
 */
public class WeeklyChildInfo {

    /**
     * id : 810
     * fid : 20160534
     * appid : 1467612648
     * appname : 少女咖啡枪
     * typename : 角色扮演
     * appsize : 430M
     * adimg : /allimgs/img_iapp/201608/_1470979549307.jpg
     * appkfs : 西山居
     * iconurl : /allimgs/img_iapp/201607/_1467612570049.png
     * addtime : 2016-08-12
     * descs : 《少女咖啡枪》是由成都西山居自主研发的3D射击作战手游。这是一款以二次元为受众群体、主打枪
     * 械及萌妹的轻科幻风格大作。游戏中你可以手持枪械击退敌人亦可以操作载具对敌方势力进行碾压，同时还可以经营在末世间隙中努力生存的咖啡馆，跟你喜欢的妹子拉近距离
     * critique : 　　《少女咖啡枪》是一款锻炼反应力和走位的激烈动作枪战型猫咪饲育指南向摸头杀爱好者天堂兼少女养成游戏，优点在于后期关卡堪比《黑暗之魂》一般的难度可以满足抖M们的心理，休闲养肝党每天也可以在喂猫摸头中了此残生(误) ，如果能加入手柄的接口想来肯定能使得操作手感方面更上一层楼，最后，他最大的缺点小编也觉得应该如实的说出来不需要隐瞒，那就是：
     * 还!T!M!没!开!放!公!测!啊!
     * <p/>
     * iszq : 0
     * typeid : 0
     * istop : 0
     */


    private String appid;
    private String appname;
    private String typename;
    private String appsize;
    private String iconurl;
    private String descs;

    public WeeklyChildInfo(String appid, String appname, String typename, String appsize, String iconurl, String descs) {
        this.appid = appid;
        this.appname = appname;
        this.typename = typename;
        this.appsize = appsize;
        this.iconurl = iconurl;
        this.descs = descs;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppname() {
        return appname;
    }

    public String getTypename() {
        return typename;
    }

    public String getAppsize() {
        return appsize;
    }

    public String getIconurl() {
        return iconurl;
    }

    public String getDescs() {
        return descs;
    }
}
