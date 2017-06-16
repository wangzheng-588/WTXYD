package com.wz.wtxyd.presenter;

import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.FileEntry;
import com.wz.wtxyd.common.model.CatalogModel;
import com.wz.wtxyd.common.rx.subscriber.ErrorHandlerSubscriber;
import com.wz.wtxyd.common.util.SearchBookUtils;
import com.wz.wtxyd.presenter.Contract.CatalogContract;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wz on 17-6-8.
 */

public class CatalogPresenter extends BasePresenter<CatalogModel, CatalogContract.CatalogView> {



    @Inject
    public CatalogPresenter(CatalogModel model, CatalogContract.CatalogView view) {
        super(model, view);
    }

    public void getCatalog(final String path) {


        //final File storageDirectory = Environment.getExternalStorageDirectory();
        final File directory = new File(path);
        String[] list = directory.list();
        List<String> strings = Arrays.asList(list);

        if (strings.size() > 0) {

            Observable.just(strings).flatMap(new Function<List<String>, ObservableSource<List<FileEntry>>>() {
                @Override
                public ObservableSource<List<FileEntry>> apply(@NonNull List<String> strings) throws Exception {


                    File file = null;
                    FileEntry fileEntry = null;
                    final List<FileEntry> fileList = new ArrayList<>();
                    //if (fileList!=null&&fileList.size()>0)
                    for (int i = 0; i < strings.size(); i++) {
                        file = new File(directory, strings.get(i));
                        if (file.exists()) {
                            if (file.isFile()) {
                                fileEntry = new FileEntry(true, file.getPath(), path);
                            } else if (file.isDirectory()) {
                                int length = file.list().length;
                                fileEntry = new FileEntry(false, file.getPath(), path, length);
                            }
                            fileList.add(fileEntry);
                        }
                    }
                    return Observable.create(new ObservableOnSubscribe<List<FileEntry>>() {
                        @Override
                        public void subscribe(ObservableEmitter<List<FileEntry>> e) throws Exception {
                            e.onNext(fileList);
                            e.onComplete();
                        }
                    });
                }

            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorHandlerSubscriber<List<FileEntry>>(mContext) {
                        @Override
                        public void onNext(List<FileEntry> fileEntries) {
                            mView.showCatalog(fileEntries);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mView.showError("此目录没有文件");
        }
    }




    public void searchBook(final String path, final String ext) {
        new Thread(){
            @Override
            public void run() {
                SearchBookUtils searchBookUtils = new SearchBookUtils();
                List<BookBean> book = searchBookUtils.findBook(path, ext);
                mView.showBooks(book);
                mView.dismissProgress();
            }
       }.start();
    }

    public void searchFile(final String path, final String ext) {
        new Thread(){
            @Override
            public void run() {
                SearchBookUtils searchBookUtils = new SearchBookUtils();
                List<FileEntry> files = searchBookUtils.findFile(path, ext);

                mView.showFiles(files);
                mView.dismissProgress();
            }
        }.start();
    }
}
