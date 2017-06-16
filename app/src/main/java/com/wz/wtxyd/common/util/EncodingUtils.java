package com.wz.wtxyd.common.util;

import com.wz.wtxyd.bean.BookBean;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wz on 17-6-8.
 */

public class EncodingUtils {

    public static String getEncoding(BookBean book){
        UniversalDetector detector = new UniversalDetector(null);
        byte[] bytes = new byte[1024];
        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(book.getPath())));
            int length;
            while ((length = bufferedInputStream.read(bytes)) > 0){
                detector.handleData(bytes,0,length);
            }
            detector.dataEnd();
            bufferedInputStream.close();
        }catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }
        return detector.getDetectedCharset();
    }
    public static int getPXWithDP(int dp){
       // float density = MyApplication.getGlobalContext().getResources().getDisplayMetrics().density;
        return DensityUtil.px2dip(dp);
    }
}
