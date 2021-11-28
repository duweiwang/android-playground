package com.example.customview.loading;

public class LoadingTypes {
    // 通用类型在 COMMON_BASE 与 SPECIAL_BASE 之间
    private static final int COMMON_BASE = 0; //base//通用

    public final static int DEFAULT = COMMON_BASE + 1; //其它//通用

    public final static int LIST_HEADER = COMMON_BASE + 2; //列表头//通用

    public final static int LIST_FOOTER = COMMON_BASE + 3; //列表底//通用

    public final static int FLOATING = COMMON_BASE + 4; //浮窗//通用

    public final static int START = COMMON_BASE + 5; //开始//通用

    public static final int CHANGE_STEAM = COMMON_BASE + 6;//切流//视频区

    public static final int WHITE_LOADING = COMMON_BASE + 7;//通用白色菊花//可以参考9108版本MV播放页面，位置不固定

    public static final int KTV_MV_LOADING = COMMON_BASE + 8;//KTV白色菊花//参考9108版本ktv作品播放页加载菊花

    public static final int MINIAPP_LOADING = COMMON_BASE + 9;//UI框架//小程序Loading

    public static final int MV_FEE_LOADING = COMMON_BASE + 10;//mv鉴权白色菊花//通用


    public static final int FEE_CHECK = COMMON_BASE + 41; //鉴权浮窗//通用

    public static final int COMMENT = COMMON_BASE + 42; //打开评论浮窗//通用

    public static final int SHARE = COMMON_BASE + 43; //分享浮窗//通用

    public static final int KUBI_RECHARGE = COMMON_BASE + 44; //酷币充值//通用

    public static final int ALBUM_BUY = COMMON_BASE + 45; //专辑购买//非音乐包通用

    public static final int MV_FEE_CHECK = COMMON_BASE + 46; //Mv鉴权//通用


    //自定义类型必须要在 SPECIAL_BASE 以上
    private static final int SPECIAL_BASE = 100;//base//通用

    public static final int SEARCH_RESULT_SONG = SPECIAL_BASE + 1; //搜索结果-歌曲//点击搜索-输入搜索内容-点击搜索

    public static final int SEARCH_RESULT_MV = SPECIAL_BASE + 2; //搜索结果-MV//点击搜索-输入搜索内容-点击搜索-点击MVtab

    public static final int SEARCH_RESULT_ALBUM = SPECIAL_BASE + 3; //搜索结果-专辑//点击搜索-输入搜索内容-点击搜索-点击专辑tab

    public static final int SEARCH_RESULT_SONGLIST = SPECIAL_BASE + 4; //搜索结果-歌单//点击搜索-输入搜索内容-点击搜索-点击歌单tab

    public static final int SEARCH_RESULT_USER = SPECIAL_BASE + 5; //搜索结果-用户//点击搜索-点击输入内容-点击搜索-点击用户tab

    public static final int SEARCH_RESULT_LYRIC = SPECIAL_BASE + 6; //搜索结果-歌词//点击搜索-点击输入内容-点击搜索-点击歌词tab

    public static final int PLAYER_FRAGMENT_RIGHT_INFO = SPECIAL_BASE + 7; //播放页-右侧歌曲信息//点击播放bar-在播放页右左划看到歌曲信息页

    public static final int SEARCH_RESULT_PROGRAM = SPECIAL_BASE + 8; //搜索结果-有声电台//点击搜索-输入搜索内容-点击搜索-点击有声电台tab

    public static final int SEARCH_RESULT_ALL = SPECIAL_BASE + 9; //搜索结果-有声电台//点击搜索-输入搜索内容-点击搜索-点击有声电台tab

    public static final int SEARCH_RESULT_SINGER = SPECIAL_BASE + 10; //搜索结果-有声电台//点击搜索-输入搜索内容-点击搜索-点击歌手tab

    public static final int SEARCH_RESULT_KSONG = SPECIAL_BASE + 11; //搜索结果-K歌//点击搜索-输入搜索内容-点击搜索-点击K歌tab


    public static final int MINE_TAG_FOLLOW_SINGER = SPECIAL_BASE + 12; //首页-我的tab//点击主播群右侧小人头

    public static final int MINE_TAG_FOLLOW_SINGER_FOOTER = SPECIAL_BASE + 13; //首页-我的tab//点击主播群右侧小人头(翻页)

    public static final int UPDATE_BG = SPECIAL_BASE + 14; //更换背景图片//个人中心-点击背景
    public static final int LOAD_DATA = SPECIAL_BASE + 15; //加载所有背景图片//个人中心-点击背景
    public static final int KTV_MID_CHANGCHANG = SPECIAL_BASE + 16; //播放页//点击麦克风跳转中间页


}