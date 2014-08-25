package com.baseapp.loadimage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * @ClassName: LoadImage
 * @Description: 异步加载图片的方法类
 * @author 杨佳星
 * @date 2014年8月25日 上午9:31:53 采用三级缓存 1.网络 2.内存 3.文件 优先从内存中读取 其次 文件 最后 网络
 * 
 * 
 * 
 * 
 * 
 */
public class LoadImage {
    private static LoadImage instance;
    private ImageMemoryCache memorycache;
    private ImageFileCache filecache;
    private ExecutorService threadpool;
    private Handler handler;

    private LoadImage() {
        handler = new Handler();
    }

    public static synchronized LoadImage getInstance() {
        if (instance == null)
            instance = new LoadImage();
        return instance;
    }

    /*
     * 显示图片
     */
    public void displayImage(ImageView target, String url, DisplayImageOptions displayImageOptions) {
        target.setScaleType(displayImageOptions.getImagescaletype());
        BitmapTask task = new BitmapTask(target, url, displayImageOptions, null);
        threadpool.execute(task);
    }

    /*
     * 显示图片
     */
    public void displayImage(ImageView target, String url, DisplayImageOptions displayImageOptions,
            ImageLoadListener listener) {
        target.setScaleType(displayImageOptions.getImagescaletype());
        BitmapTask task = new BitmapTask(target, url, displayImageOptions, listener);
        threadpool.execute(task);
    }

    /*
     * 显示图片
     */
    public void displayImage(ImageView target, String url) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions(false, null,
                ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565);
        target.setScaleType(displayImageOptions.getImagescaletype());
        BitmapTask task = new BitmapTask(target, url, displayImageOptions, null);
        threadpool.execute(task);
    }

    /*
     * 初始化
     * 该方法一定要执行 建议在application类中进行初始哈
     */
    public void init(LoaderImageOptions loaderImageOptions) {
        memorycache = new ImageMemoryCache(loaderImageOptions.getMemorycahesize());
        threadpool = Executors.newFixedThreadPool(loaderImageOptions.getThreadpoolsize());
        filecache = new ImageFileCache(loaderImageOptions.getFilepath(), 1,
                loaderImageOptions.getDiskcachesize());
    }

    /*
     * 异步下载图片任务
     */
    private class BitmapTask implements Runnable {
        private ImageView targetview;
        private String url;
        private DisplayImageOptions options;
        private ImageLoadListener listener;

        public BitmapTask(ImageView targetview, String url, DisplayImageOptions options,
                ImageLoadListener listener) {
            super();
            this.targetview = targetview;
            this.url = url;
            this.options = options;
            this.targetview.setScaleType(options.getImagescaletype());
            this.listener = listener;
            if (listener != null)
                listener.onLoadStart();
        }

        @Override
        public void run() {
            dowanload(targetview, url, options, listener);
        }
    }

    /*
     * 下载图片任务
     */
    private void dowanload(final ImageView targetview, final String url,
            final DisplayImageOptions options, ImageLoadListener listener) {
        // 先从内存获取

        final Bitmap map = memorycache.getBitmap(url);
        if (map != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String tag = targetview.getTag().toString();
                    if (tag.equals(url)) {
                        targetview.setImageBitmap(map);
                        if (options.isIsloadanim()) {
                            switch (options.getType()) {
                            case ALPHA:
                                AlphaAnimation anim = new AlphaAnimation(0f, 1.0f);
                                anim.setDuration(500);
                                targetview.startAnimation(anim);
                                break;
                            case ZOOM:
                                ScaleAnimation scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                                scale.setDuration(500);
                                targetview.startAnimation(scale);
                                break;
                            }

                        }
                    }
                }
            });
            return;
        }
        // 从文件获取
        try {
            ByteArrayOutputStream out = getBytes(filecache.getCache(url));
            if (out != null) {
                BitmapFactory.Options moptions = new Options();
                moptions.inPreferredConfig = options.getBitmapconfig();
                moptions.inInputShareable = true;
                moptions.inPurgeable = true;// 设置图片可以被回收
                byte[] bytes = out.toByteArray();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        moptions);
                if (bitmap == null) {

                } else {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            String tag = targetview.getTag().toString();
                            if (tag.equals(url)) {
                                targetview.setImageBitmap(bitmap);
                                if (options.isIsloadanim()) {
                                    switch (options.getType()) {
                                    case ALPHA:
                                        AlphaAnimation anim = new AlphaAnimation(0f, 1.0f);
                                        anim.setDuration(500);
                                        targetview.startAnimation(anim);
                                        break;
                                    case ZOOM:
                                        ScaleAnimation scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                                                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                                                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                                        scale.setDuration(500);
                                        targetview.startAnimation(scale);
                                        break;
                                    }
                                }
                            }

                        }
                    });
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 从网络获取
        getBitmapFromNet(targetview, url, options, listener);
    }

    /*
     * 从网络获取图片
     */
    private void getBitmapFromNet(final ImageView targetview, final String url,
            final DisplayImageOptions options, final ImageLoadListener listener) {
        DiskLruCache.Editor editor = filecache.getEditor(url);
        if (editor != null) {
            OutputStream outputStream;
            try {
                outputStream = editor.newOutputStream(0);
                if (downloadUrlToStream(url, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                filecache.flush();
                // 缓存被写入后，再次查找key对应的缓存
                InputStream in = filecache.getCache(url);
                // 将缓存数据解析成Bitmap对象
                BitmapFactory.Options moptions = new Options();
                moptions.inPreferredConfig = options.getBitmapconfig();
                moptions.inInputShareable = true;
                moptions.inPurgeable = true;// 设置图片可以被回收
                final Bitmap bitmap = BitmapFactory.decodeStream(in, null, moptions);
                if (bitmap != null) {
                    // 将Bitmap对象添加到内存缓存当中
                    memorycache.addCache(url, bitmap);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onLoadFinish();
                        String tag = targetview.getTag().toString();
                        if (tag.equals(url)) {
                            targetview.setImageBitmap(bitmap);
                            if (options.isIsloadanim()) {
                                switch (options.getType()) {
                                case ALPHA:
                                    AlphaAnimation anim = new AlphaAnimation(0f, 1.0f);
                                    anim.setDuration(500);
                                    targetview.startAnimation(anim);
                                    break;
                                case ZOOM:
                                    ScaleAnimation scale = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                                    scale.setDuration(500);
                                    targetview.startAnimation(scale);
                                    break;
                                }
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
     * 流到字节
     */
    private ByteArrayOutputStream getBytes(InputStream in) throws Exception {
        if (in == null)
            return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        in.close();
        bos.flush();
        return bos;
    }

    private void getBytes(InputStream in, OutputStream out) {
        if (in == null)
            return;
        BufferedOutputStream outt = null;
        BufferedInputStream inn = null;
        inn = new BufferedInputStream(in, 8 * 1024);
        outt = new BufferedOutputStream(out, 8 * 1024);
        int b;
        try {
            while ((b = inn.read()) != -1) {
                outt.write(b);
            }
            if (outt != null) {
                outt.close();
            }
            if (inn != null) {
                inn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
