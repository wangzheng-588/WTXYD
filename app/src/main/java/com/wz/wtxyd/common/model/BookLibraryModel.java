package com.wz.wtxyd.common.model;

import com.wz.wtxyd.bean.BookCatBaseEntry;
import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.Contrast;
import com.wz.wtxyd.common.http.ApiService;

import io.reactivex.Observable;

/**
 * Created by wz on 17-6-6.
 */

public class BookLibraryModel extends BaseModel{


    public BookLibraryModel(ApiService apiService, CacheProviders cacheProviders) {
        super(apiService, cacheProviders);
    }


    public Observable<BookCatBaseEntry> getBookCat(){
        return mCacheProviders.getBookCat(mApiService.getBookCat(Contrast.SHOWAPI_APPID,Contrast.SHOWAPI_SIGN));
        //return mApiService.getBookCat(Contrast.SHOWAPI_APPID,Contrast.SHOWAPI_SIGN);
    }
}
