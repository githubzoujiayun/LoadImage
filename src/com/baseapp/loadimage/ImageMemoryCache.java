package com.baseapp.loadimage;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @ClassName: ImageMemoryCache
 * @Description:图片内存缓存
 * @author 杨佳星
 * @date 2014年8月25日 上午9:40:04
 * 
 */
public class ImageMemoryCache {
    private LruCache<String, Bitmap> memeorycache;

    public ImageMemoryCache(int memorycache) {
        memeorycache = new LruCache<String, Bitmap>(memorycache) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /*
     * 添加缓存
     */
    public void addCache(String url, Bitmap map) {
        if (memeorycache != null) {
            if (!isrepeat(url)) {
                memeorycache.put(url, map);
            }
        }
    }

    /*
     * 判断是否已经向缓存中添加
     */
    private boolean isrepeat(String url) {
        if (memeorycache == null)
            return false;
        if (memeorycache.get(url) == null)
            return false;
        else
            return true;
    }

    /*
     * 移除缓存
     */
    public void removeCache(String url) {
        if (isrepeat(url)) {
            memeorycache.remove(url);
        }
    }

    public Bitmap getBitmap(String url) {
        return memeorycache.get(url);
    }

}
