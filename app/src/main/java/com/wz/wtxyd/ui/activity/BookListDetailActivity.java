package com.wz.wtxyd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.BookCat;
import com.wz.wtxyd.bean.PageBean;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.di.component.DaggerBookListDetailComponent;
import com.wz.wtxyd.di.module.BookListDetailModule;
import com.wz.wtxyd.presenter.BookListDetailPresenter;
import com.wz.wtxyd.presenter.Contract.BookListDetailContract;
import com.wz.wtxyd.ui.adapter.BookTypeDetailAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by wz on 17-6-6.
 */

public class BookListDetailActivity extends BaseActivity implements BookListDetailContract.View {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.material_refresh)
    MaterialRefreshLayout mRefreshLayout;

    private int mPage = 1;
    private int mTypeID;

    @Inject
    BookListDetailPresenter mPresenter;
    private BookTypeDetailAdapter mAdapter;
    private int mAllPage;

    @Override
    protected void setupAppComponent(AppComponent appComponent) {
        DaggerBookListDetailComponent.builder().appComponent(appComponent)
                .bookListDetailModule(new BookListDetailModule(this))
                .build().inject(this);

    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_book_type_detail;
    }

    @Override
    protected void init() {
        mRefreshLayout.setLoadMore(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        Bundle bundle = getIntent().getExtras();
        BookCat bookcat = (BookCat) bundle.getSerializable("bookcat");
        mTypeID = bookcat.getId();
        mPresenter.getMoreBookTypeDetail(mTypeID,1,false,false);

        initListener();
    }

    private void initListener() {
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mPresenter.getMoreBookTypeDetail(mTypeID,1,true,false);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                mPage++;
                if (mPage <= mAllPage) {
                    mPresenter.getMoreBookTypeDetail(mTypeID, mPage,false,true);
                }
            }
        });


    }

    private void initAdapter() {

        mAdapter = new BookTypeDetailAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookBean bookBean = mAdapter.getData().get(position);
                Intent intent = new Intent(BookListDetailActivity.this, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", bookBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showBookTypeDetail(PageBean typeList) {
        mPage = 1;
        initAdapter();
        mAllPage = typeList.getAllPages();
        mAdapter.addData(typeList.getContentlist());
    }

    @Override
    public void dismissProgress() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishRefreshLoadMore();
    }

    @Override
    public void showMoreBookTypeDetail(PageBean pagebean) {
        mAdapter.addData(pagebean.getContentlist());
    }
}
