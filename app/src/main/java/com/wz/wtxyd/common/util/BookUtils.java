package com.wz.wtxyd.common.util;

import android.util.Log;

import com.wz.wtxyd.bean.BookBean;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wz on 17-6-13.
 */

public class BookUtils {



    private int mStartPos = 0;
    private MappedByteBuffer m_mbBuf = null;
    private int m_mbBufLen = 0;
    private String m_strCharsetName = "GBK";
    private RandomAccessFile randomFile;


    //读取目录
    public void getChapter(final BookBean book) throws IOException {
        File file = new File(book.getPath());

        randomFile = new RandomAccessFile(book.getPath(), "r");

        m_mbBuf = randomFile.getChannel().map(
                FileChannel.MapMode.READ_ONLY, 0, file.length());
        m_mbBufLen = (int) file.length();
        m_strCharsetName = book.getEncoding();
      //  mMLineCount = instance.getLineCount();

        showGraph();

//
//        new Thread(){
//            @Override
//            public void run() {
//                pageDown();
//            }
//        }.start();



//        new Thread() {
//
//            private BufferedReader mBufferedReader;
//
//            @Override
//            public void run() {
//
//                try {
//                    String bookPath = book.getPath();
//
//                    String m_strCharsetName = book.getEncoding();
//                    if (TextUtils.isEmpty(m_strCharsetName)) {
//                        m_strCharsetName = EncodingUtils.getEncoding(book);
//                    }
//
//                    File file = new File(bookPath);
//                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file), m_strCharsetName);
//                    mBufferedReader = new BufferedReader(reader);
//                    String str = "";
//                    int len = 0;
////        char[] chars = new char[4096];
//                    while ((str = mBufferedReader.readLine()) != null) {
//                       // len +=str.length();
//                        int currentLen = len;
//                        byte[] bytes = str.getBytes();
//                        int length = bytes.length;
//                        len+=length;
//                        //Log.e("TAG",length+"");
//                        if (str.length() <= 30 && (str.matches(".*第.{1,8}章.*") || str.matches(".*第.{1,8}节.*"))) {
//
//                           // ChpaterBean bookCatalogue = new ChpaterBean();
//                           // bookCatalogue.setName(str);
//                           // directoryList.add(bookCatalogue);
//                            //Log.e("TAG",str);
//                            //int i = currentLen - (str.length());
//                            Log.e("TAG","当前进度是："+len);
//                        }
//
//                        if (str.contains("\r\n")) {
//                            len += str.length() + 2;
//                        }else if (str.contains("\u0000")){
//                            len += str.length() + 1;
//                        }else {
//                            len += str.length();
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (mBufferedReader!=null)
//                            mBufferedReader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }.start();
//    }




}

//
//
//    public void pageDown() {
//        int currentProgress;
//        int preProgress;
//        String strParagraph = "";
//        //Vector<String> lines = new Vector<>();
//        while ( m_mbBufEnd < mM_mbBufLen) {
//
//            currentProgress = m_mbBufEnd;
//            if (m_mbBufEnd-currentProgress>1000){
//                byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
//                m_mbBufEnd += paraBuf.length;
//                try {
//                    strParagraph = new String(paraBuf, mM_strCharsetName);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                String strReturn = "";
//                if (strParagraph.indexOf("\r\n") != -1) {
//                    strReturn = "\r\n";
//                    strParagraph = strParagraph.replaceAll("\r\n", "");
//                } else if (strParagraph.indexOf("\n") != -1) {
//                    strReturn = "\n";
//                    strParagraph = strParagraph.replaceAll("\n", "");
//                }
//
//                if (strParagraph.length() == 0) {
//                    // lines.add(strParagraph);
//                }
//                while (strParagraph.length() > 0) {
//                    int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
//                            null);
//                    String substring = strParagraph.substring(0, nSize);
//
//                    if (substring.length() <= 30 && (substring.matches(".*第.{1,8}章.*") || substring.matches(".*第.{1,8}节.*"))) {
//
//
//
//                    }
//
//                    //  lines.add(substring);
//                    strParagraph = strParagraph.substring(nSize);
//                }
//                if (strParagraph.length() != 0) {
//                    try {
//                        m_mbBufEnd -= (strParagraph + strReturn)
//                                .getBytes(mM_strCharsetName).length;
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//        }
//
//    }
//
//
//    protected byte[] readParagraphForward(int nFromPos) {
//        int nStart = nFromPos;
//        int i = nStart;
//        byte b0, b1;
//        // 根据编码格式判断换行
//        if (mM_strCharsetName.equals("UTF-16LE")) {
//            while (i < mM_mbBufLen - 1) {
//                b0 = m_mbBuf.get(i++);
//                b1 = m_mbBuf.get(i++);
//                if (b0 == 0x0a && b1 == 0x00) {
//                    break;
//                }
//            }
//        } else if (mM_strCharsetName.equals("UTF-16BE")) {
//            while (i < mM_mbBufLen - 1) {
//                b0 = m_mbBuf.get(i++);
//                b1 = m_mbBuf.get(i++);
//                if (b0 == 0x00 && b1 == 0x0a) {
//                    break;
//                }
//            }
//        } else {
//            while (i < mM_mbBufLen) {
//                b0 = m_mbBuf.get(i++);
//                if (b0 == 0x0a) {
//                    break;
//                }
//            }
//        }
//        int nParaSize = i - nStart;
//        byte[] buf = new byte[nParaSize];
//        for (i = 0; i < nParaSize; i++) {
//            buf[i] = m_mbBuf.get(nFromPos + i);
//        }
//        return buf;
//    }













