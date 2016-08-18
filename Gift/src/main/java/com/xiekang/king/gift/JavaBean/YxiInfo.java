package com.xiekang.king.gift.JavaBean;

import java.util.List;

/**
 * Created by King on 2016/8/16.
 */
public class YxiInfo {

    /**
     * id : 1467107394
     * name : 口袋妖怪重制
     * developers : 深圳市豹风网络股份有限公司
     * appsize :
     * version :
     * logo : /allimgs/img_iapp/201608/_1470650646182.png
     * download_addr : http://smbbrescdn.pkpkpk.cn/res/apk/kdyg_1.0.3_istorm_0_2016_07_27_151509.apk
     * upload_time : 1467107340
     * add_time : 1467107393
     * state : 1
     * keywords : 口袋妖怪重制评测，口袋妖怪重制攻略
     * operator : 小二
     * typeid : 2
     * orderid : 1
     * description : 口袋妖怪20周年纪念大作，最经典IP重制巨献！国民级RPG宠物对战神作《口袋妖怪重制》100%复刻原著剧情，再现精灵大师炼成之路！数百款萌宠精灵等你捕捉收集、精心养成，最强精灵无限进化！经典怀旧的对战模式，属性相克实力PK，顶级特效完美展现精灵技能释放！来《口袋妖怪重制》，精灵梦幻之旅等你来体验。
     * good_evaluation : 0
     * bad_evaluation : 0
     * downloads : 5
     * views : 0
     * flag : 1
     * is_free : 0
     * freename : 免费
     * video_addr : http://kdyg.istorm.cn/
     * statename : 已上架
     * flagname : 推荐
     * typename : 角色扮演
     * imagenum : 0
     * py : KDYGZZ
     * vtype : 1,2
     * vtypename : [安卓]&nbsp;[IOS]&nbsp;
     * vtypeimgs : <i class='android'></i><i class='ios'></i>
     * downs : 0
     * yy : 0
     * yyname : 中文
     * isnetwork : 0
     * isgame : 0
     * true_good_evaluation : 0
     * true_bad_evaluation : 0
     * true_downloads : 5
     * true_views : 0
     * tz : 休闲,策略,
     * fmoeny : 0
     * isintegral : 0
     * gflag : 0
     * libaoimg :
     * zqshowimg :
     * iszq : 0
     * zqurl : http://
     * moulds : 0
     * bgimg :
     * remarks : 一款由经典游戏改编的3D手游
     * appscore : 8.0
     * appscore1 : 8
     * appscore2 : .0
     * trueappscore : 5
     * zqcode : KDYGCZ
     * isnewgame : 0
     * packagename :
     * zqflag : 0
     * zqscore : 5.0
     */


    /**
     * id : 255675
     * address : /allimgs/img_iapp/201608/_1470650654463.jpg
     * orderno : 9
     * state : 0
     * appid : 1467107394
     */


    private String name;
    private String logo;
    private String download_addr;
    private String description;
    private String typename;
    private String appSize;
    private List<String> imageList;

    public YxiInfo(String name, String logo, String download_addr, String description, String typename, String appSize, List<String> imageList) {
        this.name = name;
        this.logo = logo;
        this.download_addr = download_addr;
        this.description = description;
        this.typename = typename;
        this.appSize = appSize;
        this.imageList = imageList;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getDownload_addr() {
        return download_addr;
    }

    public String getDescription() {
        return description;
    }

    public String getTypename() {
        return typename;
    }

    public String getAppSize() {
        return appSize;
    }

}
