package com.wz.wtxyd.bean;

/**
 * Created by wz on 17-6-8.
 */

public class ChapterBaseEntry {

    private int showapi_res_code;
    private String showapi_res_error;
    private ChapterBodyEntry showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ChapterBodyEntry getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ChapterBodyEntry showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
