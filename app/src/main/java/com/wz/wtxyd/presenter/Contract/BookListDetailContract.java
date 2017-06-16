package com.wz.wtxyd.presenter.Contract;

import com.wz.wtxyd.bean.PageBean;
import com.wz.wtxyd.ui.BaseView;

/**
 * Created by wz on 17-6-6.
 */


public interface BookListDetailContract {

    interface View extends BaseView{

        void showBookTypeDetail(PageBean typeList);

        void dismissProgress();

        void showMoreBookTypeDetail(PageBean pagebean);
    }
}
