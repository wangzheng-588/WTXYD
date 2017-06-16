package com.wz.wtxyd.presenter;

import android.util.Log;

import com.wz.wtxyd.bean.PageBaseEntry;
import com.wz.wtxyd.bean.PageBean;
import com.wz.wtxyd.common.model.BookListDetailModel;
import com.wz.wtxyd.common.rx.subscriber.ErrorHandlerSubscriber;
import com.wz.wtxyd.presenter.Contract.BookListDetailContract;

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

public class BookListDetailPresenter extends BasePresenter<BookListDetailModel, BookListDetailContract.View> {

    @Inject
    public BookListDetailPresenter(BookListDetailModel model, BookListDetailContract.View view) {
        super(model, view);
    }

    public void getMoreBookTypeDetail(int typeID, int page, boolean update, final boolean isLoadMore){
        mModel.getMoreBookTypeDetail(typeID,page,update).flatMap(new Function<PageBaseEntry, ObservableSource<PageBean>>() {
            @Override
            public ObservableSource<PageBean> apply(PageBaseEntry pageBaseEntry) throws Exception {
                if (pageBaseEntry.getShowapi_res_code()==0){
                    final PageBean pagebean = pageBaseEntry.getShowapi_res_body().getPagebean();
                    return Observable.create(new ObservableOnSubscribe<PageBean>() {
                        @Override
                        public void subscribe(ObservableEmitter<PageBean> e) throws Exception {
                            e.onNext(pagebean);
                            Log.e("TAG",pagebean.getContentlist().get(0).getName());
                            e.onComplete();
                        }
                    });
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandlerSubscriber<PageBean>(mContext) {
            @Override
            public void onNext(PageBean value) {
                if (isLoadMore){
                    mView.showMoreBookTypeDetail(value);
                    Log.e("TAG",value.getContentlist().get(0).getName());
                } else {
                    mView.showBookTypeDetail(value);
                    Log.e("TAG",value.getContentlist().get(0).getName());
                }
                mView.dismissProgress();
            }

            @Override
            public void onComplete() {
                mView.dismissProgress();
            }
        });
    }

}
