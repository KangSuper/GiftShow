package com.xiekang.king.gift.utils;

import android.graphics.Bitmap;

/**
 * Created by King on 2016/8/11.
 * 回调接口
 */
public interface ICallBack {

    /**
     * 请求json数据成功时，回调的接口方法
     * @param result
     * @param requestCode
     */
    void successJson(String result, int requestCode);

    /**
     * 请求bitmap成功时回调的接口
     * @param bitmap
     * @param requestCode
     */
    void successBitmap(Bitmap bitmap, int requestCode);

}
