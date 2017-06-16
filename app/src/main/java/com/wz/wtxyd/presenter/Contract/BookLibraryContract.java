package com.wz.wtxyd.presenter.Contract;

import com.wz.wtxyd.bean.BookCat;
import com.wz.wtxyd.ui.BaseView;

import java.util.List;

/**
 * Created by wz on 17-6-6.
 */

public interface BookLibraryContract {

    interface View extends BaseView {

        void showBookCat(List<BookCat> typeList);

        void dismissProgress();
    }
}
