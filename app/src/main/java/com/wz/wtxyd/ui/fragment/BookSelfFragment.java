package com.wz.wtxyd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.common.util.ToastUtil;
import com.wz.wtxyd.data.db.BookDBManager;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.presenter.Contract.BookContract;
import com.wz.wtxyd.ui.activity.BookReadAreaActivity;
import com.wz.wtxyd.ui.adapter.BookSelfAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wz on 17-6-6.
 */

public class BookSelfFragment extends BaseFragment implements BookContract.BookView {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_edit_menu)
    LinearLayout mLlEditMenu;
    @BindView(R.id.tv_all_selecter)
    TextView mTvAllSelecter;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.tv_top)
    TextView mTvTop;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;


    private boolean isEditState;//是否是编辑状态
    private BookSelfAdapter mAdapter;
    private List<BookBean> mDeleteBooks;


    @Override
    public void onResume() {
        super.onResume();
        BookDBManager bookDBManager = new BookDBManager();
        mAdapter.getData().clear();
        List<BookBean> bookList = bookDBManager.getBookList();
        if (bookList != null && bookList.size() > 0) {

            mAdapter.addData(bookList);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    public boolean isEditState() {
        return isEditState;
    }

    @Override
    protected void init() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        mAdapter = BookSelfAdapter.builder().showCheckbox(false).build();


        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                mAdapter.getBuilder().showCheckbox(true);
                mAdapter.getBuilder().showUnread(false);
                mAdapter.notifyItemRangeChanged(0, adapter.getData().size());
                isEditState = true;
                mLlEditMenu.setVisibility(View.VISIBLE);
                mDeleteBooks = new ArrayList<>();
                return true;
            }
        });


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (isEditState) {
                    List<BookBean> data = mAdapter.getData();
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_self);
                    //设置书架checkbox选中状态
                    BookBean bookBean = data.get(position);

                    if (checkBox.isChecked()) {
                        bookBean.setDeleteState(false);
                        mDeleteBooks.remove(bookBean);
                        checkBox.setChecked(false);
                    } else {
                        bookBean.setDeleteState(false);
                        mDeleteBooks.remove(bookBean);
                        bookBean.setDeleteState(true);
                        mDeleteBooks.add(bookBean);
                        checkBox.setChecked(true);
                    }


                } else {

                    Intent intent = new Intent(getActivity(), BookReadAreaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", (BookBean) (adapter.getData().get(position)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected int setLayoutResID() {
        return R.layout.fragment_book_self;
    }

    @Override
    protected void setupAppComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showBook() {

    }


    public void hideEditMenu() {
        //取消数据中的编辑状态，设置为false
        List<BookBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setDeleteState(false);
        }


        mLlEditMenu.setVisibility(View.GONE);
        if (mDeleteBooks!=null){

            mDeleteBooks.clear();
        }
        mAdapter.getBuilder().showCheckbox(false);
        mAdapter.getBuilder().setChecked(false);
        mAdapter.notifyDataSetChanged();
        isEditState = false;
    }


    @OnClick({R.id.tv_all_selecter, R.id.tv_delete, R.id.tv_top, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all_selecter:
                mDeleteBooks.clear();
                List<BookBean> data = mAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setDeleteState(true);
                    mDeleteBooks.add(data.get(i));
                }
                mAdapter.getBuilder().setChecked(true);
                mAdapter.notifyDataSetChanged();
               // Log.e("TAG", mDeleteBooks.size() + "");
                break;
            case R.id.tv_delete:

                BookDBManager bookDBManager = new BookDBManager();
                int i = bookDBManager.deleteBooks(mDeleteBooks);
                if (i>0){
                    mAdapter.getData().removeAll(mDeleteBooks);
                    mAdapter.notifyDataSetChanged();
                    hideEditMenu();
                    ToastUtil.show("删除成功！");
                }


                break;
            case R.id.tv_top:
                break;
            case R.id.tv_cancel:
                hideEditMenu();
                break;
        }
    }
}
