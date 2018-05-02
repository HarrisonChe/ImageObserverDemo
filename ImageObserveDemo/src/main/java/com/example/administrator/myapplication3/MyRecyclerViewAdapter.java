package com.example.administrator.myapplication3;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

    private List<String> images=new ArrayList<String>();//Image资源，内容为图片的网络地址
    private Context mContext;
    private DisplayImageOptions options;//UniversalImageLoad
    private GridLayoutManager glm;
    private OnItemClickListener mOnItemClickListener;

    public MyRecyclerViewAdapter(List<String> images, Context mContext,DisplayImageOptions options,GridLayoutManager glm) {
        this.images = images;
        this.mContext = mContext;
        this.options=options;
        this.glm=glm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.rv_item_layout,null);//加载item布局
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        myViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置图片充满ImageView并自动裁剪居中显示
        ViewGroup.LayoutParams parm = myViewHolder.imageView.getLayoutParams();
        parm.height = glm.getWidth()/glm.getSpanCount()
                - 2*myViewHolder.imageView.getPaddingLeft() - 2*((ViewGroup.MarginLayoutParams)parm).leftMargin;//设置imageView宽高相同
        ImageLoader.getInstance().displayImage(images.get(i),myViewHolder.imageView,options);//网络加载原图
        if(mOnItemClickListener!=null)//传递监听事件
        {
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(myViewHolder.imageView,i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.iv_item);
        }
    }

    /**
     * 对外暴露子项点击事件监听器
     * @param mOnItemClickListener
     */
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener=mOnItemClickListener;
    }

    /**
     * 子项点击接口
     */
    public interface OnItemClickListener
    {
        public void onClick(View view,int position);
    }
}
