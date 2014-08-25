package com.example.loadimage;

import com.baseapp.loadimage.AnimType;
import com.baseapp.loadimage.DisplayImageOptions;
import com.baseapp.loadimage.ImageLoadListener;
import com.baseapp.loadimage.LoadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
    private String[] urls;
    private Context context;
    private LayoutInflater inflater;
    private DisplayImageOptions options;

    public GridViewAdapter(String[] urls, Context context) {
        this.urls = urls;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions(true, BitmapFactory.decodeResource(
                context.getResources(), R.drawable.fdd), ImageView.ScaleType.FIT_XY,
                Bitmap.Config.RGB_565,AnimType.ZOOM);
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return urls[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_iv, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setTag(urls[position]);
        LoadImage.getInstance().displayImage(holder.iv, urls[position], options,new ImageLoadListener() {
            
            @Override
            public void onLoadStart() {
              System.out.println(position+"开始");                
            }
            
            @Override
            public void onLoadFinish() {
                System.out.println(position+"结束"); 
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
    }

}
