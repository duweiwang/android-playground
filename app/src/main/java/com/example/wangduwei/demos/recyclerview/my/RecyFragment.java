package com.example.wangduwei.demos.recyclerview.my;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc: RecyclerView缓存原理：
 * mCachedViews基本上是缓存三个的，顶部显示过的两个，底部即将出现的1个
 * @auther:duwei
 * @date:2018/9/3
 */
@PageInfo(description = "RecyclerView缓存", navigationId = R.id.fragment_recyclerview_cache)
public class RecyFragment extends BaseSupportFragment {

    public static final String TAG = "RecyclerViewTag";

    private LogRecyclerView recyclerView;
    private TextView mRecyclerInfo;
    private LogAdapter adapter;
    private LogLayoutManager layoutManager = new LogLayoutManager(getActivity());
    private ItemTouchHelper itemTouchHelper;
    private RecyItemTouchCallback itemTouchCallback;
    private LogItemDecoration itemDecoration = new LogItemDecoration();

    //反射出来的RecyclerView的缓存
    static ArrayList<RecyclerView.ViewHolder> mAttachedScrap;
    static ArrayList<RecyclerView.ViewHolder> mCachedViews;
    static List<RecyclerView.ViewHolder> mUnmodifiableAttachedScrap;
    static RecyclerView.RecycledViewPool mPool;
    SparseArray mScrap;
    RecyclerView.Recycler recycler;
    StringBuilder stringBuilder = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = initAdapter();

        itemTouchCallback = new RecyItemTouchCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchCallback);

        recyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerInfo = view.findViewById(R.id.recyclerView_info);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        injectRecyclerInfo(recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append("RecyclerView缓存信息：\n" +
                        "mAttachedScrap大小 = " + mAttachedScrap.size() + "\n" +
                        "内容 = \n" + getInfo(mAttachedScrap) + "\n\n" +
                        "mCachedViews大小 = " + mCachedViews.size() + "\n" +
                        "内容 = \n" + getInfo(mCachedViews) + "\n\n" +
                        "mUnmodifiableAttachedScrap大小 = " + mUnmodifiableAttachedScrap.size() + "\n" +
                        "内容= \n" + getInfo(mUnmodifiableAttachedScrap) + "\n\n" +
                        "mScrap大小 = \n" + mScrap.size() + "\n" +
                        "内容 = \n" + getPoolContent(mScrap));

                mRecyclerInfo.setText(stringBuilder.toString());
            }
        });
    }

    private String getPoolContent(SparseArray mScrap) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mScrap.size(); i++) {
            Object obj = mScrap.get(i);
            try {
                Field f = obj.getClass().getDeclaredField("mScrapHeap");
                f.setAccessible(true);
                ArrayList<RecyclerView.ViewHolder> mScrapData = (ArrayList<RecyclerView.ViewHolder>) f.get(obj);
                sb.append(getInfo(mScrapData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        reflectAfterShow();
    }

    private String getInfo(List<RecyclerView.ViewHolder> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (RecyclerView.ViewHolder holder : list) {
            if (holder instanceof RecyViewHolder) {
                TextView text = ((RecyViewHolder) holder).textView;
                stringBuilder.append("-" + text.getText() + "\n");
            }
        }
        return stringBuilder.toString();
    }

    private void reflectAfterShow() {
        try {
            Field pool = recycler.getClass().getDeclaredField("mRecyclerPool");
            pool.setAccessible(true);
            mPool = (RecyclerView.RecycledViewPool) pool.get(recycler);

            Class aClass = mPool.getClass();//RecyclerPool
            Field poolScrap = aClass.getDeclaredField("mScrap");//RecyclerPool#mScrap
            poolScrap.setAccessible(true);
            mScrap = (SparseArray) poolScrap.get(mPool);//mScrap

//            Class[] declaredClasses = aClass.getDeclaredClasses();//获取所有内部类
//            Class ScrapData = declaredClasses[0];//ScrapData
//            Constructor cc = ScrapData.getDeclaredConstructor(aClass);//构造函数ScrapData
//            Object obj = cc.newInstance(mPool);
//            cc.setAccessible(true);
//            Field f = ScrapData.getDeclaredField("mScrapHeap");
//            f.setAccessible(true);
//            mScrapData = (ArrayList<RecyclerView.ViewHolder>) f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private String getInfo(SparseArray array){
//        StringBuilder stringBuilder = new StringBuilder();
//
//    }

    private LogAdapter initAdapter() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            arrayList.add(i);
        }
        return new LogAdapter(getActivity(), arrayList);
    }

    /**
     * 反射内部信息
     *
     * @param recyclerView
     */
    private void injectRecyclerInfo(RecyclerView recyclerView) {
        Class clazz = recyclerView.getClass();
        try {
            Field recyclerField = clazz.getSuperclass().getDeclaredField("mRecycler");
            recyclerField.setAccessible(true);
            recycler = (RecyclerView.Recycler) recyclerField.get(recyclerView);

            Field mAttachedScrapField = recycler.getClass().getDeclaredField("mAttachedScrap");
            mAttachedScrapField.setAccessible(true);
            mAttachedScrap = (ArrayList<RecyclerView.ViewHolder>) mAttachedScrapField.get(recycler);

            Field mCachedViewsField = recycler.getClass().getDeclaredField("mCachedViews");
            mCachedViewsField.setAccessible(true);
            mCachedViews = (ArrayList<RecyclerView.ViewHolder>) mCachedViewsField.get(recycler);

            Field mUnmodifiableAttachedScrapField = recycler.getClass().getDeclaredField("mUnmodifiableAttachedScrap");
            mUnmodifiableAttachedScrapField.setAccessible(true);
            mUnmodifiableAttachedScrap = (List<RecyclerView.ViewHolder>) mUnmodifiableAttachedScrapField.get(recycler);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
