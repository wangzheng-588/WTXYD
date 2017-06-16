package com.wz.wtxyd.presenter;

import com.wz.wtxyd.bean.ChapterBaseEntry;
import com.wz.wtxyd.bean.ChpaterBean;
import com.wz.wtxyd.common.model.BookModel;
import com.wz.wtxyd.common.rx.subscriber.ErrorHandlerSubscriber;
import com.wz.wtxyd.common.util.ComparatorChapterSort;
import com.wz.wtxyd.presenter.Contract.BookContract;

import java.util.Collections;
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

public class BookPresenter extends BasePresenter<BookModel,BookContract.View>{


    @Inject
    public BookPresenter(BookModel model, BookContract.View view) {
        super(model, view);
    }

    public void getBookChapterList(int bookid){
        mModel.getChapter(bookid).flatMap(new Function<ChapterBaseEntry, ObservableSource<List<ChpaterBean>>>() {
            @Override
            public ObservableSource<List<ChpaterBean>> apply(@NonNull ChapterBaseEntry chapterBaseEntry) throws Exception {
                if (chapterBaseEntry.getShowapi_res_code()==0){
                    final List<ChpaterBean> chapterList = chapterBaseEntry.getShowapi_res_body().getBook().getChapterList();

                    Collections.sort(chapterList,new ComparatorChapterSort());
                    return Observable.create(new ObservableOnSubscribe<List<ChpaterBean>>() {
                        @Override
                        public void subscribe(ObservableEmitter<List<ChpaterBean>> e) throws Exception {
                            e.onNext(chapterList);
                            e.onComplete();
                        }
                    });
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandlerSubscriber<List<ChpaterBean>>(mContext) {
            @Override
            public void onNext(List<ChpaterBean> chpaterBeen) {
                mView.showChapter(chpaterBeen);
            }

            @Override
            public void onComplete() {
                mView.dismissProgress();
            }
        });
    }

    public void getBookChapterContent(int bookid,int CID){
        mModel.getBookChapterContent(bookid,CID);
    }

    public void searchBook(){
        mModel.searchBook();
    }
}