    // 读取一段落
    protected byte[] readParagraphForward(int nFromPos) {
        int nStart = nFromPos;
        int i = nStart;
        byte b0, b1;
        // 根据编码格式判断换行
        if (m_strCharsetName.equals("UTF-16LE")){
            while (i < m_mbBufLen - 1){
                b0 = m_mbBuf.get(i++);
                b1 = m_mbBuf.get(i++);
                if (b0 == 0x0a && b1 == 0x00){
                    break;
                }
            }
        } else if (m_strCharsetName.equals("UTF-16BE")){
            while (i < m_mbBufLen - 1){
                b0 = m_mbBuf.get(i++);
                b1 = m_mbBuf.get(i++);
                if (b0 == 0x00 && b1 == 0x0a){
                    break;
                }
            }
        } else {
            while (i < m_mbBufLen){
                b0 = m_mbBuf.get(i++);
                if (b0 == 0x0a){
                    break;
                }
            }
        }
        int nParaSize = i - nStart;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++){
            buf[i] = m_mbBuf.get(nFromPos + i);
        }
        return buf;
    }
    private String getNextString(){
        byte[] buf = readParagraphForward(mStartPos);
        mStartPos+=buf.length;
        String s = "";
        try {
            s = new String(buf,m_strCharsetName);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return s;
    }


    public void showGraph(){
        int flag = 0;
        String strLine = "";
        String content = "";
        String title = "";
        String regex = "第.{1,8}章.{0,}\r\n";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strLine);
        while(mStartPos<m_mbBufLen-1){
            while(strLine.length()<1000&&mStartPos<m_mbBufLen-1)
                strLine+=getNextString();
           // matcher = pattern.matcher(strLine);
            if(strLine.matches(".*第.{1,8}章.*") || strLine.matches(".*第.{1,8}节.*")) {
                if(flag==0){
                    content  = strLine;
                    title = matcher.group();
                    Log.e("TAG",title);
                    flag=1;
                }else{
                    content+=strLine.substring(0, matcher.start());
                    title = matcher.group();
                    content = strLine.substring(matcher.start(),strLine.length());
                    Log.e("TAG",title);
                }
            }else
                content+=strLine;
            strLine="";
        }
    }


}
