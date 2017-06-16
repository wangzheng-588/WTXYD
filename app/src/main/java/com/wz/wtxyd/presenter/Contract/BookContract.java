package com.wz.wtxyd.presenter.Contract;

import com.wz.wtxyd.bean.ChpaterBean;
import com.wz.wtxyd.ui.BaseView;

import java.util.List;

/**
 * Created by wz on 17-6-8.
 */

public class BookContract {
    public interface View extends BaseView {

        void showChapter(List<ChpaterBean> chpaterBeen);

        void dismissProgress();
    }

    public interface BookView extends BaseView{
        void showBook();
    }


}
