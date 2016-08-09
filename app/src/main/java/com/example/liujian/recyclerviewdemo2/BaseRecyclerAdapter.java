package com.example.liujian.recyclerviewdemo2;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @project_Name: RecyclerViewDemo
 * @package: com.example.liujian.recyclerviewdemo
 * @description: RecyclerView通用适配器
 * @author: liujian
 * @date: 2016/8/1 13:51
 * @version: V1.0
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;//上下文
    private List<T> list;//数据源
    private LayoutInflater inflater;//布局器
    private int itemLayoutId;//布局id
    private OnItemClickListener listener;//点击事件监听器
    private OnItemLongClickListener longClickListener;//长按监听器
    private RecyclerView recyclerView;
    private boolean isCanLoadingMore=false;
    //在RecyclerView提供数据的时候调用
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    /**
     * 定义一个点击事件接口回调
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    /**
     * 插入一项
     *
     * @param item
     * @param position
     */
    public void insert(T item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 删除一项
     *
     * @param position 删除位置
     */
    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public BaseRecyclerAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        if(isCanLoadingMore){
            return list.size() == 0 ? 0 : list.size() + 1;
        }
        return list.size() == 0 ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isCanLoadingMore){
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }else{
            return TYPE_ITEM;
        }

    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
            View view = inflater.inflate(getItemLayoutId(viewType), parent, false);
            return new ItemViewHolder(context, view);
        }else if(viewType==TYPE_FOOTER){
            View view = inflater.inflate(R.layout.item_foot, parent, false);
            return new FootViewHolder(context, view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            if (listener != null){
                holder.itemView.setBackgroundResource(R.drawable.recycler_bg);//设置背景
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && view != null && recyclerView != null) {
                        listener.onItemClick(view, holder.getLayoutPosition());
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener != null && view != null && recyclerView != null) {
                        longClickListener.onItemLongClick(view,holder.getLayoutPosition());
                        return true;
                    }
                    return false;
                }
            });
            if(list.size()>0&&position<list.size()){
                bindDataToView(holder, list.get(position), position);
            }
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    /**
     * 填充RecyclerView适配器的方法，子类需要重写
     *
     * @param holder      ViewHolder
     * @param data        子项
     * @param position    位置
     */
    public abstract void bindDataToView(BaseRecyclerHolder holder, T data, int position);

    /**
     * 得到布局的xml文件
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutId(int viewType);


    public void setCanLoadingMore(boolean mCanLoadingMore) {
        isCanLoadingMore = mCanLoadingMore;
    }

    static class ItemViewHolder extends BaseRecyclerHolder {

        TextView tv;

        public ItemViewHolder(Context context,View view) {
            super(context,view);
            tv = (TextView) view.findViewById(R.id.tv_date);
        }
    }

    static class FootViewHolder extends BaseRecyclerHolder {

        public FootViewHolder(Context context,View view) {
            super(context,view);
        }
    }
}