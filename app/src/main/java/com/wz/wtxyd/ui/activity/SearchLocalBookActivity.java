package com.wz.wtxyd.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by wz on 17-6-8.
 */

public class SearchLocalBookActivity extends BaseActivity implements CatalogContract.CatalogView {
    @BindView(R.id.tv_choice)
    TextView mTvChoice;
    @BindView(R.id.tv_storage_location)
    TextView mTvStorageLocation;
    @BindView(R.id.tv_up_catalog)
    TextView mTvUpCatalog;
    @BindView(R.id.recycler_view_catalog)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_stan)
    Button mBtnScan;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_commit)
    TextView mTvCommit;


    private ArrayList<FileEntry> mFileEntries = new ArrayList<>();
    private int mSelectedNum;
    private CatalogAdapter mAdapter;

    @Inject
    CatalogPresenter mPresenter;

    @Override
    protected void setupAppComponent(AppComponent appComponent) {
        DaggerCatalogComponent.builder().catalogModule(new CatalogModule(this))
                .build().inject(this);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_search_local_book;
    }

    @Override
    protected void init() {


        mTvStorageLocation.setText(Environment.getExternalStorageDirectory().getPath());

        mAdapter = new CatalogAdapter.Builder().showImport(false).build();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        mPresenter.getCatalog(Environment.getExternalStorageDirectory().getPath());

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileEntry item = (FileEntry) adapter.getItem(position);
                if (item.isFile()) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_file);
                    if (checkBox.isChecked()) {
                        item.setCheck(false);
                        checkBox.setChecked(false);

                        if (mSelectedNum > 0) {
                            mSelectedNum--;
                            if (mFileEntries.size() > 0) {
                                mFileEntries.remove(item);
                                if (mSelectedNum == 0) {
                                    mTvChoice.setText("未选择");
                                } else {
                                    mTvChoice.setText("已选择" + mSelectedNum + "个");
                                }
                            }
                        }
                    } else {
                        checkBox.setChecked(true);
                        item.setCheck(true);
                        mSelectedNum++;
                        mTvChoice.setText("已选择" + mSelectedNum + "个");
                        mFileEntries.add(item);
                    }
                } else {
                    File file = new File(item.getPath());
                    String[] list = file.list();
                    if (list.length > 0) {
                        mSelectedNum = 0;
                        mTvChoice.setText("未选择");
                        mTvStorageLocation.setText(item.getPath());
                        mPresenter.getCatalog(item.getPath());
                    } else {
                        ToastUtil.show("此目录没有文件");
                    }
                }
            }
        });


        mTvUpCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mTvStorageLocation.getText().toString();
                if (path.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    return;
                }
                String upPath = path.substring(0, path.lastIndexOf("/"));
                ToastUtil.show(upPath);
                if (!TextUtils.isEmpty(upPath)) {
                    mSelectedNum = 0;
                    mTvChoice.setText("未选择");
                    mPresenter.getCatalog(upPath);

                    mTvStorageLocation.setText(upPath);
                }
            }
        });

        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchLocalBookActivity.this, BookScanResultActivity.class);
                intent.putExtra("path", mTvStorageLocation.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDBManager bookDBManager = new BookDBManager();
                boolean b = bookDBManager.insertBookList(getBooks());
                if (b){
                    ToastUtil.show("导入成功");
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show("导入失败");
                }
            }
        });

    }

    @Override
    public void showError(String message) {
        ToastUtil.show(message);
    }

    @Override
    public void showCatalog(List<FileEntry> list) {
        mAdapter.getData().clear();
        mAdapter.addData(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showBooks(List<BookBean> bookBeen) {

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
    public void showFiles(List<FileEntry> files) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ArrayList<BookBean> bookBeen = getBooks();

            Intent intent = getIntent();
            intent.putExtra("books", bookBeen);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @NonNull
    private ArrayList<BookBean> getBooks() {
        ArrayList<BookBean> mBookBeen = new ArrayList<>();
        BookBean bookBean = null;
        for (int i = 0; i < mFileEntries.size(); i++) {
            File file = new File(mFileEntries.get(i).getPath());
            if (file.exists() && file.isFile()) {
                bookBean = new BookBean();
                bookBean.setPath(file.getPath());
                bookBean.setName(file.getName());
                bookBean.setPathMD5(MD5Util.MD5(file.getPath()));
                bookBean.setSize(file.length() + "");
                String encoding = EncodingUtils.getEncoding(bookBean);
                bookBean.setEncoding(encoding);
                mBookBeen.add(bookBean);
            }
        }
        return mBookBeen;
    }
}
