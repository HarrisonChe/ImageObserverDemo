package com.example.administrator.myapplication3;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by cbz on 2018/4/28 0028.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();//初始化ImageLoader
    }
    private void initImageLoader()
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(R.mipmap.ic_launcher)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiscCache(new File(Environment.getExternalStorageDirectory()+File.separator+"ImageObserverDemo"+File.separator+"Image_cache")))
                .defaultDisplayImageOptions(options). // 上面的options对象，一些属性配置
                build();
        ImageLoader.getInstance().init(config); // 初始化
    }
}
