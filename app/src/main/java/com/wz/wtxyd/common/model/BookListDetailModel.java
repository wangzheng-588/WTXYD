package com.wz.wtxyd.common.model;

import android.util.Log;

import com.wz.wtxyd.bean.PageBaseEntry;
import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.Contrast;
import com.wz.wtxyd.common.http.ApiService;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKeyGroup;

/**
 * Created by wz on 17-6-6.
 */

public class BookListDetailModel extends BaseModel{

    public BookListDetailModel(ApiService apiService, CacheProviders cacheProviders) {
        super(apiService, cacheProviders);
    }


//    public Observable<PageBaseEntry> getBookTypeDetail(int typeID){
//        return mApiService.getBookTypeDetail(Contrast.SHOWAPI_APPID,Contrast.SHOWAPI_SIGN,typeID);
//    }

    public Observable<PageBaseEntry> getMoreBookTypeDetail(int typeID,int page,boolean update){
        Log.e("TAG","当前面是："+page);
        return mCacheProviders
                .getBookList(mApiService.getMoreBookTypeDetail(Contrast.SHOWAPI_APPID,Contrast.SHOWAPI_SIGN,typeID,page),new DynamicKeyGroup(typeID,page));
        //return mApiService.getMoreBookTypeDetail(Contrast.SHOWAPI_APPID,Contrast.SHOWAPI_SIGN,typeID,page);
    }
}
