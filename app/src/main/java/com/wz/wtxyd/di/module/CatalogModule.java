package com.wz.wtxyd.di.module;

import com.wz.wtxyd.common.model.CatalogModel;
import com.wz.wtxyd.presenter.Contract.CatalogContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wz on 17-6-8.
 */

@Module
public class CatalogModule {

    CatalogContract.CatalogView mCatalogView;

    public CatalogModule(CatalogContract.CatalogView catalogView) {
        mCatalogView = catalogView;
    }

    @Provides
    CatalogModel provideModel(){
        return new CatalogModel();
    }

    @Provides
    CatalogContract.CatalogView provideView(){
        return mCatalogView;
    }
}
