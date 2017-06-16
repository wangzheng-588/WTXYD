package com.wz.wtxyd.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.ChpaterBean;

/**
 * Created by wz on 17-6-13.
 */

public class ChapterAdapter extends BaseQuickAdapter<ChpaterBean,BaseViewHolder> {

    private int mCurrentChapter;

    public void setCurrentChapter(int currentChapter) {
        mCurrentChapter = currentChapter;
    }

    public ChapterAdapter() {
        super(R.layout.item_chapter);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChpaterBean item) {

        TextView textView = helper.getView(R.id.tv_chapter_name);
        int position = helper.getLayoutPosition();
        if (mCurrentChapter == position){
            textView.setTextColor(mContext.getResources().getColor(R.color.md_red_600));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.md_black_1000));
        }

        helper.setText(R.id.tv_chapter_name,item.getName());

    }


}
