package com.wz.wtxyd.common.http;

import com.wz.wtxyd.bean.BookCatBaseEntry;
import com.wz.wtxyd.bean.ChapterBaseEntry;
import com.wz.wtxyd.bean.PageBaseEntry;
import com.wz.wtxyd.common.Contrast;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wz on 17-6-6.
 */

public interface ApiService {

    @POST(Contrast.BOOK_TYPE_FEILD)
    Observable<BookCatBaseEntry> getBookCat(@Query("showapi_appid") String appid, @Query("showapi_sign") String sign);


    @POST(Contrast.BOOK_PAGEBEAN_FEILD)
    Observable<PageBaseEntry> getMoreBookTypeDetail(@Query("showapi_appid") String appid, @Query("showapi_sign") String sign, @Query("typeId") int id, @Query("page") int page);

    @POST(Contrast.BOOK_CHAPTER_FEILD)
    Observable<ChapterBaseEntry> getBookChpater(@Query("showapi_appid") String appid, @Query("showapi_sign")String sign, @Query("bookId") int bookid);
}
