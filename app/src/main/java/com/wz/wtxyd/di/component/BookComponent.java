package com.wz.wtxyd.di.component;

import com.wz.wtxyd.di.FragmentScope;
import com.wz.wtxyd.di.module.BookModule;
import com.wz.wtxyd.ui.activity.BookChapterActivity;

import dagger.Component;

/**
 * Created by wz on 17-6-8.
 */


@FragmentScope
@Component(modules = BookModule.class,dependencies = AppComponent.class)
public interface BookComponent {

    void inject(BookChapterActivity activity);
}
