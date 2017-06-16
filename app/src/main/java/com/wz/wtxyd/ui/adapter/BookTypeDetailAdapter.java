package com.wz.wtxyd.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;

/**
 * Created by wz on 17-6-6.
 */

public class BookTypeDetailAdapter extends BaseQuickAdapter<BookBean,BaseViewHolder>{
    public BookTypeDetailAdapter() {
        super(R.layout.item_book_library);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookBean item) {
        helper.setText(R.id.tv_title,item.getName())
                .setText(R.id.tv_content,item.getNewChapter())
                .setText(R.id.tv_auth,item.getAuthor())
                .setText(R.id.tv_update_time,item.getUpdateTime());
    }
}
