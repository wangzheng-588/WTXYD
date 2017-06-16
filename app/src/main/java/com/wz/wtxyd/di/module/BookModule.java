package com.wz.wtxyd.di.module;

import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.http.ApiService;
import com.wz.wtxyd.common.model.BookModel;
import com.wz.wtxyd.presenter.Contract.BookContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wz on 17-6-8.
 */

@Module
public class BookModule {

    BookContract.View mView;

    public BookModule(BookContract.View view) {
        mView = view;
    }

    @Provides
    BookModel provideModel(ApiService apiService, CacheProviders cacheProviders){
        return new BookModel(apiService,cacheProviders);
    }

    @Provides
    BookContract.View provideView(){
        return mView;
    }
}
