package com.wz.wtxyd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.di.component.AppComponent;

import butterknife.BindView;
import jaydenxiao.com.expandabletextview.ExpandableTextView;

/**
 * Created by wz on 17-6-6.
 */

public class BookDetailActivity extends BaseActivity{
    @BindView(R.id.iv_book_icon)
    ImageView mIvBookIcon;
    @BindView(R.id.tv_book_name)
    TextView mTvBookName;
    @BindView(R.id.tv_book_auther)
    TextView mTvBookAuther;
    @BindView(R.id.tv_book_type)
    TextView mTvBookType;
    @BindView(R.id.tv_book_size)
    TextView mTvBookSize;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.etv)
    ExpandableTextView mEtv;
    @BindView(R.id.tv_book_chapter)
    TextView mTvBookChapter;
    @BindView(R.id.tv_update_time)
    TextView mTvUpdateTime;
    @BindView(R.id.ll_book_catalog)
    LinearLayout mLlBookCatalog;
    @BindView(R.id.tv_read)
    TextView mTvRead;


    @Override
    protected void setupAppComponent(AppComponent appComponent) {

    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        final BookBean book = (BookBean) bundle.getSerializable("book");
        String name = book.getName();
        String author = book.getAuthor();
        String typeName = book.getTypeName();
        String size = book.getSize();
        int bookId = book.getId();

        mTvBookName.setText(name);
        mTvBookAuther.setText(author);
        mTvBookSize.setText(size);
        mTvBookType.setText(typeName);
        mToolbar.setTitle(name);

        mLlBookCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this,BookChapterActivity.class);
                intent.putExtra("bookid",book.getId());
                startActivity(intent);
            }
        });

        mTvRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this,BookReadAreaActivity.class);
                startActivity(intent);
            }
        });

    }


}
