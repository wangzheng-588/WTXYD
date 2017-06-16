package com.wz.wtxyd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookCat;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.di.component.DaggerBookLibraryComponent;
import com.wz.wtxyd.di.module.BookLibraryModule;
import com.wz.wtxyd.presenter.BookLibraryPresenter;
import com.wz.wtxyd.presenter.Contract.BookLibraryContract;
import com.wz.wtxyd.ui.adapter.BookCatAdapter;
import com.wz.wtxyd.ui.activity.BookListDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by wz on 17-6-6.
 */

public class BookLibraryFragment extends BaseFragment implements BookLibraryContract.View{
    @BindView(R.id.book_library_recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    BookLibraryPresenter mPresenter;
    private BookCatAdapter mBookCatAdapter;

    @Override
    protected void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),RecyclerView.VERTICAL));
        mBookCatAdapter = new BookCatAdapter();
        mRecyclerView.setAdapter(mBookCatAdapter);

        initListener();
    }

    private void initListener() {
        mBookCatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookCat bookCat = (BookCat) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(),BookListDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bookcat",bookCat);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

            }
        });
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_book_library;

    }

    @Override
    protected void setupAppComponent(AppComponent appComponent) {
        DaggerBookLibraryComponent.builder().appComponent(appComponent)
                .bookLibraryModule(new BookLibraryModule(this))
                .build().inject(this);

    }

    @Override
    protected void initData() {


        mPresenter.getBookCat();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showBookCat(List<BookCat> typeList) {

        mBookCatAdapter.addData(typeList);
    }

    @Override
    public void dismissProgress() {

    }
}
