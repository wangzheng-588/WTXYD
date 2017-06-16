package com.wz.wtxyd.di.component;


import com.wz.wtxyd.AppApplication;
import com.wz.wtxyd.cache.CacheProviders;
import com.wz.wtxyd.common.http.ApiService;
import com.wz.wtxyd.di.module.AppModule;
import com.wz.wtxyd.di.module.CacheModule;
import com.wz.wtxyd.di.module.HttpModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wz on 17-6-3.
 */


@Component(modules = {AppModule.class, HttpModule.class, CacheModule.class})
@Singleton
public interface AppComponent {

    ApiService getApiService();

    CacheProviders getCacheProviders();

    AppApplication getApplication();
}
