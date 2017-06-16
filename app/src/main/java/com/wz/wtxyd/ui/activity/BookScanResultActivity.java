package com.wz.wtxyd.ui.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wz.wtxyd.R;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.FileEntry;
import com.wz.wtxyd.common.util.EncodingUtils;
import com.wz.wtxyd.common.util.MD5Util;
import com.wz.wtxyd.common.util.ToastUtil;
import com.wz.wtxyd.data.db.BookDBManager;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.di.component.DaggerCatalogComponent;
import com.wz.wtxyd.di.module.CatalogModule;
import com.wz.wtxyd.presenter.CatalogPresenter;
import com.wz.wtxyd.presenter.Contract.CatalogContract;
import com.wz.wtxyd.ui.adapter.CatalogAdapter;
import com.wz.wtxyd.ui.widget.PageFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by wz on 17-6-9.
 */

public class BookScanResultActivity extends BaseActivity implements CatalogContract.CatalogView {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_import)
    TextView mTvImport;

   // BookSelfAdapter mAdapter;
    CatalogAdapter mAdapter;
    @Inject
    CatalogPresenter mPresenter;
    private int mSelectedNum;

    private ArrayList<FileEntry> mFiles = new ArrayList<>();
    private BookDBManager mBookDBManager;


    @Override
    protected void setupAppComponent(AppComponent appComponent) {
        DaggerCatalogComponent.builder().catalogModule(new CatalogModule(this))
                .build().inject(this);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_book_scan_result;
    }

    @Override
    protected void init() {

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
      //  mAdapter = BookSelfAdapter.builder().showCheckbox(true).build();
        mAdapter = new CatalogAdapter.Builder().showImport(false).build();

        mPresenter.searchFile(path, "txt");
       // mPresenter.searchBook(path, "txt");

        mBookDBManager = new BookDBManager();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileEntry item = (FileEntry) adapter.getItem(position);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_file);
                if (checkBox.isChecked()) {
                    item.setCheck(false);
                    checkBox.setChecked(false);
                    if (mSelectedNum > 0) {
                        mSelectedNum--;
                        if (mFiles != null && mFiles.size() > 0) {
                            mFiles.remove(item);
                        }
                    }
                } else {
                    checkBox.setChecked(true);
                    item.setCheck(true);
                    mSelectedNum++;
                    if (mFiles != null) {
                        mFiles.add(item);
                    }
                }
            }

        });

        //导入书架
        mTvImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PageFactory instance = PageFactory.createPageFactory(BookScanResultActivity.this);
                ArrayList<BookBean> books = new ArrayList<>();
                BookBean book = null;
                ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
                for (int i = 0; i < mFiles.size(); i++) {
                    FileEntry fileEntry = mFiles.get(i);
                    File file = new File(fileEntry.getPath());
                    book = new BookBean();
                    book.setPath(file.getPath());
                    book.setPathMD5(MD5Util.MD5(file.getPath()));
                    String encoding = EncodingUtils.getEncoding(book);
                    book.setEncoding(encoding);
                    book.setSize(file.length()+"");
                    book.setName(file.getName());

                    books.add(book);
                }

                if (books.size() > 0) {
                    //存入到数据库

                    mBookDBManager.insertBookList(books);
                    finish();
                }

            }
        });
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showCatalog(List<FileEntry> list) {

    }

    @Override
    public void showBooks(final List<BookBean> bookBeen) {

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.getData().clear();
//                mAdapter.addData(bookBeen);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//        });

    }

    @Override
    public void dismissProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show("扫描完成");
            }
        });
    }

    @Override
    public void showFiles(final List<FileEntry> files) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.getData().clear();
                mAdapter.addData(files);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mBookDBManager = null;
        super.onDestroy();
    }
}
