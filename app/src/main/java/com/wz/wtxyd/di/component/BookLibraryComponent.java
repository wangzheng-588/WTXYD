package com.wz.wtxyd.di.component;

import com.wz.wtxyd.di.FragmentScope;
import com.wz.wtxyd.di.module.BookLibraryModule;
import com.wz.wtxyd.ui.fragment.BookLibraryFragment;

import dagger.Component;

/**
 * Created by wz on 17-6-6.
 */

@FragmentScope
@Component(modules = BookLibraryModule.class,dependencies = AppComponent.class)
public interface BookLibraryComponent {
    void inject(BookLibraryFragment fragment);
}
