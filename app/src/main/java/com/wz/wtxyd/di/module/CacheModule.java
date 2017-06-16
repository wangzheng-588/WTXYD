package com.wz.wtxyd.di.module;

import com.google.gson.Gson;
import com.wz.wtxyd.cache.CacheProviders;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by wz on 17-6-7.
 */

@Module
public class CacheModule {


    @Provides
    @Singleton
    RxCache provideRxCache(File cacheFile,Gson gson){

        return new RxCache.Builder().persistence(cacheFile,new GsonSpeaker(gson));

    }


    @Provides
    @Singleton
    CacheProviders provideCacheProviders(RxCache rxCache){
        return rxCache.using(CacheProviders.class);
    }

}
