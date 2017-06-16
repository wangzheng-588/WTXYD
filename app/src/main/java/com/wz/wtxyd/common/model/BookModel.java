package com.wz.wtxyd.common.model;

import com.wz.wtxyd.bean.ChapterBaseEntry;
import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.Contrast;
import com.wz.wtxyd.common.http.ApiService;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;

/**
 * Created by wz on 17-6-8.
 */

public class BookModel extends BaseModel {

    public BookModel(ApiService apiService, CacheProviders cacheProviders) {
        super(apiService, cacheProviders);
    }

    public Observable<ChapterBaseEntry> getChapter(int bookid){
        return mCacheProviders.getBookChpater(mApiService.getBookChpater(Contrast.SHOWAPI_APPID,Contrast.SHOWAPI_SIGN,bookid),new DynamicKey(bookid));
    }

    public void getBookChapterContent(int bookid, int cid) {

    }

    //搜索书籍
    public void searchBook() {

    }
}
