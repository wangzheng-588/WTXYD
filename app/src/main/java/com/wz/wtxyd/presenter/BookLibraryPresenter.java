package com.wz.wtxyd.presenter;

import com.wz.wtxyd.bean.BookCat;
import com.wz.wtxyd.bean.BookCatBaseEntry;
import com.wz.wtxyd.common.model.BookLibraryModel;
import com.wz.wtxyd.common.rx.subscriber.ErrorHandlerSubscriber;
import com.wz.wtxyd.presenter.Contract.BookLibraryContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wz on 17-6-6.
 */

public class BookLibraryPresenter extends BasePresenter<BookLibraryModel,BookLibraryContract.View> {

    @Inject
    public BookLibraryPresenter(BookLibraryModel model, BookLibraryContract.View view) {
        super(model, view);
    }

    public void getBookCat(){
        mModel.getBookCat().flatMap(new Function<BookCatBaseEntry, ObservableSource<List<BookCat>>>() {
            @Override
            public ObservableSource<List<BookCat>> apply(BookCatBaseEntry bookCatBaseEntry) throws Exception {
                if (bookCatBaseEntry.getShowapi_res_code()==0){
                    final List<BookCat> typeList = bookCatBaseEntry.getShowapi_res_body().getTypeList();
                    return Observable.create(new ObservableOnSubscribe<List<BookCat>>() {
                        @Override
                        public void subscribe(ObservableEmitter<List<BookCat>> e) throws Exception {
                            e.onNext(typeList);
                            e.onComplete();
                        }
                    });
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandlerSubscriber<List<BookCat>>(mContext) {
            @Override
            public void onNext(List<BookCat> value) {
                mView.showBookCat(value);
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
