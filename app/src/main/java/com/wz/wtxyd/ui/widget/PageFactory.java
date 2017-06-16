package com.wz.wtxyd.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.ChpaterBean;
import com.wz.wtxyd.bean.TRPage;
import com.wz.wtxyd.common.util.BitmapUtil;
import com.wz.wtxyd.common.util.DensityUtil;
import com.wz.wtxyd.data.db.BookDBManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class PageFactory {
    private static final String TAG = "PageFactory";
    private static PageFactory pageFactory;
    private static final int margin = DensityUtil.dip2px(10);//文字显示距离屏幕实际尺寸的偏移量
    private Context mContext;
    private Config config;

    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;
    //文字字体大小
    private float mFontSize;
    //时间格式
    private SimpleDateFormat sdf;
    // 上下与边缘的距离
    private float marginHeight;
    // 左右与边缘的距离
    private float measureMarginWidth;
    // 左右与边缘的距离
    private float marginWidth;
    //行间距
    private float lineSpace;
    //字体
    private Typeface typeface;
    //文字画笔
    private Paint mPaint;
    //加载画笔
    private Paint waitPaint;
    //文字颜色
    private int mTextColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页可以显示的行数
    private int mLineCount;
    //背景图片
    private Bitmap m_book_bg = null;

    //当前是否为第一页
    private boolean mIsfirstPage;
    //当前是否为最后一页
    private boolean mIslastPage;
    //书本widget
    private PageWidget mBookPageWidget;

    private static Status mStatus = Status.OPENING;
    private int m_mbBufLen;
    private MappedByteBuffer m_mbBuf;
    private String m_strCharsetName;
    private int m_mbBufEnd;
    private int m_mbBufBegin;
    RandomAccessFile randomFile;
    private TRPage currentPage;
    private long mFileLength;
    private TRPage cancelPage;
    private BookBean mCurrentBook;


    public enum Status {
        OPENING,
        FINISH,
        FAIL,
    }

    public static synchronized PageFactory getInstance() {
        return pageFactory;
    }

    public static synchronized PageFactory createPageFactory(Context context) {
        if (pageFactory == null) {
            synchronized (PageFactory.class) {
                if (pageFactory == null) {
                    pageFactory = new PageFactory(context);
                }
            }
        }
        return pageFactory;
    }

    private PageFactory(Context context) {
        mContext = context.getApplicationContext();
        config = Config.getInstance();
        //获取屏幕宽高
        mWidth = DensityUtil.getScreenW();
        mHeight = DensityUtil.getScreenH();

        marginWidth = DensityUtil.dip2px(15);
        marginHeight = DensityUtil.dip2px(25);
        lineSpace = DensityUtil.dip2px(5);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - DensityUtil.getNavigationBarrH() - marginHeight * 2;

        typeface = config.getTypeface();
        mFontSize = config.getFontSize();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(mFontSize);// 字体大小
        mPaint.setColor(mTextColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(mContext.getResources().getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(mTextColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果
        calculateLineCount();

        initBg();
        measureMarginWidth();
    }

    private void measureMarginWidth() {
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;
    }

    //初始化背景
    private void initBg() {

        //设置背景
        setBookBg(0);

    }


    //设置页面的背景
    public void setBookBg(int type) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                canvas = null;
                bitmap.recycle();
                if (getBgBitmap() != null) {
                    getBgBitmap().recycle();
                }


                bitmap = BitmapUtil.decodeSampledBitmapFromResource(
                        mContext.getResources(), R.drawable.paper, mWidth, mHeight);
                color = mContext.getResources().getColor(R.color.read_font_default);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_default));
                break;
            case Config.BOOK_BG_1:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_1));
                color = mContext.getResources().getColor(R.color.read_font_1);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_1));
                break;
            case Config.BOOK_BG_2:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_2));
                color = mContext.getResources().getColor(R.color.read_font_2);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_2));
                break;
            case Config.BOOK_BG_3:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_3));
                color = mContext.getResources().getColor(R.color.read_font_3);
                if (mBookPageWidget != null) {
                    mBookPageWidget.setBgColor(mContext.getResources().getColor(R.color.read_bg_3));
                }
                break;
            case Config.BOOK_BG_4:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_4));
                color = mContext.getResources().getColor(R.color.read_font_4);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_4));
                break;
        }

        setBgBitmap(bitmap);
        //设置字体颜色
        setTextColor(color);
    }

    private void calculateLineCount() {
        mLineCount = (int) (mVisibleHeight / (mFontSize + lineSpace));// 可显示的行数
    }

    private void drawStatus(Bitmap bitmap) {
        String status = "";
        switch (mStatus) {
            case OPENING:
                status = "正在打开书本...";
                break;
            case FAIL:
                status = "打开书本失败！";
                break;
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        waitPaint.setColor(getTextColor());
        waitPaint.setTextAlign(Paint.Align.CENTER);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
//        c.drawRect(targetRect, waitPaint);
        Paint.FontMetricsInt fontMetrics = waitPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        waitPaint.setTextAlign(Paint.Align.CENTER);
        c.drawText(status, targetRect.centerX(), baseline, waitPaint);
//        c.drawText("正在打开书本...", mHeight / 2, 0, waitPaint);
        mBookPageWidget.postInvalidate();

    }

    public void onDraw(Bitmap bitmap, Vector<String> m_lines) {


        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
//        word.setLength(0);
        mPaint.setTextSize(getFontSize());
        mPaint.setColor(getTextColor());

        if (m_lines.size() == 0) {
            return;
        }

        if (m_lines.size() > 0) {
            float y = marginHeight;
            for (String strLine : m_lines) {
                y += mFontSize + lineSpace;
                c.drawText(strLine, measureMarginWidth, y, mPaint);

            }
        }

        mBookPageWidget.postInvalidate();
    }


    /**
     * 打开书本
     *
     * @throws IOException
     */
    public void openBook(BookBean book) throws IOException {

        this.mCurrentBook = book;

        File book_file = new File(book.getPath());
        m_mbBufLen = (int) book_file.length();
        m_strCharsetName = book.getEncoding();


        //从数据库获取读取的进度
        BookDBManager bookDBManager = new BookDBManager();
        BookBean bookProgress = bookDBManager.getBookProgress(book);
        m_mbBufBegin = (int) bookProgress.getBegin();
        //m_mbBufEnd = (int) bookProgress.getEnd();


        randomFile = new RandomAccessFile(book_file, "r");
        m_mbBuf = randomFile.getChannel().map(
                FileChannel.MapMode.READ_ONLY, 0, m_mbBufLen);


        initBg();

        drawStatus(mBookPageWidget.getCurPage());
        drawStatus(mBookPageWidget.getNextPage());

        currentPage = getPageForBegin(m_mbBufBegin);

        if (mBookPageWidget != null) {
            currentPage(currentPage);
        }
    }

    public TRPage getPageForBegin(int begin) {
        m_mbBufBegin = begin;
        currentPage = new TRPage();
        currentPage.setBegin(begin);
        currentPage.setEnd(begin);
        currentPage.setLines(pageDown());
        currentPage.setEnd(m_mbBufEnd);
        return currentPage;
    }

    //绘制当前页面
    public void currentPage(TRPage currentPage) {
        onDraw(mBookPageWidget.getCurPage(), (Vector<String>) currentPage.getLines());
        onDraw(mBookPageWidget.getNextPage(), (Vector<String>) currentPage.getLines());
    }


    public void setBookPageBg(int color) {
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
        }
    }

    public TRPage getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(TRPage currentPage) {
        this.currentPage = currentPage;
    }

    public static Status getStatus() {
        return mStatus;
    }


    public void setM_mbBufEnd(int m_mbBufEnd) {
        this.m_mbBufEnd = m_mbBufEnd;
    }

    public int getM_mbBufBegin() {
        return m_mbBufBegin;
    }

    public void setM_mbBufBegin(int m_mbBufBegin) {
        this.m_mbBufBegin = m_mbBufBegin;
    }

    public int getM_mbBufEnd() {
        return m_mbBufEnd;
    }

    //是否是第一页
    public boolean isfirstPage() {
        return mIsfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return mIslastPage;
    }

    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //设置文字颜色
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.mTextColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.mFontSize;
    }

    public void setPageWidget(PageWidget mBookPageWidget) {
        this.mBookPageWidget = mBookPageWidget;
    }

    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
    }


    //改变字体大小
    public void changeFontSize(int fontSize) {
        this.mFontSize = fontSize;
        mPaint.setTextSize(mFontSize);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(m_mbBufBegin);
        currentPage(currentPage);
    }

    //改变字体
    public void changeTypeface(Typeface typeface) {
        this.typeface = typeface;
        mPaint.setTypeface(typeface);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(0);
        currentPage(currentPage);
    }

    //改变背景
    public void changeBookBg(int type) {
        setBookBg(type);
        currentPage = getPageForBegin(m_mbBufBegin);
        currentPage(currentPage);
    }

    protected byte[] readParagraphBack(int nFromPos) {
        int nEnd = nFromPos;
        int i;
        byte b0, b1;
        if (m_strCharsetName.equals("UTF-16LE")) {
            i = nEnd - 2;
            while (i > 0) {
                b0 = m_mbBuf.get(i);
                b1 = m_mbBuf.get(i + 1);
                if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
                    i += 2;
                    break;
                }
                i--;
            }

        } else if (m_strCharsetName.equals("UTF-16BE")) {
            i = nEnd - 2;
            while (i > 0) {
                b0 = m_mbBuf.get(i);
                b1 = m_mbBuf.get(i + 1);
                if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
                    i += 2;
                    break;
                }
                i--;
            }
        } else {
            i = nEnd - 1;
            while (i > 0) {
                b0 = m_mbBuf.get(i);
                if (b0 == 0x0a && i != nEnd - 1) {
                    i++;
                    break;
                }
                i--;
            }
        }
        if (i < 0)
            i = 0;
        int nParaSize = nEnd - i;
        int j;
        byte[] buf = new byte[nParaSize];
        for (j = 0; j < nParaSize; j++) {
            buf[j] = m_mbBuf.get(i + j);
        }
        return buf;
    }


    // 读取上一段落
    protected byte[] readParagraphForward(int nFromPos) {
        int nStart = nFromPos;
        int i = nStart;
        byte b0, b1;
        // 根据编码格式判断换行
        if (m_strCharsetName.equals("UTF-16LE")) {
            while (i < m_mbBufLen - 1) {
                b0 = m_mbBuf.get(i++);
                b1 = m_mbBuf.get(i++);
                if (b0 == 0x0a && b1 == 0x00) {
                    break;
                }
            }
        } else if (m_strCharsetName.equals("UTF-16BE")) {
            while (i < m_mbBufLen - 1) {
                b0 = m_mbBuf.get(i++);
                b1 = m_mbBuf.get(i++);
                if (b0 == 0x00 && b1 == 0x0a) {
                    break;
                }
            }
        } else {
            while (i < m_mbBufLen) {
                b0 = m_mbBuf.get(i++);
                if (b0 == 0x0a) {
                    break;
                }
            }
        }
        int nParaSize = i - nStart;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = m_mbBuf.get(nFromPos + i);
        }
        return buf;
    }

    public Vector<String> pageDown() {
        if (currentPage != null) {
            m_mbBufEnd = (int) currentPage.getEnd();
        }
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
            byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
            m_mbBufEnd += paraBuf.length;
            try {
                strParagraph = new String(paraBuf, m_strCharsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";
            if (strParagraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                strParagraph = strParagraph.replaceAll("\r\n", "");
            } else if (strParagraph.indexOf("\n") != -1) {
                strReturn = "\n";
                strParagraph = strParagraph.replaceAll("\n", "");
            }

            if (strParagraph.length() == 0) {
                lines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
                        null);
                lines.add(strParagraph.substring(0, nSize));
                strParagraph = strParagraph.substring(nSize);
                if (lines.size() >= mLineCount) {
                    break;
                }
            }
            if (strParagraph.length() != 0) {
                try {
                    m_mbBufEnd -= (strParagraph + strReturn)
                            .getBytes(m_strCharsetName).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }


    protected Vector<String> pageUp() {
        if (currentPage != null) {
            m_mbBufBegin = (int) currentPage.getBegin();
        } else {
            m_mbBufBegin = 0;
        }
        if (m_mbBufBegin < 0)
            m_mbBufBegin = 0;
        Vector<String> lines = new Vector<String>();
        String strParagraph = "";
        while (lines.size() < mLineCount && m_mbBufBegin > 0) {
            Vector<String> paraLines = new Vector<String>();
            byte[] paraBuf = readParagraphBack(m_mbBufBegin);
            m_mbBufBegin -= paraBuf.length;
            try {
                strParagraph = new String(paraBuf, m_strCharsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "");
            strParagraph = strParagraph.replaceAll("\n", "");

            if (strParagraph.length() == 0) {
                paraLines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
                        null);
                paraLines.add(strParagraph.substring(0, nSize));
                strParagraph = strParagraph.substring(nSize);
            }
            lines.addAll(0, paraLines);
        }
        while (lines.size() > mLineCount) {
            try {
                m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
                lines.remove(0);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public void prePage() throws IOException {
        if (m_mbBufBegin <= 0) {
            m_mbBufBegin = 0;
            mIsfirstPage = true;
            return;
        } else mIsfirstPage = false;
        cancelPage = currentPage;
        m_mbBufEnd = m_mbBufBegin;
        onDraw(mBookPageWidget.getCurPage(), (Vector<String>) currentPage.getLines());
        currentPage = getPrePage();
        onDraw(mBookPageWidget.getNextPage(), (Vector<String>) currentPage.getLines());
    }

    public void nextPage() throws IOException {

        if (m_mbBufEnd >= m_mbBufLen) {
            mIslastPage = true;
            return;
        } else mIslastPage = false;

        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), (Vector<String>) currentPage.getLines());
        m_mbBufBegin = m_mbBufEnd;
        currentPage = getNextPage();
        onDraw(mBookPageWidget.getNextPage(), (Vector<String>) currentPage.getLines());

    }


    public void clear() {

        mBookPageWidget = null;
        currentPage = null;
        BookDBManager bookDBManager = new BookDBManager();

        //把当前进度保存到数据库
        mCurrentBook.setBegin(m_mbBufBegin);
        // mCurrentBook.setEnd(m_mbBufEnd);
        bookDBManager.updateBookProgress(mCurrentBook);

        m_mbBufEnd = 0;
        mCurrentBook = null;

    }


    public void cancelPage() {
        currentPage = cancelPage;
        m_mbBufBegin = (int) currentPage.getBegin();
        m_mbBufEnd = (int) currentPage.getEnd();
    }


    public TRPage getNextPage() {
        TRPage trPage = new TRPage();
        trPage.setBegin(m_mbBufBegin);
        trPage.setLines(pageDown());
        trPage.setEnd(m_mbBufEnd);
        return trPage;
    }

    public TRPage getPrePage() {
        TRPage trPage = new TRPage();
        trPage.setEnd(m_mbBufEnd);
        trPage.setLines(pageUp());
        trPage.setBegin(m_mbBufBegin);
        return trPage;
    }


    private int end;

    public void getChapters(final OnChapterListener listener) {
        m_mbBufEnd = 0;
        final List<ChpaterBean> chapters = new ArrayList<>();
        try {
            listener.onStart();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (end < m_mbBufLen - 1) {
                        String regex = "第.{1,8}章.{0,}\r\n";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = null;
                        String strLine = getLineString();
                        matcher = pattern.matcher(strLine);
                        if (matcher.find()) {

                            int pos = end - getByteLength(strLine);
                            String title = matcher.group();
                            ChpaterBean chapter = new ChpaterBean();
                            chapter.setBookCatalogueStartPos(pos);
                            chapter.setName(title.replaceAll("\r\n", ""));
                            chapters.add(chapter);
                            listener.onLoading(chapter);
                        }
                    }
                    listener.onFinished(chapters);
                }
            }).start();
        } catch (Exception e) {

        }
    }

    private int getByteLength(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes(m_strCharsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes.length;
    }

    private String getLineString() {
        byte[] buf = readParagraphForward(end);
        end += buf.length;
        String s = "";
        try {
            s = new String(buf, m_strCharsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }


    public interface OnChapterListener {
        void onStart();

        void onLoading(ChpaterBean chapter);

        void onFinished(List<ChpaterBean> list);
    }


}
