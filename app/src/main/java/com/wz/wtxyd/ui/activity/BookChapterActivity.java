package com.wz.wtxyd.ui.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.ChpaterBean;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.di.component.DaggerBookComponent;
import com.wz.wtxyd.di.module.BookModule;
import com.wz.wtxyd.presenter.Contract.BookContract;
import com.wz.wtxyd.presenter.BookPresenter;
import com.wz.wtxyd.ui.adapter.BookChapterAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by wz on 17-6-8.
 */

public class BookChapterActivity extends BaseActivity implements BookContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    BookPresenter mPresenter;
    private BookChapterAdapter mAdapter;

    @Override
    protected void setupAppComponent(AppComponent appComponent) {
        DaggerBookComponent.builder().appComponent(appComponent)
                .bookModule(new BookModule(this))
                .build().inject(this);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_book_chapter;
    }

    @Override
    protected void init() {

        int bookid = getIntent().getIntExtra("bookid", 0);

        mPresenter.getBookChapterList(bookid);
        mAdapter = new BookChapterAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void showChapter(List<ChpaterBean> chpaterBeen) {
        mAdapter.addData(chpaterBeen);
    }

    @Override
    public void dismissProgress() {

    }
}
