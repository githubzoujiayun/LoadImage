package com.baseapp.loadimage;

/**
 * @ClassName: LoaderImageOptions
 * @Description: 初始哈框架类 此类建议在application类中就行初始哈
 * @author 杨佳星
 * @date 2014年8月25日 上午9:50:24
 * 
 */
public class LoaderImageOptions {
    private boolean isdiskcahe;// 是否缓存到文件中
    private String filepath;// 缓存文件路径
    private int memorycahesize;// 内存缓存大小
    private int threadpoolsize;// 线程池大小
    private int threadsize;// 线程个数
    private int diskcachesize;//文件缓存大小

    public int getDiskcachesize() {
        return diskcachesize;
    }

    public void setDiskcachesize(int diskcachesize) {
        this.diskcachesize = diskcachesize;
    }

    public boolean isIsdiskcahe() {
        return isdiskcahe;
    }

    public void setIsdiskcahe(boolean isdiskcahe) {
        this.isdiskcahe = isdiskcahe;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getMemorycahesize() {
        return memorycahesize;
    }

    public void setMemorycahesize(int memorycahesize) {
        this.memorycahesize = memorycahesize;
    }

    public int getThreadpoolsize() {
        return threadpoolsize;
    }

    public void setThreadpoolsize(int threadpoolsize) {
        this.threadpoolsize = threadpoolsize;
    }

    public int getThreadsize() {
        return threadsize;
    }

    public void setThreadsize(int threadsize) {
        this.threadsize = threadsize;
    }

    public LoaderImageOptions(boolean isdiskcahe, String filepath, int memorycahesize,
            int threadpoolsize, int threadsize,int diskcachesize) {
        super();
        this.isdiskcahe = isdiskcahe;
        this.filepath = filepath;
        this.memorycahesize = memorycahesize;
        this.threadpoolsize = threadpoolsize;
        this.threadsize = threadsize;
        this.diskcachesize=diskcachesize;
    }

    public LoaderImageOptions() {
        super();
    }

}
