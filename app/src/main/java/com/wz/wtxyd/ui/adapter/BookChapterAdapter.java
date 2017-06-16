package com.wz.wtxyd.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.ChpaterBean;

/**
 * Created by wz on 17-6-8.
 */

public class BookChapterAdapter extends BaseQuickAdapter<ChpaterBean,BaseViewHolder> {
    public BookChapterAdapter() {
        super(R.layout.item_book_chapter);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChpaterBean item) {
        helper.setText(R.id.tv_chapter,item.getName());
    }
}
