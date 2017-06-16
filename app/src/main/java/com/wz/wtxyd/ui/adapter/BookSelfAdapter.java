package com.wz.wtxyd.ui.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;

/**
 * Created by wz on 17-6-8.
 */

public class BookSelfAdapter extends BaseQuickAdapter<BookBean, BaseViewHolder> {

    private Builder mBuilder;


    public static Builder builder() {
        return new Builder();
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    private BookSelfAdapter(Builder builder) {
        super(R.layout.item_book_self);
        mBuilder = builder;
    }

    @Override
    protected void convert(final BaseViewHolder helper, BookBean item) {
        helper.getView(R.id.cb_self).setVisibility(mBuilder.isDisplayCheckbox ? View.VISIBLE : View.GONE);
        helper.getView(R.id.tv_is_read).setVisibility(mBuilder.isDisplayCheckbox ? View.GONE : View.VISIBLE);

        helper.setText(R.id.tv_title, item.getName())
                .setText(R.id.tv_auth, "本地小说");

        CheckBox checkBox = helper.getView(R.id.cb_self);
        checkBox.setChecked(item.isDeleteState());
    }

    public static class Builder {
        private boolean isDisplayCheckbox;
        private boolean isDisplayUnread;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Builder showCheckbox(boolean isDisplayCheckbox) {
            this.isDisplayCheckbox = isDisplayCheckbox;
            return this;
        }

        public Builder showUnread(boolean isDisplayUnread) {
            this.isDisplayUnread = isDisplayUnread;
            return this;
        }


        public BookSelfAdapter build() {
            return new BookSelfAdapter(this);
        }
    }
}
