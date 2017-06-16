package com.wz.wtxyd.di.module;

import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.http.ApiService;
import com.wz.wtxyd.common.model.BookListDetailModel;
import com.wz.wtxyd.presenter.Contract.BookListDetailContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wz on 17-6-6.
 */

@Module
public class BookListDetailModule {

    BookListDetailContract.View mView;

    public BookListDetailModule(BookListDetailContract.View view) {
        mView = view;
    }

    @Provides
    public BookListDetailModel provideModel(ApiService apiService, CacheProviders cacheProviders){
        return new BookListDetailModel(apiService,cacheProviders);
    }

    @Provides
    public BookListDetailContract.View provideView(){
        return mView;
    }
}
