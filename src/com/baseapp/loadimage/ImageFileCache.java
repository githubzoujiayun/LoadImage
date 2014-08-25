package com.baseapp.loadimage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.baseapp.loadimage.DiskLruCache.Snapshot;

/**
 * @ClassName: ImageFileCache
 * @Description: 图片文件缓存
 * @author 杨佳星
 * @date 2014年8月25日 上午9:40:27
 * 
 */
public class ImageFileCache {
    private DiskLruCache diskcache;

    public ImageFileCache(String dir, int appVersion, int maxSize) {
        try {
            diskcache = DiskLruCache.open(new File(dir), appVersion, 1, maxSize);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * key值
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /*
     * 得到文件缓存
     */
    public InputStream getCache(String key) {
        key = hashKeyForDisk(key);
        try {
            Snapshot snap = diskcache.get(key);
            if (snap != null) {
                return snap.getInputStream(0);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DiskLruCache.Editor getEditor(String key) {
        key = hashKeyForDisk(key);
        try {
            DiskLruCache.Editor editor = diskcache.edit(key);
            return editor;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("为什么为空呢！！！");
            return null;
        }
    }

    public void flush() {
        try {
            diskcache.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
