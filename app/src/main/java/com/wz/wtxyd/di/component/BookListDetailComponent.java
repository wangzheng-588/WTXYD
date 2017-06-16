package com.wz.wtxyd.di.component;

import com.wz.wtxyd.di.FragmentScope;
import com.wz.wtxyd.di.module.BookListDetailModule;
import com.wz.wtxyd.ui.activity.BookListDetailActivity;

import dagger.Component;

/**
 * Created by wz on 17-6-6.
 */

@FragmentScope
@Component(modules = BookListDetailModule.class,dependencies = AppComponent.class)
public interface BookListDetailComponent {

    void inject(BookListDetailActivity activity);
}
