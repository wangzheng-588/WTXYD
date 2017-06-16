package com.wz.wtxyd.common.model;

import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.http.ApiService;

/**
 * Created by wz on 17-6-7.
 */

public class BaseModel {

    ApiService mApiService;
    CacheProviders mCacheProviders;

    public BaseModel(ApiService apiService, CacheProviders cacheProviders) {
        mApiService = apiService;
        mCacheProviders = cacheProviders;
    }

    public void onDestory(){
        mApiService = null;
        mCacheProviders = null;
    }
}
