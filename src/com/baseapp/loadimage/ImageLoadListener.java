package com.baseapp.loadimage;

/**
 * @ClassName: ImageLoadListener
 * @Description:图片下载监听器
 * @author 杨佳星
 * @date 2014年8月25日 下午2:50:54
 * 注意：在添加监听器的时候 不知道为什么第一个Item总是被载入很多次 ？
 * 解答：
 */
public interface ImageLoadListener {
    //图片开始下载
    void onLoadStart();
    //图片结束下载
    void onLoadFinish();

}
