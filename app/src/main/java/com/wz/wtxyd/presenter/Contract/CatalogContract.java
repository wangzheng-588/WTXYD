package com.wz.wtxyd.presenter.Contract;

import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.FileEntry;
import com.wz.wtxyd.ui.BaseView;

import java.util.List;

/**
 * Created by wz on 17-6-8.
 */

public class CatalogContract {

    public interface CatalogView extends BaseView {
        void showCatalog(List<FileEntry> list);

        void showBooks(List<BookBean> bookBeen);

        void dismissProgress();

        void showFiles(List<FileEntry> files);
    }
}
