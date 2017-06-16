package com.wz.wtxyd.di.module;

import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.http.ApiService;
import com.wz.wtxyd.common.model.BookLibraryModel;
import com.wz.wtxyd.presenter.Contract.BookLibraryContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wz on 17-6-6.
 */

@Module
public class BookLibraryModule {

    BookLibraryContract.View mView;

    public BookLibraryModule(BookLibraryContract.View view) {
        mView = view;
    }

    @Provides
    public BookLibraryModel provideModel(ApiService apiService, CacheProviders cacheProviders){
        return new BookLibraryModel(apiService,cacheProviders);
    }

    @Provides
    public BookLibraryContract.View provideView(){
        return mView;
    }
}
