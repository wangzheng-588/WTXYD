package com.wz.wtxyd.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.ChpaterBean;
import com.wz.wtxyd.bean.TRPage;
import com.wz.wtxyd.common.util.DensityUtil;
import com.wz.wtxyd.common.util.EncodingUtils;
import com.wz.wtxyd.common.util.ToastUtil;
import com.wz.wtxyd.data.db.BookDBManager;
import com.wz.wtxyd.ui.adapter.ChapterAdapter;
import com.wz.wtxyd.ui.widget.Config;
import com.wz.wtxyd.ui.widget.DividerItemDecoration;
import com.wz.wtxyd.ui.widget.PageFactory;
import com.wz.wtxyd.ui.widget.PageWidget;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.wz.wtxyd.ui.widget.Config.PAGE_MODE_COVER;
import static com.wz.wtxyd.ui.widget.Config.PAGE_MODE_NONE;
import static com.wz.wtxyd.ui.widget.Config.PAGE_MODE_SIMULATION;
import static com.wz.wtxyd.ui.widget.Config.PAGE_MODE_SLIDE;

/**
 * Created by wz on 17-6-8.
 * 读书activity
 */

public class BookReadAreaActivity extends AppCompatActivity implements PageWidget.TouchListener, DrawerLayout.DrawerListener {

