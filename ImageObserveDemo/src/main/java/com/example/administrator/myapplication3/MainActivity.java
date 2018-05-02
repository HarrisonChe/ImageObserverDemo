package com.example.administrator.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private RecyclerView rv;
    private List<String> images=new ArrayList<String>();//图片地址
    private Context mContext;
    private DisplayImageOptions options;
    private MyRecyclerViewAdapter adapter;
    private HashMap<Integer, float[]> xyMap=new HashMap<Integer, float[]>();//所有子项的坐标
    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initView();
        initData();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    /**
     * recyclerView item点击事件
     */
    private void setEvent() {
        adapter.setmOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mContext,SecondActivity.class);
                intent.putStringArrayListExtra("urls", (ArrayList<String>) images);
                intent.putExtra("position", position);
                xyMap.clear();//每一次点击前子项坐标都不一样，所以清空子项坐标

                //子项前置判断，是否在屏幕内，不在的话获取屏幕边缘坐标
                View view0=rv.getChildAt(0);
                int position0=rv.getChildPosition(view0);
                if(position0>0)
                {
                    for(int j=0;j<position0;j++)
                    {
                        float[] xyf=new float[]{(1/6.0f+(j%3)*(1/3.0f))*screenWidth,0};//每行3张图，每张图的中心点横坐标自然是屏幕宽度的1/6,3/6,5/6
                        xyMap.put(j, xyf);
                    }
                }

                //其余子项判断
                for(int i=position0;i<rv.getAdapter().getItemCount();i++)
                {
                    View view1=rv.getChildAt(i-position0);
                    if(rv.getChildPosition(view1)==-1)//子项末尾不在屏幕部分同样赋值屏幕底部边缘
                    {
                        float[] xyf=new float[]{(1/6.0f+(i%3)*(1/3.0f))*screenWidth,screenHeight};
                        xyMap.put(i, xyf);
                    }
                    else
                    {
                        int[] xy = new int[2];
                        view1.getLocationOnScreen(xy);
                        float[] xyf=new float[]{xy[0]*1.0f+view1.getWidth()/2,xy[1]*1.0f+view1.getHeight()/2};
                        xyMap.put(i, xyf);
                    }
                }
                intent.putExtra("xyMap",xyMap);
                startActivity(intent);
            }
        });
    }

    private void initView()
    {
        rv=(RecyclerView)findViewById(R.id.rv);
        GridLayoutManager glm=new GridLayoutManager(mContext,3);//定义3列的网格布局
        rv.setLayoutManager(glm);
        rv.addItemDecoration(new RecyclerViewItemDecoration(20,3));//初始化子项距离和列数
        options=new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(5))
                .build();
        adapter=new MyRecyclerViewAdapter(images,mContext,options,glm);
        rv.setAdapter(adapter);
    }

    /**
     * 初始化网络图片地址，来自百度图片
     */
    private void initData()
    {
        for (int i = 0; i < 5; i++) {
            images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1134803622,956041466&fm=27&gp=0.jpg");
            images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2425251530,3329655954&fm=27&gp=0.jpg");
            images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1108144574,916173858&fm=27&gp=0.jpg");
            images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1764363895,1207146238&fm=27&gp=0.jpg");
            images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3571687204,1985673515&fm=27&gp=0.jpg");
        }
        adapter.notifyDataSetChanged();
    }
    public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration
    {
        private int itemSpace;//定义子项间距
        private int itemColumnNum;//定义子项的列数

        public RecyclerViewItemDecoration(int itemSpace, int itemColumnNum) {
            this.itemSpace = itemSpace;
            this.itemColumnNum = itemColumnNum;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom=itemSpace;//底部留出间距
            if(parent.getChildPosition(view)%itemColumnNum==0)//每行第一项左边不留间距，其他留出间距
            {
                outRect.left=0;
            }
            else
            {
                outRect.left=itemSpace;
            }

        }
    }

    /**
     * 重写startActivity方法，禁用activity默认动画
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0,0);
    }
}
