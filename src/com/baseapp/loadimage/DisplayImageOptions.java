package com.baseapp.loadimage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @ClassName: ImageOptions
 * @Description: 图片显示设置类
 * @author 杨佳星
 * @date 2014年8月25日 上午9:41:25
 * 
 */
public class DisplayImageOptions {
    private boolean isloadanim;// 是否开启载入动画
    private Bitmap defaultmap;// 默认加载图片
    private ImageView.ScaleType imagescaletype;// 图片缩放方式
    private Config bitmapconfig;// 设置图片的编码率
    private AnimType type;// 设置动画模式

    public AnimType getType() {
        return type;
    }

    public void setType(AnimType type) {
        this.type = type;
    }

    public DisplayImageOptions(boolean isloadanim, Bitmap defaultmap, ScaleType imagescaletype,
            Config bitmapconfig, AnimType type) {
        super();
        this.isloadanim = isloadanim;
        this.defaultmap = defaultmap;
        this.imagescaletype = imagescaletype;
        this.bitmapconfig = bitmapconfig;
        this.type = type;
    }

    public boolean isIsloadanim() {
        return isloadanim;
    }

    public void setIsloadanim(boolean isloadanim) {
        this.isloadanim = isloadanim;
    }

    public Bitmap getDefaultmap() {
        return defaultmap;
    }

    public void setDefaultmap(Bitmap defaultmap) {
        this.defaultmap = defaultmap;
    }

    public ImageView.ScaleType getImagescaletype() {
        return imagescaletype;
    }

    public void setImagescaletype(ImageView.ScaleType imagescaletype) {
        this.imagescaletype = imagescaletype;
    }

    public Config getBitmapconfig() {
        return bitmapconfig;
    }

    public void setBitmapconfig(Config bitmapconfig) {
        this.bitmapconfig = bitmapconfig;
    }

    public DisplayImageOptions(boolean isloadanim, Bitmap defaultmap,
            ImageView.ScaleType imagescaletype, Config bitmapconfig) {
        super();
        this.isloadanim = isloadanim;
        this.defaultmap = defaultmap;
        this.imagescaletype = imagescaletype;
        this.bitmapconfig = bitmapconfig;
    }

    public DisplayImageOptions() {
        super();
    }

}
