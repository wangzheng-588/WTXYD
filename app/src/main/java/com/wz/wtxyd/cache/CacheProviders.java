package com.wz.wtxyd.cache;

import com.wz.wtxyd.bean.BookCatBaseEntry;
import com.wz.wtxyd.bean.ChapterBaseEntry;
import com.wz.wtxyd.bean.PageBaseEntry;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.LifeCache;

/**
 * Created by wz on 17-6-7.
 */

public interface CacheProviders {

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<BookCatBaseEntry> getBookCat(Observable<BookCatBaseEntry> BookCatBaseEntry);

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<PageBaseEntry> getBookList(Observable<PageBaseEntry> PageBaseEntry, DynamicKeyGroup typeID);

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<ChapterBaseEntry> getBookChpater( Observable<ChapterBaseEntry> ChapterBaseEntry, DynamicKey key);

}
