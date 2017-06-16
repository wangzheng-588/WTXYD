package com.wz.wtxyd.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookCat;

/**
 * Created by wz on 17-6-6.
 */

public class BookCatAdapter extends BaseQuickAdapter<BookCat,BaseViewHolder> {


    public BookCatAdapter() {
        super(R.layout.item_book_cat);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookCat item) {
        helper.setText(R.id.tv_book_cat,item.getName());
    }


}
