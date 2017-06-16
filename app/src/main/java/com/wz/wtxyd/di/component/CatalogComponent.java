package com.wz.wtxyd.di.component;

import com.wz.wtxyd.di.module.CatalogModule;
import com.wz.wtxyd.ui.activity.BookScanResultActivity;
import com.wz.wtxyd.ui.activity.SearchLocalBookActivity;

import dagger.Component;

/**
 * Created by wz on 17-6-8.
 */


@Component(modules = CatalogModule.class)
public interface CatalogComponent {

    void inject(SearchLocalBookActivity activity);
    void inject(BookScanResultActivity activity);
}