    private static final int HIDE_MENU = 1;
    private static final int AUTO_READ = 2;
    @BindView(R.id.book_page)
    PageWidget mPageWidget;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_label)
    ImageView mIvLabel;
    @BindView(R.id.tv_up_chapter)
    TextView mTvUpChapter;
    @BindView(R.id.seek_chapter)
    SeekBar mSeekChapter;
    @BindView(R.id.tv_next_chapter)
    TextView mTvNextChapter;
    @BindView(R.id.ll_read_bottom_menu)
    LinearLayout mLlReadBottomMenu;
    @BindView(R.id.iv_mode)
    ImageView mIvMode;
    @BindView(R.id.rl_menu)
    RelativeLayout mRlMenu;
    @BindView(R.id.iv_font)
    ImageView mIvFont;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.chapter_menu)
    ImageView mChapterMenu;
    @BindView(R.id.iv_brightness_reduce)
    ImageView mIvBrightnessReduce;
    @BindView(R.id.seek_brightness)
    SeekBar mSeekBrightness;
    @BindView(R.id.iv_brightness_add)
    ImageView mIvBrightnessAdd;
    @BindView(R.id.tv_follow_system)
    TextView mTvFollowSystem;
    @BindView(R.id.ib_font_reduce)
    ImageButton mIbFontReduce;
    @BindView(R.id.tv_font)
    TextView mTvFont;
    @BindView(R.id.ib_font_add)
    ImageButton mIbFontAdd;
    @BindView(R.id.read_bg_default)
    CircleImageView mReadBgDefault;
    @BindView(R.id.read_bg_1)
    CircleImageView mReadBg1;
    @BindView(R.id.read_bg_2)
    CircleImageView mReadBg2;
    @BindView(R.id.read_bg_3)
    CircleImageView mReadBg3;
    @BindView(R.id.read_bg_4)
    CircleImageView mReadBg4;
    @BindView(R.id.read_bg_custom)
    CircleImageView mReadBgCustom;
    @BindView(R.id.tv_chapter_name)
    TextView mTvChapterName;
    @BindView(R.id.btn_chapter)
    Button mBtnChapter;
    @BindView(R.id.btn_label)
    Button mBtnLabel;
    @BindView(R.id.linear_layout)
    LinearLayout mLinearLayout;
    @BindView(R.id.tv_progress)
    TextView mTvProgress;
    @BindView(R.id.ll_progress)
    LinearLayout mLlProgress;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_font_menu)
    LinearLayout mLlFontMenu;
    @BindView(R.id.ll_read_setting_menu)
    LinearLayout mLiReadSettingMenu;
    @BindView(R.id.iv_read_setting)
    ImageView mIvReadSetting;
    @BindView(R.id.tv_auto_read)
    TextView mTvAutoRead;
    @BindView(R.id.tv_read_mode_landscape)
    TextView mTvReadModeLandscape;
    @BindView(R.id.tv_read_mode_vertical_screen)
    TextView mTvReadModeVerticalScreen;
    @BindView(R.id.tv_turn_page_1)
    TextView mTvTurnPage1;
    @BindView(R.id.tv_turn_page_2)
    TextView mTvTurnPage2;
    @BindView(R.id.tv_turn_page_3)
    TextView mTvTurnPage3;
    @BindView(R.id.tv_turn_page_4)
    TextView mTvTurnPage4;
    @BindView(R.id.tv_voice)
    TextView mTvVoice;


    private PageFactory mInstance;
    private boolean isShowSystemMenu;
    private ChapterAdapter mChapterAdapter;
    private BookBean mBook;
    private int mM_mbBufEnd;
    private List<ChpaterBean> mBookChapter;
    private int mCurrentChapter;
    private int currentChapterProgress;
    private boolean isShowFontMenu;//是否显示字体菜单
    private boolean isShowReadMenu;//是否显示阅读模式菜单
    private float mFontSize;
    private boolean isAudoRead;//是否是自动阅读
    private boolean isVoideRead;//是否是语音朗读
    SpeechSynthesizer mTts;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_MENU:
                    hideFontMenu();
                    showOrHideSystemMenu();
                    hideReadSettingMenu();
                    break;
                case AUTO_READ:
                    if (mInstance!=null){
                        try {
                            mInstance.nextPage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessageDelayed(AUTO_READ,8000);
                    }

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read_area);
        ButterKnife.bind(this);

        mPageWidget.setTouchListener(this);

        initData();
        initListener();

    }

    private void initListener() {
        mChapterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                List<ChpaterBean> data = mChapterAdapter.getData();
                long bookCatalogueStartPos = data.get(position).getBookCatalogueStartPos();

                //jumpChapter(bookCatalogueStartPos);
                TRPage pageForBegin = mInstance.getPageForBegin((int) bookCatalogueStartPos);
                mInstance.currentPage(pageForBegin);

                mDrawerLayout.closeDrawers();
            }
        });

        mSeekChapter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mBookChapter != null && mBookChapter.size() > 0) {

                }
                if (fromUser) {
                    currentChapterProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Log.e("TAG", String.valueOf(currentChapterProgress)+"当前进度是：");

                if (mBookChapter != null && mBookChapter.size() > 0) {
                    seekBar.setMax(mBookChapter.size() - 1);
                    seekBar.setProgress(currentChapterProgress);
                    ChpaterBean chpaterBean = mBookChapter.get(seekBar.getProgress());
                    long bookCatalogueStartPos = chpaterBean.getBookCatalogueStartPos();
                    //jumpChapter(bookCatalogueStartPos);
                    TRPage pageForBegin = mInstance.getPageForBegin((int) bookCatalogueStartPos);
                    mInstance.currentPage(pageForBegin);

                }


            }
        });
    }

    private void jumpChapter(long bookCatalogueStartPos) {

        TRPage currentPage = mInstance.getCurrentPage();

        currentPage.setEnd(bookCatalogueStartPos);
        currentPage = mInstance.getPageForBegin((int) bookCatalogueStartPos);
        mInstance.setCurrentPage(currentPage);
        mInstance.currentPage(currentPage);
    }

    private void initData() {
        //禁止侧滑菜单滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChapterAdapter = new ChapterAdapter();
        mDrawerLayout.addDrawerListener(this);


        Bundle extras = getIntent().getExtras();
        mBook = (BookBean) extras.getSerializable("book");
        String encoding = EncodingUtils.getEncoding(mBook);
        mBook.setEncoding(encoding);
        mInstance = PageFactory.createPageFactory(this);

        //显示模式
        int pageMode = Config.getInstance().getPageMode();
        changePageModeBG(pageMode);
        mPageWidget.setPageMode(pageMode);
        mInstance.setPageWidget(mPageWidget);
        try {
            mInstance.openBook(mBook);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getChapter();

        //当前读书的进度
        mM_mbBufEnd = mInstance.getM_mbBufEnd();

        mCurrentChapter = getCurrentChapter(mM_mbBufEnd);
        mSeekChapter.setMax(mBookChapter.size() - 1);


    }

    //切换翻页模式背景图
    private void changePageModeBG(int pageMode) {
        switch (pageMode){
            case PAGE_MODE_SIMULATION:
                mTvTurnPage1.setBackgroundResource(R.drawable.menu_bg_pressed);
                mTvTurnPage2.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage3.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage4.setBackgroundResource(R.drawable.menu_bg_normal);
                break;
            case PAGE_MODE_COVER:
                mTvTurnPage1.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage2.setBackgroundResource(R.drawable.menu_bg_pressed);
                mTvTurnPage3.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage4.setBackgroundResource(R.drawable.menu_bg_normal);
                break;
            case PAGE_MODE_SLIDE:
                mTvTurnPage1.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage2.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage3.setBackgroundResource(R.drawable.menu_bg_pressed);
                mTvTurnPage4.setBackgroundResource(R.drawable.menu_bg_normal);
                break;
            case PAGE_MODE_NONE:
                mTvTurnPage1.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage2.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage3.setBackgroundResource(R.drawable.menu_bg_normal);
                mTvTurnPage4.setBackgroundResource(R.drawable.menu_bg_pressed);
                break;
        }
    }

    //隐藏字体菜单
    private void hideFontMenu() {
        mLlFontMenu.setVisibility(View.GONE);
        isShowFontMenu = false;
        mHandler.removeMessages(HIDE_MENU);
    }

    //显示字体菜单
    private void showFontMenu() {

        mLlFontMenu.setVisibility(View.VISIBLE);
        isShowFontMenu = true;
        mRlMenu.setVisibility(View.GONE);
        mTvFont.setText(String.valueOf(DensityUtil.dip2sp(Config.getInstance().getFontSize())));
        mHandler.sendEmptyMessageDelayed(HIDE_MENU, 3000);
    }

    //显示阅读设置菜单
    private void showReadSettingMenu(){
        hideFontMenu();
        isShowReadMenu = true;
        mRlMenu.setVisibility(View.GONE);
        mLiReadSettingMenu.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(HIDE_MENU, 3000);
    }

    //隐藏阅读设置菜单
    private void hideReadSettingMenu(){
        hideFontMenu();
        isShowReadMenu = false;
        mRlMenu.setVisibility(View.GONE);
        mLiReadSettingMenu.setVisibility(View.GONE);
        mHandler.sendEmptyMessageDelayed(HIDE_MENU, 3000);
    }



    @Override
    public void center() {

        if (isShowFontMenu) {
            hideFontMenu();
        }

        if (isShowReadMenu){
            hideReadSettingMenu();
        }
        showOrHideSystemMenu();


    }



    @Override
    public Boolean prePage() {
        if (isShowSystemMenu || isShowFontMenu) {
//            mRlMenu.setVisibility(View.GONE);
//            isShowSystemMenu = false;
            showOrHideSystemMenu();
            hideFontMenu();
            return false;
        } else {
            try {
                mInstance.prePage();
                if (mInstance.isfirstPage()) {
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return true;
    }

    @Override
    public Boolean nextPage() {
        if (isShowSystemMenu || isShowFontMenu) {
            showOrHideSystemMenu();
            hideFontMenu();
            return false;
        } else {
            try {
                mInstance.nextPage();
                if (mInstance.islastPage()) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public void cancel() {
        mInstance.cancelPage();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        if (mTts!=null){

            mTts.stopSpeaking();
        }
        mInstance.clear();
        super.onDestroy();
    }

    @OnClick({R.id.chapter_menu, R.id.iv_back, R.id.iv_font, R.id.ib_font_add
            , R.id.ib_font_reduce, R.id.iv_label, R.id.tv_up_chapter, R.id.seek_chapter
            , R.id.tv_next_chapter, R.id.iv_mode, R.id.read_bg_default, R.id.read_bg_1, R.id.read_bg_2
            , R.id.read_bg_3, R.id.read_bg_4, R.id.iv_read_setting,R.id.tv_read_mode_landscape
            , R.id.tv_read_mode_vertical_screen, R.id.tv_turn_page_1, R.id.tv_turn_page_2, R.id.tv_turn_page_3
            , R.id.tv_turn_page_4,R.id.tv_auto_read,R.id.tv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_label:

                break;
            //跳转到上一章节
            case R.id.tv_up_chapter:
                mM_mbBufEnd = mInstance.getM_mbBufEnd();
                mCurrentChapter = getCurrentChapter(mM_mbBufEnd);
                if (mCurrentChapter >= 1) {

                    long bookCatalogueStartPos = mBookChapter.get(mCurrentChapter - 1).getBookCatalogueStartPos();
                    jumpChapter(bookCatalogueStartPos);
                } else {
                    ToastUtil.show("当前是第一章！");
                }
                break;
            //跳到下一章节
            case R.id.tv_next_chapter:
                mM_mbBufEnd = mInstance.getM_mbBufEnd();
                mCurrentChapter = getCurrentChapter(mM_mbBufEnd);

                if (mCurrentChapter < mBookChapter.size() - 1) {
                    long bookCatalogueStartPos = mBookChapter.get(mCurrentChapter + 1).getBookCatalogueStartPos();
                    jumpChapter(bookCatalogueStartPos);
                } else {
                    ToastUtil.show("当前是最后一章！");
                }

                break;
            case R.id.iv_mode:
                //夜晚与白天模式
                ToastUtil.show("模式");
                break;
            case R.id.iv_font:
                //显示字体菜单项
                showFontMenu();
                break;
            case R.id.chapter_menu:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                mRlMenu.setVisibility(View.GONE);
                break;
            case R.id.ib_font_add:
                addFontSize();
                break;
            case R.id.ib_font_reduce:
                ReduceFontSize();
                break;
            case R.id.read_bg_default:
                mInstance.changeBookBg(Config.BOOK_BG_DEFAULT);
                break;
            case R.id.read_bg_1:
                mInstance.changeBookBg(Config.BOOK_BG_1);
                break;
            case R.id.read_bg_2:
                mInstance.changeBookBg(Config.BOOK_BG_2);
                break;
            case R.id.read_bg_3:
                mInstance.changeBookBg(Config.BOOK_BG_3);
                break;
            case R.id.read_bg_4:
                mInstance.changeBookBg(Config.BOOK_BG_4);
                break;
            case R.id.iv_read_setting:
                //显示阅读设置菜单项
                showReadSettingMenu();
                break;
            case R.id.tv_read_mode_landscape:
                break;
            case R.id.tv_read_mode_vertical_screen:
                break;
            case R.id.tv_turn_page_1:
                Config.getInstance().setPageMode(PAGE_MODE_SIMULATION);
                ToastUtil.show("仿真模式");
                mPageWidget.setPageMode(Config.getInstance().getPageMode());
                changePageModeBG(Config.getInstance().getPageMode());

                break;
            case R.id.tv_turn_page_2:
                Config.getInstance().setPageMode(PAGE_MODE_COVER);
                ToastUtil.show("覆盖模式");
                mPageWidget.setPageMode(Config.getInstance().getPageMode());
                changePageModeBG(Config.getInstance().getPageMode());
                break;
            case R.id.tv_turn_page_3:
                Config.getInstance().setPageMode(PAGE_MODE_SLIDE);
                ToastUtil.show("滑动模式");
                mPageWidget.setPageMode(Config.getInstance().getPageMode());
                changePageModeBG(Config.getInstance().getPageMode());
                break;
            case R.id.tv_turn_page_4:
                Config.getInstance().setPageMode(PAGE_MODE_NONE);
                ToastUtil.show("无");
                mPageWidget.setPageMode(Config.getInstance().getPageMode());
                changePageModeBG(Config.getInstance().getPageMode());
                break;
            //自动阅读
            case R.id.tv_auto_read:
                changeAutoReadMode();
               // mHandler.sendEmptyMessageDelayed(AUTO_READ,5000);
                break;
            //语音朗读
            case R.id.tv_voice:
                if (isVoideRead){
                    stopVoide();
                } else {
                    initVoice();
                    startVoice();
                }

                break;


        }
        mHandler.removeMessages(HIDE_MENU);
        mHandler.sendEmptyMessageDelayed(HIDE_MENU, 3000);
    }

    private void initVoice(){

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
    }


    private void stopVoide(){
        if (mTts!=null){
            mTvVoice.setText("开启语音朗读");
            ToastUtil.show("语音朗读已关闭");
            mTts.stopSpeaking();
            isVoideRead = false;
        }
    }

    //开启语音朗读
    private void startVoice() {
        isVoideRead = true;
        ToastUtil.show("开启语音朗读模式");
        mTvVoice.setText("关闭语音朗读");
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        TRPage currentPage = mInstance.getCurrentPage();
        List<String> lines = currentPage.getLines();
        String str = "";
        for (int i = 0; i < lines.size(); i++) {
            str += lines.get(i);
        }
        //3.开始合成
        mTts.startSpeaking(str, mSynListener);


    }


    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {

            Log.e("TAG","播放结束");
            try {
                mInstance.nextPage();
                startVoice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
            Log.e("TAG","播放结束"+"paused");
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    //减小字体
    private void ReduceFontSize() {
        mFontSize = Config.getInstance().getFontSize();
        if (mFontSize < DensityUtil.dip2sp(15)) {
            return;
        }
        mFontSize = Config.getInstance().getFontSize();
        mFontSize--;
        mInstance.changeFontSize((int) mFontSize);
        Config.getInstance().setFontSize(mFontSize);
        mTvFont.setText(String.valueOf(DensityUtil.dip2sp(mFontSize)));
    }

    private void addFontSize() {
        mFontSize = Config.getInstance().getFontSize();
        if (mFontSize > DensityUtil.dip2sp(35)) {
            return;
        }
        mFontSize++;
        mInstance.changeFontSize((int) mFontSize);
        Config.getInstance().setFontSize(mFontSize);
        mTvFont.setText(String.valueOf(DensityUtil.dip2sp(mFontSize)));
    }

    public int getCurrentChapter(int currentPos) {
        int num = 0;
        for (int i = 0; i < mBookChapter.size(); i++) {
            if (currentPos >= mBookChapter.get(i).getBookCatalogueStartPos()) {

                if (i == mBookChapter.size() - 1) {
                    num = i;
                    break;
                }
                if (currentPos <= mBookChapter.get(i + 1).getBookCatalogueStartPos()) {
                    num = i;
                }
            }
        }
        return num;
    }





    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

        //当前的章节
        mCurrentChapter = getCurrentChapter(mInstance.getM_mbBufEnd());

        //跳转到指定章节
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(mCurrentChapter, 0);
        manager.setStackFromEnd(true);

        //通知adapter设置指定目录当前位置为红色
        mChapterAdapter.setCurrentChapter(mCurrentChapter);
//        mChapterAdapter.notifyItemChanged(mCurrentChapter);
        mChapterAdapter.notifyDataSetChanged();

        //隐藏设置菜单
        // mRlMenu.setVisibility(View.GONE);
        showOrHideSystemMenu();
    }


    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }


    private void getChapter() {
        BookDBManager bookDBManager = new BookDBManager();
        mBookChapter = bookDBManager.getBookChapter(mBook);
        if (mBookChapter == null || mBookChapter.size() <= 0) {
            mInstance.getChapters(new PageFactory.OnChapterListener() {
                @Override
                public void onStart() {


                }

                @Override
                public void onLoading(final ChpaterBean chapter) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mLlProgress.setVisibility(View.VISIBLE);
                        }
                    });

                }

                @Override
                public void onFinished(final List<ChpaterBean> list) {
                    //插入数据库
                    BookDBManager manager = new BookDBManager();
                    manager.deleteChapter(mBook.getPath());
                    manager.insertChapterList(list, mBook.getPath());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLlProgress.setVisibility(View.GONE);
                            //mSeekChapter.setMax(list.size());
                            mBookChapter = list;
                            setListView(list);
                        }
                    });
                }
            });
        } else {
            mLlProgress.setVisibility(View.GONE);
            setListView(mBookChapter);
//            mSeekChapter.setMax(mBookChapter.size());
        }


    }

    private void setListView(List<ChpaterBean> chapter) {
        mChapterAdapter.getData().clear();
        mChapterAdapter.addData(chapter);
        mRecyclerView.setAdapter(mChapterAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        mChapterAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPause() {
        super.onPause();
        BookDBManager bookDBManager = new BookDBManager();

        //把当前进度保存到数据库
        mBook.setBegin(mInstance.getM_mbBufBegin());
        mBook.setEnd(mInstance.getM_mbBufEnd());
        bookDBManager.updateBookProgress(mBook);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    //显示隐藏系统菜单
    private void showOrHideSystemMenu() {
        if (isShowSystemMenu) {
            mRlMenu.setVisibility(View.GONE);
            isShowSystemMenu = false;
            mHandler.removeMessages(HIDE_MENU);

        } else {
            mSeekChapter.setMax(mBookChapter.size());
            mCurrentChapter = getCurrentChapter(mInstance.getM_mbBufEnd());
            mSeekChapter.setProgress(mCurrentChapter);
            mRlMenu.setVisibility(View.VISIBLE);
            isShowSystemMenu = true;
            mHandler.sendEmptyMessageDelayed(HIDE_MENU, 3000);
        }
    }

    //切换是否是自动阅读
    public void changeAutoReadMode(){
        if (isAudoRead){
            mHandler.removeMessages(AUTO_READ);
            isAudoRead = false;
            mTvAutoRead.setText("自动阅读");
            ToastUtil.show("手动阅读模式开启");
        } else {
            mHandler.sendEmptyMessageDelayed(AUTO_READ,8000);
            isAudoRead = true;
            mTvAutoRead.setText("手动阅读");
            ToastUtil.show("自动阅读模式开启");
        }
    }

}