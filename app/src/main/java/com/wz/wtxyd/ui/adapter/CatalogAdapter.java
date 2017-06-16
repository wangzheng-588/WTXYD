package com.wz.wtxyd.ui.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.FileEntry;
import com.wz.wtxyd.common.util.MD5Util;
import com.wz.wtxyd.data.db.BookDBManager;

/**
 * Created by wz on 17-6-8.
 */

public class CatalogAdapter extends BaseQuickAdapter<FileEntry, BaseViewHolder> {

    private Builder mBuilder;
    private final BookDBManager mBookDBManager;

    public static Builder builder() {
        return new Builder();
    }

    private CatalogAdapter(Builder builder) {
        super(R.layout.item_catalog);
        this.mBuilder = builder;
        mBookDBManager = new BookDBManager();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FileEntry item) {

        final CheckBox checkBox = helper.getView(R.id.cb_file);
        TextView tvImport = helper.getView(R.id.tv_is_import);

        if (item.isFile()) {
            int bookCount = mBookDBManager.getBookCount(MD5Util.MD5(item.getPath()));

            helper.getView(R.id.tv_file_type).setBackgroundResource(R.drawable.file_icon_default);
            helper.getView(R.id.cb_file).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_next_catalog).setVisibility(View.GONE);

            helper.itemView.setClickable(bookCount <= 0);
            tvImport.setVisibility(bookCount > 0 ? View.VISIBLE : View.GONE);

           if (item.getPath().endsWith("txt")){

               checkBox.setVisibility(bookCount > 0 ? View.GONE : View.VISIBLE);
           } else {
               checkBox.setVisibility(View.GONE);
           }

            // CheckBox view = helper.getView(R.id.cb_file);

            checkBox.setChecked(item.isCheck());
//            checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    item.setCheck(checkBox.isChecked());
//                }
//            });

        } else {
            helper.getView(R.id.tv_file_type).setBackgroundResource(R.drawable.folder_icon_default);
            helper.getView(R.id.cb_file).setVisibility(View.GONE);
            helper.getView(R.id.iv_next_catalog).setVisibility(View.VISIBLE);
            tvImport.setVisibility(View.GONE);
            helper.setText(R.id.tv_child_num, item.getChildSize() + "项");
        }
        String path = item.getPath();
        // Log.e("TAG",path.substring(path.lastIndexOf("/")));
        helper.setText(R.id.tv_catalog, path.substring(path.lastIndexOf("/") + 1));
    }


    public static class Builder {
        private boolean isDisplayImport;

        public Builder showImport(boolean isDisplayImport) {
            this.isDisplayImport = isDisplayImport;
            return this;
        }

        public CatalogAdapter build() {
            return new CatalogAdapter(this);
        }

    }
}
