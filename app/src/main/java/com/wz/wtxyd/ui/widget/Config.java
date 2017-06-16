package com.wz.wtxyd.ui.widget;

import android.content.Context;
import android.graphics.Typeface;

import com.wz.wtxyd.AppApplication;
import com.wz.wtxyd.R;
import com.wz.wtxyd.common.util.SPUtil;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class Config {
    private final static String SP_NAME = "config";
    private final static String BOOK_BG_KEY = "bookbg";
    private final static String FONT_TYPE_KEY = "fonttype";
    public final static String FONT_SIZE_KEY = "fontsize";
    private final static String NIGHT_KEY = "night";
    private final static String LIGHT_KEY = "light";
    private final static String SYSTEM_LIGHT_KEY = "systemlight";
    public final static String PAGE_MODE_KEY = "pagemode";

    public final static String FONTTYPE_DEFAULT = "";
    public final static String FONTTYPE_QIHEI = "font/qihei.ttf";
    public final static String FONTTYPE_WAWA = "font/font1.ttf";

    public final static String FONTTYPE_FZXINGHEI = "font/fzxinghei.ttf";
    public final static String FONTTYPE_FZKATONG = "font/fzkatong.ttf";
    public final static String FONTTYPE_BYSONG = "font/bysong.ttf";

    public final static int BOOK_BG_DEFAULT = 0;
    public final static int BOOK_BG_1 = 1;
    public final static int BOOK_BG_2 = 2;
    public final static int BOOK_BG_3 = 3;
    public final static int BOOK_BG_4 = 4;

    public final static int PAGE_MODE_SIMULATION = 0;
    public final static int PAGE_MODE_COVER = 1;
    public final static int PAGE_MODE_SLIDE = 2;
    public final static int PAGE_MODE_NONE = 3;

    private Context mContext;
    private static Config config;
    //private SharedPreferences sp;
    //字体
    private Typeface typeface;
    //字体大小
    private float mFontSize = 0;
    //亮度值
    private float light = 0;
    private int bookBG;

    private Config() {
        this.mContext = AppApplication.getContext();
        //sp = this.mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized Config getInstance() {
        if (config == null){
            synchronized (Config.class){
                if (config == null){
                    config = new Config();
                }
            }
        }
        return config;
    }


    public int getPageMode() {
        return SPUtil.getInstance().getInt(PAGE_MODE_KEY, PAGE_MODE_SIMULATION);
    }

    public void setPageMode(int pageMode) {
//        sp.edit().putInt(PAGE_MODE_KEY,pageMode).commit();
        SPUtil.getInstance().putInt(PAGE_MODE_KEY, pageMode);
    }

    public int getBookBgType() {
//        return sp.getInt(BOOK_BG_KEY,BOOK_BG_DEFAULT);
        return SPUtil.getInstance().getInt(BOOK_BG_KEY, BOOK_BG_DEFAULT);
    }

    public void setBookBg(int type) {
        // sp.edit().putInt(BOOK_BG_KEY,type).commit();
        SPUtil.getInstance().putInt(BOOK_BG_KEY, type);
    }

    public Typeface getTypeface() {
        if (typeface == null) {
//            String typePath = sp.getString(FONT_TYPE_KEY,FONTTYPE_QIHEI);
            String typePath = SPUtil.getInstance().getString(FONT_TYPE_KEY, FONTTYPE_QIHEI);
            typeface = getTypeface(typePath);
        }
        return typeface;
    }

    public String getTypefacePath() {
//        String path = sp.getString(FONT_TYPE_KEY,FONTTYPE_QIHEI);
        return SPUtil.getInstance().getString(FONT_TYPE_KEY, FONTTYPE_QIHEI);
    }

    public Typeface getTypeface(String typeFacePath) {
        Typeface mTypeface;
        if (typeFacePath.equals(FONTTYPE_DEFAULT)) {
            mTypeface = Typeface.DEFAULT;
        } else {
            mTypeface = Typeface.createFromAsset(mContext.getAssets(), typeFacePath);
        }
        return mTypeface;
    }

    public void setTypeface(String typefacePath) {
        typeface = getTypeface(typefacePath);
//        sp.edit().putString(FONT_TYPE_KEY, typefacePath).commit();
        SPUtil.getInstance().putString(FONT_TYPE_KEY, typefacePath);
    }

    public float getFontSize() {
        if (mFontSize == 0) {
            mFontSize = SPUtil.getInstance().getFloat(FONT_SIZE_KEY, mContext.getResources().getDimension(R.dimen.reading_default_text_size));
        }
        return mFontSize;
    }


    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
        SPUtil.getInstance().putFloat(FONT_SIZE_KEY, fontSize);
    }

    /**
     * 获取夜间还是白天阅读模式,true为夜晚，false为白天
     */
    public boolean getDayOrNight() {
//        return sp.getBoolean(NIGHT_KEY, false);
        return SPUtil.getInstance().getBoolean(NIGHT_KEY, false);
    }

    public void setDayOrNight(boolean isNight) {
//        sp.edit().putBoolean(NIGHT_KEY, isNight).commit();
        SPUtil.getInstance().putBoolean(NIGHT_KEY, isNight);
    }

    public Boolean isSystemLight() {
//        return sp.getBoolean(SYSTEM_LIGHT_KEY, true);
        return SPUtil.getInstance().getBoolean(SYSTEM_LIGHT_KEY, true);
    }

    public void setSystemLight(Boolean isSystemLight) {
//        sp.edit().putBoolean(SYSTEM_LIGHT_KEY, isSystemLight).commit();
        SPUtil.getInstance().putBoolean(SYSTEM_LIGHT_KEY, isSystemLight);
    }

    public float getLight() {
        if (light == 0) {
//            light = sp.getFloat(LIGHT_KEY, 0.1f);
            light = SPUtil.getInstance().getFloat(LIGHT_KEY, 0.1f);
        }
        return light;
    }

    /**
     * 记录配置文件中亮度值
     */
    public void setLight(float light) {
        this.light = light;
//        sp.edit().putFloat(LIGHT_KEY, light).commit();
        SPUtil.getInstance().putFloat(LIGHT_KEY, light);
    }
}
